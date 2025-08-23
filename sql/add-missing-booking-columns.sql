-- Add missing columns to booking table
USE booking_db;

-- Add payment_reference column
ALTER TABLE booking ADD COLUMN payment_reference VARCHAR(100) NULL;

-- Add confirmation_date column
ALTER TABLE booking ADD COLUMN confirmation_date TIMESTAMP NULL;

-- Add cancellation_date column
ALTER TABLE booking ADD COLUMN cancellation_date TIMESTAMP NULL;

-- Verify the changes
DESCRIBE booking;
