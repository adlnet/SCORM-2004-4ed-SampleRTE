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
 *
 * <strong>Filename:</strong><br> LogMessage.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <code>LogMessage</code> stores messages classified by the 
 * <code>MessageType</code> class.
 * 
 * @author ADL Technical Team
 */
public class LogMessage
{
   /**
    * This attribute holds the type of message classified by the
    * <code>MessageType</code> class. 
    */
   private final transient int mMessageType;

   /**
    * This attribute holds the actual text to be communicated by the message.
    */
   private final transient String mMessageText;

   /**
    * This holds the name of the test file used.  It is used for links in the
    * Detailed log.  For example "Click here to view detailed MD test log" will
    * now read "Click here to view detailed MD test log for RA_1" or whatever 
    * the IdRef is for that particular
    */
   private final transient String mTestID;

   /**
    * This constructor initializes the message type and message text attributes
    * to the specified values.
    *
    * @param iMessageType - The type of message this is. Typically, this
    * should be "INFORMATION", "WARNING", or "ERROR", but this is up to the
    * client.
    *
    * @param iMessageText - The actual error message text.
    *
    */
   public LogMessage(final int iMessageType, final String iMessageText)
   {
      this(iMessageType, iMessageText, "");      
   }

   /**
    * Creates a message object with a type, message and test ID
    * 
    * @param iMsgType - The type of message
    * @param iMsgTxt - The text of the message
    * @param iTestID - An identifier used to specifiy what is being tested
    */
   public LogMessage(final int iMsgType, final String iMsgTxt, final String iTestID)
   {
      mTestID = iTestID; 
      mMessageType = iMsgType;
      mMessageText = iMsgTxt;
   }

     /**
    * This accessor returns the message type.
    *
    * @return int - The message type classified by the <code>MessageType</code>
    * class.
    */
   public int getMessageType()
   {
      return mMessageType;
   }

   /**
    * This accessor returns the message text.
    *
    * @return String - The message text.
    */
   public String getMessageText()
   {
      return mMessageText;
   }

   /**
    * Returns test id
    * 
    * @return the test id of this message
    */
   public String getTestID()
   {
       return mTestID;
   }

   /**
    * This method returns a representation of this message in a
    * predefined string form.
    *
    * Overloads the toString() method of the java.lang.Object class
    *
    * @return String - The message.
    */
   public String toString()
   {
      final StringBuffer result = new StringBuffer();

      if ( mMessageType == MessageType.INFO )
      {
         result.append("INFO");
      }
      else if ( mMessageType == MessageType.WARNING )
      {
         result.append("WARNING");
      }
      else if ( mMessageType == MessageType.PASSED )
      {
         result.append("PASSED");
      }
      else if ( mMessageType == MessageType.FAILED )
      {
         result.append("FAILED");
      }
      else
      {
         result.append("OTHER");
      }

      result.append(" : ");
      result.append(mMessageText);

      return result.toString();
   }
}
