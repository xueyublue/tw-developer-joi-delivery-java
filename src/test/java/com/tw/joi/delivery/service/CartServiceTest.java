package com.tw.joi.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.tw.joi.delivery.domain.Cart;
import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.dto.request.AddProductRequest;
import com.tw.joi.delivery.dto.response.CartProductInfo;
import com.tw.joi.delivery.port.CartRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldAddProductToUsersCart() {
        String userId = "user101";
        String outletId = "store101";
        String productId = "product101";

        User user = User.builder()
            .userId(userId)
            .firstName("John")
            .lastName("Doe")
            .build();

        Cart cart = Cart.builder()
            .cartId("cart101")
            .user(user)
            .build();

        GroceryStore store = GroceryStore.builder()
            .outletId(outletId)
            .name("Fresh Picks")
            .build();

        GroceryProduct product = GroceryProduct.builder()
            .productId(productId)
            .productName("Wheat Bread")
            .mrp(BigDecimal.TEN)
            .sellingPrice(BigDecimal.valueOf(9.5))
            .store(store)
            .build();

        AddProductRequest request = new AddProductRequest();
        request.setUserId(userId);
        request.setOutletId(outletId);
        request.setProductId(productId);

        when(userService.fetchUserById(userId)).thenReturn(user);
        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(productService.getProduct(productId, outletId)).thenReturn(product);

        CartProductInfo result = cartService.addProductToCartForUser(request);

        assertThat(cart.getProducts()).containsExactly(product);
        assertThat(result.cart()).isEqualTo(cart);
        assertThat(result.product()).isEqualTo(product);
        assertThat(result.sellingPrice()).isEqualByComparingTo(BigDecimal.valueOf(9.5));
    }

    @Test
    void shouldReturnCartForUser() {
        String userId = "user101";

        User user = User.builder()
            .userId(userId)
            .firstName("John")
            .lastName("Doe")
            .build();

        Cart cart = Cart.builder()
            .cartId("cart101")
            .user(user)
            .build();

        when(userService.fetchUserById(userId)).thenReturn(user);
        when(cartRepository.findByUserId(userId)).thenReturn(cart);

        Cart result = cartService.getCartForUser(userId);

        assertThat(result).isEqualTo(cart);
    }
}
