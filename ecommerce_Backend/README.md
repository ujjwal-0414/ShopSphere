ShopSphere — Spring Boot E-Commerce Backend

ShopSphere is a RESTful e-commerce backend built with Java 21, Spring Boot, Spring Security, JWT, Spring Data JPA/Hibernate and PostgreSQL.

The project is designed as an intermediate-level backend application and focuses on concepts that are important in real backend development: authentication and authorization, DTO-based API design, validation, centralized exception handling, transactional business logic, inventory consistency under concurrency, JPA fetch planning, order lifecycle management and clean separation of responsibilities.

Payment note: ShopSphere does not integrate a real payment gateway. The payment module is intentionally simulated for learning and portfolio purposes. A successful payment request creates a payment record with a generated transaction ID and moves a pending order to CONFIRMED. No real money is transferred and no card/UPI credentials are processed by this application.

1. Highlights

REST API built with Spring Boot

Java 21

PostgreSQL persistence with Spring Data JPA / Hibernate

Layered architecture: Controller → Service → Repository

Request/response DTOs to avoid exposing JPA entities directly

Mapper layer for Entity ↔ DTO conversion

JWT-based stateless authentication

BCrypt password hashing

Role-Based Access Control (RBAC) with USER and ADMIN roles

Custom 401 Unauthorized and 403 Forbidden responses

Global exception handling with a consistent ErrorResponse

Bean Validation for request bodies and method parameters

Transactional service methods with @Transactional

Read-only transactions with @Transactional(readOnly = true)

Pessimistic database locking for inventory concurrency

@EntityGraph for intentional eager fetching of selected associations

Lazy loading on relationships where appropriate

Order state-transition validation

Inventory restoration when eligible orders are cancelled

Historical order-item price snapshots

Historical shipping-address snapshots on orders

User-owned cart and address isolation

Product search, category filtering and price filtering

Admin order-management endpoints

CORS configuration for the React frontend

Development seed data in data.sql

2. Technology Stack

Area

Technology

Language

Java 21

Framework

Spring Boot 4.1.0

Web

Spring MVC

Security

Spring Security

Authentication

JWT / JJWT 0.12.6

Password hashing

BCrypt

ORM

Hibernate / Spring Data JPA

Database

PostgreSQL

Validation

Jakarta Bean Validation

Boilerplate reduction

Lombok

Frontend

React + Vite

API client

Axios

Build tool

Maven

3. Architecture

React / Browser
│
│ HTTP + JSON
│ Authorization: Bearer <JWT>
▼
┌──────────────────────────────┐
│ Spring Security Filter Chain │
│  - JWT Authentication Filter │
│  - Authentication Manager    │
│  - RBAC / 401 / 403          │
│  - CORS                      │
└──────────────┬───────────────┘
▼
┌──────────────────────────────┐
│ REST Controllers             │
│ Auth / Product / Cart /      │
│ Address / Order / Payment    │
└──────────────┬───────────────┘
▼
┌──────────────────────────────┐
│ Service Layer                │
│ Business rules + transactions│
│ ownership checks + workflow  │
└──────────────┬───────────────┘
▼
┌──────────────────────────────┐
│ Repository Layer             │
│ Spring Data JPA              │
│ EntityGraph + DB locking     │
└──────────────┬───────────────┘
▼
PostgreSQL Database

The codebase separates responsibilities into controller, service, repository, entity, dto, mapper, security, exception, config and enums packages.

4. Project Structure

ecommerce_Backend/
├── src/main/java/com/ujjwal/ecommerce/
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   ├── dto/
│   │   ├── request/
│   │   └── response/
│   ├── entity/
│   ├── enums/
│   ├── exception/
│   ├── mapper/
│   ├── repository/
│   ├── security/
│   └── service/
│       └── impl/
├── src/main/resources/
│   ├── application.properties
│   └── data.sql
├── src/test/
├── pom.xml
└── README.md

5. Domain Model

Main entities

User

Role

Category

Product

Cart

CartItem

Address

Order

OrderItem

Payment

Relationships

Role 1 ──────────── * User
User 1 ──────────── 1 Cart
Cart 1 ──────────── * CartItem
Product 1 ───────── * CartItem
Category 1 ──────── * Product
User 1 ──────────── * Address
User 1 ──────────── * Order
Order 1 ─────────── * OrderItem
Product 1 ───────── * OrderItem
Order 1 ─────────── 1 Payment

Historical order data

OrderItem stores the purchase-time price and subtotal. This prevents historical orders from changing when the current product price changes.

Order stores shipping-address fields as a snapshot rather than keeping a foreign-key dependency on the mutable Address record. This means changing a user's saved address later does not rewrite an already-created order's shipping destination.

6. Authentication and JWT Security

Authentication is implemented with Spring Security and JWT.

Login flow

POST /api/auth/login
│
▼
AuthenticationManager
│
▼
CustomUserDetailsService
│
▼
UserRepository → PostgreSQL
│
▼
BCrypt password verification
│
▼
JwtService generates signed JWT
│
▼
AuthResponse

For protected requests:

Authorization: Bearer <JWT>
│
▼
JwtAuthenticationFilter
│
├── verify signature
├── validate expiration
├── load user
└── populate SecurityContext

The JWT signing key is read from the environment through JWT_SECRET_KEY; it is not stored in source code.

Passwords are stored using BCrypt hashes rather than plaintext passwords.

Stateless security

The application uses:

SessionCreationPolicy.STATELESS

The server does not maintain a traditional HTTP login session. Authentication is reconstructed from the JWT on each protected request.

7. Role-Based Access Control (RBAC)

The project currently defines:

USER
ADMIN

Roles are converted into Spring Security authorities using the ROLE_ convention.

Examples:

Operation

Access

Register / Login

Public

Browse products

Public

Search/filter products

Public

Create product

ADMIN

Update product

ADMIN

Delete product

ADMIN

Create/update/delete category

ADMIN

Cart

Authenticated user

Addresses

Authenticated user

Orders

Authenticated user

Payments

Authenticated user

Admin order management

ADMIN

The security configuration distinguishes between authentication and authorization:

401 Unauthorized → no valid authenticated user

403 Forbidden → authenticated user lacks the required role/permission

Custom AuthenticationEntryPoint and AccessDeniedHandler return the project's standard error response format.

8. Validation

Request DTOs use Jakarta Validation annotations such as:

@NotBlank

@NotNull

@Email

@Pattern

@Positive

other field constraints defined by the request models

Controllers use @Valid and @Validated where appropriate.

This keeps invalid input from reaching business logic unnecessarily.

Examples of validation handled centrally include:

invalid request bodies

invalid enum values

invalid path/query parameter types

invalid positive IDs

missing request parameters

Bean Validation failures

method parameter validation failures

9. Global Exception Handling

GlobalExceptionHandler uses @RestControllerAdvice to provide consistent API errors.

Handled categories include:

Exception / condition

HTTP status

Resource not found

404

Unauthorized

401

Forbidden

403

Bad request

400

Validation failure

400

Invalid JSON / enum

400

Invalid parameter type

400

Missing request parameter

400

Database constraint violation

409

Conflict

409

Unsupported HTTP method

405

Unexpected server error

500

Responses use a common ErrorResponse structure containing timestamp, status, error, message and request path.

10. Transactions and Data Consistency

Business operations that modify multiple pieces of data are executed transactionally.

Examples include:

creating an order

reducing inventory

cancelling an order

restoring inventory

creating a payment and confirming an order

cart modifications

address/default-address updates

admin order state changes

@Transactional provides an atomic unit of work: if an operation fails, the transaction can roll back instead of leaving partially updated business data.

Read-only operations use @Transactional(readOnly = true) where appropriate.

11. Concurrency Control — Pessimistic Locking

Inventory is one of the most important concurrency-sensitive parts of an e-commerce system.

Without locking, two customers could attempt to purchase the final available units at nearly the same time and both transactions could read the same stock value.

The project uses a repository method with:

@Lock(LockModeType.PESSIMISTIC_WRITE)

and a query that selects the product row for update.

Conceptually, PostgreSQL performs the equivalent of:

SELECT ... FOR UPDATE;

The order transaction therefore:

Read cart
↓
Start transaction
↓
Lock product row
↓
Check current stock
↓
Reduce stock
↓
Create order + order items
↓
Commit transaction
↓
Release lock

A competing transaction attempting to acquire a conflicting lock on the same product must wait for the first transaction to finish.

This protects inventory from common overselling race conditions.

Cancellation

Eligible order cancellations also lock affected product rows before restoring stock.

The current implementation allows cancellation for PENDING and CONFIRMED orders; shipped/delivered/cancelled orders are not cancellable through the implemented workflow.

12. JPA Fetch Strategy and @EntityGraph

Relationships such as User → Role, Product → Category, Order → Items and OrderItem → Product use lazy loading where appropriate to avoid automatically loading large object graphs.

However, lazy loading can create N+1 query problems when a response needs related data.

The project therefore uses @EntityGraph on selected repository queries.

Examples include:

@EntityGraph(attributePaths = "category")

for product queries and:

@EntityGraph(attributePaths = {"items", "items.product"})

for order queries.

This gives the application explicit control over which associations should be fetched together for a particular read operation.

13. Order Lifecycle

The implemented order states are:

PENDING
│
├── CONFIRMED
│      │
│      ├── SHIPPED
│      │      │
│      │      └── DELIVERED
│      │
│      └── CANCELLED
│
└── CANCELLED

Admin order management validates allowed transitions instead of allowing arbitrary status changes.

The project also separates customer cancellation from admin order-management operations.

14. Cart and Inventory Model

The cart model is:

User
│
└── Cart
│
├── CartItem → Product
├── CartItem → Product
└── ...

A cart belongs to one user, and a product can appear as a cart item for many carts.

Cart operations require authentication and are scoped to the authenticated user.

The cart item table also enforces uniqueness for a product within the same cart so the same product is represented by one cart item whose quantity can be updated.

15. Address Management

Authenticated users can:

create addresses

list their addresses

fetch an address

update an address

delete an address

maintain a default address

Address types include:

HOME
OFFICE
OTHER

The order does not keep a foreign-key relationship to the mutable address record. Instead, shipping information is copied into the order when the order is placed.

16. Payment Module — Simulated Only

The payment module is intentionally a simulation, not a production payment integration.

Supported payment methods include the project's defined enum values such as:

UPI

CARD

NET_BANKING

The flow is:

PENDING Order
│
▼
POST /api/payments
│
▼
Create Payment record
│
▼
Generate TXN-<UUID>
│
▼
PaymentStatus = SUCCESS
│
▼
OrderStatus = CONFIRMED

No external payment provider is called. There is no Razorpay/Stripe/PayPal integration in the current codebase.

This design is useful for demonstrating payment-related domain modelling and order/payment state transitions without handling real financial data.

17. CORS

The backend exposes a global CORS configuration through Spring Security.

Development frontend origin:

http://localhost:5173

Allowed methods currently include:

GET, POST, PUT, DELETE, PATCH, OPTIONS

Allowed request headers include:

Authorization
Content-Type

Credentials are enabled.

Because CORS is configured globally, controllers do not need individual @CrossOrigin annotations for the current frontend setup.

For production deployment, the allowed origin should be changed to the actual deployed frontend origin rather than leaving localhost enabled.

18. API Overview

Authentication

POST /api/auth/register
POST /api/auth/login

Products

GET    /api/product/allProducts
GET    /api/product/{id}
GET    /api/product/search?name=...
GET    /api/product/category/{categoryName}
GET    /api/product/price?minPrice=...&maxPrice=...
POST   /api/product/create
PUT    /api/product/update/{id}
DELETE /api/product/delete/{id}

Product reads are public; product mutations are restricted to admins.

Categories

GET    /api/category/allCategories
GET    /api/category/{id}
POST   /api/category/create
PUT    /api/category/update/{id}
DELETE /api/category/delete/{id}

Cart

POST   /api/cart/add
GET    /api/cart/all
PUT    /api/cart/items/{productId}
DELETE /api/cart/items/{productId}
DELETE /api/cart

Addresses

POST   /api/addresses
GET    /api/addresses
GET    /api/addresses/{addressId}
PUT    /api/addresses/{addressId}
DELETE /api/addresses/{addressId}

Orders

POST   /api/orders
GET    /api/orders
GET    /api/orders/{orderId}
PUT    /api/orders/{orderId}/cancel

Payments

POST /api/payments
GET  /api/payments/order/{orderId}

Admin order management

GET /api/admin/orders
GET /api/admin/orders/{orderId}
PUT /api/admin/orders/{orderId}/status
PUT /api/admin/orders/{orderId}/cancel

19. Configuration and Secrets

The committed application.properties is intentionally configured to read sensitive values from environment variables:

spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:}
jwt.secret=${JWT_SECRET_KEY}
jwt.expiration=${JWT_EXPIRATION}

Therefore the repository should not contain:

PostgreSQL passwords

JWT signing secrets

API keys

production credentials

private certificates/keystores

personal .env files

Use the provided .env.example only as a reference. Spring Boot does not automatically treat a .env file as an environment-variable source; configure the variables in IntelliJ/your shell/CI environment.

Windows PowerShell example

$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your-local-password"
$env:JWT_SECRET_KEY="your-base64-encoded-secret"
$env:JWT_EXPIRATION="86400000"

Then start the application.

IntelliJ IDEA

Add the same variables to the Run Configuration's Environment variables field.

20. Generate a JWT Secret

JwtService expects a Base64-encoded secret and uses it to create an HMAC signing key.

Generate a strong random value rather than inventing a short secret.

For example, with OpenSSL:

openssl rand -base64 32

Store the resulting value only in your local environment or secret manager.

Do not commit it to GitHub.

21. Database Setup

Create a PostgreSQL database named:

ecommerce

Then configure:

DB_USERNAME
DB_PASSWORD

The application currently uses:

spring.jpa.hibernate.ddl-auto=update

This is convenient for development. For a production deployment, a migration tool such as Flyway or Liquibase and ddl-auto=validate would be preferable.

Development seed data

src/main/resources/data.sql contains ShopSphere demo categories and products.

Important: the current seed file begins with a TRUNCATE ... RESTART IDENTITY CASCADE operation. It is intended for development/testing and is destructive to existing development orders, carts, payments, addresses, products and categories.

Do not use this seed file as a production data migration.

22. Running the Backend

Requirements:

Java 21

Maven (or Maven Wrapper)

PostgreSQL

From ecommerce_Backend:

Windows

.\mvnw.cmd spring-boot:run

macOS / Linux

./mvnw spring-boot:run

Default API base URL:

http://localhost:8080

23. Frontend Integration

The companion React frontend is located in:

ecommerce_Frontend/

It communicates directly with:

http://localhost:8080

and sends the JWT as:

Authorization: Bearer <token>

The frontend stores the access token in browser localStorage for this development project. For a higher-security production application, an HTTP-only secure cookie/session strategy can be considered to reduce exposure to token theft through client-side script execution.

24. Testing Status

The repository currently contains a Spring Boot context-load test:

EcommerceApplicationTests

The application has also been designed for manual API testing through tools such as Postman.

For a production-grade system, the next testing layer would include:

controller integration tests

service unit tests

repository tests

Spring Security tests

validation/error-response tests

concurrent inventory tests

order-state transition tests

payment/order consistency tests

Testcontainers-based PostgreSQL integration tests

These are identified as future improvements rather than being claimed as already implemented.

25. Security Checklist for GitHub

Before pushing:

[ ] No real PostgreSQL password in source
[ ] No JWT secret in source
[ ] No API keys in source
[ ] No .env file committed
[ ] No IDE workspace files committed
[ ] No target/ committed
[ ] No node_modules/ committed
[ ] No private certificates/keys committed
[ ] application.properties contains environment-variable placeholders only
[ ] application-example.properties contains placeholders only

If a secret was ever committed in Git history, simply adding it to .gitignore is not enough. Rotate/revoke the exposed secret and remove the secret from Git history before publishing the repository.

26. Current Scope vs Production Scope

Implemented

RESTful API design

JWT authentication

BCrypt password hashing

RBAC

DTOs and mappers

Bean Validation

Global exception handling

CORS

Transactions

Pessimistic inventory locking

JPA EntityGraphs

Lazy relationship strategy

Cart/order/address/payment domain models

Order lifecycle validation

Inventory restoration on eligible cancellation

Admin order management

Simulated payments

Future production enhancements

Flyway/Liquibase database migrations

Testcontainers PostgreSQL integration tests

More extensive automated tests and CI

Refresh-token/session strategy

Rate limiting / brute-force protection

Audit logging

Observability and metrics

Production secret manager

HTTPS and production CORS configuration

Real payment gateway integration with webhook verification

Idempotency keys for payment/order operations

Stronger inventory/versioning strategy depending on workload

Production deployment and containerization

27. Why This Project Demonstrates Intermediate Backend Skills

The project goes beyond simple CRUD by implementing several real backend concerns:

Security — JWT authentication, BCrypt, stateless sessions and RBAC.

Data integrity — transactions, database constraints and ownership checks.

Concurrency — pessimistic locking around inventory updates.

ORM performance — lazy relationships combined with targeted @EntityGraph fetching.

API quality — DTOs, validation, consistent error responses and status codes.

Business workflows — cart → order → payment → confirmation and order lifecycle transitions.

Historical correctness — order price and shipping-address snapshots.

Separation of concerns — controller/service/repository/mapper/security layers.

Frontend integration — CORS, JWT propagation and a React client consuming the REST API.

The implementation is intentionally positioned as an intermediate learning/portfolio project, while the README explicitly separates implemented functionality from production-level enhancements that are still future work.

28. Author / Portfolio Notes

This repository is suitable as a portfolio project because it demonstrates backend concepts beyond basic CRUD, especially security, transactional workflows, concurrency control and JPA fetch optimization.

When presenting the project on a resume or GitHub, avoid claiming that it processes real payments or is production-hardened. A precise description is:

ShopSphere — Secure E-Commerce REST API: Built a Spring Boot e-commerce backend with JWT authentication, RBAC, DTO validation, global exception handling, transactional order processing, pessimistic locking for inventory concurrency, JPA EntityGraphs for controlled fetching, admin order management and a simulated payment workflow backed by PostgreSQL.