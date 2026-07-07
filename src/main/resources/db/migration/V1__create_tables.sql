CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE trainees
(
    id            BIGINT PRIMARY KEY,
    date_of_birth DATE,
    address       VARCHAR(255),

    CONSTRAINT fk_trainees_users
        FOREIGN KEY (id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE training_types
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    training_type_name VARCHAR(255) NOT NULL
);

CREATE TABLE trainers
(
    id             BIGINT PRIMARY KEY,
    specialization BIGINT,

    CONSTRAINT fk_trainers_users
        FOREIGN KEY (id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_trainers_training_types
        FOREIGN KEY (specialization)
            REFERENCES training_types (id)
);

CREATE TABLE trainings
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainee_id       BIGINT,
    trainer_id       BIGINT,
    training_name    VARCHAR(255) NOT NULL,
    training_type_id BIGINT,
    training_date    DATETIME     NOT NULL,
    duration_seconds INT          NOT NULL,

    CONSTRAINT fk_trainings_trainee
        FOREIGN KEY (trainee_id)
            REFERENCES trainees (id),

    CONSTRAINT fk_trainings_trainer
        FOREIGN KEY (trainer_id)
            REFERENCES trainers (id),

    CONSTRAINT fk_trainings_training_type
        FOREIGN KEY (training_type_id)
            REFERENCES training_types (id)
);

INSERT INTO training_types (training_type_name)
VALUES ('CARDIO'),
       ('STRENGTH'),
       ('FLEXIBILITY'),
       ('HIIT');