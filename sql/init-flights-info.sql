-- Flights Info Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS flights_info_db;
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

-- Insert sample data
INSERT INTO airlines (iata_code, name, country, rating, services) VALUES
('AI', 'Air India', 'India', 4.2, '{"wifi": true, "meal": true, "entertainment": true}'),
('6E', 'IndiGo', 'India', 4.5, '{"wifi": false, "meal": false, "entertainment": false}'),
('9W', 'Jet Airways', 'India', 4.0, '{"wifi": true, "meal": true, "entertainment": true}');

INSERT INTO airports (iata_code, name, city, country, timezone) VALUES
('DEL', 'Indira Gandhi International Airport', 'Delhi', 'India', 'Asia/Kolkata'),
('BOM', 'Chhatrapati Shivaji Maharaj International Airport', 'Mumbai', 'India', 'Asia/Kolkata'),
('BLR', 'Kempegowda International Airport', 'Bangalore', 'India', 'Asia/Kolkata'),
('MAA', 'Chennai International Airport', 'Chennai', 'India', 'Asia/Kolkata');

INSERT INTO aircraft (model, manufacturer, capacity, range_km, cruise_speed_kmh) VALUES
('Boeing 737-800', 'Boeing', '{"economy": 150, "business": 20, "first": 0}', 5000, 850),
('Airbus A320neo', 'Airbus', '{"economy": 180, "business": 25, "first": 0}', 6000, 800),
('Boeing 777-300ER', 'Boeing', '{"economy": 300, "business": 40, "first": 8}', 12000, 900);

INSERT INTO flight_info (flight_number, airline_id, departure_airport_id, arrival_airport_id, 
                        departure_time, arrival_time, flight_duration_minutes, operating_days) VALUES
('AI101', 1, 1, 2, '08:00:00', '10:00:00', 120, '1111100'),
('6E201', 2, 1, 3, '10:30:00', '12:30:00', 120, '1111100'),
('9W301', 3, 2, 4, '14:00:00', '16:00:00', 120, '1111100');

-- Create indexes for performance
CREATE INDEX idx_airline_name ON airlines(name);
CREATE INDEX idx_airport_city ON airports(city);
CREATE INDEX idx_flight_status ON flight_info(status);
CREATE INDEX idx_flight_operating_days ON flight_info(operating_days);
