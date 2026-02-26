package com.tw.joi.delivery.dto.response;

public record ProductInventoryHealth(
    String productId,
    String productName,
    int threshold,
    int availableStock,
    String status
) {
}

