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

*******************************************************************************/

package org.adl.datamodels;

import java.io.Serializable;


/**
 * Encapsulation of information required for processing a data model request.
 * <br><br>
 * 
 * <strong>Filename:</strong> DMDelimiterDescriptor.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * Encapsulation of all information required to describe one dot-notation bound
 * delimiter.  This information will be used to create instances of delimiters
 * assocaited with data model elements.<br><br>
 * 
 * <strong>Design Issues:</strong><br><br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * All fields are purposefully public to allow immediate access to known data
 * elements.<br><br>
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
public class DMDelimiterDescriptor implements Serializable 
{

   /**
    * Describes the name of this delimiter
    */
   public String mName = null;


   /**
    * Describes if the default value of this delimiter
    */
   public String mDefault = null;


   /**
    * Describes the SPM for the value.
    */
   public int mValueSPM = -1;


   /**
    * Describes the method used to validate the value of this delimiter.
    */
   public DMTypeValidator mValidator = null;

   /**
    * Provides a way to store delimiter information such as name, default value,
    * and type of validator.
    * 
    * @param iName  The name of the delimiter
    * @param iDefault  The default value for the delimiter
    * @param iValidator  The validator associated with the delimiter
    */
   public DMDelimiterDescriptor(String iName, 
                                String iDefault,
                                DMTypeValidator iValidator)
   {
      mName = iName;
      mDefault = iDefault;
      mValidator = iValidator;
   }

   /**
    * Provides a way to store delimiter information such as name, default value,
    * and type of validator.
    * 
    * @param iName The name of the delimiter
    * 
    * @param iDefault The default value for the delimiter
    * 
    * @param iValueSPM The smallest permitted maximum size allowed for this
    *                      delimiter
    * 
    * @param iValidator The validator associated with the delimiter
    */
   public DMDelimiterDescriptor(String iName, 
                                String iDefault,
                                int iValueSPM,
                                DMTypeValidator iValidator)
   {
      mName = iName;
      mDefault = iDefault;
      mValueSPM = iValueSPM;
      mValidator = iValidator;
   }


}  // end DMDelimiterDescriptor
