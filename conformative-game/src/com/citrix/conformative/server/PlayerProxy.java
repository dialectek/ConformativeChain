// Player proxy.

package com.citrix.conformative.server;

import java.io.Serializable;

public class PlayerProxy implements Serializable
{
   private String name;
   private String gameCode;
   private double personalResources;
   private double entitledResources;

   // Construct proxy from persistent player.
   public PlayerProxy(Player persistentPlayer)
   {
      name              = persistentPlayer.getName();
      gameCode          = persistentPlayer.getGameCode();
      personalResources = persistentPlayer.getPersonalResources();
      entitledResources = persistentPlayer.getEntitledResources();
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
}
