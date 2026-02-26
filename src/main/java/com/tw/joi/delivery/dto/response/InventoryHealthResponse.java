package com.tw.joi.delivery.dto.response;

import java.util.List;

public record InventoryHealthResponse(
    String storeId,
    String storeName,
    int totalProducts,
    int healthy,
    int lowStock,
    int outOfStock,
    String overallStatus,
    List<ProductInventoryHealth> products
) {
}

