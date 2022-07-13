// Game request service.

package com.dialectek.conformative.hyperledger.chaincode;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Property;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import java.util.Collections;

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
   public String requestService(final Context ctx, final String input) 
   {
      ChaincodeStub stub = ctx.getStub();
      input = stub.getStringState(input);	
      if (Shared.isVoid(input))
      {
	      String errorMessage = String.format("invalid input");
	      System.out.println(errorMessage);
	      throw new ChaincodeException(errorMessage); 	  
      }
      String[] args = new DelimitedString(input).parse();
      if (args.length < 2)
      {
	      String errorMessage = String.format("invalid number of arguments");
	      System.out.println(errorMessage);
	      throw new ChaincodeException(errorMessage);         
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
        	      String playerJSON = stub.getStringState(name + DelimitedString.DELIMITER + gameCode);
        	      if (Shared.isVoid(playerJSON)) 
        	      {
        		      String errorMessage = String.format("cannot find player %s", name);
        		      System.out.println(errorMessage);
        		      throw new ChaincodeException(errorMessage);  
        	      } 
        	      Player player = genson.deserialize(playerJSON, Player.class);
        	      players.add(player);
        	}
         }

         if ((request.equals(Shared.JOIN_GAME) ||
              request.equals(Shared.QUIT_GAME) ||
              request.equals(Shared.HOST_CHAT)) &&
             (args.length >= 3))
         {
            if (game == null)
            {
 		      String errorMessage = String.format("game not found");
 		      System.out.println(errorMessage);
 		      throw new ChaincodeException(errorMessage);                
            }
            String      playerName = args[2];
            Player player = null;
            for (int i = 0; i < players.size(); i++)
            {
               player = players.get(i);
               if (player.getName().equals(playerName)) { break; }
               player = null;
            }
            if (request.equals(Shared.JOIN_GAME) && (args.length == 3))
            {
               // Join game?
               if (player == null)
               {
                  if (game.getState() == Shared.JOINING)
                  {
                	 game.addPlayerName(playerName); 
                     String gameJson = genson.serialize(game);
                     stub.putStringState(gameCode, gameJson);
                     player = new Player(playerName, gameCode);
                     String playerJson = genson.serialize(player);
                     stub.putStringState(playerName + DelimitedString.DELIMITER + gameCode, playerJson);                     
                     return(Shared.OK);
                  }
                  else
                  {
                     return(Shared.error("game is not accepting players"));
                  }
               }
               else
               {
                  return(Shared.error("duplicate name"));
               }
            }
            else if (request.equals(Shared.QUIT_GAME) && (args.length == 3))
            {
               // Quit game.
               if (player != null)
               {
                  if (game.getState() != Shared.RUNNING)
                  {
                     game.removePlayerName(playerName);
                     String gameJson = genson.serialize(game);
                     stub.putStringState(gameCode, gameJson);
                     return(Shared.OK);
                  }
                  else
                  {
                     return(Shared.error("cannot quit running game"));
                  }
               }
               return(Shared.OK);
            }
            else if (request.equals(Shared.HOST_CHAT) && (args.length == 4))
            {
               // Relay chat from player to host.
               if (player != null)
               {
                  String clientId = gameCode;
                  channelService.sendMessage(new ChannelMessage(clientId, input));
                  return(Shared.OK);
               }
            }
         }
         else
         {
            if (request.equals(Shared.CREATE_GAME) && (args.length == 3))
            {
               // Create game?
               if (game == null)
               {
                  double resources = 0.0;
                  try {
                     resources = Double.parseDouble(args[2]);
                  }
                  catch (NumberFormatException e) {
                     return(Shared.error("resources must be a non-negative number"));
                  }
                  if (resources < 0.0)
                  {
                     return(Shared.error("resources must be a non-negative number"));
                  }
                  persistentGame = new Game(gameCode, resources);
                  pm.makePersistent(persistentGame);
                  game = new GameProxy(persistentGame);
                  cache.put(gameKey, game);
                  String clientId = gameCode;
                  return(channelService.createChannel(clientId, CHANNEL_TIMEOUT_MINUTES));
               }
               else
               {
                  return(Shared.error("duplicate game code"));
               }
            }
            else if (request.equals(Shared.DELETE_GAME) && (args.length == 2))
            {
               // Delete game.
               if (game != null)
               {
                  gameQuery.deletePersistentAll(gameCode);
                  playersQuery.deletePersistentAll(gameCode);
                  cache.remove(gameKey);
                  cache.remove(playerListKey);
                  for (int i = 0; i < players.size(); i++)
                  {
                     PlayerProxy     player     = players.get(i);
                     String          playerName = player.getName();
                     DelimitedString clientId   = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(playerName);
                     DelimitedString quitRequest = new DelimitedString(Shared.QUIT_GAME);
                     quitRequest.add("Game deleted by host");
                     channelService.sendMessage(
                        new ChannelMessage(clientId.toString(), quitRequest.toString()));
                  }
               }
               return(Shared.OK);
            }
            else if (request.equals(Shared.UPDATE_GAME) && (args.length == 3))
            {
               // Update game.
               if (game != null)
               {
                  int state = Integer.parseInt(args[2]);
                  game.setState(state);
                  if (persistentGame != null)
                  {
                     persistentGame.setState(state);
                  }
                  else
                  {
                     @SuppressWarnings("unchecked")
                     List<Game> games = (List<Game> )gameQuery.execute(gameCode);
                     if (games.size() == 0)
                     {
                        persistentGame = new Game(gameCode, game.getInitialCommonResources());
                        persistentGame.setCommonResources(game.getCommonResources());
                        persistentGame.setState(state);
                     }
                     else if (games.size() == 1)
                     {
                        persistentGame = games.get(0);
                        persistentGame.setState(state);
                     }
                     else
                     {
                        cache.remove(gameKey);
                        return(Shared.error("duplicate game code"));
                     }
                  }
                  pm.makePersistent(persistentGame);
                  cache.put(gameKey, game);
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.REMOVE_PLAYER) && (args.length == 3))
            {
               // Remove player.
               if (game != null)
               {
                  if (game.getState() != Shared.RUNNING)
                  {
                     String                 playerName  = args[2];
                     ArrayList<String>      playerNames = new ArrayList<String>();
                     ArrayList<PlayerProxy> playersTemp = new ArrayList<PlayerProxy>();
                     for (int i = 0; i < players.size(); i++)
                     {
                        PlayerProxy player = players.get(i);
                        if (playerName.equals(Shared.ALL_PLAYERS) ||
                            playerName.equals(player.getName()))
                        {
                           String name = player.getName();
                           playerNames.add(name);
                           playerQuery.deletePersistentAll(gameCode, name);
                        }
                        else
                        {
                           playersTemp.add(player);
                        }
                     }
                     for (int i = 0; i < playerNames.size(); i++)
                     {
                        String          name     = playerNames.get(i);
                        DelimitedString clientId = new DelimitedString();
                        clientId.add(gameCode);
                        clientId.add(name);
                        DelimitedString quitRequest = new DelimitedString(Shared.QUIT_GAME);
                        quitRequest.add("Player removed by host");
                        channelService.sendMessage(
                           new ChannelMessage(clientId.toString(), quitRequest.toString()));
                     }
                     if ((players.size() > playersTemp.size()) && (playersTemp.size() > 0))
                     {
                        double commonResources = game.getCommonResources() / (double)(playersTemp.size());
                        for (int i = 0; i < playersTemp.size(); i++)
                        {
                           PlayerProxy     player   = playersTemp.get(i);
                           DelimitedString clientId = new DelimitedString();
                           clientId.add(gameCode);
                           clientId.add(player.getName());
                           DelimitedString message = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
                           message.add(player.getPersonalResources());
                           message.add(commonResources);
                           message.add(player.getEntitledResources());
                           channelService.sendMessage(
                              new ChannelMessage(clientId.toString(), message.toString()));
                        }
                     }
                     return(Shared.OK);
                  }
                  else
                  {
                     return(Shared.error("cannot remove player from running game"));
                  }
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.GET_PLAYER_RESOURCES) &&
                     (args.length == 3))
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
                        PlayerProxy player = players.get(i);
                        totalPersonalResources += player.getPersonalResources();
                        totalEntitledResources += player.getEntitledResources();
                     }
                     DelimitedString response = new DelimitedString();
                     response.add(totalPersonalResources);
                     response.add(commonResources);
                     response.add(totalEntitledResources);
                     return(response.toString());
                  }
                  else
                  {
                     for (int i = 0; i < players.size(); i++)
                     {
                        PlayerProxy player = players.get(i);
                        if (player.getName().equals(playerName))
                        {
                           DelimitedString response = new DelimitedString();
                           response.add(player.getPersonalResources());
                           response.add(commonResources / players.size());
                           response.add(player.getEntitledResources());
                           return(response.toString());
                        }
                     }
                     return(Shared.error("player not found"));
                  }
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.PLAYER_CHAT) &&
                     ((args.length == 3) || (args.length == 4)))
            {
               // Relay chat from host to player.
               if (game != null)
               {
                  String playerName = null;
                  if (args.length == 4)
                  {
                     playerName = args[2];
                  }
                  for (int i = 0; i < players.size(); i++)
                  {
                     PlayerProxy player = players.get(i);
                     if ((playerName == null) || player.getName().equals(playerName))
                     {
                        DelimitedString clientId = new DelimitedString();
                        clientId.add(gameCode);
                        clientId.add(player.getName());
                        channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
                     }
                  }
               }
               return(Shared.OK);
            }
            else if (request.equals(Shared.PLAYER_ALERT) &&
                     ((args.length == 3) || (args.length == 4)))
            {
               // Relay alert from host to player.
               if (game != null)
               {
                  String playerName = null;
                  if (args.length == 4)
                  {
                     playerName = args[2];
                  }
                  for (int i = 0; i < players.size(); i++)
                  {
                     PlayerProxy player = players.get(i);
                     if ((playerName == null) || player.getName().equals(playerName))
                     {
                        DelimitedString clientId = new DelimitedString();
                        clientId.add(gameCode);
                        clientId.add(player.getName());
                        channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
                     }
                  }
               }
               return(Shared.OK);
            }
            else if (request.equals(Shared.AUDITOR_CHAT) && (args.length == 4))
            {
               // Relay chat from claimant to auditors.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  ArrayList<String> auditorNames = transaction.getAuditors();
                  for (int i = 0; i < auditorNames.size(); i++)
                  {
                     DelimitedString clientId = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(auditorNames.get(i));
                     channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
                  }
               }
               return(Shared.OK);
            }
            else if (request.equals(Shared.CLAIMANT_CHAT) && (args.length == 4))
            {
               // Relay chat from auditor to claimant.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  String          claimantName = transaction.getClaimant();
                  DelimitedString clientId     = new DelimitedString();
                  clientId.add(gameCode);
                  clientId.add(claimantName);
                  channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
               }
               return(Shared.OK);
            }
            else if (request.equals(Shared.START_CLAIM) && (args.length == 7))
            {
               // Start a claim.
               if (game != null)
               {
                  int         number      = Integer.parseInt(args[2]);
                  Transaction transaction = new Transaction(gameCode, number);
                  String      claimant    = args[3];
                  transaction.setClaimant(claimant);
                  double mean = Double.parseDouble(args[4]);
                  transaction.setMean(mean);
                  double sigma = Double.parseDouble(args[5]);
                  transaction.setSigma(sigma);
                  double entitlement = Double.parseDouble(args[6]);
                  transaction.setEntitlement(entitlement);
                  pm.makePersistent(transaction);
                  DelimitedString clientId = new DelimitedString();
                  clientId.add(gameCode);
                  clientId.add(claimant);
                  DelimitedString claimRequest = new DelimitedString(Shared.START_CLAIM);
                  claimRequest.add(number);
                  claimRequest.add(mean);
                  claimRequest.add(sigma);
                  claimRequest.add(entitlement);
                  claimRequest.add(players.size());
                  channelService.sendMessage(
                     new ChannelMessage(clientId.toString(), claimRequest.toString()));
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.SET_CLAIM) && (args.length == 4))
            {
               // Set claim.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  double claim = Double.parseDouble(args[3]);
                  transaction.setClaim(claim);
                  pm.makePersistent(transaction);
                  DelimitedString clientId = new DelimitedString();
                  clientId.add(gameCode);
                  DelimitedString claimRequest = new DelimitedString(Shared.SET_CLAIM);
                  claimRequest.add(number);
                  claimRequest.add(claim);
                  channelService.sendMessage(
                     new ChannelMessage(clientId.toString(), claimRequest.toString()));
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.START_AUDIT) && (args.length >= 3))
            {
               // Start audit.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  double claim = transaction.getClaim();
                  if (args.length == 3)
                  {
                     transaction.setClaimantGrant(claim);
                     String claimant = transaction.getClaimant();
                     pm.makePersistent(transaction);
                     DelimitedString clientId = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(claimant);
                     DelimitedString grantRequest = new DelimitedString(Shared.SET_GRANT);
                     grantRequest.add(number);
                     grantRequest.add(claim);
                     channelService.sendMessage(
                        new ChannelMessage(clientId.toString(), grantRequest.toString()));
                  }
                  else
                  {
                     String claimant = transaction.getClaimant();
                     double mean     = transaction.getMean();
                     double sigma    = transaction.getSigma();
                     for (int i = 3; i < args.length; i++)
                     {
                        DelimitedString clientId = new DelimitedString();
                        clientId.add(gameCode);
                        String auditor = args[i];
                        transaction.addAuditor(auditor);
                        clientId.add(auditor);
                        DelimitedString auditRequest = new DelimitedString(Shared.START_AUDIT);
                        auditRequest.add(number);
                        auditRequest.add(claimant);
                        auditRequest.add(mean);
                        auditRequest.add(sigma);
                        auditRequest.add(claim);
                        auditRequest.add(players.size());
                        channelService.sendMessage(
                           new ChannelMessage(clientId.toString(), auditRequest.toString()));
                     }
                     pm.makePersistent(transaction);
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.SET_GRANT) && (args.length == 5))
            {
               // Set grant.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  double grant       = Double.parseDouble(args[3]);
                  String auditorName = args[4];
                  transaction.addAuditorGrant(auditorName, grant);
                  pm.makePersistent(transaction);
                  DelimitedString clientId = new DelimitedString();
                  clientId.add(gameCode);
                  DelimitedString grantRequest = new DelimitedString(Shared.SET_GRANT);
                  grantRequest.add(number);
                  grantRequest.add(grant);
                  grantRequest.add(auditorName);
                  channelService.sendMessage(
                     new ChannelMessage(clientId.toString(), grantRequest.toString()));
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.SET_GRANT) && (args.length == 4))
            {
               // Set consensus grant.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  double grant = Double.parseDouble(args[3]);
                  transaction.setClaimantGrant(grant);
                  String            claimant = transaction.getClaimant();
                  ArrayList<String> auditors = transaction.getAuditors();
                  pm.makePersistent(transaction);
                  DelimitedString clientId = new DelimitedString();
                  clientId.add(gameCode);
                  clientId.add(claimant);
                  DelimitedString grantRequest = new DelimitedString(Shared.SET_GRANT);
                  grantRequest.add(number);
                  grantRequest.add(grant);
                  channelService.sendMessage(
                     new ChannelMessage(clientId.toString(), grantRequest.toString()));
                  for (int i = 0; i < auditors.size(); i++)
                  {
                     clientId = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(auditors.get(i));
                     grantRequest = new DelimitedString(Shared.SET_GRANT);
                     grantRequest.add(number);
                     grantRequest.add(grant);
                     channelService.sendMessage(
                        new ChannelMessage(clientId.toString(), grantRequest.toString()));
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.SET_PENALTY) && (args.length == 5))
            {
               // Set penalties.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  double auditorPenaltyParameter  = Double.parseDouble(args[3]);
                  double claimantPenaltyParameter = Double.parseDouble(args[4]);
                  String claimant = transaction.getClaimant();
                  double claim    = transaction.getClaim();
                  double grant    = transaction.getClaimantGrant();
                  double penalty  = (claim - grant) * claimantPenaltyParameter;
                  transaction.setClaimantPenalty(penalty);
                  ArrayList<String> auditors         = transaction.getAuditors();
                  ArrayList<Double> auditorGrants    = transaction.getAuditorGrants();
                  ArrayList<Double> auditorPenalties = new ArrayList<Double>();
                  for (int i = 0; i < auditors.size(); i++)
                  {
                     Double auditorPenalty = new Double(Math.abs(grant - auditorGrants.get(i).doubleValue()) * auditorPenaltyParameter);
                     auditorPenalties.add(auditorPenalty);
                     transaction.addAuditorPenalty(auditors.get(i), auditorPenalty.doubleValue());
                  }
                  pm.makePersistent(transaction);
                  DelimitedString clientId = new DelimitedString();
                  clientId.add(gameCode);
                  clientId.add(claimant);
                  DelimitedString penaltyRequest = new DelimitedString(Shared.SET_PENALTY);
                  penaltyRequest.add(number);
                  penaltyRequest.add(penalty);
                  channelService.sendMessage(
                     new ChannelMessage(clientId.toString(), penaltyRequest.toString()));
                  for (int i = 0; i < auditors.size(); i++)
                  {
                     clientId = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(auditors.get(i));
                     penaltyRequest = new DelimitedString(Shared.SET_PENALTY);
                     penaltyRequest.add(number);
                     penaltyRequest.add(auditorPenalties.get(i).doubleValue());
                     channelService.sendMessage(
                        new ChannelMessage(clientId.toString(), penaltyRequest.toString()));
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.SET_DONATION) && (args.length == 5))
            {
               // Donate.
               if (game != null)
               {
                  int    number      = Integer.parseInt(args[2]);
                  double donation    = Double.parseDouble(args[3]);
                  String beneficiary = args[4];
                  int    i           = 0;
                  for ( ; i < players.size(); i++)
                  {
                     if (beneficiary.equals(players.get(i).getName())) { break; }
                  }
                  if (i == players.size())
                  {
                     return(Shared.error("player not found"));
                  }
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  transaction.addDonation(beneficiary, donation);
                  pm.makePersistent(transaction);
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.FINISH_TRANSACTION) && (args.length == 4))
            {
               // Player transaction finish.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }
                  String          playerName = args[3];
                  DelimitedString clientId   = new DelimitedString();
                  clientId.add(gameCode);
                  DelimitedString finishRequest = new DelimitedString(Shared.FINISH_TRANSACTION);
                  finishRequest.add(number);
                  finishRequest.add(playerName);
                  channelService.sendMessage(
                     new ChannelMessage(clientId.toString(), finishRequest.toString()));
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.FINISH_TRANSACTION) && (args.length == 3))
            {
               // Host transaction finish.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  @SuppressWarnings("unchecked")
                  List<Transaction> transactions = (List<Transaction> )transactionQuery.execute(gameCode, number);
                  Transaction transaction        = null;
                  if (transactions.size() == 0)
                  {
                     return(Shared.error("transaction not found"));
                  }
                  else if (transactions.size() == 1)
                  {
                     transaction = transactions.get(0);
                  }
                  else
                  {
                     return(Shared.error("duplicate transactions"));
                  }

                  // Apply transaction to players.
                  String      claimantName = transaction.getClaimant();
                  PlayerProxy claimant     = null;
                  int         i            = 0;
                  for ( ; i < players.size(); i++)
                  {
                     claimant = players.get(i);
                     if (claimantName.equals(claimant.getName())) { break; }
                  }
                  if (i == players.size())
                  {
                     return(Shared.error("claimant not found"));
                  }
                  @SuppressWarnings("unchecked")
                  List<Player> claimants = (List<Player> )playerQuery.execute(gameCode, claimantName);
                  if (claimants.size() == 0)
                  {
                     return(Shared.error("claimant not found"));
                  }
                  else if (claimants.size() > 1)
                  {
                     return(Shared.error("duplicate claimants"));
                  }
                  Player persistentClaimant = claimants.get(0);
                  double entitlement        = claimant.getEntitledResources() + transaction.getEntitlement();
                  claimant.setEntitledResources(entitlement);
                  persistentClaimant.setEntitledResources(entitlement);
                  double amount = claimant.getPersonalResources() - transaction.getEntitlement() +
                                  transaction.getClaimantGrant() - transaction.getClaimantPenalty();
                  claimant.setPersonalResources(amount);
                  persistentClaimant.setPersonalResources(amount);
                  ArrayList<String> auditorNames     = transaction.getAuditors();
                  ArrayList<Double> auditorPenalties = transaction.getAuditorPenalties();
                  for (i = 0; i < auditorNames.size(); i++)
                  {
                     String      auditorName = auditorNames.get(i);
                     PlayerProxy auditor     = null;
                     int         j           = 0;
                     for ( ; j < players.size(); j++)
                     {
                        auditor = players.get(j);
                        if (auditorName.equals(auditor.getName())) { break; }
                     }
                     if (j == players.size())
                     {
                        return(Shared.error("auditor not found"));
                     }
                     @SuppressWarnings("unchecked")
                     List<Player> auditors = (List<Player> )playerQuery.execute(gameCode, auditorName);
                     if (auditors.size() == 0)
                     {
                        return(Shared.error("auditor not found"));
                     }
                     else if (auditors.size() > 1)
                     {
                        return(Shared.error("duplicate auditors"));
                     }
                     Player persistentAuditor = auditors.get(0);
                     amount = auditor.getPersonalResources() - auditorPenalties.get(i).doubleValue();
                     auditor.setPersonalResources(amount);
                     persistentAuditor.setPersonalResources(amount);
                     pm.makePersistent(persistentAuditor);
                  }
                  ArrayList<String> beneficiaryNames = transaction.getBeneficiaries();
                  ArrayList<Double> donations        = transaction.getDonations();
                  double            donationTotal    = 0.0;
                  for (i = 0; i < beneficiaryNames.size(); i++)
                  {
                     String      beneficiaryName = beneficiaryNames.get(i);
                     PlayerProxy beneficiary     = null;
                     int         j = 0;
                     for ( ; j < players.size(); j++)
                     {
                        beneficiary = players.get(j);
                        if (beneficiaryName.equals(beneficiary.getName())) { break; }
                     }
                     if (j == players.size())
                     {
                        return(Shared.error("beneficiary not found"));
                     }
                     @SuppressWarnings("unchecked")
                     List<Player> beneficiaries = (List<Player> )playerQuery.execute(gameCode, beneficiaryName);
                     if (beneficiaries.size() == 0)
                     {
                        return(Shared.error("beneficiary not found"));
                     }
                     else if (beneficiaries.size() > 1)
                     {
                        return(Shared.error("duplicate beneficiaries"));
                     }
                     Player persistentBeneficiary = beneficiaries.get(0);
                     amount         = beneficiary.getPersonalResources() + donations.get(i).doubleValue();
                     donationTotal += donations.get(i).doubleValue();
                     beneficiary.setPersonalResources(amount);
                     persistentBeneficiary.setPersonalResources(amount);
                     pm.makePersistent(persistentBeneficiary);
                  }
                  amount = claimant.getPersonalResources() - donationTotal;
                  claimant.setPersonalResources(amount);
                  persistentClaimant.setPersonalResources(amount);
                  pm.makePersistent(persistentClaimant);
                  cache.put(playerListKey, players);

                  // Apply transaction to game.
                  amount = game.getCommonResources() - transaction.getClaimantGrant();
                  game.setCommonResources(amount);
                  if (persistentGame != null)
                  {
                     persistentGame.setCommonResources(amount);
                  }
                  else
                  {
                     @SuppressWarnings("unchecked")
                     List<Game> games = (List<Game> )gameQuery.execute(gameCode);
                     if (games.size() == 0)
                     {
                        return(Shared.error("game not found"));
                     }
                     else if (games.size() == 1)
                     {
                        persistentGame = games.get(0);
                        persistentGame.setCommonResources(amount);
                     }
                     else
                     {
                        cache.remove(gameKey);
                        return(Shared.error("duplicate game code"));
                     }
                  }
                  pm.makePersistent(persistentGame);
                  cache.put(gameKey, game);

                  // Send notifications to participants.
                  double          commonResources = game.getCommonResources() / (double)(players.size());
                  DelimitedString clientId        = new DelimitedString();
                  clientId.add(gameCode);
                  clientId.add(claimantName);
                  DelimitedString finishRequest = new DelimitedString(Shared.FINISH_TRANSACTION);
                  finishRequest.add(number);
                  finishRequest.add(claimant.getPersonalResources());
                  finishRequest.add(commonResources);
                  finishRequest.add(claimant.getEntitledResources());
                  channelService.sendMessage(
                     new ChannelMessage(clientId.toString(), finishRequest.toString()));
                  for (i = 0; i < auditorNames.size(); i++)
                  {
                     String      auditorName = auditorNames.get(i);
                     PlayerProxy auditor     = null;
                     int         j           = 0;
                     for ( ; j < players.size(); j++)
                     {
                        auditor = players.get(j);
                        if (auditorName.equals(auditor.getName())) { break; }
                     }
                     if (j == players.size())
                     {
                        return(Shared.error("auditor not found"));
                     }
                     clientId = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(auditorName);
                     finishRequest = new DelimitedString(Shared.FINISH_TRANSACTION);
                     finishRequest.add(number);
                     finishRequest.add(auditor.getPersonalResources());
                     finishRequest.add(commonResources);
                     finishRequest.add(auditor.getEntitledResources());
                     channelService.sendMessage(
                        new ChannelMessage(clientId.toString(), finishRequest.toString()));
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.ABORT_TRANSACTION) && (args.length >= 4))
            {
               // Abort transaction.
               if (game != null)
               {
                  int number = Integer.parseInt(args[2]);
                  transactionQuery.deletePersistentAll(gameCode, number);
                  for (int i = 3; i < args.length; i++)
                  {
                     DelimitedString clientId = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(args[i]);
                     DelimitedString abortRequest = new DelimitedString(Shared.ABORT_TRANSACTION);
                     abortRequest.add(number);
                     channelService.sendMessage(
                        new ChannelMessage(clientId.toString(), abortRequest.toString()));
                  }
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
         }
      }
      catch (Exception e)
      {
          String errorMessage = String.format("exception: %s", e.getMessage());
          System.out.println(errorMessage);
          throw new ChaincodeException(errorMessage);
      }
      String errorMessage = String.format("unknown input: %s", input);
      System.out.println(errorMessage);
      throw new ChaincodeException(errorMessage);       
   }
}
