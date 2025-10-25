// src/main/java/com/ceos22/cgv_clone/external/payment/PaymentRecordRepository.java
package com.ceos22.cgv_clone.external.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<PaymentRecord> findByPaymentId(String paymentId);
    Optional<PaymentRecord> findTopByTypeAndRefIdOrderByIdDesc(PaymentTarget type, Long refId);

    List<PaymentRecord> findByTypeAndStatusAndCreatedAtBefore(
            PaymentTarget type,
            PaymentStatus status,
            LocalDateTime before
    );
}
