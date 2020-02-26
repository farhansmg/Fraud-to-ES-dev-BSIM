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
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.app.kafka.consumer.ConsumerCreator;
import com.app.kafka.consumer.KafkaIConsumer;
import com.app.utils.Config;

public class KafkaProcessor {
	private ExecutorService executor;
	private Consumer<Long, String> consumer;
	
	public void init(int numberOfThreads) {
		// Create a threadpool
		executor = new ThreadPoolExecutor(numberOfThreads, numberOfThreads, 0L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
		Duration sec = Duration.ofNanos(1000);
		consumer = ConsumerCreator.createConsumer();
		while (true) {
			ConsumerRecords<Long, String> consumerRecords = consumer.poll(10000);
			for (final ConsumerRecord<Long, String> record : consumerRecords) {
				executor.submit(new KafkaRecordHandler(record));
			}
		}
	}

	public void shutdown() {
		if (consumer != null) {
			consumer.close();
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
