// Conformative server game proxy.

package com.citrix.conformative.server;

import java.io.Serializable;


public class GameProxy implements Serializable
{
   private String code;
   private double initialCommonResources;
   private double commonResources;

   // State.
   private int state;

   // Construct proxy from persistent game.
   public GameProxy(Game persistentGame)
   {
      code = persistentGame.getCode();
      initialCommonResources = persistentGame.getInitialCommonResources();
      commonResources        = persistentGame.getCommonResources();
      state = persistentGame.getState();
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


   public int getState()
   {
      return(state);
   }


   public void setState(int state)
   {
      this.state = state;
   }
}
