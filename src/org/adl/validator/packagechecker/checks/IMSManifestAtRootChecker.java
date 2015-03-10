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

import java.io.File;

import org.adl.validator.packagechecker.PackageChecker;

import org.adl.validator.util.CheckerStateData;
import org.adl.validator.util.Messages;
import org.adl.validator.util.Result;
import org.adl.validator.util.ValidatorCheckerNames;
import org.adl.validator.util.ValidatorKeyNames;
import org.adl.validator.util.ValidatorMessage;
import org.adl.validator.util.processor.URIHandler;

/**
 * This checker will ensure an imsmanifest.xml is at the root of the package
 * 
 * @author ADL Technical Team
 *
 */
public class IMSManifestAtRootChecker extends PackageChecker
{
   /**
    * String representing the full path of the imsmanifest.xml file
    */
   private String mIMSManifestFile; 
   
   /**
    * String representing the file given for validation
    */
   final private String mFileName;   
      
   /**
    * Default Constructor. Sets the attributes to their initial values.
    */
   public IMSManifestAtRootChecker()
   {
      // default constructor     
      super();
      mResult = new Result();
      mResult.setPackageCheckerName(ValidatorCheckerNames.MAN_AT_ROOT);  
      
      mIMSManifestFile = 
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.ROOT_DIRECTORY_KEY).toString() +
         "imsmanifest.xml";
      
      mIMSManifestFile = URIHandler.decode(mIMSManifestFile, URIHandler.ENCODING);
      
      mFileName = CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.XML_FILE_NAME_KEY).toString();      
   }
   
   /* (non-Javadoc)
    * @see org.adl.validator.packagechecker.PackageChecker#check()
    */
   public Result check()
   {
      // We have to stop if the checkerStateData access did not work correctly
      if ( mIMSManifestFile == null || mFileName == null )
      {
         mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.FAILED,
         Messages.getString("IMSManifestAtRootChecker.0")));
         mResult.setPackageCheckerPassed(false);  
         mResult.setTestStopped(true);
      }
      else
      {
         String message = "";      
         final File manifestFile = new File(mIMSManifestFile);      
               
         mResult.setCheckerSkipped(false);
         
         // If test subjest is not a zip file, ensure it is an imsmanifest.xml file
         if ( !"imsmanifest.xml".equals(mFileName) && mFileName.toLowerCase().indexOf(".zip") == -1 )
         {
            message = Messages.getString("IMSManifestAtRootChecker.1");
            mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.WARNING, message));
         }
         
         if ( manifestFile.exists() )
         {
            mResult.setPackageCheckerPassed(true);
            mResult.setTestStopped(false);         
         }
         else
         {
            mResult.setPackageCheckerPassed(false);
            mResult.setTestStopped(true);         
            message = Messages.getString("IMSManifestAtRootChecker.2");
            mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.FAILED, message));
            mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.FAILED, message));
         }
      }
            
      return mResult;
   }
}
