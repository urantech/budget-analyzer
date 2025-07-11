# Multi-Module Microservices Project

A template for a multi-module Gradle project with Java 21, containing two microservices and supporting infrastructure.

## Project Structure

```
multi-service-project/
├── gateway-service/               # API Gateway service
├── document-processing-service/   # Document processing service
├── build.gradle                   # Root build file
├── settings.gradle                # Project settings
├── docker-compose.yml             # Docker Compose configuration
└── README.md                      # This file
```

## Services

### GatewayService
API Gateway service that routes requests to appropriate microservices.

### DocumentProcessingService
Service for processing and managing documents.

## Infrastructure

The project includes Docker Compose configuration for the following services:

- **PostgreSQL**: Database for storing document metadata and other application data
- **Kafka**: Message broker for asynchronous communication between services
- **Kafka-UI**: Web UI for monitoring and managing Kafka

## Requirements

- Java 21
- Gradle 8.5+
- Docker and Docker Compose

## Getting Started

1. Clone the repository
2. Start the infrastructure:
   ```
   docker-compose up -d
   ```
3. Build the project:
   ```
   ./gradlew build
   ```

## Development

Each service is a separate Gradle module with its own build configuration. The root project defines common settings and dependencies.

To add a new service:

1. Create a new directory for the service
2. Add the service to `settings.gradle`
3. Create a `build.gradle` file for the service
4. Implement the service code

## Configuration

- **PostgreSQL**: Available at `localhost:5432`
  - Username: postgres
  - Password: postgres
  - Database: transaction_db

- **Kafka**: Available at `localhost:29092`

- **Kafka-UI**: Available at `http://localhost:8080`
