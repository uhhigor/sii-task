package org.uhhigor.siitask;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ControllersTests {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class ProductControllerTests {
        @Test
        void testCreateProduct() throws Exception {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Product1\", \"price\": 100.0, \"currency\": \"USD\"}"))
                    .andExpectAll(status().isOk(), content()
                            .json("""
                                    {
                                        "message": "Product created successfully",
                                        "product":{
                                            "id": 1,
                                            "name": "Product1",
                                            "prices": [
                                                {
                                                    "price": 100.0,
                                                    "currency": "USD"
                                                }
                                            ]
                                        }
                                    }
                                    """));

        }
    }

}
