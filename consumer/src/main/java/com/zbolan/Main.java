package com.zbolan;

public class Main
{
    private static final String BOOTSTRAP_SERVERS = System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092");
    private static final String KAFKA_TOPICS = System.getenv().get("KAFKA_TOPICS");
    private static final String GROUP_ID = System.getenv().get("CONSUMER_GROUP_ID");

    public static void main( String[] args )
    {
        System.out.println( "[consumer] hello!" );

        init();
    }

    static void init() {
        var topics = KAFKA_TOPICS.split(",");
        var consumer = new Consumer(BOOTSTRAP_SERVERS, topics, GROUP_ID);
        consumer.poll();
    }
}
