package com.tw.joi.delivery.repository;

import com.tw.joi.delivery.domain.GroceryProduct;

import java.util.List;

public interface GroceryProductRepository {

    List<GroceryProduct> findByStoreId(String storeId);

}
