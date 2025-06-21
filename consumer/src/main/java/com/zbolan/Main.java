package com.zbolan;

/**
 * Hello world!
 *
 */
public class Main
{
    private static final String BOOTSTRAP_SERVERS = System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092");
    private static final String KAFKA_TOPICS = System.getenv().get("KAFKA_TOPICS");

    public static void main( String[] args )
    {
        System.out.println( "[consumer] hello!" );
    }
}
