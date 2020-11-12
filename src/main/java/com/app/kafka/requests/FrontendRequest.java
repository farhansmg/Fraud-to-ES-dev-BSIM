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
import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.app.kafka.respond.BoRespond;
import com.app.kafka.respond.FrontendRespond;
import com.app.kafka.streams.KafkaRecordHandler;

public class FrontendRequest{	
	static Logger LOGGER = Logger.getLogger(FrontendRequest.class.getName());
	public static JsonObject Post_JSON(JsonObject dataJson) {
		// staging
		//Ip public staging : http://101.100.201.21:8280/api/v2
//		   String query_url = "http://10.7.1.50:8280/api/v2";
        // production v1
//           String query_url = "http://101.100.201.52:8280/api/games";
		// production v2
//		   String query_url = "http://101.100.201.65:8280/api/v2";
		   String query_url = "http://10.7.1.195:8280/api/v2";
           String result = "";
           JsonObject data_esb = new JsonObject();
           JsonObject dataTemp = new JsonObject();
        // -------------USE IT FOR V1 ESB----------------
//           JsonObject data = dataJson.get("data").getAsJsonObject();
//           dataJson.remove("ip");
           try 
           {
        	// -------------COMMENT IT FOR V2 ESB----------------
//        	   	dataJson.remove("data");
        	// -------------USE IT FOR V1 ESB----------------
//        	    dataJson.remove("data");
           		URL url = new URL(query_url);
           		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	     	   	conn.setConnectTimeout(8000);
	     	   	conn.setReadTimeout(8000);
	     	   	conn.setRequestProperty("Content-Type", "application/json");
	     	   	conn.setRequestProperty("charset", "utf-8");
	     	   	conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
	     	   	conn.setDoOutput(true);
	     	   	conn.setDoInput(true);
	     	   
	     	   	conn.setRequestMethod("POST");
	     	   	try {
	     	   		OutputStream os = conn.getOutputStream();
		     	// -------------USE IT FOR V2 ESB----------------
	     	   		LOGGER.info("Sending to ESB -->"+ " ID : " + dataJson.get("id").getAsString()+ " ACT : " +  dataJson.get("act").getAsString()
	     	   			+ " CODE : " + dataJson.get("code").getAsString());
		     	   	os.write(dataJson.toString().getBytes("UTF-8"));
		     	// -------------USE IT FOR V1 ESB----------------
//		     	    os.write(data.toString().getBytes("UTF-8"));
		     	   	os.close(); 
		     	   	try {
		     	   		InputStream in = new BufferedInputStream(conn.getInputStream());
		     	   		result = IOUtils.toString(in, "UTF-8");
		     	   		in.close();
		     	   		conn.disconnect();
			     	   	try {
			     	   		dataTemp = new JsonParser().parse(result).getAsJsonObject();
			     	   		LOGGER.info("Got Msg From ESB -->"+ result);
			     	   		try {
			     	   			data_esb = dataTemp.get("data").getAsJsonObject();
			     	   			LOGGER.info("Data String --> " + data_esb);
			     	   			String data_esb_string = data_esb.toString();
			     	   			LOGGER.info("Data String --> " + data_esb_string);
			     	   			String ec_esb = dataTemp.get("ec").getAsString();
			     	   			LOGGER.info("Ec ESB --> " + ec_esb);
			     	   			if (data_esb_string.equals(null) || data_esb_string.equals("null") || ec_esb.isEmpty()){
				     	   			dataJson.addProperty("ec", -1);
					     	   		JsonObject desc = new JsonObject();
					     	   		desc.addProperty("msg", 606);
					     	   		dataJson.add("data", desc);
					     	   		LOGGER.error("Error Msg -->"+ " Msg : " + desc.get("msg").getAsString() + " ID : " + dataJson.get("id").getAsString()+ " ACT : " +  dataJson.get("act").getAsString()
					     	   			+ " CODE : " + dataJson.get("code").getAsString());
			     	   			} else {
			     	   				dataJson = new JsonParser().parse(result).getAsJsonObject();
			     	   			}
			     	   		}catch(Exception e) {
				     	   		dataJson.addProperty("ec", -1);
				     	   		JsonObject desc = new JsonObject();
				     	   		desc.addProperty("msg", 605);
				     	   		dataJson.add("data", desc);
				     	   		LOGGER.error(e);
				     	   		LOGGER.error("Error Msg -->"+ " Msg : " + desc.get("msg").getAsString() + " ID : " + dataJson.get("id").getAsString()+ " ACT : " +  dataJson.get("act").getAsString()
				     	   			+ " CODE : " + dataJson.get("code").getAsString() );
				     	   	}
			     	   	}catch(Exception e) {
			     	   		dataJson.addProperty("ec", -1);
			     	   		JsonObject desc = new JsonObject();
			     	   		desc.addProperty("msg", 603);
			     	   		dataJson.add("data", desc);
			     	   		LOGGER.error(e);
			     	   		LOGGER.error("Error Msg -->"+ " Msg : " + desc.get("msg").getAsString() + " ID : " + dataJson.get("id").getAsString()+ " ACT : " +  dataJson.get("act").getAsString()
			     	   			+ " CODE : " + dataJson.get("code").getAsString() );
			     	   	}
		     	   	}catch(Exception e) {
		     	   		dataJson.addProperty("ec", -1);
		     	   		JsonObject desc = new JsonObject();
		     	   		desc.addProperty("msg", 602);
		     	   		dataJson.add("data", desc);
		     	   		LOGGER.error(e);
		     	   		LOGGER.error("Error Msg -->"+ " Msg : " + desc.get("msg").getAsString() + " ID : " + dataJson.get("id").getAsString()+ " ACT : " +  dataJson.get("act").getAsString()
		     	   			+ " CODE : " + dataJson.get("code").getAsString() );
		     	   	}
	     	   	}catch(Exception e) {
	     	   		dataJson.addProperty("ec", -1);
	     	   		JsonObject desc = new JsonObject();
	     	   		desc.addProperty("msg", 601);
	     	   		dataJson.add("data", desc);
	     	   		LOGGER.error(e);
	     	   		LOGGER.error("Error Msg -->"+ " Msg : " + desc.get("msg").getAsString() + " ID : " + dataJson.get("id").getAsString()+ " ACT : " +  dataJson.get("act").getAsString()
	     	   			+ " CODE : " + dataJson.get("code").getAsString() );
	     	   	}
	     	   	// read the response
	     	   	
	     	   	// -------------USE IT FOR V2 ESB----------------
           } 
           catch (Exception e) {
        	   dataJson.addProperty("ec", -1);
    	   		JsonObject desc = new JsonObject();
    	   		desc.addProperty("msg", 603);
    	   		dataJson.add("data", desc);
    	   		LOGGER.error("Error Msg -->"+ " Msg : " + desc.get("msg").getAsString() + " ID : " + dataJson.get("id").getAsString()+ " ACT : " +  dataJson.get("act").getAsString()
    	   			+ " CODE : " + dataJson.get("code").getAsString() );
   			}
		return dataJson;
       
	}
}