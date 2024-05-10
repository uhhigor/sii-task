# Igor Bobrukiewicz - SII TASK FOR LAT
## igor.bobrukiewicz@gmail.com
## 666 173 216

## 1. About
Java version: 21
### 1.1. How to run
Run Spring Boot Application in 'src/main/java/org/uhhigor/siitask/SiiTaskApplication.java' (class 'SiiTaskApplication')

### 1.2. How to test
Run all JUnit tests in folder 'src/test/java/org/uhhigor/siitask'

### 1.3. How to use
To use my application I used Postman and integrated IntelliJ IDEA HTTP Client.

## 2. Endpoints
### Product
### 2.1. POST http://localhost:8080/product
#### Description: Create a new product
#### Example request body
```json
{
    "name": "Mandatory name",
    "description": "Optional description",
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
```
#### Example response body
```json
{
    "message": "Product added successfully",
    "products": [
        {
            "id": 1,
            "name": "Mandatory name",
            "description": "Optional description",
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
    ]
}
```

### 2.2. GET http://localhost:8080/product
#### Description: Get all products
#### Example response body
```json
{
  "message": "1 products found",
  "products": [
    {
      "id": 1,
      "name": "Mandatory name",
      "description": "Optional description",
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
  ]
}
```

### 2.3. GET http://localhost:8080/product/{id}
#### Description: Get product by id
#### Example response body
```json
{
  "message": null,
  "products": [
    {
      "id": 1,
      "name": "Mandatory name",
      "description": "Optional description",
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
  ]
}
```

### 2.4. PUT http://localhost:8080/product/{id}
#### Description: Update product by id
#### Example request body
```json
{
    "name": "Updated name",
    "description": "Updated description",
    "prices": [
        {
            "price": 200.0,
            "currency": "USD"
        },
        {
            "price": 180.0,
            "currency": "EUR"
        }
    ]
}
```
#### Example response body
```json
{
  "message": "Product updated successfully",
  "products": [
    {
      "id": 1,
      "name": "Updated name",
      "description": "Updated description",
      "prices": [
        {
          "price": 200.0,
          "currency": "USD"
        },
        {
          "price": 180.0,
          "currency": "EUR"
        }
      ]
    }
  ]
}
```

### 2.5. DELETE http://localhost:8080/product/{id}
#### Description: Delete product by id
#### Example response body
```json
{
  "message": "Product deleted successfully",
  "products": null
}
```
### Promo Code
### 2.6. POST http://localhost:8080/promo-code
#### Description: Create a new promo code
#### Example request body
```json
{
  "code": "ABCD",
  "expirationDate": "2024-12-12",
  "discountAmount": 10,
  "currency": "USD",
  "uses": 5,
  "type": "FIXED"
}
```
Available types: FIXED, PERCENTAGE
Available currencies: All currencies available in java.util.Currency

#### Example response body
```json
{
  "message": "Promo code added successfully",
  "promoCodes": [
    {
      "code": "ABCD",
      "expirationDate": "2024-12-12T00:00:00.000+00:00",
      "discountAmount": 10.0,
      "currency": "USD",
      "usesLeft": 5,
      "timesUsed": 0,
      "type": "FIXED"
    }
  ]
}
```

### 2.7. GET http://localhost:8080/promo-code
#### Description: Get all promo codes
#### Example response body
```json
{
  "message": "1 promo codes found",
  "promoCodes": [
    {
      "code": "ABCD",
      "expirationDate": "2024-12-12T00:00:00.000+00:00",
      "discountAmount": 10.0,
      "currency": "USD",
      "usesLeft": 5,
      "timesUsed": 0,
      "type": "FIXED"
    }
  ]
}
```

### 2.8. GET http://localhost:8080/promo-code/{code}
#### Description: Get promo code details
#### Example response body
```json
{
  "message": null,
  "promoCodes": [
    {
      "code": "ABCD",
      "expirationDate": "2024-12-12T00:00:00.000+00:00",
      "discountAmount": 10.0,
      "currency": "USD",
      "usesLeft": 5,
      "timesUsed": 0,
      "type": "FIXED"
    }
  ]
}
```

### Discount

### 2.9. POST http://localhost:8080/discount
#### Description: Get discount price for product
#### Example request body
```json
{
  "productId": 1,
  "code": "ABCD",
  "currencyCode": "USD"
}
```

#### Example response body
```json
{
  "message": null,
  "discountPrice": {
    "price": 90.0,
    "currency": "USD"
  }
}
```

### Purchase

### 2.10. POST http://localhost:8080/purchase/finalize
#### Description: Simulate purchase
#### Example request body
```json
{
  "productId": 1,
  "currencyCode": "USD",
  "promoCode": "ABCD"
}
```

#### Example response body
```json
{
  "message": "Purchase successful",
  "purchase": {
    "date": "2024-05-10T14:18:46.569+00:00",
    "productId": 1,
    "regularPrice": 100.0,
    "discountApplied": 10.0,
    "currencyCode": "USD"
  }
}
```
### 2.11. GET http://localhost:8080/purchase/report
#### Description: Get sales report
#### Example response body
```json
{
  "entries": [
    {
      "currency": "USD",
      "totalAmount": 600.0,
      "totalDiscount": 50.0,
      "numberOfPurchases": 6
    },
    {
      "currency": "EUR",
      "totalAmount": 180.0,
      "totalDiscount": 0.0,
      "numberOfPurchases": 2
    }
  ]
}
```