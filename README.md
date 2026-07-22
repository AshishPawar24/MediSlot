# 🩺 MediSlot – Intelligent Healthcare Appointment Platform

<p align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-7-6DB33F?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql)
![JWT](https://img.shields.io/badge/JWT-Authentication-blue?style=for-the-badge)
![Razorpay](https://img.shields.io/badge/Razorpay-Payment_Integration-0C2451?style=for-the-badge)
![Groq AI](https://img.shields.io/badge/Groq-AI_Integration-7B3FF2?style=for-the-badge)

</p>

---

## 📌 About The Project

**MediSlot** is an enterprise-level backend application built using **Spring Boot** that simulates a modern healthcare appointment platform.

The project demonstrates how a real-world healthcare booking system can be designed using clean architecture, secure authentication, payment integration, AI-powered doctor recommendations, and role-based access control.

Unlike a basic CRUD application, MediSlot focuses on solving real backend engineering challenges such as:

- Secure Authentication & Authorization
- Concurrent Appointment Booking
- Payment Verification
- AI Integration
- Clean Layered Architecture
- Database Design
- Exception Handling
- Production-style REST APIs

The project has been developed with the primary goal of demonstrating backend engineering skills expected from a Software Development Engineer.

---

# ✨ Features

## 🔐 Authentication & Security

- User Registration
- User Login
- JWT Authentication
- Refresh Token Rotation
- BCrypt Password Encryption
- Role Based Authorization
- Protected REST APIs
- Logout with Refresh Token Revocation
- Current User Resolution
- Secure Environment Variables

---

## 👨‍⚕️ Doctor Module

- Doctor Profile Creation
- Update Doctor Profile
- View Doctor Profile
- Manage Appointment Slots
- Prevent Overlapping Slots
- View Appointment History
- Filter Appointments
- Update Appointment Status
- Mark Appointment as:
    - Completed
    - Cancelled
    - No Show

---

## 🧑 Patient Module

- Patient Profile Creation
- Update Patient Profile
- View Patient Profile
- Search Doctors
- View Available Slots
- Book Appointment
- View Appointment History
- AI Symptom Based Doctor Recommendation

---

## 🔎 Doctor Search

Supports searching doctors using multiple filters:

- City
- Specialization
- Experience
- Consultation Fee
- Pagination
- Sorting

Implemented using **Spring Data JPA Specifications** for dynamic query generation.

---

## 📅 Appointment Management

- Slot Creation
- Slot Deletion
- Slot Availability
- Appointment Booking
- Appointment History
- Status Tracking

Appointment Lifecycle

```text
AVAILABLE
      │
      ▼
BOOKED
      │
      ▼
PENDING_PAYMENT
      │
      ▼
CONFIRMED
      │
 ┌────┼─────────┐
 ▼    ▼         ▼
COMPLETED   CANCELLED   NO_SHOW
```

---

## 💳 Razorpay Payment Integration

Secure payment flow including:

- Order Creation
- Payment Verification
- Signature Validation
- Webhook Support
- Payment Status Tracking

Payment Status

```text
PENDING
    │
    ▼
SUCCESS
    │
    ├────────► REFUNDED
    │
FAILED
```

---

## 🤖 AI Symptom Recommendation

Patients can describe their symptoms.

Example:

```text
"I have fever, headache and body pain."
```

The AI module:

- Sends symptoms to Groq AI
- Identifies relevant medical specializations
- Reuses the existing Doctor Search module
- Returns matching doctors
- Includes AI confidence score
- Returns a medical disclaimer

The AI module **does not diagnose diseases** and **does not store any patient conversations**.

---

# 🏗️ Project Architecture

The application follows a layered architecture.

```text
                Client
                   │
                   ▼
            REST Controller
                   │
                   ▼
              Service Layer
                   │
                   ▼
           Repository Layer
                   │
                   ▼
                MySQL
```

Cross-cutting components:

- Spring Security
- JWT Filter
- Exception Handler
- MapStruct
- Environment Configuration

---

# 📂 Project Structure

```text
src
└── main
    ├── java
    │   └── com.medislot.coreservice
    │       ├── ai
    │       ├── config
    │       ├── controller
    │       ├── dto
    │       │     ├── request
    │       │     └── response
    │       ├── entity
    │       ├── enums
    │       ├── exception
    │       ├── mapper
    │       ├── repository
    │       ├── security
    │       ├── service
    │       ├── service.impl
    │       └── specification
    │
    └── resources
        ├── application.properties
        └── ...
```

---

# 🛠️ Tech Stack

| Category | Technology |
|-----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 4.1.0 |
| Security | Spring Security 7 |
| Authentication | JWT |
| Password Encryption | BCrypt |
| Database | MySQL |
| ORM | Hibernate / Spring Data JPA |
| Dynamic Queries | JPA Specifications |
| Object Mapping | MapStruct |
| Payment Gateway | Razorpay |
| AI Integration | Groq API |
| Build Tool | Maven |
| IDE | IntelliJ IDEA |
| API Testing | Postman |

---

# 🔄 Application Workflow

```text
User
 │
 ▼
Authentication
 │
 ▼
Role Validation
 │
 ├──────────────┐
 ▼              ▼
Doctor       Patient
 │              │
 ▼              ▼
Manage       Search Doctors
Slots            │
 │               ▼
 ▼          Book Appointment
                 │
                 ▼
         Razorpay Payment
                 │
                 ▼
      Appointment Confirmed
                 │
                 ▼
      AI Recommendation (Optional)
                 │
                 ▼
      Doctor Manages Appointment
```

---

# 🔐 Authentication Flow

```text
Register
    │
    ▼
Password Encrypted (BCrypt)
    │
    ▼
Stored in Database
    │
    ▼
Login
    │
    ▼
JWT Access Token Generated
    │
    ▼
Refresh Token Generated
    │
    ▼
Client Stores Tokens
    │
    ▼
Protected APIs
    │
    ▼
JwtAuthenticationFilter
    │
    ▼
Security Context
    │
    ▼
Controller
```

---
# 📅 Appointment Booking Workflow

```text
Patient
   │
   ▼
Search Doctors
   │
   ▼
View Available Slots
   │
   ▼
Select Appointment Slot
   │
   ▼
Optimistic Lock Validation
   │
   ▼
Appointment Created
(Status = PENDING_PAYMENT)
   │
   ▼
Proceed to Payment
```

---

# 💳 Payment Workflow

```text
Patient
   │
   ▼
Create Razorpay Order
   │
   ▼
Razorpay Checkout
   │
   ▼
Payment Successful
   │
   ▼
Backend Signature Verification
   │
   ▼
Payment Stored
   │
   ▼
Appointment Status
CONFIRMED
```

If the client fails to notify the backend after payment, the Razorpay webhook acts as a backup to synchronize the payment status securely.

---

# 🤖 AI Recommendation Workflow

```text
Patient Symptoms
        │
        ▼
Groq API
        │
        ▼
Recommended Specializations
        │
        ▼
Reuse Existing Doctor Search
        │
        ▼
Matching Doctors Returned
```

The AI module is intentionally lightweight and focused.

It **does not**:

- Diagnose diseases
- Replace medical professionals
- Store patient conversations
- Maintain chat history

Its responsibility is limited to recommending the most relevant doctor specialization based on the symptoms provided.

---

# 📌 REST API Modules

| Module | Description |
|----------|------------|
| Authentication | User Registration, Login, Logout, Refresh Token |
| Patient Profile | Create, View, Update Patient Profile |
| Doctor Profile | Create, View, Update Doctor Profile |
| Doctor Search | Search Doctors using Dynamic Filters |
| Appointment Slots | Create, Delete, View Slots |
| Appointment Booking | Book & View Appointments |
| Payments | Razorpay Order Creation & Verification |
| AI Recommendation | Symptom Based Doctor Recommendation |
| Doctor Appointment Management | Complete / Cancel / No Show |

---

# 🚀 Getting Started

## Prerequisites

Before running the project, ensure the following are installed:

- Java 17
- Maven
- MySQL
- IntelliJ IDEA (Recommended)
- Git

---

## Clone Repository

```bash
git clone https://github.com/<your-github-username>/MediSlot.git

cd MediSlot
```

---

## Create Database

```sql
CREATE DATABASE medislot_db;
```

---

## Configure Environment Variables

The project uses environment variables for all sensitive credentials.

Required variables include:

```text
MS_DB_USERNAME
MS_DB_PASSWORD

MS_JWT_SECRET
MS_JWT_ACCESS_EXPIRATION
MS_JWT_REFRESH_EXPIRATION

MS_GROQ_API_KEY

MS_RAZORPAY_KEY_ID
MS_RAZORPAY_KEY_SECRET
MS_RAZORPAY_WEBHOOK_SECRET
```

No secrets are stored in the source code.

---

## Run the Project

Using Maven

```bash
mvn clean install

mvn spring-boot:run
```

Or simply run the Spring Boot application directly from IntelliJ IDEA.

Default Server

```
http://localhost:8080
```

---

# 🧪 Testing

The entire backend has been tested using **Postman**.

The API collection covers:

- Authentication
- JWT Authorization
- Refresh Tokens
- Patient Module
- Doctor Module
- Doctor Search
- Appointment Slot Management
- Appointment Booking
- Razorpay Payment Flow
- AI Recommendation
- Appointment Status Management

Each module includes both:

- Positive Test Cases
- Negative Test Cases

to verify business rules and exception handling.

---

# 🔒 Security Highlights

✔ JWT Authentication

✔ Refresh Token Rotation

✔ BCrypt Password Encryption

✔ Role-Based Authorization

✔ Secure Password Storage

✔ Environment Variable Configuration

✔ Razorpay Signature Verification

✔ Webhook Signature Validation

✔ Protected REST APIs

✔ Global Exception Handling

✔ Input Validation

---

# 🌟 Highlights

This project demonstrates several enterprise backend concepts, including:

- Layered Architecture
- REST API Design
- Spring Security
- JWT Authentication
- Refresh Token Rotation
- Dynamic Query Generation
- Optimistic Locking
- Payment Gateway Integration
- AI API Integration
- DTO Mapping using MapStruct
- Exception Handling
- Environment-based Configuration
- Clean Code Practices

---

# 🔮 Future Improvements

The current implementation focuses on core backend functionality.

Possible future enhancements include:

- Email & SMS Notifications
- Swagger / OpenAPI Documentation
- Docker Deployment
- CI/CD Pipeline
- Unit & Integration Testing
- Admin Dashboard
- Doctor Availability Calendar
- Medical Reports Upload
- Video Consultation
- Multi-language Support

---

# 👨‍💻 Author

**Ashish Pawar**

Backend Developer | Java & Spring Boot Enthusiast

GitHub:
https://github.com/AshishPawar24

LinkedIn:

_Add your LinkedIn profile here_

---

# 📄 License

This project has been developed for educational purposes and to demonstrate backend engineering concepts using Spring Boot.

Feel free to explore the codebase and use it as a learning reference.

---

# ⭐ If you found this project interesting...

If you like this project, consider giving it a **Star ⭐** on GitHub.

It motivates developers to build more high-quality open-source projects.

---

<p align="center">

### Thank you for visiting the repository ❤️

**Built with Java, Spring Boot, and a passion for Backend Development.**

</p>