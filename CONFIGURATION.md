# CredTravels Configuration Guide

## 📋 Overview

CredTravels supports multiple environment configurations with optimized memory usage (2-4 GB max). This guide covers all configuration options and deployment scenarios.

## 🏗️ Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│     Nginx       │────│  Spring Boot App │────│     Redis       │
│  Load Balancer  │    │   (4 Modules)    │    │     Cache       │
│   (256MB max)   │    │   (2-4GB max)    │    │   (512MB max)   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                │
                    ┌──────────────────┐
                    │      MySQL       │
                    │   (4 Databases)  │
                    │ (1GB each max)   │
                    │ - inventory_db   │
                    │ - flights_info_db│
                    │ - search_db      │
                    │ - booking_db     │
                    └──────────────────┘
```

## 💾 Memory Requirements

### Total System Memory: 6-8 GB
- **Spring Boot Application**: 2-4 GB (main application)
- **MySQL Databases**: 4 GB total (1 GB each)
- **Redis Cache**: 512 MB
- **Nginx Load Balancer**: 256 MB

### Memory Optimization Features
- G1 Garbage Collector for optimal performance
- Compressed object pointers
- String deduplication
- Optimized string concatenation
- Memory-mapped file I/O for Lucene indexes

## 🔧 Configuration Files

### 1. Base Configuration (`application.yml`)
- Common settings shared across all environments
- Default profile: `local`
- Basic server, logging, and application settings

### 2. Local Development (`application-local.yml`)
- **Profile**: `local`
- **Database**: Localhost connections
- **Memory**: 2-4 GB max
- **Features**: Debug logging, SQL queries, detailed error messages

**Key Settings:**
```yaml
spring:
  profiles:
    active: local
  datasource:
    inventory:
      jdbc-url: jdbc:mysql://localhost:3306/inventory_db
      hikari:
        maximum-pool-size: 10
        minimum-idle: 5
  data:
    redis:
      host: localhost
      port: 6379
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: validate

logging:
  level:
    com.credtravels: DEBUG
    org.hibernate.SQL: DEBUG
```

### 3. Production Configuration (`application-prod.yml`)
- **Profile**: `prod`
- **Database**: Production MySQL clusters with SSL
- **Memory**: 2-4 GB max
- **Features**: Optimized performance, circuit breakers, health checks

**Key Settings:**
```yaml
spring:
  profiles:
    active: prod
  datasource:
    inventory:
      jdbc-url: ${INVENTORY_DB_URL:jdbc:mysql://mysql-inventory:3306/inventory_db?useSSL=true}
      hikari:
        maximum-pool-size: 20
        minimum-idle: 10
        connection-test-query: SELECT 1
  jpa:
    show-sql: false
    properties:
      hibernate:
        cache:
          use_second_level_cache: true
          use_query_cache: true

resilience4j:
  circuitbreaker:
    instances:
      inventory-service:
        sliding-window-size: 10
        failure-rate-threshold: 50
```

### 4. Test Configuration (`application-test.yml`)
- **Profile**: `test`
- **Database**: H2 in-memory databases
- **Memory**: Minimal (for testing)
- **Features**: Fast startup, isolated testing

## 🐳 Docker Configurations

### Local Development (`docker-compose.yml`)
```yaml
services:
  credtravels-app:
    deploy:
      resources:
        limits:
          memory: 4G
          cpus: '2.0'
        reservations:
          memory: 2G
          cpus: '1.0'
  
  mysql-inventory:
    deploy:
      resources:
        limits:
          memory: 1G
          cpus: '0.5'
```

### Production (`docker-compose.prod.yml`)
```yaml
services:
  credtravels-app:
    environment:
      SPRING_PROFILES_ACTIVE: prod
      JWT_SECRET: ${JWT_SECRET:production-secret-key-must-be-changed}
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

## 🚀 Deployment Options

### 1. Local Development
```bash
# Start with local profile (default)
./run.sh

# Or explicitly
./run.sh local
```

**Features:**
- Local MySQL instances
- Debug logging
- Hot reload support
- Development-friendly settings

### 2. Docker Development
```bash
# Start with docker profile
./run.sh docker
```

**Features:**
- Containerized environment
- Isolated services
- Consistent environment
- Easy cleanup

### 3. Production Deployment
```bash
# Start with production profile
./run.sh prod
```

**Features:**
- Production-optimized settings
- SSL database connections
- Circuit breakers
- Health checks
- Resource limits

## 🔐 Security Configuration

### Environment Variables
Create `.env` file from `env.prod.example`:

```bash
# Database Configuration
MYSQL_ROOT_PASSWORD=your-secure-root-password-here
MYSQL_USER=credtravels
MYSQL_PASSWORD=your-secure-db-password-here

# Redis Configuration
REDIS_PASSWORD=your-secure-redis-password-here

# JWT Configuration
JWT_SECRET=your-very-long-and-secure-jwt-secret-key-here-minimum-256-bits
JWT_EXPIRATION=86400000
```

### Security Features
- **SSL/TLS**: Production database connections
- **JWT**: Secure token-based authentication
- **Rate Limiting**: API request throttling
- **Input Validation**: Comprehensive request validation
- **SQL Injection Protection**: Parameterized queries

## 📊 Monitoring & Health Checks

### Health Endpoints
- **Application Health**: `/actuator/health`
- **Database Health**: `/actuator/health/db`
- **Redis Health**: `/actuator/health/redis`
- **Disk Space**: `/actuator/health/diskspace`

### Metrics
- **Prometheus**: `/actuator/prometheus`
- **Application Info**: `/actuator/info`
- **Custom Metrics**: Business-specific KPIs

### Logging
- **Local**: Console and file logging
- **Production**: Structured JSON logging
- **Log Rotation**: Automatic log file management

## 🔧 Performance Tuning

### JVM Options (Dockerfile)
```bash
ENTRYPOINT ["java", \
  "-Xms2g", \                    # Initial heap size
  "-Xmx4g", \                    # Maximum heap size
  "-XX:+UseG1GC", \              # G1 Garbage Collector
  "-XX:MaxGCPauseMillis=200", \  # Max GC pause time
  "-XX:+UseStringDeduplication", \ # String deduplication
  "-XX:+OptimizeStringConcat", \   # String optimization
  "-XX:+UseCompressedOops", \      # Compressed object pointers
  "-XX:+UseCompressedClassPointers", # Compressed class pointers
  "-Djava.security.egd=file:/dev/./urandom", # Secure random
  "-jar", "app.jar"]
```

### Database Optimization
- **Connection Pooling**: HikariCP with optimized settings
- **Batch Processing**: Optimized batch sizes
- **Query Caching**: Second-level cache enabled
- **Indexing**: Proper database indexes

### Cache Strategy
- **Redis**: Distributed caching
- **TTL Configuration**: Environment-specific cache times
- **Cache Invalidation**: Automatic cache cleanup

## 🛠️ Troubleshooting

### Common Issues

1. **Memory Issues**
   ```bash
   # Check memory usage
   docker stats
   
   # Increase memory limits
   # Edit docker-compose.yml or docker-compose.prod.yml
   ```

2. **Database Connection Issues**
   ```bash
   # Check database health
   curl http://localhost/actuator/health/db
   
   # View database logs
   docker-compose logs mysql-inventory
   ```

3. **Application Startup Issues**
   ```bash
   # Check application logs
   docker-compose logs credtravels-app
   
   # Check health endpoint
   curl http://localhost/actuator/health
   ```

### Performance Monitoring
```bash
# Monitor resource usage
docker stats

# Check application metrics
curl http://localhost/actuator/prometheus

# Monitor logs
docker-compose logs -f credtravels-app
```

## 📚 Environment-Specific Features

### Local Development
- ✅ Debug logging
- ✅ SQL query logging
- ✅ Hot reload
- ✅ Development-friendly error messages
- ✅ Local database connections

### Production
- ✅ Performance optimization
- ✅ Circuit breakers
- ✅ Health checks
- ✅ SSL database connections
- ✅ Resource limits
- ✅ Structured logging
- ✅ Security hardening

### Testing
- ✅ In-memory databases
- ✅ Fast startup
- ✅ Isolated environment
- ✅ Test-specific configurations

## 🔄 Profile Switching

### Runtime Profile Change
```bash
# Set active profile
export SPRING_PROFILES_ACTIVE=prod

# Or use environment variable
SPRING_PROFILES_ACTIVE=prod java -jar app.jar
```

### Docker Profile Change
```bash
# Local development
docker-compose up -d

# Production
docker-compose -f docker-compose.prod.yml up -d
```

## 📈 Scaling Considerations

### Horizontal Scaling
- **Application**: Multiple instances behind load balancer
- **Database**: Read replicas for read-heavy workloads
- **Cache**: Redis cluster for high availability

### Vertical Scaling
- **Memory**: Increase heap size (max 4GB recommended)
- **CPU**: Adjust thread pool sizes
- **Database**: Optimize connection pools

### Resource Monitoring
- **Memory Usage**: Monitor heap and off-heap memory
- **CPU Usage**: Track thread pool utilization
- **Database**: Monitor connection pool and query performance
- **Cache**: Track cache hit rates and memory usage

---

## 🎯 Quick Start Commands

```bash
# Local development
./run.sh

# Docker development
./run.sh docker

# Production deployment
./run.sh prod

# Stop application
docker-compose down

# Stop production
docker-compose -f docker-compose.prod.yml down

# View logs
docker-compose logs -f credtravels-app

# Health check
curl http://localhost/actuator/health
```

This configuration setup provides a robust, scalable, and memory-efficient deployment strategy for CredTravels across different environments.
