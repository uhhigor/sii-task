package org.uhhigor.siitask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.util.AssertionErrors.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DiscountTests {

    @Autowired
    private MockMvc mockMvc;

    // Tests for discount logic

    // Test 1:
        // 1. Create a product with a price of 100 USD
        // 2. Create a promo code with a discount of 10 USD
        // 3. Get the discount price for the product with the promo code
    @Test
    void discountPriceScenario1() {
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
                    ).andExpectAll(status().isOk(), content()
                            .json("""
                            {
                                "message": "Product added successfully",
                                "products":
                                [
                                    {
                                        "id": 1,
                                        "name": "Product1",
                                        "description": null,
                                        "prices": [
                                            {
                                                "price": 100.0,
                                                "currency": "USD"
                                            }
                                        ]
                                    }
                                ]
                            }
                            """));
        } catch (Exception e) {
            fail("Failed to create product, error: " + e.getMessage());
        }
            try {
            mockMvc.perform(post("/promo-code")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "code": "TESTCODE",
                                "expirationDate": "2025-01-01",
                                "discountAmount": 10.0,
                                "currency": "USD",
                                "uses": 10
                            }
                            """)
                    ).andExpectAll(
                            status().isOk(),
                            content().json("""
                                    {
                                        "message": "Promo code added successfully",
                                        "promoCode": {
                                            "code": "TESTCODE",
                                            "expirationDate": "2025-01-01T00:00:00.000+00:00",
                                            "discountAmount": 10.0,
                                            "currency": "USD",
                                            "usesLeft": 10,
                                            "timesUsed": 0
                                        }
                                    }
                                    """)
                            );
            } catch (Exception e) {
                fail("Failed to create promo code, error: " + e.getMessage());
            }
            try {
            mockMvc.perform(post("/discount")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "productId": 1,
                                        "code": "TESTCODE",
                                        "currencyCode": "USD"
                                    }
                                    """)
                            )
                    .andExpectAll(
                            status().isOk(),
                            content().json("""
                                    {
                                        "message": null,
                                        "discountPrice": {
                                            "price": 90.0,
                                            "currency": "USD"
                                        }
                                    }
                                    """)
                            );
        } catch (Exception e) {
            fail("Failed to get discount price, error: " + e.getMessage());
        }
    }

    // Test 2:
        // 1. Create a product with a price of 100 USD
        // 2. Create a promo code with a discount of 110 USD
        // 3. Get the discount price for the product with the promo code
    @Test
    void discountPriceScenario2() {
        try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "name": "Product",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "USD"
                                    }
                                ]
                            }
                            """)
                    ).andExpectAll(status().isOk(), content()
                            .json("""
                            {
                                "message": "Product added successfully",
                                "products":
                                [
                                    {
                                        "id": 1,
                                        "name": "Product",
                                        "description": null,
                                        "prices": [
                                            {
                                                "price": 100.0,
                                                "currency": "USD"
                                            }
                                        ]
                                    }
                                ]
                            }
                            """));
        } catch (Exception e) {
            fail("Failed to create product, error: " + e.getMessage());
        }

        try {
            mockMvc.perform(post("/promo-code")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "code": "TESTCODE",
                                "expirationDate": "2025-01-01",
                                "discountAmount": 110.0,
                                "currency": "USD",
                                "uses": 10
                            }
                            """)
                    ).andExpectAll(
                            status().isOk(),
                            content().json("""
                                    {
                                        "message": "Promo code added successfully",
                                        "promoCode": {
                                            "code": "TESTCODE",
                                            "expirationDate": "2025-01-01T00:00:00.000+00:00",
                                            "discountAmount": 110.0,
                                            "currency": "USD",
                                            "usesLeft": 10,
                                            "timesUsed": 0
                                        }
                                    }
                                    """)
                            );
        } catch (Exception e) {
            fail("Failed to create promo code, error: " + e.getMessage());
        }

        try {
            mockMvc.perform(post("/discount")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "productId": 1,
                                        "code": "TESTCODE",
                                        "currencyCode": "USD"
                                    }
                                    """)
                    )
                    .andExpectAll(
                            status().isOk(),
                            content().json("""
                                    {
                                        "message": null,
                                        "discountPrice": {
                                            "price": 0.0,
                                            "currency": "USD"
                                        }
                                    }
                                    """)
                            );
        } catch (Exception e) {
            fail("Failed to get discount price, error: " + e.getMessage());
        }
    }

    // Test 3:
        // 1. Create a product with a price of 100 EUR
        // 2. Create a promo code with a discount of 10 USD
        // 3. Get the discount price for the product with the promo code
    @Test
    void discountPriceScenario3() {
        try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "name": "Product",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "EUR"
                                    }
                                ]
                            }
                            """)
                    ).andExpectAll(status().isOk(), content()
                            .json("""
                            {
                                "message": "Product added successfully",
                                "products":
                                [
                                    {
                                        "id": 1,
                                        "name": "Product",
                                        "description": null,
                                        "prices": [
                                            {
                                                "price": 100.0,
                                                "currency": "EUR"
                                            }
                                        ]
                                    }
                                ]
                            }
                            """));
        } catch (Exception e) {
            fail("Failed to create product, error: " + e.getMessage());
        }

        try {
            mockMvc.perform(post("/promo-code")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "code": "TESTCODE",
                                "expirationDate": "2025-01-01",
                                "discountAmount": 10.0,
                                "currency": "USD",
                                "uses": 10
                            }
                            """)
                    ).andExpectAll(
                            status().isOk(),
                            content().json("""
                                    {
                                        "message": "Promo code added successfully",
                                        "promoCode": {
                                            "code": "TESTCODE",
                                            "expirationDate": "2025-01-01T00:00:00.000+00:00",
                                            "discountAmount": 10.0,
                                            "currency": "USD",
                                            "usesLeft": 10,
                                            "timesUsed": 0
                                        }
                                    }
                                    """)
                            );
        } catch (Exception e) {
            fail("Failed to create promo code, error: " + e.getMessage());
        }

        try {
            mockMvc.perform(post("/discount")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "productId": 1,
                                "code": "TESTCODE",
                                "currencyCode": "EUR"
                            }
                            """)
                    )
                    .andExpectAll(
                            status().isBadRequest(),
                            content().json("""
                                    {
                                        "message": "Product currency does not match promo code currency",
                                        "discountPrice": {
                                            "price": 100.0,
                                            "currency": "EUR"
                                        }
                                    }
                                    """)
                            );
        } catch (Exception e) {
            fail("Failed to get discount price, error: " + e.getMessage());
        }
    }



}
