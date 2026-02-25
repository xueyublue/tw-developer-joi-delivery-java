package com.tw.joi.delivery.policy;

import com.tw.joi.delivery.domain.Cart;

import java.math.BigDecimal;

public class VipDiscountShippingPolicy implements ShippingPolicy {

    private final ShippingPolicy delegate;

    VipDiscountShippingPolicy(ShippingPolicy delegate) {
        this.delegate = delegate;
    }

    @Override
    public BigDecimal calculate(Cart cart) {
        return delegate.calculate(cart).multiply(new BigDecimal("0.5"));
    }

}
