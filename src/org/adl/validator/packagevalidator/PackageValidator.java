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
package org.adl.validator.packagevalidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.adl.validator.packagechecker.PackageCheckerInvoker;
import org.adl.validator.util.CheckerStateData;
import org.adl.validator.util.Result;
import org.adl.validator.util.ResultCollection;
import org.adl.validator.util.ValidatorKeyNames;
import org.adl.validator.util.processor.UnZipHandler;

/**
 * The controlling class the handles the validation of content packages.
 * 
 * @author ADL Technical Team
 *
 */
public class PackageValidator
{

   /**
    * Instance of the collection of Result objects that are returned from every
    * package checker.
    */
   private ResultCollection mResultCollection;
   
   /**
    * Object that holds the results of different checks during validation.
    */
   private Result mResult;
   
   /**
    * File object that holds the temp folder location where zip files are 
    * extracted to. 
    */
   private File mTempDir = new File(System.getProperty("java.io.tmpdir") +
                                    File.separator + "tempZipFolder" + File.separator);
   
   /**
    * Overload Constructor.
    * 
    * @param iFileName - The name of the SCORM Content Package test subject
    * @param iAppProfileType - The Application Profile type of the test
    *           subject (content aggregation or resource )
    * @param iManifestOnly - The boolean describing whether or not the IMS
    *           Manifest is to be the only subject validated. True implies that
    *           validation occurs only on the IMS Manifest (checks include
    *           wellformedness, schema validation, and application profile
    *           checks). False implies that the entire Content Package be
    *           validated (IMS Manifest checks with the inclusion of the
    *           required files checks, metadata, and sco testing)..
    * @param iResultCollection - Collection of Result objects that are returned 
    *           from every package checker.
    */
   public PackageValidator(String iFileName, String iAppProfileType, 
                      boolean iManifestOnly, ResultCollection iResultCollection)
   {
      CheckerStateData.getInstance().clearCollection();
      CheckerStateData.getInstance().setObject(ValidatorKeyNames.FILE_NAME_KEY, iFileName);
      CheckerStateData.getInstance().setObject(ValidatorKeyNames.APP_PROFILE_TYPE_KEY, iAppProfileType);
      CheckerStateData.getInstance().setObject(ValidatorKeyNames.MANIFEST_ONLY_KEY, Boolean.valueOf(iManifestOnly));
      CheckerStateData.getInstance().setObject(ValidatorKeyNames.CHECKER_LIST_KEY, new ArrayList<String>());

      CheckerStateData.getInstance().addReservedKey(ValidatorKeyNames.FILE_NAME_KEY);
      CheckerStateData.getInstance().addReservedKey(ValidatorKeyNames.APP_PROFILE_TYPE_KEY);
      CheckerStateData.getInstance().addReservedKey(ValidatorKeyNames.MANIFEST_ONLY_KEY);

      mResultCollection = iResultCollection;
      
      // Clear out any previous package data that may have been left by an
      // unexpected exit
      UnZipHandler tempUnZip = new UnZipHandler();
       tempUnZip.clearTempDir(mTempDir);
      
      PackageProcessor packageProcessor = new PackageProcessor();
      mResult = packageProcessor.processPackage();
   }
   
   /**
    * Performs the execution of the package checkers.
    * 
    * @return ResultCollection - Collection of Result objects that are returned 
    *           from every package checker.
    */
   public ResultCollection executePackageCheckers()
   {
      if( mResult.isTestStopped())
      {
         mResultCollection.addPackageResult(mResult);
      }
      else
      {
         PackageCheckerInvoker pcInvoker = new PackageCheckerInvoker(mResultCollection);
         mResultCollection = pcInvoker.performPackageChecks();
      }
      UnZipHandler tempUnZip = new UnZipHandler();
       tempUnZip.clearTempDir(mTempDir);
      CheckerStateData.getInstance().clearCollection();
      return mResultCollection;
   }
   
   /**
    * This method sets the List of checkers to be executed
    * 
    * @param iCheckerList contains a List of checkers to be executed
    */
   public void setCheckerList(List<String> iCheckerList)
   {
      CheckerStateData.getInstance().setObject(ValidatorKeyNames.CHECKER_LIST_KEY, iCheckerList);
   }
}
