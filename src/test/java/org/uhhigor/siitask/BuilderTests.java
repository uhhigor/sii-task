package org.uhhigor.siitask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.uhhigor.siitask.builder.ProductBuilder;
import org.uhhigor.siitask.builder.ProductPriceBuilder;
import org.uhhigor.siitask.builder.PromoCodeBuilder;
import org.uhhigor.siitask.builder.PurchaseBuilder;
import org.uhhigor.siitask.exception.product.ProductException;
import org.uhhigor.siitask.exception.product.ProductPriceException;
import org.uhhigor.siitask.exception.product.ProductPriceNotValidException;
import org.uhhigor.siitask.exception.promocode.PromoCodeException;
import org.uhhigor.siitask.exception.promocode.PromoCodeExpirationDateInvalidException;
import org.uhhigor.siitask.exception.promocode.PromoCodeIncorrectException;
import org.uhhigor.siitask.exception.purchase.PurchaseDateInvalidException;
import org.uhhigor.siitask.exception.purchase.PurchaseException;
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
        assertThrowsExactly(ProductPriceException.class, () -> new ProductPriceBuilder().build());

        assertThrowsExactly(ProductPriceNotValidException.class, () -> new ProductPriceBuilder()
                .currency("Non existent currency code"));

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
        } catch (ProductPriceException e) {
            fail("ProductPriceException thrown: " + e.getMessage());
        }
        System.out.println("ProductPriceBuilder test passed");
    }

    @Test
    public void testProductBuilder() {
        assertThrowsExactly(ProductException.class, () -> new ProductBuilder().build());

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
        } catch (ProductPriceException e) {
            fail("ProductPriceException thrown: " + e.getMessage());
        } catch (ProductException e) {
            fail("ProductBuilderException thrown: " + e.getMessage());
        }
        System.out.println("ProductBuilder test passed");
    }

    @Test
    public void testPromoCodeBuilder() {
        assertThrowsExactly(PromoCodeException.class, () -> new PromoCodeBuilder().build());

        assertThrowsExactly(PromoCodeIncorrectException.class, () -> new PromoCodeBuilder()
                .code(""));

        assertThrowsExactly(PromoCodeExpirationDateInvalidException.class, () -> new PromoCodeBuilder()
                .expirationDate(new Date(System.currentTimeMillis() - 1000)));

        try {
            PromoCode promoCode = new PromoCodeBuilder()
                    .code("TEST")
                    .expirationDate(new Date(System.currentTimeMillis() + 1000))
                    .discountAmount(10.0)
                    .currency("USD")
                    .uses(10)
                    .type("FIXED")
                    .build();

            assertEquals("TEST", promoCode.getCode());
            assertEquals(10.0, promoCode.getDiscountAmount());
            assertEquals(Currency.getInstance("USD"), promoCode.getCurrency());
            assertEquals(10, promoCode.getUsesLeft());
        } catch (PromoCodeException e) {
            fail("PromoCodeException thrown: " + e.getMessage());
        }
        System.out.println("PromoCodeBuilder test passed");
    }

    @Test
    void testPurchaseBuilder() {
        assertThrowsExactly(PurchaseException.class, () -> new PurchaseBuilder().build());
        assertThrowsExactly(PurchaseDateInvalidException.class, () -> new PurchaseBuilder()
                .date(new Date(System.currentTimeMillis() + 5000)));

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

            Purchase purchase = new PurchaseBuilder()
                    .date(new Date())
                    .product(product)
                    .currency(Currency.getInstance("USD"))
                    .build();

            assertEquals(product, purchase.getProduct());
            assertEquals(Currency.getInstance("USD"), purchase.getCurrency());
            assertEquals(100.0, purchase.getRegularPrice());
            assertEquals(0.0, purchase.getDiscountApplied());

            purchase = new PurchaseBuilder()
                    .date(new Date())
                    .product(product)
                    .currency(Currency.getInstance("USD"))
                    .discount(10.0)
                    .build();

            assertEquals(product, purchase.getProduct());
            assertEquals(Currency.getInstance("USD"), purchase.getCurrency());
            assertEquals(100.0, purchase.getRegularPrice());
            assertEquals(10.0, purchase.getDiscountApplied());

        } catch (ProductException e) {
            fail("ProductException thrown: " + e.getMessage());
        } catch (PurchaseException e) {
            fail("PurchaseException thrown: " + e.getMessage());
        }
        System.out.println("PurchaseBuilder test passed");
    }

}
