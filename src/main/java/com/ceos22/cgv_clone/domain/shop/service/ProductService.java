package com.ceos22.cgv_clone.domain.shop.service;

import com.ceos22.cgv_clone.domain.shop.entity.Product;
import com.ceos22.cgv_clone.domain.shop.dto.response.ProductResponseDto;
import com.ceos22.cgv_clone.domain.shop.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품 없음"));

        return ProductResponseDto.fromEntity(product);
    }
}
