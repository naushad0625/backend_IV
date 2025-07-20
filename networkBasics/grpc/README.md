# gRPC Integration Demo: NestJS & Spring Boot

## 📘 Overview

This project demonstrates a simple **gRPC communication** setup between two microservices:

- 🛒 `order-service`: Built with **NestJS**
- 💳 `payment-service`: Built with **Spring Boot (Java)**

The primary objective of this project is to validate that **gRPC works correctly across different technology stacks** (Node.js and Java).

---

## 🚀 Services

### 1. Order Service (NestJS)

- **Language**: TypeScript
- **Framework**: NestJS
- **Role**: Sends a gRPC request to the payment service upon receiving an order via a RESTful POST endpoint.

### 2. Payment Service (Spring Boot)

- **Language**: Java
- **Framework**: Spring Boot
- **Role**: Receives gRPC requests and responds with a payment confirmation.

---

## 🔁 Flow

1. A client sends a `POST /order` request to the **order-service**.
2. The **order-service** makes a gRPC call to the **payment-service**.
3. The **payment-service** processes the request and returns a response.
4. The **order-service** returns the result back to the client.

---

## 🛠 Tech Stack

- **NestJS** for the order service (TypeScript, Node.js)
- **Spring Boot** with gRPC for the payment service (Java)
- **Protocol Buffers (protobuf)** for service definition
- **gRPC** for inter-service communication

---

## 📥 How to Run

### 1. Start Payment Service (Spring Boot)

```bash
cd payment
./gradlew clean generateProto build bootRun --refresh-dependencies
```

### 2. Start Order Service (NestJS)

```bash
cd order
pnpm install
pnpm start
```

## 🧪 Test the API

POST http://localhost:3000/order
Content-Type: application/json

{
  "orderId": "123",
  "amount": 500
}

{
    "success": true,
    "transactionId": "01ddd1ac-98ca-4676-a46f-6c2354ba8683",
    "message": "Payment successful for order abc122"
}
