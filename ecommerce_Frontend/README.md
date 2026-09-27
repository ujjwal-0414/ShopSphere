ShopSphere — React E-Commerce Frontend

ShopSphere is a React + Vite storefront built for the companion Spring Boot e-commerce REST API in ecommerce_Backend/.

The frontend demonstrates a practical client-side architecture for an authenticated e-commerce application: API integration with Axios, JWT propagation, protected routes, product browsing, search/filtering, cart management, address selection, checkout, order history, payment simulation and reusable product-image handling.

Backend: http://localhost:8080
Frontend: http://localhost:5173

1. Frontend Highlights

React 19

Vite development/build tooling

React Router for client-side routing

Axios API client with request/response interceptors

JWT Bearer token propagation

Protected routes for authenticated pages

Login and registration flows

Product catalogue and product details

Category filtering

Product search

Price filtering

Cart management

Address management

Checkout workflow

Order history and order details

Simulated payment flow

Loading and empty states

Central error handling for expired/invalid authentication

React error boundary to prevent an unexpected component error from producing a blank screen

Product-specific image mapping with fallback handling

Responsive storefront layout

2. Technology Stack

Area

Technology

UI

React 19

Build tool

Vite 7

Routing

React Router 7

HTTP client

Axios 1.x

Language

JavaScript / JSX

Styling

CSS

Backend

Spring Boot REST API

Authentication

JWT Bearer token

Database

PostgreSQL through backend

3. Application Architecture

                    React Application
                           │
             ┌─────────────┴─────────────┐
             │                           │
         React Router               Context State
             │                    ┌────────┴────────┐
             │                    │                 │
       Pages / Routes          AuthContext      CartContext
             │                    │                 │
             └─────────────┬──────┴─────────────────┘
                           │
                     Axios API Client
                           │
                 Authorization: Bearer JWT
                           │
                           ▼
                  Spring Boot Backend
                           │
                           ▼
                      PostgreSQL

4. Project Structure

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

5. Routing

Public routes

/
/products
/products/:id
/login
/register

Protected routes

/cart
/checkout
/orders
/orders/:id
/profile

ProtectedRoute prevents unauthenticated users from accessing protected pages and redirects them to login.

6. Axios and JWT Authentication

The central Axios client is located at:

src/api/axios.js

It uses:

VITE_API_BASE_URL

with a development fallback of:

http://localhost:8080

Before an API request, the Axios request interceptor reads the JWT from:

localStorage → ecommerce_token

and sends:

Authorization: Bearer <token>

Authentication lifecycle

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

The response interceptor handles 401 Unauthorized by clearing the stored authentication state and redirecting the user to /login.

For a production application, token storage can be redesigned around secure HTTP-only cookies/session mechanisms depending on the deployment and threat model. The current localStorage approach is intentionally simple for this learning/portfolio project.

7. React Context

AuthContext

Responsible for client-side authentication state such as:

logged-in user

login

logout

persisted authentication state

CartContext

Provides shared cart state to the application so the navbar and shopping pages can react to cart changes without passing cart data through many component levels.

8. API Integration

Authentication

POST /api/auth/register
POST /api/auth/login

Catalog

GET /api/category/allCategories
GET /api/product/allProducts
GET /api/product/{id}
GET /api/product/search?name=...
GET /api/product/category/{categoryName}
GET /api/product/price?minPrice=...&maxPrice=...

Cart

GET    /api/cart/all
POST   /api/cart/add
PUT    /api/cart/items/{productId}
DELETE /api/cart/items/{productId}
DELETE /api/cart

Addresses

GET    /api/addresses
POST   /api/addresses
PUT    /api/addresses/{addressId}
DELETE /api/addresses/{addressId}

Orders

POST /api/orders
GET  /api/orders
GET  /api/orders/{orderId}
PUT  /api/orders/{orderId}/cancel

Payments

POST /api/payments
GET  /api/payments/order/{orderId}

The payment UI represents the backend's simulated payment system. It does not process real money or communicate with a real payment provider.

9. Product Images

The storefront uses src/components/productImage.js as a product-specific image resolver.

The resolver maps the current product names to product-specific photographs instead of intentionally showing a generic category icon for every product.

For example:

Logitech Wireless Mouse
↓
productImage.js
↓
Logitech mouse photograph

The resolver supports:

product-name mapping

backend imageUrl fallback

category placeholder fallback if an image cannot be loaded

Product images use object-fit: contain so the full product photograph remains visible instead of being aggressively cropped.

The same resolver is used across product cards, product details and cart items.

The current image mapping uses external image URLs. A future production-oriented enhancement would be to serve owned product assets from object storage/CDN or from the backend/static asset layer instead of relying on third-party hot-linked images.

10. CORS

The frontend communicates directly with the Spring Boot backend. There is no Vite /api proxy in the current configuration.

The backend must allow:

Origin:
http://localhost:5173

Methods:

GET
POST
PUT
DELETE
PATCH
OPTIONS

Headers:

Authorization
Content-Type

Credentials are enabled by the backend's global CORS configuration.

Therefore, individual React components do not need to implement CORS logic.

11. Error and Loading UX

Reusable components are used for common UI states:

Loading.jsx
EmptyState.jsx
ErrorBoundary.jsx

The application distinguishes between:

loading data

empty catalogue/cart/order states

API errors

expired authentication

unexpected React rendering errors

ErrorBoundary provides a controlled recovery screen instead of allowing an unexpected component error to leave the application blank.

12. Environment Configuration

.env.example contains the public development configuration template:

VITE_API_BASE_URL=http://localhost:8080

For local development, create .env only if you need to override the default API URL:

VITE_API_BASE_URL=http://localhost:8080

.env is ignored by Git.

Do not put passwords, database credentials or JWT signing secrets into the React application's Vite environment. Any VITE_* variable is intended for client-side use and can become visible in the browser bundle.

13. Running the Frontend

Requirements:

Node.js

npm

Spring Boot backend running on http://localhost:8080

Install dependencies:

npm install

Start development server:

npm run dev

Open:

http://localhost:5173

Production build:

npm run build

Preview production build:

npm run preview

14. Typical User Flow

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
Select/create shipping address
↓
Place order
↓
Simulated payment
↓
Order confirmed
↓
View order history/details

15. Security Notes

The frontend participates in the backend security model but does not replace backend authorization.

For example, hiding an admin button in React would not be considered an authorization mechanism. The backend independently enforces the ADMIN role for protected admin operations.

The backend remains the source of truth for:

authentication

authorization

ownership checks

product stock

order status

payment state

validation

16. GitHub Safety

Never commit:

.env
.env.*          # except .env.example
node_modules/
dist/
IDE workspace files
private certificates/keys
backend passwords
JWT secrets
API keys

The frontend .gitignore and repository-level .gitignore are configured to ignore local environment files and generated dependencies/build output.

Also remember that .gitignore cannot remove a secret that has already been committed to Git history. If a secret was exposed, rotate/revoke it and clean the Git history before making the repository public.

17. Backend Dependency

The frontend expects the companion Spring Boot application in:

ecommerce_Backend/

The backend README contains the detailed documentation for:

JWT security

RBAC

CORS

validation

global exception handling

transactions

pessimistic locking

@EntityGraph

lazy loading

order lifecycle

simulated payments

PostgreSQL configuration

GitHub secret protection

18. Current Scope and Future Improvements

Implemented

React storefront

REST API integration

JWT authentication flow

Protected routes

Product catalogue

Search/filtering

Cart

Address selection

Checkout

Orders

Simulated payment UI

Error/loading/empty states

Product-specific images

Responsive layout

Future enhancements

Admin dashboard UI

Product image upload/management

Persistent client-side cache/query library such as TanStack Query

Pagination/infinite scrolling

Wishlist

Coupon UI

Reviews/ratings

Better optimistic cart updates

Automated frontend tests

Accessibility audit and keyboard-navigation improvements

Production image CDN/object storage

Secure cookie-based authentication where appropriate

19. Portfolio Description

A concise GitHub/project description:

ShopSphere Frontend: Built a responsive React e-commerce storefront integrated with a Spring Boot REST API, implementing JWT-authenticated routes, Axios interceptors, product search/filtering, cart management, address-based checkout, order tracking, simulated payments, reusable UI state components and product-specific image handling.