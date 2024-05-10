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
                            "uses": 1
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
                                jsonPath("$.message").value("Promo code uses limit reached")
                        );
            } catch (Exception e) {
                fail(e);
            }
        }

        // Test 4: Test purchase controller with invalid promo code
            // 1. Create a product with a price of 100 USD
            // 2. Try to finalize the purchase with promo code for different currency

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
                            "uses": 1
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
                                jsonPath("$.message").value("Promo code currency does not match purchase currency")
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
                            "uses": 1
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
}
