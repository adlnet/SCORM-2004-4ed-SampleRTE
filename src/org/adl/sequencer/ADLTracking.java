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

import org.adl.util.debug.DebugIndicator;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Encapsulation of information tracked for each attempt at an activity.<br><br>
 * 
 * <strong>Filename:</strong> ADLTracking.java<br>
 *
 * <strong>Description:</strong><br>
 * An <code>ADLTracking</code> encapsulates the information required by the
 * sequencer to track status for each new attempt on an activity.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE.<br>
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
public class ADLTracking implements Serializable
{

   /**
    * Enumeration of possible values for tracking elements  -- described in 
    * Tracking Model elements 2.1 and 2.2 of the IMS SS Specification.
    * <br>unknown
    * <br><b>"unknown"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String TRACK_UNKNOWN            = "unknown";

   /**
    * Enumeration of possible values for tracking elements  -- described in 
    * Tracking Model elements 2.1 and 2.2 of the IMS SS Specification.
    * <br>satisfied
    * <br><b>"satisfied"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]           
    */
   public static String TRACK_SATISFIED          = "satisfied";

   /**
    * Enumeration of possible values for tracking elements  -- described in 
    * Tracking Model elements 2.1 and 2.2 of the IMS SS Specification.
    * <br>notSatisfied
    * <br><b>"notSatisfied"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String TRACK_NOTSATISFIED       = "notSatisfied";

   /**
    * Enumeration of possible values for tracking elements  -- described in 
    * Tracking Model elements 2.1 and 2.2 of the IMS SS Specification.
    * <br>completed
    * <br><b>"completed"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String TRACK_COMPLETED          = "completed";

   /**
    * Enumeration of possible values for tracking elements  -- described in 
    * Tracking Model elements 2.1 and 2.2 of the IMS SS Specification.
    * <br>incomplete
    * <br><b>"incomplete"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String TRACK_INCOMPLETE         = "incomplete";


   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * Indicates if the recorded Progress status is invalid
    */
   public boolean mDirtyPro = false;


   /**
    * The objectives associated with this activity
    */
   public Hashtable mObjectives = null;

   /**
    * Describes the ID for the objective that contributes to rollup.
    */
   public String mPrimaryObj = "_primary_";

   /**
    * The progress tracking status.
    */
   //private String mProgress = ADLTracking.TRACK_UNKNOWN;

   /**
    * Indicates whether completion progress should be evaluated
    * via measure threshold.
    */
   private boolean mProgressDeterminedByMeasure = false;
   
   /**
    * The required threshold for completion.
    */
   private double mProgressThreshold = 1.0;
   
   /**
    * The weight of the progress measure.
    */
   private double mProgressWeight = 1.0;

   /** 
    * This describes the activity's absolute duration.<br>
    * Tracking element 1.2.2 Element 4
    */
   public ADLDuration mAttemptAbDur = null;

   /** 
    * This describes the activity's experienced duration.<br>
    * Tracking element 1.2.2 Element 5
    */
   public ADLDuration mAttemptExDur = null;

   /**
    * Represents the attempt number.
    */
   public long mAttempt = 0;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Initializes tracking status information for this attempt on the
    * associated activity.
    * 
    * @param iObjs      A list of local Objectives (<code>SeqObjective</code>).
    * 
    * @param iLearnerID Identifies the learner this tracking information is
    *                   related to.
    * 
    * @param iScopeID   Identifies the scope this tracking information applies
    */
   ADLTracking(Vector iObjs, String iLearnerID, String iScopeID) 
   {

      if ( iObjs != null )
      {

         for ( int i = 0; i < iObjs.size(); i++ )
         {
            SeqObjective obj = (SeqObjective)iObjs.elementAt(i);

            if ( _Debug )
            {
               System.out.println("  ::--> Building Objective  :: " 
                                  + obj.mObjID);
            }

            // Construct an objective for each local objective
            SeqObjectiveTracking objTrack = 
            new SeqObjectiveTracking(obj, iLearnerID, iScopeID);


            // If the objective is defined, add it to the set of objectives
            // associated with this activity
            if ( mObjectives == null )
            {
               mObjectives = new Hashtable();
            }

            mObjectives.put(obj.mObjID, objTrack);

            // Remember if this objective contributes to rollup
            if ( obj.mContributesToRollup )
            {
               mPrimaryObj = obj.mObjID;
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Making default Obj");
         }

         // All activities must have at least one objective and that objective
         // is the primary objective

         SeqObjective def = new SeqObjective();
         def.mContributesToRollup = true;

         SeqObjectiveTracking objTrack =
         new SeqObjectiveTracking(def, iLearnerID, iScopeID);

         if ( mObjectives == null )
         {
            mObjectives = new Hashtable();
         }

         mObjectives.put(def.mObjID, objTrack);

         mPrimaryObj = def.mObjID;
      }
   }
   
   /**
    * Initializes tracking status information for this attempt on the
    * associated activity.
    * 
    * @param iObjs      A list of local Objectives (<code>SeqObjective</code>).
    * 
    * @param iLearnerID Identifies the learner this tracking information is
    *                   related to.
    * 
    * @param iScopeID   Identifies the scope this tracking information applies
    *
    * @param iThreshold Identifies the threshold of measured progress required for
    *                   completion
    * 
    * @param iWeight    Identifies the weight of measured progress required for
    *                   completion
    */
   ADLTracking(Vector iObjs, String iLearnerID, String iScopeID, double iThreshold, double iWeight) 
   {
      this(iObjs, iLearnerID, iScopeID);      
      
      // Activity completion determined via threshold evaluation
      mProgressDeterminedByMeasure = true;
      mProgressThreshold = iThreshold;
      mProgressWeight = iWeight;
   }


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * This method provides the state this <code>ADLTracking</code> object for
    * diagnostic purposes.<br>
    */
   public void dumpState()
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLTracking   --> BEGIN - dumpState");

         System.out.println("\t  ::--> Attempt #:   " + mAttempt);
         System.out.println("\t  ::--> Dirty Pro:   " + mDirtyPro);

         if ( mObjectives == null )
         {
            System.out.println("\t  ::--> Objectives :       NULL");
         }
         else
         {

            System.out.println("\t  ::--> Objectives :       [" + 
                               mObjectives.size() + "]");

            Enumeration theEnum = mObjectives.keys();

            while ( theEnum.hasMoreElements() )
            {
               String key = (String)theEnum.nextElement();

               System.out.println("\t\t  :: " + key + " ::");

               SeqObjectiveTracking obj = 
               (SeqObjectiveTracking)mObjectives.get(key);

               System.out.println("\t\t  ::--> " + 
                                  obj.getObjStatus(false));
               System.out.println("\t\t  ::--> " + 
                                  obj.getObjMeasure(false));
            }

         }

         System.out.println("\t  ::--> Primary:       " + mPrimaryObj);  
         
         System.out.println("\t  ::--> ProgByMeasure: " + mProgressDeterminedByMeasure);
         System.out.println("\t  ::--> ProgThresh:    " + mProgressThreshold);
         System.out.println("\t  ::--> ProgWeight:    " + mProgressWeight);

         System.out.println("  :: ADLTracking   --> END   - dumpState");
      }
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Package Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
     * Indicates that the current Objective state is invalid due to a new
     * attempt on the activity's parent.
     */
   void setDirtyObj()
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLTracking     --> BEGIN - " +
                            "setDirtyObj");
      }

      if ( mObjectives != null )
      {

         Enumeration theEnum = mObjectives.keys();

         while ( theEnum.hasMoreElements() )
         {
            String key = (String)theEnum.nextElement();

            SeqObjectiveTracking obj =
            (SeqObjectiveTracking)mObjectives.get(key);

            obj.setDirtyObj();

         }
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLTracking     --> END   - " +
                            "setDirtyObj");
      }
   }
   
   /**
    * Sets the activity's Completion status.
    * 
    * @param iCompleted Desired completion status.   
    */
   void setCompletionStatus(String iCompleted)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLTracking   --> BEGIN - " +
         "setCompletionStatus");
         System.out.println("  ::-->  " + iCompleted);
      }

      // If the activity is only completed by measure, don't set its status
      if ( mProgressDeterminedByMeasure )
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Cannot set: Completion satisfied by " +
            "measure");
         }
      }
      else
      {
         // Find the primary objective
         SeqObjectiveTracking obj = (SeqObjectiveTracking)mObjectives.get(mPrimaryObj); 
      
         obj.setObjCompletionStatus(iCompleted);
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLTracking   --> END   - " +
         "setCompletionStatus");
      }
   }

   /**
    * Retrieves the activity's Completion status.
    * 
    * @return The Activity's current completion status.
    */
   String getCompletionStatus(boolean iUseCurrent)
   {
      boolean isDirty = (iUseCurrent)?false:mDirtyPro;
      String status = ADLTracking.TRACK_UNKNOWN;
      // Find the primary objective
      SeqObjectiveTracking obj = (SeqObjectiveTracking)mObjectives.get(mPrimaryObj); 

      if ( mProgressDeterminedByMeasure )
      {
         status = obj.getObjProgressMeasure(isDirty);
      }
      else
      {
         status = obj.getObjCompletionStatus(isDirty);
      }
      
      return status;
   }

   /**
    * Sets an activity's progress measure.  If a completion threshold is defined, 
    * the activity's status may be set based on a threshold evaluation.
    * 
    * @param iProMeasure Desired progress measure
    * 
    */
   void setProgressMeasure(double iProMeasure)             
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLTracking   --> BEGIN - setProgressMeasure");
         System.out.println("  ::-->  " + iProMeasure);
      }

      // Find the primary objective
      SeqObjectiveTracking obj = (SeqObjectiveTracking)mObjectives.get(mPrimaryObj); 

      obj.setObjProgressMeasure(iProMeasure);

      // If objective status is determined by measure, set it
      if ( mProgressDeterminedByMeasure && (iProMeasure >= 0.0 && iProMeasure <= 1.0) )
      {
         String completion = ADLTracking.TRACK_UNKNOWN;

         if ( iProMeasure >= mProgressThreshold )
         {
            completion = ADLTracking.TRACK_COMPLETED;
         }
         else
         {
            completion = ADLTracking.TRACK_INCOMPLETE;
         }
         
         obj.setObjCompletionStatus(completion);
      }


      if ( _Debug )
      {
         System.out.println("  :: ADLTracking   --> END - " + 
         "setProMeasure");
      }
   }

   /**
    * Retrieves the activity's progress measure.
    * 
    * @return The Activity's current progress measure.
    */
   double getProgressMeasure()
   {
     // Get the status stored in the primary objective
     SeqObjectiveTracking obj = (SeqObjectiveTracking)mObjectives.get(mPrimaryObj); 

     double progress = Double.parseDouble(obj.getObjProgressMeasure(mDirtyPro));

     return progress;
   }
   
   /**
    * Gets if Progress is Determined By Measure
    * @return Progress is Determined By Measure
    */
   boolean getProgressDeterminedByMeasure()
   {
      return mProgressDeterminedByMeasure;
   }

   /**
    * Gets the progress measure weight.
    * 
    * @return The progress measure weight.
    */
   double getProgressMeasureWeight()
   {
      return mProgressWeight;
   }
   
   /**
    * Sets the progress measure weight.
    * 
    * @param iWeight The weight.
    */
   void setProgressMeasureWeight(double iWeight)
   {
      // 0 - 1 
      if ( iWeight >= 0.0 && iWeight <= 1.0 )
      {
         mProgressWeight = iWeight;
      }
   }
   
   void setProgressMeasureThreshold(double iThresh)
   {
      if ( iThresh >= 0.0 && iThresh <= 1.0 )
      {
         mProgressThreshold = iThresh;
      }
   }
   
   /**
    * Indicates if there is a progress measure.
    * 
    * @return <code>true</code> if there is a progress measure.
    */
   boolean hasProgressMeasure()
   {

     boolean ret = false;

     // Get the status stored in the primary objective
     SeqObjectiveTracking obj = (SeqObjectiveTracking)mObjectives.get(mPrimaryObj); 
     
     String progress = obj.getObjProgressMeasure(mDirtyPro);
     
     if ( !progress.equals(ADLTracking.TRACK_UNKNOWN) )
     {
        ret = true;
     }

     return ret;
   }

   /**
    * Clears the progress measure.
    */
   void clearProMeasure()
   {
      SeqObjectiveTracking obj = (SeqObjectiveTracking)mObjectives.get(mPrimaryObj);
      obj.clearObjProgressMeasure();
   }

   public double getProgressThreshold()
   {
      return mProgressThreshold;
   }

}  // end ADLTracking
