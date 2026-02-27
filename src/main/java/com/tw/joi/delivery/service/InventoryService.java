package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.Outlet;
import com.tw.joi.delivery.domain.ProductStatusHealth;
import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import com.tw.joi.delivery.exception.StoreNotFoundException;
import com.tw.joi.delivery.repository.GroceryProductRepository;
import com.tw.joi.delivery.repository.GroceryStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final GroceryStoreRepository storeRepository;
    private final GroceryProductRepository productRepository;

    public InventoryHealthResponse fetchInventoryHealth(String storeId) {
        // find the store first
        Outlet store = storeRepository.findByStoreId(storeId).orElseThrow(() -> new StoreNotFoundException(storeId));

        // find the products
        List<GroceryProduct> storeProducts = productRepository.findByStoreId(storeId);

        // determine product health
        List<ProductInventoryHealth> productHealthList = storeProducts.stream()
                .map(this::determineProductInventoryHealth)
                .toList();

        // determine store health
        int totalProducts = storeProducts.size();
        long healthy = productHealthList.stream()
                .filter(item -> item.status() == ProductStatusHealth.HEALTH)
                .count();
        long lowStock = productHealthList.stream()
                .filter(item -> item.status() == ProductStatusHealth.LOW_STOCK)
                .count();
        long outOfStore = productHealthList.stream()
                .filter(item -> item.status() == ProductStatusHealth.OUT_OF_STOCK)
                .count();

        StoreHealthStatus overallStatus = determineOverallStatus(totalProducts, (int) lowStock, (int) outOfStore);

        return new InventoryHealthResponse(
                store.getOutletId(),
                store.getName(),
                store.getDescription(),
                totalProducts,
                (int) healthy,
                (int) lowStock,
                (int) outOfStore,
                overallStatus,
                productHealthList
        );
    }

    private ProductInventoryHealth determineProductInventoryHealth(GroceryProduct product) {
        ProductStatusHealth status;

        int avail = product.getAvailableStock();
        int threshold = product.getThreshold();

        if (avail <= 0) {
            status = ProductStatusHealth.OUT_OF_STOCK;
        } else if (avail < threshold) {
            status = ProductStatusHealth.LOW_STOCK;
        } else {
            status = ProductStatusHealth.HEALTH;
        }

        return new ProductInventoryHealth(
                product.getProductId(),
                product.getProductName(),
                product.getThreshold(),
                product.getAvailableStock(),
                status
        );
    }

    private StoreHealthStatus determineOverallStatus(int totalProducts,
                                                     int lowStock,
                                                     int outOfStock) {
        if (totalProducts == 0) {
            return StoreHealthStatus.NO_INVENTORY;
        } else if (outOfStock > 0 || lowStock > 0) {
            return StoreHealthStatus.AT_RISK;
        }
        return StoreHealthStatus.HEALTHY;
    }

}
