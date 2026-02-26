package com.tw.joi.delivery.adapter.repository;

import com.tw.joi.delivery.domain.Cart;
import com.tw.joi.delivery.port.CartRepository;
import com.tw.joi.delivery.seedData.SeedData;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class SeedDataCartRepository implements CartRepository {

    private final Map<String, Cart> userCarts = SeedData.cartForUsers;

    @Override
    public Cart findByUserId(String userId) {
        return userCarts.get(userId);
    }

}
