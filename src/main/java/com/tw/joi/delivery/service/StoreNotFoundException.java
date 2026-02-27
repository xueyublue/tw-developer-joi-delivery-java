package com.tw.joi.delivery.service;

public class StoreNotFoundException extends RuntimeException {

    public StoreNotFoundException(String storeId) {
        super("Store not found: " + storeId);
    }
}

