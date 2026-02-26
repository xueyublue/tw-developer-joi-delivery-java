package com.tw.joi.delivery.controller;

import com.tw.joi.delivery.dto.response.InventoryHealthResponse;
import com.tw.joi.delivery.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/health")
    public ResponseEntity<InventoryHealthResponse> fetchStoreInventoryHealth(
        @RequestParam(name = "storeId") String storeId,
        @RequestParam(name = "includeProducts", required = false, defaultValue = "true")
        boolean includeProducts,
        @RequestParam(name = "page", required = false, defaultValue = "0")
        int page,
        @RequestParam(name = "size", required = false, defaultValue = "10")
        int size) {
        return ResponseEntity.ok(
            inventoryService.fetchInventoryHealth(storeId, includeProducts, page, size));
    }
}
