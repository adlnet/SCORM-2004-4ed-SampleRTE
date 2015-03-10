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
 * Provides an interface for the RTE to communicate navigation requests to the
 * sequencer<br><br>.
 * 
 * <strong>Filename:</strong> SeqNavigation.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * This interface represents the entry point to the Overall Sequencing Process
 * described in IMS ss.  The two <code>navigate</code> methods provide a way
 * for the RTE to signal a navigation request. Each <code>navigate</code>
 * method provides an <code>SeqLaunch</code> object, which contains the 
 * information required by the RTE to launch the resource(s) associated with the
 * idenified activity, or an error code if any sequencing process fails.<br><br>
 * 
 * When an navigation request does not result in an activity to be delivered,
 * an <code>ADLLaunch</code> object will still be returned by, however the value
 * its <code>mSeqNonContent</code> field will contain a special value from the
 * <code>ADLLaunch.LAUNCH_[XXX]</code> enumeration.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE. <br>
 * <br>
 * 
 * <strong>Implementation Issues:</strong><br>
 * It is the responsibility of the implementation of this interface to
 * perform any and all prelauch actions to prepare the identifed activity (and
 * its resource(s)) for launch, prior to returning an <code>ADLLaunch</code> 
 * object.<br><br>
 * 
 * If the navigation event does not result in a deliverable activity, it is the
 * responsibily of the RTE to gracefully handle other results.<br><br>
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
public interface SeqNavigation
{
   /**
    * This method is used to inform the sequencer that a navigation request,
    * other than 'Choice' has occurred.
    * 
    * @param iRequest Indicates which navigation request should be processed.
    * 
    * @return Information about the 'Next' activity to delivery or a processing
    *         error.
    * @see org.adl.sequencer.SeqNavRequests
    * @see org.adl.sequencer.ADLLaunch
    */
   ADLLaunch navigate(int iRequest);


   /**
    * This method is used to inform the sequencer that a 'Choice' navigation
    * request has occurred.
    * 
    * @param iTarget ID (<code>String</code>) of the target activity.
    * 
    * @param iJumpRequest Indicates if this is a jump request or not.
    * 
    * @return Information about the 'Next' activity to delivery or a processing
    *         error.
    * @see org.adl.sequencer.ADLLaunch
    */
   ADLLaunch navigate(String iTarget, boolean iJumpRequest);


}  // end SeqNavigation
