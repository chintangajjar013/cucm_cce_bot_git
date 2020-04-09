package com.cisco.ccat.tools;
import java.io.StringReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class FinesseUtility {	
	//This method invokes Finesse test login using extension and password set in prop object
	public String loginToFinesse(BotClientType prop) {
		try {				
				System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
				String fUrl = "https://"+prop.finHost+"/finesse/api/User/"+prop.un;
				prop.addtolog("Inside testLoginToFinesse : Using Finesse URL:"+fUrl);
				HttpPut hput = new HttpPut(fUrl);
				String requestXML = "<User><state>LOGIN</state><extension>"+prop.extension+"</extension></User>";
				StringEntity entity = new StringEntity(requestXML, "UTF-8");
				entity.setContentType("application/XML");
				hput.setEntity(entity);
				String encodeString = prop.un+":"+prop.pass;
				String finHeader = Base64.getEncoder().encodeToString(encodeString.getBytes("UTF-8"));
				hput.setHeader("Authorization", "Basic "+finHeader);		
				CloseableHttpClient httpc = HttpClients.custom().build();
				CloseableHttpResponse httpres;
				httpres = httpc.execute(hput);
				prop.statusCode=httpres.getStatusLine().getStatusCode();
								
				if (prop.statusCode==202) {
					prop.addtolog("Inside testLoginToFinesse() : The login request was accepted with the statusCode= "+prop.statusCode);					
					return "SUCCESS";				
				}
				else 
				{					
					prop.addtolog("Inside testLoginToFinesse() : The login request failed with the statusCode= "+prop.statusCode);
					return "FAILURE";						
				}
			
			} 
		catch (Exception e) {		
			prop.addtolog("Inside testLoginToFinesse() : The login request was rejected with exception= "+e.getLocalizedMessage());					
			e.printStackTrace();
			return "FAILURE";
			}						
		
	}
	
	//This method invokes Finesse test logout using extension and password set in prop object
	public String logoutFromFinesse(BotClientType prop){
		try {			
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			String fUrl = "https://"+prop.finHost+"/finesse/api/User/"+prop.un;
			prop.addtolog("Inside logoutFromFinesse(): Going to use the logout URL:"+fUrl);
			HttpPut hput = new HttpPut(fUrl);
			String requestXML = "<User><state>LOGOUT</state></User>";
			StringEntity entity = new StringEntity(requestXML, "UTF-8");
			entity.setContentType("application/XML");
			hput.setEntity(entity);
			String encodeString = prop.un+":"+prop.pass;
			String finHeader = Base64.getEncoder().encodeToString(encodeString.getBytes("UTF-8"));
			hput.setHeader("Authorization", "Basic "+finHeader);				
			CloseableHttpClient httpc = HttpClients.custom().build();
			CloseableHttpResponse httpres;
			httpres = httpc.execute(hput);
			int statusCode=httpres.getStatusLine().getStatusCode();
			if (statusCode==202){
				prop.addtolog("Inside logoutFromFinesse(): The Logout request was accepted with statusCode="+statusCode);
				return "SUCCESS";
			}
			else {
				prop.addtolog("Inside logoutFromFinesse(): The logout request failed with statusCode="+statusCode);
				return "FAILURE";
			}					
									
		} 
		catch (Exception e) {
			prop.addtolog("Inside logoutFromFinesse(): The logout request failed with Exception="+e.getLocalizedMessage());
			return "FAILURE";
		}				
				
	}
	
	//This method checks login status for given user
	public String checkUserStatus(BotClientType prop)
	{
		try{
			String fUrl = "https://"+prop.finHost+"/finesse/api/User/"+prop.un;
			prop.addtolog("Inside checkUserStatus(): using the url "+fUrl);
			HttpGet hget = new HttpGet(fUrl);		
			String encodeString = prop.un+":"+prop.pass;
			String finHeader = Base64.getEncoder().encodeToString(encodeString.getBytes("UTF-8"));
			hget.setHeader("Authorization","Basic "+finHeader);		
			CloseableHttpClient httpc = HttpClients.custom().build();
			CloseableHttpResponse httpres;
			httpres = httpc.execute(hget);
			int statusCode=httpres.getStatusLine().getStatusCode();
			if (statusCode==200) {
				prop.addtolog("Inside checkUserStatus(): Test user login check request got accepted with:"+statusCode);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder;
		        InputSource is;
		        builder = factory.newDocumentBuilder();
		        is = new InputSource(httpres.getEntity().getContent());	        
		        org.w3c.dom.Document doc = builder.parse(is);
		        NodeList list = doc.getElementsByTagName("state");
		        String s = list.item(0).getTextContent();
		        prop.addtolog("Inside checkUserStatus(): The Test User Login State is:"+s);
		        return s;   
			}
			else {
				prop.addtolog("Inside checkUserStatus():Test user login check request got rejected with:"+statusCode);
				return "ERROR";
			}
			
		}
		catch (Exception e) {
			prop.addtolog("Inside checkUserStatus():Test user login check request got rejected with Exception:"+e.getLocalizedMessage());			
			return "ERROR";
		}
									
	}

	//New Method
	public HashMap<Integer, LineObject> getLineList(BotClientType prop) {
		HashMap<Integer, LineObject> hm = new HashMap<Integer, LineObject>();
		try{			
			 String sql = 
					 "Select DISTINCT n2.dnorpattern,map2.fknumplan from devicenumplanmap map2,numplan n2 where map2.fkdevice in "+
					 "( select fkdevice from devicenumplanmap map1 where map1.fknumplan in ( Select map.fknumplan from devicenumplanmap map,numplan n "+
					 	"where map.fkdevice in (Select map1.fkdevice from devicenumplanmap map1 where map1.fknumplan in (Select np.pkid from numplan np where np.dnorpattern='"+prop.extension+"')) "+
					 	"and n.pkid = map.fknumplan)) "+
					 "and n2.pkid = map2.fknumplan";
					 
					String ucmHst = "https://"+prop.ucmhost+":8443/axl/";			
			 		String bdy=
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.cisco.com/AXL/API/12.0\">"
					+"<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<ns:executeSQLQuery>"
					+ "<sql>"
					+ sql
					+ "</sql>"
					+ "</ns:executeSQLQuery>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";

			//prop.addtolog("Using the request xml for the device count:"+bdy);	 		
			HttpResponse<String> response = Unirest.post(ucmHst)
					  .header("cache-control", "no-cache")
					  .header("Authorization", "Basic "+prop.ucmHeader)
					  .body(bdy)
					  .asString();
			
			if(response.getStatus()==200) {
		  		String xml = response.getBody();
		  		org.jdom2.input.SAXBuilder saxBuilder = new SAXBuilder();		      
	          	org.jdom2.Document document = saxBuilder.build(new StringReader(xml));		  		
	  			Element e = document.getRootElement();			  		
	  			List<Element> c=  e.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren();
	  			int i=c.size();	
	  			if(i==0){
	  				prop.addtolog("Inside getLineList():No Lines Found on Device for Extension:"+prop.extension);
	  			}	  			
	  			if(i>0){
	  				String s3="";
	  				for(int m=0;m<i;m++) {
	  				String s1,s2;
	  				s1 = c.get(m).getChildText("dnorpattern");
			  		s2 = c.get(m).getChildText("fknumplan");
			  		hm.put(m, new LineObject(s1,s2));
			  		s3=s3+s1+",";
	  				}	  				
	  				prop.addtolog("Inside getLineList(): total number of lines to be considered for this opertions are "+s3);
	  			}
			  	
			  }			
			else{	
				prop.addtolog("Inside getLineList(): The SQL request for getting List for given line:"+response.getStatus());				
			}
						
		} 
		catch (Exception e) {
			prop.addtolog("Inside getLineList(): The SQL request for getting list of line failed:"+ e.getLocalizedMessage());
			e.printStackTrace();
		}
		return hm;	
	}
	
	//New Method
	public String removeJtapi(LineObject lo,BotClientType prop) {
		try {			
			
			String sql= "Select d.name,d.tkclass, map.fkdevice from devicenumplanmap map,device d "+
						"where map.fknumplan in ('"+lo.linePkid+"') "+
						"and d.pkid=map.fkdevice";
			
					//prop.addtolog("Inside removeJtapi(): trying to remove/Add Devices for extension "+lo.line);	     
					String ucmHst = "https://"+prop.ucmhost+":8443/axl/";			
			 		String bdy=
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.cisco.com/AXL/API/12.0\">"
					+"<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<ns:executeSQLQuery>"
					+ "<sql>"
					+ sql
					+ "</sql>"
					+ "</ns:executeSQLQuery>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";
			HttpResponse<String> response = Unirest.post(ucmHst)
					  .header("cache-control", "no-cache")
					  .header("Authorization", "Basic "+prop.ucmHeader)
					  .body(bdy)
					  .asString();
			
			if(response.getStatus()==200) {
		  		String xml = response.getBody();
		  		org.jdom2.input.SAXBuilder saxBuilder = new SAXBuilder();		      
	          	org.jdom2.Document document = saxBuilder.build(new StringReader(xml));		  		
	  			Element e = document.getRootElement();			  		
	  			List<Element> c=e.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren();
	  			int i=c.size();	
	  			if(i==0){
	  				prop.addtolog("Inside removeJtapi(): No device for given extension");
	  			}  			
	  			if(i>0){
	  				prop.addtolog("Inside removeJtapi(): Remove operaion started for the line "+lo.line);	  				
	  				for(int m=0;m<i;m++) {
	  				String s1,s2,s3;
	  				s1 = c.get(m).getChildText("fkdevice");	
	  				s2 = c.get(m).getChildText("name");
	  				s3 = c.get(m).getChildText("tkclass");
	  				prop.addtolog("Inside addJtapi(): Removing device "+s2);
	  				String s=this.isDeviceAssociated(s1, prop);
		  				if(s.equalsIgnoreCase("TRUE")) {
		  					//prop.addtolog(s);
		  					this.deassociateDevice(s1, prop);		  					
		  				}
	  				}	  				
	  				prop.addtolog("Inside removeJtapi(): Remove operaion completed for the line "+lo.line);  				
	  				 				
	  			}
	  			prop.addtolog("\n");
  				return "SUCCESS";
  				
			  }			
			else{	
				prop.addtolog("Inside removeJtapi(): Device JTAPI Removal failed"+response.getStatus());
  				return "FAILURE";
			}
						
		}
		catch (Exception e) {
			prop.addtolog("Inside removeJtapi(): Device JTAPI Removal failed"+ e.getLocalizedMessage());
			e.printStackTrace();
			return "FAILURE";
		}
				
	}
	
	public String addJtapi(LineObject lo,BotClientType prop) {
		try {			
			
			String sql= "Select d.name,d.tkclass, map.fkdevice from devicenumplanmap map,device d "+
						"where map.fknumplan in ('"+lo.linePkid+"') "+
						"and d.pkid=map.fkdevice";
			
					//prop.addtolog("Inside dojtapiOperation(): trying to remove/Add Devices for extension "+lo.line);	     
					String ucmHst = "https://"+prop.ucmhost+":8443/axl/";			
			 		String bdy=
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.cisco.com/AXL/API/12.0\">"
					+"<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<ns:executeSQLQuery>"
					+ "<sql>"
					+ sql
					+ "</sql>"
					+ "</ns:executeSQLQuery>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";
			HttpResponse<String> response = Unirest.post(ucmHst)
					  .header("cache-control", "no-cache")
					  .header("Authorization", "Basic "+prop.ucmHeader)
					  .body(bdy)
					  .asString();
			
			if(response.getStatus()==200) {
		  		String xml = response.getBody();
		  		org.jdom2.input.SAXBuilder saxBuilder = new SAXBuilder();		      
	          	org.jdom2.Document document = saxBuilder.build(new StringReader(xml));		  		
	  			Element e = document.getRootElement();			  		
	  			List<Element> c=e.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren();
	  			int i=c.size();	
	  			if(i==0){
	  				prop.addtolog("Inside addJtapi(): No device for given extension");
	  			}  			
	  			if(i>0){
	  				prop.addtolog("Inside addJtapi(): Add operaion started for the line "+lo.line);
	  				

	  				for(int m=0;m<i;m++) {
		  				String s1,s2,s3,s4;
		  				s1 = c.get(m).getChildText("fkdevice");	
		  				s2 = c.get(m).getChildText("name");
		  				s3 = c.get(m).getChildText("tkclass");
		  				String s=this.isDeviceAssociated(s1, prop);
		  				if(s.equalsIgnoreCase("TRUE")) {
		  					//prop.addtolog(s);
		  					this.deassociateDevice(s1, prop);		  					
		  				}
		  				if(s3.contains("254")){
		  					s4="7";
		  				}
		  				else {
		  					s4="1";
		  				}
			  			prop.addtolog("Inside addJtapi(): adding device "+s2);
			  			this.associateDevice(s1, prop, s4);
		  				
	  				}
	  				prop.addtolog("Inside addJtapi(): Add operaion completed for the line "+lo.line);
	  				
	  			}
	  			prop.addtolog("\n");
  				return "SUCCESS";
  				
			  }			
			else{	
				prop.addtolog("Inside addJtapi(): Device JTAPI Add Operation failed"+response.getStatus());
  				return "FAILURE";
			}
						
		}
		catch (Exception e) {
			prop.addtolog("Inside addJtapi(): Device JTAPI Add Operation failed"+ e.getLocalizedMessage());
			e.printStackTrace();
			return "FAILURE";
		}
				
	}	
	
	//New Method	
	public String isDeviceAssociated(String deviceID,BotClientType prop) {
		try {
			String sql = "Select * from applicationuserdevicemap appdm,applicationuser app "
			         	+"where app.name='"+prop.appuser+"' "
			         	+"and appdm.fkapplicationuser=app.pkid "
			         	+"and appdm.fkdevice='"+deviceID+"'";
			
			String ucmHst = "https://"+prop.ucmhost+":8443/axl/";
			String bdy=
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.cisco.com/AXL/API/12.0\">"
					+"<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<ns:executeSQLQuery>"
					+ "<sql>"
					+ sql
					+ "</sql>"
					+ "</ns:executeSQLQuery>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";

			HttpResponse<String> response = Unirest.post(ucmHst)
					  .header("cache-control", "no-cache")
					  .header("Authorization", "Basic "+prop.ucmHeader)
					  .body(bdy)
					  .asString();
			
			if(response.getStatus()==200) {
		  		String xml = response.getBody();
		  		org.jdom2.input.SAXBuilder saxBuilder = new SAXBuilder();		      
	          	org.jdom2.Document document = saxBuilder.build(new StringReader(xml));		  		
	  			Element e = document.getRootElement();			  		
	  			List<Element> c=  e.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren();
	  			int i=c.size();
	  			if(i>0){
	  				prop.addtolog("Inside isDeviceAssociated(): Found device already associated to the JTAPI");
	  				return "TRUE";
	  			}
	  			else{
	  				prop.addtolog("Inside isDeviceAssociated(): Found no device associated to the JTAPI");
	  				return "FALSE";
	  			}
	  			
			}
			else{
				prop.addtolog("Inside isDeviceAssociated(): There was an error while trying to find the JTAPI associations:"+response.getStatus());
  				return "FAILURE";			
			}
		}
		catch(Exception e) {
			prop.addtolog("Inside isDeviceAssociated(): There was an exception while trying to find the JTAPI association:"+e.getLocalizedMessage());
			return "FAILURE";
		}
		
	}
	
	//New method
	public String deassociateDevice(String deviceID,BotClientType prop){
		try 
		{

			String sql = "delete from applicationuserdevicemap where fkdevice='"+deviceID+"' and fkapplicationuser in (select pkid from applicationuser where name='"+prop.appuser+"')";
			String ucmHst = "https://"+prop.ucmhost+":8443/axl/";			
			String bdy=
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.cisco.com/AXL/API/12.0\">"
					+"<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<ns:executeSQLUpdate>"
					+ "<sql>"
					+ sql
					+ "</sql>"
					+ "</ns:executeSQLUpdate>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";

			//prop.addtolog("Using the request xml for the userlist:"+bdy);						
			HttpResponse<String> response = Unirest.post(ucmHst)
					  .header("cache-control", "no-cache")
					  .header("Authorization", "Basic "+prop.ucmHeader)
					  .body(bdy)
					  .asString();
			
			if(response.getStatus()==200)
			{
				//200 does not mean device was deassociated successfully you got to check for the number of rows updated
				prop.addtolog("Inside deassociateDevice(): Device deassociated successfully");
				return "SUCCESS";
			}
			else
			{
				prop.addtolog("Inside deassociateDevice(): Device deassociated unsuccessfully, error code:"+response.getStatus());
				return "FAILURE";
			}		
		}
		catch(Exception e) 
		{
			prop.addtolog("Inside deassociateDevice(): There was an exception while trying JTAPI Deassociation:"+e.getLocalizedMessage());
			return "FAILURE";			
		}
		
	}	
	
	//new method
	public String associateDevice(String deviceID, BotClientType prop,String s4)
	{
		try 
		{			
			String sql="insert into applicationuserdevicemap (pkid,description,fkapplicationuser,fkdevice,tkuserassociation) values (newid(),'','"+prop.apppkid+"','"+deviceID+"',"+s4+")"; 
			String ucmHst = "https://"+prop.ucmhost+":8443/axl/";			
			String bdy=
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.cisco.com/AXL/API/12.0\">"
					+"<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<ns:executeSQLUpdate>"
					+ "<sql>"
					+ sql
					+ "</sql>"
					+ "</ns:executeSQLUpdate>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";

			//prop.addtolog("Using the request xml for the userlist:"+bdy);						
			HttpResponse<String> response = Unirest.post(ucmHst)
					  .header("cache-control", "no-cache")
					  .header("Authorization", "Basic "+prop.ucmHeader)
					  .body(bdy)
					  .asString();
			
			if(response.getStatus()==200){
				//200 does not mean device was deassociated successfully you got to check for the number of rows updated
				prop.addtolog("Inside associateDevice(): device associated successfully");
				//prop.addtolog("Inside associateDevice():"+bdy);
				//prop.addtolog("Inside associateDevice():"+ response.getStatusText());
				
				return "SUCCESS";
			}
			else
			{
				prop.addtolog("Inside associateDevice(): device associated unsuccessfully, error code:"+response.getStatus());
				return "FAILURE";
			}		
		}
		catch(Exception e) 
		{
			prop.addtolog("Inside associateDevice(): there was an exception while trying JTAPI association:"+e.getLocalizedMessage());
			return "FAILURE";			
		}
		
	}
	
	//This method grabs Stirng pkid for the application user set in prop object
	public String getAppUserID(BotClientType prop)
	{
		try 
		{
			String sql = "Select pkid from applicationuser where name='"+prop.appuser+"'";
			String ucmHst = "https://"+prop.ucmhost+":8443/axl/";			
			
			String bdy=
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://www.cisco.com/AXL/API/12.0\">"
					+"<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<ns:executeSQLQuery>"
					+ "<sql>"
					+ sql
					+ "</sql>"
					+ "</ns:executeSQLQuery>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";

			//prop.addtolog("Using the request xml for the userlist:"+bdy);						
			HttpResponse<String> response = Unirest.post(ucmHst)
					  .header("cache-control", "no-cache")
					  .header("Authorization", "Basic "+prop.ucmHeader)
					  .body(bdy)
					  .asString();
			
			if(response.getStatus()==200) {
		  		String xml = response.getBody();
		  		org.jdom2.input.SAXBuilder saxBuilder = new SAXBuilder();		      
	          	org.jdom2.Document document = saxBuilder.build(new StringReader(xml));		  		
	  			Element e = document.getRootElement();			  		
	  			List<Element> c=  e.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren();
	  			int i=c.size();	
	  			if(i==0){
	  				prop.addtolog("Inside getAppUserID(): No pkid found for given application user found");
	  				return "FAILURE";
	  			}	  			
	  			if(i==1){			  					  		
	  				prop.apppkid = c.get(0).getChildText("pkid");
	  				prop.addtolog("Inside getAppUserID(): pkid "+prop.apppkid+" found for the given appuser");
	  				return "SUCCESS";
	  			}
			  	else{
	  				prop.addtolog("Inside getAppUserID(): More than 1 pkid found for given application user");
	  				return "FAILURE";
			  	}
			}
			else
			{
				prop.addtolog("Inside getAppUserID(): Could not obtain the appuser pkid, error:"+response.getStatus());
				prop.addtolog("Inside getAppUserID():"+response.getBody());
				return "FAILURE";
			}		
		
		}		
		catch(Exception e) {
			prop.addtolog("Inside getAppUserID(): Could not obtain the appuser pkid, exception:"+e.getLocalizedMessage());
			e.printStackTrace();
			return "FAILURE";			
		}
		
	}
}