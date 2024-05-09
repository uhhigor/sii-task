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
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateProduct1() throws Exception {
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

        }
        @Test
        void testCreateProduct2() throws Exception {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "name": "Product2",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "NON EXISTENT CURRENCY"
                                    }
                                ]
                            }
                            """)
            ).andExpectAll(status().isBadRequest(), content()
                    .json("""

                         {
                                        "message": "Failed to add product: Error while adding new product price: Invalid currency code: NON EXISTENT CURRENCY",
                                        "products": []
                                    }
                                    """));

        }
        @Test
        void testCreateProduct3() throws Exception {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "name": "Product2",
                                "prices": [
                                    {
                                        "price": -50,
                                        "currency": "USD"
                                    }
                                ]
                            }
                            """)
            ).andExpectAll(status().isBadRequest(), content()
                    .json("""
                         {
                                        "message": "Failed to add product: Error while adding new product price: Price must be greater than 0",
                                        "products": []
                                    }
                                    """));

        }
        @Test
        void testCreateProduct4() throws Exception {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "name": "Product2",
                                "prices": [
                                ]
                            }
                            """)
            ).andExpectAll(status().isBadRequest(), content()
                    .json("""

                         {
                                        "message": "Failed to add product: Error while adding new product: Prices cannot be null or empty",
                                        "products": []
                                    }
                                    """));

        }

        @Test
        void testCreateProduct5() throws Exception {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "name": "Product5",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "USD"
                                    },
                                    {
                                        "price": 85.0,
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
                                                "name": "Product5",
                                                "description": null,
                                                "prices": [
                                                    {
                                                        "price": 100.0,
                                                        "currency": "USD"
                                                    },
                                                    {
                                                        "price": 85.0,
                                                        "currency": "EUR"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                    """));

        }
        @Test
        void testCreateProduct6() throws Exception {
            mockMvc.perform(post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "name": "Product6",
                                "description": "Description6",
                                "prices": [
                                    {
                                        "price": 100.0,
                                        "currency": "USD"
                                    },
                                    {
                                        "price": 85.0,
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
                                                "id": 3,
                                                "name": "Product6",
                                                "description": "Description6",
                                                "prices": [
                                                    {
                                                        "price": 100.0,
                                                        "currency": "USD"
                                                    },
                                                    {
                                                        "price": 85.0,
                                                        "currency": "EUR"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                    """));

        }
        @Test
        void testGetProduct1() throws Exception {
            mockMvc.perform(get("/product/1"))
                    .andExpectAll(status().isOk(), content()
                            .json("""
                                 {
                                        "message": "Product found",
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
        }
        @Test
        void testGetProduct2() throws Exception {
            mockMvc.perform(get("/product/2"))
                    .andExpectAll(status().isOk(), content()
                            .json("""
                                 {
                                        "message": "Product found",
                                        "products":
                                        [
                                            {
                                                "id": 2,
                                                "name": "Product5",
                                                "description": null,
                                                "prices": [
                                                    {
                                                        "price": 100.0,
                                                        "currency": "USD"
                                                    },
                                                    {
                                                        "price": 85.0,
                                                        "currency": "EUR"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                    """));
        }
        @Test
        void testGetProduct3() throws Exception {
            mockMvc.perform(get("/product/3"))
                    .andExpectAll(status().isOk(), content()
                            .json("""
                                 {
                                        "message": "Product found",
                                        "products":
                                        [
                                            {
                                                "id": 3,
                                                "name": "Product6",
                                                "description": "Description6",
                                                "prices": [
                                                    {
                                                        "price": 100.0,
                                                        "currency": "USD"
                                                    },
                                                    {
                                                        "price": 85.0,
                                                        "currency": "EUR"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                    """));
        }
        @Test
        void testGetProduct4() throws Exception {
            mockMvc.perform(get("/product/4"))
                    .andExpectAll(status().isNotFound());
        }

        @Test
        void testGetProducts() throws Exception {
            mockMvc.perform(get("/product"))
                    .andExpectAll(status().isOk(), content()
                            .json("""
                                 {
                                        "message": "3 products found",
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
                                                "name": "Product5",
                                                "description": null,
                                                "prices": [
                                                    {
                                                        "price": 100.0,
                                                        "currency": "USD"
                                                    },
                                                    {
                                                        "price": 85.0,
                                                        "currency": "EUR"
                                                    }
                                                ]
                                            },
                                            {
                                                "id": 3,
                                                "name": "Product6",
                                                "description": "Description6",
                                                "prices": [
                                                    {
                                                        "price": 100.0,
                                                        "currency": "USD"
                                                    },
                                                    {
                                                        "price": 85.0,
                                                        "currency": "EUR"
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                    """));
        }
}

