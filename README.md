# 🛒 InstaMart — E-Commerce Website

A full-stack e-commerce web application built with **React** on the frontend and **Spring Boot** on the backend, backed by **MongoDB** and secured with **JWT authentication**.

🌐 **Live Demo:** [e-commerce-website-weld-ten.vercel.app](https://e-commerce-website-weld-ten.vercel.app)

---

## 🏗️ Architecture

```
┌──────────────────────────┐         ┌──────────────────────────────┐
│   React (Vite)           │ ──────▶ │  Spring Boot REST API         │
│   frontend/instaMart     │ ◀────── │  (Backend — Port 8080)        │
│   Deployed on Vercel     │         └──────────────┬───────────────┘
└──────────────────────────┘                        │
                                           ┌────────▼────────┐
                                           │  MongoDB Atlas  │
                                           └─────────────────┘
```

The **frontend** is deployed independently on Vercel and communicates with the **Spring Boot backend** via REST APIs.

---

## 🚀 Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 3.5.11 | REST API framework |
| Spring Security + JWT (JJWT 0.11.5) | Authentication & authorization |
| Spring Data MongoDB | Database access |
| Spring Bean Validation | Request body validation |
| org.json | JSON parsing utilities |
| Lombok | Boilerplate reduction |
| Spring DevTools | Hot reload during development |
| Maven | Build tool |

### Frontend
| Technology | Purpose |
|---|---|
| React | UI framework |
| Vite | Build tool & dev server |
| JavaScript | Core language |
| CSS | Styling |

### Infrastructure
| Technology | Purpose |
|---|---|
| MongoDB Atlas | Cloud database |
| Vercel | Frontend deployment |

---

## ✨ Features

- 🔐 User registration, login & JWT-based authentication
- 🛍️ Product browsing and search
- 🛒 Shopping cart management
- 📦 Order placement and tracking
- 👤 User profile management
- 🔒 Protected routes — only authenticated users can checkout
- ✅ Server-side request validation

---

## 📁 Project Structure

```
e-commerce-website/
├── frontend/
│   └── instaMart/               # React + Vite frontend
│       ├── src/
│       │   ├── components/      # Reusable UI components
│       │   ├── pages/           # Page-level views (Home, Cart, Login, etc.)
│       │   ├── services/        # Axios API calls
│       │   └── context/         # React context (auth, cart state)
│       └── package.json
├── src/
│   └── main/java/com/InstaMart/instaMart/
│       ├── controller/          # REST controllers
│       ├── service/             # Business logic
│       ├── model/               # MongoDB document models
│       ├── repository/          # Spring Data repositories
│       ├── security/            # JWT filter, Spring Security config
│       └── dto/                 # Data Transfer Objects
├── src/main/resources/
│   └── application.properties  # App configuration
├── pom.xml
└── mvnw
```

---

## ⚙️ Prerequisites

- [Java 17+](https://adoptium.net/)
- [Node.js 18+](https://nodejs.org/) and npm
- [Maven 3.8+](https://maven.apache.org/) (or use `./mvnw`)
- [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) account (or local MongoDB instance)

---

## 🛠️ Getting Started (Local Development)

### 1. Clone the repository

```bash
git clone https://github.com/abRaq45/e-commerce-website.git
cd e-commerce-website
```

### 2. Configure the backend

Edit `src/main/resources/application.properties`:

```properties
# MongoDB
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster>.mongodb.net/instamart

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# CORS (allow frontend dev server)
cors.allowed-origins=http://localhost:5173
```

### 3. Run the backend

```bash
./mvnw spring-boot:run
```

Backend starts at `http://localhost:8080`.

### 4. Run the frontend

```bash
cd frontend/instaMart
npm install
npm run dev
```

Frontend starts at `http://localhost:5173`.

> **Note:** Update the API base URL in the frontend (e.g. in `src/services/api.js`) to point to `http://localhost:8080` for local development.

---

## 🔌 API Endpoints

### Auth

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive a JWT token |

### Products

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/search?query=` | Search products |
| `POST` | `/api/products` | Add a product *(admin)* |
| `PUT` | `/api/products/{id}` | Update a product *(admin)* |
| `DELETE` | `/api/products/{id}` | Delete a product *(admin)* |

### Cart & Orders

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/cart` | Get current user's cart |
| `POST` | `/api/cart/add` | Add item to cart |
| `DELETE` | `/api/cart/{itemId}` | Remove item from cart |
| `POST` | `/api/orders` | Place an order |
| `GET` | `/api/orders` | Get current user's orders |

> **Note:** All cart, order, and user endpoints require `Authorization: Bearer <token>` header.

---

## 🔒 Security

- Passwords hashed with **BCrypt**
- **JWT tokens** used for stateless session management
- Spring Security filters protect all routes except `/api/auth/**` and `GET /api/products/**`
- Bean Validation (`@Valid`) enforces correct request payloads on all endpoints

---

## 🚢 Deployment

### Frontend (Vercel)

The `frontend/instaMart` folder is deployed on Vercel. To redeploy:

1. Push changes to `master`
2. Vercel auto-deploys on every push (if connected via GitHub integration)

Or deploy manually:

```bash
cd frontend/instaMart
npm run build
# Deploy the dist/ folder to Vercel
```

### Backend

Deploy the Spring Boot JAR to any cloud provider (Render, Railway, AWS EC2, etc.):

```bash
./mvnw clean package -DskipTests
java -jar target/instaMart-0.0.1-SNAPSHOT.jar
```

---

## 🧪 Running Tests

```bash
./mvnw test
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
