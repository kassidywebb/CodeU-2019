package com.google.codeu.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.AbstractCollection;

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
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");
    
    com.google.codeu.data.Message message = new com.google.codeu.data.Message("Alan", "Ola k ase");
    com.google.codeu.data.Message message2 = new com.google.codeu.data.Message("Pepe", "Esta chido el CodeU");
    com.google.codeu.data.Message message3 = new com.google.codeu.data.Message("Pepe", "Que me voy al GooglePlex!!!!!!");
    
    //List of all messages
    List<com.google.codeu.data.Message> allmessages = datastore.getAllMessages();
    allmessages.add(message);
    allmessages.add(message2);
    allmessages.add(message3);
    
    //Json for the JS
    JsonObject jsonObject = new JsonObject();
    
    //Number of messages
    int messageCount = datastore.getTotalMessageCount();
    
    //Number of messages property added to the Json
    jsonObject.addProperty("messageCount", messageCount);
    
    //Store of the biggest message
    com.google.codeu.data.Message biggest = allmessages.get(0);
	
    //Checking the size of the list
	if(allmessages.size()==0) {
		
		jsonObject.addProperty("messageAvg", 0); 
		jsonObject.addProperty("biggestMessage", 0);
		jsonObject.addProperty("userCount", 0);
		jsonObject.addProperty("usersList", "Not any user");
		jsonObject.addProperty("mostActiveUser", "None");
		
	} else {
		/*
		 * countM, total number of messages
		 * users, register of all users
		 */
		int countM = 0;
		ArrayList<String> users = new ArrayList<>();
		ArrayList<Integer> usersNumberOfMessages = new ArrayList<>();
		
		for(int i = 0;i<allmessages.size();i++) {
			//count for the avg of all messages
			countM+= allmessages.get(i).getText().length();
			
			//getting the biggest message
			if(allmessages.get(i).getText().length()>biggest.getText().length()) {
				biggest = allmessages.get(i);  
			}
			
			//getting and saving the user and message count
			if(!users.contains(allmessages.get(i).getUser())) {
				users.add(allmessages.get(i).getUser());
				usersNumberOfMessages.add(1);
			} else {
				usersNumberOfMessages.set(users.indexOf(allmessages.get(i).getUser()), usersNumberOfMessages.get(users.indexOf(allmessages.get(i).getUser()))+1);
			}
		}	  
		
		//Checking who has the most sent messages
		int indexOfMoreMessages = -1;
		int mostMessages = -1;
		
		for(int j = 0;j<usersNumberOfMessages.size();j++) {
			if(usersNumberOfMessages.get(j)>mostMessages) {
				indexOfMoreMessages = j;
				mostMessages = usersNumberOfMessages.get(j); 
			}
		}
		
		int avg = countM/allmessages.size();
		String userMostMessages = users.get(indexOfMoreMessages);
		/*
		 * Adding properties to Json:
		 * Average length of messages
		 * Biggest message found
		 * Total users
		 * List of users
		 * User with most messages sent
		 */
		jsonObject.addProperty("messageAvg", avg);
		jsonObject.addProperty("biggestMessage", biggest.getText());
		jsonObject.addProperty("usersCount", users.size());
		jsonObject.addProperty("usersList", users.toString().substring(1, users.toString().length()-1));
		jsonObject.addProperty("mostActiveUser", userMostMessages);
	}
    
    response.getOutputStream().println(jsonObject.toString());
    
  }
}
