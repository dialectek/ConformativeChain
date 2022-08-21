// Game request service.

package com.dialectek.conformative.hyperledger.chaincode;

import java.util.ArrayList;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;
import com.dialectek.conformative.hyperledger.shared.DelimitedString;
import com.dialectek.conformative.hyperledger.shared.Shared;
import com.owlike.genson.Genson;

@Contract(
name = "conformative-chaincode",
info = @Info(
        title = "Conformative game",
        description = "Conformative game smart contract on Hyperledger Fabric blockchain",
        version = "0.0.1-SNAPSHOT",
        license = @License(
                name = "Apache 2.0 License",
                url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
        contact = @Contact(
                email = "portegys@gmail.com",
                name = "Tom Portegys",
                url = "http://tom.portegys.com/research.html#conformative")))

@Default
public final class GameService implements ContractInterface 
{
   private final Genson genson = new Genson();
   
   @Transaction(intent = Transaction.TYPE.SUBMIT)
   public String InitLedger(final Context ctx) 
   {
	   return(Shared.OK);
   }
   
   @Transaction(intent = Transaction.TYPE.SUBMIT)
   public String requestService(final Context ctx, final String input) 
   {
      ChaincodeStub stub = ctx.getStub();	
      if (Shared.isVoid(input))
      {
	      return Shared.error("empty input");
      }
      String[] args = new DelimitedString(input).parse();
      if (args.length < 2)
      {
	      return Shared.error("invalid number of arguments: " + args.length);       
      }
      String request       = args[0];
      String gameCode      = args[1];

      // Fetch game.
      String gameJSON = stub.getStringState(gameCode);
      Game game = null;
      if (!Shared.isVoid(gameJSON)) 
      {
    	  game = genson.deserialize(gameJSON, Game.class);
      }
      
      // Service request.
      try
      {
         // Get the players?
         ArrayList<Player> players = new ArrayList<Player>();
         if (game != null)
         {
        	ArrayList<String> playerNames = game.getPlayerNames();
        	for (String name : playerNames) 
        	{
        	      String playerJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + name);
        	      if (Shared.isVoid(playerJSON)) 
        	      {
        		      return Shared.error("cannot find player: " + name); 
        	      } 
        	      Player player = genson.deserialize(playerJSON, Player.class);
        	      players.add(player);
        	}
         }
         
         if ((request.equals(Shared.SYNC_PLAYER) ||
        	  request.equals(Shared.JOIN_GAME) || 
              request.equals(Shared.QUIT_GAME) ||
              request.equals(Shared.PLAYER_SYNC_MESSAGES) ||
              request.equals(Shared.PLAYER_CHAT_MESSAGE) ||             
              request.equals(Shared.PLAYER_CLEAR_MESSAGES)) &&     		 
             (args.length >= 3))
         {
	        String      playerName = args[2];        	 
            Player player = null;        	 
            if (game != null)
            {
	            for (int i = 0; i < players.size(); i++)
	            {
	               player = players.get(i);
	               if (player.getName().equals(playerName)) { break; }
	               player = null;
	            }
            }
            if (request.equals(Shared.SYNC_PLAYER) && (args.length == 3 || args.length == 4))
            {
            	// Synchronize player.
            	if (game != null)
            	{
	        		if (player != null)
	        		{
	                    DelimitedString response = new DelimitedString(Shared.OK);
	                    response.add(game.getState());
	                    response.add(1);
	                    response.add(player.getPersonalResources());
	                    response.add(game.getCommonResources() / (double)players.size());
	                    response.add(player.getEntitledResources());                     
	                    if (args.length == 4)
	                    {
	                          int number;
		               		  try 
		               		  {
		               			  number = Integer.parseInt(args[3]);
		               		  } catch (NumberFormatException e) {
		                             return(Shared.error("invalid transaction number: " + args[3])); 
		               		  }                  
		               	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
		               	      if (Shared.isVoid(transactionJSON)) 
		               	      {
		               		      return Shared.error("transaction number not found: " + number); 
		               	      } 
		               	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
		               	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
		               	      response.add(transaction.getClaimantName());
		               	      response.add(transaction.getMean());
		               	      response.add(transaction.getSigma());	
		               	      response.add(transaction.getEntitlement());
		               	      response.add(transaction.getClaim());	 
		               	      response.add(transaction.getClaimantName());
		               	      ArrayList<String> auditorNames = transaction.getAuditorNames();
		               	      response.add(auditorNames.size());
		               	      for (String name : auditorNames)
		               	      {
		               	    	  response.add(name);
		               	      }
		               	      ArrayList<Double> auditorGrants = transaction.getAuditorGrants();
		               	      response.add(auditorGrants.size());
		               	      for (Double grant : auditorGrants)
		               	      {
		               	    	  response.add(grant);
		               	      }
		               	      response.add(transaction.getClaimantGrant());
		               	      ArrayList<Double> auditorPenalties = transaction.getAuditorPenalties();
		               	      response.add(auditorPenalties.size());
		               	      for (Double penalty : auditorPenalties)
		               	      {
		               	    	  response.add(penalty);
		               	      }
		               	      response.add(transaction.getClaimantPenalty());
		               	      ArrayList<String> beneficiaries = transaction.getBeneficiaries();
		               	      response.add(beneficiaries.size());
		               	      for (String name : beneficiaries)
		               	      {
		               	    	  response.add(name);
		               	      }
		               	      ArrayList<Double> donations = transaction.getDonations();
		               	      response.add(donations.size());
		               	      for (Double donation : donations)
		               	      {
		               	    	  response.add(donation);
		               	      }	               	      
	                    }
	                    return(response.toString());
	        		} else {
	                    DelimitedString response = new DelimitedString(Shared.OK);
	                    response.add(game.getState());
	                    response.add(0);
	                    return(response.toString());
	        		}
            	} else {
                    DelimitedString response = new DelimitedString(Shared.OK);
                    response.add(0);
                    return(response.toString());            		
            	}
            }
            else if (request.equals(Shared.PLAYER_SYNC_MESSAGES) && (args.length == 3))
            {
               // Get status and messages.
               if (game == null)
               {
                	 DelimitedString response = new DelimitedString(Shared.OK);
                  	 response.add(0);
                  	 response.add(0);
                     return(response.toString());            	   
               } else {
	               if (player != null)
	               {
	            	 DelimitedString response = new DelimitedString(Shared.OK);
	            	 response.add(game.getState());
	            	 response.add(1);
	            	 ArrayList<String> messages = player.getMessages();
	            	 if (messages.size() > 0)
	            	 {
		            	 for (String message : messages)
		            	 {
		            		 response.add(message);
		            		 response.add(Shared.MESSAGE_DELIMITER);
		            	 } 
		            	 player.clearMessages();
		                 String playerJson = genson.serialize(player);
		                 stub.putStringState(gameCode + DelimitedString.DELIMITER + playerName, playerJson);
	            	 }
	                 return(response.toString());
	               } else {
	              	 DelimitedString response = new DelimitedString(Shared.OK);
	              	 response.add(game.getState());
	              	 response.add(0);
	                 return(response.toString());            	   
	               }
               }
            }
            else if (game == null)
            {
                return(Shared.error("game not found: " + gameCode));            	
            }
            else if (request.equals(Shared.JOIN_GAME) && (args.length == 3))
            {
               // Join game?
               if (player == null)
               {
                  if (game.getState() == Shared.JOINING)
                  {
                	 if (game.addPlayerName(playerName))
                	 {
                		 DelimitedString message = new DelimitedString(Shared.JOIN_GAME);
                		 message.add(playerName);
                		 game.addMessage(message.toString());
	                     String gameJson = genson.serialize(game);
	                     stub.putStringState(gameCode, gameJson);
	                     players.add(new Player(playerName, gameCode));
	                     double          commonResources = game.getCommonResources() / (double)(players.size());
	                     for (Player p : players)
	                     {
	                        DelimitedString resourceMessage = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
	                        resourceMessage.add(p.getPersonalResources());
	                        resourceMessage.add(commonResources);
	                        resourceMessage.add(p.getEntitledResources());                	  
	                        String      name = p.getName();
	                        p.addMessage(resourceMessage.toString());
	                        String playerJson = genson.serialize(p);
	                        stub.putStringState(gameCode + DelimitedString.DELIMITER + name, playerJson);
	                     }          
	                     return(Shared.OK);
                	 } else {
                         return(Shared.error("invalid player name: " + playerName));               		 
                	 }
                  }
                  else
                  {
                     return(Shared.error("game is not accepting players: " + gameCode));
                  }
               }
               else
               {
                  return(Shared.error("duplicate player name: " + playerName));
               }
            }
            else if (request.equals(Shared.QUIT_GAME) && (args.length == 3))
            {
               // Quit game.
               if (player != null)
               {
                  if (game.getState() == Shared.JOINING)
                  {
                     game.removePlayerName(playerName);
            		 DelimitedString message = new DelimitedString(Shared.QUIT_GAME);
            		 message.add(playerName);
            		 game.addMessage(message.toString());                     
                     String gameJson = genson.serialize(game);
                     stub.putStringState(gameCode, gameJson);
                     stub.delState(gameCode + DelimitedString.DELIMITER + playerName);
                     double          commonResources = game.getCommonResources() / (double)(players.size() - 1);
                     for (Player p : players)
                     {
                    	if (p == player) continue;
                        DelimitedString resourceMessage = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
                        resourceMessage.add(p.getPersonalResources());
                        resourceMessage.add(commonResources);
                        resourceMessage.add(p.getEntitledResources());                	  
                        String      name = p.getName();
                        p.addMessage(resourceMessage.toString());
                        String playerJson = genson.serialize(p);
                        stub.putStringState(gameCode + DelimitedString.DELIMITER + name, playerJson);
                     }                             
                     return(Shared.OK);
                  }
                  else
                  {
                     return(Shared.error("cannot quit game after running: " + gameCode));
                  }
               } else {
            	   return(Shared.OK);
               }
            }
            else if (request.equals(Shared.PLAYER_CHAT_MESSAGE) && (args.length == 4))
            {
               // Relay message from player to host.
               if (player != null)
               {
            	  DelimitedString message = new DelimitedString(Shared.CHAT_MESSAGE);
            	  message.add(args[2]);
            	  message.add(args[3]);
            	  game.addMessage(message.toString());
                  String gameJson = genson.serialize(game);
                  stub.putStringState(gameCode, gameJson);
                  return(Shared.OK);
               } else {
                   return(Shared.error("player not found: " + playerName));              	   
               }
            }
            else if (request.equals(Shared.PLAYER_CLEAR_MESSAGES) && (args.length == 3))
            {
               // Clear player messages.
               if (player != null)
               {
            	 player.clearMessages();
                 String playerJson = genson.serialize(player);
                 stub.putStringState(gameCode + DelimitedString.DELIMITER + playerName, playerJson);            	 
                 return(Shared.OK); 
               } else {
                   return(Shared.error("player not found: " + playerName));             	   
               }
            }   
         }
         else
         {
            if (request.equals(Shared.SYNC_GAME) && (args.length == 2 || args.length == 3))
            {
             	 // Synchronize game.
                 if (game != null)
                 {
                     DelimitedString response = new DelimitedString(Shared.OK);
                     response.add(game.getState());
                     response.add(game.getInitialCommonResources());
                     double totalPersonalResources = 0.0;
                     double totalEntitledResources = 0.0;
                     for (int i = 0; i < players.size(); i++)
                     {
                        Player player = players.get(i);
                        totalPersonalResources += player.getPersonalResources();
                        totalEntitledResources += player.getEntitledResources();
                     } 
                     response.add(totalPersonalResources);
                     response.add(game.getCommonResources());                     
                     response.add(totalEntitledResources);                     
                     ArrayList<String> playerNames = game.getPlayerNames();
                     response.add(playerNames.size());
                     for (String name : playerNames)
                     {
                     	response.add(name);
                     }                                                      
                     if (args.length == 3)
                     {
                          int number;
 	               		  try 
 	               		  {
 	               			  number = Integer.parseInt(args[2]);
 	               		  } catch (NumberFormatException e) {
 	                             return(Shared.error("invalid transaction number: " + args[2])); 
 	               		  }                  
 	               	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
 	               	      if (Shared.isVoid(transactionJSON)) 
 	               	      {
 	               		      return Shared.error("transaction number not found: " + number); 
 	               	      } 
 	               	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
 	               	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
 	               	      response.add(transaction.getClaimantName());
 	               	      response.add(transaction.getMean());
 	               	      response.add(transaction.getSigma());	
 	               	      response.add(transaction.getEntitlement());
 	               	      response.add(transaction.getClaim());	 
 	               	      response.add(transaction.getClaimantName());
 	               	      ArrayList<String> auditorNames = transaction.getAuditorNames();
 	               	      response.add(auditorNames.size());
 	               	      for (String name : auditorNames)
 	               	      {
 	               	    	  response.add(name);
 	               	      }
 	               	      ArrayList<Double> auditorGrants = transaction.getAuditorGrants();
 	               	      response.add(auditorGrants.size());
 	               	      for (Double grant : auditorGrants)
 	               	      {
 	               	    	  response.add(grant);
 	               	      }
 	               	      response.add(transaction.getClaimantGrant());
 	               	      ArrayList<Double> auditorPenalties = transaction.getAuditorPenalties();
 	               	      response.add(auditorPenalties.size());
 	               	      for (Double penalty : auditorPenalties)
 	               	      {
 	               	    	  response.add(penalty);
 	               	      }
 	               	      response.add(transaction.getClaimantPenalty());
 	               	      ArrayList<String> beneficiaries = transaction.getBeneficiaries();
 	               	      response.add(beneficiaries.size());
 	               	      for (String name : beneficiaries)
 	               	      {
 	               	    	  response.add(name);
 	               	      }
 	               	      ArrayList<Double> donations = transaction.getDonations();
 	               	      response.add(donations.size());
 	               	      for (Double donation : donations)
 	               	      {
 	               	    	  response.add(donation);
 	               	      }	               	      
                     }
                     return(response.toString());               
                 }
                 else
                 {
                     DelimitedString response = new DelimitedString(Shared.OK);
                     response.add(0);
                 	 return(response.toString());
                 } 
            }
            else if (request.equals(Shared.CREATE_GAME) && (args.length == 3))
            {
               // Create game?
               if (game == null)
               {
                  double resources = 0.0;
                  try {
                     resources = Double.parseDouble(args[2]);
                  }
                  catch (NumberFormatException e) {
                     return(Shared.error("invalid resources: " + args[2]));
                  }
                  if (resources < 0.0)
                  {
                     return(Shared.error("resources must be a non-negative number: " + resources));
                  }
                  game = new Game(gameCode, resources);
                  String gameJson = genson.serialize(game);
                  stub.putStringState(gameCode, gameJson);            	 
                  return(Shared.OK); 
               }
               else
               {
                  return(Shared.error("game code exists: " + gameCode));
               }
            }
            else if (request.equals(Shared.DELETE_GAME) && (args.length == 2))
            {
               // Delete game.
               if (game != null)
               {
                  for (int i = 0, j = game.getTransactionCount(); i < j; i++)
                  {
                	 stub.delState(gameCode + DelimitedString.DELIMITER + i); 
                  }            	   
               	  ArrayList<String> playerNames = game.getPlayerNames();
                  for (String name : playerNames)
                  {
                	 stub.delState(gameCode + DelimitedString.DELIMITER + name); 
                  }
                  stub.delState(gameCode);                   
                  return(Shared.OK);                  
               } else {
                   return(Shared.error("game code not found: " + gameCode));            	   
               }
            }
            else if (request.equals(Shared.UPDATE_GAME) && (args.length == 3))
            {
               // Update game.
               if (game != null)
               {
                  int state;
        		  try 
        		  {
        			  state = Integer.parseInt(args[2]);
        		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid game state: " + args[2])); 
        		  }
                  game.setState(state);
                  String gameJson = genson.serialize(game);
                  stub.putStringState(gameCode, gameJson);            	 
                  return(Shared.OK); 
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.REMOVE_PLAYER) && (args.length == 3))
            {
               // Remove player.
               if (game != null)
               {
                  if (game.getState() == Shared.JOINING)
                  {
                     String                 playerName  = args[2];
                     ArrayList<String> playerNames = new ArrayList<String>();               
                     for (Player player : players)
                     {
                    	String name = player.getName();
                        if (playerName.equals(Shared.ALL_PLAYERS) ||
                            playerName.equals(name))
                        {
                           stub.delState(gameCode + DelimitedString.DELIMITER + name);
                        }
                        else
                        {
                           playerNames.add(name);
                        }
                     }
                     game.clearPlayerNames();
                     for (String name : playerNames)
                     {
                    	game.addPlayerName(name);
                     }
                     String gameJson = genson.serialize(game);
                     stub.putStringState(gameCode, gameJson);
                     playerNames = game.getPlayerNames();
                     double          commonResources = game.getCommonResources() / (double)(playerNames.size());                     
                	 for (String name : playerNames) 
                	 {
                	      String playerJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + name);
                	      if (Shared.isVoid(playerJSON)) 
                	      {
                		      return Shared.error("cannot find player: " + name); 
                	      } 
                	      Player player = genson.deserialize(playerJSON, Player.class);
                          DelimitedString resourceMessage = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
                          resourceMessage.add(player.getPersonalResources());
                          resourceMessage.add(commonResources);
                          resourceMessage.add(player.getEntitledResources());                	  
                          player.addMessage(resourceMessage.toString());
                          String playerJson = genson.serialize(player);
                          stub.putStringState(gameCode + DelimitedString.DELIMITER + name, playerJson);
                	 }                            
                     return(Shared.OK);
                  }
                  else
                  {
                     return(Shared.error("cannot remove player from game after running: " + gameCode));
                  }
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.GET_PLAYER_RESOURCES) && (args.length == 3))
            {
               // Get player resources.
               if (game != null)
               {
                  double commonResources = game.getCommonResources();
                  String playerName      = args[2];
                  if (playerName.equals(Shared.ALL_PLAYERS))
                  {
                     double totalPersonalResources = 0.0;
                     double totalEntitledResources = 0.0;
                     for (int i = 0; i < players.size(); i++)
                     {
                        Player player = players.get(i);
                        totalPersonalResources += player.getPersonalResources();
                        totalEntitledResources += player.getEntitledResources();
                     }
                     DelimitedString response = new DelimitedString(Shared.OK);
                     response.add(totalPersonalResources);
                     response.add(commonResources);
                     response.add(totalEntitledResources);
                     return(response.toString());
                  }
                  else
                  {
                     for (int i = 0; i < players.size(); i++)
                     {
                        Player player = players.get(i);
                        if (player.getName().equals(playerName))
                        {
                           DelimitedString response = new DelimitedString(Shared.OK);
                           response.add(player.getPersonalResources());
                           response.add(commonResources / (double)players.size());
                           response.add(player.getEntitledResources());
                           return(response.toString());
                        }
                     }
                     return(Shared.error("player not found: " + playerName));
                  }
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.HOST_SYNC_MESSAGES) && (args.length == 2))
            { 
            	if (game != null)
            	{
                  	 DelimitedString response = new DelimitedString(Shared.OK);
                  	 response.add(game.getState());
                	 ArrayList<String> messages = game.getMessages();
                	 if (messages.size() > 0)
                	 {
	                	 for (String message : messages)
	                	 {
	                		 response.add(message);
	                		 response.add(Shared.MESSAGE_DELIMITER);
	                	 }
	                	 game.clearMessages();
	                     String gameJson = genson.serialize(game);
	                     stub.putStringState(gameCode, gameJson);
                	 }
                	 return response.toString();
            	} else {
                 	 DelimitedString response = new DelimitedString(Shared.OK);
                 	 response.add(0);
                	 return response.toString();                 	 
            	}
            }
            else if (request.equals(Shared.HOST_CHAT_MESSAGE) && (args.length == 3 || args.length == 4))
            {
               // Relay message from host to player(s).
               if (game != null)
               {
                  String playerName = null;
                  String message = args[2];
                  if (args.length == 4)
                  {
                     playerName = args[2];
                     message = args[3];
                  }
                  DelimitedString playerMessage = new DelimitedString(Shared.CHAT_MESSAGE);
                  playerMessage.add(message);
                  for (int i = 0; i < players.size(); i++)
                  {
                     Player player = players.get(i);
                     if ((playerName == null) || player.getName().equals(playerName))
                     {
                    	player.addMessage(playerMessage.toString());
                        String playerJson = genson.serialize(player);
                        stub.putStringState(gameCode + DelimitedString.DELIMITER + player.getName(), playerJson);  
                     }
                  }
                  return Shared.OK;
               } else {
            	   return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.HOST_CLEAR_MESSAGES) && (args.length == 2))
            { 
            	if (game != null)
            	{
            		game.clearMessages();
                    String gameJson = genson.serialize(game);
                    stub.putStringState(gameCode, gameJson);  
                    return(Shared.OK);
            	} else {
             	   return(Shared.error("game code not found: " + gameCode));            		
            	}                                  	       
            }
            else if (request.equals(Shared.CLAIMANT_CHAT_MESSAGE) && (args.length == 4))
            {
               // Relay chat from claimant to auditors.
                if (game != null)
                {
                   String chatText = args[3];
                   int number;
           		   try 
           		   {
           			  number = Integer.parseInt(args[2]);
           		   } catch (NumberFormatException e) {
                         return(Shared.error("invalid transaction number: " + args[2])); 
           		   }                  
           	       String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
           	       if (Shared.isVoid(transactionJSON)) 
           	       {
           		      return Shared.error("cannot find transaction: " + number); 
           	       } 
           	       com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
           	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
                   String claimantName = transaction.getClaimantName();
                   Player claimant = null;
	              for (int i = 0; i < players.size(); i++)
	              {
	                   claimant = players.get(i);
	                   if (claimant.getName().equals(claimantName)) { break; }
	                   claimant = null;
	              }            	
	              if (claimant == null)
	              {
	                   return(Shared.error("claimant not found: " + claimantName));            	   
	              }                         
                  ArrayList<String> auditorNames = transaction.getAuditorNames();                  
                  for (int i = 0; i < auditorNames.size(); i++)
                  {
                	  String auditorName = auditorNames.get(i);
                      Player auditor = null;
    	              for (int j = 0; j < players.size(); j++)
    	              {
    	                   auditor = players.get(j);
    	                   if (auditor.getName().equals(auditorName)) { break; }
    	                   auditor = null;
    	              }            	
    	              if (auditor == null)
    	              {
    	                return(Shared.error("auditor not found: " + auditorName));            	   
    	              }                                         	  
                     DelimitedString message = new DelimitedString(Shared.CLAIMANT_CHAT_MESSAGE);
                     message.add(number);
                     message.add(chatText);
                     auditor.addMessage(message.toString());
                     String auditorJson = genson.serialize(auditor);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + auditorName, auditorJson); 
                   }
                   return(Shared.OK);
                }
                else
                {
                   return(Shared.error("game code not found: " + gameCode));
                }
            }
            else if (request.equals(Shared.AUDITOR_CHAT_MESSAGE) && (args.length == 4))
            {
               // Relay chat from auditor to claimant.
                if (game != null)
                {
                   String chatText = args[3];
                   int number;
           		   try 
           		   {
           			  number = Integer.parseInt(args[2]);
           		   } catch (NumberFormatException e) {
                         return(Shared.error("invalid transaction number: " + args[2])); 
           		   }                  
           	       String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
           	       if (Shared.isVoid(transactionJSON)) 
           	       {
           		      return Shared.error("cannot find transaction: " + number); 
           	       } 
           	       com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
           	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
                   String claimantName = transaction.getClaimantName();
                   Player claimant = null;
	              for (int i = 0; i < players.size(); i++)
	              {
	                   claimant = players.get(i);
	                   if (claimant.getName().equals(claimantName)) { break; }
	                   claimant = null;
	              }            	
	              if (claimant == null)
	              {
	                   return(Shared.error("claimant not found: " + claimantName));            	   
	              }
                  DelimitedString message = new DelimitedString(Shared.AUDITOR_CHAT_MESSAGE);
                  message.add(number);
                  message.add(chatText);
                  claimant.addMessage(message.toString());
                  String claimantJson = genson.serialize(claimant);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + claimantName, claimantJson);	              
                   return(Shared.OK);
                }
                else
                {
                   return(Shared.error("game code not found: " + gameCode));
                }
            }                     
            else if (request.equals(Shared.START_CLAIM) && (args.length == 6))
            {
               // Start a claim.
               if (game != null)
               {
         	      int number = game.newTransactionNumber();
                  String gameJson = genson.serialize(game);
                  stub.putStringState(gameCode, gameJson);          	      
        	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
        	    		  new com.dialectek.conformative.hyperledger.chaincode.Transaction(number, gameCode);
                  String      claimantName    = args[2];
                  Player claimant = null;
	              for (int i = 0; i < players.size(); i++)
	              {
	                   claimant = players.get(i);
	                   if (claimant.getName().equals(claimantName)) { break; }
	                   claimant = null;
	              }            	
	              if (claimant == null)
	              {
	                   return(Shared.error("claimant not found: " + claimantName));            	   
	              }                  
                  transaction.setClaimantName(claimantName);
                  double mean, sigma, entitlement;
                  try
                  {
	                  mean = Double.parseDouble(args[3]);
        		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid claim mean: " + args[3])); 
        		  }              
	              transaction.setMean(mean);
                  try
                  {
	                  sigma = Double.parseDouble(args[4]);
        		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid claim sigma: " + args[4])); 
        		  }              
	              transaction.setSigma(sigma);
	              try
	              {
	                  entitlement = Double.parseDouble(args[5]);

         		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid claim entitlement: " + args[5])); 
        		  } 
                  transaction.setEntitlement(entitlement);	              
                  String transactionJson = genson.serialize(transaction);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + number, transactionJson);                  			  
                  DelimitedString claimantMessage = new DelimitedString(Shared.START_CLAIM);
                  claimantMessage.add(number);
                  claimantMessage.add(mean);
                  claimantMessage.add(sigma);
                  claimantMessage.add(entitlement);
                  claimantMessage.add(players.size());
                  claimant.addMessage(claimantMessage.toString());
                  String claimantJson = genson.serialize(claimant);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + claimantName, claimantJson); 
                  DelimitedString response = new DelimitedString(Shared.OK);
                  response.add(number);
                  return(response.toString());
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.SET_CLAIM) && (args.length == 4))
            {
               // Set claim.
               if (game != null)
               {
                  int number;
         		  try 
         		  {
         			  number = Integer.parseInt(args[2]);
         		  } catch (NumberFormatException e) {
                       return(Shared.error("invalid transaction number: " + args[2])); 
         		  }                  
         	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
         	      if (Shared.isVoid(transactionJSON)) 
         	      {
         		      return Shared.error("transaction number not found: " + number); 
         	      } 
         	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
         	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
                  double claim;
                  try
                  {
	                  claim = Double.parseDouble(args[3]);
         		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid claim=" + args[3])); 
        		  }
                  transaction.setClaim(claim);   			                    
                  String claimantName = transaction.getClaimantName();
                  Player claimant = null;
	              for (int i = 0; i < players.size(); i++)
	              {
	                   claimant = players.get(i);
	                   if (claimant.getName().equals(claimantName)) { break; }
	                   claimant = null;
	              }            	
	              if (claimant == null)
	              {
	                   return(Shared.error("claimant not found: " + claimantName));            	   
	              }
                  String transactionJson = genson.serialize(transaction);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + number, transactionJson);               	              
                  DelimitedString claimantMessage = new DelimitedString(Shared.SET_CLAIM);
                  claimantMessage.add(number);
                  claimantMessage.add(claim);
                  game.addMessage(claimantMessage.toString());
                  String gameJson = genson.serialize(game);
                  stub.putStringState(gameCode, gameJson);                     
                  return Shared.OK;         
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.START_AUDIT) && (args.length >= 3))
            {
               // Start audit.
               if (game != null)
               {
                  int number;
          		  try 
          		  {
          			  number = Integer.parseInt(args[2]);
          		  } catch (NumberFormatException e) {
                        return(Shared.error("invalid transaction number: " + args[2])); 
          		  }                  
          	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
          	      if (Shared.isVoid(transactionJSON)) 
          	      {
          		      return Shared.error("cannot find transaction: " + number); 
          	      } 
          	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
          	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
                  double claim = transaction.getClaim();
                  String claimantName = transaction.getClaimantName();
                  Player claimant = null;
   	              for (int i = 0; i < players.size(); i++)
   	              {
   	                   claimant = players.get(i);
   	                   if (claimant.getName().equals(claimantName)) { break; }
   	                   claimant = null;
   	              }            	
   	              if (claimant == null)
   	              {
   	                   return(Shared.error("claimant not found: " + claimantName));            	   
   	              }                         
                  if (args.length == 3)
                  {
                     transaction.setClaimantGrant(claim);
                     String transactionJson = genson.serialize(transaction);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + number, transactionJson);                            
                     DelimitedString claimantMessage = new DelimitedString(Shared.SET_GRANT);                     
                     claimantMessage.add(number);
                     claimantMessage.add(claim);
                     claimant.addMessage(claimantMessage.toString());
                     String claimantJson = genson.serialize(claimant);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + claimantName, claimantJson);                                
                  }
                  else
                  {
                     double mean     = transaction.getMean();
                     double sigma    = transaction.getSigma();
                     for (int i = 3; i < args.length; i++)
                     {
                        String auditorName = args[i];
                        Player auditor = null;
	      	            for (int j = 0; j < players.size(); j++)
	      	            {
      	                   auditor = players.get(j);
      	                   if (auditor.getName().equals(auditorName)) { break; }
      	                   auditor = null;
	      	            }            	
	      	            if (auditor == null)
	      	            {
	      	                return(Shared.error("auditor not found: " + auditorName));            	   
	      	            }
                        transaction.addAuditorName(auditorName);
                        DelimitedString auditorMessage = new DelimitedString(Shared.START_AUDIT);
                        auditorMessage.add(number);
                        auditorMessage.add(claimantName);
                        auditorMessage.add(mean);
                        auditorMessage.add(sigma);
                        auditorMessage.add(claim);
                        auditorMessage.add(players.size());
                        auditor.addMessage(auditorMessage.toString());
                        String auditorJson = genson.serialize(auditor);
                        stub.putStringState(gameCode + DelimitedString.DELIMITER + auditorName, auditorJson); 
                     }
                     String transactionJson = genson.serialize(transaction);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + number, transactionJson); 
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.SET_GRANT) && (args.length == 5))
            {
               // Set grant.
               if (game != null)
               {
                  int number;
           		  try 
           		  {
           			  number = Integer.parseInt(args[2]);
           		  } catch (NumberFormatException e) {
                         return(Shared.error("invalid transaction number: " + args[2])); 
           		  }                  
           	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
           	      if (Shared.isVoid(transactionJSON)) 
           	      {
           		      return Shared.error("cannot find transaction: " + number); 
           	      } 
           	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
           	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
                  double grant       = 0.0;
	              try
	              {
	                  grant = Double.parseDouble(args[3]);
         		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid grant: " + args[3])); 
        		  }                   
                  String auditorName = args[4];
                  Player auditor = null;
	              for (int i = 0; i < players.size(); i++)
	              {
	                   auditor = players.get(i);
	                   if (auditor.getName().equals(auditorName)) { break; }
	                   auditor = null;
	              }            	
	              if (auditor == null)
	              {
	                return(Shared.error("auditor not found: " + auditorName));            	   
	              }                          
                  transaction.addAuditorGrant(auditorName, grant);
                  String transactionJson = genson.serialize(transaction);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + number, transactionJson); 
                  DelimitedString grantMessage = new DelimitedString(Shared.SET_GRANT);
                  grantMessage.add(number);
                  grantMessage.add(grant);
                  grantMessage.add(auditorName);
                  game.addMessage(grantMessage.toString());
                  String gameJson = genson.serialize(game);
                  stub.putStringState(gameCode, gameJson); 
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.SET_GRANT) && (args.length == 4))
            {
               // Set consensus grant.
               if (game != null)
               {
                  int number;
           		  try 
           		  {
           			  number = Integer.parseInt(args[2]);
           		  } catch (NumberFormatException e) {
                         return(Shared.error("invalid transaction number: " + args[2])); 
           		  }                  
           	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
           	      if (Shared.isVoid(transactionJSON)) 
           	      {
           		      return Shared.error("cannot find transaction: " + number); 
           	      } 
           	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
           	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
                  double grant       = 0.0;
	              try
	              {
	                  grant = Double.parseDouble(args[3]);
         		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid grant: " + args[3])); 
        		  }      
                  transaction.setClaimantGrant(grant);
                  String transactionJson = genson.serialize(transaction);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + number, transactionJson);                  
                  String            claimantName = transaction.getClaimantName();
                  Player claimant = null;
   	              for (int i = 0; i < players.size(); i++)
   	              {
   	                   claimant = players.get(i);
   	                   if (claimant.getName().equals(claimantName)) { break; }
   	                   claimant = null;
   	              }            	
   	              if (claimant == null)
   	              {
   	                   return(Shared.error("claimant not found: " + claimantName));            	   
   	              }                 
                  DelimitedString grantMessage = new DelimitedString(Shared.SET_GRANT);
                  grantMessage.add(number);
                  grantMessage.add(grant);
                  claimant.addMessage(grantMessage.toString());
                  String claimantJson = genson.serialize(claimant);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + claimantName, claimantJson); 
                  ArrayList<String> auditorNames = transaction.getAuditorNames();                  
                  for (int i = 0; i < auditorNames.size(); i++)
                  {
                	  String auditorName = auditorNames.get(i);
                      Player auditor = null;
    	              for (int j = 0; j < players.size(); j++)
    	              {
    	                   auditor = players.get(j);
    	                   if (auditor.getName().equals(auditorName)) { break; }
    	                   auditor = null;
    	              }            	
    	              if (auditor == null)
    	              {
    	                return(Shared.error("auditor not found: " + auditorName));            	   
    	              }                                         	  
                     grantMessage = new DelimitedString(Shared.SET_GRANT);
                     grantMessage.add(number);
                     grantMessage.add(grant);
                     auditor.addMessage(grantMessage.toString());
                     String auditorJson = genson.serialize(auditor);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + auditorName, auditorJson); 
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.SET_PENALTY) && (args.length == 5))
            {
               // Set penalties.
               if (game != null)
               {
                  int number;
        		  try 
        		  {
        			  number = Integer.parseInt(args[2]);
        		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid transaction number: " + args[2])); 
        		  }                  
        	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
        	      if (Shared.isVoid(transactionJSON)) 
        	      {
        		      return Shared.error("cannot find transaction: " + number); 
        	      } 
        	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
        	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
                  double auditorPenaltyParameter       = 0.0;
	              try
	              {
	            	  auditorPenaltyParameter = Double.parseDouble(args[3]);
         		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid auditor penalty parameter: " + args[3])); 
        		  }             	      
                  double claimantPenaltyParameter       = 0.0;
	              try
	              {
	            	  claimantPenaltyParameter = Double.parseDouble(args[4]);
         		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid claimant penalty parameter: " + args[4])); 
        		  }                       
                  String claimantName = transaction.getClaimantName();
                  double claim    = transaction.getClaim();
                  double grant    = transaction.getClaimantGrant();
                  double penalty  = (claim - grant) * claimantPenaltyParameter;
                  transaction.setClaimantPenalty(penalty);
                  ArrayList<String> auditorNames         = transaction.getAuditorNames();
                  ArrayList<Double> auditorGrants    = transaction.getAuditorGrants();
                  ArrayList<Double> auditorPenalties = new ArrayList<Double>();
                  for (int i = 0; i < auditorNames.size(); i++)
                  {
                     Double auditorPenalty = new Double(Math.abs(grant - auditorGrants.get(i).doubleValue()) * auditorPenaltyParameter);
                     auditorPenalties.add(auditorPenalty);
                     transaction.addAuditorPenalty(auditorNames.get(i), auditorPenalty.doubleValue());
                  }
                  String transactionJson = genson.serialize(transaction);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + number, transactionJson);                  
                  DelimitedString penaltyMessage = new DelimitedString(Shared.SET_PENALTY);
                  penaltyMessage.add(number);
                  penaltyMessage.add(penalty);
                  Player claimant = null;
   	              for (int i = 0; i < players.size(); i++)
   	              {
   	                   claimant = players.get(i);
   	                   if (claimant.getName().equals(claimantName)) { break; }
   	                   claimant = null;
   	              }            	
   	              if (claimant == null)
   	              {
   	                   return(Shared.error("claimant not found: " + claimantName));            	   
   	              } 
   	              claimant.addMessage(penaltyMessage.toString());
                  String claimantJson = genson.serialize(claimant);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + claimantName, claimantJson);
                  for (int i = 0; i < auditorNames.size(); i++)
                  {
                	  String auditorName = auditorNames.get(i);
                      Player auditor = null;
    	              for (int j = 0; j < players.size(); j++)
    	              {
    	                   auditor = players.get(j);
    	                   if (auditor.getName().equals(auditorName)) { break; }
    	                   auditor = null;
    	              }            	
    	              if (auditor == null)
    	              {
    	                return(Shared.error("auditor not found: " + auditorName));            	   
    	              }                                	  
                     penaltyMessage = new DelimitedString(Shared.SET_PENALTY);
                     penaltyMessage.add(number);
                     penaltyMessage.add(auditorPenalties.get(i).doubleValue());
                     auditor.addMessage(penaltyMessage.toString());
                     String auditorJson = genson.serialize(auditor);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + auditorName, auditorJson); 
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.SET_DONATION) && (args.length == 5))
            {
               // Donate.
               if (game != null)
               {
                  int number;
        		  try 
        		  {
        			  number = Integer.parseInt(args[2]);
        		  } catch (NumberFormatException e) {
                      return(Shared.error("invalid transaction number: " + args[2])); 
        		  }                  
        	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
        	      if (Shared.isVoid(transactionJSON)) 
        	      {
        		      return Shared.error("cannot find transaction: " + number); 
        	      } 
        	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
        	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);
                  double donation       = 0.0;
 	              try
 	              {
 	            	  donation = Double.parseDouble(args[3]);
          		  } catch (NumberFormatException e) {
                       return(Shared.error("invalid donation: " + args[3])); 
         		  }                      
                  String beneficiaryName = args[4];
                  int    i           = 0;
                  for ( ; i < players.size(); i++)
                  {
                     if (beneficiaryName.equals(players.get(i).getName())) { break; }
                  }
                  if (i == players.size())
                  {
                     return(Shared.error("beneficiary not found: " + beneficiaryName));
                  }
                  transaction.addDonation(beneficiaryName, donation);
                  String transactionJson = genson.serialize(transaction);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + number, transactionJson);                  
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.FINISH_TRANSACTION) && (args.length == 4))
            {
               // Player transaction finish.
               if (game != null)
               {
                  int number;
         		  try 
         		  {
         			  number = Integer.parseInt(args[2]);
         		  } catch (NumberFormatException e) {
                       return(Shared.error("invalid transaction number: " + args[2])); 
         		  }                  
                  String          playerName = args[3];
                  Player player = null;
	              for (int i = 0; i < players.size(); i++)
	              {
	                   player = players.get(i);
	                   if (player.getName().equals(playerName)) { break; }
	                   player = null;
	              }            	
	              if (player == null)
	              {
	                return(Shared.error("player not found: " + playerName));            	   
	              }                                   
                  DelimitedString finishMessage = new DelimitedString(Shared.FINISH_TRANSACTION);
                  finishMessage.add(number);
                  finishMessage.add(playerName);
                  game.addMessage(finishMessage.toString());
                  String gameJson = genson.serialize(game);
                  stub.putStringState(gameCode, gameJson); 
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.FINISH_TRANSACTION) && (args.length == 3))
            {
               // Host transaction finish.
               if (game != null)
               {
                  int number;
          		  try 
          		  {
          			  number = Integer.parseInt(args[2]);
          		  } catch (NumberFormatException e) {
                        return(Shared.error("invalid transaction number: " + args[2])); 
          		  }                  
          	      String transactionJSON = stub.getStringState(gameCode + DelimitedString.DELIMITER + number);
          	      if (Shared.isVoid(transactionJSON)) 
          	      {
          		      return Shared.error("cannot find transaction: " + number); 
          	      } 
          	      com.dialectek.conformative.hyperledger.chaincode.Transaction transaction = 
          	    		  genson.deserialize(transactionJSON, com.dialectek.conformative.hyperledger.chaincode.Transaction.class);

                  // Apply transaction to players.
                  String      claimantName = transaction.getClaimantName();
                  Player claimant = null;
                  int         i            = 0;
                  for ( ; i < players.size(); i++)
                  {
                     claimant = players.get(i);
                     if (claimantName.equals(claimant.getName())) { break; }
                  }
                  if (i == players.size())
                  {
                     return(Shared.error("claimant not found: " + claimantName));
                  }
                  double entitlement        = claimant.getEntitledResources() + transaction.getEntitlement();
                  claimant.setEntitledResources(entitlement);
                  double amount = claimant.getPersonalResources() - transaction.getEntitlement() +
                                  transaction.getClaimantGrant() - transaction.getClaimantPenalty();
                  claimant.setPersonalResources(amount); 
                  ArrayList<String> auditorNames     = transaction.getAuditorNames();
                  ArrayList<Double> auditorPenalties = transaction.getAuditorPenalties();
                  for (i = 0; i < auditorNames.size(); i++)
                  {
                     String      auditorName = auditorNames.get(i);
                     Player auditor = null;
                     int         j           = 0;
                     for ( ; j < players.size(); j++)
                     {
                        auditor = players.get(j);
                        if (auditorName.equals(auditor.getName())) { break; }
                     }
                     if (j == players.size())
                     {
                        return(Shared.error("auditor not found: " + auditorName));
                     }
                     amount = auditor.getPersonalResources() - auditorPenalties.get(i).doubleValue();
                     auditor.setPersonalResources(amount);
                     String auditorJson = genson.serialize(auditor);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + auditorName, auditorJson); 
                  }
                  ArrayList<String> beneficiaryNames = transaction.getBeneficiaries();
                  ArrayList<Double> donations        = transaction.getDonations();
                  double            donationTotal    = 0.0;
                  for (i = 0; i < beneficiaryNames.size(); i++)
                  {
                     String      beneficiaryName = beneficiaryNames.get(i);
                     Player beneficiary = null;
                     int         j = 0;
                     for ( ; j < players.size(); j++)
                     {
                        beneficiary = players.get(j);
                        if (beneficiaryName.equals(beneficiary.getName())) { break; }
                     }
                     if (j == players.size())
                     {
                        return(Shared.error("beneficiary not found: " + beneficiaryName));
                     }
                     amount         = beneficiary.getPersonalResources() + donations.get(i).doubleValue();
                     donationTotal += donations.get(i).doubleValue();
                     beneficiary.setPersonalResources(amount);
                     String beneficiaryJson = genson.serialize(beneficiary);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + beneficiaryName, beneficiaryJson);
                  }
                  amount = claimant.getPersonalResources() - donationTotal;
                  claimant.setPersonalResources(amount);
 
                  // Apply transaction to game.
                  amount = game.getCommonResources() - transaction.getClaimantGrant();
                  game.setCommonResources(amount);
                  String gameJson = genson.serialize(game);
                  stub.putStringState(gameCode, gameJson);                  

                  // Notify participants of transaction finish.
                  DelimitedString finishMessage = new DelimitedString(Shared.FINISH_TRANSACTION);
                  finishMessage.add(number);
                  claimant.addMessage(finishMessage.toString());
                  String claimantJson = genson.serialize(claimant);
                  stub.putStringState(gameCode + DelimitedString.DELIMITER + claimantName, claimantJson);
                  for (i = 0; i < auditorNames.size(); i++)
                  {
                     String      auditorName = auditorNames.get(i);
                     Player auditor = null;
                     int         j           = 0;
                     for ( ; j < players.size(); j++)
                     {
                        auditor = players.get(j);
                        if (auditorName.equals(auditor.getName())) { break; }
                     }
                     if (j == players.size())
                     {
                        return(Shared.error("auditor not found: " + auditorName));
                     }
                     finishMessage = new DelimitedString(Shared.FINISH_TRANSACTION);
                     finishMessage.add(number);
                     auditor.addMessage(finishMessage.toString());
                     String auditorJson = genson.serialize(auditor);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + auditorName, auditorJson);
                  }
                  
                  // Notify update of player resources.
                  double          commonResources = game.getCommonResources() / (double)(players.size());
                  for (Player player : players)
                  {
                     DelimitedString resourceMessage = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
                     resourceMessage.add(player.getPersonalResources());
                     resourceMessage.add(commonResources);
                     resourceMessage.add(player.getEntitledResources());                	  
                     String      playerName = player.getName();
                     player.addMessage(resourceMessage.toString());
                     String playerJson = genson.serialize(player);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + playerName, playerJson);
                  }                  
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
            else if (request.equals(Shared.ABORT_TRANSACTION) && (args.length >= 4))
            {
               // Abort transaction.
               if (game != null)
               {
                  int number;
           		  try 
           		  {
           			  number = Integer.parseInt(args[2]);
           		  } catch (NumberFormatException e) {
                         return(Shared.error("invalid transaction number: " + args[2])); 
           		  }
           		  stub.delState(gameCode + DelimitedString.DELIMITER + number);
                  for (int i = 3; i < args.length; i++)
                  {
                     String playerName = args[i];
                     Player player = null;
                     int         j           = 0;
                     for ( ; j < players.size(); j++)
                     {
                        player = players.get(j);
                        if (playerName.equals(player.getName())) { break; }
                     }
                     if (j == players.size())
                     {
                        return(Shared.error("auditor not found: " + playerName));
                     }                     
                     DelimitedString abortMessage = new DelimitedString(Shared.ABORT_TRANSACTION);
                     abortMessage.add(number);
                     player.addMessage(abortMessage.toString());
                     String playerJson = genson.serialize(player);
                     stub.putStringState(gameCode + DelimitedString.DELIMITER + playerName, playerJson);
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game code not found: " + gameCode));
               }
            }
         }
      }
      catch (Exception e)
      {
          return Shared.error("exception: " + e.getMessage());
      }
      return Shared.error("unknown input: " + input);      
   }
}
