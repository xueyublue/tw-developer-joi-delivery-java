package com.tw.joi.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.domain.ProductHealthStatus;
import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.repository.GroceryProductRepository;
import com.tw.joi.delivery.repository.GroceryStoreRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InventoryServiceTest {

    private GroceryProductRepository productRepository;
    private GroceryStoreRepository storeRepository;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(GroceryProductRepository.class);
        storeRepository = Mockito.mock(GroceryStoreRepository.class);
        inventoryService = new InventoryService(productRepository, storeRepository);
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

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(productRepository.findByStoreId(storeId)).thenReturn(List.of(product));

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

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(productRepository.findByStoreId(storeId))
            .thenReturn(List.of(lowStockProduct, outOfStockProduct));

        InventoryHealthResponse response = inventoryService.fetchInventoryHealth(storeId, true);

        assertThat(response.totalProducts()).isEqualTo(2);
        assertThat(response.lowStock()).isEqualTo(1);
        assertThat(response.outOfStock()).isEqualTo(1);
        assertThat(response.overallStatus()).isEqualTo(StoreHealthStatus.AT_RISK);
    }

    @Test
    void shouldThrowStoreNotFoundExceptionWhenStoreDoesNotExist() {
        String unknownStoreId = "unknown-store";
        when(storeRepository.findById(unknownStoreId)).thenReturn(Optional.empty());

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

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(productRepository.findByStoreId(storeId)).thenReturn(List.of());

        InventoryHealthResponse response = inventoryService.fetchInventoryHealth(storeId, false);

        assertThat(response.products()).isEmpty();
        assertThat(response.overallStatus()).isEqualTo(StoreHealthStatus.NO_INVENTORY);
    }
}

