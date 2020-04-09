package com.cisco.ccat.tools;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class GetMessage 
{
	 private String REST_URI = JinProperties.wxURL;
	 private String token = JinProperties.botToken;
	 
	 public String getMessage(String id)
	 {
		try {
			 HttpResponse<String> response = Unirest.get(REST_URI+id)
					  .header("cache-control", "no-cache")
					  .header("Authorization", "Bearer "+token)
					  .asString();
			if(response.getStatus()==200){
				JSONObject obj = new JSONObject(response.getBody());
				String s ="";
				s=(String) obj.get("text");				
				return s;
			}
			else{
				System.out.println(response.getStatus()+" "+response.getStatusText());
				return "FAILURE";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILURE";
		} 
	 }
	 

	 public String postMessage(String id, String txt)
	 {
		try {
			
			String bdy = "{"+ 
							"\"markdown\":"+"\""+txt+"\""+","+
							"\"roomId\":"+"\""+id+"\""+
							"}";
			
			 HttpResponse<String> response = Unirest.post(REST_URI)
					  .header("cache-control", "no-cache")
					  .header("Content-Type", "application/json")
					  .header("Authorization", "Bearer "+token).body(bdy)
					  .asString();
			 
			 System.out.println("Body:");
			 System.out.println(bdy);			 
			 
			if(response.getStatus()==200){
				System.out.println("Message Posted Successfully");				
				return "SUCESS";
			}
			else{
				System.out.println(response.getStatus()+" "+response.getBody());
				
				return "FAILURE";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILURE";
		} 
	 
	 }	 	 
	 
   }
	


