CREATE TABLE tokens
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    token      VARCHAR(512) NOT NULL UNIQUE,
    token_type VARCHAR(50)  NOT NULL,
    revoked    BOOLEAN      NOT NULL DEFAULT FALSE,
    expired    BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id    BIGINT       NOT NULL,

    CONSTRAINT fk_tokens_users
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);