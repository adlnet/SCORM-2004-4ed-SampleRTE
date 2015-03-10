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
package org.adl.validator.packagechecker.checks;

import java.util.Iterator;
import java.util.List;

import org.adl.validator.packagechecker.PackageChecker;
import org.adl.validator.packagechecker.parsers.WellformednessSaxParser;
import org.adl.validator.util.CheckerStateData;
import org.adl.validator.util.Messages;
import org.adl.validator.util.Result;
import org.adl.validator.util.ValidatorCheckerNames;
import org.adl.validator.util.ValidatorKeyNames;
import org.adl.validator.util.ValidatorMessage;

/**
 * This checker will determine if the xml instance is wellformed
 * 
 * @author ADL Technical Team
 *
 */
public class WellformednessChecker extends PackageChecker
{
   /**
    * String representing the full path of the imsmanifest.xml file
    */
   private String mIMSManifestFile;
   
   /**
    * Default Constructor. Sets the attributes to their initial values.
    */
   public WellformednessChecker()
   {
      // default constructor
      super();
      mIMSManifestFile = 
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.ROOT_DIRECTORY_KEY).toString() +
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.XML_FILE_NAME_KEY).toString();
      
      mResult = new Result();
      mResult.setPackageCheckerName(ValidatorCheckerNames.WELLFORM); 
   }
   
   /* (non-Javadoc)
    * @see org.adl.validator.packagechecker.PackageChecker#check()
    */
   public Result check()
   {
      String message = "";
      mResult.setCheckerSkipped(false);
    
      // We have to stop if the checkerStateData access did not work correctly
      if ( mIMSManifestFile == null )
      {
         mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.FAILED,
         Messages.getString("WellformednessChecker.0")));
         mResult.setPackageCheckerPassed(false);   
         mResult.setTestStopped(true);
         return mResult;
      }
      
      final WellformednessSaxParser wellformednessParser = new WellformednessSaxParser();
      
      mResult.setPackageCheckerPassed(
            wellformednessParser.performParse(mIMSManifestFile));
      
      mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.INFO,
            Messages.getString("WellformednessChecker.1")));
      
      if ( mResult.isPackageCheckerPassed() )
      {
         mResult.setTestStopped(false);         
         message = Messages.getString("WellformednessChecker.3");
         mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.PASSED, message));
         mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.PASSED,
               message));
      }
      else
      {         
         mResult.setTestStopped(true);
         final List<ValidatorMessage> parseErrors = wellformednessParser.getParseMessages();
         final Iterator<ValidatorMessage> iter = parseErrors.iterator();
         
         while ( iter.hasNext() )
         {
            mResult.addPackageCheckerMessage((ValidatorMessage)iter.next());
         }
         mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.FAILED,
               Messages.getString("WellformednessChecker.2")));
      }
      
      return mResult;
   }
}
