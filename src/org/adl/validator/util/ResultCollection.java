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
 * Holds all of the results of the package checkers during package validation.
 * 
 * @author ADL Technical Team
 *
 */
public class ResultCollection
{
   
   /**
    * List of Result objects that represent the results of the package ckeckers.
    */
   private List<Result> mPackageResultsCollection;
   
   /**
    * Constructor
    */
   public ResultCollection()
   {
      mPackageResultsCollection = new ArrayList<Result>();
      
   }

   /**
    * Provides a way to get the map of the Results.
    * 
    * @return Returns the mPackageResultsCollection.
    */
   public List<Result> getPackageResultsCollection()
   {
      return mPackageResultsCollection;
   }
   
   /**
    * Provides a way to get a specific Result based on the package checker name 
    * that is stored in the result.
    * 
    * @param iResultName - String that represents the name of a package checker.
    * @return Result - A Result object that is associated with a specific package
    * checker.
    */
   public Result getPackageResult(String iResultName)
   {
      Result returnResult = new Result();
      for ( int i = 0; i < mPackageResultsCollection.size(); i++ )
      {
         Result tempResult = (Result)mPackageResultsCollection.get(i);
         if ( tempResult.getPackageCheckerName().equals(iResultName) )
         {
            returnResult = tempResult;
            break;
         }
      }
      return returnResult;
      
   }
   
   /**
    * Adds a package checker result to the ResultCollection.
    * 
    * @param iPackageResult - A Result object that is associated with a specific 
    * package checker.
    */
   public void addPackageResult(Result iPackageResult)
   {
      mPackageResultsCollection.add( iPackageResult );
   }
   
   /**
    * Provides a way to find if all the checkers in the ResultCollection
    * combined to equal passed or failed.
    * 
    * @return boolean indicating if all combined to pass or fail.
    */
   public boolean isAllCheckerPassed()
   {
      boolean allPassed = true;
      
      for ( int i = 0; i < mPackageResultsCollection.size(); i++ )
      {
         if ( !( ( (Result)mPackageResultsCollection.get(i) ).isPackageCheckerPassed() ) )
         {
            allPassed = false;
            break;
         }
      }
      return allPassed;
   }
   
   

}
