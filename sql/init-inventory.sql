-- Inventory Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- Flight Inventory Table
CREATE TABLE IF NOT EXISTS flight_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flight_info_id BIGINT NOT NULL,
    flight_date DATE NOT NULL,
    available_seats JSON NOT NULL, -- {"economy": 150, "business": 20, "first": 8}
    pricing JSON NOT NULL, -- {"economy": 15000, "business": 45000, "first": 80000}
    total_capacity JSON NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 1, -- For optimistic locking
    status ENUM('ACTIVE', 'CANCELLED', 'DELAYED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_flight_date (flight_info_id, flight_date),
    INDEX idx_date_status (flight_date, status)
);

-- Inventory Update Log
CREATE TABLE IF NOT EXISTS inventory_update_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flight_inventory_id BIGINT,
    update_type ENUM('MANUAL', 'BOOKING', 'CANCELLATION', 'EVENT') NOT NULL,
    old_values JSON,
    new_values JSON,
    updated_by VARCHAR(100),
    update_reason VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (flight_inventory_id) REFERENCES flight_inventory(id),
    INDEX idx_inventory_updates (flight_inventory_id, created_at DESC)
);

-- Reserved Seats (for booking process)
CREATE TABLE IF NOT EXISTS seat_reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flight_inventory_id BIGINT,
    reservation_id VARCHAR(100) UNIQUE NOT NULL,
    seat_class ENUM('ECONOMY', 'BUSINESS', 'FIRST'),
    seat_count INT NOT NULL,
    reserved_until TIMESTAMP NOT NULL,
    status ENUM('RESERVED', 'CONFIRMED', 'EXPIRED') DEFAULT 'RESERVED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (flight_inventory_id) REFERENCES flight_inventory(id),
    INDEX idx_reservation_expiry (reserved_until, status)
);

-- Insert sample data
INSERT INTO flight_inventory (flight_info_id, flight_date, available_seats, pricing, total_capacity) VALUES
(1, '2024-01-15', '{"economy": 150, "business": 20, "first": 8}', '{"economy": 15000, "business": 45000, "first": 80000}', '{"economy": 150, "business": 20, "first": 8}'),
(2, '2024-01-15', '{"economy": 180, "business": 25, "first": 10}', '{"economy": 12000, "business": 40000, "first": 75000}', '{"economy": 180, "business": 25, "first": 10}'),
(3, '2024-01-15', '{"economy": 120, "business": 15, "first": 6}', '{"economy": 18000, "business": 50000, "first": 90000}', '{"economy": 120, "business": 15, "first": 6}');

-- Create indexes for performance
CREATE INDEX idx_flight_info_date ON flight_inventory(flight_info_id, flight_date);
CREATE INDEX idx_status_date ON flight_inventory(status, flight_date);
CREATE INDEX idx_last_updated ON flight_inventory(last_updated);
