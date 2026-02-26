package com.tw.joi.delivery.dto.response;

import com.tw.joi.delivery.domain.StoreHealthStatus;
import java.util.List;

public record InventoryHealthResponse(
    String storeId,
    String storeName,
    int totalProducts,
    int healthy,
    int lowStock,
    int outOfStock,
    StoreHealthStatus overallStatus,
    int page,
    int size,
    int totalPages,
    List<ProductInventoryHealth> products
) {
}

