# 🚀 First Quarkus API

This is a demonstration project based on **Quarkus** (a Java framework optimized for Kubernetes and GraalVM), implementing a RESTful API for managing products and customers.

The project is built on a microservices architecture: it acts as a client that retrieves necessary data from another service using the high-performance **gRPC** protocol.

## 🛠 Tech Stack
*   **Java 21**
*   **Quarkus** (RESTEasy Reactive)
*   **gRPC** (for inter-service communication)
*   **Maven** (build tool)
*   **SmallRye OpenAPI** (automated Swagger documentation)

## 🔄 Inter-service Communication
This service uses a **gRPC client** to communicate with backend services. This provides:
*   Low latency for data transfer.
*   Strong typing thanks to Protocol Buffers.
*   High performance for inter-service requests.

## 🛰 API Endpoints (REST)

All resources are grouped under API version `v1`.

### 📦 Products



| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/api/v1/products` | Get a list of all products |
| **GET** | `/api/v1/products/{id}` | Get a product by its **ID** |
| **POST** | `/api/v1/products` | Create a new product |
| **PUT** | `/api/v1/products` | Update an existing product |
| **DELETE** | `/api/v1/products/{id}` | Delete a product by its **ID** |

### 👥 Customers



| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/api/v1/customers` | Get a list of all customers |
| **GET** | `/api/v1/customers/{id}` | Get customer data by **ID** |
| **POST** | `/api/v1/customers` | Register a new customer |
| **PUT** | `/api/v1/customers` | Update customer data |
| **DELETE** | `/api/v1/customers/{id}` | Delete a customer by **ID** |

## 🚀 Getting Started

### Development Mode
Run the application with **Live Coding** support (changes are applied instantly):
```shell
./mvnw compile quarkus:dev
```
### 🚀 Build and Run (JVM Mode)

To create a standard JAR file and run it, use the following commands:

```shell
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

## 📖 Documentation

Once the project is running, you can use the interactive console to test requests:

*   **Swagger UI**: [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui) — a visual interface for API testing.
*   **OpenAPI Spec**: [http://localhost:8080/q/openapi](http://localhost:8080/q/openapi) — raw API specification in YAML format.
