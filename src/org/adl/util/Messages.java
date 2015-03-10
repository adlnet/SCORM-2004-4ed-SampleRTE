/******************************************************************************

ADL SCORM 2004 4th Edition Sample Run-Time Environment

The ADL SCORM 2004 4th Ed. Sample Run-Time Environment is licensed under
Creative Commons Attribution-Noncommercial-Share Alike 3.0 United States.

The Advanced Distributed Learning Initiative allows you to:
  *  Share - to copy, distribute and transmit the work.
  *  Remix - to adapt the work. 

Under the following conditions:
  *  Attribution. You must attribute the work in the manner specified by the author or
     licensor (but not in any way that suggests that they endorse you or your use
     of the work).
  *  Noncommercial. You may not use this work for commercial purposes. 
  *  Share Alike. If you alter, transform, or build upon this work, you may distribute
     the resulting work only under the same or similar license to this one. 

For any reuse or distribution, you must make clear to others the license terms of this work. 

Any of the above conditions can be waived if you get permission from the ADL Initiative. 
Nothing in this license impairs or restricts the author's moral rights.

******************************************************************************/

package org.adl.util;


/**
 * <strong>Filename: </strong>Messages.java<br><br>
 *
 * <strong>Description: </strong> <br>
 * The <code>Messages</code> creates a resource bundle that contains all of the
 * messages for the logging of the ADL Test Suite.    <br>
 *
 * @author ADL Technical Team<br><br>
 */
public class Messages
{
   /**
    * Default constructor.  No explicitly defined functionality for this 
    * constructor.
    */
   private Messages()
   {
      // No explicitly defined functionality
   }

   /**
    * Returns the CTS logging message associated with the given key. The method 
    * retrieves a parameterized message based on the input key and replaces the 
    * parameter with the input parameter. If the key is not found a default 
    * message based on the key will be returned.
    * 
    * @param iKey A unique identifier that identifies a message.
    * @return The message associated with the key.
    */
   public static String getString( String iKey, Object... args )
   {
      return Resources.getResources(Messages.class).getString(iKey, args);
   }
   
   /**
    * Utility to convert potential null values to empty strings.
    * 
    * @param iString String that if null should be converted to empty string.
    * 
    * @return The string or empty string if string was null.
    */
   public static String unNull(String iString)
   {
      return (iString == null) ? "" : iString;
   }
}
