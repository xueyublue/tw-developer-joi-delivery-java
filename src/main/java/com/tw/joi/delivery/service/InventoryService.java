package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.InventoryCapable;
import com.tw.joi.delivery.domain.Outlet;
import com.tw.joi.delivery.domain.ProductHealthStatus;
import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import com.tw.joi.delivery.repository.OutletRepository;
import com.tw.joi.delivery.service.inventory.StoreInventoryHandler;
import com.tw.joi.delivery.service.inventory.StoreInventoryHandlerFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for managing inventory health across different store types.
 * Supports multiple store types (GroceryStore, Restaurant, etc.) through the Strategy pattern.
 */
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final OutletRepository outletRepository;
    private final StoreInventoryHandlerFactory handlerFactory;

    /**
     * Fetches inventory health for a store/outlet of any type.
     * 
     * @param storeId the store/outlet ID
     * @param includeProducts whether to include product details in the response
     * @return InventoryHealthResponse containing health metrics
     * @throws StoreNotFoundException if the store is not found or not supported
     */
    public InventoryHealthResponse fetchInventoryHealth(String storeId, boolean includeProducts) {
        Outlet outlet = outletRepository.findById(storeId)
            .orElseThrow(() -> new StoreNotFoundException(storeId));

        StoreInventoryHandler handler = handlerFactory.getHandler(outlet);
        List<? extends InventoryCapable> products = handler.getProducts(storeId);

        var productHealthList = products.stream()
            .map(handler::toProductInventoryHealth)
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
            outlet.getName(),
            totalProducts,
            (int) healthyCount,
            (int) lowStockCount,
            (int) outOfStockCount,
            overallStatus,
            includeProducts ? productHealthList : List.of()
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

