package com.app.main;

import javax.persistence.Persistence;

import org.wso2.msf4j.MicroservicesRunner;

import java.util.Hashtable;

import com.app.kafka.respond.BoRespond;
import com.app.kafka.streams.KafkaProcessor;
import com.app.utils.Config;

public class KafkaMain {
	// For using config properties, set the properties inside app.properties
	private static Config appConfig;
	public static Hashtable<Long,Thread> threads = new Hashtable<Long,Thread>();
	// main function
	public static void main(String args[]) {		
		appConfig = new Config(args);
		// For using it to dss, uncomment code below
		/*
		 * RepositoryFactory.emf = Persistence.createEntityManagerFactory("kafkadss");
		 */
		
		// For using End point, uncomment code below
//		MicroservicesRunner runner = new MicroservicesRunner();
//		runner.deploy(new BoRespond());
//    	runner.start();
		
    	KafkaProcessor processor = new KafkaProcessor();
		try {
			processor.init(5);
		} 
		catch (Exception e) {
			e.printStackTrace();
			processor.shutdown();
		}
	}

}