
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

    int messageCount = datastore.getTotalMessageCount();

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("messageCount", messageCount);
    response.getOutputStream().println(jsonObject.toString());
  }
  
  /*
   * Obtaining the average message by going through all the list
   */
  public void avgMessage(HttpServletRequest request, HttpServletResponse response) 
		  throws IOException{
	  
	  response.setContentType("application/json");
	  
	  List<com.google.codeu.data.Message> allmessages = datastore.getAllMessages();
	  
	  if(allmessages.size()==0) {
		  JsonObject jsonObject = new JsonObject();
		  jsonObject.addProperty("messageAvg", 0);
		  response.getOutputStream().println(jsonObject.toString());
		  
	  } else {
		  int count = 0;
		  
		  for(int i = 0;i<allmessages.size();i++) {
			  count+= allmessages.get(i).getText().length();
		  }
		  
		  int avg = count/allmessages.size();
		  JsonObject jsonObject = new JsonObject();
		  jsonObject.addProperty("messageAvg", avg);
		  response.getOutputStream().println(jsonObject.toString());
	  }
  }


  public void biggestMessage(HttpServletRequest request, HttpServletResponse response) 
		  throws IOException{
	  
	  response.setContentType("application/json");
	  
	  List<com.google.codeu.data.Message> allmessages = datastore.getAllMessages();
	  
	  if(allmessages.size()!=0) {
		  com.google.codeu.data.Message biggest = allmessages.get(0);
		  
		  for(int i = 1;i<allmessages.size();i++) {
			  if(allmessages.get(i).getText().length()>biggest.getText().length()) {
				biggest = allmessages.get(i);  
			  }
		  }
		  
		  JsonObject jsonObject = new JsonObject();
		  jsonObject.addProperty("biggestMessage", biggest.toString());
		  response.getOutputStream().println(jsonObject.toString());
	  } else {
		  JsonObject jsonObject = new JsonObject();
		  jsonObject.addProperty("biggestMessage", 0);
		  response.getOutputStream().println(jsonObject.toString());
	  }
	  
}
}

