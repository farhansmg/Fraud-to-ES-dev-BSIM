package com.app.utils;

import com.google.gson.JsonObject;

public class Utilities {
	protected String name;
	protected JsonObject json;
	protected boolean valid = true;
	protected int errorCode;
	
	public boolean isValid() {
		return valid;
	}
	
	public Utilities(JsonObject json) {
		this.json = json;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

	public Utilities setName(String name) {
		this.name=name;
		return this;
	}
	
	public Utilities verifyLength(int min, int max) {
		if(valid) {
			int length = json.get(name).getAsString().length();
			
			if(min > 0) {
				if(length <= min) {
					valid = false;
					errorCode = -1;
				}
			}
			
			if(max > 0) {
				if(length >= max) {
					valid = false;
					errorCode = -1;
				}
			}
		}
		return this;
	}
	
	public Utilities verifyRestrictedChar() {
		if(valid) {
			String str = json.get(name).getAsString();
			for (int i=0; i<str.length(); i++) {
		        char c = str.charAt(i);
		        if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a) {
		            valid = false;
		            errorCode = -2;
		        }
		    }
		}
		return this;
	}
	
	public Utilities verifyNumeric() {
		String str = json.get(name).getAsString();
		for (int i=0; i<str.length(); i++) {
	        char c = str.charAt(i);
	        if (c < 0x30 || c > 0x39)
	            valid = false;
	    }
		valid = true;
	    return this;
	}
}
