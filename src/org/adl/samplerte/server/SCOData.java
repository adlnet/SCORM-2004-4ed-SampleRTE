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

import java.util.Vector;


/**
 * Encapsulation of information required for launch.<br><br>
 * 
 * <strong>Filename:</strong>SCOData.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>SCOData</code> encapsulates the information about a specific
 * SCO returned from the from the Sample RTE Database.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE<br>
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
public class SCOData
{
   /**
    * The unique activity identifier.  This is the identifier that the Sample 
    * RTE uses internally to track a unique activity.    
    */ 
   public String mActivityID = null;

   /**
    * The item's title.  This is the title as defined by the &lt;title&gt;
    * sub-element of the &lt;item&gt; element in the imsmanifest.xml
    * file
    */ 
   public String mItemTitle = null;

   /**
    * The comment to be used to initialize the cmi.comment_from_lms.n array of
    * comments.
    */ 
   public Vector mComment = null;

   /**
    * The comment to be used to initialize the cmi.comment_from_lms.n array of
    * dates and times.
    */ 
   public Vector mDateTime = null;

   /**
    * The comment to be used to initialize the cmi.comment_from_lms.n array of
    * locations.
    */ 
   public Vector mLocation = null;

}



