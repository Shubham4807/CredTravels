# CredTravels - Single MySQL Migration Summary

## 🎯 Migration Overview

This document summarizes the migration from **multiple MySQL Docker instances** to a **single MySQL instance with 4 databases** and **reduced memory requirements from 2-4 GB to 1 GB**.

## 🔄 What Changed

### Before (Multiple MySQL Instances)
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
                    │   (4 Instances)  │
                    │ (1GB each max)   │
                    │ - mysql-inventory│
                    │ - mysql-flights  │
                    │ - mysql-search   │
                    │ - mysql-booking  │
                    └──────────────────┘
```

### After (Single MySQL Instance)
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│     Nginx       │────│  Spring Boot App │────│     Redis       │
│  Load Balancer  │    │   (4 Modules)    │    │     Cache       │
│   (256MB max)   │    │   (1GB max)      │    │   (512MB max)   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                                │
                    ┌──────────────────┐
                    │      MySQL       │
                    │   (1 Instance)   │
                    │ (1GB max)        │
                    │ - inventory_db   │
                    │ - flights_info_db│
                    │ - search_db      │
                    │ - booking_db     │
                    └──────────────────┘
```

## 💾 Memory Requirements Comparison

### Before Migration
- **Total System Memory**: 6-8 GB
  - Spring Boot Application: 2-4 GB
  - MySQL Databases: 4 GB total (1 GB each)
  - Redis Cache: 512 MB
  - Nginx Load Balancer: 256 MB

### After Migration
- **Total System Memory**: 3-4 GB
  - Spring Boot Application: 1 GB
  - MySQL Database: 1 GB (single instance)
  - Redis Cache: 512 MB
  - Nginx Load Balancer: 256 MB

**Memory Savings**: **3-4 GB** (50% reduction)

## 🔧 Configuration Changes

### 1. Docker Compose Files

#### `docker-compose.yml` (Local/Docker)
**Before:**
```yaml
services:
  mysql-inventory:
    image: mysql:8.0
    container_name: credtravels-mysql-inventory
    # ... separate configuration
  
  mysql-flights-info:
    image: mysql:8.0
    container_name: credtravels-mysql-flights-info
    # ... separate configuration
  
  mysql-search:
    image: mysql:8.0
    container_name: credtravels-mysql-search
    # ... separate configuration
  
  mysql-booking:
    image: mysql:8.0
    container_name: credtravels-mysql-booking
    # ... separate configuration
```

**After:**
```yaml
services:
  mysql:
    image: mysql:8.0
    container_name: credtravels-mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_USER: credtravels
      MYSQL_PASSWORD: credtravels123
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql/init-all-databases.sql:/docker-entrypoint-initdb.d/init-all-databases.sql
```

#### `docker-compose.prod.yml` (Production)
Similar changes applied to production configuration.

### 2. Database Initialization

**Before:** 4 separate SQL files
- `init-inventory.sql`
- `init-flights-info.sql`
- `init-search.sql`
- `init-booking.sql`

**After:** 1 combined SQL file
- `init-all-databases.sql` - Creates all 4 databases in sequence

### 3. Spring Boot Configuration

**Before:**
```yaml
spring:
  datasource:
    inventory:
      jdbc-url: jdbc:mysql://mysql-inventory:3306/inventory_db
    flights-info:
      jdbc-url: jdbc:mysql://mysql-flights-info:3306/flights_info_db
    search:
      jdbc-url: jdbc:mysql://mysql-search:3306/search_db
    booking:
      jdbc-url: jdbc:mysql://mysql-booking:3306/booking_db
```

**After:**
```yaml
spring:
  datasource:
    inventory:
      jdbc-url: jdbc:mysql://mysql:3306/inventory_db
    flights-info:
      jdbc-url: jdbc:mysql://mysql:3306/flights_info_db
    search:
      jdbc-url: jdbc:mysql://mysql:3306/search_db
    booking:
      jdbc-url: jdbc:mysql://mysql:3306/booking_db
```

### 4. JVM Memory Settings

**Before:**
```bash
ENTRYPOINT ["java", \
  "-Xms2g", \
  "-Xmx4g", \
  # ... other options
  "-jar", "app.jar"]
```

**After:**
```bash
ENTRYPOINT ["java", \
  "-Xms512m", \
  "-Xmx1g", \
  "-XX:+UseSerialGC", \
  "-XX:MaxMetaspaceSize=256m", \
  # ... other options
  "-jar", "app.jar"]
```

## 📊 Benefits of Migration

### 1. **Resource Efficiency**
- **Memory**: 50% reduction in total memory usage
- **CPU**: Reduced CPU overhead from multiple MySQL processes
- **Storage**: Single data volume instead of 4 separate volumes
- **Network**: Simplified network configuration

### 2. **Operational Benefits**
- **Easier Management**: Single MySQL container to monitor and maintain
- **Simplified Backup**: Single database backup instead of 4
- **Reduced Complexity**: Fewer containers, simpler orchestration
- **Faster Startup**: Single MySQL instance starts faster than 4

### 3. **Cost Benefits**
- **Lower Infrastructure Costs**: Reduced memory and CPU requirements
- **Better Resource Utilization**: More efficient use of available resources
- **Scalability**: Easier to scale horizontally with multiple application instances

### 4. **Development Benefits**
- **Simplified Local Development**: Single MySQL instance on local machine
- **Easier Testing**: Single database connection for integration tests
- **Consistent Environment**: Same database setup across all environments

## 🚀 Deployment Commands

### Local Development
```bash
# Start with single MySQL instance
./run.sh

# Or explicitly
./run.sh local
```

### Docker Development
```bash
# Start with docker profile
./run.sh docker
```

### Production Deployment
```bash
# Start with production profile
./run.sh prod
```

## 🔍 Verification Steps

### 1. Check Container Status
```bash
# Verify single MySQL instance is running
docker ps | grep mysql

# Should show only one MySQL container
```

### 2. Verify Database Creation
```bash
# Connect to MySQL and check databases
docker exec -it credtravels-mysql mysql -u credtravels -p

# In MySQL:
SHOW DATABASES;
USE inventory_db;
SHOW TABLES;
USE flights_info_db;
SHOW TABLES;
USE search_db;
SHOW TABLES;
USE booking_db;
SHOW TABLES;
```

### 3. Check Application Connectivity
```bash
# Test health endpoint
curl http://localhost/actuator/health

# Check database health
curl http://localhost/actuator/health/db
```

### 4. Monitor Memory Usage
```bash
# Check container memory usage
docker stats

# Should show:
# - credtravels-app: ~1GB max
# - mysql: ~1GB max
# - redis: ~512MB max
# - nginx: ~256MB max
```

## ⚠️ Important Notes

### 1. **Database Isolation**
- All 4 databases are now in the same MySQL instance
- Each module still has its own database schema
- No cross-database queries or dependencies
- Maintains the same modular architecture

### 2. **Connection Pooling**
- Each datasource maintains its own connection pool
- No shared connections between modules
- Same isolation level as before

### 3. **Backup Strategy**
- Single MySQL backup covers all 4 databases
- Consider using `mysqldump --all-databases` for full backup
- Individual database backups still possible with `--database` flag

### 4. **Performance Considerations**
- Single MySQL instance may have slightly higher latency under heavy load
- Consider MySQL tuning parameters for better performance
- Monitor query performance and adjust indexes as needed

## 🔄 Rollback Plan

If you need to rollback to multiple MySQL instances:

### 1. **Revert Docker Compose Files**
```bash
# Restore original docker-compose.yml and docker-compose.prod.yml
git checkout HEAD~1 -- docker-compose.yml docker-compose.prod.yml
```

### 2. **Restore Database Initialization**
```bash
# Restore individual SQL files
git checkout HEAD~1 -- sql/init-*.sql
```

### 3. **Update Application Configuration**
```bash
# Restore original datasource URLs
# Update jdbc-url to point to individual MySQL instances
```

### 4. **Restart Services**
```bash
# Stop current services
docker-compose down

# Start with multiple MySQL instances
docker-compose up -d
```

## 📈 Future Enhancements

### 1. **MySQL Optimization**
- Tune MySQL configuration for better performance
- Implement connection pooling at MySQL level
- Add read replicas for read-heavy workloads

### 2. **Monitoring & Alerting**
- Set up MySQL performance monitoring
- Configure alerts for memory and CPU usage
- Implement query performance monitoring

### 3. **Backup & Recovery**
- Automated backup scheduling
- Point-in-time recovery capabilities
- Cross-region backup replication

## ✅ Migration Checklist

- [x] Update Docker Compose files to use single MySQL instance
- [x] Create combined database initialization script
- [x] Update Spring Boot datasource configurations
- [x] Reduce JVM memory settings to 1GB max
- [x] Update all configuration files and documentation
- [x] Test build and compilation
- [x] Verify database connectivity
- [x] Update deployment scripts
- [x] Create migration documentation

## 🎉 Summary

The migration to a single MySQL instance with 4 databases successfully:

1. **Reduced Memory Requirements**: From 2-4 GB to 1 GB (50% reduction)
2. **Simplified Architecture**: Single MySQL container instead of 4
3. **Maintained Modularity**: Each module still has its own database
4. **Improved Efficiency**: Better resource utilization and easier management
5. **Preserved Functionality**: All existing features work as before

The system is now more resource-efficient, easier to manage, and ready for production deployment with significantly lower infrastructure requirements.
