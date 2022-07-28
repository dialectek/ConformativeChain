// Shared server and client.

package com.dialectek.conformative.hyperledger.shared;

public class Shared
{
   // Messages.
   // Formats: <operation>/<game code>/<args> | <operation>/<game code>/<player name>/<args>
   public static final String CREATE_GAME          = "create_game";
   public static final String UPDATE_GAME          = "update_game";
   public static final String DELETE_GAME          = "delete_game";
   public static final String JOIN_GAME            = "join_game";
   public static final String QUIT_GAME            = "quit_game";
   public static final String REMOVE_PLAYER        = "remove_player";
   public static final String GET_PLAYER_RESOURCES = "get_player_resources";
   public static final String SET_PLAYER_RESOURCES = "set_player_resources";
   public static final String CHAT_MESSAGE         = "chat_message";
   public static final String MESSAGE_DELIMITER    = "messagez_delimiter";      
   public static final String HOST_GET_MESSAGES    = "host_get_messages";   
   public static final String HOST_CHAT_MESSAGE    = "host_chat_message";
   public static final String HOST_CLEAR_MESSAGES  = "host_clear_messages";
   public static final String PLAYER_GET_MESSAGES  = "player_get_messages";    
   public static final String PLAYER_CHAT_MESSAGE  = "player_chat_message";   
   public static final String PLAYER_CLEAR_MESSAGES= "player_clear_messages"; 
   public static final String START_CLAIM          = "start_claim";
   public static final String START_AUDIT          = "start_audit";
   public static final String SET_CLAIM            = "set_claim";
   public static final String SET_GRANT            = "set_grant";
   public static final String SET_PENALTY          = "set_penalty";    
   public static final String SET_DONATION         = "set_donation";
   public static final String FINISH_TRANSACTION   = "finish_transaction";
   public static final String ABORT_TRANSACTION    = "abort_transaction";
   
   // Game state.
   public static final int PENDING   = 1;
   public static final int JOINING   = 2;
   public static final int RUNNING   = 3;
   public static final int COMPLETED = 4;
  
   // All players symbol.
   public static final String ALL_PLAYERS = "<all>";

   // OK status.
   public static final String OK = "OK";

   // Error prefix.
   public static final String ERROR_PREFIX = "Error: ";


   public static boolean isVoid(String str)
   {
      return(str == null || "".equals(str.trim()));
   }


   public static boolean isOK(String str)
   {
      if (!isVoid(str))
      {
         if (str.startsWith(OK)) { return(true); }
      }
      return(false);
   }


   public static boolean isError(String str)
   {
      if (!isVoid(str))
      {
         if (str.startsWith(ERROR_PREFIX)) { return(true); }
      }
      return(false);
   }


   public static String error(String str)
   {
      return(ERROR_PREFIX + str);
   }
}
