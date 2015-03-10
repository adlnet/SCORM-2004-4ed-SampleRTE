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
 * <strong>Filename:</strong> SeqActivityTrackingAccess.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * This interface provides common accessor methods to the nodes of an
 * activity tree.  During the internal sequencing processes, the sequencer may
 * request information from activities in the current context (the currently
 * active branch of the activity tree) to perform sequencing behaviors.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * It is the responsibility of the requester to utilize any information
 * provided to establish sequencing behaviors as described in the IMS SS
 * Specification.<br><br>
 * 
 * <strong>Known Problems:</strong><br>
 * 
 * <strong>Side Effects:</strong><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS 1.0</li>
 *     <li>SCORM 2004 4th Edition</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
abstract class SeqActivityTrackingAccess
{

   /**
    * Retrieves the attempt status of this activity.
    * 
    * @return <code>true</code> if the activity has been attempted, otherwise,
    *         <code>false</code>.
    */
   abstract boolean getActivityAttempted();


   /**
    * Retrieves the current attempt's progress status.
    *
    * @param iRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the current attempt on the activity is
    *         completed, otherwise <code>false</code>.
    */
   abstract boolean getAttemptCompleted(boolean iRetry);


   /**
    * Set the current attempt's progress status to the desired value.<br><br>
    * Valid values are: <code>unknown</code>, <code>completed</code>, and
    * <code>incomplete</code>.
    * 
    * @param iProgress New value for the attempt's progress status.
    *
    * @return <code>true</code> if the progress status of the activty changed,
    *         otherwise <code>false</code>.
    */
   abstract boolean setProgress(String iProgress);

   /**
    * Determines if the current attempt's progress status is valid.
    * 
    * @param iRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the progress status of the current attempt
    *         is valid, otherwise <code>false</code>.
    */
   abstract boolean getProgressStatus(boolean iRetry);

   /**
    * Retreives the designated objective's minimum measure value.<br><br>
    * 
    * @param iObjID ID of the objective whose minimum measure is desired.
    * 
    * @return The measure of the designated objective, or <code>-1.0</code>
    *         if no minimum measure is defined.
    */
   abstract double getObjMinMeasure(String iObjID);

   /**
    * Retreives the primary objective's minimum measure value.<br><br>
    * 
    * @return The measure of the designated objective, or <code>-1.0</code>
    *         if no minimum measure is defined.
    
    */
   abstract double getObjMinMeasure();

   /**
    * Indicates if the designated objective's measure value is valid.
    * 
    * @param iObjID   ID of the objective whose measure is desired.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the designated objective's measure is valid,
    *         otherwise <code>false</code>.
    */
   abstract boolean getObjMeasureStatus(String iObjID, boolean iIsRetry);

   /**
    * Indicates if the primary objective's measure value is valid.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    *
    * @return <code>true</code> if the designated objective's measure is valid,
    *         otherwise <code>false</code>.
    */
   abstract boolean getObjMeasureStatus(boolean iIsRetry);

   /**
    * Clears the value of the primary objective's measure.
    *
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    */
   abstract boolean clearObjMeasure();

   /**
    * Clears the value of the desintated objective's measure.
    * 
    * @param iObjID      ID of the objective whose measure has changed.
    *
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    */
   abstract boolean clearObjMeasure(String iObjID);

   /**
    * Sets the primary objective's measure to the desired value.
    * 
    * @param iMeasure    The value of the objective's measure.
    *
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    */
   abstract boolean setObjMeasure(double iMeasure);

   /**
    * Sets the designated objective's measure to the desired value.
    * 
    * @param iObjID      ID of the objective whose measure has changed.
    * 
    * @param iMeasure    The value of the objective's measure.
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    */
   abstract boolean setObjMeasure(String iObjID, double iMeasure);


   /**
    * Retreives the designated objective's measure value.<br><br>
    * <b>NOTE:</b> the value returned has no signifigance unless the
    * objective's measure status is <code>true</code>.
    * 
    * @param iObjID   ID of the objective whose measure is desired.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return The measure of the designated objective.
    */
   abstract double getObjMeasure(String iObjID, boolean iIsRetry);

   /**
    * Retreives the primary objective's measure value.<br><br>
    * <b>NOTE:</b> the value returned has no signifigance unless the
    * objective's measure status is <code>true</code>.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return The measure of the primary objective.
    */
   abstract double getObjMeasure(boolean iIsRetry);

   /**
    * Determines if the designated objective's progress status is valid.
    * 
    * @param iObjID   ID of the objective.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the designated objective's progress status is
    *         valid, otherwise <code>false</code>.
    */
   abstract boolean getObjStatus(String iObjID, boolean iIsRetry);

   /**
    * Determines if the primary objective's progress status is valid.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the primary objective's progress status is
    *         valid, otherwise <code>false</code>.
    */
   abstract boolean getObjStatus(boolean iIsRetry);

   /**
    * Set the designated objective's status to the desired value.
    * 
    * @param iObjID  ID of the objective whose satisfaction has changed.
    * 
    * @param iStatus New value for the objective's satisfaction status.<br><br>
    *                Valid values are: <code>unknown</code>,
    *                <code>satisfied</code>, or <code>notsatisfied</code>.
    *
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    */
   abstract boolean setObjSatisfied(String iObjID, String iStatus);

   /**
    * Set the primary objective's status to the desired value.
    * 
    * @param iStatus New value for the objective's satisfaction status.<br><br>
    *                Valid values are: <code>unknown</code>,
    *                <code>satisfied</code>, or <code>notsatisfied</code>.
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    */
   abstract boolean setObjSatisfied(String iStatus);

   /**
    * Retrieves the designated objective's status.
    * 
    * @param iObjID   ID of the objective.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return <code>true</code> if the designated objective is satisfied
    *         otherwise <code>false</code>.
    */
   abstract boolean getObjSatisfied(String iObjID, boolean iIsRetry);

   /**
    * Retrieves the primary objective's status.
    * 
    * @param iIsRetry Indicates if this evaluation is occuring during the
    *                 processing of a 'retry' sequencing request.
    *
    * @return <code>true</code> if the primary objective is satisfied
    *         otherwise <code>false</code>.
    */
   abstract boolean getObjSatisfied(boolean iIsRetry);

   /**
    * Determines if the activity's primary objective is satisfied by measure.
    * 
    * @return <code>true</code> if the primary objective is satisfied by
    *         measure, otherwise <code>false</code>
    */
   abstract boolean getObjSatisfiedByMeasure();

   /**
    * Sets the designated objective's experienced duration for the current
    * attempt.
    * 
    * @param iDur   The experienced duration of the current attempt on the
    *               activity.
    */
   abstract void setCurAttemptExDur(ADLDuration iDur);

   /**
    * Increment the attempt count for this activity by one.
    */
   abstract void incrementAttempt();

   /**
    * Reset this activity's attempt count.
    */ 
   abstract void resetNumAttempt();

   /**
    * Retrieve this activity's attempt count.
    * 
    * @return A <code>long</code> value indicating the number attempts on this
    *         activity.
    */
   abstract long getNumAttempt();

   /**
    * Sets the progress measure.
    * 
    * @param iProgressMeasure The progress measure.
    * 
    * @return If the status changed.
    */
   abstract boolean setProgressMeasure(String iProgressMeasure);
   
   /**
    * Indicates if this activity is completed by measure.
    * 
    * @return If this act is completed by measure.
    */
   abstract boolean getCompletedByMeasure();
   
   /**
    * Gets the progress measure weight.
    * 
    * @return The weight or 1.0 if there is none.
    */
   abstract double getProMeasureWeight();
   
   /**
    * Gets the progress measure status.
    * 
    * @param iIsRetry Is this a retry?
    * 
    * @return true if there is a progress measure.
    */
   abstract boolean getProMeasureStatus(boolean iIsRetry);
   
   /**
    * Gets the progress measure.
    * 
    * @param iIsRetry Indicates if this is a retry.
    * 
    * @return The measure.
    */
   abstract double getProMeasure(boolean iIsRetry);
   
   /**
    * Sets progress measure.
    * 
    * @param iProMeasure The progress measure.
    */
   abstract void setProMeasure(double iProMeasure);
   
   /**
    * Clears the progress measure.
    */
   abstract void clearProMeasure();
   
   /**
    * Stores whether this activity's progress is determined by measure.
    * 
    * @param iDeterminedByMeasure <code>true</code> if this activity's progress is determined by measure.
    */
   abstract void setProgressDeterminedByMeasure(boolean iDeterminedByMeasure);
   
   /**
    * Sets the progress threshold.
    * 
    * @param iThreshold The threshold.
    */
   abstract void setProgressThreshold(double iThreshold);
   
   /**
    * Sets the progress Weight.
    * 
    * @param iWeight The Weight.
    */
   abstract void setProgressWeight(double iWeight);

}  // end SeqActivityTrackingAcces
