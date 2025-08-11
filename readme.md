# Streaming Kafka with Flink

An example application demonstrating how to use Apache Flink to process streaming data from Kafka.
The project has 3 main components:

1. **Kafka Producer**: A simple producer that sends messages to Kafka topic(s).
2. **Kafka Consumer**: A consumer that reads messages from Kafka topic(s).
3. **Flink Application**: A Flink job that consumes messages from Kafka, processes them, and writes results to another Kafka topic.

## Local Development

### docker compose

Spin everything up with:

```shell
docker compose up --build --detach
```

Restart a service

```shell
docker compose up --build --no-deps --detach <service>
```

## Directory Structure

The project is organized as a monorepo where subdirectories are their own deployable modules.
This mimics the main components of the project.

```text
flink-app/
    Dockerfile
    src/    # java source
consumer/
    Dockerfile
    src/    # java source
producer/
    Dockerfile
    src/    # java source
```
