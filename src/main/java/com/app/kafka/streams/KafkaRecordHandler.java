package com.app.kafka.streams;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.app.kafka.requests.FrontendRequest;
import com.app.kafka.respond.BoRespond;
import com.app.kafka.respond.FrontendRespond;
import com.app.kafka.respond.KafkaIProducer;
import com.app.main.KafkaMain;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class KafkaRecordHandler implements Runnable {

	private ConsumerRecord<Long, String> record;
	private Producer<Long, String> producer;
	private ProducerRecord<Long, String> producer_record;
	public KafkaRecordHandler(ConsumerRecord<Long, String> record, Producer<Long, String> producer) {
		this.record = record;
		this.producer = producer;
	}

	@Override
	public void run() { // this is where further processing happens
//		System.out.println("received... :"+record.value());
		System.out.println("received message ...");
		JsonElement jelement = new JsonParser().parse(record.value());
		Thread mythread = Thread.currentThread();
		
		JsonObject message = new JsonObject();
		JsonObject res = new JsonObject();
		synchronized(mythread) {
	        System.out.println("threadId1 = "+mythread.getId());
       		try { 
       			message = jelement.getAsJsonObject();
       			System.out.println("Sending to esb ...");
//				System.out.println("Kirim ke esb :"+message);
				res = FrontendRequest.Post_JSON(message);
				String code = message.get("code").getAsString();
				switch(code) {
     	   			case "Fe":
     	   				this.producer_record = new ProducerRecord<Long, String>(KafkaIProducer.TOPIC_FE, res.toString());
     	   				break;
     	   			case "Bo":
     	   				this.producer_record = new ProducerRecord<Long, String>(KafkaIProducer.TOPIC_BO, res.toString());
     	   				break;
     	   		}
				producer.send(producer_record);
       		} 
       		catch (Exception e) { 
       			System.out.println("Error in sending record");
       			System.out.println(e); 
       		}
	    }
	}

}
