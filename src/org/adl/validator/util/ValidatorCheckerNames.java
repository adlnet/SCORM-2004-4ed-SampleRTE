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

/**
 * Holds the String values of the names of the package checkers.
 * 
 * @author ADL Technical Team
 *
 */
public class ValidatorCheckerNames
{

   /**
    * Holds the name for the ApplicationProfileChecker.
    */
   public static final String APP_PROFILE = "APPLICATION_PROFILE"; 
   
   /**
    * Holds the name for the ExcessBaggageChecker.
    */
   public static final String EXCESS_BAG = "EXCESS_BAGGAGE";
   
   /**
    * Holds the name for the IMSManifestAtRootChecker.
    */
   public static final String MAN_AT_ROOT = "MANIFEST_AT_ROOT";
   
   /**
    * Holds the name for the ManifestRootElementChecker.
    */
   public static final String MAN_ROOT_ELE = "MANIFEST_ROOT_ELEMENT";
   
   /**
    * Holds the name for the result returned in PackageProcessor.
    */
   public static final String PACK_PROCESS = "PACKAGE_PROCESSOR";
   
   /**
    * Holds the name for the RequiredFilesChecker.
    */
   public static final String REQ_FILES = "REQUIRED_FILES_AT_ROOT";
   
   /**
    * Holds the name for the ResourceHrefChecker.
    */
   public static final String RES_HREF = "RESOURCE_HREF";
   
   /**
    * Holds the name for the SchemaValidationChecker.
    */
   public static final String SCHEMA_VAL = "SCHEMA_VALIDATION";
   
   /**
    * Holds the name for the SubmanifestChecker.
    */
   public static final String SUBMANIFEST = "SUBMANIFEST";
   
   /**
    * Holds the name for the SequenceOrderChecker.
    */
   public static final String SEQUENCE_ORDER = "SEQUENCE_ORDER";
   
   /**
    * Holds the name for the WellformednessChecker.
    */
   public static final String WELLFORM = "WELLFORMEDNESS";
}
