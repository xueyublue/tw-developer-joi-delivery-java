package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.domain.ProductHealthStatus;
import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import com.tw.joi.delivery.repository.GroceryProductRepository;
import com.tw.joi.delivery.repository.GroceryStoreRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final GroceryProductRepository productRepository;
    private final GroceryStoreRepository storeRepository;

    public InventoryHealthResponse fetchInventoryHealth(String storeId, boolean includeProducts) {
        GroceryStore store = storeRepository.findById(storeId)
            .orElseThrow(() -> new StoreNotFoundException(storeId));

        List<GroceryProduct> storeProducts = productRepository.findByStoreId(storeId);

        var productHealthList = storeProducts.stream()
            .map(this::toProductInventoryHealth)
            .collect(Collectors.toList());

        int totalProducts = productHealthList.size();
        long healthyCount = productHealthList.stream()
            .filter(p -> p.status() == ProductHealthStatus.HEALTHY)
            .count();
        long lowStockCount = productHealthList.stream()
            .filter(p -> p.status() == ProductHealthStatus.LOW_STOCK)
            .count();
        long outOfStockCount = productHealthList.stream()
            .filter(p -> p.status() == ProductHealthStatus.OUT_OF_STOCK)
            .count();

        StoreHealthStatus overallStatus =
            determineOverallStatus(totalProducts, (int) healthyCount,
                                   (int) lowStockCount,
                                   (int) outOfStockCount);

        return new InventoryHealthResponse(
            storeId,
            store.getName(),
            totalProducts,
            (int) healthyCount,
            (int) lowStockCount,
            (int) outOfStockCount,
            overallStatus,
            includeProducts ? productHealthList : List.of()
        );
    }

    private ProductInventoryHealth toProductInventoryHealth(GroceryProduct product) {
        ProductHealthStatus status;
        if (product.getAvailableStock() <= 0) {
            status = ProductHealthStatus.OUT_OF_STOCK;
        } else if (product.getAvailableStock() <= product.getThreshold()) {
            status = ProductHealthStatus.LOW_STOCK;
        } else {
            status = ProductHealthStatus.HEALTHY;
        }

        return new ProductInventoryHealth(
            product.getProductId(),
            product.getProductName(),
            product.getThreshold(),
            product.getAvailableStock(),
            status
        );
    }

    private StoreHealthStatus determineOverallStatus(int totalProducts, int healthy,
                                                     int lowStock,
                                                     int outOfStock) {
        if (totalProducts == 0) {
            return StoreHealthStatus.NO_INVENTORY;
        }
        if (outOfStock > 0 || lowStock > 0) {
            return StoreHealthStatus.AT_RISK;
        }
        return StoreHealthStatus.HEALTHY;
    }
}

