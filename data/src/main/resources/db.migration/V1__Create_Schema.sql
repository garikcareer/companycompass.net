CREATE SCHEMA IF NOT EXISTS `company`;

CREATE TABLE company
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(40) UNIQUE NOT NULL,
    location VARCHAR(100)       NOT NULL
);

CREATE INDEX idx_name ON company (name);