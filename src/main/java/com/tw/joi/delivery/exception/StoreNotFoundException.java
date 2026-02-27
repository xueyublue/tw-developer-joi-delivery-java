package com.tw.joi.delivery.exception;

public class StoreNotFoundException extends RuntimeException {

    public StoreNotFoundException(String storeId) {
        super("store not found: " + storeId);
    }

}
