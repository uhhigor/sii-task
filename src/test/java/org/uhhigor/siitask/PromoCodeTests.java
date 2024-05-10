package org.uhhigor.siitask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PromoCodeTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void promoCodeTest1() throws Exception {
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
                                    "promoCodes": [{
                                        "code": "TESTCODE",
                                        "expirationDate": "2025-01-01T00:00:00.000+00:00",
                                        "discountAmount": 10.0,
                                        "currency": "USD",
                                        "usesLeft": 10,
                                        "timesUsed": 0
                                    }]
                                }
                                """)
                        );
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
                status().isBadRequest(),
                content().json("""
                        {
                            "message": "Promo code with this code already exists",
                            "promoCodes": null
                        }
                        """)
        );

        mockMvc.perform(get("/promo-code/TESTCODE"))
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                                {
                                    "message": null,
                                    "promoCodes": [{
                                        "code": "TESTCODE",
                                        "expirationDate": "2025-01-01T00:00:00.000+00:00",
                                        "discountAmount": 10.0,
                                        "currency": "USD",
                                        "usesLeft": 10,
                                        "timesUsed": 0
                                    }
                                    ]
                                }
                                """)
                );
        mockMvc.perform(get("/promo-code/TESTCODE6"))
                .andExpect(status().isNotFound());
    }
    @Test
    public void promoCodeTest2() throws Exception {
        mockMvc.perform(post("/promo-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "code": "TESTCODE3",
                            "expirationDate": "2025-01-01",
                            "discountAmount": 10.0,
                            "currency": "USD",
                            "uses": 0
                        }
                        """)
                ).andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                                {
                                    "message": "Error while creating promo code: Uses must be greater than 0",
                                    "promoCodes": null
                                }
                                """)
                        );
    }
    @Test
    public void promoCodeTest3() throws Exception {
        mockMvc.perform(post("/promo-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "code": "TESTCODE4",
                            "expirationDate": "2025-01-01",
                            "discountAmount": 0.0,
                            "currency": "USD",
                            "uses": 10
                        }
                        """)
                ).andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                                {
                                    "message": "Error while creating promo code: Discount amount must be greater than 0",
                                    "promoCodes": null
                                }
                                """)
                        );
    }

    @Test
    public void promoCodeTest4() throws Exception {
        mockMvc.perform(post("/promo-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "code": "TESTCODE5",
                            "expirationDate": "2022-01-01",
                            "discountAmount": 10.0,
                            "currency": "USD",
                            "uses": 10
                        }
                        """)
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                                {
                                    "message": "Error while creating promo code: Expiration date cannot be null or in the past",
                                    "promoCodes": null
                                }
                                """)
                        );
    }
}
