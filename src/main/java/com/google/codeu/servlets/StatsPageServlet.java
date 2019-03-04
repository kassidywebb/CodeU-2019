package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.mail.MailService.Message;
import com.google.codeu.data.Datastore;
import com.google.gson.JsonObject;

/**
 * Handles fetching site statistics.
 */
@WebServlet("/stats")
public class StatsPageServlet extends HttpServlet{

  private Datastore datastore;
  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with site statistics in JSON.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("application/json");
    
    //Json for the JS
    JsonObject jsonObject = new JsonObject();
    
    //Number of messages
    int messageCount = datastore.getTotalMessageCount();
    
    //Number of messages property added to the Json
    jsonObject.addProperty("messageCount", messageCount);
    
    //List of all messages
    List<com.google.codeu.data.Message> allmessages = datastore.getAllMessages();
    com.google.codeu.data.Message message = new com.google.codeu.data.Message("Alan", "Ola k ase");
    allmessages.add(message);
	
    //Checking the size of the list
	if(allmessages.size()==0) {
		jsonObject.addProperty("messageAvg", 0); 
	} else {
		int count = 0;
		  
		for(int i = 0;i<allmessages.size();i++) {
	    count+= allmessages.get(i).getText().length();
		}	  
		
		int avg = count/allmessages.size();
		jsonObject.addProperty("messageAvg", avg);
	}
	  
	//
	if(allmessages.size()!=0) {
		com.google.codeu.data.Message biggest = allmessages.get(0);
		  
		for(int i = 1;i<allmessages.size();i++) {
			if(allmessages.get(i).getText().length()>biggest.getText().length()) {
			biggest = allmessages.get(i);  
			}
		}
		jsonObject.addProperty("biggestMessage", biggest.getText());
	  } else {
		jsonObject.addProperty("biggestMessage", 0);
	  }
    
    response.getOutputStream().println(jsonObject.toString());
    
  }
}
