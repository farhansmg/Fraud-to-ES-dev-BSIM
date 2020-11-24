package com.app.kafka.streams;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.app.kafka.consumer.ConsumerCreator;
import com.app.kafka.consumer.KafkaIConsumer;
import com.app.kafka.respond.ProducerCreator;
import com.app.utils.Config;

public class KafkaProcessorBo implements Runnable {
	private Consumer<Long, String> consumer;
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	private ExecutorService execHandler;
	private static KafkaProcessorBo myself;
	private boolean isRunning = true;
	private Producer<Long, String> producer;
	static Logger LOGGER = Logger.getLogger(KafkaProcessorBo.class.getName());
	
	public KafkaProcessorBo() {
		consumer = ConsumerCreator.createBoConsumer();
		producer = ProducerCreator.createProducerBo();
		
		int threads = Runtime.getRuntime().availableProcessors();
		execHandler = Executors.newFixedThreadPool(threads);
		myself = this;
	}
	
	public static KafkaProcessorBo get() {
		return myself;
	}

	public KafkaProcessorBo start() {
		exec.submit(this);
		return this;
	}
	
	@Override
	public void run() {
		try {
			while (isRunning) {
				LOGGER.info("Consumer BO running ...");
				ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
				for (final ConsumerRecord<Long, String> record : consumerRecords) {
					execHandler.execute(new FutureTask<Object>(new KafkaRecordHandler(record,producer)));
				}
			}
		} catch (WakeupException e) {
			LOGGER.error("Processor WakeupException : ", e);
			// Ignore exception if closing
		} catch (Exception e) {
			LOGGER.error("KafkaProcessor Bo CRASHED !!!", e);
			// TODO just find a way to restart this thread if we can't fix the problem
			// is this the right way to restart the consumer whenever it crashed ?
//			start();
		}
		isRunning = false;
		return;
	}

	public void shutdown() {
		if (consumer != null) {
			consumer.close();
		}
		if (exec != null) {
			exec.shutdown();
			exec = null;
		}
		if (producer != null) {
			producer.flush();
			producer.close();
		}
		if(execHandler != null) { 
			execHandler.shutdown();
			execHandler = null;
		}
		myself = null;
		if (consumer != null) {
			consumer.close();
		}
	}
}
