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
import org.adl.validator.packagechecker.parsers.SchemaValidationDOMParser;
import org.adl.validator.util.CheckerStateData;
import org.adl.validator.util.Messages;
import org.adl.validator.util.Result;
import org.adl.validator.util.ValidatorCheckerNames;
import org.adl.validator.util.ValidatorKeyNames;
import org.adl.validator.util.ValidatorMessage;
import org.adl.validator.util.processor.SchemaHandler;

/**
 * This checker will validate the xml against the schemas defined in the xml
 * file
 * 
 * @author ADL Technical Team
 *
 */
public class SchemaValidationChecker extends PackageChecker
{

   /**
    * String representing the full path of the imsmanifest.xml file
    */
   final private String mIMSManifestFile;
   
   /**
    * Default Constructor. Sets the attributes to their initial values.
    */
   public SchemaValidationChecker()
   {
      // default constructor
      super();
      mIMSManifestFile = 
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.ROOT_DIRECTORY_KEY).toString() +
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.XML_FILE_NAME_KEY).toString();
      
      mResult = new Result();
      mResult.setPackageCheckerName(ValidatorCheckerNames.SCHEMA_VAL);
   }
   
   /* (non-Javadoc)
    * @see org.adl.validator.packagechecker.PackageChecker#check()
    */
   public Result check()
   {      
      final String rootDir = CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.ROOT_DIRECTORY_KEY).toString();
      
      // We have to stop if the checkerStateData access did not work correctly
      if ( mIMSManifestFile == null || rootDir == null )
      {
         mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.FAILED,
         Messages.getString("SchemaValidationChecker.0")));
         mResult.setPackageCheckerPassed(false);  
      }
      else
      {
         mResult.setCheckerSkipped(false);
         
         // Obtain schemaLocation list
         final SchemaHandler handler = new SchemaHandler(mIMSManifestFile);         
         
         final SchemaValidationDOMParser schemaValidationParser = new SchemaValidationDOMParser(handler.createSchemaLocationList(), rootDir);
         
         mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.INFO,
               Messages.getString("SchemaValidationChecker.1")));
         mResult.setPackageCheckerPassed(
               schemaValidationParser.performParse(mIMSManifestFile));
         
         if ( mResult.isPackageCheckerPassed() )
         {            
            final String msg = Messages.getString("SchemaValidationChecker.3");
            mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.PASSED,
                  msg));
            mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.PASSED, 
                  msg));
         }
         else
         {   
            final List<ValidatorMessage> parseErrors = schemaValidationParser.getParseMessages();
            final Iterator<ValidatorMessage> iter = parseErrors.iterator();
            while ( iter.hasNext() )
            {
               mResult.addPackageCheckerMessage((ValidatorMessage)iter.next());
            }
            mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.FAILED,
                  Messages.getString("SchemaValidationChecker.2")));
         }
      }
      return mResult;
   }
}
