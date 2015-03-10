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
import java.util.Vector;

import org.adl.datamodels.SCODataManager;
import org.adl.sequencer.ADLValidRequests;

/**
 * <strong>Filename:</strong> LMSCMIServletResponse<br><br>
 *
 * <strong>Description:</strong><br>
 * This class contains the data that the <code>LMSCMIServlet</code> sends  
 * across the socket to the <code>APIAdaptor</code>.<br><br>
 * 
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
 *     <li>IMS SS Specification</li>
 *     <li>SCORM 2004</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class LMSCMIServletResponse implements Serializable
{
   /**
    * The run-time data associated with the activity.
    */
   public SCODataManager mActivityData = null;

   /**
    * The state of the UI in relation to the current activity.
    */
   public ADLValidRequests mValidRequests = null;

   /**
    * Indicates if an activity is avaliable for immediate delivery.
    */
   public boolean mAvailableActivity = false;

   /**
    * Provides time out tracking data for the LMS Client.
    */
   public Vector mTimeoutTracking = null;

   /**
    * Indicates if the user 'logged out'.
    */
   public boolean mLogout = false;

   /**
    * Indicates any error that occured while processing the request.
    */
   public String mError = null;

   /**
    * Indicates if control mode is flow.
    */
   public boolean mFlow = true;

   /**
    * Indicates if control mode is choice.
    */
   public boolean mChoice = false;

   /**
    * Indicates if control mode is autoadvance.
    */
   public boolean mAuto = false;

   /**
    * Default constructor
    */
   public LMSCMIServletResponse()
   {
   }
} // LMSCMIServletResponse

