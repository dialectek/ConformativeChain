/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.dialectek.conformative.hyperledger.client;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;

import org.apache.commons.io.IOUtils;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import com.alibaba.dcm.DnsCacheManipulator;
import com.dialectek.conformative.hyperledger.shared.Shared;

public class NetworkClient
{
   static
   {
      System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
   }

   static public final String DEFAULT_BLOCKCHAIN_ADDRESS = "localhost";
   static public String       BLOCKCHAIN_ADDRESS         = DEFAULT_BLOCKCHAIN_ADDRESS;

   // Network and contract.
   static public Gateway  gateway;
   static public Network  network;
   static public Contract contract;

   // Initialize.
   public static boolean init(String blockchainAddress) throws Exception
   {
      BLOCKCHAIN_ADDRESS = blockchainAddress;
      return(init());
   }


   // Initialize.
   public static boolean init() throws Exception
   {
      boolean result = true;

      // Resolve blockchain addresses.
      // https://github.com/alibaba/java-dns-cache-manipulator#-user-guide
      DnsCacheManipulator.setDnsCache("peer0.org1.example.com", BLOCKCHAIN_ADDRESS);
      DnsCacheManipulator.setDnsCache("peer0.org2.example.com", BLOCKCHAIN_ADDRESS);
      DnsCacheManipulator.setDnsCache("orderer.example.com", BLOCKCHAIN_ADDRESS);

      // Get client credentials and createe files.
      try
      {
         HttpURLConnection connection  = (HttpURLConnection) new URL("http://" + BLOCKCHAIN_ADDRESS + ":7059").openConnection();
         InputStream       inputStream = connection.getInputStream();
         String            text        = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                                            .lines()
                                            .collect(Collectors.joining("\n"));
         List<String> lines  = IOUtils.readLines(new StringReader(text));
         PrintWriter  writer = null;
         for (String line : lines)
         {
            if (line.startsWith("<<<"))
            {
               String filename = line.substring(3, line.indexOf(">>>"));
               if (writer != null) { writer.close(); }
               writer = new PrintWriter(filename);
            }
            else
            {
               writer.println(line.replace("localhost:", BLOCKCHAIN_ADDRESS + ":"));
            }
         }
         if (writer != null) { writer.close(); }
         inputStream.close();
      }
      catch (Exception e)
      {
         System.err.println("Cannot get blockchain client credentials: " + e.getMessage());
         System.exit(1);
      }

      // enroll admin
      try {
         enrollAdmin();
      }
      catch (Exception e) {
         System.err.println("Error enrolling admin: " + e.getMessage());
         System.err.println(e);
         result = false;
      }

      // connect to the network and access the smart contract
      try
      {
         // connect
         gateway = connect();

         // get the network and contract
         network  = gateway.getNetwork(Shared.CHANNEL_NAME);
         contract = network.getContract(Shared.CONTRACT_NAME);

         if ((gateway == null) || (network == null) || (contract == null)) { result = false; }
      }
      catch (Exception e) {
         System.err.println("Error connecting to network: " + e.getMessage());
         System.err.println(e);
         result = false;
      }
      return(result);
   }


   public static void terminate()
   {
      if (gateway != null) { gateway.close(); }
   }


   // helper function for getting connected to the gateway
   public static Gateway connect() throws Exception
   {
      // Load a file system based wallet for managing identities.
      Path   walletPath = Paths.get("wallet");
      Wallet wallet     = Wallets.newFileSystemWallet(walletPath);

      // load a CCP
      //Path networkConfigPath = Paths.get("..", "..", "..", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
      Path networkConfigPath = Paths.get("connection-org1.yaml");

      Gateway.Builder builder = Gateway.createBuilder();
      builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);
      builder.discovery(true);
      return(builder.connect());
   }


   // Initialize ledger.
   public static String initLedger() throws Exception
   {
      try
      {
         byte[] response = contract.submitTransaction("InitLedger");
         if (response != null)
         {
            return(new String(response, StandardCharsets.UTF_8));
         }
      }
      catch (Exception e) {
         System.err.println(e);
      }
      return(null);
   }


   // Enroll admin.
   public static void enrollAdmin() throws Exception
   {
      // Create a CA client for interacting with the CA.
      Properties props = new Properties();

      //props.put("pemFile", "../../../organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
      props.put("pemFile", "ca.org1.example.com-cert.pem");
      props.put("allowAllHostNames", "true");
      HFCAClient  caClient    = HFCAClient.createNewInstance("https://" + BLOCKCHAIN_ADDRESS + ":7054", props);
      CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
      caClient.setCryptoSuite(cryptoSuite);

      // Create a wallet for managing identities
      Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

      // Remove possibly expired admin identity.
      if (wallet.get("admin") != null)
      {
         wallet.remove("admin");
      }

      // Enroll the admin user, and import the new identity into the wallet.
      final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
      enrollmentRequestTLS.addHost(BLOCKCHAIN_ADDRESS);
      enrollmentRequestTLS.setProfile("tls");
      Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
      Identity   user       = Identities.newX509Identity("Org1MSP", enrollment);
      wallet.put("admin", user);
   }


   // Register user.
   public static void registerUser(String userName) throws Exception
   {
      // Create a CA client for interacting with the CA.
      Properties props = new Properties();

      //props.put("pemFile", "../../../organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
      props.put("pemFile", "ca.org1.example.com-cert.pem");
      props.put("allowAllHostNames", "true");
      HFCAClient  caClient    = HFCAClient.createNewInstance("https://" + BLOCKCHAIN_ADDRESS + ":7054", props);
      CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
      caClient.setCryptoSuite(cryptoSuite);

      // Create a wallet for managing identities
      Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

      // Remove possibly expired user identity.
      if (wallet.get(userName) != null)
      {
         wallet.remove(userName);
      }

      X509Identity adminIdentity = (X509Identity)wallet.get("admin");
      if (adminIdentity == null)
      {
         System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
         return;
      }

      User admin = new User()
      {
         @Override
         public String getName()
         {
            return("admin");
         }


         @Override
         public Set<String> getRoles()
         {
            return(null);
         }


         @Override
         public String getAccount()
         {
            return(null);
         }


         @Override
         public String getAffiliation()
         {
            return("org1.department1");
         }


         @Override
         public Enrollment getEnrollment()
         {
            return(new Enrollment()
                   {
                      @Override
                      public PrivateKey getKey()
                      {
                         return adminIdentity.getPrivateKey();
                      }


                      @Override
                      public String getCert()
                      {
                         return Identities.toPemString(adminIdentity.getCertificate());
                      }
                   }
                   );
         }


         @Override
         public String getMspId()
         {
            return("Org1MSP");
         }
      };

      // Register the user, enroll the user, and import the new identity into the wallet.
      RegistrationRequest registrationRequest = new RegistrationRequest(userName);
      registrationRequest.setAffiliation("org1.department1");
      registrationRequest.setEnrollmentID(userName);
      String     enrollmentSecret = caClient.register(registrationRequest, admin);
      Enrollment enrollment       = caClient.enroll(userName, enrollmentSecret);
      Identity   user             = Identities.newX509Identity("Org1MSP", enrollment);
      wallet.put(userName, user);
   }


   public static void main(String[] args) throws Exception
   {
      init();
      initLedger();
      registerUser("appUser");
      terminate();
   }
}
