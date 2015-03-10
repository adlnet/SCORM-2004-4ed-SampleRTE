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

import org.adl.datamodels.DMErrorCodes;
import org.adl.datamodels.DMTypeValidator;
import java.io.Serializable;

/**
 * 
 * 
 * <strong>Filename:</strong> VocabularyValidator.java<br><br>
 * 
 * <strong>Description:</strong><br>Provides support for the Vocab data 
 * type, as defined in the SCORM 2004 RTE Book<br><br>
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
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class VocabularyValidator extends DMTypeValidator implements Serializable
{

   /**
    * A array of vocabularies values
    */
   String [] mVocabList = null;

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Constructor required for vocabulary initialization.
    * 
    * @param iVocab The array of vocabulary strings to be used in 
    * initialization.
    */
   public VocabularyValidator(String [] iVocab) 
   { 
      mVocabList = iVocab;
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
      // Assume the value is not valid
      int valid = DMErrorCodes.TYPE_MISMATCH;
      
      // Check first if mVocablist is null
      if ( mVocabList != null )
      {
         for ( int i = 0; i < mVocabList.length; i++ )
         {
            String tmpVocab = mVocabList[i];

            // Check if tmpVocab is null
            if ( tmpVocab != null )
            {
               // Check to see if the element equals the input value
               if ( tmpVocab.equals(iValue) )
               {
                  valid = DMErrorCodes.NO_ERROR;
                  break;
               }
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

} // end VocabularyValidator
