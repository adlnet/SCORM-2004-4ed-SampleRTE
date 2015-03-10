/*******************************************************************************

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
package org.adl.validator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This method holds the information representing the results collected when a 
 * package checker is ran.
 * 
 * @author ADL Technical Team
 *
 */
public class Result
{
   
   /**
    * String that holds the name of the package checker that the Result represents.
    */
   private String mPackageCheckerName;
   
   /**
    * Boolean that indicates if the package checker resulted in a pass or fail.
    */
   private boolean mIsPackageCheckerPassed = true;
   
   /**
    * Boolean indicating if the package checker was skipped.
    */
   private boolean mIsCheckerSkipped = false;
   
   /**
    * Boolean indicating if the validating should be stopped.
    */
   private boolean mIsTestStopped = false;
   
   /**
    * List that holds the messages that describes the overall status of a checker.
    */
   private List<ValidatorMessage> mOverallStatusMessage = new ArrayList<ValidatorMessage>();
   
   /**
    * List that holds the output messages for the package checker.
    */
   private List<ValidatorMessage> mPackageCheckerMessages = new ArrayList<ValidatorMessage>();
   
   /**
    * Constructor
    */
   public Result()
   {
      // default constructor
   }

   /**
    * Provides a way to see if the package checker was skipped.
    * 
    * @return Returns the mIsCheckerSkipped.
    */
   public boolean isCheckerSkipped()
   {
      return mIsCheckerSkipped;
   }

   /**
    * Provides a way to find if the package checker passed or failed.
    * 
    * @return Returns the mIsPackageCheckerStatus.
    */
   public boolean isPackageCheckerPassed()
   {
      return mIsPackageCheckerPassed;
   }

   /**
    * Provides a way to seed if the package validation should stop after the 
    * package checker has run.
    * 
    * @return Returns the mIsStopTest.
    */
   public boolean isTestStopped()
   {
      return mIsTestStopped;
   }

   /**
    * Provides a way to get the report messages set by the package checker.
    * 
    * @return Returns the mPackageCheckerMessages.
    */
   public List<ValidatorMessage> getPackageCheckerMessages()
   {
      return mPackageCheckerMessages;
   }

   /**
    * Provides a way to get the name of the package checker.]
    * 
    * @return Returns the mPackageCheckerName.
    */
   public String getPackageCheckerName()
   {
      return mPackageCheckerName;
   }
   
   /**
    * Provides a way to get the message that describes the overall status of a 
    * package checker.
    * 
    * @return Returns the mOverallStatusMessage.
    */
   public List<ValidatorMessage> getOverallStatusMessage()
   {
      return mOverallStatusMessage;
   }

   /**
    * Sets the status that indicates that the checker was skippped.
    * 
    * @param iIsCheckerSkipped The mIsCheckerSkipped to set.
    */
   public void setCheckerSkipped(boolean iIsCheckerSkipped)
   {
      mIsCheckerSkipped = iIsCheckerSkipped;
   }

   /**
    * Sets that status that indicates if the checker passed or failed.
    * 
    * @param iIsPackageCheckerPassed The mIsPackageCheckerPassed to set.
    */
   public void setPackageCheckerPassed(boolean iIsPackageCheckerPassed)
   {
      mIsPackageCheckerPassed = iIsPackageCheckerPassed;
   }

   /**
    * Sets that status that indicates if the validator should stop after the 
    * checker has ran.
    * 
    * @param iIsTestStopped The mIsTestStopped to set.
    */
   public void setTestStopped(boolean iIsTestStopped)
   {
      mIsTestStopped = iIsTestStopped;
   }

   /**
    * Sets the list that holds the checker report messages.
    * 
    * @param iPackageCheckerMessage The mPackageCheckerMessage to be added to 
    * the packageCheckerMessages List.
    */
   public void addPackageCheckerMessage(ValidatorMessage iPackageCheckerMessage)
   {      
      if ( iPackageCheckerMessage.getMessageText().indexOf("[") != -1 &&
            iPackageCheckerMessage.getMessageText().length() > 1000)
      {
         mPackageCheckerMessages.add(
               new ValidatorMessage(iPackageCheckerMessage.getMessageType(), 
                     processMessage(iPackageCheckerMessage.getMessageText())));
      }
      else
      {
         mPackageCheckerMessages.add(iPackageCheckerMessage);
      }
   }

   /**
    * Sets the variable that holds the message that describes the overall status
    * of a package checker.
    * 
    * @param iOverallStatusMessage The mOverallStatusMessage to set.
    */
   public void addOverallStatusMessage(ValidatorMessage iOverallStatusMessage)
   {
      mOverallStatusMessage.add(iOverallStatusMessage);
   }
   
   /**
    * Sets the variable that holds the name of the package checker that this 
    * result is associated with.
    * 
    * @param iPackageCheckerName The mPackageCheckerName to set.
    */
   public void setPackageCheckerName(String iPackageCheckerName)
   {
      mPackageCheckerName = iPackageCheckerName;
   }
   
   /**
    * This method will remove any values larger than 1000 characters and replace them with
    * the "Original value removed due to size" String
    * 
    * @param iMessage A String representing the message to be processed
    * @return A String containing the possibly formatted message
    */
   public String processMessage( String iMessage )
   {
      if ( iMessage.indexOf("[") != -1 )
      {
         String message = iMessage;            
         int startIndex = message.indexOf("["); 
         int endIndex = message.indexOf("]");
         
         String tempMsg1 = message.substring(0,startIndex);
         String value = message.substring(startIndex+1, endIndex);
         String tempMsg2 = message.substring(endIndex+1,message.length());
         
         if ( value.length() > 1000 )
         {
            value = Messages.getString("Result.0");
         }
         
         return tempMsg1 + "[" + value + "]" + processMessage(tempMsg2);
      }
      else
      {
         return iMessage;
      }
   }

}
