# Gym CRM System (Spring Boot REST API)

## Overview

The Gym CRM System is a Spring Boot REST application for managing trainees, trainers, and trainings.

The application follows a layered architecture and exposes a RESTful API documented with OpenAPI 3. Persistence is
implemented using Spring Data JPA and Hibernate, database schema management is handled by Flyway, and the application
supports both H2 (development) and MySQL (production) databases.

Core domain entities:

- Trainee
- Trainer
- Training
- TrainingType
- User

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── io.github.George_Al3xander
│   │       ├── actuator      → Custom Spring Boot Actuator health indicators
│   │       ├── aspect        → Aspect-oriented exception logging
│   │       ├── auth          → Spring Security user details and authorization interceptors
│   │       ├── config        → Spring Boot, MVC, Security, Swagger and metrics configuration
│   │       ├── controller    → REST API controllers
│   │       ├── dao
│   │       │   ├── impl      → JPA DAO implementations
│   │       │   └── *Dao      → DAO interfaces
│   │       ├── dto
│   │       │   ├── auth      → Authentication and login response DTOs
│   │       │   ├── filter    → Request filter DTOs
│   │       │   ├── trainee   → Trainee request/response DTOs
│   │       │   ├── trainer   → Trainer request/response DTOs
│   │       │   ├── training  → Training request DTOs
│   │       │   ├── user      → User management DTOs
│   │       │   └── *Response → Shared response DTOs
│   │       ├── exception     → Custom exceptions and global exception handler
│   │       ├── facade
│   │       │   ├── impl      → GymFacade implementation
│   │       │   └── GymFacade → Application facade
│   │       ├── filter        → Servlet filters, including JWT authentication
│   │       ├── logging       → REST request/response logging
│   │       ├── mapper        → MapStruct mappers
│   │       ├── model         → JPA entities, including persisted tokens
│   │       ├── service
│   │       │   ├── impl      → Service implementations
│   │       │   └── interfaces
│   │       ├── util          → Utility classes
│   │       ├── validation    → Custom validation annotations and validators
│   │       ├── web           → MVC configuration and interceptors
│   │       └── App           → Spring Boot application entry point
│
│   ├── resources
│   │   ├── application.properties
│   │   ├── application-dev.properties
│   │   ├── application-prod.properties
│   │   ├── logback.xml
│   │   └── db
│   │       └── migration
│   │           ├── V1__create_tables.sql
│   │           └── V2__create_tokens_table.sql
│
│   └── webapp
│       └── WEB-INF
│
└── test
    └── java
        └── io.github.George_Al3xander
            ├── controller
            ├── dao
            ├── facade
            ├── service
            └── util
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

```text
src/main/resources
├── application.properties
├── application-dev.properties
├── application-prod.properties
├── logback.xml
└── db
    └── migration
        ├── V1__create_tables.sql
        └── V2__create_tokens_table.sql
```

# Technologies

## Language

- Java 21

## Frameworks

- Spring Boot
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Spring AOP
- Spring Validation
- Spring Boot Actuator
- Spring Profiles

## Security

- Spring Security
- JWT authentication
- JJWT 0.13.0
- BCrypt password encoding
- Stateless session management
- Brute-force login protection

## Persistence

- Hibernate ORM
- Spring Data JPA
- Flyway

## Database

Development

- H2

Production

- MySQL

## API

- REST
- OpenAPI 3 (springdoc-openapi)
- Swagger UI

## Monitoring

- Spring Boot Actuator
- Micrometer
- Prometheus

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

# Configuration

The application is fully configured using Spring Boot auto-configuration supplemented by custom configuration classes.

Enabled features include:

- Spring Boot auto configuration
- Component scanning
- Spring MVC
- Spring Data JPA
- Spring Security
- JWT request filtering
- Flyway migrations
- REST controllers
- OpenAPI documentation
- CORS configuration
- Request interception
- Spring Boot Actuator
- Micrometer metrics

Security configuration is defined in `WebSecurityConfig`. The application uses stateless authentication with JWT bearer
tokens. Public endpoints include `/login`, Swagger/OpenAPI resources, Actuator endpoints, and trainee/trainer
registration. All other endpoints require authentication.

Password hashing is provided by a Spring-managed `BCryptPasswordEncoder`.

Main annotations used throughout the project include:

- `@Configuration`
- `@ComponentScan`
- `@EnableWebSecurity`
- `@EnableAspectJAutoProxy`
- `@EnableTransactionManagement`
- `@Bean`
- `@Service`
- `@Repository`
- `@RestController`
- `@ControllerAdvice`
- `@Transactional`

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

# Security Configuration

JWT and CORS settings are configured through application properties.

Common properties include:

```properties
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
cors.allowed-origins=${CORS_ALLOWED_ORIGINS}
```

The development profile provides:

```properties
jwt.expiration=1h
cors.allowed-origins=http://localhost:3000
```

The JWT secret should be supplied through the `JWT_SECRET` environment variable rather than committed to the repository.

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

```text
src/main/resources/db/migration
```

Current migrations:

```text
V1__create_tables.sql
V2__create_tokens_table.sql
```

The initial migration creates:

- users
- trainees
- trainers
- trainings
- training_types

The second migration creates the `tokens` table used for persisted JWT session tracking and token revocation.

# REST Architecture

Business logic is exposed through Spring Boot REST controllers.

The REST layer consists of:

- Controllers
- Request DTOs
- Response DTOs
- MapStruct mappers
- Global exception handling
- Request logging interceptor
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

## Token

`Token` represents a persisted JWT session.

Fields:

- id
- token
- tokenType
- revoked
- expired
- user

The token belongs to a `User` through a many-to-one relationship.

Only non-expired and non-revoked tokens are considered active during JWT validation.

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

The application uses MapStruct with Spring integration.

Mapper interfaces include:

- TraineeMapper
- TrainerMapper
- TrainingMapper

Mappings convert between:

- request DTOs
- entities
- response DTOs
- summary DTOs

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
- TokenDao

`TokenDao` is a Spring Data JPA repository used to find persisted JWT tokens and retrieve all active tokens belonging to
a user.

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
- password changes

Passwords are verified with Spring Security's `PasswordEncoder` using BCrypt.

Invalid credentials result in a `BadCredentialsException`.

---

## JwtService

Responsible for:

- generating signed JWT bearer tokens
- persisting issued tokens
- extracting usernames from JWT claims
- validating token signatures and expiration
- checking persisted token revocation state
- revoking the current token
- revoking all active tokens for a user

JWT configuration is provided through `jwt.secret` and `jwt.expiration`.

---

## BruteForceProtectionService

Provides protection against repeated failed login attempts.

The protection:

- tracks attempts by username and remote IP address
- blocks a key after the configured maximum number of failed attempts
- keeps the key blocked for the configured lock duration
- clears the attempt counter after a successful login

Default values are 3 failed attempts and a 5-minute lock period. They can be overridden with:

```properties
security.brute-force.max-attempts=3
security.brute-force.lock-time-minutes=5
```

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

Protected requests are authenticated by Spring Security before reaching the controller and facade. The JWT filter loads
the user from the database and populates the Spring Security context with the user's authorities.

---

# Authentication

The application uses stateless JWT bearer authentication.

## Login

Users authenticate by sending credentials as a JSON request body to:

```http
POST /login
Content-Type: application/json
```

A successful login returns a `LoginResponse` containing a signed JWT:

```json
{
  "token": "<jwt>"
}
```

The token is persisted in the `tokens` table and can subsequently be supplied in the HTTP `Authorization` header:

```http
Authorization: Bearer <token>
```

## JWT Validation

Protected requests pass through `JwtRequestFilter`, which:

1. Reads the `Authorization` header.
2. Extracts the bearer token.
3. Extracts the username from the JWT subject.
4. Loads the user using `DatabaseUserDetailsService`.
5. Validates the JWT signature and expiration.
6. Checks that the persisted token is not expired or revoked.
7. Creates a Spring Security authentication object.

The application uses stateless sessions and does not maintain server-side HTTP sessions.

## Authorization

Users are assigned roles based on their domain type:

- `TRAINEE`
- `TRAINER`

## Public Endpoints

The following resources are publicly accessible:

- `/login`
- `/actuator/**`
- `/swagger-ui/**`
- `/swagger-resources/**`
- `/v2/api-docs/**`
- `/v3/api-docs/**`
- `POST /trainees`
- `POST /trainers`

All other requests require authentication.

## Brute-Force Protection

Login attempts are protected by `BruteForceProtectionService`.

A login key is composed of the username and remote IP address. Failed attempts are tracked and the key is temporarily
blocked after the configured threshold. Successful authentication clears the failed-attempt state.

## Logout

The API supports two logout operations:

- `POST /logout` — revokes the JWT used for the current session.
- `POST /logout-all` — revokes all active JWT tokens belonging to the authenticated user.

Revoked tokens remain persisted but can no longer authenticate requests.

## Password Changes

`PUT /change-password` requires JWT authentication.

The request includes:

- username
- old password
- new password

The old password is verified using BCrypt before the new password is encoded and stored.

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

### AuthenticationController

Responsible for:

- JWT login
- password changes
- current-session logout
- logout from all sessions

---

# Authentication

Protected endpoints require a valid JWT bearer token.

The token must be supplied in the `Authorization` header:

```http
Authorization: Bearer <token>
```

JWT authentication is handled by `JwtRequestFilter` and Spring Security. The filter validates the token, loads the
corresponding user, checks the persisted token state, and establishes the authenticated security context.

Public endpoints are limited to user registration, login, Swagger/OpenAPI resources, and Actuator endpoints.

# OpenAPI Documentation

The REST API is documented using springdoc-openapi.

Documentation is generated automatically from OpenAPI annotations.

Swagger UI provides:

- endpoint documentation
- request examples
- response schemas
- authentication requirements
- response codes

OpenAPI annotations used include:

- @OpenAPIDefinition
- @Tag
- @Operation
- @Parameter
- @ApiResponse

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

# Monitoring

The application exposes operational metrics using Spring Boot Actuator.

Metrics can be exported through Micrometer using the Prometheus registry.

These endpoints can be used for application health checks and monitoring.

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
JWT Authentication Filter (if required)
      │
      ▼
REST Controller
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

The project uses Spring Boot testing support.

Tests cover:

- REST controllers
- service layer
- persistence layer
- JWT authentication
- brute-force login protection
- password encoding
- token persistence and revocation
- Flyway migrations

Frameworks:

- Spring Boot Test
- Spring Data JPA Test
- Mockito
- JUnit 5

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

- Spring Boot application
- RESTful API
- Spring Data JPA
- OpenAPI 3 (springdoc-openapi)
- Swagger UI
- Flyway database migrations
- Spring Boot Actuator
- Micrometer metrics
- Prometheus monitoring
- MapStruct object mapping
- REST request logging
- Spring Profiles
- H2 development environment
- MySQL production support
- Profile-based configuration
- Hibernate schema validation
- Spring Security authentication
- JWT bearer tokens
- persisted token revocation
- stateless sessions
- BCrypt password encoding
- brute-force login protection
- centralized exception handling
- Spring Transaction Management
- Spring AOP
- Constructor-based dependency injection
- Jakarta Validation
- Comprehensive unit testing
- Clean layered architecture