-- CredTravels - Combined Database Initialization Script
-- This script creates all 4 databases in a single MySQL instance
-- Memory: 1 GB max

-- Create databases
CREATE DATABASE IF NOT EXISTS inventory_db;
CREATE DATABASE IF NOT EXISTS flights_info_db;
CREATE DATABASE IF NOT EXISTS search_db;
CREATE DATABASE IF NOT EXISTS booking_db;

-- Grant permissions to credtravels user
GRANT ALL PRIVILEGES ON inventory_db.* TO 'credtravels'@'%';
GRANT ALL PRIVILEGES ON flights_info_db.* TO 'credtravels'@'%';
GRANT ALL PRIVILEGES ON search_db.* TO 'credtravels'@'%';
GRANT ALL PRIVILEGES ON booking_db.* TO 'credtravels'@'%';

-- Use inventory_db and create tables
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
    FOREIGN KEY (flight_inventory_id) REFERENCES flight_inventory(id)
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

-- Use flights_info_db and create tables
USE flights_info_db;

-- Airlines
CREATE TABLE IF NOT EXISTS airlines (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    iata_code VARCHAR(3) UNIQUE NOT NULL,
    icao_code VARCHAR(4) UNIQUE,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    logo_url VARCHAR(500),
    rating DECIMAL(3,2),
    services JSON, -- {"wifi": true, "meal": true, "entertainment": true}
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Airports
CREATE TABLE IF NOT EXISTS airports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    iata_code VARCHAR(3) UNIQUE NOT NULL,
    icao_code VARCHAR(4) UNIQUE,
    name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL,
    timezone VARCHAR(50),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    elevation INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Aircraft
CREATE TABLE IF NOT EXISTS aircraft (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    model VARCHAR(50) NOT NULL,
    manufacturer VARCHAR(50),
    capacity JSON NOT NULL, -- {"economy": 150, "business": 20, "first": 8}
    range_km INT,
    cruise_speed_kmh INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Flight Information
CREATE TABLE IF NOT EXISTS flight_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flight_number VARCHAR(10) NOT NULL,
    airline_id BIGINT NOT NULL,
    aircraft_id BIGINT,
    departure_airport_id BIGINT NOT NULL,
    arrival_airport_id BIGINT NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    flight_duration_minutes INT NOT NULL,
    operating_days VARCHAR(7) NOT NULL, -- "1111100" (Mon-Sun)
    effective_from DATE,
    effective_until DATE,
    services JSON, -- {"baggage": "20kg", "meal": "included", "wifi": true}
    status ENUM('ACTIVE', 'SUSPENDED', 'CANCELLED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (airline_id) REFERENCES airlines(id),
    FOREIGN KEY (aircraft_id) REFERENCES aircraft(id),
    FOREIGN KEY (departure_airport_id) REFERENCES airports(id),
    FOREIGN KEY (arrival_airport_id) REFERENCES airports(id),
    UNIQUE KEY unique_flight (flight_number, airline_id),
    INDEX idx_route (departure_airport_id, arrival_airport_id),
    INDEX idx_airline_flight (airline_id, flight_number)
);

-- Flight Routes (for search optimization)
CREATE TABLE IF NOT EXISTS flight_routes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    departure_airport_id BIGINT NOT NULL,
    arrival_airport_id BIGINT NOT NULL,
    distance_km INT,
    average_duration_minutes INT,
    route_popularity_score INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (departure_airport_id) REFERENCES airports(id),
    FOREIGN KEY (arrival_airport_id) REFERENCES airports(id),
    UNIQUE KEY unique_route (departure_airport_id, arrival_airport_id),
    INDEX idx_departure (departure_airport_id),
    INDEX idx_arrival (arrival_airport_id)
);

-- Use search_db and create tables
USE search_db;

-- Search Flight (optimized for search queries)
CREATE TABLE IF NOT EXISTS search_flight (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flight_info_id BIGINT NOT NULL,
    flight_number VARCHAR(10) NOT NULL,
    airline_name VARCHAR(100) NOT NULL,
    departure_airport_code VARCHAR(3) NOT NULL,
    departure_city VARCHAR(100) NOT NULL,
    arrival_airport_code VARCHAR(3) NOT NULL,
    arrival_city VARCHAR(100) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    duration_minutes INT NOT NULL,
    operating_days VARCHAR(7) NOT NULL,
    base_price DECIMAL(10,2),
    aircraft_model VARCHAR(50),
    services JSON,
    route_popularity_score INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_route_search (departure_airport_code, arrival_airport_code),
    INDEX idx_date_search (departure_time),
    INDEX idx_popularity (route_popularity_score DESC)
);

-- Search Log (for analytics)
CREATE TABLE IF NOT EXISTS search_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    search_query VARCHAR(500) NOT NULL,
    from_airport VARCHAR(3),
    to_airport VARCHAR(3),
    search_date DATE,
    results_count INT,
    search_time_ms INT,
    user_agent VARCHAR(500),
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_search_analytics (from_airport, to_airport, search_date)
);

-- Use booking_db and create tables
USE booking_db;

-- Customer Profile
CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    nationality VARCHAR(50),
    passport_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_user_id (user_id)
);

-- Flight Booking
CREATE TABLE IF NOT EXISTS booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_reference VARCHAR(100) UNIQUE NOT NULL,
    customer_id BIGINT NOT NULL,
    flight_info_id BIGINT NOT NULL,
    flight_date DATE NOT NULL,
    seat_class ENUM('ECONOMY', 'BUSINESS', 'FIRST') NOT NULL,
    passenger_count INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_status ENUM('PENDING', 'PAID', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    booking_status ENUM('CONFIRMED', 'CANCELLED', 'COMPLETED') DEFAULT 'CONFIRMED',
    special_requests TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 1, -- For optimistic locking
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    INDEX idx_booking_ref (booking_reference),
    INDEX idx_customer_bookings (customer_id),
    INDEX idx_flight_date (flight_info_id, flight_date)
);

-- Passenger Details
CREATE TABLE IF NOT EXISTS passenger_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    passport_number VARCHAR(50),
    seat_number VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking(id),
    INDEX idx_booking_passengers (booking_id)
);

-- Payment History
CREATE TABLE IF NOT EXISTS payment_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_id VARCHAR(100),
    payment_status ENUM('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED') NOT NULL,
    payment_details JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES booking(id),
    INDEX idx_booking_payments (booking_id),
    INDEX idx_transaction (transaction_id)
);

-- Insert sample data for testing
USE inventory_db;
INSERT INTO flight_inventory (flight_info_id, flight_date, available_seats, pricing, total_capacity, status) VALUES
(1, '2024-01-15', '{"economy": 150, "business": 20, "first": 8}', '{"economy": 15000, "business": 45000, "first": 80000}', '{"economy": 150, "business": 20, "first": 8}', 'ACTIVE');

USE flights_info_db;
INSERT INTO airlines (iata_code, icao_code, name, country, rating) VALUES
('AI', 'AIC', 'Air India', 'India', 4.2),
('6E', 'IGO', 'IndiGo', 'India', 4.5);

INSERT INTO airports (iata_code, icao_code, name, city, country) VALUES
('DEL', 'VIDP', 'Indira Gandhi International Airport', 'Delhi', 'India'),
('BOM', 'VABB', 'Chhatrapati Shivaji Maharaj International Airport', 'Mumbai', 'India');

USE search_db;
INSERT INTO search_flight (flight_info_id, flight_number, airline_name, departure_airport_code, departure_city, arrival_airport_code, arrival_city, departure_time, arrival_time, duration_minutes, operating_days, base_price) VALUES
(1, 'AI101', 'Air India', 'DEL', 'Delhi', 'BOM', 'Mumbai', '10:00:00', '12:00:00', 120, '1111100', 15000.00);

USE booking_db;
INSERT INTO customer (user_id, first_name, last_name, email, phone) VALUES
('user001', 'John', 'Doe', 'john.doe@example.com', '+91-9876543210');

-- Flush privileges
FLUSH PRIVILEGES;
