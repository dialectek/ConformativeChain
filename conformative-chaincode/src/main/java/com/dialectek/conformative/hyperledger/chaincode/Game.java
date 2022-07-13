// Game.

package com.dialectek.conformative.hyperledger.chaincode;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
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
   
   public Game(@JsonProperty("code") final String code, 
           @JsonProperty("initialCommonResources") final double initialCommonResources) 
   {
	  this.code = code;
      this.initialCommonResources = initialCommonResources;
      commonResources             = initialCommonResources;
      playerNames = new ArrayList<String>();
      state = Shared.PENDING;
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
	  if (name.contains(DelimitedString.DELIMITER)) return false;
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
}
