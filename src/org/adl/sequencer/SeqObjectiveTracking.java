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

import java.io.Serializable;
import java.util.Vector;

import org.adl.util.debug.DebugIndicator;

/**
 * Encapsulation mastery status tracking and behavior.<br><br>
 * 
 * <strong>Filename:</strong> SeqObjectiveTracking.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>SeqObjectiveTracking</code> encapsulates the objective tracking
 * information for one objective as described in the SS Tracking  Model
 * (TM) section.  This class provides seamless access to both local and global
 * objectives and provides for measure-based mastery evaluation.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br><br>
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
public class SeqObjectiveTracking implements Serializable
{
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * Identifies the learner with which this objective is associated.
    */
   private String mLearnerID = null;

   /**
    * Identifies the scope with which this objective is associated.
    */
   private String mScopeID = null;

   /**
    * Identifies the objective being tracked.
    */
   private SeqObjective mObj = null;

   /**
    * Indicates if the recorded Objective status is invalid
    */
   private boolean mDirtyObj = false;

   /** 
    * Indicates that the an objective set is unconditionally allowed
    */
   private boolean mSetOK = false;

   /**
    * Indicates if this objective has valid satisfaction data
    */
   private boolean mHasSatisfied = false;

   /**
    * This objective's satisfied status.
    */
   private boolean mSatisfied = false;

   /**
    * Indicates if this objective has a valid measure.
    */
   private boolean mHasMeasure = false;

   /**
    * This objective's measure.<br><br>
    * Valid range: <code>[-1.0..1.0]</code>
    */
   private double mMeasure = 0.0;
   
   /** 
    * Indicates if this objective has a known raw measure.
    */
   private boolean mHasRawScore = false;
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid type: <code>real(10,7)</code>
    */
   private double mRawScore = 0;
   
   /** 
    * Indicates if this objective has a known min score.
    */
   private boolean mHasMinScore = false;
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid type: <code>real(10,7)</code>
    */
   private double mMinScore = 0;
   
   /** 
    * Indicates if this objective has a known max score.
    */
   private boolean mHasMaxScore = false;
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid type: <code>real(10,7)</code>
    */
   private double mMaxScore = 0;
   
   /** 
    * Indicates if this objective has a known progress measure.
    */
   private boolean mHasProgressMeasure = false;
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid range: <code>[0.0, 1.0]</code>
    */
   private double mProgressMeasure = 0.0;
   
   /** 
    * Indicates if this objective has a known completion status.
    */
   private boolean mHasCompletionStatus = false;
   
   /**
    * Indicates the measure known for this objective.<br><br>
    * Valid vocab: <code>completed, incomplete, not attempted,
    *  unknown</code>
    */
   private String mCompletionStatus = "unknown";

   /**
    * Indicates the unique global objective where satisfied status is read.
    */
   private String mReadStatus = null;

   /**
    * Indicates the unique global objective where measure is read.
    */
   private String mReadMeasure = null;

   /**
    * Indicates the unique global objective where raw score is read.
    */
   private String mReadRawScore = null;
   
   /**
    * Indicates the unique global objective where min score is read.
    */
   private String mReadMinScore = null;
   
   /**
    * Indicates the unique global objective where max score is read.
    */
   private String mReadMaxScore = null;
   
   /**
    * Indicates the unique global objective where completion status is read.
    */
   private String mReadCompletionStatus = null;
   
   /**
    * Indicates the unique global objective where progress measure is read.
    */
   private String mReadProgressMeasure = null;
   
   /**
    * Indicates the set of global objectives that receive satisfied status
    */
   private Vector mWriteStatus = null;

   /**
    * Indicates the set of global objectives that receive measure
    */
   private Vector mWriteMeasure = null;
   
   /**
    * Indicates the set of global objectives that receive raw score
    */
   private Vector mWriteRawScore = null;
   
   /**
    * Indicates the set of global objectives that receive min score
    */
   private Vector mWriteMinScore = null;
   
   /**
    * Indicates the set of global objectives that receive max score
    */
   private Vector mWriteMaxScore = null;
   
   /**
    * Indicates the set of global objectives that receive completion status
    */
   private Vector mWriteCompletionStatus = null;
   
   /**
    * Indicates the set of global objectives that receive progress measure
    */
   private Vector mWriteProgressMeasure = null;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

    Constructors 

   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Initializes the objective tracking information for one objective.
    * 
    * @param iObj       The objective being tracked.
    * 
    * @param iLearnerID The learner this objective is associated with.
    * 
    * @param iScopeID   The scope to which this objective can be resolved.
    *
    */
   public SeqObjectiveTracking(SeqObjective iObj, 
         String iLearnerID,
         String iScopeID)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking --> BEGIN -  " +
         "constructor");
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
      }

      if ( iObj != null )
      {

         if ( _Debug )
         {
            System.out.println("  ::--> Objective ID : " + 
                  iObj.mObjID);
         }

         mObj = iObj;
         mLearnerID = iLearnerID;
         mScopeID = iScopeID;

         if ( iObj.mMaps != null )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Setting up obj maps");
            }

            for ( int i = 0; i < mObj.mMaps.size(); i++ )
            {
               SeqObjectiveMap map = (SeqObjectiveMap)mObj.mMaps.elementAt(i);

               if ( map.mReadStatus )
               {
                  mReadStatus = map.mGlobalObjID;
               }

               if ( map.mReadMeasure )
               {
                  mReadMeasure = map.mGlobalObjID;
               }
               
               if ( map.mReadRawScore )
               {
                  mReadRawScore = map.mGlobalObjID;
               }
               
               if ( map.mReadMinScore )
               {
                  mReadMinScore = map.mGlobalObjID;
               }
               
               if ( map.mReadMaxScore )
               {
                  mReadMaxScore = map.mGlobalObjID;
               }
               
               if ( map.mReadCompletionStatus )
               {
                  mReadCompletionStatus = map.mGlobalObjID;
               }
               
               if ( map.mReadProgressMeasure )
               {
                  mReadProgressMeasure = map.mGlobalObjID;
               }

               if ( map.mWriteStatus )
               {
                  if ( mWriteStatus == null )
                  {
                     mWriteStatus = new Vector();
                  }

                  mWriteStatus.add(map.mGlobalObjID);
               }

               if ( map.mWriteMeasure )
               {
                  if ( mWriteMeasure == null )
                  {
                     mWriteMeasure = new Vector();
                  }

                  mWriteMeasure.add(map.mGlobalObjID);
               }
               
               if ( map.mWriteRawScore )
               {
                  if ( mWriteRawScore == null )
                  {
                     mWriteRawScore = new Vector();
                  }

                  mWriteRawScore.add(map.mGlobalObjID);
               }
               
               if ( map.mWriteMinScore )
               {
                  if ( mWriteMinScore == null )
                  {
                     mWriteMinScore = new Vector();
                  }

                  mWriteMinScore.add(map.mGlobalObjID);
               }
               
               if ( map.mWriteMaxScore )
               {
                  if ( mWriteMaxScore == null )
                  {
                     mWriteMaxScore = new Vector();
                  }

                  mWriteMaxScore.add(map.mGlobalObjID);
               }
               
               if ( map.mWriteCompletionStatus )
               {
                  if ( mWriteCompletionStatus == null )
                  {
                     mWriteCompletionStatus = new Vector();
                  }

                  mWriteCompletionStatus.add(map.mGlobalObjID);
               }
               
               if ( map.mWriteProgressMeasure )
               {
                  if ( mWriteProgressMeasure == null )
                  {
                     mWriteProgressMeasure = new Vector();
                  }

                  mWriteProgressMeasure.add(map.mGlobalObjID);
               }
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : No associated objective");
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking --> END   -  " +
         "constructor");
      }
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

    Package Methods 

   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Get the objective ID of this objective.
    * 
    * @return The ID (<code>String</code>) of this objective.
    */
   String getObjID()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "getObjID");
         System.out.println("  ::-->  " + mObj.mObjID);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
         "getObjID");
      }

      return mObj.mObjID;
   }


   /**
    * Get the objective definition for this objective status record
    * 
    * @return The objective (<code>SeqObjective</code>) for this objective
    *         status record.
    */
   SeqObjective getObj()
   {
      return mObj;
   }

   /**
    * Sets the activity's objective status as determined by rollup.  This method
    * only applies to measure rollup.
    * 
    * @param iSatisfied Desired objective status.   
    */
   void forceObjStatus(String iSatisfied)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "forceObjStatus");
         System.out.println("  ::-->  " + iSatisfied);
      }


      if ( iSatisfied.equals(ADLTracking.TRACK_UNKNOWN) )
      {
         clearObjStatus();
      }
      else
      {
         // Set any global objectives
         if ( mWriteStatus != null )
         {
            for ( int i = 0; i < mWriteStatus.size(); i++ )
            {
               String objID = (String)mWriteStatus.elementAt(i);

               ADLSeqUtilities.setGlobalObjSatisfied(objID, 
                     mLearnerID,
                     mScopeID,
                     iSatisfied);
            }
         }

         mHasSatisfied = true;

         if ( iSatisfied.equals(ADLTracking.TRACK_SATISFIED) )
         {
            mSatisfied = true;
         }
         else
         {
            mSatisfied = false;
         }
      }


      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "forceObjStatus");
      }
   }

   /**
    * Sets the activity's objective status.
    * 
    * @param iSatisfied Desired objective status.   
    */
   void setObjStatus(String iSatisfied)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "setObjStatus");
         System.out.println("  ::-->  " + mObj.mObjID);
         System.out.println("  ::-->  " + iSatisfied);
      }

      // If the objective is only satisfied by measure, don't set its status
      if ( mObj.mSatisfiedByMeasure && !mSetOK )
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Cannot set: Objective satisfied by " +
            "measure");
         }
      }
      else
      {
         if ( iSatisfied.equals(ADLTracking.TRACK_UNKNOWN) )
         {
            clearObjStatus();
         }
         else
         {

            // Set any global objectives
            if ( mWriteStatus != null )
            {
               for ( int i = 0; i < mWriteStatus.size(); i++ )
               {
                  String objID = (String)mWriteStatus.elementAt(i);

                  ADLSeqUtilities.setGlobalObjSatisfied(objID, 
                        mLearnerID,
                        mScopeID,
                        iSatisfied);
               }
            }

            mHasSatisfied = true;

            if ( iSatisfied.equals(ADLTracking.TRACK_SATISFIED) )
            {
               mSatisfied = true;
            }
            else
            {
               mSatisfied = false;
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "setObjStatus");
      }
   }

   /**
    * Clears the recorded objective status.
    *
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    */
   boolean clearObjStatus()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "clearObjStatus");
      }

      boolean statusChange = false;

      if ( mHasSatisfied )
      {

         if ( mObj.mSatisfiedByMeasure )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Cannot clear: Objective " +
               "satisfied by measure");
            }
         }
         else
         {

            // Clear any global objectives
            if ( mWriteStatus != null )
            {
               for ( int i = 0; i < mWriteStatus.size(); i++ )
               {
                  String objID = (String)mWriteStatus.elementAt(i);

                  ADLSeqUtilities.
                  setGlobalObjSatisfied(objID, 
                        mLearnerID, 
                        mScopeID,
                        ADLTracking.TRACK_UNKNOWN);
               }
            }

            // Clear the satisfaction status
            mHasSatisfied = false;
            statusChange = true;
         }
      }


      if ( _Debug )
      {
         System.out.println("  :: has satisfied --> " + mHasSatisfied);
         System.out.println("  :: status change --> " + statusChange);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "clearObjStatus");
      }

      return statusChange;
   }

   /**
    * Clears the recorded measure for this objective.
    * 
    * @param iAffectSatisfaction
    *                 Indicates if the minNormalizedMeasure should
    *                 be evaluated
    * 
    * @return <code>true</code> if the satisfaction of the objective changed,
    *         otherwise <code>false</code>.
    */
   boolean clearObjMeasure(boolean iAffectSatisfaction)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "clearObjMeasure");
         System.out.println("  ::--> " + iAffectSatisfaction);
      }

      boolean statusChange = false;

      if ( mHasMeasure )
      {
         // Clear any global objectives
         if ( mWriteMeasure != null )
         {
            for ( int i = 0; i < mWriteMeasure.size(); i++ )
            {
               String objID = (String)mWriteMeasure.elementAt(i);

               ADLSeqUtilities.setGlobalObjMeasure(objID, 
                     mLearnerID,
                     mScopeID,
                     ADLTracking.TRACK_UNKNOWN);
            }
         }

         // Clear the measure
         mHasMeasure = false;

         // If measure is used to determine status, status is also cleared
         if ( iAffectSatisfaction )
         {
            forceObjStatus(ADLTracking.TRACK_UNKNOWN);
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + statusChange);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
         "clearObjMeasure");
      }

      return statusChange;
   }
   
   /**
    * Clears the recorded raw score.
    *
    * @return <code>true</code> if the raw score of the objective changed,
    *         otherwise <code>false</code>.
    */
   boolean clearObjRawScore()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "clearObjRawScore");
      }

      boolean statusChange = false;
      
      if ( mHasRawScore )
      {
         // Clear any global objectives
         if ( mWriteRawScore != null )
         {
            for ( int i = 0; i < mWriteRawScore.size(); i++ )
            {
               String objID = (String)mWriteRawScore.elementAt(i);

               ADLSeqUtilities.
               setGlobalObjRawScore(objID, 
                     mLearnerID, 
                     mScopeID,
                     ADLTracking.TRACK_UNKNOWN);
            }
         }

         // Clear the satisfaction status
         mHasRawScore = false;
         statusChange = true;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + statusChange);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "clearObjRawScore");
      }

      return statusChange;
   }
   
   /**
    * Clears the recorded min score.
    *
    * @return <code>true</code> if the min score of the objective changed,
    *         otherwise <code>false</code>.
    */
   boolean clearObjMinScore()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "clearObjMinScore");
      }

      boolean statusChange = false;
      
      if ( mHasMinScore )
      {
         // Clear any global objectives
         if ( mWriteMinScore != null )
         {
            for ( int i = 0; i < mWriteMinScore.size(); i++ )
            {
               String objID = (String)mWriteMinScore.elementAt(i);

               ADLSeqUtilities.
               setGlobalObjMinScore(objID, 
                     mLearnerID, 
                     mScopeID,
                     ADLTracking.TRACK_UNKNOWN);
            }
         }

         // Clear the satisfaction status
         mHasMinScore = false;
         statusChange = true;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + statusChange);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "clearObjMinScore");
      }

      return statusChange;
   }
   
   /**
    * Clears the recorded max score.
    *
    * @return <code>true</code> if the max score of the objective changed,
    *         otherwise <code>false</code>.
    */
   boolean clearObjMaxScore()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "clearObjMaxScore");
      }

      boolean statusChange = false;
      
      if ( mHasMaxScore )
      {
         // Clear any global objectives
         if ( mWriteMaxScore != null )
         {
            for ( int i = 0; i < mWriteMaxScore.size(); i++ )
            {
               String objID = (String)mWriteMaxScore.elementAt(i);

               ADLSeqUtilities.
               setGlobalObjMaxScore(objID, 
                     mLearnerID, 
                     mScopeID,
                     ADLTracking.TRACK_UNKNOWN);
            }
         }

         // Clear the satisfaction status
         mHasMaxScore = false;
         statusChange = true;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + statusChange);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "clearObjMaxScore");
      }

      return statusChange;
   }
   
   /**
    * Clears the recorded progress measure.
    *
    * @return <code>true</code> if the progress of the objective changed,
    *         otherwise <code>false</code>.
    */
   boolean clearObjProgressMeasure()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "clearObjProgressMeasure");
      }

      boolean statusChange = false;
      
      if ( mHasProgressMeasure )
      {
         // Clear any global objectives
         if ( mWriteProgressMeasure != null )
         {
            for ( int i = 0; i < mWriteProgressMeasure.size(); i++ )
            {
               String objID = (String)mWriteProgressMeasure.elementAt(i);

               ADLSeqUtilities.
               setGlobalObjProgressMeasure(objID, 
                     mLearnerID, 
                     mScopeID,
                     ADLTracking.TRACK_UNKNOWN);
            }
         }

         // Clear the satisfaction status
         mHasProgressMeasure = false;
         statusChange = true;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + statusChange);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "clearObjProgressMeasure");
      }

      return statusChange;
   }
   
   /**
    * Clears the recorded completion status.
    *
    * @return <code>true</code> if the completion status of the objective changed,
    *         otherwise <code>false</code>.
    */
   boolean clearObjCompletionStatus()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "clearObjCompletionStatus");
      }

      boolean statusChange = false;
      
      if ( mHasCompletionStatus )
      {
         // Clear any global objectives
         if ( mWriteCompletionStatus != null )
         {
            for ( int i = 0; i < mWriteCompletionStatus.size(); i++ )
            {
               String objID = (String)mWriteCompletionStatus.elementAt(i);

               ADLSeqUtilities.
               setGlobalObjCompletion(objID, 
                     mLearnerID, 
                     mScopeID,
                     ADLTracking.TRACK_UNKNOWN);
            }
         }

         // Clear the satisfaction status
         mHasCompletionStatus = false;
         statusChange = true;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + statusChange);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "clearObjCompletionStatus");
      }

      return statusChange;
   }

   /**
    * Sets an activity's measure and compares the new measure with a defined
    * minimum measure, if one exists. The objectives's status may be set
    * based on this comparison.
    * 
    * @param iMeasure Desired measure
    * 
    * @param iAffectSatisfaction
    *                 Indicates if the minNormalizedMeasure should
    *                 be evaluated
    */
   void setObjMeasure(double iMeasure, boolean iAffectSatisfaction)             
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "setObjMeasure");
         System.out.println("  ::-->  " + iMeasure);
         System.out.println("  ::-->  " + iAffectSatisfaction);
      }

      // Validate the range of the measure
      if ( iMeasure < -1.0 || iMeasure > 1.0 )
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Invalid Measure: " + iMeasure);
            System.out.println("  ::--> Assume 'Unknown'");
         }

         clearObjMeasure(iAffectSatisfaction);
      }
      else
      {
         mHasMeasure = true;
         mMeasure = iMeasure;

         // Set any global objectives
         if ( mWriteMeasure != null )
         {
            for ( int i = 0; i < mWriteMeasure.size(); i++ )
            {
               String objID = (String)mWriteMeasure.elementAt(i);

               ADLSeqUtilities.
               setGlobalObjMeasure(objID, 
                     mLearnerID,
                     mScopeID,
                     (new Double(iMeasure)).toString());
            }
         }

         // If objective status is determined by measure, set it
         if ( iAffectSatisfaction )
         {
            if ( mMeasure >= mObj.mMinMeasure )
            {
               forceObjStatus(ADLTracking.TRACK_SATISFIED);
            }
            else
            {
               forceObjStatus(ADLTracking.TRACK_NOTSATISFIED);
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> END - " + 
         "setObjMeasure");
      }
   }

   /**
    * Retrieves the objective's status.
    * 
    * @param iIsRetry Indicates if this evaluation is occurring during the
    *                 processing of a 'retry' sequencing request.
    * 
    * @return The objective's status -- <code>unknown, true, false</code>.
    */
   String getObjStatus(boolean iIsRetry)
   {
      return getObjStatus(iIsRetry, false);
   }

   /**
    * Retrieves the objective's status.
    * 
    * @param iIsRetry  Indicates if this evaluation is occurring during the
    *                  processing of a 'retry' sequencing request.
    * 
    * @param iUseLocal Indicates if only the local status should be
    *                  returned.
    * 
    * @return The objective's status -- <code>unknown, true, false</code>.
    */
   String getObjStatus(boolean iIsRetry, boolean iUseLocal)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "getObjStatus");
         System.out.println("  ::  LOCAL --> " + iUseLocal);
      }

      String ret = ADLTracking.TRACK_UNKNOWN;
      boolean done = false;

      // if satisfied by measure, ensure that it has been set if a measure is
      // available.
      if ( mObj.mSatisfiedByMeasure )
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Only using Measure +---><---+  ");
         }

         done = true;
         String measure = null;

         // Is there a 'read' objective map?
         if ( mReadMeasure != null )
         {

            if ( _Debug )
            {
               System.out.println("  ::-->  Looking at shared measure");
            }

            measure =
               ADLSeqUtilities.getGlobalObjMeasure(mReadMeasure, 
                     mLearnerID,
                     mScopeID);
         }


         if ( mHasMeasure && measure == null )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Using local measure");
            }

            if ( mHasMeasure && !(iIsRetry && mDirtyObj) )
            {
               measure = (new Double(mMeasure)).toString();
            }
         }

         double val = -999.0;

         try
         {
            val = (new Double(measure)).doubleValue();
         }
         catch ( Exception e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: Bad measure value");
            }
         }

         // Validate the range of the measure
         if ( val < -1.0 || val > 1.0 )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR :  Invalid Measure: " + val);
            }
         }
         else
         {
            if ( val >= mObj.mMinMeasure )
            {
               ret = ADLTracking.TRACK_SATISFIED;
            }
            else
            {
               ret = ADLTracking.TRACK_NOTSATISFIED;
            }
         }
      }

      if ( !done )
      {
         boolean globalvalexists = false;
         // Is there a 'read' objective map?
         if ( mReadStatus != null )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Using shared status");
            } 

            // Retrieve shared competency mastery status
            String status =
               ADLSeqUtilities.getGlobalObjSatisfied(mReadStatus, 
                     mLearnerID,
                     mScopeID);
            if ( status != null )
            {
               ret = status;
               done = true;
               globalvalexists = true;
            }
         }

         if ( ! globalvalexists )
         {
            if ( mHasSatisfied && (!done || iUseLocal) )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Using local objective status");
               }

               if ( mHasSatisfied && !(iIsRetry && mDirtyObj) )
               {
                  if ( mSatisfied )
                  {
                     ret = ADLTracking.TRACK_SATISFIED;
                  }
                  else
                  {
                     ret = ADLTracking.TRACK_NOTSATISFIED;
                  }
               }
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + ret);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "getObjStatus");
      }

      return ret;
   }


   /**
    * Retrieves the Objective's measure.
    * 
    * @param iIsRetry Indicates if this evaluation is occurring during the
    *                 processing of a 'retry' sequencing request.
    *
    * @return The objective's measure (<code>[-1.0, 1.0]</code>), or <code>
    *         unknown</code>, if the objective does not have a valid measure.
    */
   String getObjMeasure(boolean iIsRetry)
   {
      return getObjMeasure(iIsRetry, false);
   }

   /**
    * Retrieves the Objective's measure.
    * 
    * @param iIsRetry  Indicates if this evaluation is occurring during the
    *                  processing of a 'retry' sequencing request.
    * 
    * @param iUseLocal Indicates if only the local status should be
    *                  returned.
    * 
    * @return The objective's measure (<code>[-1.0, 1.0]</code>), or <code>
    *         unknown</code>, if the objective does not have a valid measure.
    */
   String getObjMeasure(boolean iIsRetry, boolean iUseLocal)
   {

      // Do not assume there is a valid measure
      String ret = ADLTracking.TRACK_UNKNOWN;

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN   - " +
         "getObjMeasure");
         System.out.println("  ::  LOCAL --> " + iUseLocal);
      }

      boolean done = false;
      boolean globalvalexists = false;
      // Is there a 'read' objective map?
      if ( mReadMeasure != null )
      {

         if ( _Debug )
         {
            System.out.println("  ::-->  Looking at shared measure");
         }

         String measure =
            ADLSeqUtilities.getGlobalObjMeasure(mReadMeasure, 
                  mLearnerID,
                  mScopeID);

         // Always use shared measure if available
         if ( measure != null )
         {
            ret = measure;
            done = true;
            globalvalexists = true;
         }
      }


      if ( ! globalvalexists )
      {
         if ( mHasMeasure && (!done || iUseLocal) )
         {

            if ( _Debug )
            {
               System.out.println("  ::--> Using local measure");
            }

            if ( mHasMeasure && !(iIsRetry && mDirtyObj) )
            {
               ret = (new Double(mMeasure)).toString();
            }
         }
      }

      // If a global measure or a local min measure is defined,
      // test the threshold and set status
      if ( !ret.equals(ADLTracking.TRACK_UNKNOWN) &&
            mObj.mSatisfiedByMeasure && !(iIsRetry && mDirtyObj) )
      {

         double val = -999.0;

         try
         {
            val = (new Double(ret)).doubleValue();
         }
         catch ( Exception e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: Bad measure value");
            }
         }

         // Validate the range of the measure
         if ( val < -1.0 || val > 1.0 )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR :  Invalid Measure: "
                     + val);
            }
         }
         else
         {
            mSetOK = true;

            if ( val >= mObj.mMinMeasure )
            {
               setObjStatus(ADLTracking.TRACK_SATISFIED);
            }
            else
            {
               setObjStatus(ADLTracking.TRACK_NOTSATISFIED);
            }

            mSetOK = false;
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + ret);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
         "getObjMeasure");
      }

      return ret;
   }

   /**
    * Determines if the activity's objective is satisfied by measure.
    * 
    * @return <code>true</code> if the objective is satisfied by measure,
    *         otherwise <code>false</code>
    */
   boolean getByMeasure()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "getByMeasure");
      }

      boolean byMeasure = false;

      if ( mObj != null )
      {
         byMeasure = mObj.mSatisfiedByMeasure;
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
         "getByMeasure");
      }

      return byMeasure;
   }

   /**
    * Indicates that the current Objective state is invalid due to a new
    * attempt on the activity's parent.
    */
   void setDirtyObj()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking     --> BEGIN - " +
         "setDirtyObj");
      }

      mDirtyObj = true;

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking     --> END   - " +
         "setDirtyObj");
      }
   }

   /**
    * Retrieves the Objective's raw score.
    * 
    * @param iIsRetry Indicates if this evaluation is occurring during the
    *                 processing of a 'retry' sequencing request.
    *
    * @return The objective's raw score (<code>real(10,7)</code>), or <code>
    *         unknown</code>, if the objective does not have a valid raw score.
    */
   String getObjRawScore(boolean iIsRetry)
   {
      // Do not assume there is a valid raw score
      String ret = ADLTracking.TRACK_UNKNOWN;

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN   - " +
         "getObjRawScore");
         System.out.println("  ::  LOCAL --> " + false);
      }

      boolean done = false;
      boolean globalvalexists = false;
      // Is there a 'read' objective map?
      if ( mReadRawScore != null )
      {

         if ( _Debug )
         {
            System.out.println("  ::-->  Looking at shared raw score");
         }

         String rawscore =
            ADLSeqUtilities.getGlobalObjRawScore(mReadRawScore, 
                  mLearnerID,
                  mScopeID);

         // Always use shared raw score if available
         if ( rawscore != null )
         {
            ret = rawscore;
            done = true;
            globalvalexists = true;
         }
      }

      if ( ! globalvalexists )
      {
         if ( mHasRawScore && !done )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Using local raw score");
            }

            if ( mHasRawScore && !(iIsRetry && mDirtyObj) )
            {
               ret = Double.toString(mRawScore);
            }
         }
      }

      if ( !ret.equals(ADLTracking.TRACK_UNKNOWN) &&
             !(iIsRetry && mDirtyObj) )
      {
         try
         {
            Double.parseDouble(ret);
         }
         catch ( Exception e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: Bad raw score value");
               System.out.println("  ::-->  " + ret);
               System.out.println("  :: Returning " + ADLTracking.TRACK_UNKNOWN);
               System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
               "getObjRawScore");
            }

            return ADLTracking.TRACK_UNKNOWN;
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + ret);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
         "getObjRawScore");
      }

      return ret;
   }

   /**
    * Sets an activity's raw score.
    * 
    * @param iRawScore The raw score to be set.
    */
   void setObjRawScore(double iRawScore)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "setObjRawScore");
         System.out.println("  ::-->  " + iRawScore);
      }

      mHasRawScore = true;
      mRawScore = iRawScore;

      // Set any global objectives
      if ( mWriteRawScore != null )
      {
         for ( int i = 0; i < mWriteRawScore.size(); i++ )
         {
            String objID = (String)mWriteRawScore.elementAt(i);

            ADLSeqUtilities.
            setGlobalObjRawScore(objID, 
                  mLearnerID,
                  mScopeID,
                  Double.toString(iRawScore));
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> END - " + 
         "setObjRawScore");
      }
   }

   /**
    * Retrieves the Objective's min score.
    * 
    * @param iIsRetry Indicates if this evaluation is occurring during the
    *                 processing of a 'retry' sequencing request.
    *
    * @return The objective's min score (<code>real(10,7)</code>), or <code>
    *         unknown</code>, if the objective does not have a valid min score.
    */
   String getObjMinScore(boolean iIsRetry)
   {
      // Do not assume there is a valid min score
      String ret = ADLTracking.TRACK_UNKNOWN;

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN   - " +
         "getObjMinScore");
         System.out.println("  ::  LOCAL --> " + false);
      }

      boolean done = false;
      boolean globalvalexists = false;
      // Is there a 'read' objective map?
      if ( mReadMinScore != null )
      {
         if ( _Debug )
         {
            System.out.println("  ::-->  Looking at shared min score");
         }

         String minscore =
            ADLSeqUtilities.getGlobalObjMinScore(mReadMinScore, 
                  mLearnerID,
                  mScopeID);

         // Always use shared min score if available
         if ( minscore != null )
         {
            ret = minscore;
            done = true;
            globalvalexists = true;
         }
      }

      if ( ! globalvalexists )
      {
         if ( mHasMinScore && !done )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Using local min score");
            }

            if ( mHasMinScore && !(iIsRetry && mDirtyObj) )
            {
               ret = Double.toString(mMinScore);
            }
         }
      }

      if ( !ret.equals(ADLTracking.TRACK_UNKNOWN) &&
             !(iIsRetry && mDirtyObj) )
      {
         try
         {
            Double.parseDouble(ret);
         }
         catch ( Exception e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: Bad min score value");
               System.out.println("  ::-->  " + ret);
               System.out.println("  :: Returning " + ADLTracking.TRACK_UNKNOWN);
               System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
               "getObjMinScore");
            }

            return ADLTracking.TRACK_UNKNOWN;
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + ret);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
         "getObjMinScore");
      }

      return ret;
   }

   /**
    * Sets an activity's min score.
    * 
    * @param iMinScore The min score to be set.
    */
   void setObjMinScore(double iMinScore)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "setObjMinScore");
         System.out.println("  ::-->  " + iMinScore);
      }

      mHasMinScore = true;
      mMinScore = iMinScore;

      // Set any global objectives
      if ( mWriteMinScore != null )
      {
         for ( int i = 0; i < mWriteMinScore.size(); i++ )
         {
            String objID = (String)mWriteMinScore.elementAt(i);

            ADLSeqUtilities.
            setGlobalObjMinScore(objID, 
                  mLearnerID,
                  mScopeID,
                  Double.toString(iMinScore));
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> END - " + 
         "setObjMinScore");
      }
   }

   /**
    * Retrieves the Objective's max score.
    * 
    * @param iIsRetry Indicates if this evaluation is occurring during the
    *                 processing of a 'retry' sequencing request.
    *
    * @return The objective's max score (<code>real(10,7)</code>), or <code>
    *         unknown</code>, if the objective does not have a valid max score.
    */
   String getObjMaxScore(boolean iIsRetry)
   {
      // Do not assume there is a valid raw score
      String ret = ADLTracking.TRACK_UNKNOWN;

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN   - " +
         "getObjMaxScore");
         System.out.println("  ::  LOCAL --> " + false);
      }

      boolean done = false;
      boolean globalvalexists = false;
      // Is there a 'read' objective map?
      if ( mReadMaxScore != null )
      {

         if ( _Debug )
         {
            System.out.println("  ::-->  Looking at shared max score");
         }

         String maxscore =
            ADLSeqUtilities.getGlobalObjMaxScore(mReadMaxScore, 
                  mLearnerID,
                  mScopeID);

         // Always use shared raw score if available
         if ( maxscore != null )
         {
            ret = maxscore;
            done = true;
            globalvalexists = true;
         }
      }

      if ( ! globalvalexists )
      {
         if ( mHasMaxScore && !done )
         {

            if ( _Debug )
            {
               System.out.println("  ::--> Using local max score");
            }

            if ( mHasMaxScore && !(iIsRetry && mDirtyObj) )
            {
               ret = Double.toString(mMaxScore);
            }
         }
      }

      if ( !ret.equals(ADLTracking.TRACK_UNKNOWN) &&
             !(iIsRetry && mDirtyObj) )
      {
         try
         {
            Double.parseDouble(ret);
         }
         catch ( Exception e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: Bad max score value");
               System.out.println("  ::-->  " + ret);
               System.out.println("  :: Returning " + ADLTracking.TRACK_UNKNOWN);
               System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
               "getObjMaxScore");
            }

            return ADLTracking.TRACK_UNKNOWN;
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + ret);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
         "getObjMaxScore");
      }

      return ret;
   }

   /**
    * Sets an activity's max score.
    * 
    * @param iMaxScore The max score to be set.
    */
   void setObjMaxScore(double iMaxScore)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "setObjMaxScore");
         System.out.println("  ::-->  " + iMaxScore);
      }

      mHasMaxScore = true;
      mMaxScore = iMaxScore;

      // Set any global objectives
      if ( mWriteMaxScore != null )
      {
         for ( int i = 0; i < mWriteMaxScore.size(); i++ )
         {
            String objID = (String)mWriteMaxScore.elementAt(i);

            ADLSeqUtilities.
            setGlobalObjMaxScore(objID, 
                  mLearnerID,
                  mScopeID,
                  Double.toString(iMaxScore));
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> END - " + 
         "setObjMaxScore");
      }
   }

   /**
    * Retrieves the Objective's progress measure.
    * 
    * @param iUseCurrentProgress Indicates if this evaluation is occurring during the
    *                 processing of a 'retry' sequencing request.
    *
    * @return The objective's progress measure (<code>[0 - 1.0]</code>), or <code>
    *         unknown</code>, if the objective does not have a valid progress measure.
    */
   String getObjProgressMeasure(boolean iDirtyProgress)
   {
      // Do not assume there is a valid progress measure
      String ret = ADLTracking.TRACK_UNKNOWN;

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN   - " +
         "getObjProgressMeasure");
         System.out.println("  ::  LOCAL --> " + false);
      }

      boolean done = false;
      boolean globalvalexists = false;
      // Is there a 'read' objective map?
      if ( mReadProgressMeasure != null )
      {
         if ( _Debug )
         {
            System.out.println("  ::-->  Looking at shared progress measure");
            System.out.println("  ::-->  " + mReadProgressMeasure);
         }

         String progress =
            ADLSeqUtilities.getGlobalObjProgressMeasure(mReadProgressMeasure, 
                  mLearnerID,
                  mScopeID);

         // Always use shared raw score if available
         if ( progress != null )
         {
            if (! progress.equals(ADLTracking.TRACK_UNKNOWN) )
            {
               ret = progress;
               done = true;
               globalvalexists = true;
            }
         }
      }

      if ( ! globalvalexists )
      {
         if ( mHasProgressMeasure && !done )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Using local progress measure");
            }

            if ( mHasProgressMeasure && ! iDirtyProgress )
            {
               ret = Double.toString(mProgressMeasure);
            }
         }
      }

      if ( !ret.equals(ADLTracking.TRACK_UNKNOWN) &&
             !(iDirtyProgress && mDirtyObj) )
      {
         boolean valid = true;
         double val = -999;
         try
         {
            val = Double.parseDouble(ret);
         }
         catch ( Exception e )
         {
            valid = false;
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: Bad progress measure value");
            }
         }
         
         if ( ! valid || ( val < 0.0 || val > 1.0 ) )
         {
            ret = ADLTracking.TRACK_UNKNOWN;
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR :  Invalid Progress Measure " + val);
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + ret);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " + 
         "getObjProgressMeasure");
      }

      return ret;
   }

   /**
    * Sets the activity's progress measure.
    * 
    * @param iProgressMeasure The progress measure to set.
    */
   void setObjProgressMeasure(double iProgressMeasure)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "setObjProgressMeasure");
         System.out.println("  ::-->  " + iProgressMeasure);
      }

      // Validate the range of the measure
      if ( iProgressMeasure < 0.0 || iProgressMeasure > 1.0 )
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Invalid Measure: " + iProgressMeasure);
            System.out.println("  ::--> Assume 'Unknown'");
         }

         clearObjProgressMeasure();
      }
      else
      {
         mHasProgressMeasure = true;
         mProgressMeasure = iProgressMeasure;

         // Set any global objectives
         if ( mWriteProgressMeasure != null )
         {
            for ( int i = 0; i < mWriteProgressMeasure.size(); i++ )
            {
               String objID = (String)mWriteProgressMeasure.elementAt(i);

               ADLSeqUtilities.
               setGlobalObjProgressMeasure(objID, 
                     mLearnerID,
                     mScopeID,
                     Double.toString(iProgressMeasure));
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> END - " + 
         "setObjProgressMeasure");
      }
   }

   /**
    * Gets the completion status for this activity.
    * 
    * @param iIsRetry Indicates that this was called during a retry.
    * 
    * @return The completion status.
    */
   String getObjCompletionStatus(boolean iDirtyProgress)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " + 
         "getObjCompletionStatus");
         System.out.println("  ::  LOCAL --> " + false);
      }

      String ret = ADLTracking.TRACK_UNKNOWN;
      boolean done = false;
      
      boolean globalvalexists = false;
      // Is there a 'read' objective map?
      if ( mReadCompletionStatus != null )
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Using shared status");
         } 

         // Retrieve shared competency mastery status
         String status =
            ADLSeqUtilities.getGlobalObjCompletion(mReadCompletionStatus, 
                  mLearnerID,
                  mScopeID);
         if ( status != null )
         {
            if ( ! status.equals(ADLTracking.TRACK_UNKNOWN) )
            {
               ret = status;
               done = true;
               globalvalexists = true;
            }
         }
      }

      if ( ! globalvalexists )
      {
         if ( mHasCompletionStatus && (!done) )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Using local objective status");
            }

            if ( mHasCompletionStatus && !iDirtyProgress )
            {
               ret = mCompletionStatus;
            }
         }
      }
      
      if ( _Debug )
      {
         System.out.println("  ::-->  " + ret);
         System.out.println("  :: SeqObjectiveTracking   --> END   - " +
         "getObjCompletionStatus");
      }

      return ret;
   }

   /**
    * Sets the completion status for the activity.
    * 
    * @param iCompletionStatus The completion status.
    */
   void setObjCompletionStatus(String iCompletionStatus)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveTracking   --> BEGIN - " +
         "setObjCompletionStatus");
         System.out.println("  ::-->  " + mObj.mObjID);
         System.out.println("  ::-->  " + iCompletionStatus);
      }

      mCompletionStatus = iCompletionStatus;
      mHasCompletionStatus = true;
      
      // Set any global objectives
      if ( mWriteCompletionStatus != null )
      {
         for ( int i = 0; i < mWriteCompletionStatus.size(); i++ )
         {
            String objID = (String)mWriteCompletionStatus.elementAt(i);
            ADLSeqUtilities.setGlobalObjCompletion(objID, 
                  mLearnerID,
                  mScopeID,
                  iCompletionStatus);
         }
      }
   }

}  // end SeqObjectiveTracking
