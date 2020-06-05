package com.app.kafka.respond;

public interface KafkaIProducer { 
//	Staging
//	public static String KAFKA_BROKERS = "10.7.1.23:9092,10.7.1.27:9092,10.7.1.28:9092";
//	Production
	public static String KAFKA_BROKERS = "10.7.1.180:9092,10.7.1.181:9092,10.7.1.182:9092";
	public static String ACKS="all";
//	Local
//	public static String CLIENT_FE="KafkaEsbLocal1";
//	public static String CLIENT_BO="KafkaEsbLocal1";
//	Staging
//	public static String CLIENT_FE="KafkaEsbStag1";
//	public static String CLIENT_BO="KafkaEsbStag2";
//	Production
	public static String CLIENT_FE="KafkaEsbProd1";
	public static String CLIENT_BO="KafkaEsbProd2";
//	public static String CLIENT_BO_EP="KafkaEsbProd3";
	public static String TOPIC_FE="FeResp";
	public static String TOPIC_BO="BoResp";
//	public static String TOPIC_BO_EP="KafkaEsb";
}