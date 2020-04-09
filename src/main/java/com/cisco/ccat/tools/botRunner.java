package com.cisco.ccat.tools;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class botRunner {
	
	public String[] botRun(String extension,String user) {

		//Lets run the bot
		String br= "<br>";
		String res = "BOT is trying to execute your request, please wait"+br;
		//lets validate the input

		if(JinProperties.getFinesse(extension).equalsIgnoreCase("null")) {
			
			res = res+ "Please provide Valid Agent Extension"+br;
			return new String[] {res,"NULL"};
		}
		
		//lets set the properties first
		BotClientType bct = new BotClientType();
		bct.un = JinProperties.cce_agent_username;
		bct.pass = JinProperties.cce_agent_password;
		bct.extension = extension;
		bct.finHost = JinProperties.getFinesse(extension);
		bct.ucmhost = JinProperties.getCUCM(extension);
		try {
			bct.ucmHeader = JinProperties.getucmHeader();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			res = res+"BOT unable to get cucm security header"+br;
			return new String[] {res,bct.log};
		}
		bct.appuser = JinProperties.getAppUser(extension);
		
		//getting the applicatio  user pkid for UCM
		FinesseUtility fu = new FinesseUtility();
		if(!fu.getAppUserID(bct).equalsIgnoreCase("success")){
			res = res+"BOT unable to get cucm application user id,please contact your system administrator"+br;
			return new String[] {res,bct.log};		
			}
		
		//let get the list of line
		HashMap<Integer,LineObject> hm;
		hm=fu.getLineList(bct);
		if(hm.size()<=0) {
			System.out.println(bct.log);
			res = res+"There was no device found on "+bct.ucmhost+"for the given extension"+br;
			return new String[] {res,bct.log};
		}
		
		//now lets do the JTAPI Remove on devices/lines retrieved
		res = res+"Starting to do the JTAPI Operations on: "+bct.ucmhost+" for device associated to line "+bct.extension+" and its co-lines"+br;
		for(int i=0;i<hm.size();i++) {
			res = res+"Removing all devices for "+hm.get(i).line+br;
			fu.removeJtapi(hm.get(i), bct);			
		}
		
		//now lets do the JTAPI Add on devices/lines retrieved
		for(int i=0;i<hm.size();i++) {
			res = res+"Adding all devices for "+hm.get(i).line+br;
			fu.addJtapi(hm.get(i), bct);
		}
		
		res= res+"Operation Completed"+br;
		System.out.print(bct.log);
		return new String[] {res,bct.log};
	}

}
