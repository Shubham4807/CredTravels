#!/bin/bash

echo "🚀 Starting CredTravels Application..."

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

# Start the services
echo "🐳 Starting Docker services..."
docker-compose up -d

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
    echo "🛑 To stop the application, run: docker-compose down"
else
    echo "❌ Service health check failed. Please check the logs:"
    echo "   docker-compose logs credtravels-app"
fi
