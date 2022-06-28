// Delimited string.

package com.dialectek.conformative.shared;

public class DelimitedString
{
   public static final String DELIMITER = "/";

   private String str;

   public DelimitedString()
   {
      str = "";
   }


   public DelimitedString(String str)
   {
      if (Shared.isVoid(str))
      {
         this.str = "";
      }
      else
      {
         this.str = new String(str);
      }
   }


   public String toString()
   {
      return(str);
   }


   public String[] parse()
   {
      String[] args = str.split(DELIMITER);
      for (int i = 0; i < args.length; i++)
      {
         args[i] = args[i].trim();
      }
      return(args);
   }


   public String add(String arg)
   {
      if (!Shared.isVoid(arg))
      {
         if (Shared.isVoid(str))
         {
            str = arg;
         }
         else
         {
            str = str + DELIMITER + arg;
         }
      }
      return(str);
   }


   public String add(int arg)
   {
      if (Shared.isVoid(str))
      {
         str = arg + "";
      }
      else
      {
         str = str + DELIMITER + arg;
      }
      return(str);
   }


   public String add(double arg)
   {
      if (Shared.isVoid(str))
      {
         str = arg + "";
      }
      else
      {
         str = str + DELIMITER + arg;
      }
      return(str);
   }
}
