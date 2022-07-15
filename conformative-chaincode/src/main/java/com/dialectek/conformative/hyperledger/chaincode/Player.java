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
   private final String name;

   @Property()
   private final String gameCode;

   @Property()
   private double personalResources;

   @Property()
   private double entitledResources;
   
   @Property()   
   ArrayList<String> playerChat;
   
   @Property()
   ArrayList<String> claimantChat;
   
   @Property()
   ArrayList<String> auditorChat;
   
   public Player(@JsonProperty("name") final String name,
           @JsonProperty("gameCode") final String gameCode) 
   {
      this.name         = name;
      this.gameCode     = gameCode;
      personalResources = 0.0;
      entitledResources = 0.0;
      playerChat = new ArrayList<String>();
      claimantChat = new ArrayList<String>();
      auditorChat = new ArrayList<String>();
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
   
   public void addPlayerChat(String message)
   {
      playerChat.add(message);
   }

   public ArrayList<String> getPlayerChat()
   {
      return playerChat;
   }
   
   public void clearPlayerChat()
   {
      playerChat.clear();
   } 
   
   public void addClaimantChat(String message)
   {
      claimantChat.add(message);
   }

   public ArrayList<String> getClaimantChat()
   {
      return claimantChat;
   }
   
   public void clearClaimantChat()
   {
      claimantChat.clear();
   } 
   
   
   public void addAuditorChat(String message)
   {
      auditorChat.add(message);
   }

   public ArrayList<String> getAuditorChat()
   {
      return auditorChat;
   }
   
   public void clearAuditorChat()
   {
      auditorChat.clear();
   }        
}
