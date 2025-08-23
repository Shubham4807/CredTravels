-- Booking Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS booking_db;
USE booking_db;

-- Customer information
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    nationality VARCHAR(50),
    passport_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_email (email)
);

-- Flight bookings
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_reference VARCHAR(10) UNIQUE NOT NULL,
    customer_id BIGINT NOT NULL,
    flight_info_id BIGINT NOT NULL,
    flight_date DATE NOT NULL,
    seat_class ENUM('ECONOMY', 'BUSINESS', 'FIRST') NOT NULL,
    passenger_count INT NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    payment_reference VARCHAR(100),
    booking_status ENUM('RESERVED', 'CONFIRMED', 'CANCELLED', 'COMPLETED') DEFAULT 'RESERVED',
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmation_date TIMESTAMP NULL,
    cancellation_date TIMESTAMP NULL,
    version BIGINT DEFAULT 1, -- For optimistic locking
    special_requests TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    INDEX idx_customer_bookings (customer_id, booking_date DESC),
    INDEX idx_booking_ref (booking_reference),
    INDEX idx_flight_date (flight_info_id, flight_date),
    INDEX idx_payment_status (payment_status, booking_date)
);

-- Passenger details
CREATE TABLE IF NOT EXISTS passengers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    title ENUM('MR', 'MS', 'MRS', 'DR') NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    nationality VARCHAR(50),
    passport_number VARCHAR(20),
    seat_number VARCHAR(10),
    meal_preference VARCHAR(50),
    special_assistance TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    INDEX idx_booking_passengers (booking_id)
);

-- Payment transactions
CREATE TABLE IF NOT EXISTS payment_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    payment_method ENUM('CREDIT_CARD', 'DEBIT_CARD', 'NET_BANKING', 'UPI', 'WALLET') NOT NULL,
    transaction_id VARCHAR(100) UNIQUE NOT NULL,
    gateway_response JSON,
    amount DECIMAL(12,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    status ENUM('INITIATED', 'SUCCESS', 'FAILED', 'CANCELLED') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_booking_payment (booking_id, status)
);

-- Booking modifications log
CREATE TABLE IF NOT EXISTS booking_modifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    modification_type ENUM('PASSENGER_UPDATE', 'DATE_CHANGE', 'SEAT_UPGRADE', 'CANCELLATION') NOT NULL,
    old_values JSON,
    new_values JSON,
    fee_amount DECIMAL(10,2) DEFAULT 0,
    reason VARCHAR(500),
    modified_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id),
    INDEX idx_booking_modifications (booking_id, created_at DESC)
);

-- Insert sample data
INSERT INTO customers (user_id, first_name, last_name, email, phone, nationality) VALUES
('user001', 'John', 'Doe', 'john.doe@email.com', '+91-9876543210', 'Indian'),
('user002', 'Jane', 'Smith', 'jane.smith@email.com', '+91-9876543211', 'Indian'),
('user003', 'Bob', 'Johnson', 'bob.johnson@email.com', '+91-9876543212', 'American');

INSERT INTO bookings (booking_reference, customer_id, flight_info_id, flight_date, seat_class, passenger_count, total_amount) VALUES
('BK001', 1, 1, '2024-01-15', 'ECONOMY', 2, 30000.00),
('BK002', 2, 2, '2024-01-15', 'BUSINESS', 1, 40000.00),
('BK003', 3, 3, '2024-01-15', 'ECONOMY', 3, 54000.00);

INSERT INTO passengers (booking_id, title, first_name, last_name, date_of_birth, nationality) VALUES
(1, 'MR', 'John', 'Doe', '1985-05-15', 'Indian'),
(1, 'MRS', 'Jane', 'Doe', '1987-08-20', 'Indian'),
(2, 'MS', 'Jane', 'Smith', '1990-03-10', 'Indian'),
(3, 'MR', 'Bob', 'Johnson', '1982-12-25', 'American'),
(3, 'MRS', 'Alice', 'Johnson', '1984-07-14', 'American'),
(3, 'MR', 'Charlie', 'Johnson', '2010-11-30', 'American');

INSERT INTO payment_transactions (booking_id, payment_method, transaction_id, amount, status) VALUES
(1, 'CREDIT_CARD', 'TXN001', 30000.00, 'SUCCESS'),
(2, 'NET_BANKING', 'TXN002', 40000.00, 'SUCCESS'),
(3, 'UPI', 'TXN003', 54000.00, 'PENDING');

-- Create indexes for performance
CREATE INDEX idx_customer_name ON customers(first_name, last_name);
CREATE INDEX idx_booking_status ON bookings(booking_status);
CREATE INDEX idx_flight_date_status ON bookings(flight_date, booking_status);
CREATE INDEX idx_payment_method ON payment_transactions(payment_method);
CREATE INDEX idx_transaction_status ON payment_transactions(status);
