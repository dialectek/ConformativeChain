// Player.

package com.dialectek.conformative.hyperledger.chaincode;

import java.util.ArrayList;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class Player
{  
   @Property()
   public final String name;

   @Property()
   public final String gameCode;

   @Property()
   public double personalResources;

   @Property()
   public double entitledResources;
   
   @Property()   
   public String[] messages;

   public Player(@JsonProperty("name") final String name,
           @JsonProperty("gameCode") final String gameCode) 
   {
      this.name         = name;
      this.gameCode     = gameCode;
      personalResources = 0.0;
      entitledResources = 0.0;
      messages = new String[0];
   }

   public String getName()
   {
      return(name);
   }


   public String getGameCode()
   {
      return(gameCode);
   }


   public double getPersonalResources()
   {
      return(personalResources);
   }


   public void setPersonalResources(double personalResources)
   {
      this.personalResources = personalResources;
   }


   public double getEntitledResources()
   {
      return(entitledResources);
   }


   public void setEntitledResources(double entitledResources)
   {
      this.entitledResources = entitledResources;
   }
   
   public void addMessage(String message)
   {
	  int n = messages.length;
	  String[] tmp = new String[n + 1];
	  for (int i = 0; i < n; i++)
	  {
		  tmp[i] = messages[i];
	  }
	  tmp[n] = message;
      messages = tmp;
   }

   public ArrayList<String> getMessages()
   {
	  ArrayList<String> results = new ArrayList<String>();
	  for (String message: messages)
	  {
		  results.add(message);
	  }
      return results;
   }
   
   public void clearMessages()
   {
      messages = new String[0];
   }           
}
