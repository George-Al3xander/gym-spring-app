# Gym CRM System (Spring Core + JPA Module)

## Overview

The Gym CRM System manages three core domain entities:

- Trainee
- Trainer
- Training

The application is built using the Spring Framework with Java-based configuration, Hibernate ORM, JPA, transaction
management, Aspect-Oriented Programming (AOP), Spring Profiles, and supports both H2 (development) and MySQL (
production) databases.

---

## Project Structure

```
io.github.George_AI3xander
 ├── aspect        → Cross-cutting concerns (exception logging)
 ├── config        → Spring, JPA and profile-specific configuration
 ├── dao
 │    ├── impl     → JPA DAO implementations
 │    └── *Dao     → DAO interfaces
 ├── dto           → Data Transfer Objects
 ├── exception     → Custom exceptions
 ├── facade        → Unified application API
 ├── model         → JPA entities
 ├── service
 │    └── impl     → Business logic implementations
 ├── util          → Utility classes
 └── App           → Application entry point
```

---

## Resources

```
src/main/resources
 ├── application.properties
 └── logback.xml
```

---

## Technologies

- Spring Framework
- Spring Context
- Spring ORM
- Spring JDBC
- Spring AOP
- Spring Transaction Management
- Spring Profiles
- Hibernate ORM
- Jakarta Persistence (JPA)
- Jakarta Validation
- H2 Database (development)
- MySQL (production)
- HikariCP
- Lombok
- SLF4J + Logback

---

## Configuration

The application uses Java-based Spring configuration.

### Main features enabled

- Component scanning
- AspectJ auto proxy
- Transaction management
- JPA EntityManager
- HikariCP datasource
- Hibernate integration

### Key annotations

- `@Configuration`
- `@ComponentScan`
- `@Bean`
- `@EnableAspectJAutoProxy`
- `@EnableTransactionManagement`
- `@Profile`
- `@Service`
- `@Repository`
- `@Component`
- `@Transactional`

---

## Spring Profiles

The application supports two runtime profiles.

### Development (`dev`)

Uses an embedded H2 in-memory database.

```
spring.profiles.active=dev
```

### Production (`prod`)

Uses a MySQL database configured through environment variables.

```
spring.profiles.active=prod
```

---

## Database Configuration

### Development

Database:

```
H2 In-Memory
```

Datasource:

```
jdbc:h2:mem:testdb
```

### Production

Database:

```
MySQL
```

Datasource configuration is provided through environment variables:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Connection pooling is provided by HikariCP.

Hibernate configuration includes:

- automatic schema update
- SQL logging enabled
- automatic dialect detection

---

## Domain Model

### User

Base entity containing:

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

### Trainee

Extends `User`.

Additional fields:

- dateOfBirth
- address

### Trainer

Extends `User`.

Additional field:

- specialization (TrainingType)

### Training

Contains:

- trainee
- trainer
- trainingName
- trainingType
- trainingDate
- durationSeconds

### TrainingType

Represents a training specialization.

---

## Persistence Layer

The application uses JPA repositories implemented with `EntityManager`.

DAO interfaces:

- UserDao
- TraineeDao
- TrainerDao
- TrainingDao
- TrainingTypeDao

Each DAO implementation provides standard CRUD operations using JPA.

Additional repository methods include:

### UserDao

- findByUsername()
- countByName()

### TraineeDao

- findByUsername()

### TrainerDao

- findByUsername()
- findUnassignedByTraineeUsername()

### TrainingDao

- findByTraineeUsername()
- findByTrainerUsername()

---

## Business Services

### AuthenticationService

Responsible for user authentication.

### UserService

Provides:

- password reset
- account activation/deactivation

### TraineeService

Provides:

- create trainee
- update trainee
- delete trainee
- find by id
- find by username
- list all trainees

### TrainerService

Provides:

- create trainer
- update trainer
- find by id
- find by username
- list all trainers
- retrieve unassigned trainers

### TrainingService

Provides:

- create training
- find by id
- list all trainings
- filter trainee trainings
- filter trainer trainings

### UsernameGenerator

Generates unique usernames.

---

## GymFacade

`GymFacade` provides a unified entry point to the application's business logic.

### Public operations

- create trainer
- create trainee

### Protected operations (authentication required)

- retrieve trainer
- retrieve trainee
- update trainer
- update trainee
- delete trainee
- add training
- retrieve trainee trainings
- retrieve trainer trainings
- retrieve unassigned trainers
- reset user password
- activate/deactivate user accounts

---

## Authentication

Protected operations require valid credentials.

Authentication is performed using:

```
CredentialsDTO
```

containing:

- username
- password

Before executing any protected operation, the facade delegates authentication to `AuthenticationService`.

If authentication fails, a `BadCredentialsException` is thrown.

---

## User Management

The system supports additional account management features:

- password reset
- activation/deactivation of user accounts

Password reset automatically generates a new random password and persists the updated credentials.

User activation toggles the account status between active and inactive.

---

## Training Filtering

Training queries support filtering by:

- date range
- trainee first name
- trainee last name
- trainer first name
- trainer last name
- training type

Filtering is represented by:

```
TrainingFilter
```

---

## Business Rules

### Username Generation

Format:

```
firstName.lastName
```

Duplicate usernames are resolved by appending a numeric suffix.

Example:

```
john.doe
john.doe1
john.doe2
```

### Password Generation

Passwords are automatically generated during user creation.

Characteristics:

- random
- fixed length of 10 characters

---

## Validation

Entities use Jakarta Validation annotations.

Examples include:

- `@NotBlank`
- `@NotNull`
- `@Positive`
- `@Size`

---

## Transactions

Business services use Spring transaction management.

- Read-only transactions for query operations
- Read/write transactions for data modifications

---

## Aspect-Oriented Programming

An exception logging aspect intercepts service-layer exceptions.

Features include:

- centralized exception logging
- automatic error reporting
- SLF4J integration

---

## Dependency Injection

The project consistently uses constructor injection.

Lombok's `@RequiredArgsConstructor` is used to simplify dependency injection and encourage immutable dependencies.

---

## Application Entry Point

The sample application demonstrates usage of the `GymFacade` by:

- creating a trainee
- automatically generating credentials
- performing authenticated operations
- deleting the trainee

---

## Logging

Logging is implemented using SLF4J and Logback.

Logging covers:

- application startup
- service operations
- exception handling via AOP
- Hibernate SQL output

---

## Testing

Unit tests cover:

- service layer
- DAO layer
- facade layer
- authentication
- username generation
- password generation
- password reset
- user activation/deactivation

Frameworks:

- JUnit 5
- Mockito
- Spring Test

---

## Key Highlights

- Layered architecture
- Facade pattern
- Spring Profiles (dev/prod)
- JPA/Hibernate persistence
- MySQL production support
- H2 development environment
- Environment-based configuration
- Spring Transaction Management
- Spring AOP
- Constructor-based dependency injection
- HikariCP connection pooling
- Authentication layer
- User account management
- Dynamic training filtering
- Jakarta Validation
- Comprehensive unit testing
- Clean separation of concerns