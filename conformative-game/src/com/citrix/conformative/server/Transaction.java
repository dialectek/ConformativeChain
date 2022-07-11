// Conformative server transaction.

package com.citrix.conformative.server;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Transaction
{
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   Key key;

   @Persistent
   private String gameCode;

   @Persistent
   private int number;

   @Persistent
   private String claimant;

   @Persistent
   private double mean;

   @Persistent
   private double sigma;

   @Persistent
   private double entitlement;

   @Persistent
   private double claim;

   @Persistent
   private ArrayList<String> auditors;

   @Persistent
   private ArrayList<Double> auditorGrants;

   @Persistent
   private double claimantGrant;

   @Persistent
   private ArrayList<Double> auditorPenalties;

   @Persistent
   private double claimantPenalty;

   @Persistent
   private ArrayList<String> beneficiaries;

   @Persistent
   private ArrayList<Double> donations;

   public Transaction(String gameCode, int number)
   {
      this.gameCode    = gameCode;
      this.number      = number;
      claimant         = null;
      mean             = sigma = 0.0;
      entitlement      = 0.0;
      claim            = 0.0;
      auditors         = new ArrayList<String>();
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


   public String getClaimant()
   {
      return(claimant);
   }


   public void setClaimant(String claimant)
   {
      this.claimant = claimant;
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


   public ArrayList<String> getAuditors()
   {
      return(auditors);
   }


   public void addAuditor(String auditor)
   {
      auditors.add(auditor);
      auditorGrants.add(new Double(0.0));
      auditorPenalties.add(new Double(0.0));
   }


   public ArrayList<Double> getAuditorGrants()
   {
      return(auditorGrants);
   }


   public void addAuditorGrant(String auditor, double grant)
   {
      for (int i = 0; i < auditors.size(); i++)
      {
         if (auditor.equals(auditors.get(i)))
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


   public void addAuditorPenalty(String auditor, double penalty)
   {
      for (int i = 0; i < auditors.size(); i++)
      {
         if (auditor.equals(auditors.get(i)))
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
