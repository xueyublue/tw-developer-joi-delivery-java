package com.tw.joi.delivery.repository;

import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.domain.Outlet;
import com.tw.joi.delivery.seedData.SeedData;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Unified repository implementation that supports all outlet types.
 * Currently supports GroceryStore, but can be extended for other store types.
 */
@Repository
public class SeedDataOutletRepository implements OutletRepository {

    private final Map<String, Outlet> outlets = Map.of(
        SeedData.store101.getOutletId(), SeedData.store101,
        SeedData.store102.getOutletId(), SeedData.store102
    );

    @Override
    public Optional<Outlet> findById(String outletId) {
        return Optional.ofNullable(outlets.get(outletId));
    }
}
