# Remote Locking Prototype with Redis

This prototype demonstrates a **remote locking mechanism** using **Redis** to synchronize multiple consumers processing
shared resources. It ensures data consistency while handling concurrent order processing and inventory updates.

---

## How It Works

1. **Orders are queued in Redis**:
    - A list (`orders`) holds pending orders.
    - Consumers fetch and process orders one at a time.

2. **Redis-based locking**:
    - A lock is acquired using Redis before processing an order for a specific product.
    - The lock prevents multiple consumers from processing orders for the same product simultaneously.

3. **Inventory update**:
    - The stock for a product is updated if sufficient inventory is available.
    - If the lock cannot be acquired, the order is requeued for future processing.

---

## Running the Prototype

### Prerequisites

- Ensure **Docker** is installed and running.
- Install **Java 17** for running the Spring Boot application.
- Install **Gradle** or use the provided Gradle wrapper (`./gradlew`).

### Steps

1. **Start Redis and PostgreSQL**:
   Run the following command to start the necessary services using `docker-compose`:
   ```bash
   docker-compose up -d
   ```
2. **Connect to the postgres db with the following credentials and execute the script in schema.sql to seed the postgres
   remotelockdb**
   ```text
   user: remotelock
   pass: remotelock
   database: remotelockdb
   ```
   For example with psql client from the project root directory:
   ```bash
      psql -h localhost -U remotelock -d remotelockdb -f src/main/resources/schema.sql
   ```
3. **Run the Application: Start the Spring Boot application:**
   ```bash
   ./gradlew bootRun
   ```
4. **Seed Data: The application includes a RedisOrderProducer that seeds the Redis queue with dummy orders upon startup.
   **

---

## Monitoring logs

### Consumer Logs

- Observe the logs for order consumption:
  ```text
  Consumer one consumed: {"orderId":123,"productId":1,"quantity":7}
  Order processed: 123, Stock after update: 93
  ```

### Requeuing Logs

- Requeuing Logs
  ```text
  Could not acquire lock with lock:1. Requeuing order 123
  ```
  

