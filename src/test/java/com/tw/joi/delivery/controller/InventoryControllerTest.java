package com.tw.joi.delivery.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tw.joi.delivery.domain.ProductStatusHealth;
import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import com.tw.joi.delivery.exception.StoreNotFoundException;
import com.tw.joi.delivery.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Test
    void shouldReturnTheHealthOfTheStore() throws Exception {
        String getUrl = "/inventory/health?storeId={storeId}";
        String storeId = "store101";

        ProductInventoryHealth productInventoryHealth = new ProductInventoryHealth(
                "product001", "Pencil", 10, 20, ProductStatusHealth.HEALTH
        );

        InventoryHealthResponse inventoryHealthResponse = new InventoryHealthResponse(
                storeId, "My first store", "", 1, 1, 0, 0,
                StoreHealthStatus.HEALTHY,
                List.of(productInventoryHealth)
        );

        when(inventoryService.fetchInventoryHealth(anyString())).thenReturn(inventoryHealthResponse);

        mockMvc.perform(get(getUrl, storeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId", is(storeId)))
                .andExpect(jsonPath("$.storeName", is("My first store")))
                .andExpect(jsonPath("$.totalProducts", is(1)))
                .andExpect(jsonPath("$.overallStatus", is("HEALTHY")));
    }

    @Test
    void shouldReturn404IfStoreNotFound() throws Exception {
        String getUrl = "/inventory/health?storeId={storeId}";
        String storeId = "store101";

        when(inventoryService.fetchInventoryHealth(storeId))
                .thenThrow(new StoreNotFoundException(storeId));

        mockMvc.perform(get(getUrl, storeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
//                .andExpect(content().string("store not found: " + storeId));
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("store not found: " + storeId)));
    }

}