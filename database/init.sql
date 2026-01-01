CREATE TABLE IF NOT EXISTS calendar (
    id CHAR(36) PRIMARY KEY,
    address VARCHAR(100),
    location VARCHAR(100),
    date DATE,
    is_online BOOLEAN NOT NULL
);