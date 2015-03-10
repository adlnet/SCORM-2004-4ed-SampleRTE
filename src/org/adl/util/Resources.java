package org.adl.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Resources
{
   private static final Integer MAX_LENGTH = 1000;
   private static final Resources resources = Resources.getResources(Resources.class);
   private static Class mClass;
   
   public static <T> Resources getResources(Class<T> forClass)
   {
      mClass = forClass;
      return getResources(getResourceName(forClass));
   }

   public static <T> String getResourceName(Class<T> forClass)
   {
      return forClass.getPackage().getName() + ".resources."
            + forClass.getSimpleName();
   }

   public static Resources getResources(String name)
   {
      return new Resources(ResourceBundle.getBundle(name));
   }

   private ResourceBundle bundle;

   public Resources(ResourceBundle bundle)
   {
      this.bundle = bundle;
   }

   public String getString(String iKey, Object... args)
   {
      try
      {
         String preFormat = bundle.getString(iKey);
         for (int i = 0; i < args.length; i++)
         {
            if ((args[i] instanceof String)
                  && (((String) args[i]).length() >= MAX_LENGTH) && 
                  (mClass != org.adl.validator.util.Messages.class))
            {
               args[i] = resources.getString("truncated");
            }
            else if ( args[i] == null )
            {
               return "";
            }
         }
         String postFormat = String.format(preFormat, args);
         return postFormat;
      }
      catch (MissingResourceException e)
      {
         return iKey;
      }
   }
}
