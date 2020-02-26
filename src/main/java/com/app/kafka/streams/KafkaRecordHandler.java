package com.app.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.app.kafka.requests.FrontendRequest;
import com.app.kafka.respond.FrontendRespond;
import com.app.main.KafkaMain;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class KafkaRecordHandler implements Runnable {

	private ConsumerRecord<Long, String> record;

	public KafkaRecordHandler(ConsumerRecord<Long, String> record) {
		this.record = record;
	}

	@Override
	public void run() { // this is where further processing happens
		
		try
		{
			System.out.println("Terima di kafka consumer :"+record.value());
			JsonElement jelement = new JsonParser().parse(record.value());
			JsonObject message = new JsonObject();
		    try {
					message = jelement.getAsJsonObject();
					System.out.println("Kirim ke esb :"+message);
					FrontendRequest.Post_JSON(message);
		    }
		    catch(Exception e)
		        {
		            //TangkasRespond.respondToFe = "error";
		           	System.out.println(e);
		    }
		}
		catch (Exception e)
		{
			//TangkasRespond.respondToFe = "error";
			e.printStackTrace();
		}
			

	}

}
