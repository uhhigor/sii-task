package org.uhhigor.siitask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.uhhigor.siitask.builder.ProductBuilder;
import org.uhhigor.siitask.builder.ProductPriceBuilder;
import org.uhhigor.siitask.exception.ProductBuilderException;
import org.uhhigor.siitask.exception.ProductPriceBuilderException;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.repository.ProductRepository;
import org.uhhigor.siitask.model.Product;

import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BuilderTests {

    @Test
    public void testProductPriceBuilder() {
        assertThrowsExactly(ProductPriceBuilderException.class, () -> {
            new ProductPriceBuilder().build();
        });

        assertThrowsExactly(ProductPriceBuilderException.class, () -> {
            new ProductPriceBuilder()
                    .currency("Non existent currency code")
                    .build();
        });

        try {
            ProductPrice productPrice = new ProductPriceBuilder()
                    .currency("USD")
                    .price(100.0)
                    .build();
            assertEquals(100.0, productPrice.getPrice());
            assertEquals(Currency.getInstance("USD"), productPrice.getCurrency());

            ProductPrice productPrice1 = new ProductPriceBuilder()
                    .currency("EUR")
                    .price(200.0)
                    .build();

            assertEquals(200.0, productPrice1.getPrice());
            assertEquals(Currency.getInstance("EUR"), productPrice1.getCurrency());
        } catch (ProductPriceBuilderException e) {
            fail("ProductPriceBuilderException thrown");
        }
        System.out.println("ProductPriceBuilder test passed");
    }

    @Test
    public void testProductBuilder() {
        assertThrowsExactly(ProductBuilderException.class, () -> {
            new ProductBuilder().build();
        });

        try {
            ProductPrice productPrice = new ProductPriceBuilder()
                    .currency("USD")
                    .price(100.0)
                    .build();
            Product product = new ProductBuilder()
                    .name("Example product name")
                    .description("Example description")
                    .prices(List.of(productPrice))
                    .build();
            assertEquals("Example product name", product.getName());
            assertEquals("Example description", product.getDescription());
            assertEquals(List.of(productPrice), product.getPrices());
        } catch (ProductPriceBuilderException e) {
            fail("ProductPriceBuilderException thrown");
        } catch (ProductBuilderException e) {
            fail("ProductBuilderException thrown");
        }
        System.out.println("ProductBuilder test passed");
    }

}
