package com.tw.joi.delivery.dto.response;

import com.tw.joi.delivery.domain.Product;

import java.util.Set;

public record Inventory(Set<Product> inventory) {
}
