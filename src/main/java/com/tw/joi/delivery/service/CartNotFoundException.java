package com.tw.joi.delivery.service;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(String userId) {
        super("Cart not found for user: " + userId);
    }
}

