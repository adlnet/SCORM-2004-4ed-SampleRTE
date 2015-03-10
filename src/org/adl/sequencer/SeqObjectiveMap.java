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
 * <strong>Filename:</strong> SeqObjectiveMap.java<br><br>
 *
 * <strong>Description:</strong><br>
 * The <code>SeqObjectiveMap</code> encapsulates the information describing how
 * an activity's local objectives are mapped to the global objective space
 * for the current content aggregation.<br><br>
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
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS 1.0</li>
 *     <li>SCORM 2004 4th Edition</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class SeqObjectiveMap implements Serializable
{
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * The target global objective.
    */
   public String mGlobalObjID = null;

   /**
    * Indicates if satisfied status should be read.
    */
   public boolean mReadStatus = true;

   /**
    * Indicates if measure should be read.
    */
   public boolean mReadMeasure = true;

   /**
    * Indicates if raw score should be read.
    */
   public boolean mReadRawScore = true;
   
   /**
    * Indicates if min score should be read.
    */
   public boolean mReadMinScore = true;
   
   /**
    * Indicates if max score should be read.
    */
   public boolean mReadMaxScore = true;
   
   /**
    * Indicates if completion status should be read.
    */
   public boolean mReadCompletionStatus = true;
   
   /**
    * Indicates if progress measure should be read.
    */
   public boolean mReadProgressMeasure = true;
   
   /**
    * Indicates if satisfied status should be written.
    */
   public boolean mWriteStatus = false;

   /**
    * Indicates if measure should be written.
    */
   public boolean mWriteMeasure = false;
   
   /**
    * Indicates if raw score should be written.
    */
   public boolean mWriteRawScore = false;
   
   /**
    * Indicates if min score should be written.
    */
   public boolean mWriteMinScore = false;
   
   /**
    * Indicates if max score should be written.
    */
   public boolean mWriteMaxScore = false;
   
   /**
    * Indicates if completion status should be written.
    */
   public boolean mWriteCompletionStatus = false;
   
   /**
    * Indicates if progress measure should be written.
    */
   public boolean mWriteProgressMeasure = false;
   
   /**
    * Indicates if this objective map has any write maps defined.
    * 
    * @return If any write maps are defined for this objective map
    */
   public boolean hasWriteMaps()
   {
      return mWriteCompletionStatus || mWriteMaxScore || mWriteMeasure ||
             mWriteMinScore || mWriteProgressMeasure || mWriteRawScore ||
             mWriteStatus;
   }
   
   /**
    * Indicates if this objective map has any read maps defined.
    * 
    * @return If any read maps are defined for this objective map
    */
   public boolean hasReadMaps()
   {
      return mReadCompletionStatus || mReadMaxScore || mReadMeasure ||
             mReadMinScore || mReadProgressMeasure || mReadRawScore ||
             mReadStatus;
   }

   /**
    * This method provides the state this <code>SeqObjectiveMap</code> object 
    * for diagnostic purposes.
    */
   public void dumpState()
   {
      if ( _Debug )
      {
         System.out.println("  :: SeqObjectiveMap   --> BEGIN - dumpState");

         System.out.println("  ::--> Global ID: " + mGlobalObjID);
         System.out.println("  ::--> Read Satisfied Status?: " + mReadStatus);
         System.out.println("  ::--> Read Measure?: " + mReadMeasure);
         System.out.println("  ::--> Read Raw Score?: " + mReadRawScore);
         System.out.println("  ::--> Read Min Score?: " + mReadMinScore);
         System.out.println("  ::--> Read Max Score?: " + mReadMaxScore);
         System.out.println("  ::--> Read Completion Status?: " + mReadCompletionStatus);
         System.out.println("  ::--> Read Progress Measure?: " + mReadProgressMeasure);
         System.out.println("  ::--> Write Satisfied Status?: " + mWriteStatus);
         System.out.println("  ::--> Write Measure?: " + mWriteMeasure);
         System.out.println("  ::--> Write Raw Score?: " + mWriteRawScore);
         System.out.println("  ::--> Write Min Score?: " + mWriteMinScore);
         System.out.println("  ::--> Write Max Score?: " + mWriteMaxScore);
         System.out.println("  ::--> Write Completion Status?: " + mWriteCompletionStatus);
         System.out.println("  ::--> Write Progress Measure?: " + mWriteProgressMeasure);

         System.out.println("  :: SeqObjectiveMap   --> END   - dumpState");
      }
   }

   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object iToCompare)
   {
      if ( iToCompare instanceof SeqObjectiveMap )
      {
         SeqObjectiveMap other = (SeqObjectiveMap)iToCompare;
         return this.mGlobalObjID.equals(other.mGlobalObjID);
      }
      // it wasn't a obj map object, can't compare
      return false;
   }
   
   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   public int hashCode()
   {
      return (mGlobalObjID != null) ? (mGlobalObjID).hashCode() : 0;
   }
   
   /**
    * Merges this SeqObjectiveMap with another.
    * 
    * @param candidate The other.
    */
   public void merge(SeqObjectiveMap candidate)
   {
      if ( mGlobalObjID.equals(candidate.mGlobalObjID) )
      {
         mReadStatus = mReadStatus || candidate.mReadStatus;
         mReadMeasure = mReadMeasure || candidate.mReadMeasure;
         mReadRawScore = mReadRawScore || candidate.mReadRawScore;
         mReadMinScore = mReadMinScore || candidate.mReadMinScore;
         mReadMaxScore = mReadMaxScore || candidate.mReadMaxScore;
         mReadCompletionStatus = mReadCompletionStatus || candidate.mReadCompletionStatus;
         mReadProgressMeasure = mReadProgressMeasure || candidate.mReadProgressMeasure;
         mWriteStatus = mWriteStatus || candidate.mWriteStatus;
         mWriteMeasure = mWriteMeasure || candidate.mWriteMeasure;
         mWriteRawScore = mWriteRawScore || candidate.mWriteRawScore;
         mWriteMinScore = mWriteMinScore || candidate.mWriteMinScore;
         mWriteMaxScore = mWriteMaxScore || candidate.mWriteMaxScore;
         mWriteCompletionStatus = mWriteCompletionStatus || candidate.mWriteCompletionStatus;
         mWriteProgressMeasure = mWriteProgressMeasure || candidate.mWriteProgressMeasure;
      }
   }
}  // end SeqObjectiveMap
