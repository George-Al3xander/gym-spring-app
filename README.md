# Gym CRM System (Spring Core Module)

## Overview

The system manages three core domain entities:

- Trainee
- Trainer
- Training

---

## Project Structure

```
io.github.George_AI3xander
 ├── config        → Spring configuration classes
 ├── dao           → Data Access Objects (persistence layer)
 ├── exception     → Custom exceptions
 ├── model         → Domain entities (Trainee, Trainer, Training)
 ├── service       → Business logic layer
 ├── storage       → In-memory storage (Map-based beans)
 ├── util          → Utility classes (username/password generation, helpers)
 └── App           → Application entry point
```

---

## Resources

```
src/main/resources
 ├── data
 │    └── gym-data.json     → Initial dataset for storage initialization
 ├── application.properties
 └── logback.xml
```

---

## Configuration

The application uses Java-based Spring configuration with annotation-driven component scanning.

### Key annotations:

- `@Configuration`
- `@ComponentScan`
- `@Bean`
- `@Service`
- `@Repository`
- `@Autowired`

---

## Property Configuration

`application.properties`:

```properties
storage.file.path=classpath:data/gym-data.json
```

This property defines the path to the initial dataset used to populate in-memory storage during application startup.

---

## Functional Requirements

### TraineeService

- Create trainee profile
- Update trainee profile
- Delete trainee profile
- Get trainee by id
- Get all trainees

### TrainerService

- Create trainer profile
- Update trainer profile
- Get trainer by id
- Get all trainers

### TrainingService

- Create training profile
- Get training by id
- Get all trainings

---

## Business Rules

### Username Generation

Format:

```
firstName.lastName
```

If username already exists:

```
john.doe
john.doe1
john.doe2
```

### Password Generation

- Random string
- Length: 10 characters
- Generated at profile creation time

---

## Persistence Layer

- Each entity has its own DAO:
    - TraineeDao
    - TrainerDao
    - TrainingDao

- Each DAO uses a dedicated Map-based in-memory storage bean
- Storage is separated per entity type

---

## Storage Initialization

- Storage is implemented as a separate Spring bean
- Initial data is loaded from:

```
src/main/resources/data/gym-data.json
```

- Initialization is performed using lifecycle hooks
- File path is injected using:

```
${storage.file.path}
```

---

## Dependency Injection Rules

- DAO → injected into Service using field autowiring
- Other dependencies → setter injection

---

## Logging

Logging is implemented using SLF4J + Logback.

Covers:

- Application startup
- CRUD operations
- Storage initialization
- Error handling

---

## Testing

Unit tests cover:

- Service layer logic
- DAO operations
- Username generation
- Password generation

### Frameworks:

- JUnit
- Mockito
- Spring Test Context

---

## Key Highlights

- Layered architecture (Service / DAO / Storage)
- In-memory Map-based persistence
- Spring IoC and DI usage
- Bean lifecycle management
- External configuration via properties
- Clean modular structure

