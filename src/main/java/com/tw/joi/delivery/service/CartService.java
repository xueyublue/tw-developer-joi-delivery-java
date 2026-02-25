package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.Cart;
import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.Product;
import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.dto.request.AddProductRequest;
import com.tw.joi.delivery.dto.response.CartProductInfo;
import com.tw.joi.delivery.seedData.SeedData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final Map<String,Cart> userCarts= SeedData.cartForUsers;
    private final UserService userService;
    private final ProductService productService;

    public CartProductInfo addProductToCartForUser(AddProductRequest addProductRequest) {
        User user=userService.fetchUserById(addProductRequest.getUserId());
        Cart cart = fetchCartForUser(user);
        GroceryProduct product = productService.getProduct(addProductRequest.getProductId(),
                                                           addProductRequest.getOutletId());
        cart.getProducts().add(product);
        return new CartProductInfo(cart, product, calculateSellingPrice(cart.getProducts()));
    }

    public Cart getCartForUser(String userId) {
        User user=userService.fetchUserById(userId);
        return fetchCartForUser(user);
    }

    private Cart fetchCartForUser(User user) {
        return userCarts.get(user.getUserId());
    }

    private BigDecimal calculateSellingPrice(List<Product> products) {
        BigDecimal productTotal = products.stream()
                .map(item -> (GroceryProduct) item)
                .map(item -> item.getSellingPrice().multiply(BigDecimal.ONE.subtract(item.getDiscount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalWeight = products.stream()
                .map(item -> (GroceryProduct) item)
                .map(GroceryProduct::getWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = totalWeight.multiply(BigDecimal.TEN);

        return productTotal.add(shippingFee);
    }
}
