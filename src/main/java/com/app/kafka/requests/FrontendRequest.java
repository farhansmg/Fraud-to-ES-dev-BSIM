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
	
	public static JsonObject Post_JSON(JsonObject dataJson) {
		// staging
		   String query_url = "http://101.100.201.21:8280/api/v2";
        // production v1
//           String query_url = "http://101.100.201.52:8280/api/games";
		// production v2
//		   String query_url = "http://101.100.201.65:8280/api/v2";
           String result = "";
        // -------------COMMENT IT FOR V2 ESB----------------
//           JsonObject data = dataJson.get("data").getAsJsonObject();
//           dataJson.remove("ip");
        // -------------USE IT FOR V1 ESB----------------
//           JsonObject data = dataJson.get("data").getAsJsonObject();
//           dataJson.remove("ip");
           try 
           {
        	// -------------COMMENT IT FOR V2 ESB----------------
//        	   	dataJson.remove("data");
        	// -------------USE IT FOR V1 ESB----------------
//        	    dataJson.remove("data");
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
	     	// -------------USE IT FOR V2 ESB----------------
	     	   	os.write(dataJson.toString().getBytes("UTF-8"));
	     	// -------------USE IT FOR V1 ESB----------------
//	     	    os.write(data.toString().getBytes("UTF-8"));
	     	   	os.close(); 
	        
	     	   	// read the response
	     	   	InputStream in = new BufferedInputStream(conn.getInputStream());
	     	   	
	     	   	result = IOUtils.toString(in, "UTF-8");
	     	   	
	     	   	// -------------USE IT FOR V2 ESB----------------
	     	   	dataJson = new JsonParser().parse(result).getAsJsonObject();
	     	   	
//	     	   	JsonObject jobject = new JsonParser().parse(result).getAsJsonObject();
//	     	   	dataJson.addProperty("ec", jobject.get("ec").getAsInt());
//	     	   	jobject.remove("ec");
//	     	   	dataJson.add("data", jobject);
//	     	   	FrontendRespond.produce(dataJson);
	     	   	
	     	   	// -------------USE IT FOR V1 ESB----------------
//	     	    JsonObject jobject = new JsonParser().parse(result).getAsJsonObject();
//	     	   	dataJson.addProperty("ec", jobject.get("ec").getAsInt());
//	     	   	jobject.remove("ec");
//	     	   	dataJson.add("data", jobject);
//	     	   	String code = dataJson.get("code").getAsString();
//	     	   	switch(code) {
//	     	   		case "Fe":
//	     	   			FrontendRespond.produce(resJson);
//	     	   			break;
//	     	   		case "Bo":
//	     	   			BoRespond.produce(resJson);
//	     	   			break;
//	     	   	}
	     	   	
	     	   	in.close();
	     	   	conn.disconnect();
           } 
           catch (Exception e) {
        	   dataJson.addProperty("ec", -1);
        	   JsonObject desc = new JsonObject();
        	   desc.addProperty("desc", e.toString());
        	   dataJson.add("data", desc);
//        	   String code = dataJson.get("code").getAsString();
//        	   switch(code) {
//        	   		case "Fe":
//        	   			FrontendRespond.produce(dataJson);
//        	   		case "Bo":
//        	   			BoRespond.produce(dataJson);
//        	   }
//        	   System.out.println(e);
   			}
		return dataJson;
       
	}
}