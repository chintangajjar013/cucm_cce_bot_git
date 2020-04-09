package com.cisco.ccat.tools;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class JinProperties {	
	public static String cce_agent_username = "<finesse agent username>";
	public static String cce_agent_password = "<finesse agent password>";
	public static String ucmUsername = "AXL username";
	public static String ucmPassword = "AXL password";
	public static String cucmEncodeString = ucmUsername+":"+ucmPassword;
	public static String botToken = "security token for bot obtained from webex teams";
	public static String botName = "BOT name";
	public static String botEmail = "BOT email";
	public static String wxURL = "https://api.ciscospark.com/v1/messages/";
	public static boolean log_in_Room = true;
	public static String log_room_id = "<if you want to post logs in webex room, your webex room id where bot is already added>";
	public static String log_room_eurl = "eurl to to join webex room where bot posts the detailed log";
	
	public static String getucmHeader() throws UnsupportedEncodingException{
		String ucmHeader = Base64.getEncoder().encodeToString(JinProperties.cucmEncodeString.getBytes("UTF-8"));
		return ucmHeader;
	}
	
	//returns the cucm address for given extension
	//build your ucm logic here
	public static String getCUCM(String extension) {
		
		if(extension.matches("^8XXX....")) {
			return "ucm1.acme.com";
		}
		if(extension.matches("^9XXX....")) {
			return "ucm2.acme.com";
		}
		return "NULL";
	}
	
	//returns the finesse address address for given extension
	//build your finesse logic here

	public static String getFinesse(String extension) {
		
		if(extension.matches("^8XXX....")) {
			return "fin1.acme.com";
		}
		if(extension.matches("^9XXX....")) {
			return "fin2.acme.com";
		}
		return "NULL";
	}

	
	//returns the ucm application username for given extension
	//build your ucm logic here
	
public static String getAppUser(String extension) {
	
		if(extension.matches("^8XXX....")) {
			return "app-user1";
		}
		if(extension.matches("^9XXX....")) {
			return "app-user2";
		}
		return "NULL";
	}

}
