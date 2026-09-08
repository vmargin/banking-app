CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     mobile_number VARCHAR(20) NOT NULL UNIQUE,
    pin VARCHAR(20) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    balance NUMERIC(15, 2) NOT NULL DEFAULT 0,
    CHECK (balance >= 0)
    );

CREATE TABLE IF NOT EXISTS transactions (
                                            id BIGSERIAL PRIMARY KEY,
                                            user_id BIGINT NOT NULL REFERENCES users(id),
    type VARCHAR(30) NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    CHECK (amount > 0),
    details VARCHAR(255) NOT NULL,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_transaction_type
    CHECK (type IN ('CASH_IN', 'TRANSFER_SENT', 'TRANSFER_RECEIVED'))
    );

CREATE INDEX IF NOT EXISTS idx_transactions_user_id
    ON transactions(user_id);
