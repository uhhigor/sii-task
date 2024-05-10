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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductTests {

    @Autowired
    private MockMvc mockMvc;

    // Tests for product logic

    // Test 1:
        // 1. Create a product with name "Product1" and price 100 USD
        // 2. Get all products, check if the product is in the list
        // 3. Create a product with name "Product2" and price 200 USD, and price 195 EUR, and description "Example description"
        // 4. Get all products, check if the products are in the list
        // 5. Get product with id 1, check if the product is the same as Product1
        // 6. Get product with id 2, check if the product is the same as Product2
        // 7. Get product with id 3, check if the product is not found
        // 8. Update product with id 1, change name to "Product1-2", check if the product is updated
        // 9. Update product with id 2, add price 300 PLN, check if the product is updated
    @Test
    void testCreateProduct1() {
        // 1. Create a product with name "Product1" and price 100 USD
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
            fail(e);
        }

        // 2. Get all products, check if the product is in the list
        try {
            mockMvc.perform(get("/product"))
                    .andExpectAll(status().isOk(),
                            content().json("""
                            
                                    {
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
            fail(e);
        }
        // 3. Create a product with name "Product2" and price 200 USD, and price 195 EUR, and description "Example description"
            try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""

                            {
                            "name": "Product2",
                            "description": "Example description",
                            "prices": [
                                {
                                    "price": 200.0,
                                    "currency": "USD"
                                },
                                {
                                    "price": 195.0,
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
                                    "id": 2,
                                    "name": "Product2",
                                    "description": "Example description",
                                    "prices": [
                                        {
                                            "price": 200.0,
                                            "currency": "USD"
                                        },
                                        {
                                            "price": 195.0,
                                            "currency": "EUR"
                                        }
                                    ]
                                }
                            ]
                        }
                        """));
        } catch (Exception e) {
            fail(e);
        }
        // 4. Get all products, check if the products are in the list
        try {
            mockMvc.perform(get("/product"))
                    .andExpectAll(status().isOk(),
                            content().json("""
                            
                                    {
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
                                    },
                                    {
                                        "id": 2,
                                        "name": "Product2",
                                        "description": "Example description",
                                        "prices": [
                                            {
                                                "price": 200.0,
                                                "currency": "USD"
                                            },
                                            {
                                                "price": 195.0,
                                                "currency": "EUR"
                                            }
                                        ]
                                    }
                                ]
                            }
                            """));
        } catch (Exception e) {
            fail(e);
        }
        // 5. Get product with id 1, check if the product is the same as Product1
        try {
            mockMvc.perform(get("/product/1"))
                    .andExpectAll(status().isOk(),
                            content().json("""
                            
                                    {
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
            fail(e);
        }
        // 6. Get product with id 2, check if the product is the same as Product2
        try {
            mockMvc.perform(get("/product/2"))
                    .andExpectAll(status().isOk(),
                            content().json("""
                            
                                    {
                                "products":
                                [
                                    {
                                        "id": 2,
                                        "name": "Product2",
                                        "description": "Example description",
                                        "prices": [
                                            {
                                                "price": 200.0,
                                                "currency": "USD"
                                            },
                                            {
                                                "price": 195.0,
                                                "currency": "EUR"
                                            }
                                        ]
                                    }
                                ]
                            }
                            """));
        } catch (Exception e) {
            fail(e);
        }

        // 7. Get product with id 3, check if the product is not found
        try {
            mockMvc.perform(get("/product/3"))
                    .andExpectAll(status().isNotFound());
        } catch (Exception e) {
            fail(e);
        }

        // 8. Update product with id 1, change name to "Product1-2", check if the product is updated
        try {
            mockMvc.perform(put("/product/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""

                            {
                            "name": "Product1-2",
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
                            "message": "Product updated successfully",
                            "products":
                            [
                                {
                                    "id": 1,
                                    "name": "Product1-2",
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
            fail(e);
        }

        // 9. Update product with id 2, add price 300 PLN, check if the product is updated

        try {
            mockMvc.perform(put("/product/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""

                            {
                            "name": "Product2",
                            "description": "Example description",
                            "prices": [
                                {
                                    "price": 200.0,
                                    "currency": "USD"
                                },
                                {
                                    "price": 195.0,
                                    "currency": "EUR"
                                },
                                {
                                    "price": 300.0,
                                    "currency": "PLN"
                                }
                            ]
                        }
                        """)
                ).andExpectAll(status().isOk(), content()
                        .json("""
                             {
                            "message": "Product updated successfully",
                            "products":
                            [
                                {
                                    "id": 2,
                                    "name": "Product2",
                                    "description": "Example description",
                                    "prices": [
                                        {
                                            "price": 200.0,
                                            "currency": "USD"
                                        },
                                        {
                                            "price": 195.0,
                                            "currency": "EUR"
                                        },
                                        {
                                            "price": 300.0,
                                            "currency": "PLN"
                                        }
                                    ]
                                }
                            ]
                        }
                        """));
        } catch (Exception e) {
            fail(e);
        }

    }

    // Test 2.
        // 1. Create a product without a name, check if the product is not created
        // 2. Create a product without a price, check if the product is not created
        // 3. Create a product with a negative price, check if the product is not created
        // 4. Create a product with invalid currency, check if the product is not created

    @Test
    void testCreateProduct2() {
        // 1. Create a product without a name, check if the product is not created
        try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""

                            {
                            "prices": [
                                {
                                    "price": 100.0,
                                    "currency": "USD"
                                }
                            ]
                        }
                        """)
                ).andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                    "message": "Error while adding new product: Name cannot be empty",
                                    "products": null
                                }
                                """));
        } catch (Exception e) {
            fail(e);
        }

        // 2. Create a product without a price, check if the product is not created
        try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                            "name": "Product1"
                            }
                        """)
                ).andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                    "message": "Error while adding new product: Prices cannot be null or empty",
                                    "products": null
                                }
                                """));
        } catch (Exception e) {
            fail(e);
        }

        // 3. Create a product with a negative price, check if the product is not created
        try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""

                            {
                            "name": "Product1",
                            "prices": [
                                {
                                    "price": -100.0,
                                    "currency": "USD"
                                }
                            ]
                        }
                        """)
                ).andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                    "message": "Error while adding new product: Error creating ProductPrice: Price must be greater than 0",
                                    "products": null
                                }
                                """));
        } catch (Exception e) {
            fail(e);
        }

        // 4. Create a product with invalid currency, check if the product is not created
        try {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""

                            {
                            "name": "Product1",
                            "prices": [
                                {
                                    "price": 100.0,
                                    "currency": "USDDDD"
                                }
                            ]
                            }
                        """)
                ).andExpectAll(status().isBadRequest(),
                        content().json("""
                                {
                                    "message": "Error while adding new product: Error creating ProductPrice: Invalid currency code: USDDDD",
                                    "products": null
                                }
                                """));
        } catch (Exception e) {
            fail(e);
        }
    }





}
