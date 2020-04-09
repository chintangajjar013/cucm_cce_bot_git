# cucm_cce_bot_git
 
This is sample project which lets you create you webex teams bot which takes input in the form of <agent extension>,<username>
and resets JTAPI association on CUCM as following:

Step 1) the bot loads the data from JinPorperties.class file, so Setup the JinPorperties.java file with AXL creds, webex bot paramters, CUCM/Finesse server selection logic.

Step 2) based on the agent extension provided the bot tries to find the CUCM server it should use. The logic is available in JinProperties.java file, you can modify the logic to meet your business specific requirement.

Step 3) the bot is going to find out all co-lines for given extension, co-lines are other line which exist with given extension on same device.

Step 4) the BOT then tries to find out list of device available in CUCM for all those list of lines.

Step 5) the BOT is then going to remove the phone from application/JTAPI user, application/JTAPI username logic is built in JinProperties.java file.

Step 6) the BOT then associates the list of devices back to JTAPI.
 
 What else you will need:

 1) The bot is written using spring boot frame work, import the bot(Spring STS is preferred) and try to understand the code.
 The bot listens on http://localhost:8000/tshoot
 the bot can be trigerred by HTTP POST, the bot accepts the standard webex webhook request payload as below:
 https://developer.webex.com/docs/api/guides/webhooks#handling-requests-from-webex-teams
 
 2) you have to setup webex teams bot as documented here:
 https://developer.webex.com/docs/bots

 3) after creating bot you have create a webhook for the BOT to post the message when its @mentioned or sent the messages directly:
 https://developer.webex.com/docs/api/guides/webhooks
 the url for the webhook should be https://<your application server>:8000/tshoot
 
 4) bot uses a separte webex teams room to submit the log the operation details. Create a webex teams room and add bot to that team and provide that room id in JinProperties.Java file.
 
