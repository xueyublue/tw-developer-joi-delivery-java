package com.tw.joi.delivery.port;

import com.tw.joi.delivery.domain.GroceryProduct;

public interface ProductRepository {

    GroceryProduct findByProductAndOutlet(String productId, String outletId);

}
