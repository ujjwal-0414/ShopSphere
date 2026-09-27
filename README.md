ShopSphere — Full-Stack E-Commerce Application

ShopSphere is a full-stack e-commerce project with a Spring Boot REST API + PostgreSQL backend and a React + Vite frontend.

The project is intentionally positioned as an intermediate backend-focused portfolio project. It goes beyond basic CRUD by implementing JWT authentication, role-based access control, DTO validation, centralized exception handling, transactional business operations, pessimistic locking for inventory concurrency, JPA @EntityGraph fetch planning, order lifecycle management, inventory restoration and a simulated payment workflow.

Repository Structure

ShopSphere/
├── ecommerce_Backend/
│   ├── src/main/java/...
│   ├── src/main/resources/application.properties
│   ├── src/main/resources/data.sql
│   └── README.md
│
├── ecommerce_Frontend/
│   ├── src/
│   ├── public/
│   └── README.md
│
└── README.md

Run Locally

Backend

Requirements:

Java 21

PostgreSQL

Maven Wrapper

Create a PostgreSQL database named ecommerce and configure the environment variables documented in ecommerce_Backend/README.md.

Windows:

cd ecommerce_Backend
.\mvnw.cmd spring-boot:run

macOS/Linux:

cd ecommerce_Backend
./mvnw spring-boot:run

Backend:

http://localhost:8080

Frontend

Requirements:

Node.js

npm

cd ecommerce_Frontend
npm install
npm run dev

Frontend:

http://localhost:5173

Security / Configuration

No real database password or JWT signing key belongs in Git. The backend reads sensitive values from environment variables:

DB_USERNAME
DB_PASSWORD
JWT_SECRET_KEY
JWT_EXPIRATION

The repository includes .env.example files containing placeholders only. Never commit a real .env file or real secret values.

Documentation

Backend documentation

Frontend documentation

Payment Disclaimer

The payment module is simulated. It creates an internal payment record and transaction ID and confirms the order; it does not connect to Razorpay, Stripe, PayPal or any other real payment processor.

Project Scope

Implemented:

JWT authentication

BCrypt password hashing

USER / ADMIN RBAC

Product/category management

Search and filtering

Cart management

Address management

Transactional order creation

Pessimistic inventory locking

Order cancellation and stock restoration

Admin order management

DTOs and mappers

Bean Validation

Global exception handling

Custom 401 / 403 responses

JPA @EntityGraph fetch optimization

Lazy relationship strategy

CORS

React storefront and checkout flow

Simulated payments

The documentation also lists production-oriented improvements that are intentionally outside the current scope, such as Flyway/Liquibase migrations, comprehensive automated tests, rate limiting, observability, refresh-token/session strategy and real payment-gateway integration.