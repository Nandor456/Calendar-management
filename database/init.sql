CREATE TABLE IF NOT EXISTS calendar (
    id UUID PRIMARY KEY,
    address VARCHAR(100),
    location VARCHAR(100),
    date DATE,
    is_online BOOLEAN NOT NULL
);