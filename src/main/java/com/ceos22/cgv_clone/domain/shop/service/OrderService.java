package com.ceos22.cgv_clone.domain.shop.service;

import com.ceos22.cgv_clone.domain.shop.repository.OrderItemRepository;
import com.ceos22.cgv_clone.domain.shop.repository.OrderRepository;
import com.ceos22.cgv_clone.domain.shop.repository.StockRepository;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.shop.entity.Order;
import com.ceos22.cgv_clone.domain.shop.entity.OrderItem;
import com.ceos22.cgv_clone.domain.shop.entity.Stock;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import com.ceos22.cgv_clone.domain.user.repository.UserRepository;
import com.ceos22.cgv_clone.domain.shop.dto.request.OrderItemRequestDto;
import com.ceos22.cgv_clone.domain.shop.dto.request.OrderRequestDto;
import com.ceos22.cgv_clone.domain.shop.dto.response.OrderItemResponseDto;
import com.ceos22.cgv_clone.domain.shop.dto.response.OrderResponseDto;
import com.ceos22.cgv_clone.global.redis.RedissonLockHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final StockRepository stockRepository;

    private final RedissonLockHelper lockHelper;

    private String stockKey(Long theaterId, Long productId) {
        return "snack:%d:%d".formatted(theaterId, productId);
    }

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(String username, OrderRequestDto request) {

        int itemCount = request.getItems() == null ? 0 : request.getItems().size();
        log.debug("[SVC] createOrder start - user={}, theaterId={}, items={}",
                username, request.getTheaterId(), itemCount);

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            Theater theater = theaterRepository.findById(request.getTheaterId())
                    .orElseThrow(() -> new IllegalArgumentException("매점 없음"));

            // 이번 주문의 재고 키들을 수집
            List<String> keys = request.getItems().stream()
                    .map(i -> stockKey(request.getTheaterId(), i.getProductId()))
                    .distinct()
                    .toList();

            // 잠금(lock) 실행
            return lockHelper.withLocks(keys, 2000, 5000, () -> {
                Order order = Order.builder()
                        .user(user)
                        .theater(theater)
                        .totalPrice(0)
                        .build();
                ordersRepository.save(order);

                int totalPrice = 0;
                List<OrderItemResponseDto> itemDtos = new ArrayList<>();

                for (OrderItemRequestDto item : request.getItems()) {
                    Stock stock = stockRepository
                            .findByProduct_IdAndTheater_Id(item.getProductId(), request.getTheaterId())
                            .orElseThrow(() -> new IllegalArgumentException("재고 없음"));

                    if (stock.getStock() < item.getQuantity()) {
                        throw new IllegalStateException("재고 부족");
                    }

                    // 재고 차감(차감 로직 분리할것!)
                    stock.setStock(stock.getStock() - item.getQuantity());

                    // 주문 상품 생성/저장
                    OrderItem orderItem = OrderItem.builder()
                            .order(order)
                            .product(stock.getProduct())
                            .quantity(item.getQuantity())
                            .price(stock.getProduct().getPrice() * item.getQuantity())
                            .build();
                    orderItemRepository.save(orderItem);

                    totalPrice += orderItem.getPrice();
                    itemDtos.add(OrderItemResponseDto.fromEntity(orderItem));
                }

                order.setTotalPrice(totalPrice);

                log.info("[SVC] 주문 생성 완료 - user={}, orderId={}, theaterId={}, items={}, totalPrice={}",
                        username, order.getId(), request.getTheaterId(), itemDtos.size(), totalPrice);

                return OrderResponseDto.fromEntity(order, itemDtos);
            });
        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(주문 생성) - user={}, theaterId={}, msg={}",
                    username, request.getTheaterId(), e.getMessage());
            throw e;
        }
        catch (IllegalStateException e) {
            log.warn("[SVC] 비정상 상태(주문 생성) - user={}, theaterId={}, msg={}",
                    username, request.getTheaterId(), e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 주문 생성 실패 - user={}, theaterId={}", username, request.getTheaterId(), e);
            throw e;
        }
    }

    //주문 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(String username, Long orderId) {
        log.debug("[SVC] getOrder start - user={}, orderId={}", username, orderId);
        try {
            Order order = ordersRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

            if (!order.getUser().getUsername().equals(username)) {
                throw new SecurityException("본인의 주문만 조회할 수 있습니다.");
            }

            List<OrderItemResponseDto> items = orderItemRepository.findByOrder_Id(orderId)
                    .stream()
                    .map(OrderItemResponseDto::fromEntity)
                    .toList();

            log.info("[SVC] 주문 조회 완료 - user={}, orderId={}, items={}, totalPrice={}",
                    username, orderId, items.size(), order.getTotalPrice());

            return OrderResponseDto.fromEntity(order, items);

        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(주문 조회) - user={}, orderId={}, msg={}", username, orderId, e.getMessage());
            throw e;
        }
        catch (SecurityException e) {
            log.warn("[SVC] 권한 오류(주문 조회) - user={}, orderId={}, msg={}", username, orderId, e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 주문 조회 실패 - user={}, orderId={}", username, orderId, e);
            throw e;
        }
    }
}
