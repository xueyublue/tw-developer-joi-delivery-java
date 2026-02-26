package com.tw.joi.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import org.junit.jupiter.api.Test;

class InventoryServiceTest {

    private final InventoryService inventoryService = new InventoryService();

    @Test
    void shouldReturnHealthyInventoryForSeedStore101() {
        InventoryHealthResponse response = inventoryService.fetchInventoryHealth("store101");

        assertThat(response.storeId()).isEqualTo("store101");
        assertThat(response.storeName()).isEqualTo("Fresh Picks");
        assertThat(response.totalProducts()).isGreaterThan(0);
        assertThat(response.healthy()).isEqualTo(response.totalProducts());
        assertThat(response.lowStock()).isZero();
        assertThat(response.outOfStock()).isZero();
        assertThat(response.overallStatus()).isEqualTo("HEALTHY");
    }

    @Test
    void shouldReturnNoInventoryForUnknownStore() {
        InventoryHealthResponse response = inventoryService.fetchInventoryHealth("unknown-store");

        assertThat(response.storeId()).isEqualTo("unknown-store");
        assertThat(response.storeName()).isNull();
        assertThat(response.totalProducts()).isZero();
        assertThat(response.healthy()).isZero();
        assertThat(response.lowStock()).isZero();
        assertThat(response.outOfStock()).isZero();
        assertThat(response.overallStatus()).isEqualTo("NO_INVENTORY");
    }
}

