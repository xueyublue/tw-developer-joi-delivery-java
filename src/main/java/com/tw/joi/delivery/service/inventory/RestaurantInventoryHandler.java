package com.tw.joi.delivery.service.inventory;

import com.tw.joi.delivery.domain.InventoryCapable;
import com.tw.joi.delivery.domain.Outlet;
import com.tw.joi.delivery.domain.ProductHealthStatus;
import com.tw.joi.delivery.domain.Restaurant;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Handler for inventory operations specific to Restaurant.
 * 
 * Note: Restaurants typically don't track inventory the same way as grocery stores.
 * This handler returns empty inventory as restaurants usually manage availability differently
 * (e.g., menu items availability vs. stock levels).
 * 
 * If restaurants need inventory tracking in the future, FoodProduct can be extended
 * to implement InventoryCapable, and this handler can be updated accordingly.
 */
@Component
public class RestaurantInventoryHandler implements StoreInventoryHandler {

    @Override
    public boolean supports(Outlet outlet) {
        return outlet instanceof Restaurant;
    }

    @Override
    public List<? extends InventoryCapable> getProducts(String storeId) {
        // Restaurants don't currently track inventory like grocery stores
        // Return empty list - can be extended when FoodProduct implements InventoryCapable
        return Collections.emptyList();
    }

    @Override
    public ProductInventoryHealth toProductInventoryHealth(InventoryCapable product) {
        // This method won't be called if getProducts returns empty list
        // But if needed in the future, implement conversion logic here
        throw new UnsupportedOperationException(
            "Restaurant inventory tracking not yet implemented");
    }
}
