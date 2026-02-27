package com.tw.joi.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.domain.InventoryCapable;
import com.tw.joi.delivery.domain.ProductHealthStatus;
import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import com.tw.joi.delivery.repository.OutletRepository;
import com.tw.joi.delivery.service.inventory.GroceryStoreInventoryHandler;
import com.tw.joi.delivery.service.inventory.StoreInventoryHandlerFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InventoryServiceTest {

    private OutletRepository outletRepository;
    private StoreInventoryHandlerFactory handlerFactory;
    private GroceryStoreInventoryHandler groceryHandler;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        outletRepository = Mockito.mock(OutletRepository.class);
        handlerFactory = Mockito.mock(StoreInventoryHandlerFactory.class);
        groceryHandler = Mockito.mock(GroceryStoreInventoryHandler.class);
        inventoryService = new InventoryService(outletRepository, handlerFactory);
    }

    @Test
    void shouldReturnHealthyInventoryWhenAllProductsAreHealthy() {
        String storeId = "store101";
        GroceryStore store = GroceryStore.builder()
            .name("Fresh Picks")
            .outletId(storeId)
            .build();

        GroceryProduct product = GroceryProduct.builder()
            .productId("product101")
            .productName("Wheat Bread")
            .mrp(BigDecimal.TEN)
            .threshold(10)
            .availableStock(30)
            .store(store)
            .build();

        ProductInventoryHealth productHealth = new ProductInventoryHealth(
            "product101",
            "Wheat Bread",
            10,
            30,
            ProductHealthStatus.HEALTHY
        );

        when(outletRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(handlerFactory.getHandler(store)).thenReturn(groceryHandler);
        doReturn(List.of(product)).when(groceryHandler).getProducts(storeId);
        when(groceryHandler.toProductInventoryHealth(product)).thenReturn(productHealth);

        InventoryHealthResponse response = inventoryService.fetchInventoryHealth(storeId, true);

        assertThat(response.storeId()).isEqualTo(storeId);
        assertThat(response.storeName()).isEqualTo("Fresh Picks");
        assertThat(response.totalProducts()).isEqualTo(1);
        assertThat(response.healthy()).isEqualTo(1);
        assertThat(response.lowStock()).isZero();
        assertThat(response.outOfStock()).isZero();
        assertThat(response.overallStatus()).isEqualTo(StoreHealthStatus.HEALTHY);
        assertThat(response.products()).hasSize(1);
        assertThat(response.products().get(0).status())
            .isEqualTo(ProductHealthStatus.HEALTHY);
    }

    @Test
    void shouldClassifyStoreAsAtRiskWhenAnyProductIsLowOrOutOfStock() {
        String storeId = "store101";
        GroceryStore store = GroceryStore.builder()
            .name("Fresh Picks")
            .outletId(storeId)
            .build();

        GroceryProduct lowStockProduct = GroceryProduct.builder()
            .productId("productLow")
            .productName("Low Stock Item")
            .mrp(BigDecimal.TEN)
            .threshold(10)
            .availableStock(5)
            .store(store)
            .build();

        GroceryProduct outOfStockProduct = GroceryProduct.builder()
            .productId("productOut")
            .productName("Out Of Stock Item")
            .mrp(BigDecimal.TEN)
            .threshold(10)
            .availableStock(0)
            .store(store)
            .build();

        ProductInventoryHealth lowStockHealth = new ProductInventoryHealth(
            "productLow",
            "Low Stock Item",
            10,
            5,
            ProductHealthStatus.LOW_STOCK
        );

        ProductInventoryHealth outOfStockHealth = new ProductInventoryHealth(
            "productOut",
            "Out Of Stock Item",
            10,
            0,
            ProductHealthStatus.OUT_OF_STOCK
        );

        when(outletRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(handlerFactory.getHandler(store)).thenReturn(groceryHandler);
        doReturn(List.of(lowStockProduct, outOfStockProduct)).when(groceryHandler).getProducts(storeId);
        when(groceryHandler.toProductInventoryHealth(lowStockProduct)).thenReturn(lowStockHealth);
        when(groceryHandler.toProductInventoryHealth(outOfStockProduct)).thenReturn(outOfStockHealth);

        InventoryHealthResponse response = inventoryService.fetchInventoryHealth(storeId, true);

        assertThat(response.totalProducts()).isEqualTo(2);
        assertThat(response.lowStock()).isEqualTo(1);
        assertThat(response.outOfStock()).isEqualTo(1);
        assertThat(response.overallStatus()).isEqualTo(StoreHealthStatus.AT_RISK);
    }

    @Test
    void shouldThrowStoreNotFoundExceptionWhenStoreDoesNotExist() {
        String unknownStoreId = "unknown-store";
        when(outletRepository.findById(unknownStoreId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.fetchInventoryHealth(unknownStoreId, true))
            .isInstanceOf(StoreNotFoundException.class)
            .hasMessageContaining(unknownStoreId);
    }

    @Test
    void shouldAllowSummaryWithoutProductDetails() {
        String storeId = "store101";
        GroceryStore store = GroceryStore.builder()
            .name("Fresh Picks")
            .outletId(storeId)
            .build();

        when(outletRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(handlerFactory.getHandler(store)).thenReturn(groceryHandler);
        doReturn(List.<InventoryCapable>of()).when(groceryHandler).getProducts(storeId);

        InventoryHealthResponse response = inventoryService.fetchInventoryHealth(storeId, false);

        assertThat(response.products()).isEmpty();
        assertThat(response.overallStatus()).isEqualTo(StoreHealthStatus.NO_INVENTORY);
    }
}

