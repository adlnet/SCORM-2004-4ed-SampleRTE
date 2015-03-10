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
 * Encapsulation of a set of rollup rules associated with an activity.<br><br>
 * 
 * <strong>Filename:</strong> SeqRollupRuleset.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * This is an implementation of rollup behavior defined in section RB of the
 * IMS SS Specification.  This class performs evaluation and makes appropriate
 * status change(s) to the affected activity.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * As with other classes that encapsulate sequencing behaviors, this class is
 * not optimized.  It is intended to demonstrate the intension of the
 * specification and not provide a 'full-featured' implementation.<br><br>
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
public class SeqRollupRuleset implements Serializable
{
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * This is the set of rollup rules applied to the activity.
    */
   private Vector mRollupRules = null;

   /**
    * This is the result of evaluating the 'Satisfied' rollup rules
    */
   private boolean mIsSatisfied = false;

   /**
    * This is the result of evaluating the 'Not Satisfied' rollup rules
    */
   private boolean mIsNotSatisfied = false;

   /**
    * This is the result of evaluating the 'Completed' rollup rules
    */
   private boolean mIsCompleted = false;

   /**
    * This is the result of evaluating the 'Incomplete' rollup rules
    */
   private boolean mIsIncomplete = false;


   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
   Constructors 
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Default Constructor
    */
   public SeqRollupRuleset()
   {
      // Default constructor
   }

   /**
    * Initializes the current set of rollup rules.
    * 
    * @param iRules      Set of preconstructed rollup rules (<code>SeqRollupRule
    *                    </code>).
    */
   public SeqRollupRuleset(Vector iRules)
   {

      if ( _Debug )
      {
         System.out.println("  :: SeqRollupRuleset  --> BEGIN - constructor");

         if ( iRules == null )
         {
            System.out.println("  ::--> Default Rules");
         }
         else
         {
            for ( int i = 0; i < iRules.size(); i++ )
            {
               SeqRollupRule temp = (SeqRollupRule)iRules.elementAt(i);

               temp.dumpState();
            }
         }
      }

      mRollupRules = iRules;

      if ( _Debug )
      {
         System.out.println("  :: SeqRollupRuleset  --> END   - constructor");
      }
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Public Methods 
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
   /**
    * Evaluates this set of rollup rules for the target activity.  
    *
    * @param ioThisActivity The target activity of the rollup evaluation.
    */
   public void evaluate(SeqActivity ioThisActivity)
   {
      // Clear previous evaluation state -- nothing should change due to rollup.
      mIsCompleted = false;
      mIsIncomplete = false;
      mIsSatisfied = false;
      mIsNotSatisfied = false;

      // This method implements part of RB.1.5

      if ( _Debug )
      {
         System.out.println("  :: SeqRollupRuleset  --> BEGIN - evaluate");
         if ( mRollupRules != null )
         {
            System.out.println("  ::-->  " + mRollupRules.size());
         }
         else
         {
            System.out.println("  ::-->  NULL");
         }
      }

      // Evaluate all defined rollup rules for this activity.
      // Make sure there is a legal target and a set of children. 
      if ( ioThisActivity != null )
      {

         if ( ioThisActivity.getChildren(false) != null )
         {
            // Step 3.1 -- apply the Measure Rollup Process
            applyMeasureRollup(ioThisActivity);
            
            // Apply Progress Measure Rollup Process
            applyProgressMeasureRollup(ioThisActivity);

            boolean satisfiedRule = false;
            boolean completedRule = false;

            if ( mRollupRules != null )
            {
               // Confirm at least one rule is defined for both sets --
               //  Complete/Incomplete and Satisfied/Not Satisfied
               for ( int i = 0; i < mRollupRules.size(); i++ )
               {
                  SeqRollupRule rule = (SeqRollupRule)mRollupRules.
                                       elementAt(i);

                  if ( rule.mAction == SeqRollupRule.
                       ROLLUP_ACTION_SATISFIED ||
                       rule.mAction == SeqRollupRule.
                       ROLLUP_ACTION_NOTSATISFIED )
                  {
                     satisfiedRule = true;
                  }

                  if ( rule.mAction == SeqRollupRule.
                       ROLLUP_ACTION_COMPLETED ||
                       rule.mAction == SeqRollupRule.
                       ROLLUP_ACTION_INCOMPLETE )
                  {
                     completedRule = true;
                  }
               }
            }

            // If no satisfied rule is defined, use default objective rollup
            if ( !satisfiedRule )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Creating default " + 
                                     "satisfied rules");
               }

               if ( mRollupRules == null )
               {
                  mRollupRules = new Vector();
               }

               // Create default Not Satisfied rule
               SeqConditionSet set = new SeqConditionSet(true);
               SeqCondition cond = new SeqCondition();
               SeqRollupRule rule = new SeqRollupRule();

               set.mCombination = SeqConditionSet.COMBINATION_ANY;
               set.mConditions = new Vector();

               cond.mCondition = SeqCondition.OBJSTATUSKNOWN;
               set.mConditions.add(cond);

//               cond = new SeqCondition();
//               cond.mCondition = SeqCondition.SATISFIED;
//               cond.mNot = true;
               set.mConditions.add(cond);

               rule.mAction = SeqRollupRule.ROLLUP_ACTION_NOTSATISFIED;
               rule.mConditions = set;

               // Add the default Not Satisfied rule to the set
               mRollupRules.add(rule);

               // Create default Satisfied rule
               rule = new SeqRollupRule();
               set = new SeqConditionSet(true);
               cond = new SeqCondition();

               set.mCombination = SeqConditionSet.COMBINATION_ALL;
               cond.mCondition = SeqCondition.SATISFIED;
               set.mConditions = new Vector();
               set.mConditions.add(cond);

               rule.mAction = SeqRollupRule.ROLLUP_ACTION_SATISFIED;
               rule.mConditions = set;

               // Add the default Satisfied rule to the set
               mRollupRules.add(rule);
            }

            // If no completion rule is defined, use default completion rollup
            if ( !completedRule )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Creating default " + 
                                     "completion rules");
               }

               if ( mRollupRules == null )
               {
                  mRollupRules = new Vector();
               }

               // Create default Incomplete rule
               SeqConditionSet set = new SeqConditionSet(true);
               SeqCondition cond = new SeqCondition();
               SeqRollupRule rule = new SeqRollupRule();

               set.mCombination = SeqConditionSet.COMBINATION_ANY;
               set.mConditions = new Vector();

               cond.mCondition = SeqCondition.PROGRESSKNOWN;
               set.mConditions.add(cond);

//               cond = new SeqCondition();
//               cond.mCondition = SeqCondition.COMPLETED;
//               cond.mNot = true;
               set.mConditions.add(cond);

               rule.mAction = SeqRollupRule.ROLLUP_ACTION_INCOMPLETE;
               rule.mConditions = set;

               // Add the default Incomplete rule to the set
               mRollupRules.add(rule);

               // Create default Completion rule
               rule = new SeqRollupRule();
               set = new SeqConditionSet(true);
               cond = new SeqCondition();

               set.mCombination = SeqConditionSet.COMBINATION_ALL;
               cond.mCondition = SeqCondition.COMPLETED;
               set.mConditions = new Vector();
               set.mConditions.add(cond);

               rule = new SeqRollupRule();
               rule.mAction = SeqRollupRule.ROLLUP_ACTION_COMPLETED;
               rule.mConditions = set;

               // Add the default Completion rule to the set
               mRollupRules.add(rule);
            }

            if ( _Debug )
            {
               System.out.println("  ::--> Size == " + mRollupRules.size());
            }

            // Evaluate all rollup rules.
            for ( int i = 0; i < mRollupRules.size(); i++ )
            {
               SeqRollupRule rule = (SeqRollupRule)mRollupRules.elementAt(i);

               if ( _Debug )
               {
                  System.out.print("  :: EVALUATE ::-->  ");

                  switch(rule.mAction)
                  {
                     case SeqRollupRule.ROLLUP_ACTION_SATISFIED:

                        System.out.println("satisified");
                        break;

                     case SeqRollupRule.ROLLUP_ACTION_NOTSATISFIED:

                        System.out.println("notSatisified");
                        break;

                     case SeqRollupRule.ROLLUP_ACTION_COMPLETED:

                        System.out.println("completed");
                        break;

                     case SeqRollupRule.ROLLUP_ACTION_INCOMPLETE:

                        System.out.println("incomplete");
                        break;

                     default:
                        System.out.println("ERROR");

                  }
               }

               int result = rule.evaluate(ioThisActivity.getChildren(false));

               // Track state changes
               switch ( result )
               {
                  case SeqRollupRule.ROLLUP_ACTION_NOCHANGE:

                     // No status change indicated
                     if ( _Debug )
                     {
                        System.out.println("  :+ NO STATUS CHANGE +: CHANGE");
                     }

                     break;

                  case SeqRollupRule.ROLLUP_ACTION_SATISFIED:

                     if ( _Debug )
                     {
                        System.out.println("  :+ SATISFIED +: CHANGE");
                     }

                     mIsSatisfied = true;
                     break;

                  case SeqRollupRule.ROLLUP_ACTION_NOTSATISFIED:

                     if ( _Debug )
                     {
                        System.out.println("  :+ NOT SATISFIED +: CHANGE");
                     }

                     mIsNotSatisfied = true;
                     break;

                  case SeqRollupRule.ROLLUP_ACTION_COMPLETED:

                     if ( _Debug )
                     {
                        System.out.println("  :+ COMPLETED +: CHANGE");
                     }

                     mIsCompleted = true;
                     break;

                  case SeqRollupRule.ROLLUP_ACTION_INCOMPLETE:

                     if ( _Debug )
                     {
                        System.out.println("  :+ INCOMPLETE +: CHANGE");
                     }

                     mIsIncomplete = true;
                     break;
                     
                  default:
                     break;
               }
            }

            // If a measure threshold exists, it was already used to determine
            // the activity's status.  Otherwise, use the results of the rollup
            if ( !ioThisActivity.getObjSatisfiedByMeasure() )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Objective rollup using rules");
               }

               if ( mIsSatisfied )
               {
                  ioThisActivity.setObjSatisfied(ADLTracking.TRACK_SATISFIED);
               }
               else if ( mIsNotSatisfied )
               {
                  // assume we got unknown
                  if ( ioThisActivity.isPrimaryStatusSetBySCO() && ioThisActivity.getObjSatValue().equals(ADLTracking.TRACK_UNKNOWN) )
                  {
                     // if this is the case, ignore it
                     if ( _Debug )
                     {
                        System.out.println("  ::--> Ignoring not satisfied value");
                     }                     
                  }
                  else
                  {
                     ioThisActivity.setObjSatisfied(ADLTracking.TRACK_NOTSATISFIED);
                  }
               }
            }

            // If a progress measure threshold exists, it was already used to determine
            // the activity's completion status.  Otherwise, use the results of the rollup
            if ( !ioThisActivity.getCompletedByMeasure() )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Completion rollup using rules");
               }

               if ( mIsCompleted )
               {
                  ioThisActivity.setProgress(ADLTracking.TRACK_COMPLETED);
               }
               else if ( mIsIncomplete )
               {
               // assume we got unknown
                  if ( ioThisActivity.isPrimaryProgressSetBySCO() && ioThisActivity.getProgressValue().equals(ADLTracking.TRACK_UNKNOWN) )
                  {
                     // if this is the case, ignore it
                     if ( _Debug )
                     {
                        System.out.println("  ::--> Ignoring not satisfied value");
                     }                     
                  }
                  else
                  {
                     ioThisActivity.setProgress(ADLTracking.TRACK_INCOMPLETE);
                  }
               }
            }

         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR : No Children");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : Invalid rollup rules");
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqRollupRuleset  --> END - evaluate");
      }
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Private Methods 
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
   /**
    * Applies the Measure Rollup Process to the activity (RB.1.1).
    *
    * @param ioThisActivity The target activity of the rollup evaluation.
    */
   private void applyMeasureRollup(SeqActivity ioThisActivity)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqRollupRuleset  --> BEGIN - " + 
                            "applyMeasureRollup");
      }

      double total = 0.0;
      double countedMeasure = 0.0;

      Vector children = ioThisActivity.getChildren(false);

      // Measure Rollup Behavior 
      for ( int i = 0; i < children.size(); i++ )
      {
         SeqActivity child = (SeqActivity)children.elementAt(i);

         if ( _Debug )
         {
            System.out.println("  ::--> Look At :: " + child.getID());
         }

         if ( child.getIsTracked() )
         {
            // Make sure a non-zero weight is defined
            if ( child.getObjMeasureWeight() > 0.0 )
            {
               countedMeasure += child.getObjMeasureWeight();

               // If a measure is defined for the child
               if ( child.getObjMeasureStatus(false) )
               {
                  total += child.getObjMeasureWeight() * 
                           child.getObjMeasure(false);
               }
            }
         }
      }

      if ( countedMeasure > 0.0 )
      {

         if ( _Debug )
         {
            System.out.println("  ::--> Counted         --> " +
                               countedMeasure);
            System.out.println("  ::--> Setting Measure --> " + 
                               (total / countedMeasure));
         }

         ioThisActivity.setObjMeasure(total / countedMeasure);
      }
      else
      {

         if ( _Debug )
         {
            System.out.println("  ::--> Setting Measure --> UNKNOWN");
         }

         // Measure could not be determined through rollup, clear measure
         ioThisActivity.clearObjMeasure();
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqRollupRuleset  --> END   - " + 
                            "applyMeasureRollup");
      }
   }

   /**
    * Applies the Progress Measure Rollup Process to the activity (RB.1.1 b).
    *
    * @param ioThisActivity The target activity of the rollup evaluation.
    */
   private void applyProgressMeasureRollup(SeqActivity ioThisActivity)
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqRollupRuleset  --> BEGIN - " + 
                            "applyProgressMeasureRollup");
      }

      double total = 0.0;
      double countedMeasure = 0.0;

      Vector children = ioThisActivity.getChildren(false);

      // Progress Measure Rollup Behavior 
      for ( int i = 0; i < children.size(); i++ )
      {
         SeqActivity child = (SeqActivity)children.elementAt(i);

         if ( _Debug )
         {
            System.out.println("  ::--> Look At :: " + child.getID());
         }

         if ( child.getIsTracked() )
         {
            // Make sure a non-zero weight is defined
            if ( child.getProMeasureWeight() > 0.0 )
            {
               countedMeasure += child.getProMeasureWeight();

               // If a measure is defined for the child
               if ( child.getProMeasureStatus(false) )
               {
                  double pretotal = total;
                  total += child.getProMeasureWeight() * 
                           child.getProMeasure(false);
                  if ( _Debug )
                  {
                     System.out.println("  ::--> Pro Measure eval for " + child.getID());
                     System.out.println("\t  ::--> Pro Measure weight: " + child.getProMeasureWeight());
                     System.out.println("\t  ::--> Pro Measure: " + child.getProMeasure(false));
                     System.out.println("\t  ::--> Pro weight * measure: " + (child.getProMeasureWeight() * 
                           child.getProMeasure(false)));
                     System.out.println("\t  ::--> pretotal: " + pretotal);
                     System.out.println("\t  ::--> new total: " + total);
                  }
               }
               else
               {
                  if ( _Debug )
                  {
                     System.out.println("Progress Measure Status not defined");
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("Progress Measure Weight is 0");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("NOT TRACKED");
            }
         }
      }

      if ( countedMeasure > 0.0 )
      {

         if ( _Debug )
         {
            System.out.println("  ::--> Counted         --> " +
                               countedMeasure);
            System.out.println("  ::--> Setting Progress Measure --> " + 
                               (total / countedMeasure));
         }


         // Setting the rolled-up measure will force a threshold evaluation if applicable
         ioThisActivity.setProMeasure(total / countedMeasure);
      }
      else
      {

         if ( _Debug )
         {
            System.out.println("  ::--> Setting Progress Measure --> UNKNOWN");
         }

         // Measure could not be determined through rollup, clear measure
         ioThisActivity.clearProMeasure();
      }

      if ( _Debug )
      {
         System.out.println("  :: SeqRollupRuleset  --> END   - " + 
                            "applyProgressMeasureRollup");
      }
   
   }

   /**
    * Describes the number of rollup rules in this set
    * 
    * @return The count of rollup rules in this set.
    */
   public int size()
   {
      if ( mRollupRules != null )
      {
         return mRollupRules.size();
      }

      return 0;
   }

}  // end SeqRollupRuleset
