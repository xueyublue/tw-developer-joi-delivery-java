package com.tw.joi.delivery.repository;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.seedData.SeedData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class SeedDataGroceryProductRepository implements GroceryProductRepository {

    private final List<GroceryProduct> products = SeedData.groceryProducts;

    @Override
    public List<GroceryProduct> findByStoreId(String storeId) {
        return products.stream()
                .filter(item -> Objects.equals(item.getStore().getOutletId(), storeId))
                .toList();
    }

}
