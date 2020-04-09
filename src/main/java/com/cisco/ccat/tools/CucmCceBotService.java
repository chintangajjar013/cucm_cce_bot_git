package com.cisco.ccat.tools;
import org.springframework.stereotype.Service;

@Service
public class CucmCceBotService {
	
	public String runBot(CucmCceBotRequestStructure req,String log) {
		String str[] = {"NULL","NULL"};
		log = log + "We are now in Bot Service class." + "\n";
    	
		GetMessage gm = new GetMessage();   	
    	String msg = gm.getMessage(req.getData().getId());
    	String roomid=req.getData().getRoomId();
    	String fromEmail = req.getData().getPersonEmail();
    	
    	log = log + "the request is recieved from:"+fromEmail+ "\n";
    	log = log + "the request message is:"+msg+"\n";
    	
    	if(msg.startsWith(JinProperties.botName)){  		
    		msg=msg.replaceFirst(JinProperties.botName, "");
    	}
    	msg=msg.trim();
    	if(msg.equalsIgnoreCase("hi")||msg.equalsIgnoreCase("hello")||msg.equalsIgnoreCase("help")||msg.equalsIgnoreCase("hey")){
    	
    		log = log +"Hi, I can help you with JTAPI Device reassociation for given extension. i can find out all the devices associated to given extension(line1) and on personal line(line2).<br>Remove those devices from CUCM JTAPI and add them back.<br> all you need to do is @mention me and provide input as <extension>,<cecid> (i.e. 80970025,chgajjar).";
    		str[0]="Hi, I can help you with JTAPI Device reassociation for given extension. i can find out all the devices associated to given extension(line1) and on personal line(line2).<br>Remove those devices from CUCM JTAPI and add them back.<br> all you need to do is send me your request as <extension>,<cecid> (i.e. 80970025,chgajjar).(@mention me if trying to access from group space)";
    		System.out.print(log);
        	System.out.println("Replying back to the user");
    		gm.postMessage(roomid, str[0]);
    		return "SUCCESS";
    	}
    	
    	String s[]=msg.split(",");
    	if(s.length==2) {
	    	botRunner br = new botRunner();
	    	str = br.botRun(s[0].trim(), s[1].trim());
	    	log=log+str[0];   	
	    	System.out.println("Replying back to the user");
	    	str[0]=str[0]+"<br>To check detailed log for this operation, please join webex team space here:"+JinProperties.log_room_eurl;
	    	gm.postMessage(roomid, str[0]);
	    	if(JinProperties.log_in_Room) {
		    	System.out.println("Posting in Webex Teams for bot log");  
		    	String s1 = str[1].replaceAll("(\r\n|\n)", "<br>");
		    	gm.postMessage(JinProperties.log_room_id,s1);
	    	}
	    	return "SUCCESS";
	    	
    	}
    	else {
    		log = log +"Please provide Valid input in the form of 'extension','cecid'"+"\n"+"i.e. 80972121,chgajjar\n";
    		str[0]="Please provide Valid input in the form of 'extension','cecid' <br>"+
    				"i.e. 80972121,chgajjar";
    		System.out.print(log);
        	System.out.println("Replying back to the user");
        	gm.postMessage(roomid, str[0]);
        	return "SUCCESS";
    	}
    	
	}

	
	
	public String runBotBL(BotLiteRequestStructure req,String log) {
		String str[] = {"NULL","NULL"};
		log = log + "We are now in Bot Service class. The request is recieved from BotLite service" + "\n";
    	
		
		GetMessage gm = new GetMessage();   	
    	String msg = req.getMessage();
    	String fromEmail = req.getPersonEmail();
    	log = log + "the request is recieved from:"+fromEmail+ "\n";
    	log = log + "the request message is:"+msg+"\n";
    	
    	if(msg.startsWith(JinProperties.botName)){  		
    		msg=msg.replaceFirst(JinProperties.botName, "");
    	}
    	msg=msg.trim();
    	if(msg.equalsIgnoreCase("hi")||msg.equalsIgnoreCase("hello")||msg.equalsIgnoreCase("help")||msg.equalsIgnoreCase("hey")){
    	
    		log = log +"Hi, I can help you with JTAPI Device reassociation for given extension. i can find out all the devices associated to given extension(line1) and on personal line(line2).<br>Remove those devices from CUCM JTAPI and add them back.<br> all you need to do is @mention me and provide input as <extension>,<cecid> (i.e. 80970025,chgajjar).";
    		str[0]="Hi, I can help you with JTAPI Device reassociation for given extension. i can find out all the devices associated to given extension(line1) and on personal line(line2).<br>Remove those devices from CUCM JTAPI and add them back.<br> all you need to do is send me your request as <extension>,<cecid> (i.e. 80970025,chgajjar).(@mention me if trying to access from group space)";
    		System.out.print(log);
        	System.out.println("Replying back to the user");
    		return str[0];
    	}
    	
    	String s[]=msg.split(",");
    	if(s.length==2) {
	    	botRunner br = new botRunner();
	    	str = br.botRun(s[0].trim(), s[1].trim());
	    	log=log+str[0];   	
	    	System.out.println("Replying back to the user");
	    	str[0]=str[0]+"<br>To check detailed log for this operation, please join webex team space here:"+JinProperties.log_room_eurl;
	    	if(JinProperties.log_in_Room) {
		    	System.out.println("Posting in Webex Teams for bot log");  
		    	String s1 = str[1].replaceAll("(\r\n|\n)", "<br>");
		    	gm.postMessage(JinProperties.log_room_id,s1);
	    	}
	    	return str[0];	    	
    	}
    	else {
    		log = log +"Please provide Valid input in the form of 'extension','cecid'"+"\n"+"i.e. 80972121,chgajjar\n";
    		str[0]="Please provide Valid input in the form of 'extension','cecid' <br>"+
    				"i.e. 80972121,chgajjar";
    		System.out.print(log);
        	System.out.println("Replying back to the user");

        	return str[0];
    	}
    	
	}
		
}
