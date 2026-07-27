# Gym CRM System (Spring REST + JPA Module)

## Overview

The Gym CRM System is a RESTful web application for managing gym members, trainers, and trainings.

The application follows a layered architecture built with the Spring Framework and provides REST APIs documented with
OpenAPI (Swagger). Persistence is implemented using JPA/Hibernate with Flyway-managed database migrations. The system
supports profile-based configuration for development and production environments.

Core domain entities:

- Trainee
- Trainer
- Training
- TrainingType
- User

---

# Project Structure

```
io.github.George_AI3xander
├── aspect
│   ├── logging
│   └── exception
│
├── config
│   ├── persistence
│   ├── security
│   ├── swagger
│   ├── web
│   └── profile
│
├── controller
│
├── dao
│   ├── impl
│   └── interfaces
│
├── dto
│   ├── request
│   ├── response
│   ├── mapper
│   └── common
│
├── exception
│
├── facade
│
├── model
│
├── service
│   ├── impl
│   └── interfaces
│
├── util
│
└── App
```

The application follows a classic layered architecture:

```
REST Controller
        │
        ▼
     Facade
        │
        ▼
    Service Layer
        │
        ▼
   Repository (DAO)
        │
        ▼
      Database
```

---

# Resources

```
src/main/resources
├── application-dev.properties
├── application-prod.properties
├── logback.xml
└── db
    └── migration
        └── V1__create_tables.sql
```

---

# Technologies

## Language

- Java 21

## Frameworks

- Spring Framework
- Spring MVC
- Spring Context
- Spring JDBC
- Spring ORM
- Spring AOP
- Spring Transaction Management
- Spring Profiles

## Persistence

- Hibernate ORM
- Jakarta Persistence (JPA)
- Flyway

## Database

Development

- H2

Production

- MySQL

## API

- REST
- OpenAPI 3
- Swagger UI

## Validation

- Jakarta Validation

## Mapping

- MapStruct

## Utilities

- Lombok
- HikariCP

## Logging

- SLF4J
- Logback

---

# Configuration

The application is fully configured using Java-based Spring configuration.

Enabled features include:

- Component scanning
- Spring MVC
- REST controllers
- AspectJ auto proxy
- Transaction management
- Hibernate integration
- Flyway migrations
- JPA EntityManager
- HikariCP datasource
- OpenAPI configuration
- Request interceptors

Main annotations used throughout the project:

- `@Configuration`
- `@ComponentScan`
- `@EnableWebMvc`
- `@EnableAspectJAutoProxy`
- `@EnableTransactionManagement`
- `@Bean`
- `@Service`
- `@Repository`
- `@RestController`
- `@ControllerAdvice`
- `@Transactional`

---

# Spring Profiles

The active profile is selected using:

```bash
-Dspring.profiles.active=dev
```

If no profile is specified, the application starts using the **development** profile.

## Development

Configuration file:

```
application-dev.properties
```

Uses:

- H2 in-memory database
- H2 JDBC driver
- automatic Flyway migration
- SQL logging

## Production

Configuration file:

```
application-prod.properties
```

Uses:

- MySQL
- datasource configured through environment variables
- Flyway migration
- optimized production configuration

---

# Database Configuration

Datasource configuration is loaded automatically from the active Spring profile.

## Development

```
spring.datasource.url=jdbc:h2:mem:store
spring.datasource.username=sa
spring.datasource.password=password
spring.datasource.driver-class-name=org.h2.Driver
```

## Production

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=${DB_DRIVER_CLASS_NAME}
```

Connection pooling is provided by HikariCP.

Hibernate configuration includes:

- schema validation
- SQL logging
- automatic dialect detection

---

# Database Migration

Database schema management is performed using Flyway.

At startup:

1. Flyway checks the database version.
2. Pending SQL migrations are executed.
3. Hibernate validates the resulting schema.
4. Spring initializes the persistence layer.

Migration scripts are located in:

```
src/main/resources/db/migration
```

Current migration:

```
V1__create_tables.sql
```

The initial migration creates:

- users
- trainees
- trainers
- trainings
- training_types

and inserts the default training types required by the application.

---

# REST Architecture

The application exposes its functionality through RESTful endpoints.

The REST layer consists of:

- REST controllers
- request DTOs
- response DTOs
- MapStruct mappers
- centralized exception handling
- request logging
- OpenAPI documentation

Each controller delegates business logic to the `GymFacade`, which acts as the unified entry point for all application
operations.

Controllers return DTOs instead of exposing JPA entities directly, ensuring a clear separation between the API contract
and the persistence model.

Request validation is performed using Jakarta Validation annotations before requests reach the service layer.

All API endpoints produce and consume JSON.

# Domain Model

## User

`User` is the base entity for all system users.

Fields:

- id
- firstName
- lastName
- username
- password
- isActive

Inheritance strategy:

```
JOINED
```

Primary key strategy:

```
GenerationType.IDENTITY
```

---

## Trainee

Extends `User`.

Additional fields:

- dateOfBirth
- address

Relationships:

- One-to-many with `Training`

Represents a gym member participating in training sessions.

---

## Trainer

Extends `User`.

Additional fields:

- specialization (`TrainingType`)

Relationships:

- One-to-many with `Training`

Represents a trainer responsible for conducting training sessions.

---

## Training

Represents a completed or scheduled training session.

Fields:

- id
- trainee
- trainer
- trainingName
- trainingType
- trainingDate
- durationSeconds

Database column mapping:

```
duration_seconds
```

Relationships:

- Many-to-one with `Trainer`
- Many-to-one with `Trainee`
- Many-to-one with `TrainingType`

---

## TrainingType

Represents a specialization available within the gym.

Examples include:

- Fitness
- Yoga
- Boxing
- CrossFit

Training types are initialized automatically during the first Flyway migration.

---

# Data Transfer Objects (DTOs)

The REST layer does not expose JPA entities directly.

Instead, dedicated request and response DTOs define the public API contract.

DTOs are organized into separate packages for incoming requests and outgoing responses.

## Request DTOs

Used for:

- authentication
- trainee creation
- trainer creation
- trainee update
- trainer update
- training creation
- password change
- account activation
- training filtering

Incoming requests are validated using Jakarta Validation annotations before reaching the service layer.

---

## Response DTOs

Response objects expose only the data required by API consumers.

Typical response objects include:

- trainee details
- trainer details
- training information
- generated credentials
- success responses
- error responses
- filtered training lists

Sensitive fields are omitted unless explicitly required.

---

# Object Mapping

The application uses **MapStruct** for object conversion.

Mappings include:

- Entity → Response DTO
- Request DTO → Entity
- Update Request → Existing Entity

Benefits include:

- compile-time mapper generation
- type safety
- reduced boilerplate
- improved maintainability

---

# Persistence Layer

Persistence is implemented using JPA with Hibernate.

Repository interfaces include:

- UserDao
- TraineeDao
- TrainerDao
- TrainingDao
- TrainingTypeDao

Each repository is implemented using `EntityManager`.

Supported operations include:

- create
- update
- delete
- find by id
- find all
- find by username
- custom filtering queries

Additional repository methods provide:

- counting duplicate usernames
- retrieving trainers not assigned to a trainee
- retrieving trainings using filter criteria

Repository lookups by username use JPA's `getSingleResult()`.

If no matching entity exists, JPA throws `NoResultException`.

---

# Business Services

Business logic is encapsulated within the service layer.

## AuthenticationService

Responsible for:

- user authentication
- credential verification

Invalid credentials result in a `BadCredentialsException`.

---

## UserService

Responsible for:

- password reset
- account activation
- account deactivation

---

## TraineeService

Provides operations for:

- creating trainees
- updating trainees
- deleting trainees
- retrieving trainees
- listing trainees

---

## TrainerService

Provides operations for:

- creating trainers
- updating trainers
- retrieving trainers
- listing trainers
- retrieving unassigned trainers

---

## TrainingService

Provides operations for:

- creating trainings
- retrieving trainings
- filtering trainee trainings
- filtering trainer trainings

---

## UsernameGenerator

Responsible for generating unique usernames.

Username format:

```
firstName.lastName
```

Duplicates are resolved by appending a numeric suffix.

Example:

```
john.doe
john.doe1
john.doe2
```

---

## PasswordGenerator

Automatically generates secure random passwords.

Characteristics:

- random
- 10 characters
- generated during user creation
- generated during password reset

---

# GymFacade

The `GymFacade` serves as the primary entry point for the application's business logic.

It coordinates service interactions and exposes a simplified API for the REST controllers.

## Public operations

- create trainee
- create trainer

## Authenticated operations

- retrieve trainee
- retrieve trainer
- update trainee
- update trainer
- delete trainee
- create training
- retrieve trainee trainings
- retrieve trainer trainings
- retrieve unassigned trainers
- reset password
- activate user
- deactivate user

The facade performs authentication before delegating protected operations to the corresponding service.

---

# Authentication

Protected operations require valid user credentials.

Authentication is performed using a dedicated request object containing:

- username
- password

Requests are authenticated by `AuthenticationService` before business logic is executed.

Invalid credentials result in an authentication exception.

---

# User Management

The system supports account management features including:

- password reset
- account activation
- account deactivation

Password reset automatically generates a new random password and persists the updated credentials.

User activation changes the account status without deleting user information.

---

# Training Filtering

Training retrieval endpoints support filtering using multiple optional criteria.

Available filters include:

- start date
- end date
- trainee first name
- trainee last name
- trainer first name
- trainer last name
- training type

Filtering is encapsulated within a dedicated filter DTO and translated into repository queries by the service layer.

---

# Business Rules

## Username Generation

Format:

```
firstName.lastName
```

Examples:

```
john.doe
john.doe1
john.doe2
```

---

## Password Generation

Passwords are:

- randomly generated
- exactly 10 characters long
- generated during user registration
- regenerated during password reset

---

## Validation

Request DTOs and entities use Jakarta Validation.

Common constraints include:

- `@NotBlank`
- `@NotNull`
- `@Positive`
- `@Size`
- `@Email` (where applicable)

Validation failures are handled centrally and returned as REST error responses.

# REST API

The application exposes a RESTful API for managing trainees, trainers, trainings, and user accounts.

The API follows standard REST principles:

- JSON request/response bodies
- Resource-oriented endpoints
- HTTP status codes
- Request validation
- Centralized exception handling

---

## Controllers

The REST layer is organized into dedicated controllers.

### TraineeController

Responsible for:

- creating trainees
- retrieving trainee profiles
- updating trainee profiles
- deleting trainees
- retrieving trainee trainings
- retrieving unassigned trainers
- updating trainee trainer assignments

---

### TrainerController

Responsible for:

- creating trainers
- retrieving trainer profiles
- updating trainer profiles
- retrieving trainer trainings

---

### TrainingController

Responsible for:

- creating trainings

---

### UserController

Responsible for:

- login/authentication
- password reset
- account activation
- account deactivation

---

# Authentication

Protected endpoints require authentication.

Authentication credentials are supplied with each secured request and are verified by the `AuthenticationService` before
business logic is executed.

Authentication failures result in an appropriate HTTP error response.

Public endpoints are limited to user registration and authentication.

---

# OpenAPI Documentation

The REST API is documented using **OpenAPI 3**.

Swagger UI provides interactive API documentation including:

- available endpoints
- request parameters
- request bodies
- response schemas
- response codes
- authentication requirements

The generated documentation stays synchronized with the controller layer through OpenAPI annotations.

---

# Exception Handling

The application provides centralized REST exception handling using `@ControllerAdvice`.

Common exceptions handled include:

- validation failures
- authentication failures
- entity not found
- duplicate resources
- illegal requests
- unexpected server errors

REST error responses provide:

- HTTP status code
- error message
- timestamp
- request information (when applicable)

---

# Request Validation

Incoming REST requests are validated before reaching the business layer.

Validation includes:

- required fields
- string length constraints
- positive numeric values
- valid dates
- non-null references

Validation failures return descriptive HTTP 400 (Bad Request) responses.

---

# Logging

Logging is implemented using SLF4J and Logback.

The application logs:

- application startup
- Flyway migration execution
- incoming REST requests
- outgoing REST responses
- authentication attempts
- business operations
- SQL statements
- handled exceptions
- unexpected server errors

Logging concerns are implemented using Spring AOP and web interceptors, keeping business logic free from logging code.

---

# Aspect-Oriented Programming

The application uses Spring AOP to implement cross-cutting concerns.

Implemented aspects include:

- exception logging
- service-level error reporting

This approach centralizes logging while reducing duplication throughout the service layer.

---

# Transactions

Business services use Spring transaction management.

Transaction strategy:

- read-only transactions for query operations
- read/write transactions for create, update, and delete operations

Transaction boundaries are defined declaratively using `@Transactional`.

---

# Dependency Injection

The application consistently uses constructor-based dependency injection.

Dependencies are managed by the Spring IoC container.

Lombok's `@RequiredArgsConstructor` is used to reduce boilerplate and encourage immutable dependencies.

---

# Application Flow

A typical request follows this sequence:

```
HTTP Request
      │
      ▼
REST Controller
      │
      ▼
Authentication (if required)
      │
      ▼
GymFacade
      │
      ▼
Service Layer
      │
      ▼
Repository (DAO)
      │
      ▼
Database
      │
      ▼
Entity
      │
      ▼
MapStruct Mapper
      │
      ▼
Response DTO
      │
      ▼
HTTP Response
```

---

# Testing

The project includes comprehensive unit tests covering:

## Service Layer

- business logic
- authentication
- password reset
- account activation
- account deactivation
- username generation
- password generation

## Persistence Layer

- CRUD operations
- custom queries
- username lookups
- missing entity handling
- Flyway-backed persistence

## Facade Layer

- controller-facing business operations
- authentication delegation
- service coordination

## REST Layer

Tests verify:

- request validation
- endpoint behavior
- HTTP response codes
- JSON serialization
- exception handling

Frameworks used:

- JUnit 5
- Mockito
- Spring Test

---

# Key Highlights

- Layered architecture
- RESTful API
- Spring MVC
- OpenAPI / Swagger documentation
- Facade pattern
- DTO-based API design
- MapStruct object mapping
- Spring Profiles
- Flyway database migrations
- JPA/Hibernate persistence
- Hibernate schema validation
- H2 development environment
- MySQL production support
- Profile-based datasource configuration
- HikariCP connection pooling
- Authentication layer
- Centralized exception handling
- Spring Transaction Management
- Spring AOP
- Constructor-based dependency injection
- Request validation
- Dynamic training filtering
- Jakarta Validation
- Comprehensive unit testing
- Clean separation of concerns