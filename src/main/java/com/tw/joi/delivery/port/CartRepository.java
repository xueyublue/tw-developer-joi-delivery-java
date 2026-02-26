package com.tw.joi.delivery.port;

import com.tw.joi.delivery.domain.Cart;

public interface CartRepository {

    Cart findByUserId(String userId);

}
