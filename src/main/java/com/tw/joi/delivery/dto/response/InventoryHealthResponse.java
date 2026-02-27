package com.tw.joi.delivery.dto.response;

import com.tw.joi.delivery.domain.StoreHealthStatus;

import java.util.List;

public record InventoryHealthResponse(String storeId,
                                      String storeName,
                                      String description,
                                      int totalProducts,
                                      int healthy,
                                      int lowStock,
                                      int outOfStore,
                                      StoreHealthStatus overallStatus,
                                      List<ProductInventoryHealth> products) {

}
