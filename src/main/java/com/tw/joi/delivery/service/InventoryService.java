package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.seedData.SeedData;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class InventoryService {

    public Set<GroceryProduct> getInventoryForStore(String storeId) {
        if (SeedData.store101.getOutletId().equals(storeId)) {
            return SeedData.store101.getInventory();
        } else if (SeedData.store102.getOutletId().equals(storeId)) {
            return SeedData.store102.getInventory();
        }
        return null;
    }

}
