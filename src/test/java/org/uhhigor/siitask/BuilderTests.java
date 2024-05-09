package org.uhhigor.siitask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.uhhigor.siitask.builder.ProductBuilder;
import org.uhhigor.siitask.builder.ProductPriceBuilder;
import org.uhhigor.siitask.builder.PromoCodeBuilder;
import org.uhhigor.siitask.builder.PurchaseBuilder;
import org.uhhigor.siitask.exception.product.ProductBuilderException;
import org.uhhigor.siitask.exception.promocode.PromoCodeBuilderException;
import org.uhhigor.siitask.exception.purchase.PurchaseBuilderException;
import org.uhhigor.siitask.model.ProductPrice;
import org.uhhigor.siitask.model.PromoCode;
import org.uhhigor.siitask.model.Product;
import org.uhhigor.siitask.model.Purchase;


import java.util.Currency;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BuilderTests {

    @Test
    public void testProductPriceBuilder() {
        assertThrowsExactly(ProductPriceBuilderException.class, () -> new ProductPriceBuilder().build());

        assertThrowsExactly(ProductPriceBuilderException.class, () -> new ProductPriceBuilder()
                .currency("Non existent currency code")
                .build());

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

    @Test
    public void testPromoCodeBuilder() {
        assertThrowsExactly(PromoCodeBuilderException.class, () -> new PromoCodeBuilder().build());

        assertThrowsExactly(PromoCodeBuilderException.class, () -> new PromoCodeBuilder()
                .code(""));

        assertThrowsExactly(PromoCodeBuilderException.class, () -> new PromoCodeBuilder()
                .expirationDate(new Date(System.currentTimeMillis() - 1000)));

        try {
            Product product = new ProductBuilder()
                    .name("Example product name")
                    .description("Example description")
                    .prices(List.of(new ProductPriceBuilder()
                            .currency("USD")
                            .price(100.0)
                            .build()))
                    .build();

            PromoCode promoCode = new PromoCodeBuilder()
                    .code("TEST")
                    .expirationDate(new Date(System.currentTimeMillis() + 1000))
                    .discountAmount(10.0)
                    .currency("USD")
                    .uses(10)
                    .eligibleProducts(List.of(product))
                    .build();

            assertEquals("TEST", promoCode.getCode());
            assertEquals(10.0, promoCode.getDiscountAmount());
            assertEquals(Currency.getInstance("USD"), promoCode.getCurrency());
            assertEquals(10, promoCode.getUsesLeft());
            assertEquals(List.of(product), promoCode.getEligibleProducts());

        } catch (ProductPriceBuilderException e) {
            fail("ProductPriceBuilderException thrown");
        } catch (ProductBuilderException e) {
            fail("ProductBuilderException thrown");
        } catch (PromoCodeBuilderException e) {
            fail("PromoCodeBuilderException thrown");
        }
        System.out.println("PromoCodeBuilder test passed");
    }

    @Test
    void testPurchaseBuilder() {
        assertThrowsExactly(PurchaseBuilderException.class, () -> {
            new PurchaseBuilder().build();
        });
        assertThrowsExactly(PurchaseBuilderException.class, () -> {
            new PurchaseBuilder()
                    .date(new Date(System.currentTimeMillis() + 5000));
        });

        assertDoesNotThrow(() -> {
            new PurchaseBuilder()
                    .date(new Date());
        });

        try {
            Product product = new ProductBuilder()
                    .name("Example product name")
                    .description("Example description")
                    .prices(List.of(new ProductPriceBuilder()
                            .currency("USD")
                            .price(100.0)
                            .build()))
                    .build();

            PromoCode promoCode = new PromoCodeBuilder()
                    .code("TEST")
                    .expirationDate(new Date(System.currentTimeMillis() + 1000))
                    .discountAmount(10.0)
                    .currency("USD")
                    .uses(10)
                    .eligibleProducts(List.of(product))
                    .build();

            Purchase purchase = new PurchaseBuilder()
                    .date(new Date())
                    .product(product)
                    .currency("USD")
                    .build();

            assertEquals(product, purchase.getProduct());
            assertEquals(Currency.getInstance("USD"), purchase.getCurrency());
            assertEquals(100.0, purchase.getRegularPrice());
            assertEquals(0.0, purchase.getDiscountApplied());
            assertEquals(100.0, purchase.getFinalPrice());

            purchase = new PurchaseBuilder()
                    .date(new Date())
                    .product(product)
                    .currency("USD")
                    .promoCode(promoCode)
                    .build();

            assertEquals(product, purchase.getProduct());
            assertEquals(Currency.getInstance("USD"), purchase.getCurrency());
            assertEquals(100.0, purchase.getRegularPrice());
            assertEquals(10.0, purchase.getDiscountApplied());
            assertEquals(90.0, purchase.getFinalPrice());

        } catch (ProductPriceBuilderException e) {
            fail("ProductPriceBuilderException thrown");
        } catch (ProductBuilderException e) {
            fail("ProductBuilderException thrown");
        } catch (PromoCodeBuilderException e) {
            fail("PromoCodeBuilderException thrown");
        } catch (PurchaseBuilderException e) {
            fail("PurchaseBuilderException thrown");
        }
        System.out.println("PurchaseBuilder test passed");
    }

}
