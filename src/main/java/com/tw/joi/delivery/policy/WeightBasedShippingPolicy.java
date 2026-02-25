package com.tw.joi.delivery.policy;

import com.tw.joi.delivery.domain.Cart;

import java.math.BigDecimal;

public class WeightBasedShippingPolicy implements ShippingPolicy {

    @Override
    public BigDecimal calculate(Cart cart) {
        return BigDecimal.TEN;
    }

}
