-- =============================================================
-- Script de creacion de base de datos MySQL
-- Generado a partir del Diagrama ER (docs/DIAGRAMS.md)
-- =============================================================

CREATE DATABASE IF NOT EXISTS chn_test
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE chn_test;

-- -------------------------------------------------------------
-- Tabla: user
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
    id              VARCHAR(36)     NOT NULL,
    username        VARCHAR(100)    NOT NULL,
    password_hash   VARCHAR(255)    NOT NULL,
    role            VARCHAR(50)     NOT NULL,
    email           VARCHAR(150)    NOT NULL,
    is_active       BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_username (username),
    UNIQUE KEY uk_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- Tabla: client
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS client (
    id              VARCHAR(36)     NOT NULL,
    name            VARCHAR(100)    NOT NULL,
    last_name       VARCHAR(100)    NOT NULL,
    birthday        DATE            NOT NULL,
    address         VARCHAR(255)    NULL,
    email           VARCHAR(150)    NOT NULL,
    phone_number    VARCHAR(20)     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_client_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- Tabla: loan_term
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS loan_term (
    id              VARCHAR(36)     NOT NULL,
    description     VARCHAR(255)    NOT NULL,
    days            INT             NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- Tabla: loan
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS loan (
    id                  VARCHAR(36)     NOT NULL,
    amount              DECIMAL(12,2)   NOT NULL,
    pending_amount      DECIMAL(12,2)   NOT NULL,
    application_status  VARCHAR(50)     NOT NULL,
    payment_status      VARCHAR(50)     NOT NULL,
    request_date        DATETIME            NULL,
    resolution_notes    VARCHAR(500)    NULL,
    resolution_date     DATE            NULL,
    client_id           VARCHAR(36)     NOT NULL,
    loan_term_id        VARCHAR(36)     NOT NULL,
    user_id             VARCHAR(36)     NULL,
    PRIMARY KEY (id),
    KEY idx_loan_client_id (client_id),
    KEY idx_loan_loan_term_id (loan_term_id),
    KEY idx_loan_user_id (user_id),
    CONSTRAINT fk_loan_client
        FOREIGN KEY (client_id) REFERENCES client (id),
    CONSTRAINT fk_loan_loan_term
        FOREIGN KEY (loan_term_id) REFERENCES loan_term (id),
    CONSTRAINT fk_loan_user
        FOREIGN KEY (user_id) REFERENCES `user` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------------
-- Tabla: loan_payment
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS loan_payment (
    id              VARCHAR(36)     NOT NULL,
    payment_date    DATE            NOT NULL,
    payment_method  VARCHAR(50)     NOT NULL,
    amount          DECIMAL(12,2)   NOT NULL,
    loan_id         VARCHAR(36)     NOT NULL,
    user_id         VARCHAR(36)     NOT NULL,
    PRIMARY KEY (id),
    KEY idx_loan_payment_loan_id (loan_id),
    KEY idx_loan_payment_user_id (user_id),
    CONSTRAINT fk_loan_payment_loan
        FOREIGN KEY (loan_id) REFERENCES loan (id),
    CONSTRAINT fk_loan_payment_user
        FOREIGN KEY (user_id) REFERENCES `user` (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
