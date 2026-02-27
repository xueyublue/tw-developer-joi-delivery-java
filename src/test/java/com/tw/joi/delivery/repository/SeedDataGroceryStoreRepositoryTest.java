package com.tw.joi.delivery.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.seedData.SeedData;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SeedDataGroceryStoreRepositoryTest {

    private final SeedDataGroceryStoreRepository repository = new SeedDataGroceryStoreRepository();

    @Test
    void shouldReturnStoreWhenStoreIdExists() {
        String existingStoreId = SeedData.store101.getOutletId();

        Optional<GroceryStore> result = repository.findByStoreId(existingStoreId);

        assertThat(result).isPresent();
        assertThat(result.get().getOutletId()).isEqualTo(existingStoreId);
        assertThat(result.get().getName()).isEqualTo(SeedData.store101.getName());
    }

    @Test
    void shouldReturnEmptyWhenStoreIdDoesNotExist() {
        Optional<GroceryStore> result = repository.findByStoreId("unknown-store");

        assertThat(result).isEmpty();
    }
}

