
package com.app.kafka.respond;
 
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Producer; 
import org.apache.kafka.clients.producer.ProducerRecord; 
import org.apache.kafka.clients.producer.RecordMetadata;


import com.app.kafka.respond.KafkaIProducer; 
import com.app.kafka.respond.ProducerCreator;
import com.app.kafka.requests.FrontendRequest;
import com.app.main.KafkaMain;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;



public class FrontendRespond { 
		public static void produce(JsonObject res) {
			runProducer(res); 
		}

		static void runProducer(JsonObject res) { 
			Producer<Long, String> producer = ProducerCreator.createProducerFe();
			String respond = res.toString();
//			System.out.println(respond);
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(KafkaIProducer.TOPIC_FE, respond);
			System.out.println("Success");
			try { 
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent with partition " + metadata.partition() + " with offset " + metadata.offset());
				producer.flush();
				producer.close();
			} 
			catch (ExecutionException e) { 
				System.out.println("Error in sending record");
				System.out.println(e); 
			} 
			catch (InterruptedException e) {
				System.out.println("Error in sending record"); System.out.println(e); 
			}  
		} 
}
 