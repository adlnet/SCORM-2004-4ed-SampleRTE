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

package org.adl.sequencer;


/**
 * Encapsulation of objective status information associated with an activity.
 * <br><br>
 * 
 * <strong>Filename:</strong> ADLObjStatus.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * <br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE. <br>
 * <br>
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
 *     <li>IMS SS 1.0
 *     <li>SCORM 2004 4th Edition
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class ADLObjStatus 
{

   /**
    * Identifier of this objective.
    */
   public String mObjID = null;

   /** 
    * Indicates if this objective has a known measure.
    */
   public boolean mHasMeasure = false;
   
   /** 
    * Indicates if this objective has a known raw measure.
    */
   public boolean mHasRawScore = false;
   
   /** 
    * Indicates if this objective has a known min score.
    */
   public boolean mHasMinScore = false;
   
   /** 
    * Indicates if this objective has a known max score.
    */
   public boolean mHasMaxScore = false;
   
   /** 
    * Indicates if this objective has a known progress measure.
    */
   public boolean mHasProgressMeasure = false;

   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid range: <code>[-1.0, 1.0]</code>
    */
   public double mMeasure = 1.0;

   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid type: <code>real(10,7)</code>
    */
   public double mRawScore = 0;
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid type: <code>real(10,7)</code>
    */
   public double mMinScore = 0;
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid type: <code>real(10,7)</code>
    */
   public double mMaxScore = 0;
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid vocab: <code>completed, incomplete, not attempted,
    *  unknown</code>
    */
   public String mCompletionStatus = "unknown";
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid range: <code>[0.0, 1.0]</code>
    */
   public double mProgressMeasure = 0;
   
   /** 
    * Indicates if the objective contributes to rollup
    */
   public String mStatus = ADLTracking.TRACK_UNKNOWN;


}  // end ADLObjStatus
