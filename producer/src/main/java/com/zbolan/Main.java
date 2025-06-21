package com.zbolan;


public class Main
{
    private static final String BOOTSTRAP_SERVERS = System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092");
    private static final String KAFKA_TOPICS = System.getenv().get("KAFKA_TOPICS");

    public static void main( String[] args )
    {
        System.out.println( "[producer] hello!" );
        System.out.println(KAFKA_TOPICS);
        setup();
    }


    static void setup() {
        var topics = KAFKA_TOPICS.split(",");
        var producer = new Producer(BOOTSTRAP_SERVERS, topics);

        while (true) {
            producer.emitTestMessages();
        }
    }
}
