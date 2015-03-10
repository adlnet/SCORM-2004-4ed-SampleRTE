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
package org.adl.samplerte.server.packageimport.sequencingOrderChecker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.adl.validator.packagechecker.PackageChecker;
import org.adl.validator.util.CheckerStateData;
import org.adl.validator.util.Messages;
import org.adl.validator.util.Result;
import org.adl.validator.util.ValidatorKeyNames;
import org.adl.validator.util.ValidatorMessage;
import org.adl.validator.util.processor.URIHandler;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * This class will parse and transform an xml instance using a schematron transform
 * The transform will perform the application profile checks and record the results
 * in an xml document object
 * 
 * @author ADL Technical Team
 *
 */
public class SequencingOrderChecker extends PackageChecker
{
   /**
    * A String containing the URI for the imscp namespace
    */
   static final String IMSCP = "http://www.imsglobal.org/xsd/imscp_v1p1";
   
   /**
    * A String containing the URI for the imsss namespace
    */
   static final String IMSSS = "http://www.imsglobal.org/xsd/imsss";
   
   /**
    * A String containing the URI for the xml namespace
    */
   static final Namespace XML = Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace");

   /**
    * This constant value represents the value 1024
    */
   static final int KILOBYTE = 1024;

   /**
    * A Document containing the xml instance to be validated
    */
   private static Document mXMLInstance;

   /**
    * A SAXBuilder object used to create Document Objects and execute tranforms
    */
   private SAXBuilder mBuilder;
   
   /**
    * A String containing the path to the xml instance being validated
    */
   private String mIMSManifestFile;
   
   /**
    * A list of the error messages generated 
    */
   private List mErrorMessages;
   
   /**
    * This constructor sets the parser to use the submanifest in validation
    * 
    * @param iValidateSubmanifest A boolean indicating whether or not to validate the submanifest
    */
   public SequencingOrderChecker()
   {
      mBuilder = new SAXBuilder(/*"org.apache.xerces.parsers.SAXParser"*/);
      mBuilder.setReuseParser(false);
      
      mIMSManifestFile = 
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.ROOT_DIRECTORY_KEY).toString() + 
         CheckerStateData.getInstance().getObjectValue(ValidatorKeyNames.XML_FILE_NAME_KEY).toString();
      
      mErrorMessages = new ArrayList();
      mResult = new Result();
      mResult.setPackageCheckerName("SequencingOrderChecker");
   }
   
   /* (non-Javadoc)
    * @see org.adl.validator.packagechecker.PackageChecker#check()
    */
   public Result check()
   {
      boolean success = true;
      
      try
      {
         mXMLInstance = parse(mIMSManifestFile);
         
         XPath seqPath = XPath.newInstance("//imsss:sequencing");
         seqPath.addNamespace("imscp", IMSCP);
         seqPath.addNamespace("imsss", IMSSS);

         Iterator seqIter = seqPath.selectNodes(mXMLInstance).iterator();
         while ( seqIter.hasNext() )
         {
            // Get all children of the current sequencing element
            Iterator seqChildIter = ((Element)seqIter.next()).getChildren().iterator();
            while ( seqChildIter.hasNext() )
            {
               Element child = (Element)seqChildIter.next();
               
               if ( child.getName().equals("controlMode") && child.getNamespaceURI().equals(IMSSS) )
               {
                  List successors = new ArrayList();
                  successors.add(IMSSS + ":sequencingRules");
                  successors.add(IMSSS + ":limitConditions");
                  successors.add(IMSSS + ":auxiliaryResources");
                  successors.add(IMSSS + ":rollupRules");
                  successors.add(IMSSS + ":objectives");
                  successors.add(IMSSS + ":randomizationControls");
                  successors.add(IMSSS + ":deliveryControls");
                  success = success && checkSiblings(child, successors);
                  
               }
               // sequencingRules child of sequencing
               else if ( child.getName().equals("sequencingRules") && child.getNamespaceURI().equals(IMSSS) )
               {
                  List successors = new ArrayList();
                  successors.add(IMSSS + ":limitConditions");
                  successors.add(IMSSS + ":auxiliaryResources");
                  successors.add(IMSSS + ":rollupRules");
                  successors.add(IMSSS + ":objectives");
                  successors.add(IMSSS + ":randomizationControls");
                  successors.add(IMSSS + ":deliveryControls");
                  success = success && checkSiblings(child, successors);
                  
                  // Get all children of the current element
                  Iterator siblingChildIter = child.getChildren().iterator();
                  while ( siblingChildIter.hasNext() )
                  {
                     Element siblingChild = (Element)siblingChildIter.next();
                     // preConditionRule child of sequencingRules
                     if ( siblingChild.getName().equals("preConditionRule") && siblingChild.getNamespaceURI().equals(IMSSS) )
                     {
                        successors = new ArrayList();
                        successors.add(IMSSS + ":exitConditionRule");
                        successors.add(IMSSS + ":postConditionRule");
                        success = success && checkSiblings(siblingChild, successors);
                        
                        // Get all children of the current element
                        Iterator siblingChildChildIter = siblingChild.getChildren().iterator();
                        while ( siblingChildChildIter.hasNext() )
                        {
                           Element siblingChildChild = (Element)siblingChildChildIter.next();
                           // ruleConditions child of preConditionRule
                           if ( siblingChildChild.getName().equals("ruleConditions") && siblingChildChild.getNamespaceURI().equals(IMSSS) )
                           {
                              successors = new ArrayList();
                              successors.add(IMSSS + ":ruleAction");
                              success = success && checkSiblings(siblingChildChild, successors);
                           }
                        }
                     }
                     // exitConditionRule child of sequencingRules
                     else if ( siblingChild.getName().equals("exitConditionRule") && siblingChild.getNamespaceURI().equals(IMSSS) )
                     {
                        successors = new ArrayList();
                        successors.add(IMSSS + ":postConditionRule");
                        success = success && checkSiblings(siblingChild, successors);
                        
                        // Get all children of the current element
                        Iterator siblingChildChildIter = siblingChild.getChildren().iterator();
                        while ( siblingChildChildIter.hasNext() )
                        {
                           Element siblingChildChild = (Element)siblingChildChildIter.next();
                           // ruleConditions child of exitConditionRule
                           if ( siblingChildChild.getName().equals("ruleConditions") && siblingChildChild.getNamespaceURI().equals(IMSSS) )
                           {
                              successors = new ArrayList();
                              successors.add(IMSSS + ":ruleAction");
                              success = success && checkSiblings(siblingChildChild, successors);
                           }
                        }
                     }
                     // postConditionRule child of sequencingRules
                     else if ( siblingChild.getName().equals("postConditionRule") && siblingChild.getNamespaceURI().equals(IMSSS) )
                     {                        
                        // Get all children of the current element
                        Iterator siblingChildChildIter = siblingChild.getChildren().iterator();
                        while ( siblingChildChildIter.hasNext() )
                        {
                           Element siblingChildChild = (Element)siblingChildChildIter.next();
                           // ruleConditions child of postConditionRule
                           if ( siblingChildChild.getName().equals("ruleConditions") && siblingChildChild.getNamespaceURI().equals(IMSSS) )
                           {
                              successors = new ArrayList();
                              successors.add(IMSSS + ":ruleAction");
                              success = success && checkSiblings(siblingChildChild, successors);
                           }
                        }
                     }
                  }
               }
               // limitConditions child of sequencing
               else if ( child.getName().equals("limitConditions") && child.getNamespaceURI().equals(IMSSS) )
               {
                  List successors = new ArrayList();
                  successors.add(IMSSS + ":auxiliaryResources");
                  successors.add(IMSSS + ":rollupRules");
                  successors.add(IMSSS + ":objectives");
                  successors.add(IMSSS + ":randomizationControls");
                  successors.add(IMSSS + ":deliveryControls");
                  success = success && checkSiblings(child, successors);
               }
               // auxillaryResources child of sequencing
               else if ( child.getName().equals("auxiliaryResources") && child.getNamespaceURI().equals(IMSSS) )
               {
                  List successors = new ArrayList();
                  successors.add(IMSSS + ":rollupRules");
                  successors.add(IMSSS + ":objectives");
                  successors.add(IMSSS + ":randomizationControls");
                  successors.add(IMSSS + ":deliveryControls");
                  success = success && checkSiblings(child, successors);
               }
               // rollupRules child of sequencing
               else if ( child.getName().equals("rollupRules") && child.getNamespaceURI().equals(IMSSS) )
               {
                  List successors = new ArrayList();
                  successors.add(IMSSS + ":objectives");
                  successors.add(IMSSS + ":randomizationControls");
                  successors.add(IMSSS + ":deliveryControls");
                  success = success && checkSiblings(child, successors);
                  
                  // Get all children of the current element
                  Iterator siblingChildIter = child.getChildren().iterator();
                  while ( siblingChildIter.hasNext() )
                  {
                     Element siblingChild = (Element)siblingChildIter.next();
                     // rollupRule child of rollupRules
                     if ( siblingChild.getName().equals("rollupRule") && siblingChild.getNamespaceURI().equals(IMSSS) )
                     {                        
                        // Get all children of the current element
                        Iterator siblingChildChildIter = siblingChild.getChildren().iterator();
                        while ( siblingChildChildIter.hasNext() )
                        {
                           Element siblingChildChild = (Element)siblingChildChildIter.next();
                           // rollupConditions child of rollupRule
                           if ( siblingChildChild.getName().equals("rollupConditions") && siblingChildChild.getNamespaceURI().equals(IMSSS) )
                           {
                              successors = new ArrayList();
                              successors.add(IMSSS + ":rollupAction");
                              success = success && checkSiblings(siblingChildChild, successors);
                           }
                        }
                     }
                  }
               }
               // objectives child of sequencing
               else if ( child.getName().equals("objectives") && child.getNamespaceURI().equals(IMSSS) )
               {
                  List successors = new ArrayList();
                  successors.add(IMSSS + ":randomizationControls");
                  successors.add(IMSSS + ":deliveryControls");
                  success = success && checkSiblings(child, successors);
                  
                  // Get all children of the current element
                  Iterator siblingChildIter = child.getChildren().iterator();
                  while ( siblingChildIter.hasNext() )
                  {
                     Element siblingChild = (Element)siblingChildIter.next();
                     // primaryObjective child of objectives
                     if ( siblingChild.getName().equals("primaryObjective") && siblingChild.getNamespaceURI().equals(IMSSS) )
                     {
                        successors = new ArrayList();
                        successors.add(IMSSS + ":objective");
                        success = success && checkSiblings(siblingChild, successors);
                        
                        // Get all children of the current element
                        Iterator siblingChildChildIter = siblingChild.getChildren().iterator();
                        while ( siblingChildChildIter.hasNext() )
                        {
                           Element siblingChildChild = (Element)siblingChildChildIter.next();
                           // minNormalizedMeasure child of primaryObjective
                           if ( siblingChildChild.getName().equals("minNormalizedMeasure") && siblingChildChild.getNamespaceURI().equals(IMSSS) )
                           {
                              successors = new ArrayList();
                              successors.add(IMSSS + ":mapInfo");
                              success = success && checkSiblings(siblingChildChild, successors);
                           }
                        }
                        
                     }
                     // objective child of objectives
                     else if ( siblingChild.getName().equals("objective") && siblingChild.getNamespaceURI().equals(IMSSS) )
                     {
                        // Get all children of the current element
                        Iterator siblingChildChildIter = siblingChild.getChildren().iterator();
                        while ( siblingChildChildIter.hasNext() )
                        {
                           Element siblingChildChild = (Element)siblingChildChildIter.next();
                           // minNormalizedMeasure child of objective
                           if ( siblingChildChild.getName().equals("minNormalizedMeasure") && siblingChildChild.getNamespaceURI().equals(IMSSS) )
                           {
                              successors = new ArrayList();
                              successors.add(IMSSS + ":mapInfo");
                              success = success && checkSiblings(siblingChildChild, successors);
                           }
                        }
                     }
                  }
               }
               // randomizationControls child of sequencing
               else if ( child.getName().equals("randomizationControls") && child.getNamespaceURI().equals(IMSSS) )
               {
                  List successors = new ArrayList();
                  successors.add(IMSSS + ":deliveryControls");
                  success = success && checkSiblings(child, successors);
               }
            }
         }
         
         mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.INFO,
            "Testing the order of the children of the <sequencing> element"));
         
         if ( !success )
         {
            mResult.setPackageCheckerPassed(false);
            
            mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.FAILED,
                  "The children of the <sequencing> element are not in the correct order"));
            
            mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.FAILED,
            "Sequencing information is ambiguous. The children of the <sequencing> element are not in the correct order"));
         }
         else
         {
            mResult.setPackageCheckerPassed(true);
            mResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.PASSED,
            "The children of the <sequencing> element are in the correct order"));
            mResult.addOverallStatusMessage(new ValidatorMessage(ValidatorMessage.PASSED,
                  "The children of the <sequencing> element are in the correct order"));
         }
         
         // Clean up unused DOM objects
         mXMLInstance = null;
         System.gc();
      }
      catch ( JDOMException jde )
      {
         jde.printStackTrace();
         mErrorMessages.add(new ValidatorMessage(ValidatorMessage.FAILED,
               Messages.getString("SchematronValidationDOMParser.5")));
         success = false;
      }
      catch ( NullPointerException npe )
      {
         npe.printStackTrace();
         mErrorMessages.add(new ValidatorMessage(ValidatorMessage.FAILED,
                Messages.getString("SchematronValidationDOMParser.6")));
         success = false;
      }
      
      return mResult;
   }
   
   private boolean checkSiblings ( Element iNode, List iSuccessors )
   {
      boolean success = true;
      
      try
      {
         XPath siblingPath = XPath.newInstance("preceding-sibling::*");
         Iterator siblingIter = siblingPath.selectNodes(iNode).iterator();
         while ( siblingIter.hasNext() && success )
         {
            Element sibling = (Element)siblingIter.next();
            String siblingName = sibling.getNamespaceURI() + ":" + sibling.getName();
            success = success && !iSuccessors.contains(siblingName);
         }
      }
      catch ( JDOMException jde )
      {
         jde.printStackTrace();
         success = false;
      }
      return success;
   }
   
   /**
    * This method parses a file to create a Document object
    * 
    * @param iFile The file to be parsed
    * @return A Document object containing the file
    */
   private Document parse(String iFile)
   {
      iFile = URIHandler.decode(iFile, URIHandler.ENCODING);
      try
      {
         FileInputStream fis = new FileInputStream(iFile);
         return parse(fis);
      }
      catch ( IOException ioe )
      {
         //ioe.printStackTrace();
         String msg = 
            Messages.getString("SchematronValidationDOMParser.7");
         ValidatorMessage message = new ValidatorMessage(ValidatorMessage.FAILED, msg);
         mErrorMessages.add(message);
         return null;
      }      
   }
   
   /**
    * This method parses an inputStream to create a Document object
    * 
    * @param iStream Is an inputStream connected to the file to be parsed
    * @return A Document object containing the file
    */
   private Document parse( InputStream iStream )
   {
      try
      {
         return mBuilder.build(iStream);
      }
      catch ( IOException ioe )
      {
         //ioe.printStackTrace();
         String msg = 
            Messages.getString("SchematronValidationDOMParser.7");
         ValidatorMessage message = new ValidatorMessage(ValidatorMessage.FAILED, msg);
         mErrorMessages.add(message);
         return null;
      }
      catch ( JDOMException jde )
      {
         //jde.printStackTrace();
         String msg = 
            Messages.getString("SchematronValidationDOMParser.7");
         ValidatorMessage message = new ValidatorMessage(ValidatorMessage.FAILED, msg);
         mErrorMessages.add(message);
         return null;
      }     
   }
   

   
   
}
