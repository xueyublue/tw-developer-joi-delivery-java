package com.tw.joi.delivery.controller;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/health")
    public ResponseEntity<Set<GroceryProduct>> fetchStoreInventoryHealth(@RequestParam(name = "storeId") String storeId) {
        return ResponseEntity.ok(inventoryService.getInventoryForStore(storeId));
    }
}
