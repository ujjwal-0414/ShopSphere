# ShopSphere — React E-Commerce Frontend

ShopSphere is a **React 19 + Vite storefront** built for the companion Spring Boot e-commerce REST API in `ecommerce_Backend/`.

The frontend demonstrates a practical client-side architecture for an authenticated e-commerce application with **Axios API integration, JWT propagation, protected routes, product browsing, search/filtering, cart management, address selection, checkout, order history, simulated payments and reusable product-image handling**.

Backend: `http://localhost:8080`  
Frontend: `http://localhost:5173`

---

## Core Features & Architecture

- **React 19 Storefront:** Component-based responsive shopping interface.
- **React Router:** Public and authenticated route separation.
- **Axios Interceptors:** Central API client with automatic JWT propagation and authentication-error handling.
- **Protected Routes:** Authenticated pages are guarded through `ProtectedRoute`.
- **Authentication:** Registration, login, logout and persisted client-side authentication state.
- **Product Catalogue:** Browse, search and filter products by category and price.
- **Cart Management:** Add products, update quantities, remove items and clear the cart.
- **Address Management:** Create/select delivery addresses during checkout.
- **Orders:** Order history and order-detail views.
- **Simulated Payments:** Checkout UI connected to the backend's internal payment simulation.
- **Reusable UI States:** Loading, empty, API-error and unexpected-render-error states.
- **Product Image Resolver:** Product-specific image mapping with backend-image and placeholder fallbacks.

---

## Technical Stack

| Area | Technology |
|---|---|
| UI | React 19 |
| Build Tool | Vite 7 |
| Routing | React Router 7 |
| HTTP Client | Axios 1.x |
| Language | JavaScript / JSX |
| Styling | CSS |
| Backend | Spring Boot REST API |
| Authentication | JWT Bearer Token |
| Database | PostgreSQL through backend |

---

## Application Architecture

```text
                         React Application
                                │
              ┌─────────────────┴─────────────────┐
              │                                   │
        React Router                        Context State
              │                         ┌─────────┴─────────┐
              │                         │                   │
         Pages / Routes            AuthContext        CartContext
              │                         │                   │
              └─────────────────┬───────┴───────────────────┘
                                │
                         Axios API Client
                                │
                    Authorization: Bearer JWT
                                │
                                ▼
                       Spring Boot Backend
                                │
                                ▼
                         PostgreSQL Database
```

---

## Project Structure

```text
ecommerce_Frontend/
├── public/
│   └── placeholders/
├── src/
│   ├── api/
│   │   ├── addressApi.js
│   │   ├── authApi.js
│   │   ├── axios.js
│   │   ├── cartApi.js
│   │   ├── catalogApi.js
│   │   ├── orderApi.js
│   │   └── paymentApi.js
│   ├── components/
│   │   ├── EmptyState.jsx
│   │   ├── ErrorBoundary.jsx
│   │   ├── Loading.jsx
│   │   ├── Navbar.jsx
│   │   ├── ProductCard.jsx
│   │   ├── ProtectedRoute.jsx
│   │   └── productImage.js
│   ├── context/
│   │   ├── AuthContext.jsx
│   │   └── CartContext.jsx
│   ├── pages/
│   │   ├── Cart.jsx
│   │   ├── Checkout.jsx
│   │   ├── Home.jsx
│   │   ├── Login.jsx
│   │   ├── OrderDetails.jsx
│   │   ├── Orders.jsx
│   │   ├── ProductDetails.jsx
│   │   ├── Products.jsx
│   │   ├── Profile.jsx
│   │   └── Register.jsx
│   ├── App.jsx
│   ├── main.jsx
│   └── styles.css
├── .env.example
├── .gitignore
├── index.html
├── package.json
├── package-lock.json
└── vite.config.js
```

---

## Route Matrix

### Public Routes

| Route | Purpose |
|---|---|
| `/` | Storefront home |
| `/products` | Product catalogue |
| `/products/:id` | Product details |
| `/login` | User login |
| `/register` | Account registration |

### Protected Routes

| Route | Purpose |
|---|---|
| `/cart` | Shopping cart |
| `/checkout` | Address and payment checkout |
| `/orders` | Order history |
| `/orders/:id` | Order details |
| `/profile` | User profile |

`ProtectedRoute` redirects unauthenticated users to the login page.

---

## Axios & JWT Authentication

The central Axios client is:

```text
src/api/axios.js
```

It uses:

```text
VITE_API_BASE_URL
```

with a development fallback of:

```text
http://localhost:8080
```

### Request Interceptor

The request interceptor reads the JWT from:

```text
localStorage → ecommerce_token
```

and sends:

```http
Authorization: Bearer <token>
```

### Authentication Lifecycle

```text
Login
  ↓
Backend validates credentials
  ↓
Backend returns JWT
  ↓
Frontend stores JWT
  ↓
Axios automatically attaches JWT
  ↓
Protected API request
```

### Response Interceptor

A `401 Unauthorized` response clears the stored authentication state and redirects the user to `/login`.

The current `localStorage` approach is intentionally simple for this learning/portfolio project. A production deployment can evaluate HTTP-only secure cookies/session mechanisms according to its threat model.

---

## React Context

### `AuthContext`

Provides shared authentication state such as:

- Logged-in user
- Login
- Logout
- Persisted authentication state

### `CartContext`

Provides shared cart state so the navbar and shopping pages can react to cart changes without excessive prop drilling.

---

## API Integration Matrix

### Authentication

```text
POST /api/auth/register
POST /api/auth/login
```

### Catalogue

```text
GET /api/category/allCategories
GET /api/product/allProducts
GET /api/product/{id}
GET /api/product/search?name=...
GET /api/product/category/{categoryName}
GET /api/product/price?minPrice=...&maxPrice=...
```

### Cart

```text
GET    /api/cart/all
POST   /api/cart/add
PUT    /api/cart/items/{productId}
DELETE /api/cart/items/{productId}
DELETE /api/cart
```

### Addresses

```text
GET    /api/addresses
POST   /api/addresses
PUT    /api/addresses/{addressId}
DELETE /api/addresses/{addressId}
```

### Orders

```text
POST   /api/orders
GET    /api/orders
GET    /api/orders/{orderId}
PUT    /api/orders/{orderId}/cancel
```

### Payments

```text
POST /api/payments
GET  /api/payments/order/{orderId}
```

The payment UI represents the backend's simulated payment system. It does not process real money or communicate with a real payment provider.

---

## Product Image Handling

Product images are resolved through:

```text
src/components/productImage.js
```

The resolver supports:

1. Product-name mapping
2. Backend `imageUrl` fallback
3. Category placeholder fallback

The same resolver is used across product cards, product details and cart items.

Images use `object-fit: contain` so the product remains visible without aggressive cropping.

The current mapping uses external image URLs. A production-oriented implementation could move owned product assets to object storage/CDN or a backend/static asset layer.

---

## Storefront Screenshots

### Home

![ShopSphere Home](../docs/screenshots/home.png)

### Product Catalogue

![ShopSphere Product Catalogue](../docs/screenshots/product-catalog.png)

### Shopping Cart

![ShopSphere Cart](../docs/screenshots/cart.png)

### Login

![ShopSphere Login](../docs/screenshots/login.png)

### Registration

![ShopSphere Registration](../docs/screenshots/register.png)

---

## CORS & Backend Communication

The frontend communicates directly with the Spring Boot backend. There is no Vite `/api` proxy in the current configuration.

The backend allows the development origin:

```text
http://localhost:5173
```

and supports:

```text
GET POST PUT DELETE PATCH OPTIONS
```

with headers including:

```text
Authorization Content-Type
```

CORS is handled by the backend's global configuration rather than individual React components.

---

## Error, Loading & Empty States

Reusable components include:

```text
Loading.jsx
EmptyState.jsx
ErrorBoundary.jsx
```

The UI distinguishes between:

- Loading data
- Empty catalogue/cart/order states
- API errors
- Expired authentication
- Unexpected React rendering errors

`ErrorBoundary` provides a controlled recovery screen instead of allowing an unexpected component error to leave the application blank.

---

## Environment Configuration

`.env.example` contains:

```env
VITE_API_BASE_URL=http://localhost:8080
```

Create `.env` only when you need to override the development API URL.

Do **not** place database credentials, JWT signing secrets or private API keys into Vite environment variables. `VITE_*` values are intended for client-side use and can become visible in the browser bundle.

---

## Typical User Flow

```text
Register
  ↓
Login
  ↓
JWT stored in browser
  ↓
Browse products
  ↓
Search / filter
  ↓
View product details
  ↓
Add product to cart
  ↓
Manage quantity
  ↓
Select / create shipping address
  ↓
Place order
  ↓
Simulated payment
  ↓
Order confirmed
  ↓
View order history/details
```

---

## Security Model

The frontend participates in the backend security model but does not replace backend authorization.

For example, hiding an admin button in React is not an authorization mechanism. The backend independently enforces the required role.

The backend remains the source of truth for:

- Authentication
- Authorization
- Ownership checks
- Product stock
- Order status
- Payment state
- Validation

---

## GitHub Safety

Never commit:

```text
.env
.env.*          # except .env.example
node_modules/
dist/
IDE workspace files
private certificates/keys
backend passwords
JWT secrets
API keys
```

If a secret was exposed in Git history, `.gitignore` cannot remove the historical secret. Rotate/revoke it and clean the repository history before publishing.

---

## Running the Frontend

### Requirements

- Node.js
- npm
- Spring Boot backend running on `http://localhost:8080`

### Install Dependencies

```bash
npm install
```

### Start Development Server

```bash
npm run dev
```

Open:

```text
http://localhost:5173
```

### Production Build

```bash
npm run build
```

### Preview Production Build

```bash
npm run preview
```

---

## Backend Dependency

The frontend expects the companion backend in:

```text
ecommerce_Backend/
```

The backend README documents JWT security, RBAC, CORS, validation, global exception handling, transactions, pessimistic locking, `@EntityGraph`, lazy loading, order lifecycle, simulated payments and PostgreSQL configuration.

---

## Current Scope vs Future Improvements

### Implemented

- React storefront
- REST API integration
- JWT authentication flow
- Protected routes
- Product catalogue
- Search/filtering
- Cart
- Address selection
- Checkout
- Orders
- Simulated payment UI
- Error/loading/empty states
- Product-specific images
- Responsive layout

### Future Enhancements

- Admin dashboard UI
- Product image upload/management
- TanStack Query or another persistent client-side cache/query library
- Pagination/infinite scrolling
- Wishlist
- Coupon UI
- Reviews/ratings
- Better optimistic cart updates
- Automated frontend tests
- Accessibility audit and keyboard navigation improvements
- Production image CDN/object storage
- Secure cookie-based authentication where appropriate

---

## Portfolio Description

**ShopSphere Frontend:** Built a responsive React e-commerce storefront integrated with a Spring Boot REST API, implementing JWT-authenticated routes, Axios interceptors, product search/filtering, cart management, address-based checkout, order tracking, simulated payments, reusable UI state components and product-specific image handling.
