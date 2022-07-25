// Game.

package com.dialectek.conformative.hyperledger.chaincode;

import java.util.ArrayList;
import java.util.Collections;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.dialectek.conformative.hyperledger.shared.DelimitedString;
import com.dialectek.conformative.hyperledger.shared.Shared;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class Game
{
   @Property()
   private final String code;

   @Property()
   private final double initialCommonResources;

   @Property()
   private double commonResources;
   
   @Property()
   private ArrayList<String> playerNames;
   
   @Property()
   private int state;
   
   @Property()
   private int transactionCount;
   
   @Property()
   private ArrayList<String> hostMessages;
   
   public Game(@JsonProperty("code") final String code, 
           @JsonProperty("initialCommonResources") final double initialCommonResources) 
   {
	  this.code = code;
      this.initialCommonResources = initialCommonResources;
      commonResources             = initialCommonResources;
      playerNames = new ArrayList<String>();
      hostMessages = new ArrayList<String>();
      state = Shared.PENDING;
      transactionCount = 0;
   }


   public String getCode()
   {
      return(code);
   }


   public double getInitialCommonResources()
   {
      return(initialCommonResources);
   }


   public double getCommonResources()
   {
      return(commonResources);
   }


   public void setCommonResources(double commonResources)
   {
      this.commonResources = commonResources;
   }

   public ArrayList<String> getPlayerNames()
   {
      return(playerNames);
   }

   public void clearPlayerNames()
   {
      playerNames.clear();
   }
   
   public boolean addPlayerName(String name)
   {
	  if (Shared.isVoid(name) ||
			  name.contains(DelimitedString.DELIMITER) ||
			  name.equals(Shared.ALL_PLAYERS))
	  {
		  return false;
	  }
	  try 
	  {
		    Integer.parseInt(name);
		    return false;
	  } catch (NumberFormatException e) {}
	  for (String s : playerNames)
	  {
		  if (s.equals(name)) return false;
	  }
	  playerNames.add(name);
	  Collections.sort(playerNames);
	  return true;
   }

   public boolean removePlayerName(String name)
   {
	  for (String s : playerNames)
	  {
		  if (s.equals(name))
		  {
			  playerNames.remove(s);
			  return true;
		  }
	  }
	  return false;
   }
   
   public int getState()
   {
      return(state);
   }

   public void setState(int state)
   {
      this.state = state;
   }
   
   public int getTransactionCount()
   {
      return(transactionCount);
   }

   public void setTransactionCount(int count)
   {
      transactionCount = count;
   }

   public int newTransactionNumber()
   {
      transactionCount++;
      return transactionCount - 1;
   }
   
   public void addHostMessage(String message)
   {
      hostMessages.add(message);
   }

   public ArrayList<String> getHostMessages()
   {
      return hostMessages;
   }
   
   public void clearHostMessages()
   {
      hostMessages.clear();
   }           
}
