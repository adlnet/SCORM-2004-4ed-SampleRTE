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
package org.adl.validator.packagechecker;

import org.adl.validator.util.Result;
/**
 * This class is an abstract implementation which will be used as a
 * template for all other checkers
 * 
 * @author ADL Technical Team
 *
 */
public abstract class PackageChecker
{      
      /**
       * Result object which contains the results of the tests performed by the
       * checker
       */
      protected Result mResult;
      
      /**
       * Default Constructor. Sets the attributes to their initial values.
       */
      public PackageChecker()
      {
         // default constructor
      }      
      
      /**
       * Performs the given check
       * 
       * @return Result object containing the results and other information
       * relating to the given check
       */
      public abstract Result check();
      
}
