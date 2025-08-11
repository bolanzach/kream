package com.zbolan;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Main {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
        if (bootstrapServers == null) {
            bootstrapServers = "kafka:29092";
        }
        
        String topics = System.getenv("KAFKA_TOPICS");
        if (topics == null) {
            topics = "topic_1,topic_2,topic_3";
        }
        
        String groupId = System.getenv("CONSUMER_GROUP_ID");
        if (groupId == null) {
            groupId = "flink-consumer-group";
        }
        
        String[] topicList = topics.split(",");
        
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(bootstrapServers)
                .setTopics(topicList)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        
        DataStream<String> kafkaStream = env.fromSource(
                source,
                WatermarkStrategy.noWatermarks(),
                "Kafka Source"
        );
        
        kafkaStream
                .map(value -> "[Flink Processed] " + value.toUpperCase())
                .print();
        
        System.out.println("Starting Flink job...");
        System.out.println("Bootstrap Servers: " + bootstrapServers);
        System.out.println("Topics: " + String.join(", ", topicList));
        System.out.println("Consumer Group: " + groupId);
        
        env.execute("Simple Kafka Flink Application");
    }
}
