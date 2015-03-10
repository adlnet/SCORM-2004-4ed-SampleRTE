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
import java.util.Vector;

import java.net.URISyntaxException;
import java.net.URI;

/**
 * Provides support for the URI data type, as defined in the 
 * SCORM 2004 RTE Book<br><br>
 * 
 * <strong>Filename:</strong> URIValidator.java<br><br>
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
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class URIValidator extends DMTypeValidator implements Serializable
{

   /**
    * Describes the smallest permitted maximum allowed for a URI.
    */
   private int mSPM = -1;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Constructors
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Default constructor required for serialization.
    */
   public URIValidator() 
   {
      mType = "URI";
   }


   /**
    * Describes the smallest permitted maximum allowed for the URI.
    * 
    * @param iSPM  Describes the SPM for the URI being validated
    * 
    * @param iType Describes the human readable name for this type validator.
    */
   public URIValidator(int iSPM, String iType) 
   { 
      mSPM = iSPM;
      mType = iType;
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Compares two valid data model elements for equality.
    * 
    * @param iFirst  The first value being compared.
    * 
    * @param iSecond The second value being compared.
    * 
    * @param iDelimiters
    *                The common set of delimiters associated with the
    *                values being compared.
    * 
    * @return Returns <code>true</code> if the two values are equal, otherwise
    *         <code>false</code>.
    */
   public boolean compare(String iFirst, String iSecond, Vector iDelimiters)
   {
      boolean equal = true;

      if ( iFirst == null || iFirst.trim().equals("") )
      {
         // The first string is an invalid URI
         equal = false;
      }
      else
      {
         if ( iSecond == null || iSecond.trim().equals("") )
         {
            // The second string is an invalid URI
            equal = false;
         }
         else
         {

            try
            {
               // Try to create URIs from the provided strings
               URI uri1 = new URI(iFirst);
               URI uri2 = new URI(iSecond);

               equal = uri1.equals(uri2);
            }
            catch ( URISyntaxException use )
            {
               // One of the stings is not a valid URI
               equal = false;
            }
         }
      }

      return equal;
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

      if ( (mSPM > 0) && (iValue.length() > mSPM) )
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

      if ( iValue == null )
      {
         // A null value can never be valid
         return DMErrorCodes.UNKNOWN_EXCEPTION;
      }

      // URIs are not allowed to be empty strings
      if ( iValue.trim().equals("") )
      {
         valid = DMErrorCodes.TYPE_MISMATCH;
      }
      else
      {
         try
         {
            // Try to create a URI from the provided string
            URI newURI = new URI(iValue); // TODO: the newURI variable is never used or read - Do we need it?

            if ( mSPM > -1 )
            {
               if ( iValue.length() > mSPM )
               {
                  valid = DMErrorCodes.SPM_EXCEEDED;
               }
            }
         }
         catch ( URISyntaxException use )
         {
            valid = DMErrorCodes.TYPE_MISMATCH;
         }
      }

      return valid;
   }

} // end URIValidatorValidator

