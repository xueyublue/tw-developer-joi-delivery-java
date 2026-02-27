package com.tw.joi.delivery.service.inventory;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.domain.InventoryCapable;
import com.tw.joi.delivery.domain.Outlet;
import com.tw.joi.delivery.domain.ProductHealthStatus;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import com.tw.joi.delivery.repository.GroceryProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Handler for inventory operations specific to GroceryStore.
 */
@Component
@RequiredArgsConstructor
public class GroceryStoreInventoryHandler implements StoreInventoryHandler {

    private final GroceryProductRepository productRepository;

    @Override
    public boolean supports(Outlet outlet) {
        return outlet instanceof GroceryStore;
    }

    @Override
    public List<? extends InventoryCapable> getProducts(String storeId) {
        return productRepository.findByStoreId(storeId);
    }

    @Override
    public ProductInventoryHealth toProductInventoryHealth(InventoryCapable product) {
        if (!(product instanceof GroceryProduct)) {
            throw new IllegalArgumentException(
                "GroceryStoreInventoryHandler can only process GroceryProduct instances");
        }
        
        GroceryProduct groceryProduct = (GroceryProduct) product;
        ProductHealthStatus status = determineProductHealthStatus(
            groceryProduct.getAvailableStock(), 
            groceryProduct.getThreshold()
        );

        return new ProductInventoryHealth(
            groceryProduct.getProductId(),
            groceryProduct.getProductName(),
            groceryProduct.getThreshold(),
            groceryProduct.getAvailableStock(),
            status
        );
    }

    private ProductHealthStatus determineProductHealthStatus(int availableStock, int threshold) {
        if (availableStock <= 0) {
            return ProductHealthStatus.OUT_OF_STOCK;
        } else if (availableStock <= threshold) {
            return ProductHealthStatus.LOW_STOCK;
        } else {
            return ProductHealthStatus.HEALTHY;
        }
    }
}
