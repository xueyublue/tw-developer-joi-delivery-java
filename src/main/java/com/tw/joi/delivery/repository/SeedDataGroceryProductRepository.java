package com.tw.joi.delivery.repository;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.seedData.SeedData;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Repository;

@Repository
public class SeedDataGroceryProductRepository implements GroceryProductRepository {

    private final List<GroceryProduct> products = SeedData.groceryProducts;

    @Override
    public List<GroceryProduct> findByStoreId(String storeId) {
        return products.stream()
            .filter(product -> product.getStore() != null
                && Objects.equals(product.getStore().getOutletId(), storeId))
            .toList();
    }
}

