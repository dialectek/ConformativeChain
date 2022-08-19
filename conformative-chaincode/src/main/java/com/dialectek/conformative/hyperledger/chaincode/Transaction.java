// Transaction.

package com.dialectek.conformative.hyperledger.chaincode;

import java.util.ArrayList;
import java.util.Arrays;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class Transaction
{
   @Property()
   public final int number;
   
   @Property()
   public final String gameCode;

   @Property()
   public String claimantName;

   @Property()
   public double mean;

   @Property()
   public double sigma;

   @Property()
   public double entitlement;

   @Property()
   public double claim;

   @Property()
   public String[] auditorNames;

   @Property()
   public double[] auditorGrants;

   @Property()
   public double claimantGrant;

   @Property()
   public double[] auditorPenalties;

   @Property()
   public double claimantPenalty;

   @Property()
   public String[] beneficiaries;

   @Property()
   public double[] donations;
   
   public Transaction(@JsonProperty("number") final int number, 
		   @JsonProperty("gameCode") final String gameCode)
   {
      this.number      = number;
      this.gameCode    = gameCode;      
      claimantName     = null;
      mean             = sigma = 0.0;
      entitlement      = 0.0;
      claim            = 0.0;
      auditorNames     = new String[0];
      auditorGrants    = new double[0];
      claimantGrant    = 0.0;
      auditorPenalties = new double[0];
      claimantPenalty  = 0.0;
      beneficiaries    = new String[0];
      donations        = new double[0];
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
	  ArrayList<String> results = new ArrayList<String>();
	  for (String name: auditorNames)
	  {
		  results.add(name);
	  }
      return results;
   }

   public void addAuditorName(String auditorName)
   {
	  auditorNames = Arrays.copyOf(auditorNames, auditorNames.length + 1);
	  auditorNames[auditorNames.length - 1] = auditorName;
	  auditorGrants = Arrays.copyOf(auditorGrants, auditorGrants.length + 1);
	  auditorGrants[auditorGrants.length - 1] = 0.0;	  
	  auditorPenalties = Arrays.copyOf(auditorPenalties, auditorPenalties.length + 1);
	  auditorPenalties[auditorPenalties.length - 1] = 0.0;	
   }


   public ArrayList<Double> getAuditorGrants()
   {
	  ArrayList<Double> results = new ArrayList<Double>();
	  for (Double grant: auditorGrants)
	  {
		  results.add(grant);
	  }
      return results;	   
   }


   public void addAuditorGrant(String auditorName, double grant)
   {
      for (int i = 0; i < auditorNames.length; i++)
      {
         if (auditorName.equals(auditorNames[i]))
         {
            auditorGrants[i] = grant;
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
	  ArrayList<Double> results = new ArrayList<Double>();
	  for (Double penalty: auditorPenalties)
	  {
		  results.add(penalty);
	  }
      return results;	   	   
   }


   public void addAuditorPenalty(String auditorName, double penalty)
   {
      for (int i = 0; i < auditorNames.length; i++)
      {
         if (auditorName.equals(auditorNames[i]))
         {
            auditorPenalties[i] = penalty;
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
	  ArrayList<String> results = new ArrayList<String>();
	  for (String name: beneficiaries)
	  {
		  results.add(name);
	  }
      return results;	   
   }


   public ArrayList<Double> getDonations()
   {
	  ArrayList<Double> results = new ArrayList<Double>();
	  for (Double donation: donations)
	  {
		  results.add(donation);
	  }
      return results;	   
   }


   public void addDonation(String beneficiary, double donation)
   {
      beneficiaries = Arrays.copyOf(beneficiaries, beneficiaries.length + 1);
      beneficiaries[beneficiaries.length - 1] = beneficiary;
      donations = Arrays.copyOf(donations, donations.length + 1);
      donations[donations.length - 1] = donation;	      
   }
}
