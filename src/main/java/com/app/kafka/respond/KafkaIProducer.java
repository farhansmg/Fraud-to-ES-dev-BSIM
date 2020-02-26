package com.app.kafka.respond;

public interface KafkaIProducer { 
	public static String KAFKA_BROKERS = "10.7.1.23:9092,10.7.1.27:9092,10.7.1.28:9092"; 
	public static Integer MESSAGE_COUNT=1000;
	public static String ACKS="all";
	public static String CLIENT_ID="client1"; 
	public static String TOPIC_NAME="FeRes";
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100; 
	public static String OFFSET_RESET_LATEST="latest"; 
	public static String OFFSET_RESET_EARLIER="earliest"; 
	public static Integer MAX_POLL_RECORDS=1; 
}