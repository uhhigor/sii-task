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
public class PromoCodeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreatePromoCode1() throws Exception {
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
    }
}
