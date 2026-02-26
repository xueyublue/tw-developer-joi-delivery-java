package com.tw.joi.delivery.adapter.repository;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.port.ProductRepository;
import com.tw.joi.delivery.seedData.SeedData;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SeedDataProductRepository implements ProductRepository {

    private final List<GroceryProduct> products = SeedData.groceryProducts;

    @Override
    public GroceryProduct findByProductAndOutlet(String productId, String outletId) {
        return products.stream()
            .filter(groceryProduct ->
                        groceryProduct.getProductId().equals(productId)
                            && groceryProduct.getStore().getOutletId().equals(outletId))
            .findFirst()
            .orElse(null);
    }

}
