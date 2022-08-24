// Game.

package com.dialectek.conformative.hyperledger.chaincode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.dialectek.conformative.hyperledger.shared.DelimitedString;
import com.dialectek.conformative.hyperledger.shared.Shared;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class Game
{
   @Property()
   public final String code;

   @Property()
   public final double initialCommonResources;

   @Property()
   public double commonResources;

   @Property()
   public String[] playerNames;

   @Property()
   public int state;

   @Property()
   public int transactionCount;

   @Property()
   public String[] messages;

   public Game(@JsonProperty("code") final String                   code,
               @JsonProperty("initialCommonResources") final double initialCommonResources) throws Exception
   {
      this.code = code;
      if (Shared.isVoid(code) || code.contains(DelimitedString.DELIMITER))
      {
         throw new Exception("Invalid game code");
      }
      this.initialCommonResources = initialCommonResources;
      if (initialCommonResources < 0.0)
      {
         throw new Exception("Invalid initial common resources");
      }
      commonResources  = initialCommonResources;
      playerNames      = new String[0];
      messages         = new String[0];
      state            = Shared.PENDING;
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
      ArrayList<String> results = new ArrayList<String>();
      for (String name : playerNames)
      {
         results.add(name);
      }
      return(results);
   }


   public void clearPlayerNames()
   {
      playerNames = new String[0];
   }


   public boolean addPlayerName(String name)
   {
      if (Shared.isVoid(name) ||
          name.contains(DelimitedString.DELIMITER) ||
          name.equals(Shared.ALL_PLAYERS))
      {
         return(false);
      }
      if (!Pattern.matches("[a-zA-Z0-9]+", name))
      {
         return(false);
      }
      try
      {
         Integer.parseInt(name);
         return(false);
      }
      catch (NumberFormatException e) {}
      for (String s : playerNames)
      {
         if (s.equals(name)) { return(false); }
      }
      int n = playerNames.length;
      String[] tmp = new String[n + 1];
      for (int i = 0; i < n; i++)
      {
         tmp[i] = playerNames[i];
      }
      tmp[n]      = name;
      playerNames = tmp;
      Arrays.sort(playerNames);
      return(true);
   }


   public boolean removePlayerName(String name)
   {
      int n = playerNames.length;
      int i = 0;

      for ( ; i < n; i++)
      {
         if (playerNames[i].equals(name))
         {
            break;
         }
      }
      if (i < n)
      {
         String[] tmp = new String[n - 1];
         i            = 0;
         for (int j = 0; i < n; i++)
         {
            if (!playerNames[i].equals(name))
            {
               tmp[j] = playerNames[i];
               j++;
            }
         }
         playerNames = tmp;
         return(true);
      }
      else
      {
         return(false);
      }
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
      return(transactionCount - 1);
   }


   public void addMessage(String message)
   {
      int n = messages.length;

      String[] tmp = new String[n + 1];
      for (int i = 0; i < n; i++)
      {
         tmp[i] = messages[i];
      }
      tmp[n]   = message;
      messages = tmp;
   }


   public ArrayList<String> getMessages()
   {
      ArrayList<String> results = new ArrayList<String>();
      for (String message : messages)
      {
         results.add(message);
      }
      return(results);
   }


   public void clearMessages()
   {
      messages = new String[0];
   }
}
