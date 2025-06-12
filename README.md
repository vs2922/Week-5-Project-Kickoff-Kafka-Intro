````markdown
# PharmaInventory: Event-Driven Product Catalog Service

A Spring Boot microservice that extends our Product Catalog with real-time Kafka messaging.  
It publishes domain events (Medicine, Pallet, Task) to Kafka topics, consumes them in dedicated listeners (with dead-letter support), and offers a REST endpoint for manual event emission.  

---

## Table of Contents

- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Getting Started](#getting-started)  
- [Configuration](#configuration)  
- [Running Locally](#running-locally)  
- [Kafka Topics](#kafka-topics)  
- [API Endpoints](#api-endpoints)  
- [Testing](#testing)  
- [Contributing](#contributing)  
- [License](#license)  

---

## Features

- **Domain Events**: `MedicineCreatedEvent`, `PalletProcessedEvent`, `TaskCompletedEvent`  
- **Kafka Integration**: Producers & Consumers via Spring Kafka  
- **Dead-Letter Queues**: Automatic forwarding of failed messages to `<topic>.DLT`  
- **Docker Compose**: Single-command startup of ZooKeeper & Kafka  
- **Manual Event Trigger**: REST endpoint for quick publishing of sample events  
- **Embedded Kafka Tests**: End-to-end verification without external brokers  
- **Clean Package Structure**: `config`, `dto`, `event`, `producer`, `consumer`, `service`, `integration`  

---

## Prerequisites

- **Java 17+**  
- **Maven 3.8+**  
- **Docker & Docker Compose**  

---

## Getting Started

```bash
# Clone the repository
git clone https://github.com/<your-username>/pharmainventory-kafka.git
cd pharmainventory-kafka
````

---

## Configuration

Edit `src/main/resources/application-dev.yml` to point at your local Kafka:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: pharma-group
      auto-offset-reset: earliest
    json:
      trusted-packages: "*"
```

---

## Running Locally

1. **Start Kafka & ZooKeeper**

   ```bash
   docker-compose up -d
   ```
2. **Run the application**

   ```bash
   mvn spring-boot:run -Dspring-boot.profile=dev
   ```
3. **Publish a test event**

   ```bash
   curl -X POST "http://localhost:8080/api/kafka/publish?name=SampleMed"
   ```

---

## Kafka Topics

| Topic                    | Description                       |
| ------------------------ | --------------------------------- |
| `medicine-created-topic` | Medicine creation events          |
| `pallet-processed-topic` | Pallet processed events           |
| `task-completed-topic`   | Task completion events            |
| `*.DLT`                  | Dead-letter for each of the above |

---

## API Endpoints

* **POST** `/api/kafka/publish?name={medicineName}`
  Publish a sample `MedicineCreatedEvent` (name only).

* (Existing REST CRUD & patch endpoints carry over from previous weeks.)

---

## Testing

* **Unit Tests**

  ```bash
  mvn test -Dskip.integration=false
  ```
* **Embedded Kafka Integration**

  * `KafkaIntegrationTest`
  * `EmbeddedKafkaTest`

---

## Contributing

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/xyz`)
3. Commit your changes (`git commit -m "Add xyz"`)
4. Push (`git push origin feature/xyz`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License.

```
```
