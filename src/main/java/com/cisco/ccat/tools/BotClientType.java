package com.cisco.ccat.tools;


public class BotClientType {
	String un;
	String pass;
	String ucmhost;
	String finHost;
	String log;
	String statusReason;	
	String extension;
	String ucmHeader;
	String appuser;
	String apppkid;
	int statusCode;
	
	
	public void addtolog(String s) {
		log = log+s+"\n";
	}
}
