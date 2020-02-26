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

import com.google.gson.JsonObject;
import com.app.kafka.respond.FrontendRespond;

public class FrontendRequest{	
	
	public static void Post_JSON(JsonObject jsonObject) {
           String query_url = "http://ei.wso2.local/api/games";
           String result = "";
           String json = jsonObject.toString();
           
           try 
           {
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
        	   os.write(json.getBytes("UTF-8"));
        	   os.close(); 
           
        	   // read the response
        	   InputStream in = new BufferedInputStream(conn.getInputStream());
        	   //InputStream in = new GZIPInputStream(conn.getInputStream());
        	   //InputStream in = new DeflaterInputStream(conn.getInputStream());
        	   //String result = IOUtils.toString(in, "UTF-8");
        	   result = IOUtils.toString(in, "UTF-8");
        	   //System.out.println(result);
        	   System.out.println(result);
        	   FrontendRespond.produce(result);
        	   //in.close();
        	   in.close();
        	   conn.disconnect();
        	   
           } 
           catch (Exception e) {
   			System.out.println(e);
   		}
	}
}