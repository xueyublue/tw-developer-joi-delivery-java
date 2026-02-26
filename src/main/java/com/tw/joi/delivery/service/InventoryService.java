package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import com.tw.joi.delivery.seedData.SeedData;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final List<GroceryProduct> products = SeedData.groceryProducts;

    public InventoryHealthResponse fetchInventoryHealth(String storeId) {
        // find all products for the store
        List<GroceryProduct> storeProducts = products.stream()
                .filter(product -> product.getStore() != null
                        && Objects.equals(product.getStore().getOutletId(), storeId))
                .toList();

        // find the store
        GroceryStore store = storeProducts.stream()
                .map(GroceryProduct::getStore)
                .findFirst()
                .orElse(null);

        // check product health and convert to DTO
        var productHealthList = storeProducts.stream()
                .map(this::toProductInventoryHealth)
                .collect(Collectors.toList());

        // determine store health
        int totalProducts = productHealthList.size();
        long healthyCount = productHealthList.stream()
                .filter(p -> "HEALTHY".equals(p.status()))
                .count();
        long lowStockCount = productHealthList.stream()
                .filter(p -> "LOW_STOCK".equals(p.status()))
                .count();
        long outOfStockCount = productHealthList.stream()
                .filter(p -> "OUT_OF_STOCK".equals(p.status()))
                .count();

        // determine overall status
        String overallStatus = determineOverallStatus(totalProducts, (int) healthyCount,
                (int) lowStockCount,
                (int) outOfStockCount);

        // return result
        return new InventoryHealthResponse(
                storeId,
                store != null ? store.getName() : null,
                totalProducts,
                (int) healthyCount,
                (int) lowStockCount,
                (int) outOfStockCount,
                overallStatus,
                productHealthList
        );
    }

    private ProductInventoryHealth toProductInventoryHealth(GroceryProduct product) {
        String status;
        if (product.getAvailableStock() <= 0) {
            status = "OUT_OF_STOCK";
        } else if (product.getAvailableStock() <= product.getThreshold()) {
            status = "LOW_STOCK";
        } else {
            status = "HEALTHY";
        }

        return new ProductInventoryHealth(
                product.getProductId(),
                product.getProductName(),
                product.getThreshold(),
                product.getAvailableStock(),
                status
        );
    }

    private String determineOverallStatus(int totalProducts, int healthy, int lowStock,
                                          int outOfStock) {
        if (totalProducts == 0) {
            return "NO_INVENTORY";
        }
        if (outOfStock > 0 || lowStock > 0) {
            return "AT_RISK";
        }
        return "HEALTHY";
    }
}

