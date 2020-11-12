package com.app.kafka.streams;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.app.kafka.consumer.ConsumerCreator;
import com.app.kafka.consumer.KafkaIConsumer;
import com.app.kafka.respond.ProducerCreator;
import com.app.main.KafkaMain;
import com.app.utils.Config;

public class KafkaProcessorFe implements Runnable{
	private Consumer<Long, String> consumer;
	private ExecutorService executor;
	private Producer<Long, String> producer;
	static Logger LOGGER = Logger.getLogger(KafkaProcessorFe.class.getName());
	
	public KafkaProcessorFe() {
		consumer = ConsumerCreator.createFeConsumer();
		producer = ProducerCreator.createProducerFe();
	}
//	KafkaRecordHandler recordHandler;
	@Override
	public void run() {
		Integer numberOfThreads = 5;
//		consumer = ConsumerCreator.createFeConsumer();
		executor = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 0L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
		Duration sec = Duration.ofNanos(1000);
		LOGGER.info("Consumer FE running ...");
		while (true) {
			ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
			for (final ConsumerRecord<Long, String> record : consumerRecords) {
				executor.submit(new KafkaRecordHandler(record,producer));
			}
		}
	}
	public void shutdown() {
		if (consumer != null) {
			consumer.close();
		}
		if (producer != null) {
			producer.flush();
			producer.close();
		}
		if (executor != null) {
			executor.shutdown();
		}
		try {
			if (executor != null && !executor.awaitTermination(60, TimeUnit.MILLISECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}
	}
}
