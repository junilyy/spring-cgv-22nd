package com.ceos22.cgv_clone.external.payment.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequest {
    private String storeId;
    private String orderName;
    private int totalPayAmount;
    private String currency;   // "KRW"
    private String customData; // JSON string(optional)
}
