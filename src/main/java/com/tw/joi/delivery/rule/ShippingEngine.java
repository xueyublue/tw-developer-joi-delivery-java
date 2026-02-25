package com.tw.joi.delivery.rule;

import com.tw.joi.delivery.domain.Cart;

import java.math.BigDecimal;
import java.util.List;

public class ShippingEngine {

    private final List<ShippingRule> rules;

    public ShippingEngine(List<ShippingRule> rules) {
        this.rules = rules;
    }

    public BigDecimal calculate(Cart cart) {
        BigDecimal shipping = BigDecimal.TEN;   // the calculated base shipping
        for (ShippingRule rule : rules) {
            shipping = rule.apply(cart, shipping);
        }
        return shipping;
    }

}
