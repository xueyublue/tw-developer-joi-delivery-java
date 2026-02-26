package com.tw.joi.delivery.dto.response;

import com.tw.joi.delivery.domain.ProductHealthStatus;

public record ProductInventoryHealth(
    String productId,
    String productName,
    int threshold,
    int availableStock,
    ProductHealthStatus status
) {
}


