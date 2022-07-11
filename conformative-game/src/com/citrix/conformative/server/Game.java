// Conformative server game.

package com.citrix.conformative.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;
import com.citrix.conformative.shared.Shared;
import com.google.appengine.api.datastore.Key;


@PersistenceCapable
public class Game
{
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   Key key;

   @Persistent
   private String code;

   @Persistent
   private double initialCommonResources;

   @Persistent
   private double commonResources;

   @Persistent
   private int state;

   public Game(String code, double initialCommonResources)
   {
      this.code = code;
      this.initialCommonResources = initialCommonResources;
      commonResources             = initialCommonResources;
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


   public int getState()
   {
      return(state);
   }


   public void setState(int state)
   {
      this.state = state;
   }
}
