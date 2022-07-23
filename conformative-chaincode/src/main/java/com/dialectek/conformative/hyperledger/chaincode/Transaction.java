// Transaction.

package com.dialectek.conformative.hyperledger.chaincode;

import java.util.ArrayList;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class Transaction
{
   @Property()
   private final int number;
   
   @Property()
   private final String gameCode;

   @Property()
   private String claimantName;

   @Property()
   private double mean;

   @Property()
   private double sigma;

   @Property()
   private double entitlement;

   @Property()
   private double claim;

   @Property()
   private ArrayList<String> auditorNames;

   @Property()
   private ArrayList<Double> auditorGrants;

   @Property()
   private double claimantGrant;

   @Property()
   private ArrayList<Double> auditorPenalties;

   @Property()
   private double claimantPenalty;

   @Property()
   private ArrayList<String> beneficiaries;

   @Property()
   private ArrayList<Double> donations;
   
   public Transaction(@JsonProperty("number") final int number, 
		   @JsonProperty("gameCode") final String gameCode)
   {
      this.number      = number;
      this.gameCode    = gameCode;      
      claimantName     = null;
      mean             = sigma = 0.0;
      entitlement      = 0.0;
      claim            = 0.0;
      auditorNames     = new ArrayList<String>();
      auditorGrants    = new ArrayList<Double>();
      claimantGrant    = 0.0;
      auditorPenalties = new ArrayList<Double>();
      claimantPenalty  = 0.0;
      beneficiaries    = new ArrayList<String>();
      donations        = new ArrayList<Double>();
   }


   public String getGameCode()
   {
      return(gameCode);
   }


   public int getNumber()
   {
      return(number);
   }


   public String getClaimantName()
   {
      return(claimantName);
   }


   public void setClaimantName(String claimantName)
   {
      this.claimantName = claimantName;
   }


   public double getMean()
   {
      return(mean);
   }


   public void setMean(double mean)
   {
      this.mean = mean;
   }


   public double getSigma()
   {
      return(sigma);
   }


   public void setSigma(double sigma)
   {
      this.sigma = sigma;
   }


   public double getEntitlement()
   {
      return(entitlement);
   }


   public void setEntitlement(double entitlement)
   {
      this.entitlement = entitlement;
   }


   public double getClaim()
   {
      return(claim);
   }


   public void setClaim(double claim)
   {
      this.claim = claim;
   }


   public ArrayList<String> getAuditorNames()
   {
      return(auditorNames);
   }


   public void addAuditorName(String auditorName)
   {
      auditorNames.add(auditorName);
      auditorGrants.add(new Double(0.0));
      auditorPenalties.add(new Double(0.0));
   }


   public ArrayList<Double> getAuditorGrants()
   {
      return(auditorGrants);
   }


   public void addAuditorGrant(String auditorName, double grant)
   {
      for (int i = 0; i < auditorNames.size(); i++)
      {
         if (auditorName.equals(auditorNames.get(i)))
         {
            auditorGrants.set(i, new Double(grant));
            break;
         }
      }
   }


   public double getClaimantGrant()
   {
      return(claimantGrant);
   }


   public void setClaimantGrant(double grant)
   {
      claimantGrant = grant;
   }


   public ArrayList<Double> getAuditorPenalties()
   {
      return(auditorPenalties);
   }


   public void addAuditorPenalty(String auditorName, double penalty)
   {
      for (int i = 0; i < auditorNames.size(); i++)
      {
         if (auditorName.equals(auditorNames.get(i)))
         {
            auditorPenalties.set(i, new Double(penalty));
            break;
         }
      }
   }


   public double getClaimantPenalty()
   {
      return(claimantPenalty);
   }


   public void setClaimantPenalty(double penalty)
   {
      claimantPenalty = penalty;
   }


   public ArrayList<String> getBeneficiaries()
   {
      return(beneficiaries);
   }


   public ArrayList<Double> getDonations()
   {
      return(donations);
   }


   public void addDonation(String beneficiary, double donation)
   {
      beneficiaries.add(beneficiary);
      donations.add(new Double(donation));
   }
}
