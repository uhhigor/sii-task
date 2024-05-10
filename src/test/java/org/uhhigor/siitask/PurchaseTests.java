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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void testPurchaseControllerWithValidPurchase() {
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
}
