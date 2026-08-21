CREATE TABLE trainer_workload
(
    id                 BIGSERIAL PRIMARY KEY,
    trainer_username   VARCHAR(255) NOT NULL UNIQUE,
    trainer_first_name VARCHAR(255) NOT NULL,
    trainer_last_name  VARCHAR(255) NOT NULL,
    trainer_status     BOOLEAN      NOT NULL
);

CREATE TABLE training_year
(
    id                  BIGSERIAL PRIMARY KEY,
    workload_year       INTEGER NOT NULL,
    trainer_workload_id BIGINT  NOT NULL,
    CONSTRAINT fk_training_year_trainer
        FOREIGN KEY (trainer_workload_id)
            REFERENCES trainer_workload (id)
            ON DELETE CASCADE
);

CREATE TABLE training_month
(
    id                        BIGSERIAL PRIMARY KEY,
    workload_month            INTEGER NOT NULL,
    training_summary_duration INTEGER NOT NULL,
    year_workload_id          BIGINT  NOT NULL,
    CONSTRAINT fk_training_month_year
        FOREIGN KEY (year_workload_id)
            REFERENCES training_year (id)
            ON DELETE CASCADE
);
