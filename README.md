# 🏋️ Gym CRM - Microservices-Based Gym Management System

A comprehensive backend system for managing gym operations, trainees, trainers, training sessions, user accounts, authentication, workload management, and supporting business processes.

The project is implemented using **Java 17 and Spring Boot** and follows a **microservice-oriented architecture**. The main service provides the core gym management functionality, while supporting services are designed to separate infrastructure and cross-cutting responsibilities.

The system was developed with an emphasis on clean architecture, separation of concerns, security, validation, persistence, automated testing, observability, caching, asynchronous messaging, and service discovery.

---

## 📋 Table of Contents

* [Project Overview](#-project-overview)
* [Architecture](#-architecture)
* [Main Service](#-main-service)
* [Domain Model](#-domain-model)
* [User Roles](#-user-roles)
* [Core Features](#-core-features)
* [Authentication & Security](#-authentication--security)
* [Trainee Management](#-trainee-management)
* [Trainer Management](#-trainer-management)
* [Training Management](#-training-management)
* [Training Type Management](#-training-type-management)
* [Trainer Workload Management](#-trainer-workload-management)
* [User Management](#-user-management)
* [Database & Persistence](#-database--persistence)
* [Caching](#-caching)
* [Asynchronous Messaging](#-asynchronous-messaging)
* [Service Discovery](#-service-discovery)
* [Exception Handling](#-exception-handling)
* [Validation](#-validation)
* [Logging & Observability](#-logging--observability)
* [Testing](#-testing)
* [Project Structure](#-project-structure)
* [Technology Stack](#-technology-stack)
* [Configuration Profiles](#-configuration-profiles)
* [Running the Application](#-running-the-application)
* [Docker](#-docker)
* [API Documentation](#-api-documentation)
* [Supporting Microservices](#-supporting-microservices)
* [Development Practices](#-development-practices)
* [Repository](#-repository)

---

# 📋 Project Overview

The **Gym CRM** is a backend platform designed to automate and centralize the management of a fitness club.

The application provides functionality for:

* Managing gym users
* Managing trainees
* Managing trainers
* Creating and managing training sessions
* Managing training types
* Assigning trainers to trainees
* Tracking trainer workloads
* Authentication and authorization
* JWT-based security
* Brute-force login protection
* Token blacklisting
* Database persistence
* Input validation
* Global exception handling
* Caching
* Asynchronous communication
* Service discovery
* Application monitoring
* Transaction logging
* Unit testing
* Integration testing
* BDD testing with Cucumber
* Docker-based deployment

The main service is implemented as a Spring Boot REST API and is structured into separate layers and responsibilities rather than placing all business logic inside controllers.

---

# 🏗️ Architecture

The project follows a **microservice-oriented architecture** in which the main Gym CRM service represents the core business functionality and supporting services handle infrastructure or cross-cutting concerns.

## High-Level Architecture

```text
                         ┌───────────────────────┐
                         │      Client / UI      │
                         │                       │
                         │ REST API Consumers    │
                         └───────────┬───────────┘
                                     │
                                     │ HTTP / REST
                                     ▼
                    ┌────────────────────────────────┐
                    │       Gym CRM Main Service     │
                    │                                │
                    │       Spring Boot API          │
                    │                                │
                    │ ┌──────────┐  ┌─────────────┐  │
                    │ │Controller│  │   Security  │  │
                    │ └────┬─────┘  └──────┬──────┘  │
                    │      │               │         │
                    │ ┌────▼──────────────────────┐  │
                    │ │      Business Services    │  │
                    │ └────────────┬──────────────┘  │
                    │              │                 │
                    │ ┌────────────▼─────────────┐   │
                    │ │ DAO / Persistence Layer  │   │
                    │ └────────────┬─────────────┘   │
                    └──────────────┼─────────────────┘
                                   │
                 ┌─────────────────┼─────────────────┐
                 │                 │                 │
                 ▼                 ▼                 ▼
        ┌────────────────┐ ┌───────────────┐ ┌──────────────┐
        │  PostgreSQL    │ │    Caffeine   │ │   ActiveMQ   │
        │    Database    │ │     Cache     │ │   Messaging  │
        └────────────────┘ └───────────────┘ └──────┬───────┘
                                                     │
                                                     ▼
                                          ┌────────────────────┐
                                          │ Supporting Services│
                                          │                    │
                                          │ Authentication     │
                                          │ Notifications      │
                                          │ Other Services     │
                                          └────────────────────┘

                         ┌───────────────────────┐
                         │   Eureka Discovery    │
                         │        Server         │
                         └───────────────────────┘

                         ┌───────────────────────┐
                         │ Actuator / Prometheus │
                         │      Monitoring       │
                         └───────────────────────┘
```

The main service explicitly includes a Eureka client, ActiveMQ integration, Actuator, Prometheus metrics, and Caffeine caching.

---

# 🚀 Main Service

The main repository contains the central Gym CRM business logic.

Repository:

* `gym_crm_main_service`
* Spring Boot REST API
* Java 17
* Maven
* PostgreSQL
* Spring Data JPA / Hibernate
* Spring Security
* JWT
* Eureka Client
* ActiveMQ
* Caffeine
* Spring Boot Actuator
* Prometheus metrics
* Cucumber
* JUnit
* H2 for testing

The project is organized into dedicated packages for controllers, DAOs, DTOs, exceptions, facades, filters, handlers, mappers, models, services, and utilities.

---

# 👥 User Roles

The CRM is designed around different types of users participating in gym operations.

Typical domain responsibilities include:

### Administrator

Responsible for system-level management and administrative operations.

Possible responsibilities include:

* User management
* Trainer management
* Trainee management
* Training management
* Training type management
* System administration

### Trainer

Responsible for managing assigned trainees and conducting training sessions.

Responsibilities include:

* Managing training sessions
* Working with assigned trainees
* Monitoring personal workload
* Managing training-related information

### Trainee

Represents a gym member receiving training.

Responsibilities include:

* Viewing personal information
* Working with assigned trainers
* Participating in training sessions
* Viewing training information

> Exact authorization rules are enforced through Spring Security configuration and controller-level security rather than relying solely on the frontend.

---

# ✨ Core Features

## Backend

* RESTful API
* Layered architecture
* JWT authentication
* Spring Security authorization
* Role-based access control
* PostgreSQL persistence
* JPA / Hibernate
* DTO-based API communication
* Entity-to-DTO mapping
* Input validation
* Global exception handling
* Transaction management
* Caffeine caching
* ActiveMQ messaging
* Eureka service discovery
* Application metrics
* Spring Boot Actuator
* Prometheus integration
* Transaction logging
* Brute-force protection
* Account locking
* JWT token blacklisting
* Password security
* Unit tests
* Integration tests
* Cucumber BDD tests
* H2-based test environment
* Docker support

The dependency configuration confirms Spring Security, JPA, PostgreSQL, Caffeine, Eureka Client, ActiveMQ, Actuator, Prometheus, JWT, OpenAPI, Cucumber, JUnit, and H2 testing support.

---

# 🔐 Authentication & Security

Security is one of the central parts of the application.

The project implements authentication using **JWT (JSON Web Tokens)** together with **Spring Security**.

## Authentication Flow

```text
                 ┌─────────────┐
                 │    Client   │
                 └──────┬──────┘
                        │
                        │ Login credentials
                        ▼
                 ┌─────────────┐
                 │AuthController│
                 └──────┬──────┘
                        │
                        ▼
                 ┌─────────────┐
                 │ AuthService │
                 └──────┬──────┘
                        │
                        ▼
                 ┌─────────────┐
                 │ UserService │
                 └──────┬──────┘
                        │
                        ▼
                 ┌─────────────┐
                 │  Database   │
                 └─────────────┘

                        │
                        │ Successful authentication
                        ▼

                 ┌─────────────┐
                 │ JwtService  │
                 └──────┬──────┘
                        │
                        ▼
                  JWT Access Token
```

The project contains dedicated services for:

* Authentication
* JWT generation/validation
* Brute-force protection
* Token blacklisting
* User details loading

These responsibilities are separated into individual service classes rather than being combined into one authentication component.

---

## JWT Authentication

JWT is used to authenticate subsequent requests.

The project uses the JJWT library and contains dedicated `JwtService` and `JwtServiceImpl` components.

The general flow is:

```text
Login
  │
  ▼
Validate credentials
  │
  ▼
Generate JWT
  │
  ▼
Return token
  │
  ▼
Client sends:
Authorization: Bearer <token>
  │
  ▼
JwtAuthFilter
  │
  ▼
Validate token
  │
  ▼
Load authenticated user
  │
  ▼
Authorize request
```

---

# 🛡️ Brute-Force Protection

The authentication subsystem contains a dedicated:

```text
BruteForceProtectionService
BruteForceProtectionServiceImpl
```

This separates login-attempt protection from the main authentication logic.

The purpose is to prevent repeated failed authentication attempts from being used to continuously attack user accounts.

The implementation also includes account-locking functionality as part of the authentication/security layer.

---

# 🚫 JWT Token Blacklisting

The project contains a dedicated:

```text
TokenBlacklistService
TokenBlacklistServiceImpl
```

This provides a mechanism for invalidating JWT tokens before their natural expiration.

This is especially useful for logout and token revocation scenarios.

```text
Valid JWT
   │
   ▼
User logs out
   │
   ▼
Token added to blacklist
   │
   ▼
Future request
   │
   ▼
Blacklist check
   │
   ├── Token blacklisted → Reject
   │
   └── Token valid       → Continue
```

The token blacklist is implemented as a separate service responsibility rather than being embedded directly inside the authentication controller.

---

# 👤 User Management

User management is represented by:

```text
User
UserController
UserService
UserServiceImpl
UserDetailsServiceImpl
UserDAO
```

The `UserDetailsServiceImpl` integrates user information with Spring Security's authentication process.

This separation allows the system to distinguish between:

* User persistence
* User business logic
* Authentication
* Security-specific user loading
* API communication

The repository contains dedicated user controller, DAO, service, implementation, DTO, and model components.

---

# 🧑‍💼 Trainee Management

The trainee domain is represented by the `Trainee` model and corresponding service/controller/mapper components.

The system provides dedicated functionality for managing trainee information rather than treating trainees as generic users.

Relevant components include:

```text
Trainee
TraineeController
TraineeService
TraineeServiceImpl
TraineeMapper
```

This separation allows trainee-specific business rules to remain isolated from generic user-management logic.

---

# 🏋️ Trainer Management

Trainer management is implemented as a dedicated domain.

The repository contains:

```text
Trainer
TrainerController
TrainerService
TrainerServiceImpl
TrainerDAO
TrainerMapper
```

Trainer functionality is separated from general user management, making it possible to implement trainer-specific business rules.

Examples of responsibilities include:

* Trainer information management
* Trainer assignments
* Training-related operations
* Workload calculation
* Trainer-specific business logic

The dedicated `TrainerWorkloadService` is especially important because workload calculations are kept separate from basic trainer CRUD operations.

---

# 📚 Training Management

Training sessions are represented by the `Training` domain model.

The project contains:

```text
Training
TrainingController
TrainingService
TrainingServiceImpl
TrainingDAO
TrainingMapper
```

The training subsystem is responsible for business operations related to gym training sessions.

This domain can connect the main actors of the system:

```text
Trainee
   │
   │ participates in
   ▼
Training
   │
   │ conducted by
   ▼
Trainer
```

The separation of the Training domain from Trainer and Trainee domains keeps the business model modular.

---

# 🏷️ Training Type Management

Training types are represented by:

```text
TrainingType
TrainingTypeController
TrainingTypeService
TrainingTypeServiceImpl
TrainingTypeDAO
```

A separate training-type domain allows the application to distinguish between the classification of a training and an individual training session.

For example, a training type can represent a category of workout or service, while `Training` represents a concrete scheduled/recorded session.

The repository contains dedicated model, controller, DAO, and service components for this functionality.

---

# 📊 Trainer Workload Management

One of the more specialized parts of the application is trainer workload management.

The project contains a dedicated:

```text
TrainerWorkloadService
TrainerWorkloadServiceImpl
```

Instead of mixing workload calculations with basic trainer CRUD functionality, workload-related business logic is isolated into its own service.

This makes the application easier to extend with additional workload rules and calculations.

For example, workload logic can be based on:

* Number of assigned trainings
* Training duration
* Trainer assignments
* Training schedules
* Other business rules

The dedicated service demonstrates separation of business responsibilities within the application.

---

# 🗄️ Database & Persistence

The main service uses:

* PostgreSQL
* Spring Data JPA
* Hibernate

The project defines JPA-based persistence through dedicated DAO and model layers. PostgreSQL is configured as the runtime database, while H2 is included for testing.

## Persistence Architecture

```text
REST Controller
      │
      ▼
Service Layer
      │
      ▼
DAO Layer
      │
      ▼
JPA / Hibernate
      │
      ▼
PostgreSQL
```

The repository contains a base DAO abstraction together with specialized DAOs for:

* Users
* Trainers
* Trainings
* Training types

---

# 🧱 Base Entity

The project contains a reusable:

```text
BaseEntity
```

This provides a common foundation for domain entities.

Using a shared base entity reduces duplication and creates a consistent structure across persistent models.

The current domain model includes:

```text
BaseEntity
├── User
├── Trainee
├── Trainer
└── Training
```

The repository also contains the `TrainingType` domain model.

---

# 🔄 DTO Layer

The application does not need to expose its persistence entities directly through every API operation.

A dedicated DTO package contains request and response structures:

```text
dto/
├── request/
├── response/
└── BaseUserPayload.java
```

DTOs provide a boundary between:

```text
HTTP API
   │
   ▼
DTO
   │
   ▼
Business Layer
   │
   ▼
Domain Entity
   │
   ▼
Database
```

This approach reduces coupling between the API contract and database/domain implementation.

---

# 🔀 Mapper Layer

The project contains dedicated mapper classes:

```text
TraineeMapper
TrainerMapper
TrainingMapper
```

Their responsibility is to convert between domain objects and API-oriented DTO structures.

For example:

```text
Entity
  │
  ▼
Mapper
  │
  ▼
Response DTO
```

and:

```text
Request DTO
  │
  ▼
Mapper
  │
  ▼
Entity
```

This keeps conversion logic out of controllers and business services.

---

# 🧩 Service Layer

Business logic is encapsulated in dedicated services.

The service package includes:

```text
AbstractUserService
AuthService
BaseService
BruteForceProtectionService
JwtService
TokenBlacklistService
TraineeService
TrainerService
TrainerWorkloadService
TrainingService
TrainingTypeService
UserService
```

Concrete implementations are kept inside:

```text
service/impl/
```

This creates a clear separation between service contracts and their implementations.

---

# 🎯 Facade Layer

The project also contains a dedicated:

```text
facade/
```

layer.

The Facade pattern can be used to provide a simplified entry point to more complicated business operations.

This helps avoid exposing unnecessary internal service interactions directly to controllers.

The presence of a dedicated facade package demonstrates an additional separation between HTTP/API handling and complex business workflows.

---

# ⚠️ Exception Handling

The application contains a centralized:

```text
GlobalExceptionHandler
```

inside the `handler` package.

This provides a centralized location for converting application exceptions into appropriate HTTP responses.

Instead of duplicating error handling inside every controller:

```text
Controller A ─┐
Controller B ─┤
Controller C ─┼──► GlobalExceptionHandler
Controller D ─┤
Controller E ─┘
```

This provides:

* Consistent error responses
* Less duplicated code
* Cleaner controllers
* Centralized exception processing

---

# ✅ Validation

The project includes Hibernate Validator support.

Validation is used to ensure that incoming API data satisfies required business and structural constraints before it reaches deeper layers of the application.

The general request flow is:

```text
HTTP Request
     │
     ▼
Request DTO
     │
     ▼
Validation
     │
     ├── Invalid → Error Response
     │
     ▼
Controller
     │
     ▼
Service
```

Hibernate Validator is included in the Maven dependencies.

---

# 🧵 Filters

The application contains dedicated HTTP filters:

```text
JwtAuthFilter
TransactionLoggingFilter
```

## JwtAuthFilter

Responsible for processing authentication information associated with incoming requests.

It participates in the JWT security pipeline and works together with the JWT service and Spring Security.

## TransactionLoggingFilter

Provides request/transaction-level logging functionality.

Keeping this logic in a filter prevents logging concerns from being duplicated throughout individual controllers.

---

# 📝 Logging & Observability

The application includes dedicated monitoring and observability functionality.

The Maven configuration includes:

* Spring Boot Actuator
* Micrometer
* Prometheus registry

The project also contains a dedicated:

```text
actuator/
```

package and transaction logging filter.

This makes it possible to monitor the application and expose operational metrics rather than relying only on application logs.

---

# ⚡ Caching

The application uses **Caffeine** for caching.

Caffeine is included as a project dependency and can be used to reduce unnecessary repeated database operations for frequently accessed data.

General caching flow:

```text
Request
  │
  ▼
Service
  │
  ▼
Cache
  │
  ├── HIT ─────► Return cached data
  │
  └── MISS
       │
       ▼
    Database
       │
       ▼
   Store result
       │
       ▼
 Return response
```

Caching is particularly useful for data that is frequently requested but does not change on every request.

---

# 📨 Asynchronous Messaging

The main service includes **ActiveMQ** integration through Spring Boot's ActiveMQ starter. The project also includes an embedded ActiveMQ broker dependency for testing.

The purpose of introducing a message broker is to allow services to communicate asynchronously.

Instead of:

```text
Service A
   │
   │ synchronous HTTP call
   ▼
Service B
```

the architecture can support:

```text
Service A
   │
   │ publish message
   ▼
ActiveMQ
   │
   │ consume message
   ▼
Service B
```

This approach can be useful for operations such as:

* Notifications
* Background processing
* Cross-service events
* Decoupled business workflows

The ActiveMQ dependency is explicitly part of the main service configuration.

---

# 🔎 Service Discovery

The main service includes the:

```text
spring-cloud-starter-netflix-eureka-client
```

dependency.

This enables the service to participate in a **service discovery architecture**.

Instead of hard-coding the network location of every service:

```text
Service A → http://192.168.x.x:8081
Service A → http://192.168.x.x:8082
Service A → http://192.168.x.x:8083
```

services can register with Eureka:

```text
                  ┌──────────────────┐
                  │ Eureka Discovery │
                  │      Server      │
                  └────────┬─────────┘
                           │
             ┌─────────────┼─────────────┐
             │             │             │
             ▼             ▼             ▼
        Main Service   Auth Service   Other Service
```

This is especially useful when the system grows into multiple independently deployed services.

---

# 🧪 Testing

Testing is an important part of the project.

The Maven configuration includes:

* Spring Boot Test
* Spring MVC Test
* Spring Security Test
* Spring Data JPA Test
* JUnit Platform
* Cucumber
* Cucumber Spring
* Cucumber JUnit Platform Engine
* H2
* Embedded ActiveMQ broker

---

# 🧪 Unit Testing

Business services can be tested independently from the web layer and database.

Typical structure:

```text
Service Test
    │
    ├── Mock DAO
    ├── Mock Mapper
    ├── Mock Dependencies
    │
    ▼
Service Business Logic
    │
    ▼
Assertions
```

This makes it possible to verify business rules without requiring the entire application stack.

---

# 🥒 Cucumber / BDD Testing

The project includes:

```text
cucumber-java
cucumber-spring
cucumber-junit-platform-engine
```

Cucumber enables behavior-driven testing using scenarios that describe application behavior from a business perspective.

A typical BDD workflow is:

```text
Feature
  │
  ▼
Scenario
  │
  ▼
Given
  │
  ▼
When
  │
  ▼
Then
  │
  ▼
Application behavior
```

This makes complex business requirements easier to express and verify.

---

# 🗃️ H2 Test Database

The application includes H2 as a runtime test dependency.

H2 can provide an isolated in-memory database for tests without requiring a separate PostgreSQL instance.

This is useful for:

* Repository tests
* Integration tests
* Automated test execution
* Isolated test environments

---

# 🧪 Embedded ActiveMQ for Tests

The project also includes:

```text
activemq-broker
```

as a runtime dependency for testing.

This allows messaging-related tests to use an embedded broker instead of requiring an externally running ActiveMQ server.

---

# 📁 Project Structure

The main service follows a structured package organization:

```text
gym_crm_main_service/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   │
│   ├── main/
│   │   │
│   │   ├── java/
│   │   │   └── epam/com/gym/crm/
│   │   │       │
│   │   │       ├── actuator/
│   │   │       │
│   │   │       ├── config/
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── AuthController
│   │   │       │   ├── TraineeController
│   │   │       │   ├── TrainerController
│   │   │       │   ├── TrainingController
│   │   │       │   ├── TrainingTypeController
│   │   │       │   └── UserController
│   │   │       │
│   │   │       ├── dao/
│   │   │       │   ├── filter/
│   │   │       │   ├── impl/
│   │   │       │   ├── BaseDAO
│   │   │       │   ├── TrainerDAO
│   │   │       │   ├── TrainingDAO
│   │   │       │   ├── TrainingTypeDAO
│   │   │       │   └── UserDAO
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   ├── response/
│   │   │       │   └── BaseUserPayload
│   │   │       │
│   │   │       ├── exception/
│   │   │       │
│   │   │       ├── facade/
│   │   │       │
│   │   │       ├── filter/
│   │   │       │   ├── JwtAuthFilter
│   │   │       │   └── TransactionLoggingFilter
│   │   │       │
│   │   │       ├── handler/
│   │   │       │   └── GlobalExceptionHandler
│   │   │       │
│   │   │       ├── mapper/
│   │   │       │   ├── TraineeMapper
│   │   │       │   ├── TrainerMapper
│   │   │       │   └── TrainingMapper
│   │   │       │
│   │   │       ├── model/
│   │   │       │   ├── common/
│   │   │       │   ├── BaseEntity
│   │   │       │   ├── Trainee
│   │   │       │   ├── Trainer
│   │   │       │   ├── Training
│   │   │       │   ├── TrainingType
│   │   │       │   └── User
│   │   │       │
│   │   │       ├── service/
│   │   │       │   ├── impl/
│   │   │       │   ├── AbstractUserService
│   │   │       │   ├── AuthService
│   │   │       │   ├── BaseService
│   │   │       │   ├── BruteForceProtectionService
│   │   │       │   ├── JwtService
│   │   │       │   ├── TokenBlacklistService
│   │   │       │   ├── TraineeService
│   │   │       │   ├── TrainerService
│   │   │       │   ├── TrainerWorkloadService
│   │   │       │   ├── TrainingService
│   │   │       │   ├── TrainingTypeService
│   │   │       │   └── UserService
│   │   │       │
│   │   │       └── utility/
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-local.properties
│   │       ├── application-docker.properties
│   │       ├── application-compose.properties
│   │       ├── application-integration.properties
│   │       ├── application-stg.properties
│   │       ├── application-prod.properties
│   │       ├── schema.sql
│   │       ├── data.sql
│   │       └── docker-seed-data.sql
│   │
│   └── test/
│       ├── java/
│       └── resources/
│
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── .gitignore
```

The package structure is directly reflected in the repository, including dedicated controller, DAO, DTO, filter, handler, mapper, model, and service packages.

---

# 🛠️ Technology Stack

## Programming Language

* Java 17

## Backend

* Spring Boot 4.0.2
* Spring MVC
* Spring Security
* Spring Data JPA
* Hibernate
* Hibernate Validator
* Lombok

## Security

* Spring Security
* JWT
* JJWT
* Brute-force protection
* Token blacklist
* Account locking

## Database

* PostgreSQL
* H2 for testing

## Messaging

* ActiveMQ

## Caching

* Caffeine

## Microservices

* Spring Cloud
* Netflix Eureka Client

## Monitoring

* Spring Boot Actuator
* Micrometer
* Prometheus

## API Documentation

* Springdoc OpenAPI / Swagger UI

## Testing

* JUnit
* Spring Boot Test
* Spring Security Test
* Spring MVC Test
* Spring Data JPA Test
* Cucumber
* Cucumber Spring
* H2
* Embedded ActiveMQ

## Build

* Maven

## Containerization

* Docker

The dependency list in `pom.xml` confirms these technologies and testing libraries.

---

# ⚙️ Configuration Profiles

The project contains several Spring configuration profiles:

```text
application.properties
application-dev.properties
application-local.properties
application-docker.properties
application-compose.properties
application-integration.properties
application-stg.properties
application-prod.properties
```

These profiles allow the application to be configured differently for:

* Local development
* Development environments
* Docker
* Docker Compose
* Integration testing
* Staging
* Production

This prevents environment-specific configuration from being hard-coded into the Java source code.

---

# 🗃️ Database Initialization

The resources directory contains:

```text
schema.sql
data.sql
docker-seed-data.sql
```

These files provide database/schema and seed-data support for different execution environments.

---

# 🚀 Running the Application

## Prerequisites

Make sure the following are installed:

* Java 17+
* Maven
* PostgreSQL
* Docker (optional)
* Git

---

## 1. Clone the Repository

```bash
git clone https://github.com/Rahimjon-A/gym_crm_main_service.git
cd gym_crm_main_service
```

---

## 2. Configure PostgreSQL

Create a PostgreSQL database for the application and configure the required connection properties in the appropriate Spring profile.

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gym_crm
spring.datasource.username=your_username
spring.datasource.password=your_password
```

> Use the configuration already provided by the project profiles as the source of truth for the exact environment-specific property names and values.

---

## 3. Build the Project

Using Maven Wrapper:

### Windows

```bash
mvnw.cmd clean install
```

### Linux / macOS

```bash
./mvnw clean install
```

Or, if Maven is installed globally:

```bash
mvn clean install
```

---

## 4. Run the Application

### Maven

```bash
mvn spring-boot:run
```

### Maven Wrapper - Windows

```bash
mvnw.cmd spring-boot:run
```

### Maven Wrapper - Linux / macOS

```bash
./mvnw spring-boot:run
```

---

# 🐳 Docker

The project contains a Dockerfile based on:

```text
eclipse-temurin:17-jre-alpine
```

The Docker image:

1. Uses a Java 17 JRE Alpine image
2. Creates `/app` as the working directory
3. Copies the built JAR into the container
4. Exposes port `8080`
5. Starts the Spring Boot application

## Dockerfile Flow

```text
Build application
      │
      ▼
Maven generates JAR
      │
      ▼
Docker image
      │
      ▼
Copy JAR → /app/app.jar
      │
      ▼
Expose 8080
      │
      ▼
java -jar app.jar
```

Build the application first:

```bash
mvn clean package
```

Then build the Docker image:

```bash
docker build -t gym-crm-main-service .
```

Run the container:

```bash
docker run -p 8080:8080 gym-crm-main-service
```

---

# 📡 API Documentation

The application includes:

```text
springdoc-openapi-starter-webmvc-ui
```

which provides OpenAPI/Swagger integration.

Swagger/OpenAPI can be used to explore and test the REST API directly from a browser when the application is running.

Typical Swagger UI location:

```text
http://localhost:8080/swagger-ui.html
```

If the configured Springdoc version exposes the newer path, use:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# 🔌 REST API Domains

The main service contains dedicated REST controllers for the following domains:

## Authentication

```text
AuthController
```

Responsible for authentication-related API operations.

---

## Users

```text
UserController
```

Responsible for user-related operations.

---

## Trainees

```text
TraineeController
```

Responsible for trainee-related operations.

---

## Trainers

```text
TrainerController
```

Responsible for trainer-related operations.

---

## Trainings

```text
TrainingController
```

Responsible for training-session operations.

---

## Training Types

```text
TrainingTypeController
```

Responsible for training-type operations.

The repository contains all six controllers in the main controller package.

---

# 🔄 Typical Request Flow

The application follows a layered request-processing pipeline:

```text
                HTTP Request
                     │
                     ▼
              Security Filter
                     │
              ┌──────┴──────┐
              │             │
           JWT valid      JWT invalid
              │             │
              ▼             ▼
          Controller      401/403
              │
              ▼
             DTO
              │
              ▼
          Validation
              │
              ▼
           Service
              │
              ▼
            Mapper
              │
              ▼
             DAO
              │
              ▼
          PostgreSQL
              │
              ▼
          Entity/Data
              │
              ▼
            Mapper
              │
              ▼
        Response DTO
              │
              ▼
        HTTP Response
```

This separation is supported by the repository's dedicated filters, controllers, DTOs, services, mappers, DAOs, and domain models.

---

# 🧠 Business Logic Separation

One of the main architectural principles of the project is keeping business logic outside controllers.

Instead of:

```java
@PostMapping
public ResponseEntity<?> create(...) {
    // validation
    // database access
    // business logic
    // mapping
    // error handling
}
```

the application separates responsibilities:

```text
Controller
    │
    ▼
Service
    │
    ├── Business Rules
    ├── Security Rules
    ├── Transactions
    └── Domain Operations
         │
         ▼
       DAO
         │
         ▼
     Database
```

This makes the codebase easier to:

* Test
* Maintain
* Extend
* Refactor
* Debug

---

# 🔒 Separation of Security Responsibilities

Security concerns are also separated into dedicated components:

```text
AuthService
     │
     ├── Authentication
     │
     ▼
JwtService
     │
     ├── JWT processing
     │
     ▼
JwtAuthFilter
     │
     ├── Request authentication
     │
     ▼
Spring Security
     │
     ├── Authorization
     │
     ▼
BruteForceProtectionService
     │
     ├── Failed login protection
     │
     ▼
TokenBlacklistService
     │
     └── Token revocation
```

The repository explicitly contains these dedicated components.

---

# 🌐 Supporting Microservices

The Gym CRM is designed as a broader microservice ecosystem rather than only a standalone Spring Boot application.

The main service represents the **core gym-management domain**, while supporting services can be deployed independently and communicate with the core system through service-to-service mechanisms.

The architecture is designed to allow responsibilities to be separated into independently maintained services.

A conceptual deployment looks like:

```text
                       ┌──────────────────┐
                       │   Client / UI    │
                       └────────┬─────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │    Main CRM Service   │
                    │                       │
                    │ Users                │
                    │ Trainees             │
                    │ Trainers             │
                    │ Trainings            │
                    │ Training Types        │
                    │ Workload              │
                    └───────────┬───────────┘
                                │
              ┌─────────────────┼──────────────────┐
              │                 │                  │
              ▼                 ▼                  ▼
       ┌─────────────┐   ┌─────────────┐   ┌─────────────┐
       │ Auth /      │   │ Notification│   │ Supporting  │
       │ Security    │   │ / Messaging │   │ Services    │
       │ Services    │   │ Services    │   │             │
       └─────────────┘   └─────────────┘   └─────────────┘
              │                 │                  │
              └─────────────────┼──────────────────┘
                                │
                                ▼
                     ┌────────────────────┐
                     │ Service Discovery  │
                     │      Eureka        │
                     └────────────────────┘
```

The main service already contains the infrastructure necessary for this style of architecture, including Eureka Client and ActiveMQ integration.

---

# 📨 Inter-Service Communication

The architecture supports multiple communication styles.

## Synchronous Communication

Used when an immediate response is required:

```text
Service A
   │
   │ HTTP request
   ▼
Service B
   │
   │ HTTP response
   ▼
Service A
```

## Asynchronous Communication

ActiveMQ can be used when the producer should not wait for the consumer:

```text
Service A
   │
   │ Publish event
   ▼
 ActiveMQ
   │
   │ Consume event
   ▼
Service B
```

This allows services to remain more loosely coupled.

---

# 📈 Scalability Considerations

The project contains several architectural decisions that make future scaling easier:

### Independent business domains

Trainee, Trainer, Training, Training Type, and User logic are separated into their own components.

### Service interfaces

Business services have interfaces with implementations separated into `service/impl`.

### DAO abstraction

Persistence is separated from business logic.

### DTO boundaries

API contracts are separated from persistence entities.

### Caching

Frequently requested data can be cached using Caffeine.

### Asynchronous messaging

ActiveMQ allows background and event-driven workflows.

### Service discovery

Eureka allows services to discover one another without relying entirely on static addresses.

### Monitoring

Actuator and Prometheus provide application metrics.

### Containerization

The application can be packaged into a Docker image.

---

# 🩺 Monitoring & Health

Spring Boot Actuator is included in the project.

Actuator provides operational endpoints that can be used to monitor the application and inspect its health and runtime information.

Prometheus integration is also configured through Micrometer:

```text
Application
     │
     ▼
Spring Boot Actuator
     │
     ▼
Micrometer
     │
     ▼
Prometheus
     │
     ▼
Monitoring / Dashboards
```

The required Actuator and Prometheus dependencies are present in the Maven configuration.

---

# 🧰 Development Tools

The project uses:

| Tool              | Purpose                          |
| ----------------- | -------------------------------- |
| Java 17           | Backend programming language     |
| Spring Boot       | Application framework            |
| Spring Security   | Authentication and authorization |
| JWT               | Stateless authentication         |
| PostgreSQL        | Production database              |
| Hibernate / JPA   | ORM and persistence              |
| Maven             | Build and dependency management  |
| ActiveMQ          | Asynchronous messaging           |
| Eureka            | Service discovery                |
| Caffeine          | Application caching              |
| Actuator          | Application monitoring           |
| Prometheus        | Metrics collection               |
| Swagger / OpenAPI | API documentation                |
| JUnit             | Unit/integration testing         |
| Cucumber          | BDD testing                      |
| H2                | Test database                    |
| Docker            | Containerization                 |
| Lombok            | Boilerplate reduction            |

---

# 🧪 Test Execution

Run the complete test suite using Maven:

```bash
mvn test
```

or:

```bash
./mvnw test
```

Windows:

```bash
mvnw.cmd test
```

The Maven Surefire configuration includes both standard test classes and Cucumber test classes.

---

# 🧹 Code Quality & Maintainability

The project structure follows several maintainability principles:

* Separation of concerns
* Dependency injection
* Service interfaces
* DAO abstraction
* DTO pattern
* Mapper pattern
* Facade pattern
* Centralized exception handling
* Dedicated security components
* Dedicated filtering
* Dedicated workload service
* Configuration profiles
* Automated tests

These practices make it easier to modify one part of the application without introducing unnecessary changes throughout the codebase.

---

# 🔗 Repository

## Main Service

**Gym CRM Main Service**

Repository:

`https://github.com/Rahimjon-A/gym_crm_main_service`

This repository contains the central Spring Boot Gym CRM application, including the core domain logic, REST API, persistence layer, security, caching, messaging, testing, and monitoring infrastructure.

---

## Microservices Ecosystem

The project is intended to work together with the supporting microservices repository:

`https://github.com/Rahimjon-A/gym-crm-microservices`

> If the repository is moved or renamed, update this reference accordingly.

---

# 📚 Useful References

### Spring Boot

Official Spring Boot documentation:

`https://spring.io/projects/spring-boot`

### Spring Security

`https://spring.io/projects/spring-security`

### Spring Data JPA

`https://spring.io/projects/spring-data-jpa`

### Spring Cloud

`https://spring.io/projects/spring-cloud`

### Netflix Eureka

`https://spring.io/projects/spring-cloud-netflix`

### Apache ActiveMQ

`https://activemq.apache.org/`

### Caffeine

`https://github.com/ben-manes/caffeine`

### PostgreSQL

`https://www.postgresql.org/`

### Hibernate

`https://hibernate.org/`

### JUnit

`https://junit.org/`

### Cucumber

`https://cucumber.io/`

### Docker

`https://www.docker.com/`

### Prometheus

`https://prometheus.io/`

### Springdoc OpenAPI

`https://springdoc.org/`

---

# 📌 Project Highlights

This project demonstrates practical backend development using modern Java and Spring technologies.

### Backend Engineering

* Java 17
* Spring Boot
* REST APIs
* Spring Security
* JPA/Hibernate
* PostgreSQL
* Maven

### Security

* JWT authentication
* Role-based authorization
* Brute-force protection
* Account locking
* Token blacklisting
* Spring Security filters

### Architecture

* Layered architecture
* Service/DAO separation
* DTO pattern
* Mapper pattern
* Facade pattern
* Microservice architecture
* Service discovery
* Asynchronous messaging

### Performance

* Caffeine caching
* Database abstraction
* Asynchronous processing support

### Reliability

* Global exception handling
* Validation
* Transaction logging
* Integration testing
* BDD testing
* Isolated H2 test database
* Embedded ActiveMQ testing

### Operations

* Docker
* Spring Boot Actuator
* Prometheus metrics
* Multiple environment profiles

---

# 🎯 Conclusion

The Gym CRM is more than a basic CRUD application. It was structured as a real backend system with dedicated business domains, security infrastructure, persistence abstraction, testing, monitoring, caching, asynchronous messaging, and microservice support.

The core service provides the foundation for gym operations through:

```text
Users
  │
  ├── Trainees
  │
  ├── Trainers
  │
  ├── Trainings
  │
  ├── Training Types
  │
  └── Trainer Workload
```

while the supporting infrastructure provides:

```text
JWT Security
      │
      ├── Brute-force protection
      ├── Account locking
      └── Token blacklisting

Infrastructure
      │
      ├── PostgreSQL
      ├── Caffeine
      ├── ActiveMQ
      ├── Eureka
      ├── Actuator
      └── Prometheus

Quality
      │
      ├── JUnit
      ├── Integration Tests
      ├── Cucumber
      ├── H2
      └── Embedded ActiveMQ
```

The resulting architecture provides a strong foundation for extending the system with additional gym-management functionality and independently deployable microservices.
