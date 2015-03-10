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

import java.io.FileInputStream;
import java.io.IOException;

import org.adl.validator.packagechecker.PackageChecker;
import org.adl.validator.util.CheckerStateData;
import org.adl.validator.util.Messages;
import org.adl.validator.util.Result;
import org.adl.validator.util.ValidatorCheckerNames;
import org.adl.validator.util.ValidatorKeyNames;
import org.adl.validator.util.ValidatorMessage;
import org.adl.validator.util.processor.URIHandler;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * This class will determine if the root element of the xml instance is imscp:manifest
 * 
 * @author ADL Technical Team
 */
public class ManifestRootElementChecker extends PackageChecker
{      
   /**
    * String representing the full path of the imsmanifest.xml file
    */
   private String mIMSManifestFile;
   
   /**
    * A SAXBuilder object used to create Document Objects and execute tranforms
    */
   final private SAXBuilder mBuilder;
   
   /**
    * Default Constructor. Sets the attributes to their initial values.
    */
   public ManifestRootElementChecker()
   {
      super();
      mIMSManifestFile = 
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.ROOT_DIRECTORY_KEY).toString() +
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.XML_FILE_NAME_KEY).toString();
            
      mIMSManifestFile = URIHandler.decode(mIMSManifestFile, URIHandler.ENCODING);
      
      mBuilder = new SAXBuilder(/*"org.apache.xerces.parsers.SAXParser"*/);
      mBuilder.setReuseParser(false);
      mResult = new Result();
      mResult.setPackageCheckerName(ValidatorCheckerNames.MAN_ROOT_ELE);
   }      
      
   /**
    * Determines if the root element of the xml instance is imscp:manifest
    * 
    * @return Result object containing the results and other information
    * relating to the given check
    */
   public Result check()
   {
      // We have to stop if the checkerStateData access did not work correctly
      if ( mIMSManifestFile == null )
      {
         mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.FAILED,
         Messages.getString("ManifestRootElementChecker.0")));
         mResult.setPackageCheckerPassed(false); 
         mResult.setTestStopped(true);
      }
      else
      {
      
         final Document manifestDoc = parseDocument(mIMSManifestFile);
         
         // Add newly created xml manifest JDom object to CheckerStateData for future checkers
         CheckerStateData.getInstance().setObject("manifestDOM", manifestDoc);
         CheckerStateData.getInstance().addReservedKey("manifestDOM");
         
         // If root element is imscp:manifest
         final Element rootEle = manifestDoc.getRootElement();
         if ( rootEle.getName().equals("manifest") &&
              rootEle.getNamespaceURI().equals("http://www.imsglobal.org/xsd/imscp_v1p1") )
         {
            mResult.setPackageCheckerPassed(true);
         }
         else
         {
            mResult.setTestStopped(true);
            mResult.setPackageCheckerPassed(false);
            String strMessage = "";
            strMessage = Messages.getString("ManifestRootElementChecker.1", rootEle.getName(), rootEle.getNamespaceURI());
            ValidatorMessage message = new ValidatorMessage(ValidatorMessage.FAILED, strMessage);
            mResult.addPackageCheckerMessage(message);
            
            strMessage = Messages.getString("ManifestRootElementChecker.2");
            message = new ValidatorMessage(ValidatorMessage.FAILED, strMessage);
            mResult.addPackageCheckerMessage(message);
            mResult.addOverallStatusMessage(message);
         }
      }
      return mResult;
   }
   
   /**
    * This method parses an inputStream to create a Document object
    * 
    * @param iFile Is a String connected representing the file to be parsed
    * @return A Document object containing the file
    */
   private Document parseDocument( final String iFile )
   {
      try
      {
         return mBuilder.build(new FileInputStream(iFile));
      }
      catch ( IOException ioe )
      {
         //ioe.printStackTrace();
         final ValidatorMessage message = new ValidatorMessage(ValidatorMessage.FAILED, 
               Messages.getString("ManifestRootElementChecker.3"));
         mResult.addPackageCheckerMessage(message);
         return null;
      }
      catch ( JDOMException jde )
      {
         //jde.printStackTrace();
         final ValidatorMessage message = new ValidatorMessage(ValidatorMessage.FAILED, 
               Messages.getString("ManifestRootElementChecker.4"));
         mResult.addPackageCheckerMessage(message);
         return null;
      }     
   }
}
