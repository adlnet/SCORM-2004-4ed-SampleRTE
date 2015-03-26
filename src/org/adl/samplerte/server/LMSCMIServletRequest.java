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

import java.io.Serializable;

/**
 * <strong>Filename:</strong> LMSCMIServletRequest<br><br>
 *
 * This class contains the data that the <code>APIAdapterApplet</code> needs
 * to send across the socket to the <code>LMSCMIServlet</code>.<br><br>
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE. <br>
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
 *     <li>IMS SS Specification
 *     <li>SCORM 2004</li>
 * </ul>
 *
 * @author ADL Technical Team
 */
public class LMSCMIServletRequest implements Serializable
{
   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_UNKNOWN = 0;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_INIT = 1;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_GET = 2;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_TIMEOUT = 3;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_SET = 4;

   /**
    * Enumeration of possible request types that are processed by the <code>
    * LMSCMIServlet</code>.
    */
   public static final int TYPE_NAV = 5;

   /**
    * The run-time data that is being send from the client
    */
   public String[][] mActivityData = null;

   /**
    * Indicates if the request is being sent due to an LMSFinish
    */
   public boolean mIsFinished = false;

   /**
    * The activity ID of the activity that caused a time out.
    */
   public String mTimeoutActivity = null;

   /**
    * The type of the current Request.
    */
   public int mRequestType = LMSCMIServletRequest.TYPE_UNKNOWN;

   /**
    * The ID of the course associated with this request.
    */
   public String mCourseID = null;

   /**
    * The ID of the student associated with this request.
    */
   public String mStudentID = null;

   /**
    * The name of the student associated with this request.
    */
   public String mUserName = null;

   /**
    * The ID of the run-time data associated with this request.
    */
   public String mStateID = null;

   /**
    * The attempt count associated with the run-time data.
    */
   public String mNumAttempt = null;

   /**
    * The ID of the activity associated with this request.
    */
   public String mActivityID = null;

   /**
    * Whether or not the Quit button was pressed.
    */
   public boolean mQuitPushed = false;

   /**
    * Whether or not the Suspend button was pressed.
    */
   public boolean mSuspendPushed = false;

   /**
    * Default constuctor. No defined implementation.
    */
   public LMSCMIServletRequest()
   {
   }

} // LMSCMIServletRequest

