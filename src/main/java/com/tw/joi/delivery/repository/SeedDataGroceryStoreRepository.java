package com.tw.joi.delivery.repository;

import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.seedData.SeedData;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class SeedDataGroceryStoreRepository implements GroceryStoreRepository {

    private final Map<String, GroceryStore> stores =
        Map.of(
            SeedData.store101.getOutletId(), SeedData.store101,
            SeedData.store102.getOutletId(), SeedData.store102
        );

    @Override
    public Optional<GroceryStore> findById(String storeId) {
        return Optional.ofNullable(stores.get(storeId));
    }
}

