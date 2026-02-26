package com.tw.joi.delivery.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class GroceryProductTest {

    @Test
    void effectivePriceShouldPreferSellingPriceWhenPresent() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("p1")
            .productName("Test")
            .mrp(BigDecimal.valueOf(100))
            .sellingPrice(BigDecimal.valueOf(80))
            .build();

        assertThat(product.effectivePrice()).isEqualByComparingTo("80");
    }

    @Test
    void effectivePriceShouldFallbackToMrpWhenSellingPriceIsNull() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("p1")
            .productName("Test")
            .mrp(BigDecimal.valueOf(100))
            .build();

        assertThat(product.effectivePrice()).isEqualByComparingTo("100");
    }

    @Test
    void effectivePriceShouldBeZeroWhenBothPricesAreNull() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("p1")
            .productName("Test")
            .build();

        assertThat(product.effectivePrice()).isEqualByComparingTo("0");
    }

    @Test
    void applyDiscountShouldSetSellingPriceFromMrpAndDiscount() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("p1")
            .productName("Test")
            .mrp(BigDecimal.valueOf(100))
            .discount(BigDecimal.valueOf(25))
            .build();

        product.applyDiscountIfPresent();

        assertThat(product.getSellingPrice()).isEqualByComparingTo("75");
    }

    @Test
    void applyDiscountShouldNotGoBelowZero() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("p1")
            .productName("Test")
            .mrp(BigDecimal.valueOf(50))
            .discount(BigDecimal.valueOf(100))
            .build();

        product.applyDiscountIfPresent();

        assertThat(product.getSellingPrice()).isEqualByComparingTo("0");
    }

    @Test
    void setSellingPriceShouldRejectNegativeValues() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("p1")
            .productName("Test")
            .mrp(BigDecimal.valueOf(100))
            .build();

        assertThatThrownBy(() -> product.setSellingPrice(BigDecimal.valueOf(-1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("negative");
    }

    @Test
    void setSellingPriceShouldRejectValuesGreaterThanMrpWhenMrpPresent() {
        GroceryProduct product = GroceryProduct.builder()
            .productId("p1")
            .productName("Test")
            .mrp(BigDecimal.valueOf(100))
            .build();

        assertThatThrownBy(() -> product.setSellingPrice(BigDecimal.valueOf(120)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("exceed MRP");
    }
}

