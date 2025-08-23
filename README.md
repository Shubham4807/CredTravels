# CredTravels - Complete Travel Booking System

## 🚀 Project Overview

CredTravels is a comprehensive travel booking system built with Spring Boot, featuring a modular architecture that separates concerns into distinct services while maintaining a single application deployment. The system provides flight search, inventory management, booking capabilities, and real-time availability tracking.

## 🏗️ Architecture Overview

### System Design
- **Modular Monolith**: Single Spring Boot application with clear module boundaries
- **Multi-Database**: Separate databases for each module (inventory, flights-info, search, booking)
- **Caching Strategy**: Redis-based caching with application-level caching
- **Load Balancing**: Nginx reverse proxy with rate limiting
- **Search Engine**: Apache Lucene for intelligent flight search

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
# Check application health
curl http://localhost:8080/actuator/health

# Check Nginx health
curl http://localhost/health
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

## 🔌 API Endpoints

### Inventory Management
- `POST /api/inventory/flights/{flightId}/update` - Update flight inventory
- `GET /api/inventory/flights/{flightId}` - Get flight inventory
- `GET /api/inventory/flights/{flightId}/availability` - Check availability
- `PUT /api/inventory/flights/{flightId}/reserve` - Reserve seats
- `PUT /api/inventory/flights/{flightId}/release` - Release reservation
- `GET /api/inventory/flights/search` - Search flights by route
- `POST /api/inventory/batch-update` - Bulk inventory update

### Flights Information
- `GET /api/flights-info/flights/{flightId}` - Get flight details
- `GET /api/flights-info/flights/search` - Search flights by criteria
- `GET /api/flights-info/airlines` - List airlines
- `GET /api/flights-info/airports` - List airports
- `GET /api/flights-info/routes` - Get route information

### Search Service
- `GET /api/search/flights` - Search flights with multi-hop support
- `GET /api/search/flights/direct` - Direct flight search
- `GET /api/search/airports` - Airport search
- `GET /api/search/suggestions` - Airport suggestions
- `POST /api/search/flights/multi-city` - Multi-city search
- `GET /api/search/popular-routes` - Popular routes

### Booking Service
- `POST /api/booking/flights/reserve` - Reserve flight
- `POST /api/booking/flights/confirm` - Confirm booking
- `GET /api/booking/{bookingId}` - Get booking details
- `PUT /api/booking/{bookingId}/modify` - Modify booking
- `DELETE /api/booking/{bookingId}/cancel` - Cancel booking
- `GET /api/booking/user/{userId}` - User bookings
- `POST /api/booking/payment/callback` - Payment callback

### Health & Monitoring
- `GET /actuator/health` - Application health check
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Performance metrics
- `GET /actuator/prometheus` - Prometheus metrics

## 🧪 Testing

### Postman Collection
Import the Postman collection from `postman/CredTravels.postman_collection.json` to test all APIs.

### API Testing Examples
```bash
# Test inventory update
curl -X POST "http://localhost:8080/api/inventory/flights/1/update?flightDate=2024-01-15" \
  -H "Content-Type: application/json" \
  -d '{
    "flightDate": "2024-01-15",
    "availableSeats": {"economy": 150, "business": 20, "first": 8},
    "pricing": {"economy": 15000, "business": 45000, "first": 80000},
    "totalCapacity": {"economy": 150, "business": 20, "first": 8}
  }'

# Test flight search
curl "http://localhost:8080/api/search/flights?from=DEL&to=BOM&date=2024-01-15&maxHops=2&limit=50"

# Test booking reservation
curl -X POST "http://localhost:8080/api/booking/flights/reserve" \
  -H "Content-Type: application/json" \
  -d '{
    "flightInfoId": 1,
    "flightDate": "2024-01-15",
    "seatClass": "ECONOMY",
    "passengerCount": 2,
    "totalAmount": 30000.00
  }'
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
  
  security:
    jwt:
      secret: your-secret-key      # JWT secret key
      expiration: 86400000         # Token expiration (milliseconds)
    rate-limit:
      requests-per-minute: 100     # API rate limiting
      burst-capacity: 200
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

## 🚀 Deployment

### Docker Deployment
```bash
# Build and start all services
docker-compose up -d --build

# View logs
docker-compose logs -f credtravels-app

# Scale application instances
docker-compose up -d --scale credtravels-app=3
```

### Production Considerations
1. **Environment Variables**: Override configuration with environment variables
2. **Resource Limits**: Set appropriate memory and CPU limits
3. **Health Checks**: Monitor application health with actuator endpoints
4. **Logging**: Configure centralized logging (ELK stack)
5. **Monitoring**: Use Prometheus and Grafana for metrics
6. **SSL/TLS**: Configure HTTPS with proper certificates

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
- Security event logging

## 🔒 Security Features

### API Security
- JWT-based authentication
- Role-based access control (RBAC)
- Rate limiting per user/IP
- Input validation and sanitization

### Data Security
- AES-256 encryption for sensitive data
- TLS encryption for data in transit
- PII protection and tokenization
- Comprehensive audit logging

### Payment Security
- PCI compliance considerations
- No storage of payment card details
- 3D Secure integration ready
- Fraud detection system integration

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

---

**CredTravels** - Making travel booking simple, efficient, and reliable! ✈️
