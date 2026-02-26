package com.tw.joi.delivery.dto.response;

import java.math.BigDecimal;

public record CartSummaryResponse(
    String userId,
    String cartId,
    int totalItems,
    BigDecimal totalMrp,
    BigDecimal totalPrice,
    BigDecimal totalSavings
) {
}

