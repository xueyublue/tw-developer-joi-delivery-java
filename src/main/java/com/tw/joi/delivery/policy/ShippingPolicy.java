package com.tw.joi.delivery.policy;

import com.tw.joi.delivery.domain.Cart;

import java.math.BigDecimal;

public interface ShippingPolicy {

    BigDecimal calculate(Cart cart);

}
