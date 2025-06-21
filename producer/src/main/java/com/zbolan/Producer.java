package com.zbolan;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicCollection;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Producer {

    private final KafkaProducer<String, String> kafkaProducer;
    private final AdminClient adminClient;
    private TopicCollection.TopicNameCollection registeredTopics;

    public Producer(String bootstrapServers, String... topics) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        adminClient = AdminClient.create(props);
        kafkaProducer = new KafkaProducer<>(props);
        initTopics(topics);
    }

    void initTopics(String ... topics) {
        List<String> topicList = new ArrayList<>(topics.length);
        var existingTopics = adminClient.listTopics();

        for (var topic : topics) {
            NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
            try {
                if (!existingTopics.names().get().contains(topic)) {
                    adminClient.createTopics(List.of(newTopic)).all().get();
                }

                topicList.add(topic);
                System.out.println("Registered to topic: " + topic);
            } catch (Exception e) {
                System.err.println("Failed to initialize topic " + topic + ": " + e.getMessage());
            }
        }
        registeredTopics = TopicCollection.ofTopicNames(topicList);
    }

    public void emitTestMessages() {
        try {
            for (var topicName : registeredTopics.topicNames()) {
                String key = "key-" + topicName;
                String value = "Hello Kafka! Message for topic " + topicName;

                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);

                // Send the record asynchronously
                kafkaProducer.send(record, (metadata, exception) -> {
                    if (exception != null) {
                        System.err.println("Error sending message to topic " + topicName + ": " + exception.getMessage());
                    } else {
                        System.out.printf("Sent message to topic %s: key=%s, value=%s, partition=%d, offset=%d%n",
                                topicName, key, value, metadata.partition(), metadata.offset());
                    }
                });

                try {
                    Thread.sleep(5 * 1000); // Wait 5 seconds between messages
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            kafkaProducer.close();
        }
    }


}
