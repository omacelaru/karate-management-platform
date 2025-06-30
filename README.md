# Karate Management Platform - Setup Guide

This guide will help you set up and configure all the necessary tools and services for the Karate Management Platform.

## Prerequisites

- Docker and Docker Compose installed
- Git installed
- At least 4GB of RAM available for the containers
- Ports 5432, 9200, 5601, 9000, 9090, 3001, 9093, 5044, 5000, 9600 available

## Services Overview

The platform consists of the following services:

1. PostgreSQL 13 (Database)
2. Elasticsearch 8.12.1 (Search Engine)
3. Logstash 8.12.1 (Log Processing)
4. Kibana 8.12.1 (Data Visualization)
5. SonarQube (Code Quality)
6. Prometheus (Monitoring)
7. Grafana (Metrics Visualization)
8. AlertManager (Alerting)

## Detailed Configuration Steps

### 1. PostgreSQL Setup

```bash
# Default credentials:
Username: karate_user
Password: Karate.user2025
Database: karate_users_db
Port: 5432

# Database Management Commands

# 1. Clear all data from tables (except flyway_schema_history)
DO $$
    DECLARE
        r RECORD;
    BEGIN
        -- Dezactivează temporar constrângerile de integritate referențială
        EXECUTE 'SET session_replication_role = replica';

        -- Șterge datele din toate tabelele
        FOR r IN
            SELECT tablename
            FROM pg_tables
            WHERE schemaname = 'public' and tablename <> 'flyway_schema_history'
            LOOP
                EXECUTE format('TRUNCATE TABLE public.%I CASCADE;', r.tablename);
            END LOOP;

        -- Reactivează constrângerile
        EXECUTE 'SET session_replication_role = DEFAULT';
    END $$;

# 2. Restore database from backup
cat backup.sql | docker exec -i postgres-DB psql -U karate_user -d karate_users_db
```

### 2. Elasticsearch Setup

```bash
# Access URL: http://localhost:9200
# Configuration:
- Single node discovery
- Security disabled
- Memory: 512MB

# Verify Elasticsearch is running:
curl http://localhost:9200
```

### 3. Logstash Setup

```bash
# Ports:
- 5044: Beats input
- 5000: TCP/UDP input
- 9600: API

# Configuration files location:
- ./src/main/resources/logstash/config/logstash.yml
- ./src/main/resources/logstash/pipeline

# Create logstash.yml:
input {
  beats {
    port => 5044
  }
  tcp {
    port => 5000
  }
}

filter {
  if [type] == "spring-boot" {
    json {
      source => "message"
    }
  }
}

output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "karate-logs-%{+YYYY.MM.dd}"
  }
}
```

### 4. Kibana Setup

```bash
# Access URL: http://localhost:5601

# Steps to configure logging visualization:

1. Access Kibana at http://localhost:5601
2. Go to "Stack Management" > "Index Patterns"
3. Create new index pattern:
   - Name: karate-logs-*
   - Time field: @timestamp
4. Go to "Discover":
   - Select the created index pattern
   - View and filter logs
5. Create visualizations:
   - Go to "Visualize"
   - Create visualization
   - Choose visualization type (e.g., Line, Bar, Pie)
   - Select index pattern
   - Configure metrics and buckets
6. Create dashboard:
   - Go to "Dashboard"
   - Click "Create dashboard"
   - Add visualizations
   - Save dashboard
```

### 5. SonarQube Setup

```bash
# Access URL: http://localhost:9000

# Initial Setup:
1. Access SonarQube at http://localhost:9000
2. Login with default credentials:
   - Username: admin
   - Password: admin
3. Change admin password when prompted

# Project Setup:
1. Click "Create Project"
2. Choose "Manually"
3. Enter project key (e.g., karate-management-platform)
4. Enter project name
5. Choose "Locally" for analysis
6. Generate token:
   - Go to Administration > Security > Users
   - Click on your user
   - Generate token
7. Copy token to sonar-project.properties:
   sonar.login=your-generated-token

# Run Analysis:
1. Install SonarQube Scanner
2. Run analysis:
   sonar-scanner
```

### 6. Prometheus Setup

```bash
# Access URL: http://localhost:9090

# Configuration Steps:
1. Create prometheus.yml in ./src/main/resources/prometheus:
   global:
     scrape_interval: 15s
   
   scrape_configs:
     - job_name: 'spring-boot'
       metrics_path: '/actuator/prometheus'
       static_configs:
         - targets: ['karate-management-platform:8080']

2. Verify targets are being scraped:
   - Go to http://localhost:9090/targets
   - Check if targets are UP
   
jvm_memory_used_bytes
process_open_files
system_cpu_usage
process_uptime_seconds
```

### 7. Grafana Setup

```bash
# Access URL: http://localhost:3001

# Initial Setup:
1. Login with default credentials:
   - Username: admin
   - Password: admin
2. Change password when prompted

# Add Prometheus Data Source:
1. Go to Configuration > Data Sources
2. Click "Add data source"
3. Select "Prometheus"
4. Configure:
   - URL: http://prometheus:9090
   - Access: Server (default)
5. Click "Save & Test"

# Create Dashboard:
1. Click "+" > "Create Dashboard"
2. Add new panel:
   - Click "Add new panel"
   - Select Prometheus data source
   - Choose metrics (e.g., jvm_memory_used_bytes)
   - Configure visualization
3. Save dashboard

# Import Spring Boot Dashboard:
1. Go to Dashboards > Import
2. Enter dashboard ID: 6756
3. Select Prometheus data source
4. Click "Import"
```

### 8. AlertManager Setup

```bash
# Access URL: http://localhost:9093

# Configuration Steps:
1. Create alertmanager.yml in ./src/main/resources/alertmanager:
   global:
     resolve_timeout: 5m
   
   route:
     group_by: ['alertname']
     group_wait: 10s
     group_interval: 10s
     repeat_interval: 1h
     receiver: 'email-notifications'
   
   receivers:
   - name: 'email-notifications'
     email_configs:
     - to: 'your-email@example.com'
       from: 'alertmanager@example.com'
       smarthost: 'smtp.example.com:587'
       auth_username: 'your-username'
       auth_password: 'your-password'

2. Create alert rules in prometheus.yml:
   rule_files:
     - "alert.rules"

3. Create alert.rules:
   groups:
   - name: example
     rules:
     - alert: HighMemoryUsage
       expr: jvm_memory_used_bytes > 1000000000
       for: 5m
       labels:
         severity: warning
       annotations:
         summary: High memory usage
         description: Memory usage is above 1GB
```

## Monitoring and Logging Setup

### Logging Configuration
1. Add logging configuration to application.properties:
```properties
logging.config=classpath:logback-spring.xml
```

2. Create logback-spring.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>logstash:5000</destination>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <customFields>{"appname":"karate-management-platform"}</customFields>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="LOGSTASH"/>
    </root>
</configuration>
```

### Metrics Configuration
1. Add Actuator dependencies to pom.xml:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

2. Configure Actuator in application.properties:
```properties
management.endpoints.web.exposure.include=health,info,prometheus
management.endpoint.health.show-details=always
```

## Troubleshooting Common Issues

### 1. Services Not Starting
```bash
# Check logs
docker-compose logs <service-name>

# Check container status
docker-compose ps

# Restart specific service
docker-compose restart <service-name>
```

### 2. Connection Issues
```bash
# Check network
docker network ls
docker network inspect app-network

# Check service logs
docker-compose logs <service-name>
```

### 3. Data Persistence Issues
```bash
# Check volumes
docker volume ls
docker volume inspect <volume-name>

# Reset data (if needed)
docker-compose down -v
docker-compose up -d
```

### 4. Port Conflicts
```bash
# Check port usage
netstat -ano | findstr :<port-number>

# Stop conflicting service
taskkill /PID <process-id> /F
```

## Maintenance

### Backup
```bash
# Backup PostgreSQL
docker exec postgres-DB pg_dump -U karate_user karate_users_db > backup.sql

# Backup Elasticsearch
curl -X GET "localhost:9200/_snapshot/my_backup/snapshot_1?pretty"
```

### Restore
```bash
# Restore PostgreSQL
cat backup.sql | docker exec -i postgres-DB psql -U karate_user -d karate_users_db

# Restore Elasticsearch
curl -X POST "localhost:9200/_snapshot/my_backup/snapshot_1/_restore?pretty"
```

### Cleanup
```bash
# Remove unused containers
docker container prune

# Remove unused volumes
docker volume prune

# Remove unused networks
docker network prune

# Remove all unused resources
docker system prune -a
```

## Starting the Services

1. Clone the repository:
```bash
git clone <repository-url>
cd karate-management-platform
```

2. Start all services:
```bash
docker-compose up -d
```

3. Verify all services are running:
```bash
docker-compose ps
```

## Important Notes

1. **Data Persistence**
   - All services use Docker volumes for data persistence
   - Volumes are automatically created when services start
   - Data will persist between container restarts

2. **Network**
   - All services are connected through the `app-network` bridge network
   - Services can communicate using their service names as hostnames

3. **Memory Requirements**
   - Elasticsearch: 512MB
   - Logstash: 256MB
   - Other services: Default configurations

4. **Security**
   - Default credentials are provided for development purposes
   - Change passwords in production environment
   - Elasticsearch security is disabled for development

## Monitoring

- Prometheus metrics: http://localhost:9090
- Grafana dashboards: http://localhost:3001
- AlertManager: http://localhost:9093

## Logging

- Kibana: http://localhost:5601
- Logstash: Configured to receive logs on ports 5044 and 5000

## Code Quality

- SonarQube: http://localhost:9000
- Run analysis using SonarQube scanner
- Configure quality gates and rules in SonarQube interface 