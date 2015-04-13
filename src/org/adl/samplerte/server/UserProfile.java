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

/**
 * The UserProfile class handles the data required for a user profile.<br><br>
 * 
 * <strong>Filename:</strong> UserProfile.java<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE <br>
 * <br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS Specification
 *     <li>SCORM 2004 4th Edition
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class UserProfile 
{
   /**
    * The user's first name.
    */
   public String mFirstName = null;

   /**
    * The user's last name.
    */
   public String mLastName = null;


   /**
    * The user's ID.
    */
   public String mUserID = null;


   /**
    * The cmi.learner_preference.audio_level associated with this user.
    */
   public String mAudioLevel = null;


   /**
    * The cmi.learner_preference.audio_captioning associated with this user.
    */
   public String mAudioCaptioning = null;


   /**
    * The cmi.learner_preference.language associated with this user.
    */
   public String mLanguage = null;


   /**
    * The cmi.learner_preference.delivery_speed associated with this user.
    */
   public String mDeliverySpeed = null;
   
   /**
    * The admin status associated with this user. True if user is an admin.
    */
   public boolean mAdmin = false;

}
   

