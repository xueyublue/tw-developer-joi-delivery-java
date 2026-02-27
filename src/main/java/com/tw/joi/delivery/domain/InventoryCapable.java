package com.tw.joi.delivery.domain;

/**
 * Interface for products that support inventory tracking.
 * Products implementing this interface can be used in inventory health checks.
 */
public interface InventoryCapable {
    int getThreshold();
    int getAvailableStock();
    String getProductId();
    String getProductName();
}
