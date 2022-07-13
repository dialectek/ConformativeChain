// Player.

package com.dialectek.conformative.hyperledger.chaincode;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class Player
{  
   @Property()
   private final String name;

   @Property()
   private final String gameCode;

   @Property()
   private double personalResources;

   @Property()
   private double entitledResources;

   public Player(@JsonProperty("name") final String name,
           @JsonProperty("gameCode") final String gameCode) 
   {
      this.name         = name;
      this.gameCode     = gameCode;
      personalResources = 0.0;
      entitledResources = 0.0;
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
