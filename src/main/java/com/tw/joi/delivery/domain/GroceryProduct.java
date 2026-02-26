package com.tw.joi.delivery.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroceryProduct extends Product {

    private BigDecimal sellingPrice;
    private BigDecimal weight;

    private int expiryDate;

    private int threshold;

    private int availableStock;

    private BigDecimal discount;

    private GroceryStore store;

    @Builder
    public GroceryProduct(String productId, String productName, BigDecimal mrp, Cart cart,
                          BigDecimal sellingPrice, BigDecimal weight, int expiryDate, int threshold,
                          int availableStock, GroceryStore store, BigDecimal discount) {
        super(productId, productName,  mrp);
        this.sellingPrice = sellingPrice;
        this.weight = weight;
        this.expiryDate = expiryDate;
        this.threshold = threshold;
        this.availableStock = availableStock;
        this.store = store;
        this.discount = discount;
    }

    public BigDecimal effectivePrice() {
        if (sellingPrice != null) {
            return sellingPrice;
        }
        return mrp != null ? mrp : BigDecimal.ZERO;
    }

    public void applyDiscountIfPresent() {
        if (discount == null || mrp == null || sellingPrice != null) {
            return;
        }
        BigDecimal discounted = mrp.subtract(discount);
        if (discounted.compareTo(BigDecimal.ZERO) < 0) {
            discounted = BigDecimal.ZERO;
        }
        setSellingPrice(discounted);
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        if (sellingPrice != null && sellingPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Selling price cannot be negative");
        }
        if (sellingPrice != null && mrp != null
            && sellingPrice.compareTo(mrp) > 0) {
            throw new IllegalArgumentException("Selling price cannot exceed MRP");
        }
        this.sellingPrice = sellingPrice;
    }

}
