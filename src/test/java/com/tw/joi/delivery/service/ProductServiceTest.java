package com.tw.joi.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.tw.joi.delivery.domain.GroceryProduct;
import com.tw.joi.delivery.domain.GroceryStore;
import com.tw.joi.delivery.port.ProductRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldFetchProductByIdAndOutletFromRepository() {
        String productId = "product101";
        String outletId = "store101";

        GroceryStore store = GroceryStore.builder()
            .outletId(outletId)
            .name("Fresh Picks")
            .build();

        GroceryProduct product = GroceryProduct.builder()
            .productId(productId)
            .productName("Wheat Bread")
            .mrp(BigDecimal.TEN)
            .store(store)
            .build();

        when(productRepository.findByProductAndOutlet(productId, outletId)).thenReturn(product);

        GroceryProduct result = productService.getProduct(productId, outletId);

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getStore().getOutletId()).isEqualTo(outletId);
    }
}
