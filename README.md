# CredTravels - Complete Travel Booking System

## 📋 **Table of Contents**

### 🚀 **Getting Started**
- [Project Overview](#-project-overview)
- [Architecture Overview](#️-architecture-overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#-project-structure)
- [Quick Start](#-quick-start)

### 🗄️ **Data & Configuration**
- [Database Initialization & Dummy Data](#️-database-initialization--dummy-data)
- [Database Schema](#️-database-schema)
- [Configuration Profiles](#️-configuration-profiles)
- [Application Properties](#application-properties)
- [Database Configuration](#database-configuration)
- [Redis Configuration](#redis-configuration)

### 🔌 **API Documentation**
- [API Endpoints & Detailed Logic](#️-api-endpoints--detailed-logic)
  - [Health & Monitoring](#️-health--monitoring)
  - [Inventory Management](#️-inventory-management)
  - [Flights Information](#️-flights-information)
  - [Search Service](#️-search-service)
  - [Booking Service](#️-booking-service)
  - [Background Processes](#️-background-processes--scheduled-tasks)

### 🚀 **Deployment & Operations**
- [Deployment & Configuration](#️-deployment--configuration)
  - [Prerequisites](#️-prerequisites)
  - [Docker Deployment](#️-docker-deployment)
  - [Production Deployment Guide](#️-production-deployment-guide)
  - [Resource Requirements](#️-resource-requirements)
  - [Monitoring & Health Checks](#️-monitoring--health-checks)
  - [Troubleshooting](#️-troubleshooting)
  - [Scaling & Performance](#️-scaling--performance)
  - [Deployment Checklist](#️-deployment-checklist)

### 🧪 **Testing & Development**
- [Testing](#️-testing)
  - [No Authentication Required](#️-no-authentication-required)
  - [Postman Collection](#postman-collection)
  - [API Testing Examples](#api-testing-examples)

### 🔍 **Monitoring & Observability**
- [Monitoring & Observability](#️-monitoring--observability)
  - [Health Checks](#health-checks)
  - [Metrics](#metrics)
  - [Logging](#logging)

### 🔓 **System Features**
- [Open Access (No Security)](#️-open-access-no-security)
- [Performance Optimization](#️-performance-optimization)
- [Recent Fixes & Improvements](#️-recent-fixes--improvements)
- [Future Enhancements](#️-future-enhancements)

### 🐛 **Support & Troubleshooting**
- [Troubleshooting](#️-troubleshooting-1)
- [Additional Resources](#️-additional-resources)
- [Contributing](#️-contributing)
- [License](#️-license)
- [Support](#️-support)
- [Current System Status](#️-current-system-status)

---

## 🚀 Project Overview

CredTravels is a comprehensive travel booking system built with Spring Boot, featuring a modular architecture that separates concerns into distinct services while maintaining a single application deployment. The system provides flight search, inventory management, booking capabilities, and real-time availability tracking.

## 🏗️ Architecture Overview

### System Design
- **Modular Monolith**: Single Spring Boot application with clear module boundaries
- **Multi-Database**: Separate databases for each module (inventory, flights-info, search, booking)
- **Caching Strategy**: Redis-based caching with application-level caching
- **Load Balancing**: Nginx reverse proxy (no rate limiting)
- **Search Engine**: Apache Lucene for intelligent flight search
- **Open Access**: No authentication, no security, direct API access

### Technology Stack
- **Backend**: Spring Boot 3.x with Java 17
- **Database**: MySQL 8.0 (4 separate databases)
- **Cache**: Redis 7.x
- **Load Balancer**: Nginx
- **Search Engine**: Apache Lucene 8.11.2
- **Containerization**: Docker Compose
- **Connection Management**: HikariCP with circuit breakers

## 📁 Project Structure

```
CredTravelsCode/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── credtravels/
│       │           ├── common/           # Shared components
│       │           │   ├── config/       # Configuration classes
│       │           │   ├── dto/          # Data Transfer Objects
│       │           │   └── exception/    # Exception handling
│       │           ├── inventory/        # Inventory Management Module
│       │           │   ├── controller/   # REST API endpoints
│       │           │   ├── service/      # Business logic
│       │           │   ├── repository/   # Data access
│       │           │   ├── model/        # Entity models
│       │           │   └── dto/          # Module-specific DTOs
│       │           ├── flightsinfo/      # Flights Info Module
│       │           ├── search/           # Search Service Module
│       │           └── booking/          # Booking Service Module
│       └── resources/
│           ├── application.yml           # Application configuration
│           └── sql/                      # Database initialization scripts
├── docker-compose.yml                    # Infrastructure setup
├── Dockerfile                           # Application containerization
├── pom.xml                              # Maven dependencies
├── postman/                             # API testing collection
├── nginx/                               # Load balancer configuration
└── README.md                            # This file
```

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- At least 8GB RAM available

### 1. Clone and Setup
```bash
git clone <repository-url>
cd CredTravelsCode
```

### 2. Start Infrastructure
```bash
docker-compose up -d
```

This will start:
- 4 MySQL databases (ports 3306-3309)
- Redis cache (port 6379)
- Nginx load balancer (port 80)
- CredTravels application (port 8080)

### 3. Build and Run Application
```bash
# Build the application
mvn clean package

# Run the application
java -jar target/credtravels-1.0.0.jar
```

### 4. Verify Setup
```bash
# Check application health (No authentication required)
curl http://localhost:8080/actuator/health

# Check Nginx health
curl http://localhost/health

# Test any API endpoint directly (No login required)
curl http://localhost:8080/api/inventory/flights/1?flightDate=2024-01-15
```

## 🗄️ Database Initialization & Dummy Data

### Database Setup Process

The application uses a **single MySQL instance** with **4 separate databases** for modular architecture:

#### **1. Database Creation Order**
```sql
-- Step 1: Create all databases
CREATE DATABASE IF NOT EXISTS inventory_db;
CREATE DATABASE IF NOT EXISTS flights_info_db;
CREATE DATABASE IF NOT EXISTS search_db;
CREATE DATABASE IF NOT EXISTS booking_db;

-- Step 2: Grant permissions
GRANT ALL PRIVILEGES ON inventory_db.* TO 'credtravels'@'%';
GRANT ALL PRIVILEGES ON flights_info_db.* TO 'credtravels'@'%';
GRANT ALL PRIVILEGES ON search_db.* TO 'credtravels'@'%';
GRANT ALL PRIVILEGES ON booking_db.* TO 'credtravels'@'%';
```

#### **2. Table Creation Order**
The tables are created in **dependency order** to ensure foreign key constraints work correctly:

**Phase 1: Core Reference Tables**
```sql
-- flights_info_db (Independent tables first)
USE flights_info_db;
CREATE TABLE airlines (...);
CREATE TABLE airports (...);
CREATE TABLE aircraft (...);
CREATE TABLE flight_routes (...);
CREATE TABLE flight_info (...);
```

**Phase 2: Inventory Tables**
```sql
-- inventory_db (Depends on flight_info)
USE inventory_db;
CREATE TABLE flight_inventory (...);
CREATE TABLE inventory_update_log (...);
CREATE TABLE seat_reservations (...);
```

**Phase 3: Search Tables**
```sql
-- search_db (Depends on flight_info)
USE search_db;
CREATE TABLE search_flight (...);
CREATE TABLE popular_routes (...);
CREATE TABLE search_analytics (...);
```

**Phase 4: Booking Tables**
```sql
-- booking_db (Depends on customer and flight_info)
USE booking_db;
CREATE TABLE customer (...);
CREATE TABLE booking (...);
CREATE TABLE passenger_detail (...);
CREATE TABLE payment_history (...);
```

#### **3. Dummy Data Population**
After table creation, comprehensive dummy data is populated:

**Step 1: Basic Reference Data**
```sql
-- Airlines (5 records)
INSERT INTO airlines VALUES ('AI', 'AIC', 'Air India', 'India', 4.2, ...);

-- Airports (8 records)
INSERT INTO airports VALUES ('DEL', 'VIDP', 'Indira Gandhi International Airport', ...);

-- Aircraft (5 records)
INSERT INTO aircraft VALUES ('Boeing 737-800', 'Boeing', '{"economy": 150, ...}', ...);
```

**Step 2: Flight Routes & Schedules**
```sql
-- Flight Routes (18 routes)
INSERT INTO flight_routes VALUES ('DEL', 'BOM', 1150, 120, 'DOMESTIC');

-- Flight Information (20 flights)
INSERT INTO flight_info VALUES ('AI101', 1, 1, 'DEL', 'BOM', '10:00:00', ...);
```

**Step 3: Inventory Data**
```sql
-- Flight Inventory (12 records across multiple dates)
INSERT INTO flight_inventory VALUES (1, '2024-01-15', '{"economy": 150, ...}', ...);

-- Seat Reservations (4 test reservations)
INSERT INTO seat_reservations VALUES (1, 'RES001', 'ECONOMY', 2, ...);
```

**Step 4: Search Optimization Data**
```sql
-- Search Flight Data (20 optimized records)
INSERT INTO search_flight VALUES (1, 'AI101', 'Air India', 'DEL', ...);

-- Popular Routes (10 analytics records)
INSERT INTO popular_routes VALUES ('DEL', 'BOM', 1500, 15500.00, ...);
```

**Step 5: Customer & Booking Data**
```sql
-- Customers (5 profiles)
INSERT INTO customer VALUES ('user001', 'John', 'Doe', 'john.doe@example.com', ...);

-- Bookings (8 records with different statuses)
INSERT INTO booking VALUES ('BK20240115001', 1, 1, '2024-01-15', 'ECONOMY', ...);

-- Passenger Details (12 records)
INSERT INTO passenger_detail VALUES (1, 'John', 'Doe', '1990-01-15', ...);

-- Payment History (8 transactions)
INSERT INTO payment_history VALUES (1, 'CREDIT_CARD', 30000.00, 'TXN001', ...);
```

#### **4. Data Population Summary**
| **Database** | **Tables** | **Dummy Records** | **Purpose** |
|--------------|------------|-------------------|-------------|
| **inventory_db** | 3 | 16 | Flight availability & seat reservations |
| **flights_info_db** | 5 | 56 | Airlines, airports, aircraft, routes, schedules |
| **search_db** | 3 | 30 | Search optimization & analytics |
| **booking_db** | 4 | 33 | Customer profiles, bookings, payments |

#### **5. Initialization Files**
- **`sql/init-all-databases.sql`**: Creates databases, tables, and basic sample data
- **`sql/dummy-data.sql`**: Comprehensive dummy data for testing (run separately)

#### **6. Running Dummy Data**
```bash
# After container startup, populate with comprehensive dummy data
docker exec -i credtravels-mysql-prod mysql -uroot -prootpassword < sql/dummy-data.sql
```

## 🗄️ Database Schema

### Inventory Database (`inventory_db`)
- **flight_inventory**: Flight seat availability and pricing
- **inventory_update_log**: Audit trail for inventory changes
- **seat_reservations**: Temporary seat holds during booking

### Flights Info Database (`flights_info_db`)
- **airlines**: Airline information and services
- **airports**: Airport details and coordinates
- **aircraft**: Aircraft specifications and capacity
- **flight_info**: Flight schedules and routes
- **flight_routes**: Route optimization data

### Search Database (`search_db`)
- **search_flights**: Optimized flight search data
- **search_airports**: Airport search optimization
- **popular_routes**: Cached popular route data
- **search_logs**: Search analytics and metrics

### Booking Database (`booking_db`)
- **customers**: Customer information and profiles
- **bookings**: Flight booking records
- **passengers**: Passenger details for each booking
- **payment_transactions**: Payment processing records
- **booking_modifications**: Booking change history

## 🔌 API Endpoints & Detailed Logic

### 🏥 Health & Monitoring
- `GET /actuator/health` - **Application Health Check**
  - **Logic**: Comprehensive health check of all system components
  - **Checks**: Database connectivity (all 4 databases), Redis availability, disk space, application status
  - **Response**: JSON with status UP/DOWN and component details
  - **Use Case**: Load balancer health checks, monitoring systems

- `GET /actuator/info` - **Application Information**
  - **Logic**: Returns application metadata and version information
  - **Response**: Build info, version, description, and configuration details

- `GET /actuator/metrics` - **Performance Metrics**
  - **Logic**: Exposes application metrics for monitoring
  - **Metrics**: HTTP requests, database connections, cache hits/misses, custom business metrics

### 📦 Inventory Management

#### `GET /api/inventory/flights/{flightId}`
- **Purpose**: Retrieve current flight inventory status
- **Logic**: 
  - Fetches inventory from database with caching (Redis L2 cache)
  - Validates flight exists and is active
  - Returns available seats, pricing, and capacity by seat class
- **Parameters**: `flightId` (path), `flightDate` (query)
- **Response**: Flight inventory with seat availability and pricing
- **Cache TTL**: 5 minutes (configurable)

#### `POST /api/inventory/flights/{flightId}/update`
- **Purpose**: Update flight inventory (seats, pricing, capacity)
- **Logic**:
  - Validates inventory data (seats ≤ capacity, pricing > 0)
  - Updates database with optimistic locking (version control)
  - Logs all changes for audit trail
  - Evicts related cache entries
  - Triggers inventory update notifications
- **Request Body**: `InventoryUpdateRequest` with seats, pricing, capacity
- **Response**: Updated inventory with new version
- **Audit**: All changes logged to `inventory_update_log` table

#### `PUT /api/inventory/flights/{flightId}/reserve`
- **Purpose**: Temporarily reserve seats for booking process
- **Logic**:
  - Checks seat availability in real-time
  - Creates temporary reservation with TTL (15 minutes)
  - Updates available seats count
  - Uses optimistic locking to prevent overbooking
  - Returns reservation ID for confirmation
- **Request Body**: `SeatReservationRequest` with seat class, count, customer info
- **Response**: Reservation ID and confirmation details
- **TTL**: 15 minutes (configurable)

#### `PUT /api/inventory/flights/{flightId}/release`
- **Purpose**: Release previously reserved seats
- **Logic**:
  - Validates reservation exists and belongs to user
  - Restores seats to available inventory
  - Updates inventory counts
  - Logs release action for audit
- **Parameters**: `reservationId` in request body
- **Response**: Confirmation of seat release

#### `POST /api/inventory/batch-update`
- **Purpose**: Bulk update multiple flight inventories
- **Logic**:
  - Processes multiple inventory updates in single transaction
  - Validates all updates before committing
  - Uses batch processing for performance
  - Rolls back entire batch if any update fails
- **Request Body**: List of `InventoryUpdateRequest`
- **Response**: Summary of successful/failed updates

### ✈️ Flights Information

#### `GET /api/flights-info/flights/{flightId}`
- **Purpose**: Get comprehensive flight information
- **Logic**:
  - Retrieves flight details from multiple related tables
  - Includes airline, aircraft, route, and schedule information
  - Applies business rules (active flights only, valid dates)
  - Caches frequently accessed flight data
- **Response**: Complete flight information with all related details
- **Cache Strategy**: Flight info cached for 1 hour, schedules for 15 minutes

#### `GET /api/flights-info/flights/search`
- **Purpose**: Search flights by various criteria
- **Logic**:
  - Supports multiple search parameters (origin, destination, date, airline)
  - Uses database indexes for optimal performance
  - Applies filters for active flights and valid schedules
  - Returns paginated results with sorting options
- **Parameters**: Origin, destination, date range, airline, aircraft type
- **Response**: Paginated list of matching flights

#### `GET /api/flights-info/airlines`
- **Purpose**: List all airlines with their services
- **Logic**:
  - Returns active airlines with service information
  - Includes airline codes, names, and service levels
  - Cached for performance
- **Response**: List of airlines with service details

#### `GET /api/flights-info/airports`
- **Purpose**: List airports with location and facility information
- **Logic**:
  - Returns airports with coordinates, timezone, and facilities
  - Supports search by city, country, or airport code
  - Includes airport type (domestic/international)
- **Response**: Airport information with location details

### 🔍 Search Service

#### `GET /api/search/flights`
- **Purpose**: Advanced flight search with multi-hop support
- **Logic**:
  - **Route Finding Algorithm**: Uses Dijkstra's algorithm for optimal route discovery
  - **Multi-hop Support**: Finds flights with up to 3 connections
  - **Layover Optimization**: Considers minimum connection times and layover preferences
  - **Price Aggregation**: Calculates total price including all segments
  - **Availability Check**: Real-time seat availability verification
  - **Sorting Options**: By price, duration, departure time, or airline preference
- **Parameters**: Origin, destination, date, max hops, max layover time
- **Response**: Ranked list of flight options with pricing and availability
- **Performance**: Results cached for 10 minutes, popular routes for 1 hour

#### `GET /api/search/flights/direct`
- **Purpose**: Direct flight search (no connections)
- **Logic**:
  - Filters for non-stop flights only
  - Optimized for speed (no route calculation needed)
  - Real-time availability check
  - Price comparison across airlines
- **Response**: Direct flight options with pricing

#### `GET /api/search/airports`
- **Purpose**: Airport search with autocomplete
- **Logic**:
  - **Fuzzy Search**: Uses Lucene for intelligent text matching
  - **Autocomplete**: Suggests airports as user types
  - **Geographic Search**: Finds airports by city, country, or region
  - **Popularity Ranking**: Prioritizes frequently searched airports
- **Parameters**: Search query, limit, include international
- **Response**: Matching airports with relevance scores

#### `POST /api/search/flights/multi-city`
- **Purpose**: Multi-city flight search (complex itineraries)
- **Logic**:
  - **Itinerary Planning**: Supports multiple destinations and dates
  - **Route Optimization**: Finds optimal sequence of flights
  - **Price Calculation**: Total cost for entire itinerary
  - **Constraint Validation**: Ensures logical travel sequence
- **Request Body**: List of origin-destination-date combinations
- **Response**: Complete itinerary with all flight segments

#### `GET /api/search/popular-routes`
- **Purpose**: Get popular and trending flight routes
- **Logic**:
  - **Analytics**: Based on search frequency and booking patterns
  - **Seasonal Trends**: Considers time-based popularity
  - **Price Trends**: Historical pricing information
  - **Recommendations**: Suggests alternative routes
- **Response**: Popular routes with search frequency and price trends

### 🎫 Booking Service

#### `POST /api/booking/flights/reserve`
- **Purpose**: Create flight reservation
- **Logic**:
  - **Seat Validation**: Checks real-time seat availability
  - **Price Calculation**: Applies current pricing and taxes
  - **Customer Validation**: Verifies customer information
  - **Reservation Creation**: Creates booking record with PENDING status
  - **Seat Hold**: Temporarily reserves seats (15-minute TTL)
  - **Payment Initiation**: Generates payment request
- **Request Body**: Flight details, passenger information, seat preferences
- **Response**: Booking reference and payment details
- **Business Rules**: Maximum 9 passengers per booking, seat class validation

#### `POST /api/booking/flights/confirm`
- **Purpose**: Confirm booking after payment
- **Logic**:
  - **Payment Verification**: Confirms payment completion
  - **Seat Confirmation**: Permanently assigns reserved seats
  - **Inventory Update**: Reduces available seat count
  - **Booking Confirmation**: Changes status to CONFIRMED
  - **Notification**: Sends confirmation emails/SMS
  - **Documentation**: Generates e-tickets and itineraries
- **Parameters**: `bookingReference` and payment confirmation
- **Response**: Confirmed booking with ticket details

#### `GET /api/booking/{bookingId}`
- **Purpose**: Retrieve booking details
- **Logic**:
  - **Data Retrieval**: Fetches complete booking information
  - **Status Tracking**: Current booking and payment status
  - **Passenger Details**: All passenger information
  - **Flight Details**: Complete flight and seat information
- **Response**: Comprehensive booking details
- **Access**: Public endpoint, no authentication required

#### `PUT /api/booking/{bookingId}/modify`
- **Purpose**: Modify existing booking
- **Logic**:
  - **Change Validation**: Checks if modifications are allowed
  - **Availability Check**: Verifies new flight/seat availability
  - **Price Recalculation**: Calculates fare difference
  - **Change Fee**: Applies applicable change fees
  - **Audit Trail**: Logs all modifications
  - **Notification**: Informs customer of changes
- **Request Body**: Modified flight details, passenger changes
- **Response**: Updated booking with new details
- **Business Rules**: Changes allowed up to 24 hours before departure

#### `DELETE /api/booking/{bookingId}/cancel`
- **Purpose**: Cancel flight booking
- **Logic**:
  - **Cancellation Rules**: Validates cancellation eligibility
  - **Refund Calculation**: Determines refund amount based on policy
  - **Seat Release**: Returns seats to available inventory
  - **Status Update**: Changes status to CANCELLED
  - **Refund Processing**: Initiates refund to customer
  - **Audit Log**: Records cancellation reason and details
- **Response**: Cancellation confirmation with refund details
- **Business Rules**: Full refund up to 24 hours, partial refund up to 2 hours

#### `GET /api/booking/user/{userId}`
- **Purpose**: Get user's booking history
- **Logic**:
  - **Data Retrieval**: Fetches all user bookings
  - **Status Filtering**: Supports filtering by booking status
  - **Date Range**: Optional date range filtering
  - **Pagination**: Large result sets are paginated
- **Parameters**: User ID, status filter, date range, page, size
- **Response**: Paginated list of user bookings
- **Access**: Public endpoint, no authentication required

#### `POST /api/booking/payment/callback`
- **Purpose**: Handle payment gateway callbacks
- **Logic**:
  - **Payment Verification**: Validates payment gateway response
  - **Callback Validation**: Verifies callback data integrity
  - **Status Update**: Updates booking payment status
  - **Inventory Update**: Confirms seat allocation
  - **Notification**: Sends confirmation to customer
- **Response**: Payment processing confirmation
- **Access**: Public endpoint, no authentication required

### 🔄 Background Processes & Scheduled Tasks

#### `@Scheduled cleanupExpiredReservations()`
- **Purpose**: Automatically clean up expired seat reservations
- **Schedule**: Every 5 minutes (300,000ms)
- **Logic**:
  - **Expiration Check**: Finds reservations past their TTL (15 minutes)
  - **Seat Release**: Automatically releases expired seats back to inventory
  - **Audit Logging**: Records all cleanup actions for audit trail
  - **Error Handling**: Continues processing even if individual releases fail
  - **Performance**: Batch processing for efficiency
- **Database Query**: `SELECT * FROM seat_reservations WHERE reserved_until < :now AND status = 'RESERVED'`
- **Use Case**: Prevents inventory from being locked by abandoned reservations

#### `@Scheduled updateSearchIndexes()`
- **Purpose**: Update Lucene search indexes with latest flight data
- **Schedule**: Every 30 minutes
- **Logic**:
  - **Data Synchronization**: Syncs database changes to search indexes
  - **Index Optimization**: Maintains optimal search performance
  - **Incremental Updates**: Only processes changed records
  - **Error Recovery**: Handles index corruption gracefully

#### `@Scheduled cacheWarmup()`
- **Purpose**: Pre-populate cache with frequently accessed data
- **Schedule**: Every hour
- **Logic**:
  - **Popular Routes**: Caches frequently searched routes
  - **Flight Schedules**: Pre-loads upcoming flight schedules
  - **Airport Data**: Caches airport information and coordinates
  - **Performance**: Reduces response time for common queries

## 🧪 Testing

### 🔓 **No Authentication Required**
- **All APIs are publicly accessible** without login, JWT tokens, or authentication headers
- **Direct API calls** - simply use the endpoints with appropriate HTTP methods
- **No user registration** or account creation needed
- **Development friendly** - easy testing and integration

### Postman Collection
Import the complete Postman collection from `postman/CredTravels.postman_collection.json` to test all APIs with comprehensive dummy data examples.

**Features of the Collection:**
- ✅ **Complete API Coverage**: All 4 modules with their endpoints
- ✅ **Comprehensive Dummy Data**: Realistic test data for all scenarios
- ✅ **Multiple Test Cases**: Different data sets for each endpoint
- ✅ **Real Request Examples**: Proper JSON payloads for POST/PUT requests
- ✅ **Query Parameters**: Pre-filled with realistic values from dummy data
- ✅ **Environment Variables**: Easy switching between local and production
- ✅ **Organized by Module**: Clear separation of concerns
- ✅ **Health Check Endpoints**: Application monitoring and diagnostics
- ✅ **Background Process Testing**: Scheduled task endpoints for testing
- ✅ **No Authentication Required**: All APIs publicly accessible

**Collection Structure:**
1. **🏥 Health & Monitoring**: Application health, info, and metrics
2. **📦 Inventory Management**: Flight inventory, seat reservations, batch operations
3. **✈️ Flights Information**: Flight details, airlines, airports, routes
4. **🔍 Search Service**: Flight search, multi-hop, multi-city, airport search
5. **🎫 Booking Service**: Flight reservations, confirmations, modifications, cancellations

### API Testing Examples
```bash
# Test inventory update (No authentication required)
curl -X POST "http://localhost:8080/api/inventory/flights/1/update?flightDate=2024-01-15" \
  -H "Content-Type: application/json" \
  -d '{
    "availableSeats": {"economy": 150, "business": 20, "first": 8},
    "pricing": {"economy": 15000, "business": 45000, "first": 80000},
    "totalCapacity": {"economy": 150, "business": 20, "first": 8},
    "updateReason": "Initial setup",
    "updatedBy": "admin"
  }'

# Test flight search (No authentication required)
curl "http://localhost:8080/api/search/flights?from=DEL&to=BOM&date=2024-01-15&maxHops=2&limit=50"

# Test booking reservation (No authentication required)
curl -X POST "http://localhost:8080/api/booking/flights/reserve" \
  -H "Content-Type: application/json" \
  -d '{
    "flightInfoId": 1,
    "flightDate": "2024-01-15",
    "seatClass": "ECONOMY",
    "passengerCount": 2,
    "totalAmount": 30000.00,
    "customerId": "CUST001"
  }'

# Test health check (No authentication required)
curl "http://localhost:8080/actuator/health"
```

## 🔧 Configuration

### Application Properties
Key configuration options in `application.yml`:

```yaml
credtravels:
  search:
    max-hops: 3                    # Maximum connections for multi-hop search
    max-layover-hours: 12          # Maximum layover time
    min-connection-time:
      domestic: 45                 # Minimum connection time (minutes)
      international: 90
    result-limit: 100              # Maximum search results
  
  inventory:
    reservation-timeout: 900        # Seat reservation timeout (seconds)
    cache-ttl: 300                 # Inventory cache TTL (seconds)
    pricing-cache-ttl: 60          # Pricing cache TTL (seconds)
  
  booking:
    payment-timeout: 300           # Payment processing timeout (seconds)
    max-passengers: 9              # Maximum passengers per booking
    seat-reservation-ttl: 900     # Seat reservation TTL (seconds)
  
  # No security configuration required
  # All APIs are publicly accessible without authentication
```

### Database Configuration
Each module connects to its dedicated database:

```yaml
spring:
  datasource:
    inventory:
      jdbc-url: jdbc:mysql://localhost:3306/inventory_db
    flights-info:
      jdbc-url: jdbc:mysql://localhost:3307/flights_info_db
    search:
      jdbc-url: jdbc:mysql://localhost:3308/search_db
    booking:
      jdbc-url: jdbc:mysql://localhost:3309/booking_db
```

### Redis Configuration
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 2000ms
      jedis:
        pool:
          max-active: 20
          max-idle: 10
          min-idle: 5
```

## 🚀 Deployment & Configuration

### 📋 **Prerequisites**
- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- At least 3GB RAM available (reduced from 8GB)

### 🔧 **Configuration Profiles**

The application supports multiple configuration profiles:

#### **Local Development (`local`)**
- **Memory**: 1GB maximum
- **Database**: Local MySQL instance with 4 databases
- **Cache**: Local Redis
- **Configuration**: `application-local.yml`

#### **Production (`prod`)**
- **Memory**: 1GB maximum
- **Database**: Production MySQL instance with 4 databases
- **Cache**: Production Redis cluster
- **Configuration**: `application-prod.yml`
- **SSL/TLS**: Enabled for database connections
- **Externalized Configuration**: Environment variables

#### **Docker (`docker`)**
- **Memory**: 1GB maximum
- **Database**: Single MySQL Docker container with 4 databases
- **Cache**: Redis Docker container
- **Configuration**: Environment variables override

### 🐳 **Docker Deployment**

#### **Local Development Setup**
```bash
# Clone and navigate to project
git clone <repository-url>
cd CredTravelsCode

# Start all services (local configuration)
docker-compose up -d

# This will start:
# - MySQL (port 3306) with 4 databases
# - Redis (port 6379)
# - CredTravels App (port 8080)
# - Nginx (port 80)

# View logs
docker-compose logs -f credtravels-app

# Check health
curl http://localhost:8080/actuator/health
```

#### **Production Deployment**
```bash
# Set up production environment variables
cp env.prod.example .env.prod
# Edit .env.prod with your production values

# Deploy with production configuration
docker-compose -f docker-compose.prod.yml up -d --build

# This will start:
# - MySQL Production (port 3306) with SSL
# - Redis Production (port 6379) with password
# - CredTravels App Production (port 8080)
# - Nginx Production (port 80, 443)

# View production logs
docker-compose -f docker-compose.prod.yml logs -f credtravels-app-prod

# Check production health
curl http://localhost:8080/actuator/health
```

#### **Environment-Specific Commands**

**Local Development:**
```bash
# Start local environment
docker-compose up -d

# Stop local environment
docker-compose down

# Rebuild and restart
docker-compose up -d --build

# View logs
docker-compose logs -f credtravels-app
```

**Production Environment:**
```bash
# Start production environment
docker-compose -f docker-compose.prod.yml up -d --build

# Stop production environment
docker-compose -f docker-compose.prod.yml down

# Scale application instances
docker-compose -f docker-compose.prod.yml up -d --scale credtravels-app=3

# View production logs
docker-compose -f docker-compose.prod.yml logs -f credtravels-app-prod
```

### 🔄 **Configuration Management**

#### **Application Properties**
Key configuration options in `application.yml`:

```yaml
credtravels:
  search:
    max-hops: 3                    # Maximum connections for multi-hop search
    max-layover-hours: 12          # Maximum layover time
    min-connection-time:
      domestic: 45                 # Minimum connection time (minutes)
      international: 90
    result-limit: 100              # Maximum search results
  
  inventory:
    reservation-timeout: 900        # Seat reservation timeout (seconds)
    cache-ttl: 300                 # Inventory cache TTL (seconds)
    pricing-cache-ttl: 60          # Pricing cache TTL (seconds)
  
  booking:
    payment-timeout: 300           # Payment processing timeout (seconds)
    max-passengers: 9              # Maximum passengers per booking
    seat-reservation-ttl: 900     # Seat reservation TTL (seconds)
  
  # No security configuration required
  # All APIs are publicly accessible without authentication
```

#### **Database Configuration**
Each module connects to its dedicated database:

**Local Configuration:**
```yaml
spring:
  datasource:
    inventory:
      jdbc-url: jdbc:mysql://localhost:3306/inventory_db
    flights-info:
      jdbc-url: jdbc:mysql://localhost:3307/flights_info_db
    search:
      jdbc-url: jdbc:mysql://localhost:3308/search_db
    booking:
      jdbc-url: jdbc:mysql://localhost:3309/booking_db
```

**Production Configuration (Single MySQL Instance):**
```yaml
spring:
  datasource:
    inventory:
      jdbc-url: jdbc:mysql://mysql:3306/inventory_db?useSSL=true&allowPublicKeyRetrieval=false&serverTimezone=UTC
    flights-info:
      jdbc-url: jdbc:mysql://mysql:3306/flights_info_db?useSSL=true&allowPublicKeyRetrieval=false&serverTimezone=UTC
    search:
      jdbc-url: jdbc:mysql://mysql:3306/search_db?useSSL=true&allowPublicKeyRetrieval=false&serverTimezone=UTC
    booking:
      jdbc-url: jdbc:mysql://mysql:3306/booking_db?useSSL=true&allowPublicKeyRetrieval=false&serverTimezone=UTC
```

#### **Redis Configuration**
```yaml
spring:
  data:
    redis:
      host: localhost              # localhost for local, redis for Docker
      port: 6379
      timeout: 2000ms
      jedis:
        pool:
          max-active: 20
          max-idle: 10
          min-idle: 5
```

### 🚀 **Production Deployment Guide**

#### **1. Environment Setup**
```bash
# Copy production environment template
cp env.prod.example .env.prod

# Edit environment variables
nano .env.prod
```

**Required Environment Variables:**
```bash
# Database Configuration
MYSQL_ROOT_PASSWORD=your-secure-root-password
MYSQL_USER=credtravels
MYSQL_PASSWORD=your-secure-password

# Redis Configuration
REDIS_PASSWORD=your-redis-password

# Application Configuration
JWT_SECRET=your-jwt-secret-key
JWT_EXPIRATION=86400000

# Optional: Override database URLs
INVENTORY_DB_URL=jdbc:mysql://mysql:3306/inventory_db?useSSL=true
FLIGHTS_INFO_DB_URL=jdbc:mysql://mysql:3306/flights_info_db?useSSL=true
SEARCH_DB_URL=jdbc:mysql:mysql:3306/search_db?useSSL=true
BOOKING_DB_URL=jdbc:mysql:mysql:3306/booking_db?useSSL=true
```

#### **2. Database Initialization**
```bash
# Initialize all databases
docker-compose -f docker-compose.prod.yml up -d mysql

# Wait for MySQL to start, then initialize databases
docker exec -i credtravels-mysql-prod mysql -uroot -p${MYSQL_ROOT_PASSWORD} < sql/init-all-databases.sql
```

#### **3. Application Deployment**
```bash
# Deploy application with production configuration
docker-compose -f docker-compose.prod.yml up -d --build

# Monitor startup
docker-compose -f docker-compose.prod.yml logs -f credtravels-app-prod

# Verify health
curl http://localhost:8080/actuator/health
```

#### **4. Verification Steps**
```bash
# Check all services are running
docker-compose -f docker-compose.prod.yml ps

# Test database connectivity
docker exec credtravels-app-prod nc -zv mysql 3306

# Test Redis connectivity
docker exec credtravels-app-prod redis-cli -h redis ping

# Test API endpoints
curl http://localhost:8080/api/inventory/flights/1?flightDate=2024-01-15
curl http://localhost:8080/api/search/flights?from=DEL&to=BOM&date=2024-01-15
```

### 📊 **Resource Requirements**

#### **Memory Optimization (1GB Total)**
- **Application**: 512MB-1GB (JVM heap)
- **MySQL**: 512MB-1GB
- **Redis**: 256MB-512MB
- **Nginx**: 64MB-128MB

#### **CPU Requirements**
- **Application**: 0.5-1.0 CPU cores
- **MySQL**: 0.25-0.5 CPU cores
- **Redis**: 0.1-0.25 CPU cores
- **Nginx**: 0.1 CPU cores

### 🔍 **Monitoring & Health Checks**

#### **Health Endpoints**
```bash
# Application health
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info

# Application metrics
curl http://localhost:8080/actuator/metrics
```

#### **Log Monitoring**
```bash
# View application logs
docker-compose logs -f credtravels-app

# View production logs
docker-compose -f docker-compose.prod.yml logs -f credtravels-app-prod

# View database logs
docker-compose logs mysql

# View Redis logs
docker-compose logs redis
```

### 🛠️ **Troubleshooting**

#### **Common Issues**

**Application Won't Start:**
```bash
# Check logs
docker-compose logs credtravels-app

# Check resource usage
docker stats

# Restart with rebuild
docker-compose up -d --build
```

**Database Connection Issues:**
```bash
# Check database connectivity
docker exec credtravels-app nc -zv mysql 3306

# Check database logs
docker-compose logs mysql

# Verify database initialization
docker exec -i credtravels-mysql mysql -uroot -p${MYSQL_ROOT_PASSWORD} -e "SHOW DATABASES;"
```

**Redis Connection Issues:**
```bash
# Check Redis connectivity
docker exec credtravels-app redis-cli -h redis ping

# Check Redis logs
docker-compose logs redis
```

### 🔄 **Scaling & Performance**

#### **Horizontal Scaling**
```bash
# Scale application instances
docker-compose -f docker-compose.prod.yml up -d --scale credtravels-app=3

# Load balancer will distribute traffic
```

#### **Performance Monitoring**
```bash
# Monitor resource usage
docker stats

# Check application metrics
curl http://localhost:8080/actuator/metrics

# Monitor database connections
docker exec credtravels-mysql mysql -uroot -p${MYSQL_ROOT_PASSWORD} -e "SHOW PROCESSLIST;"
```

### 📋 **Deployment Checklist**

#### **Pre-Deployment**
- [ ] Environment variables configured
- [ ] Database passwords set
- [ ] SSL certificates ready (production)
- [ ] Resource limits configured
- [ ] Health check endpoints accessible

#### **Post-Deployment**
- [ ] All services running
- [ ] Health checks passing
- [ ] Database connectivity verified
- [ ] Redis connectivity verified
- [ ] API endpoints responding
- [ ] Scheduled tasks running
- [ ] Logs being generated

#### **Production Considerations**
1. **Environment Variables**: Override configuration with environment variables
2. **Resource Limits**: Set appropriate memory and CPU limits (1GB total)
3. **Health Checks**: Monitor application health with actuator endpoints
4. **Logging**: Configure centralized logging (ELK stack)
5. **Monitoring**: Use Prometheus and Grafana for metrics
6. **API Access**: All endpoints publicly accessible (no authentication required)
7. **Backup Strategy**: Regular database backups
8. **SSL/TLS**: Configure HTTPS for production

## 🔍 Monitoring & Observability

### Health Checks
- Application health: `/actuator/health`
- Database connectivity
- Redis availability
- External service status

### Metrics
- Performance metrics: Response times, throughput
- Business metrics: Search conversion, booking success
- Resource metrics: CPU, memory, database connections
- Custom metrics: Flight search algorithm performance

### Logging
- Structured JSON logging
- Correlation IDs for request tracing
- Different log levels for environments
- Business event logging

## 🔓 Open Access (No Security)

### API Access
- **No Authentication Required**: All APIs are publicly accessible
- **No Login Required**: No user registration or login screens
- **No JWT Tokens**: Direct API access without authentication
- **No Rate Limiting**: Unlimited API calls (configure as needed)

### Data Access
- **Public APIs**: All endpoints accessible without credentials
- **No User Management**: No user accounts or profiles required
- **Direct Access**: Simple HTTP requests without headers
- **Development Friendly**: Easy testing and integration

## 🚀 Performance Optimization

### Caching Strategy
- **L1 (Application)**: Caffeine in-memory cache
- **L2 (Redis)**: Distributed cache for search results
- **L3 (Database)**: Query result caching

### Database Optimization
- Composite indexes for complex queries
- Query optimization and prepared statements
- Connection pooling with HikariCP
- Read replica support ready

### Search Performance
- Lucene index optimization
- Result pagination
- Query caching
- Precomputed route caching

## 🔧 Recent Fixes & Improvements

### ✅ **Database Configuration Issues Resolved**
- **Single MySQL Instance**: Migrated from 4 separate MySQL containers to single instance with 4 databases
- **Memory Optimization**: Reduced application memory from 6-8GB to 1GB maximum
- **Entity-Table Mapping**: Fixed all mismatches between JPA entities and database schema
- **JPA Configuration**: Separated configuration per module to avoid conflicts

### ✅ **Scheduled Task Issues Fixed**
- **Repository Method Conflict**: Resolved duplicate `findExpiredReservations()` methods
- **Parameter Binding**: Fixed `:now` parameter binding in scheduled cleanup task
- **Background Processing**: Scheduled tasks now run without errors every 5 minutes

### ✅ **Application Startup Issues Resolved**
- **EntityManagerFactory Conflict**: Added `@Primary` annotation to resolve multiple bean conflicts
- **Database Schema Validation**: All entities now properly map to database tables
- **Health Checks**: Application starts successfully and passes all health checks

### 🔄 **Background Processes Working**
- **Seat Reservation Cleanup**: Runs every 5 minutes, automatically releases expired seats
- **Inventory Updates**: Real-time seat availability and pricing updates
- **Audit Logging**: All changes logged for compliance and debugging

## 🔮 Future Enhancements

### Planned Features
1. **Event-Driven Architecture**: Apache Kafka integration
2. **Real-time Updates**: WebSocket support for live inventory
3. **Machine Learning**: Personalized recommendations
4. **Advanced Search**: AI-powered search algorithms
5. **Mobile App**: React Native mobile application
6. **Analytics Dashboard**: Business intelligence platform

### Scalability Improvements
1. **Microservices Migration**: Gradual service extraction
2. **Service Mesh**: Istio for inter-service communication
3. **Database Sharding**: Horizontal scaling strategy
4. **CDN Integration**: Static content delivery
5. **Auto-scaling**: Kubernetes deployment

## 🐛 Troubleshooting

### Common Issues

#### Database Connection Issues
```bash
# Check database connectivity
docker-compose exec credtravels-app nc -zv mysql-inventory 3306

# View database logs
docker-compose logs mysql-inventory
```

#### Redis Connection Issues
```bash
# Check Redis connectivity
docker-compose exec credtravels-app redis-cli -h redis ping

# View Redis logs
docker-compose logs redis
```

#### Application Startup Issues
```bash
# Check application logs
docker-compose logs credtravels-app

# Check application health
curl http://localhost:8080/actuator/health
```

### Performance Issues
1. **High Response Times**: Check database query performance
2. **Memory Issues**: Monitor heap usage and garbage collection
3. **Cache Misses**: Verify Redis connectivity and cache configuration
4. **Database Bottlenecks**: Check connection pool and query performance

## 📚 Additional Resources

### Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Redis Documentation](https://redis.io/documentation)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Nginx Documentation](https://nginx.org/en/docs/)

### Related Projects
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [Resilience4j](https://resilience4j.readme.io/)
- [Apache Lucene](https://lucene.apache.org/)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the troubleshooting section above

## 🚀 **Current System Status**

### ✅ **System Health: OPERATIONAL**
- **Application**: Running successfully on port 8080
- **Database**: All 4 databases connected and healthy
- **Redis**: Cache operational with proper TTL configuration
- **Scheduled Tasks**: Background processes running without errors
- **API Endpoints**: All REST endpoints accessible and functional
- **Security**: Completely removed - all APIs publicly accessible without authentication

### 📊 **Performance Metrics**
- **Startup Time**: ~13 seconds (optimized)
- **Memory Usage**: 1GB maximum (reduced from 6-8GB)
- **Database Connections**: HikariCP pools configured for optimal performance
- **Cache Hit Rate**: Redis L2 cache with configurable TTLs
- **Response Time**: Sub-second response times for most operations

### 🔧 **Configuration Status**
- **Environment**: Production configuration active
- **Profiles**: `prod` profile with externalized configuration
- **Security**: No authentication required, all APIs publicly accessible
- **Monitoring**: Actuator endpoints enabled for health checks
- **Logging**: Structured logging with correlation IDs

---

**CredTravels** - Making travel booking simple, efficient, and reliable! ✈️

**Last Updated**: August 23, 2025  
**Version**: 1.0.0  
**Status**: Production Ready ✅
