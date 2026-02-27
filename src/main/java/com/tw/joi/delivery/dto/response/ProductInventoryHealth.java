package com.tw.joi.delivery.dto.response;

import com.tw.joi.delivery.domain.ProductStatusHealth;

public record ProductInventoryHealth(String productId,
                                     String productName,
                                     int threshold,
                                     int availStock,
                                     ProductStatusHealth status) {
}
