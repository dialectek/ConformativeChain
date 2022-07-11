package com.citrix.conformative.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

@SuppressWarnings("serial")
public class ChannelDisconnectedServlet extends HttpServlet
{
   public void doPost(HttpServletRequest req, HttpServletResponse resp)
   throws IOException, ServletException
   {
      ChannelService  channelService = ChannelServiceFactory.getChannelService();
      ChannelPresence presence       = channelService.parsePresence(req);

      String[] args = presence.clientId().split("/");
      if (args.length != 2) { return; }
      for (int i = 0; i < args.length; i++)
      {
         args[i] = args[i].trim();
      }
      String             gameCode   = args[0];
      String             playerName = args[1];
      PersistenceManager pm         = PMF.get().getPersistenceManager();

      try
      {
         @SuppressWarnings("unchecked")
         List<Player> players = (List<Player> )pm.newQuery(Player.class ).execute();
         Player player        = null;
         for (int i = 0; i < players.size(); i++)
         {
            player = players.get(i);
            if (player.getName().equals(playerName) &&
                player.getGameCode().equals(gameCode))
            {
               pm.deletePersistent(player);
               break;
            }
         }
      }
      finally
      {
         pm.close();
      }
   }
}
