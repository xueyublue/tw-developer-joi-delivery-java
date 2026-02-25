package com.tw.joi.delivery.policy;

import com.tw.joi.delivery.domain.Cart;

import java.math.BigDecimal;

public class FreeShippingOverAmountPolicy implements ShippingPolicy {

    private final ShippingPolicy delegate;
    private final BigDecimal threshold;

    public FreeShippingOverAmountPolicy(ShippingPolicy delegate, BigDecimal threshold) {
        this.delegate = delegate;
        this.threshold = threshold;
    }

    @Override
    public BigDecimal calculate(Cart cart) {
        return BigDecimal.TEN;
    }

}
