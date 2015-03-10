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

/**
 * <strong>Filename:</strong> SeqCondition.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * This class wraps one condition evaluated during rollup and sequencing rules
 * evaluation<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * All fields are purposefully public to allow immediate access to known data
 * elements.<br>
 * 
 * All possible conditions are enumerated by this class although some 
 * conditions do not apply in all instances.<br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS 1.0</li>
 *     <li>SCORM 2004 4th Edition</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class SeqCondition implements Serializable
{
  
   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Satisfied
    * <br><b>"satisfied"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String SATISFIED         = "satisfied";


   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Objective Status Known
    * <br><b>"objectiveStatusKnown"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String OBJSTATUSKNOWN    = "objectiveStatusKnown";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Objective Measure Known
    * <br><b>"objectiveMeasureKnown"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String OBJMEASUREKNOWN   = "objectiveMeasureKnown";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Objective Measure Greater Than
    * <br><b>"objectiveMeasureGreaterThan"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String OBJMEASUREGRTHAN  = "objectiveMeasureGreaterThan";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Objective Measure Less Than
    * <br><b>"objectiveMeasureLessThan"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String OBJMEASURELSTHAN  = "objectiveMeasureLessThan";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Completed
    * <br><b>"completed"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String COMPLETED         = "completed";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Progress Known
    * <br><b>"progressKnown"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String PROGRESSKNOWN     = "activityProgressKnown";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Attempted
    * <br><b>"attempted"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String ATTEMPTED         = "attempted";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Attempt Limit Exceeded
    * <br><b>"attemptLimitExeeded"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String ATTEMPTSEXCEEDED  = "attemptLimitExceeded";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Time Limit Exceeded
    * <br><b>"timeLimitExceeded"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String TIMELIMITEXCEEDED = "timeLimitExceeded";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Outside Avaliable Time Range
    * <br><b>"outsideAvailableTimeRange"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String OUTSIDETIME       = "outsideAvailableTimeRange";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Always
    * <br><b>"always"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String ALWAYS       = "always";

   /**
    * Enumeration of possible evaluation criteria -- described in Sequencing
    * Rule Description (element 2.2.1) and Rollup Rule Description 
    * (element 3.2.1) of the IMS SS Specification.
    * <br>Never
    * <br><b>"never"</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static String NEVER       = "never";

   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;
   
   /**
    * The condition to be evaluated
    */
   public String mCondition = null;

   /**
    * Indicates if the condition evaluation should be negated
    */
   public boolean mNot = false;

   /** 
    * Indicates the objective being tested
    */
   public String mObjID = null;

   /**
    * Indicates the measure threshold being tested
    */
   public double mThreshold = 0.0;

   /**
    *  Default constructor 
    */
   public SeqCondition()
   {
      // Default constructor does nothing explicitly 
   }

   /**
    * This method provides the state this <code>SeqCondition</code> object for
    * diagnostic purposes.
    */
   public void dumpState()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqCondition  --> BEGIN - dumpState");

         System.out.println("  ::--> Condition :  " + mCondition);
         System.out.println("  ::--> Not?      :  " + mNot);
         System.out.println("  ::--> Obj ID    :  " + mObjID);
         System.out.println("  ::--> Threshold :  " + mThreshold);

         System.out.println("  :: SeqCondition  --> END   - dumpState");
      }
   }

}  // end SeqCondition
