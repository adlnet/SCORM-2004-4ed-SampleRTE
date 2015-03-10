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
 * Provides the mechanism to allow the RTE to communicate runtime activity
 * state and status information to the sequencer.<br><br>
 * 
 * <strong>Filename:</strong> SeqReportActivityStatus.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * An activity may report status and tracking information to the RTE, while it
 * is active.  It is the reponsibility of the RTE to communicate any state
 * and status information it recieves to the sequencer.  This ensures that the
 * state of the activity tree remains valid throughout a learner's session.
 * <br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE.<br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * These methods only apply to an active leaf activity (the current activity).
 * Invoking any of these methods commits the data and will affect validation of
 * potential sequencing requests.<br><br>
 *  
 * <strong>Known Problems:</strong><br><br>
 * 
 * <strong>Side Effects:</strong><br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS Specification
 *     <li>SCORM 2004 4th Edition
 * </ul>
 * 
 * @author ADL Technical Team
 */
public interface SeqReportActivityStatus
{

   /**
    * This method is used to inform the sequencer of the suspended state for the
    * current activity.  This state will take affect when the activity
    * terminates.
    * 
    * @param iID        ID of the activity whose suspended state is being set.
    * 
    * @param iSuspended Indicates if the activity is suspended (<code>true
    *                   </code>) or not (<code>false</code>).
    */
   void reportSuspension(String iID, boolean iSuspended);


   /**
    * This method is used to inform the sequencer of a change to an activity's
    * current attempt experienced duration.
    * 
    * @param iID    ID of the activity being affected.
    * 
    * @param iDur   Indicates the experienced duration of the current attempt.
    */
   void setAttemptDuration(String iID, ADLDuration iDur);


   /**
    * This method is used to inform the sequencer of a change to the activity's
    * progress status.
    * 
    * @param iID       ID of the activity whose progress status has changed.
    * 
    * @param iProgress New value for the activity's progress status.
    *                  Valid values are: 'unknown', 'completed', 'incomplete'.
    */
   void setAttemptProgressStatus(String iID, 
                                        String iProgress);


   /**
    * This method is used to inform the sequencer of a change to one of the
    * activity's objective's measures.
    * 
    * @param iID      ID of the activity whose measure has changed.
    * 
    * @param iObjID   ID of the objective whose measure has changed.
    * 
    * @param iMeasure New value for the objective's measure.
    */
   void setAttemptObjMeasure(String iID,
                                    String iObjID,
                                    double iMeasure);

   /**
    * This method is used to inform the sequencer to clear one of the
    * activity's objective's measures -- set it to 'unknown'.
    * 
    * @param iID    ID of the activity whose measure has changed.
    * 
    * @param iObjID ID of the objective whose measure has changed.
    */
   void clearAttemptObjMeasure(String iID,
                                      String iObjID);

   /**
    * This method is used to inform the sequencer of a change to one of the
    * activity's objective's satisfaction statuses.
    * 
    * @param iID     ID of the activity whose status has changed.
    * 
    * @param iObjID  ID of the objective whose satisfaction has changed.
    * 
    * @param iStatus New value for the objective's satisfaction status.
    *                Valid values are 'unknown', 'satisfied, 'notsatisfied'.
    */
   void setAttemptObjSatisfied(String iID,
                                      String iObjID,
                                      String iStatus);



}  // end SeqReportActivityStatus
