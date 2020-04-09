package com.cisco.ccat.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CucmCceBotController {

	@Autowired
	private CucmCceBotService productService;

	@RequestMapping(method=RequestMethod.POST, value="/tshoot")
	public String getResponse(@RequestBody CucmCceBotRequestStructure product) {		
    	//we dont respondback to bot		
    	if(product.getData().getPersonEmail().contains(JinProperties.botEmail)){
        	return "SUCCESS";
    	} 	
		String log="";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date( );
		log = log + dateFormat.format(date).toString()+": Recieved the request to invoke BOT, we are in controller class."+"\n";	
		String s=productService.runBot(product,log);
		System.out.println("Bot Operation Completed");
		System.out.println("\n");
		return s;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/tshootBL")
	public String getResponseBL(@RequestBody BotLiteRequestStructure product) {		
    	//we dont respondback to bot		
    	if(product.getPersonEmail().contains(JinProperties.botEmail)){
        	return "";
    	} 	
		String log="";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date( );
		log = log + dateFormat.format(date).toString()+": Recieved the request to invoke BOT, we are in controller class."+"\n";	
		String s=productService.runBotBL(product,log);
		System.out.println("Bot Operation Completed");
		System.out.println("\n");
		return s;
	}
}
