package com.tw.joi.delivery.rule;

import com.tw.joi.delivery.domain.Cart;

import java.math.BigDecimal;

public class VipDiscountRule implements ShippingRule {

    @Override
    public BigDecimal apply(Cart cart, BigDecimal shipping) {
        return BigDecimal.TEN;
    }

}
