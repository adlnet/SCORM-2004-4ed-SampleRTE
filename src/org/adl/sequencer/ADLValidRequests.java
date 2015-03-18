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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Encapsulation of information required for delivery.<br><br>
 * 
 * <strong>Filename:</strong> ADLValidRequests.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>ADLUIState</code> encapsulates the information required by the
 * SCORM 2004 4th Edition Sample RTE delivery system to determine which
 * navigation UI controls should be enabled on for the current launched
 * activity.<br><br>
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
public class ADLValidRequests implements Serializable
{

   /**
    * Should a 'Start' button be enabled before the sequencing session begins
    */
   public boolean mStart = false;


   /**
    * Should a 'Resume All' button be enabled before the sequencing session begins
    */
   public boolean mResume = false;


   /**
    * Should a 'Continue' button be enabled during delivery of the current
    * activity.
    */
   public boolean mContinue = false;

   /**
    * Should a 'Continue' button be enabled during delivery of the current
    * activity that triggers an Exit navigation request.
    */
   public boolean mContinueExit = false;

   /**
    * Should a 'Previous' button be enabled during the delivery of the
    * current activity.
    */
   public boolean mPrevious = false;

   /**
    * Indictates if the sequencing session has begun and a 'SuspendAll'
    * navigation request is valid.
    */
   public boolean mSuspend = false;

   /**
    * Set of valid targets for a choice navigation request
    */
   public Hashtable mChoice = null;

   /**
    * Set of valid targets for a jump navigation request.
    */
   public Hashtable mJump = null;
   
   /**
    * The currently valid table of contents (list of <code>ADLTOC</code>) to be
    * provided during the current activity.
    */
   public Vector mTOC = null;
   
   public String toJSONString() 
   {
      StringBuilder sb = new StringBuilder("{\n");
      sb.append("\"mStart\":" + mStart + ",\n");
      sb.append("\"mResume\":" + mResume + ",\n");
      sb.append("\"mContinue\":" + mContinue + ",\n");
      sb.append("\"mContinueExit\":" + mContinueExit + ",\n");
      sb.append("\"mPrevious\":" + mPrevious + ",\n");
      sb.append("\"mSuspend\":" + mSuspend + ",\n");
      // choice <string><adltoc>
//      if (mChoice.size() > 0) {
//         Enumeration<String> ckeys = mChoice.keys();
//         while (ckeys.hasMoreElements())
//         {
//            
//         }
//      }
//      else
//      {
//         sb.append("\"mChoice\": {}\n");
//      }
      
      // mJump <string><seqactivity> ?!
      // mTOC [adltoc]
      sb.deleteCharAt(sb.length() -2);;
      sb.append("}");
      return sb.toString();
   }

}  // end ADLValidRequests
