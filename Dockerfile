FROM eclipse-temurin:17-jdk

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy pom.xml first for better caching
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Create runtime image
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built jar
COPY --from=0 /app/target/*.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application with memory optimization (1GB max)
ENTRYPOINT ["java", \
  "-Xms512m", \
  "-Xmx1g", \
  "-XX:+UseG1GC", \
  "-XX:MaxGCPauseMillis=200", \
  "-XX:+UseStringDeduplication", \
  "-XX:+OptimizeStringConcat", \
  "-XX:+UseCompressedOops", \
  "-XX:+UseCompressedClassPointers", \
  "-XX:MaxMetaspaceSize=256m", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
