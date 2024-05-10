package org.uhhigor.siitask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PurchaseTests {
    @Autowired
    private MockMvc mockMvc;

    // Tests for the purchase controller

    // Test 1: Test purchase controller with valid purchase
        // 1. Create a product with a price of 100 USD
        // 2. Finalize the purchase with the product
        // 3. Check sales report

    @Test
    public void purchaseScenario1() {
        try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""

                            {
                            "name": "Product1",
                            "prices": [
                                {
                                    "price": 100.0,
                                    "currency": "USD"
                                }
                            ]
                        }
                        """)
            ).andExpectAll(status().isOk());

            mockMvc.perform(post("/purchase/finalize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "productId": 1,
                        "currencyCode": "USD"
                    }
                    """))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.message").value("Purchase successful"),
                            jsonPath("$.purchase.productId").value(1),
                            jsonPath("$.purchase.regularPrice").value(100),
                            jsonPath("$.purchase.discountApplied").value(0),
                            jsonPath("$.purchase.currencyCode").value("USD")
                    );

            mockMvc.perform(get("/purchase/report"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.entries[0].currency").value("USD"),
                            jsonPath("$.entries[0].totalAmount").value(100),
                            jsonPath("$.entries[0].totalDiscount").value(0),
                            jsonPath("$.entries[0].numberOfPurchases").value(1)
                    );
        } catch (Exception e) {
            fail(e);
        }
    }

    // Test 2: Test purchase controller with invalid purchase
        // 1. Create a product with a price of 100 USD
        // 2. Try to finalize the purchase with different currency code

    @Test
    public void purchaseScenario2() {
        try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""

                            {
                            "name": "Product1",
                            "prices": [
                                {
                                    "price": 100.0,
                                    "currency": "USD"
                                }
                            ]
                            }
                        """)
            ).andExpectAll(status().isOk());

            mockMvc.perform(post("/purchase/finalize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""

                                    {
                        "productId": 1,
                        "currencyCode": "EUR"
                    }
                    """))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("Error while finalizing purchase: Product price in selected currency not found")
                    );
        } catch (Exception e) {
            fail(e);
        }
        }

        // Test 3: Test purchase controller with valid purchase and promo code

        // 1. Create a product with a price of 100 USD
        // 2. Create a promo code with a discount of 10 USD, 1 use
        // 3. Finalize the purchase with the product and the promo code
        // 4. Check if promo code usage increased by 1, and available uses decreased by 1
        // 5. Check if promo code is not usable anymore
        // 6. Check sales report

        @Test
        public void purchaseScenario3() {
            try {
                mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""

                                {
                                "name": "Product1",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "USD"
                                    }
                                ]
                            }
                            """)
                ).andExpectAll(status().isOk());

                mockMvc.perform(post("/promo-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "code": "PROMO1",
                            "expirationDate": "2025-12-31",
                            "discountAmount": 10.0,
                            "currency": "USD",
                            "uses": 1,
                            "type": "FIXED"
                        }
                        """))
                        .andExpectAll(status().isOk());

                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 1,
                            "currencyCode": "USD",
                            "promoCode": "PROMO1"
                        }
                        """))
                        .andExpectAll(
                                status().isOk(),
                                jsonPath("$.message").value("Purchase successful"),
                                jsonPath("$.purchase.productId").value(1),
                                jsonPath("$.purchase.regularPrice").value(100),
                                jsonPath("$.purchase.discountApplied").value(10),
                                jsonPath("$.purchase.currencyCode").value("USD")
                        );

                mockMvc.perform(get("/promo-code/PROMO1"))
                        .andExpectAll(
                                status().isOk(),
                                jsonPath("$.promoCodes[0].code").value("PROMO1"),
                                jsonPath("$.promoCodes[0].discountAmount").value(10),
                                jsonPath("$.promoCodes[0].currency").value("USD"),
                                jsonPath("$.promoCodes[0].usesLeft").value(0),
                                jsonPath("$.promoCodes[0].timesUsed").value(1)
                        );

                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 1,
                            "currencyCode": "USD",
                            "promoCode": "PROMO1"
                        }
                        """))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$.message").value("Error while finalizing purchase: Error while getting discount price: Promo code has no uses left")
                        );

                mockMvc.perform(get("/purchase/report"))
                        .andExpectAll(
                                status().isOk(),
                                jsonPath("$.entries[0].currency").value("USD"),
                                jsonPath("$.entries[0].totalAmount").value(100),
                                jsonPath("$.entries[0].totalDiscount").value(10),
                                jsonPath("$.entries[0].numberOfPurchases").value(1)
                        );
            } catch (Exception e) {
                fail(e);
            }
        }

        // Test 4: Test purchase controller with invalid promo code
            // 1. Create a product with a price of 100 USD
            // 2. Try to finalize the purchase with promo code for different currency
            // 3. Check sales report

        @Test
        public void purchaseScenario4() {
            try {
                mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""

                                {
                                "name": "Product1",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "USD"
                                    }
                                ]
                            }
                            """)
                ).andExpectAll(status().isOk());

                mockMvc.perform(post("/promo-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "code": "PROMO1",
                            "expirationDate": "2025-12-31",
                            "discountAmount": 10.0,
                            "currency": "EUR",
                            "uses": 1,
                            "type": "FIXED"
                        }
                        """))
                        .andExpectAll(status().isOk());

                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 1,
                            "currencyCode": "USD",
                            "promoCode": "PROMO1"
                        }
                        """))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$.message").value("Error while finalizing purchase: Error while getting discount price: Product currency does not match promo code currency")
                        );

                mockMvc.perform(get("/purchase/report"))
                        .andExpectAll(
                                status().isOk(),
                                content().json("""
                                {
                                    "entries": []
                                }
                                """)
                        );
            } catch (Exception e) {
                fail(e);
            }
        }

        // Test 5: Test purchase controller with invalid product
            // 1. Try to finalize the purchase with non-existent product

        @Test
        public void purchaseScenario5() {
            try {
                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 1,
                            "currencyCode": "USD"
                        }
                        """))
                        .andExpectAll(
                                status().isNotFound()
                        );
            } catch (Exception e) {
                fail(e);
            }
        }

        // Test 5: Test purchase controller with product having two prices and promo code with one of the currencies
            // 1. Create a product with two prices: 100 USD and 90 EUR
            // 2. Create a promo code with a discount of 10 USD, 1 use
            // 3. Finalize the purchase with the product and the promo code

        @Test
        public void purchaseScenario6() {
            try {
                mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""

                                {
                                "name": "Product1",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "USD"
                                    },
                                    {
                                        "price": 90.0,
                                        "currency": "EUR"
                                    }
                                ]
                            }
                            """)
                ).andExpectAll(status().isOk());

                mockMvc.perform(post("/promo-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "code": "PROMO1",
                            "expirationDate": "2025-12-31",
                            "discountAmount": 10.0,
                            "currency": "EUR",
                            "uses": 1,
                            "type": "FIXED"
                        }
                        """))
                        .andExpectAll(status().isOk());

                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 1,
                            "currencyCode": "EUR",
                            "promoCode": "PROMO1"
                        }
                        """))
                        .andExpectAll(
                                status().isOk(),
                                jsonPath("$.message").value("Purchase successful"),
                                jsonPath("$.purchase.productId").value(1),
                                jsonPath("$.purchase.regularPrice").value(90),
                                jsonPath("$.purchase.discountApplied").value(10),
                                jsonPath("$.purchase.currencyCode").value("EUR")
                        );
            } catch (Exception e) {
                fail(e);
            }
        }

        // Test 6: Test sales report for multiple purchases
            // 1. Create a product with a price of 100 USD
            // 2. Create a promo code with a discount of 10 USD, 1 use
            // 3. Finalize the purchase with the product and the promo code
            // 4. Finalize the purchase with the product and without the promo code
            // 5. Check sales report
            // 6. Create a product with a price of 90 EUR
            // 7. Finalize the purchase with the product
            // 8. Check sales report
            // 9. Create a product with a price of 80 USD
            // 10. Finalize the purchase with the product
            // 11. Check sales report

        @Test
        public void purchaseScenario7() {
            try {
                mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""

                                {
                                "name": "Product1",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "USD"
                                    }
                                ]
                                }
                            """)
                ).andExpectAll(status().isOk());

                mockMvc.perform(post("/promo-code")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""

                                        {
                            "code": "PROMO1",
                            "expirationDate": "2025-12-31",
                            "discountAmount": 10.0,
                            "currency": "USD",
                            "uses": 1,
                            "type": "FIXED"
                        }
                        """))
                        .andExpectAll(status().isOk());

                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 1,
                            "currencyCode": "USD",
                            "promoCode": "PROMO1"
                        }
                        """))
                        .andExpect(status().isOk());

                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 1,
                            "currencyCode": "USD"
                        }
                        """))
                        .andExpect(status().isOk());

                mockMvc.perform(get("/purchase/report"))
                        .andExpectAll(
                                status().isOk(),
                                jsonPath("$.entries[0].currency").value("USD"),
                                jsonPath("$.entries[0].totalAmount").value(200),
                                jsonPath("$.entries[0].totalDiscount").value(10),
                                jsonPath("$.entries[0].numberOfPurchases").value(2)
                        );

                mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Product2",
                            "prices": [
                                {
                                    "price": 90.0,
                                    "currency": "EUR"
                                }
                            ]
                        }
                        """))
                        .andExpectAll(status().isOk());

                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 2,
                            "currencyCode": "EUR"
                        }
                        """))
                        .andExpect(status().isOk());

                mockMvc.perform(get("/purchase/report"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$..entries[?(@.currency=='USD')]").exists())
                        .andExpect(jsonPath("$..entries[?(@.currency=='EUR')]").exists());

                mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Product3",
                            "prices": [
                                {
                                    "price": 80.0,
                                    "currency": "USD"
                                }
                            ]
                        }
                        """))
                        .andExpectAll(status().isOk());

                mockMvc.perform(post("/purchase/finalize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "productId": 3,
                            "currencyCode": "USD"
                        }
                        """))
                        .andExpect(status().isOk());

                mockMvc.perform(get("/purchase/report"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$..entries[?(@.currency=='USD')]").exists())
                        .andExpect(jsonPath("$..entries[?(@.currency=='EUR')]").exists());
            } catch (Exception e) {
                fail(e);
            }
            }

            // Test 7: Test purchase controller with PERCENTAGE BASED promo code
                // 1. Create a product with a price of 125 USD
                // 2. Create a promo code with a discount of 15%, 1 use
                // 3. Finalize the purchase with the product and the promo code

            @Test
            public void purchaseScenario8() {
                try {
                    mockMvc.perform(post("/product")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""

                                    {
                                    "name": "Product1",
                                    "prices": [
                                        {
                                            "price": 125.0,
                                            "currency": "USD"
                                        }
                                    ]
                                }
                                """)
                    ).andExpectAll(status().isOk());

                    mockMvc.perform(post("/promo-code")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "code": "PROMO1",
                                "expirationDate": "2025-12-31",
                                "discountAmount": 15.0,
                                "currency": "USD",
                                "uses": 1,
                                "type": "PERCENTAGE"
                            }
                            """))
                            .andExpectAll(status().isOk());

                    mockMvc.perform(post("/purchase/finalize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "productId": 1,
                                "currencyCode": "USD",
                                "promoCode": "PROMO1"
                            }
                            """))
                            .andExpectAll(
                                    status().isOk(),
                                    jsonPath("$.message").value("Purchase successful"),
                                    jsonPath("$.purchase.productId").value(1),
                                    jsonPath("$.purchase.regularPrice").value(125),
                                    jsonPath("$.purchase.discountApplied").value(18.75),
                                    jsonPath("$.purchase.currencyCode").value("USD")
                            );
                } catch (Exception e) {
                    fail(e);
                }
            }

            // Test 8: Test purchase controller with PERCENTAGE BASED promo code and multiple uses
                // 1. Create a product with a price of 172 USD
                // 2. Create a promo code with a discount of 20%, 2 uses
                // 3. Finalize the purchase with the product and the promo code twice
                // 4. Check if promo code usage increased by 2, and available uses decreased by 2
                // 5. Check if promo code is not usable anymore

            @Test
            public void purchaseScenario9() {
                try {
                    mockMvc.perform(post("/product")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""

                                    {
                                    "name": "Product1",
                                    "prices": [
                                        {
                                            "price": 172.0,
                                            "currency": "USD"
                                        }
                                    ]
                                    }
                                """)
                    ).andExpectAll(status().isOk());

                    mockMvc.perform(post("/promo-code")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""

                                            {
                                "code": "PROMO1",
                                "expirationDate": "2025-12-31",
                                "discountAmount": 20.0,
                                "currency": "USD",
                                "uses": 2,
                                "type": "PERCENTAGE"
                            }
                            """))
                            .andExpectAll(status().isOk());

                    mockMvc.perform(post("/purchase/finalize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "productId": 1,
                                "currencyCode": "USD",
                                "promoCode": "PROMO1"
                            }
                            """))
                            .andExpect(status().isOk());

                    mockMvc.perform(post("/purchase/finalize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "productId": 1,
                                "currencyCode": "USD",
                                "promoCode": "PROMO1"
                            }
                            """))
                            .andExpect(status().isOk());

                    mockMvc.perform(get("/promo-code/PROMO1"))
                            .andExpectAll(
                                    status().isOk(),
                                    jsonPath("$.promoCodes[0].code").value("PROMO1"),
                                    jsonPath("$.promoCodes[0].discountAmount").value(20),
                                    jsonPath("$.promoCodes[0].currency").value("USD"),
                                    jsonPath("$.promoCodes[0].usesLeft").value(0),
                                    jsonPath("$.promoCodes[0].timesUsed").value(2)
                            );

                    mockMvc.perform(post("/purchase/finalize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "productId": 1,
                                "currencyCode": "USD",
                                "promoCode": "PROMO1"
                            }
                            """))
                            .andExpectAll(
                                    status().isBadRequest(),
                                    jsonPath("$.message").value("Error while finalizing purchase: Error while getting discount price: Promo code has no uses left")
                            );
                } catch (Exception e) {
                    fail(e);
                }
                }
}
