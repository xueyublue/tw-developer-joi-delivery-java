package com.tw.joi.delivery.service.inventory;

import com.tw.joi.delivery.domain.InventoryCapable;
import com.tw.joi.delivery.domain.Outlet;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import java.util.List;

/**
 * Strategy interface for handling inventory operations for different store types.
 * Each store type (GroceryStore, Restaurant, etc.) should have its own handler implementation.
 */
public interface StoreInventoryHandler {
    
    /**
     * Checks if this handler supports the given outlet/store type.
     * 
     * @param outlet the outlet to check
     * @return true if this handler can process inventory for this outlet type
     */
    boolean supports(Outlet outlet);
    
    /**
     * Retrieves all inventory-capable products for the given store.
     * 
     * @param storeId the store/outlet ID
     * @return list of products that support inventory tracking
     */
    List<? extends InventoryCapable> getProducts(String storeId);
    
    /**
     * Converts an inventory-capable product to ProductInventoryHealth DTO.
     * 
     * @param product the product to convert
     * @return ProductInventoryHealth DTO
     */
    ProductInventoryHealth toProductInventoryHealth(InventoryCapable product);
}
