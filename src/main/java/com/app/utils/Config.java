package com.app.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	public String topic, kafkaHost, kafkaPort, DSSHost;
	
	public Config(String conf[]) {
		String appConfigPath = "";
		Boolean useDefault = true;
		
		Properties appConfig = new Properties();
		
		if(conf.length > 0) {			
			for (String cfg : conf) {
				String[] keyValues = cfg.split("=");
				if(keyValues.length > 1) {
					appConfig.put(keyValues[0], keyValues[1]);
				}
			}
			
			if(appConfig.getProperty("configFile") != null) {
				appConfigPath = appConfig.getProperty("configFile");
				useDefault = false;
			}
		}
		
		try {
			if(useDefault) {
				ClassLoader classLoader = getClass().getClassLoader();
				InputStream is = classLoader.getResourceAsStream("app.properties");
				appConfig.load(is);
			} else {
				appConfig.load(new FileInputStream(appConfigPath));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		topic = appConfig.getProperty("topic");
		kafkaHost = appConfig.getProperty("kafkaHost");
		kafkaPort = appConfig.getProperty("kafkaPort");
		DSSHost = appConfig.getProperty("DSSHost");
	}
}