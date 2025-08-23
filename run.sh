#!/bin/bash

# CredTravels - Application Runner Script
# Supports local, docker, and production profiles
# Memory: 1 GB max

PROFILE=${1:-local}

echo "🚀 Starting CredTravels Application with profile: $PROFILE"
echo "💾 Memory requirements: 1 GB max"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Build the application
echo "📦 Building the application..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi

echo "✅ Build successful!"

# Start the services based on profile
echo "🐳 Starting Docker services with profile: $PROFILE..."

if [ "$PROFILE" = "prod" ]; then
    echo "🏭 Starting production environment..."
    docker-compose -f docker-compose.prod.yml up -d
elif [ "$PROFILE" = "docker" ]; then
    echo "🐳 Starting Docker environment..."
    docker-compose up -d
else
    echo "🏠 Starting local environment..."
    docker-compose up -d
fi

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 30

# Check service health
echo "🔍 Checking service health..."
curl -f http://localhost/health > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo "✅ CredTravels is running successfully!"
    echo "🌐 Application URL: http://localhost"
    echo "🔧 API Base URL: http://localhost/api"
    echo "📊 Health Check: http://localhost/health"
    echo "📚 Swagger UI: http://localhost:8080/swagger-ui.html"
    echo ""
    echo "📋 Available endpoints:"
    echo "   - Inventory: http://localhost/api/inventory"
    echo "   - Flights Info: http://localhost/api/flights-info"
    echo "   - Search: http://localhost/api/search"
    echo "   - Booking: http://localhost/api/booking"
    echo ""
    echo "🛑 To stop the application, run:"
    if [ "$PROFILE" = "prod" ]; then
        echo "   docker-compose -f docker-compose.prod.yml down"
    else
        echo "   docker-compose down"
    fi
    echo ""
    echo "📚 Usage:"
    echo "   ./run.sh          # Start with local profile (default)"
    echo "   ./run.sh docker   # Start with docker profile"
    echo "   ./run.sh prod     # Start with production profile"
else
    echo "❌ Service health check failed. Please check the logs:"
    if [ "$PROFILE" = "prod" ]; then
        echo "   docker-compose -f docker-compose.prod.yml logs credtravels-app-prod"
    else
        echo "   docker-compose logs credtravels-app"
    fi
fi
