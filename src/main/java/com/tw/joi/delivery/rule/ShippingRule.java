package com.tw.joi.delivery.rule;

import com.tw.joi.delivery.domain.Cart;

import java.math.BigDecimal;

public interface ShippingRule {

    BigDecimal apply(Cart cart, BigDecimal currentShipping);

}
