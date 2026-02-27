package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.exception.StoreNotFoundException;
import com.tw.joi.delivery.repository.GroceryProductRepository;
import com.tw.joi.delivery.repository.GroceryStoreRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @MockitoBean
    private GroceryStoreRepository storeRepository;

    @MockitoBean
    private GroceryProductRepository productRepository;

    @Test
    void shouldReturnHealthyInventory() {
        String storeId = "store101";

        GroceryStore store = GroceryStore.builder()
                .outletId(storeId)
                .name("fake store")
                .description("fake store in Singapore")
                .build();

        GroceryProduct product = GroceryProduct.builder()
                .productId("product001")
                .productName("Pencil")
                .mrp(BigDecimal.valueOf(10.5))
                .weight(BigDecimal.valueOf(500.00))
                .store(store)
                .threshold(10)
                .availableStock(30)
                .build();

        store.getInventory().add(product);

        Mockito.when(storeRepository.findByStoreId(storeId)).thenReturn(Optional.of(store));
        Mockito.when(productRepository.findByStoreId(storeId)).thenReturn(List.of(product));

        InventoryHealthResponse response = inventoryService.fetchInventoryHealth(storeId);

        assertThat(response).isNotNull();
        assertThat(response.storeId()).isEqualTo(storeId);
        assertThat(response.storeName()).isEqualTo("fake store");
        assertThat(response.totalProducts()).isEqualTo(1);
        assertThat(response.healthy()).isEqualTo(1);
        assertThat(response.lowStock()).isZero();
        assertThat(response.outOfStore()).isZero();
        assertThat(response.overallStatus()).isEqualTo(StoreHealthStatus.HEALTHY);
    }

    @Test
    void shouldThrownStoreNotFoundException() {
        String storeId = "unknown-store";

        assertThatThrownBy(() -> inventoryService.fetchInventoryHealth(storeId))
                .isInstanceOf(StoreNotFoundException.class);
    }

}