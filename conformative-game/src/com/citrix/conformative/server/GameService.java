// Game service request.

package com.citrix.conformative.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.citrix.conformative.shared.Shared;
import com.citrix.conformative.shared.DelimitedString;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import java.util.Collections;


@SuppressWarnings("serial")
public class GameService extends RemoteServiceServlet implements
com.citrix.conformative.client.GameService
{
   // Channel timeout.
   static final int CHANNEL_TIMEOUT_MINUTES = 240;

   public String requestService(String input) throws IllegalArgumentException
   {
      if (Shared.isVoid(input))
      {
         return(Shared.error("invalid input"));
      }
      String[] args = new DelimitedString(input).parse();
      if (args.length < 2)
      {
         return(Shared.error("invalid number of arguments"));
      }
      String request       = args[0];
      String gameCode      = args[1];
      String gameKey       = gameCode + DelimitedString.DELIMITER;
      String playerListKey = gameCode + DelimitedString.DELIMITER + Shared.ALL_PLAYERS;

      // Access storage and cache.
      Cache cache;
      try {
         CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
         cache = cacheFactory.createCache(Collections.emptyMap());
      }
      catch (CacheException e) {
         return(Shared.error("cannot access cache"));
      }
      PersistenceManager pm        = PMF.get().getPersistenceManager();
      Query              gameQuery = pm.newQuery(Game.class );
      gameQuery.setFilter("code == codeParam");
      gameQuery.declareParameters("String codeParam");
      Query playersQuery = pm.newQuery(Player.class );
      playersQuery.setFilter("gameCode == gameCodeParam");
      playersQuery.declareParameters("String gameCodeParam");
      Query playerQuery = pm.newQuery(Player.class );
      playerQuery.setFilter("gameCode == gameCodeParam && name == nameParam");
      playerQuery.declareParameters("String gameCodeParam, String nameParam");
      Query transactionQuery = pm.newQuery(Transaction.class );
      transactionQuery.setFilter("gameCode == gameCodeParam && number == numberParam");
      transactionQuery.declareParameters("String gameCodeParam, int numberParam");

      // Do requested action.
      try
      {
         // Game proxy cached?
         Game      persistentGame = null;
         GameProxy game           = (GameProxy)cache.get(gameKey);
         if (game == null)
         {
            // Get persistent game.
            @SuppressWarnings("unchecked")
            List<Game> games = (List<Game> )gameQuery.execute(gameCode);
            if (games.size() == 1)
            {
               persistentGame = games.get(0);
               game           = new GameProxy(persistentGame);
               cache.put(gameKey, game);
            }
            else if (games.size() > 1)
            {
               return(Shared.error("duplicate game codes"));
            }
         }

         // Get the players?
         ArrayList<PlayerProxy> players = new ArrayList<PlayerProxy>();
         if (game != null)
         {
            if (game.getState() == Shared.RUNNING)
            {
               // List of players cached?
               players = (ArrayList<PlayerProxy> )cache.get(playerListKey);
               if (players == null)
               {
                  // Cache players and list.
                  players = new ArrayList<PlayerProxy>();
                  @SuppressWarnings("unchecked")
                  List<Player> playersWork = (List<Player> )playersQuery.execute(gameCode);
                  for (int i = 0; i < playersWork.size(); i++)
                  {
                     PlayerProxy player = new PlayerProxy(playersWork.get(i));
                     players.add(player);
                  }
                  cache.put(playerListKey, players);
               }
            }
            else
            {
               @SuppressWarnings("unchecked")
               List<Player> playersWork = (List<Player> )playersQuery.execute(gameCode);
               for (int i = 0; i < playersWork.size(); i++)
               {
                  players.add(new PlayerProxy(playersWork.get(i)));
               }
            }
         }
         ChannelService channelService =
            ChannelServiceFactory.getChannelService();

         if ((request.equals(Shared.JOIN_GAME) ||
              request.equals(Shared.QUIT_GAME) ||
              request.equals(Shared.HOST_CHAT)) &&
             (args.length >= 3))
         {
            if (game == null)
            {
               return(Shared.error("game not found"));
            }
            String      playerName = args[2];
            PlayerProxy player     = null;
            for (int i = 0; i < players.size(); i++)
            {
               player = players.get(i);
               if (player.getName().equals(playerName) &&
                   player.getGameCode().equals(gameCode)) { break; }
               player = null;
            }
            if (request.equals(Shared.JOIN_GAME) && (args.length == 3))
            {
               // Join game?
               if (player == null)
               {
                  if (game.getState() == Shared.JOINING)
                  {
                     Player persistentPlayer = new Player(playerName, gameCode);
                     pm.makePersistent(persistentPlayer);
                     DelimitedString clientId = new DelimitedString(gameCode);
                     channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
                     double commonResources = game.getCommonResources() / (double)(players.size() + 1);
                     for (int i = 0; i < players.size(); i++)
                     {
                        player   = players.get(i);
                        clientId = new DelimitedString();
                        clientId.add(gameCode);
                        clientId.add(player.getName());
                        DelimitedString message = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
                        message.add(player.getPersonalResources());
                        message.add(commonResources);
                        message.add(player.getEntitledResources());
                        channelService.sendMessage(
                           new ChannelMessage(clientId.toString(), message.toString()));
                     }
                     clientId = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(playerName);
                     DelimitedString response =
                        new DelimitedString(
                           channelService.createChannel(clientId.toString(), CHANNEL_TIMEOUT_MINUTES));
                     response.add("0");
                     response.add(commonResources);
                     response.add("0");
                     return(response.toString());
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
                     playerQuery.deletePersistentAll(gameCode, playerName);
                     DelimitedString clientId = new DelimitedString(gameCode);
                     channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
                     players.remove(player);
                     if (players.size() > 0)
                     {
                        double commonResources = game.getCommonResources() / (double)(players.size());
                        for (int i = 0; i < players.size(); i++)
                        {
                           player   = players.get(i);
                           clientId = new DelimitedString();
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
      finally
      {
         pm.close();
      }
      return(Shared.error("unknown input: " + input));
   }
}
