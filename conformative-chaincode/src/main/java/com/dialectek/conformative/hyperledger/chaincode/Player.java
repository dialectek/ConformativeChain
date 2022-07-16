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
   ArrayList<String> playerMessages;
   
   @Property()
   ArrayList<String> claimantMessages;
   
   @Property()
   ArrayList<String> auditorMessages;
   
   public Player(@JsonProperty("name") final String name,
           @JsonProperty("gameCode") final String gameCode) 
   {
      this.name         = name;
      this.gameCode     = gameCode;
      personalResources = 0.0;
      entitledResources = 0.0;
      playerMessages = new ArrayList<String>();
      claimantMessages = new ArrayList<String>();
      auditorMessages = new ArrayList<String>();
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
   
   public void addPlayerMessage(String message)
   {
      playerMessages.add(message);
   }

   public ArrayList<String> getPlayerMessages()
   {
      return playerMessages;
   }
   
   public void clearPlayerMessages()
   {
      playerMessages.clear();
   } 
   
   public void addClaimantMessage(String message)
   {
      claimantMessages.add(message);
   }

   public ArrayList<String> getClaimantMessages()
   {
      return claimantMessages;
   }
   
   public void clearClaimantMessages()
   {
      claimantMessages.clear();
   } 
      
   public void addAuditorMessage(String message)
   {
      auditorMessages.add(message);
   }

   public ArrayList<String> getAuditorMessages()
   {
      return auditorMessages;
   }
   
   public void clearAuditorMessages()
   {
      auditorMessages.clear();
   }        
}
