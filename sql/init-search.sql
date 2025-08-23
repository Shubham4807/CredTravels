-- Search Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS search_db;
USE search_db;

-- Search optimized flight view
CREATE TABLE IF NOT EXISTS search_flights (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flight_info_id BIGINT NOT NULL,
    flight_number VARCHAR(10) NOT NULL,
    airline_name VARCHAR(100),
    departure_airport_code VARCHAR(3),
    departure_city VARCHAR(100),
    arrival_airport_code VARCHAR(3),
    arrival_city VARCHAR(100),
    departure_time TIME,
    arrival_time TIME,
    duration_minutes INT,
    operating_days VARCHAR(7),
    base_price DECIMAL(10,2),
    aircraft_model VARCHAR(50),
    services JSON,
    route_popularity_score INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_departure_search (departure_airport_code, operating_days),
    INDEX idx_arrival_search (arrival_airport_code, operating_days),
    INDEX idx_route_search (departure_airport_code, arrival_airport_code),
    FULLTEXT idx_text_search (airline_name, departure_city, arrival_city)
);

-- Airport search optimization
CREATE TABLE IF NOT EXISTS search_airports (
    id BIGINT PRIMARY KEY,
    iata_code VARCHAR(3) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL,
    search_keywords VARCHAR(500), -- Combined searchable text
    popularity_score INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FULLTEXT idx_airport_search (name, city, country, search_keywords)
);

-- Popular routes for caching and recommendations
CREATE TABLE IF NOT EXISTS popular_routes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    departure_airport_code VARCHAR(3),
    arrival_airport_code VARCHAR(3),
    search_count BIGINT DEFAULT 0,
    booking_count BIGINT DEFAULT 0,
    average_price DECIMAL(10,2),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_popular_route (departure_airport_code, arrival_airport_code),
    INDEX idx_popularity (search_count DESC, booking_count DESC)
);

-- Search query logs for analytics
CREATE TABLE IF NOT EXISTS search_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_session VARCHAR(100),
    departure_airport VARCHAR(3),
    arrival_airport VARCHAR(3),
    departure_date DATE,
    max_hops INT,
    results_count INT,
    search_time_ms BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_search_analytics (departure_airport, arrival_airport, created_at)
);

-- Insert sample data
INSERT INTO search_flights (flight_info_id, flight_number, airline_name, departure_airport_code, 
                           departure_city, arrival_airport_code, arrival_city, departure_time, 
                           arrival_time, duration_minutes, operating_days, base_price, aircraft_model) VALUES
(1, 'AI101', 'Air India', 'DEL', 'Delhi', 'BOM', 'Mumbai', '08:00:00', '10:00:00', 120, '1111100', 15000.00, 'Boeing 737-800'),
(2, '6E201', 'IndiGo', 'DEL', 'Delhi', 'BLR', 'Bangalore', '10:30:00', '12:30:00', 120, '1111100', 12000.00, 'Airbus A320neo'),
(3, '9W301', 'Jet Airways', 'BOM', 'Mumbai', 'MAA', 'Chennai', '14:00:00', '16:00:00', 120, '1111100', 18000.00, 'Boeing 777-300ER');

INSERT INTO search_airports (id, iata_code, name, city, country, search_keywords, popularity_score) VALUES
(1, 'DEL', 'Indira Gandhi International Airport', 'Delhi', 'India', 'Delhi Airport IGI Dilli', 100),
(2, 'BOM', 'Chhatrapati Shivaji Maharaj International Airport', 'Mumbai', 'India', 'Mumbai Airport BOM', 95),
(3, 'BLR', 'Kempegowda International Airport', 'Bangalore', 'India', 'Bangalore Airport BLR', 90),
(4, 'MAA', 'Chennai International Airport', 'Chennai', 'India', 'Chennai Airport MAA', 85);

INSERT INTO popular_routes (departure_airport_code, arrival_airport_code, search_count, booking_count, average_price) VALUES
('DEL', 'BOM', 1000, 150, 15000.00),
('DEL', 'BLR', 800, 120, 12000.00),
('BOM', 'MAA', 600, 90, 18000.00);

-- Create indexes for performance
CREATE INDEX idx_flight_number ON search_flights(flight_number);
CREATE INDEX idx_airline_name ON search_flights(airline_name);
CREATE INDEX idx_base_price ON search_flights(base_price);
CREATE INDEX idx_popularity_score ON search_flights(route_popularity_score);
CREATE INDEX idx_last_updated ON search_flights(last_updated);
