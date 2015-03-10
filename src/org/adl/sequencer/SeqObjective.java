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
import java.util.Vector;

import org.adl.util.debug.DebugIndicator;

/**
 * Encapsulation mastery status tracking and behavior.<br><br>
 * 
 * <strong>Filename:</strong> SeqObjective.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>SeqObjective</code> encapsulates one objective description and its
 * associated objective maps as described in the IMS SS Specification SD
 * section.  This class provides seamless access to both local and global
 * objectives and provides for score-based mastery evaluation.<br><br>
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
 *     <li>IMS SS 1.0
 *     <li>SCORM 2004 4th Edition
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class SeqObjective implements Serializable
{

   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * Identifier of this objective.
    */
   public String mObjID = "_primary_";

   /** 
    * Indicates if this objective is satisfied by measure.
    */
   public boolean mSatisfiedByMeasure = false;

   /**
    * Indicates if the objective can be satisfied by measure when its activity
    * is still active.
    */
   public boolean mActiveMeasure = true;

   /**
    * Indicates the minimum measure used to satisfy this objective.<br><br>
    * Valid range: <code>[-1.0, 1.0]</code>
    */
   public double mMinMeasure = 1.0;

   /** 
    * Indicates if the objective contributes to rollup
    */
   public boolean mContributesToRollup = false;

   /**
    * Describes the mapping of local objective information to global objectives
    */
   public Vector mMaps = null;

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Constructors 
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
   /**
    * Default Constructor
    */
   public SeqObjective()
   {
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Constructors 
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
   /**
    * This method provides the state this <code>ADLObjective</code> object for
    * diagnostic purposes.<br>
    */
   public void dumpState()
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLObjective  --> BEGIN - dumpState");

         System.out.println("  ::--> ID:                 " + mObjID);
         System.out.println("  ::--> Satisfy by Measure: " +
                            mSatisfiedByMeasure);
         System.out.println("  ::--> Active Measure:     " + mActiveMeasure);
         System.out.println("  ::--> Min Measure:        " + mMinMeasure);
         System.out.println("  ::--> Contrib to Rollup:  " +
                            mContributesToRollup);
         System.out.println("  ::--> ------------------- <--::");

         if ( mMaps != null )
         {
            for ( int i = 0 ; i < mMaps.size(); i++ )
            {
               SeqObjectiveMap map = (SeqObjectiveMap)mMaps.elementAt(i);

               map.dumpState();
            }
         }

         System.out.println("  :: ADLObjective --> END   - dumpState");
      }
   }
   
   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object iToCompare)
   {
      if ( iToCompare instanceof SeqObjective )
      {
         SeqObjective other = (SeqObjective)iToCompare;
         return this.mObjID.equals(other.mObjID);
      }
      // it wasn't a seq obj object, can't compare
      return false;
   }
   
   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   public int hashCode()
   {
      return (mObjID != null) ? (mObjID).hashCode() : 0;
   }

   /**
    * Merges this SeqObjective with another.
    * 
    * @param toadd The other.
    */
   public void merge(SeqObjective toadd)
   {
      if ( toadd.equals(this) )
      {
         if ( mMaps != null )
         {
            for ( int i = 0; i < toadd.mMaps.size(); i++ )
            {
               SeqObjectiveMap candidate = (SeqObjectiveMap)toadd.mMaps.get(i);
               if ( mMaps.contains(candidate) )
               {
                  ((SeqObjectiveMap) mMaps.get(mMaps.indexOf(candidate))).merge(candidate);
               }
               else
               {
                  mMaps.add(candidate);
               }
            }
         }
         else
         {
            mMaps = toadd.mMaps;
         }
      }
   }
}  // end SeqObjective
