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

package org.adl.samplerte.server;

/**
 * Encapsulation of information required for launch.<br><br>
 * 
 * <strong>Filename:</strong>ObjectivesData.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>ObjectivesData</code> encapsulates the information about a specific
 * Objective returned from the dsp_createObjective form.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE<br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * All fields are purposely public to allow immediate access to known data
 * elements.<br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS 1.0
 *     <li>SCORM 2004 4th Edition
 * </ul>
 * 
 * @author ADL Technical Team
 */ 
public class ObjectivesData
{
   /**
    * Valid values are any real (10,7) decimal number or the string "unknown"
    */
   public String mRawScore = null;
   
   /**
    * Valid values are any real (10,7) decimal number or the string "unknown"
    */
   public String mMinScore = null;
   
   /**
    * Valid values are any real (10,7) decimal number or the string "unknown"
    */
   public String mMaxScore = null;
   
   /**
    * Valid values are any real (10,7) decimal number between 0.0 and 1.0 or the string "unknown".
    */
   public String mProgressMeasure = null;
   
   /**
    * The completion status of the objective. The value can be any one of the following 4 values -  
    * notAttempted, incomplete, completed or unknown.
    */
   public String mCompletionStatus = null;  
   
   /**
    * The user's ID. This is an ID that represent the user passed from the 
    * form. 
    */ 
   public String mUserID = null;

   /**
    * The ID given to the objective in the form by the user.  
    *     
    */ 
   public String mObjectiveID = null;

   /**
    * The satisfied status of the objective. The value can be any one of 3 values:
    * unknown, satisfied, not satisfied
    */ 
   public String mSatisfied = null;

   /**        
    * The measure of this objective. This value can be between -1.0 and 1.0 
    * inclusive or the string "unknown".
    */ 
   public String mMeasure = null;

   /**
    *  Indicator of whether the objective was a duplicate or not. This will
    * be left as an empty string unless there is an error.
    */
   public String mObjErr = "";


}



