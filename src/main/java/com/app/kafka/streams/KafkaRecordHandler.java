package com.app.kafka.streams;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import com.app.kafka.requests.FrontendRequest;
import com.app.kafka.respond.BoRespond;
import com.app.kafka.respond.FrontendRespond;
import com.app.kafka.respond.KafkaIProducer;
import com.app.main.KafkaMain;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class KafkaRecordHandler implements Callable<Object> {

	private ConsumerRecord<Long, String> record;
	private Producer<Long, String> producer;
	private ProducerRecord<Long, String> producer_record;
	static Logger LOGGER = Logger.getLogger(KafkaRecordHandler.class.getName());
	public KafkaRecordHandler(ConsumerRecord<Long, String> record, Producer<Long, String> producer) {
		this.record = record;
		this.producer = producer;
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		handle();
		return null;
	}
	
	public void handle() { // this is where further processing happens
//		System.out.println("received... :"+record.value());
		JsonElement jelement = new JsonParser().parse(record.value());
		LOGGER.info("Received -->"+ " ID : " + jelement.getAsJsonObject().get("id").getAsString()+ " ACT : " +  jelement.getAsJsonObject().get("act").getAsString()
				+ " CODE : " + jelement.getAsJsonObject().get("code").getAsString());
		Thread mythread = Thread.currentThread();
		
		JsonObject message = new JsonObject();
		JsonObject res = new JsonObject();
   		try { 
   			message = jelement.getAsJsonObject();
//       			System.out.println("Sending to esb ...");
//				System.out.println("Kirim ke esb :"+message);
			res = FrontendRequest.Post_JSON(message);
			String code = message.get("code").getAsString();
			int ec = res.get("ec").getAsInt();
			if (ec == 10084 || ec == 10021) {
				//Do Nothing
				LOGGER.error("Error EC = " + ec);
			}else {
//					LOGGER.info("EC = " + ec);
				switch(code) {
 	   			case "Fe":
 	   				this.producer_record = new ProducerRecord<Long, String>(KafkaIProducer.TOPIC_FE, res.toString());
 	   				break;
 	   			case "Bo":
 	   				this.producer_record = new ProducerRecord<Long, String>(KafkaIProducer.TOPIC_BO, res.toString());
 	   				break;
 	   			}
				producer.send(producer_record);
				LOGGER.info("Sent to " +res.get("code").getAsString() + " -->"+ " ID : " + res.get("id").getAsString()+ " ACT : " +  res.get("act").getAsString());
			}
   		} 
   		catch (Exception e) { 
   			System.out.println("Error in sending record");
   			System.out.println(e); 
   		}
	}

}
