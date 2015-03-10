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
 * Enumeration of acceptable navigation requests.<br><br>
 * 
 * <strong>Filename:</strong> SeqNavRequests.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>ADLSequencer</code> will accept the navigation requests defined
 * in by this class.  Requests correspond to the IMS SS Specification section
 * Navagation Behavior (NB) section.<br><br>
 * 
 * Navigation requests are signaled to the sequencer through the <code>
 * ADLNavigation</code> interface.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE.<br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * This enumeration does not include the 'choice' navigation request.  Because 
 * the 'choice' navigation request requires a parameter indicating the target
 * activity, 'choice' navigation requests are triggered through an independent
 * method on the <code>ADLNavigation</code> interface.
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
public class SeqNavRequests
{

   /**
    * Enumeration of possible navigation requests -- 
    * In this case, No navigation request is also valid.
    * <br>None
    * <br><b>0</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_NONE               =  0;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>Start
    * <br><b>1</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_START              =  1;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>Resume All
    * <br><b>2</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_RESUMEALL          =  2;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>Continue
    * <br><b>3</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_CONTINUE           =  3;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>Previous
    * <br><b>4</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_PREVIOUS           =  4;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>Abandon
    * <br><b>5</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_ABANDON            =  5;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>AbandonAll
    * <br><b>6</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_ABANDONALL         =  6;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>SuspendAll
    * <br><b>7</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_SUSPENDALL         =  7; 

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>Exit
    * <br><b>8</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_EXIT               =  8;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>ExitAll
    * <br><b>9</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_EXITALL            =  9;

   /**
    * Enumeration of possible navigation requests -- described in Navigation
    * Behavior (Section NB of the IMS SS Specification).
    * <br>Jump
    * <br><b>10</b>
    * <br>[SEQUENCING SUBSYSTEM CONSTANT]
    */
   public static final int NAV_JUMP            =  10;

}  // end SeqNavRequests
