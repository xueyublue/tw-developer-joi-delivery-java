package com.tw.joi.delivery.repository;

import com.tw.joi.delivery.domain.GroceryStore;

import java.util.Optional;

public interface GroceryStoreRepository {

    Optional<GroceryStore> findByStoreId(String storeId);

}
