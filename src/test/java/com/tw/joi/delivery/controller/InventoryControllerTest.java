package com.tw.joi.delivery.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.dto.response.ProductInventoryHealth;
import com.tw.joi.delivery.domain.ProductHealthStatus;
import com.tw.joi.delivery.domain.StoreHealthStatus;
import com.tw.joi.delivery.service.InventoryService;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

        ProductInventoryHealth productHealth =
            new ProductInventoryHealth("product101", "Wheat Bread", 10, 30,
                                       ProductHealthStatus.HEALTHY);
        InventoryHealthResponse response =
            new InventoryHealthResponse(storeId, "Fresh Picks", 1, 1, 0, 0,
                                        StoreHealthStatus.HEALTHY,
                                        List.of(productHealth));

        when(inventoryService.fetchInventoryHealth(storeId)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get(getUrl, storeId)
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.storeId", Is.is(storeId)))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.storeName", Is.is("Fresh Picks")))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.totalProducts", Is.is(1)))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.overallStatus", Is.is("HEALTHY")));
    }
}
