# Notes App – Full Stack Application

A full-stack Notes Management application built using Spring Boot, React, PostgreSQL, and JWT Authentication. Users can securely register, log in, create notes, search notes, update notes, and delete notes.

---

# Features

## Authentication & Authorization

- User Registration
- User Login
- JWT-based Authentication
- Protected Routes
- Token Verification
- Session Persistence
- Role-based User Model

## Notes Management

- Create Notes
- Update Notes
- Delete Notes
- Search Notes
- Pagination Support
- User-specific Notes Access
- Ownership Validation

## Frontend Features

- Responsive UI
- Toast Notifications
- Search with Debouncing
- Protected Routing
- Context API State Management
- Axios API Integration

## Backend Features

- RESTful APIs
- Layered Architecture
- DTO-based Response Handling
- Global Exception Handling
- Validation Support
- PostgreSQL Integration
- Spring Security + JWT
- Dockerized Deployment Support

---

# Tech Stack

## Frontend

- React
- Vite
- Tailwind CSS
- Axios
- React Router DOM
- React Toastify

## Backend

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT Authentication
- Maven
- Docker

---

# Project Structure

## Frontend

```txt
frontend/
 ├── src/
 │    ├── api/
 │    ├── components/
 │    ├── context/
 │    ├── pages/
 │    └── App.jsx
 ├── public/
 ├── package.json
 └── vite.config.js
```

## Backend

```txt
backend/
 ├── src/main/java/com/sailesh/notes/
 │    ├── auth/
 │    ├── common/
 │    ├── config/
 │    ├── notes/
 │    └── user/
 ├── src/main/resources/
 │    └── application.properties
 ├── Dockerfile
 └── pom.xml
```

---

# API Endpoints

## Authentication APIs

| Method | Endpoint             | Description      |
| ------ | -------------------- | ---------------- |
| POST   | `/api/auth/register` | Register user    |
| POST   | `/api/auth/login`    | Login user       |
| GET    | `/api/auth/verify`   | Verify JWT token |

## Notes APIs

| Method | Endpoint            | Description         |
| ------ | ------------------- | ------------------- |
| GET    | `/api/notes`        | Get paginated notes |
| GET    | `/api/notes/search` | Search notes        |
| POST   | `/api/notes`        | Create note         |
| PUT    | `/api/notes/{id}`   | Update note         |
| DELETE | `/api/notes/{id}`   | Delete note         |

---

# Environment Variables

## Frontend (.env)

```env
VITE_API_BASE_URL=http://localhost:8080
```

## Backend Environment Variables

```env
PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/notesapp
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret_key
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

---

# Local Setup

## Clone Repository

```bash
git clone https://github.com/Sailesh13K/notes-application-spring.git
cd notesapp-spring
```

---

# Backend Setup

## Navigate to Backend

```bash
cd backend
```

## Run Backend

```bash
./mvnw spring-boot:run
```

OR

```bash
mvn spring-boot:run
```

Backend runs on:

```txt
http://localhost:8080
```

---

# Frontend Setup

## Navigate to Frontend

```bash
cd frontend
```

## Install Dependencies

```bash
npm install
```

## Run Frontend

```bash
npm run dev
```

Frontend runs on:

```txt
http://localhost:5173
```

---

# Docker Deployment

## Build Docker Image

```bash
docker build -t notes-backend .
```

## Run Docker Container

```bash
docker run --env-file .env notes-backend
```

---

# Security Features

- Password Encryption using BCrypt
- JWT Authentication
- Protected API Routes
- User Ownership Validation
- Stateless Authentication
- CORS Configuration
- Request Validation

---

# Future Improvements

- Refresh Token Authentication
- File Attachments
- Rich Text Notes
- Note Categories
- Dark Mode
- CI/CD Pipeline
- Unit & Integration Tests
- Rate Limiting
- Email Verification
- Redis Caching

---

# Deployment

## Frontend

- Vercel

## Backend

- Render (Docker Deployment)

## Database

- PostgreSQL / Neon

---

# Author

Sailesh Kattamuri

- GitHub: [https://github.com/Sailesh13K]
- LinkedIn: [https://www.linkedin.com/in/sailesh-kattamuri/]
