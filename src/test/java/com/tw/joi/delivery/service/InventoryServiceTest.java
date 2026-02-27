package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.exception.StoreNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    void shouldReturnHealthyInventory() {
        String storeId = "store101";

        InventoryHealthResponse response = inventoryService.fetchInventoryHealth(storeId);

        assertThat(response).isNotNull();
        assertThat(response.storeId()).isEqualTo(storeId);
        assertThat(response.storeName()).isEqualTo("Fresh Picks");
        assertThat(response.totalProducts()).isEqualTo(3);
        assertThat(response.healthy()).isEqualTo(response.totalProducts());
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