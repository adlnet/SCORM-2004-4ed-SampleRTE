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

package org.adl.datamodels;

import java.util.Vector;

/**
 * Encapsulation of information required for processing a data model request.
 * 
 * <strong>Filename:</strong> DMProcessingInfo.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>ADLLaunch</code> encapsulates the information that may be returned
 * (out parameters) during the processing of a data model request.<br><br>
 * 
 * <strong>Design Issues:</strong><br><br>
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
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */ 
public class DMProcessingInfo
{

   /**
    * Describes the value being maintained by a data model element.
    */
   public String mValue = null;


   /**
    * Describes the data model element that processing should be applied to.
    */
   public DMElement mElement = null;


   /**
    * Describes the set this data model element is contained in.
    */
   public Vector mRecords = null;

   /**
    * Describes whether this data model element is initialized or not.
    */
   public boolean mInitialized = false;
   
   /**
    * Describes if this data model element's value was set by the SCO or not.
    */
   public boolean mSetBySCO = false;
}  // end DMProcessingInfo
