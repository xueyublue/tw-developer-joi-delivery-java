package com.tw.joi.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.tw.joi.delivery.domain.Cart;
import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.dto.response.CartSummaryResponse;
import com.tw.joi.delivery.seedData.SeedData;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CartServiceTest {

    private UserService userService;
    private ProductService productService;
    private CartService cartService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        productService = Mockito.mock(ProductService.class);
        cartService = new CartService(userService, productService);
    }

    @Test
    void shouldCalculateCartSummaryForUser() {
        String userId = "user101";
        User user = SeedData.user101;
        Cart cart = SeedData.cartForUsers.get(userId);
        cart.getProducts().clear();

        GroceryStore store = SeedData.store101;
        GroceryProduct product1 = GroceryProduct.builder()
            .productId("p1")
            .productName("P1")
            .mrp(BigDecimal.valueOf(100))
            .sellingPrice(BigDecimal.valueOf(80))
            .store(store)
            .build();
        GroceryProduct product2 = GroceryProduct.builder()
            .productId("p2")
            .productName("P2")
            .mrp(BigDecimal.valueOf(50))
            .sellingPrice(null)
            .store(store)
            .build();

        cart.getProducts().add(product1);
        cart.getProducts().add(product2);

        when(userService.fetchUserById(userId)).thenReturn(user);

        CartSummaryResponse summary = cartService.getCartSummary(userId);

        assertThat(summary.userId()).isEqualTo(userId);
        assertThat(summary.cartId()).isEqualTo(cart.getCartId());
        assertThat(summary.totalItems()).isEqualTo(2);
        assertThat(summary.totalMrp()).isEqualByComparingTo("150");
        assertThat(summary.totalPrice()).isEqualByComparingTo("130");
        assertThat(summary.totalSavings()).isEqualByComparingTo("20");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        String unknownUser = "unknown";
        when(userService.fetchUserById(unknownUser)).thenReturn(null);

        assertThatThrownBy(() -> cartService.getCartSummary(unknownUser))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining(unknownUser);
    }
}

