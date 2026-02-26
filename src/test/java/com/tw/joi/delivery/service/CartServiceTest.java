package com.tw.joi.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.dto.request.AddProductRequest;
import com.tw.joi.delivery.dto.response.CartProductInfo;
import com.tw.joi.delivery.seedData.SeedData;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(new UserService(), new ProductService());
    }

    @Test
    void shouldUseMrpAsSellingPriceWhenNoOverrideIsPresent() {
        AddProductRequest request = new AddProductRequest();
        request.setUserId("user101");
        request.setOutletId("store101");
        request.setProductId("product101");

        CartProductInfo info = cartService.addProductToCartForUser(request);

        BigDecimal expectedMrp = info.product().getMrp();
        assertThat(info.sellingPrice()).isEqualByComparingTo(expectedMrp);
    }

    @Test
    void shouldPreferExplicitSellingPriceOverMrpInCartProductInfo() {
        GroceryProduct seedProduct = SeedData.groceryProducts.get(0);
        seedProduct.setSellingPrice(BigDecimal.valueOf(5));

        AddProductRequest request = new AddProductRequest();
        request.setUserId("user101");
        request.setOutletId(seedProduct.getStore().getOutletId());
        request.setProductId(seedProduct.getProductId());

        CartProductInfo info = cartService.addProductToCartForUser(request);

        assertThat(info.sellingPrice()).isEqualByComparingTo("5");
    }
}

