package com.app.kafka.requests;

import java.io.BufferedInputStream;
//import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.DeflaterInputStream;

/*import com.app.kafka.respond.TangkasRespond;*/
import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.app.kafka.respond.BoRespond;
import com.app.kafka.respond.FrontendRespond;
import com.app.kafka.streams.KafkaRecordHandler;

public class FrontendRequest{	
	
	public static void Post_JSON(JsonObject dataJson) {
           String query_url = "http://101.100.201.21:80/api/v2";
           String result = "";
//           JsonObject data = dataJson.get("data").getAsJsonObject();
//           dataJson.remove("ip");
           try 
           {
//        	   	dataJson.remove("data");
           		System.out.println("ThreadId2 :"+Thread.currentThread().getId());
           		URL url = new URL(query_url);
           		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	     	   	conn.setConnectTimeout(5000);
	     	   	conn.setRequestProperty("Content-Type", "application/json");
	     	   	conn.setRequestProperty("charset", "utf-8");
	     	   	conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
	     	   	conn.setDoOutput(true);
	     	   	conn.setDoInput(true);
	     	   
	     	   	conn.setRequestMethod("POST");
	     	   	OutputStream os = conn.getOutputStream();
	     	   	os.write(dataJson.toString().getBytes("UTF-8"));
	     	   	os.close(); 
	        
	     	   	// read the response
	     	   	InputStream in = new BufferedInputStream(conn.getInputStream());
	     	   	
	     	   	result = IOUtils.toString(in, "UTF-8");
	     	   	JsonObject resJson = new JsonParser().parse(result).getAsJsonObject();
	     	   	System.out.println(resJson);
//	     	   	JsonObject jobject = new JsonParser().parse(result).getAsJsonObject();
//	     	   	dataJson.addProperty("ec", jobject.get("ec").getAsInt());
//	     	   	jobject.remove("ec");
//	     	   	dataJson.add("data", jobject);
//	     	   	FrontendRespond.produce(dataJson);
	     	   	String code = resJson.get("code").getAsString();
	     	   	switch(code) {
	     	   		case "Fe":
	     	   			FrontendRespond.produce(resJson);
	     	   			break;
	     	   		case "Bo":
	     	   			BoRespond.produce(resJson);
	     	   			break;
	     	   	}
	     	   	
	     	   	in.close();
	     	   	conn.disconnect();
        	   
           } 
           catch (Exception e) {
        	   dataJson.addProperty("ec", -1);
        	   JsonObject desc = new JsonObject();
        	   desc.addProperty("desc", "error on bridge " + e.toString());
        	   dataJson.add("data", desc);
        	   String code = dataJson.get("code").getAsString();
        	   switch(code) {
        	   		case "Fe":
        	   			FrontendRespond.produce(dataJson);
        	   		case "Bo":
        	   			BoRespond.produce(dataJson);
        	   }
        	   System.out.println(e);
   		}
	}
}