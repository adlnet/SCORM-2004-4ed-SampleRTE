/******************************************************************************

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

*******************************************************************************/

package org.adl.datamodels.datatypes;

import org.adl.datamodels.DMTypeValidator;
import org.adl.datamodels.DMErrorCodes;

import java.io.Serializable;
/**
 * Provides support for the Smallest Permitted Maximum (SPM) value, as defined  
 * in the SCORM 2004 RTE Book<br><br>
 * 
 * <strong>Filename:</strong> SpmRangeValidator.java<br><br>
 * 
 * <strong>Description:</strong><br><br>
 * 
 * <strong>Design Issues:</strong><br><br>
 * 
 * <strong>Implementation Issues:</strong><br><br>
 * 
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:</strong><br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>SCORM 2004</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class SPMRangeValidator extends DMTypeValidator implements Serializable
{
   /**
    * Specifies the smallest permitted maximum allowed for a string.
    */
   private int mSPM = -1;

   /**
    * Default constructor required for serialization.
    */
   public SPMRangeValidator()
   {
      mType = "characterstring";
   }

   /**
    * Describes the smallest permitted maximum allowed for a string.
    * 
    * @param iSPM Defines the initial SPM
    */
   public SPMRangeValidator(int iSPM) 
   { 
      mSPM = iSPM;
      mType = "characterstring";
   }

   /**
    * Truncates the value to meet the DataType's SPM
    * 
    * @param  iValue  The value to be truncated
    * 
    * @return Returns the value truncated at the DataType's SPM
    */
   public String trunc(String iValue)
   {
      String trunc = iValue;

      if ( mSPM > 0 && iValue.length() > mSPM )
      {
         trunc = trunc.substring(0, mSPM);
      }

      return trunc;
   }


   /**
    * Validates the provided string against a known format.
    * 
    * @param iValue The value being validated.
    * 
    * @return An abstract data model error code indicating the result of this
    *         operation.
    */
   public int validate(String iValue)
   {
      // Assume the value is valid
      int valid = DMErrorCodes.NO_ERROR;

      if ( iValue != null )
      {
         int spmLength = iValue.length();

         if ( mSPM > -1 )
         {
            if ( spmLength > mSPM )
            {
               valid = DMErrorCodes.SPM_EXCEEDED;
            }
         }
      }
      else
      {
         // A null value can never be valid
         valid = DMErrorCodes.UNKNOWN_EXCEPTION;
      }

      return valid;
   }

} // end SPMRangeValidator