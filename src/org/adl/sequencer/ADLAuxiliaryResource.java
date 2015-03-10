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
 * <strong>Filename:</strong> ADLAuxiliaryResource.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>ADLAuxiliaryResource</code> encapsulates the information required
 * by the SCORM 2004 4th Edition Sample RTE to identify, provide access to, and
 * request avaliable auxiliary resources.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE. <br>
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
 *     <li>IMS SS 1.0</li>
 *     <li>SCORM 2004 4th Edition</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class ADLAuxiliaryResource implements Serializable 
{
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * The type of the available auxillary resource.
    */
   public String mType = null;

   /**
    * The resource ID used to deliver the auxillary resource.
    */
   public String mResourceID = null;

   /**
    * Describes the delivery parameter for the auxillary resource.
    */
   public String mParameter = null;

   /**
    * This method provides the state this <code>ADLAuxiliaryResource</code> obje
    * diagnostic purposes.
    */
   public void dumpState()
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLAuxiliaryResource --> BEGIN - dumpState");

         System.out.println("  ::--> Type:        " + mType);
         System.out.println("  ::--> Resource ID: " + mResourceID);
         System.out.println("  ::--> Parameter:   " + mParameter);

         System.out.println("  :: ADLAuxiliaryResource --> END   - dumpState");
      }
   }

}  // end ADLAuxiliaryResource