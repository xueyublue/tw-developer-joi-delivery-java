package com.tw.joi.delivery.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.seedData.SeedData;
import java.util.List;
import org.junit.jupiter.api.Test;

class SeedDataGroceryProductRepositoryTest {

    private final SeedDataGroceryProductRepository repository =
        new SeedDataGroceryProductRepository();

    @Test
    void shouldReturnProductsForExistingStore() {
        String storeId = SeedData.store101.getOutletId();

        List<GroceryProduct> products = repository.findByStoreId(storeId);

        assertThat(products).isNotEmpty();
        assertThat(products)
            .allMatch(p -> p.getStore() != null && storeId.equals(p.getStore().getOutletId()));
    }

    @Test
    void shouldReturnEmptyListForUnknownStore() {
        List<GroceryProduct> products = repository.findByStoreId("unknown-store");

        assertThat(products).isEmpty();
    }
}

