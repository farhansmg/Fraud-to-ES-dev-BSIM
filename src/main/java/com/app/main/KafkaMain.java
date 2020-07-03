package com.app.main;

import javax.persistence.Persistence;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.wso2.msf4j.MicroservicesRunner;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.app.kafka.respond.BoRespond;
import com.app.kafka.streams.KafkaProcessorBo;
import com.app.kafka.streams.KafkaProcessorFe;
//import com.app.utils.Config;

public class KafkaMain {
	// For using config properties, set the properties inside app.properties
//	private static Config appConfig;
	public static Hashtable<Long,Thread> threads = new Hashtable<Long,Thread>();
	// main function
	public static void main(String args[]) {
		System.out.println("Application Starting ...");
//		appConfig = new Config(args);
		// For using it to dss, uncomment code below
		/*
		 * RepositoryFactory.emf = Persistence.createEntityManagerFactory("kafkadss");
		 */
		
		// For using End point, uncomment code below
//		MicroservicesRunner runner = new MicroservicesRunner();
//		runner.deploy(new BoRespond());
//    	runner.start();
		ExecutorService processorFe = Executors.newFixedThreadPool(5);
		ExecutorService processorBo = Executors.newFixedThreadPool(5);
//    	KafkaProcessorFe processor1 = new KafkaProcessorFe();
//    	KafkaProcessorBo processor2 = new KafkaProcessorBo();
		try {
			processorFe.submit(new KafkaProcessorFe());
			processorBo.submit(new KafkaProcessorBo());
//			processor1.init(10);
//			processor2.init(5);
		} 
		catch (Exception e) {
			e.printStackTrace();
			processorFe.shutdown();
			processorBo.shutdown();
//			processor1.shutdown();
//			processor2.shutdown();
		}
	}

}