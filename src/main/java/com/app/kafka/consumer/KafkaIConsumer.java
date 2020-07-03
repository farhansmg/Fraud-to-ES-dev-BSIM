package com.app.kafka.consumer;

public interface KafkaIConsumer { 
//	Staging
	public static String KAFKA_FE_BROKERS = "10.7.1.23:9092,10.7.1.27:9092,10.7.1.28:9092";
	public static String KAFKA_BO_BROKERS = "10.7.1.113:9092,10.7.1.114:9092,10.7.1.115:9092";
//	Production
//	public static String KAFKA_BROKERS = "10.7.1.180:9092,10.7.1.181:9092,10.7.1.182:9092"; 
	public static Integer MESSAGE_COUNT=1000; 
	public static String CLIENT_ID="client1"; 
	public static String TOPIC_BO_NAME="KafkaEsbBo";
	public static String TOPIC_FE_NAME="KafkaEsb";
//	public static String GROUP_ID_CONFIG="KafkaEsbLocal1";
	public static String GROUP_ID_CONFIG="KafkaEsbGroup1"; 
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100; 
	public static String OFFSET_RESET_LATEST="latest"; 
	public static String OFFSET_RESET_EARLIER="earliest"; 
	public static Integer MAX_POLL_RECORDS=1; 
}