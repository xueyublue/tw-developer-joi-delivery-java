package com.tw.joi.delivery.service;

import com.tw.joi.delivery.domain.Cart;
import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.User;
import com.tw.joi.delivery.dto.request.AddProductRequest;
import com.tw.joi.delivery.dto.response.CartProductInfo;
import com.tw.joi.delivery.dto.response.CartSummaryResponse;
import com.tw.joi.delivery.seedData.SeedData;
import java.math.BigDecimal;
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
        User user = findUserOrThrow(addProductRequest.getUserId());
        Cart cart = fetchCartForUserOrThrow(user);
        GroceryProduct product = productService.getProduct(addProductRequest.getProductId(),
                                                           addProductRequest.getOutletId());
        cart.getProducts().add(product);
        return new CartProductInfo(cart, product, product.getSellingPrice());
    }

    public Cart getCartForUser(String userId) {
        User user = findUserOrThrow(userId);
        return fetchCartForUserOrThrow(user);
    }

    public CartSummaryResponse getCartSummary(String userId) {
        User user = findUserOrThrow(userId);
        Cart cart = fetchCartForUserOrThrow(user);

        int totalItems = cart.getProducts().size();
        BigDecimal totalMrp = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (var product : cart.getProducts()) {
            if (product.getMrp() != null) {
                totalMrp = totalMrp.add(product.getMrp());
            }
            BigDecimal effectivePrice;
            if (product instanceof GroceryProduct groceryProduct
                && groceryProduct.getSellingPrice() != null) {
                effectivePrice = groceryProduct.getSellingPrice();
            } else {
                effectivePrice = product.getMrp() != null ? product.getMrp() : BigDecimal.ZERO;
            }
            totalPrice = totalPrice.add(effectivePrice);
        }

        BigDecimal totalSavings = totalMrp.subtract(totalPrice);

        return new CartSummaryResponse(
            user.getUserId(),
            cart.getCartId(),
            totalItems,
            totalMrp,
            totalPrice,
            totalSavings
        );
    }

    private User findUserOrThrow(String userId) {
        User user = userService.fetchUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    private Cart fetchCartForUserOrThrow(User user) {
        Cart cart = userCarts.get(user.getUserId());
        if (cart == null) {
            throw new CartNotFoundException(user.getUserId());
        }
        return cart;
    }

}
