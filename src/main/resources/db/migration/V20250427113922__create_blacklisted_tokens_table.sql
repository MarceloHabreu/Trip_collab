CREATE TABLE tb_blacklisted_tokens (
    token TEXT PRIMARY KEY,
    expires_at TIMESTAMP NOT NULL
);