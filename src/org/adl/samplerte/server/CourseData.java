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

import java.util.List;

/**
 * Encapsulation of information required for launch.<br><br>
 * 
 * <strong>Filename:</strong> CourseData.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * The <code>CourseData</code> encapsulates the information about a specific
 * course returned from the from the Sample RTE Database.<br><br>
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
public class CourseData
{
   /**
    * The unique course identifier.  This is the identifier used internally
    * by the Sample RTE.
    */ 
   public String mCourseID = null;

   /**
    * The course title.  This is the title as defined by the &lt;title&gt;
    * sub-element of the &lt;organization&gt; element in the imsmanifest.xml
    * file
    */ 
   public String mCourseTitle = null;

   /**
    * The course import date and time.  This is the date and time that the SCORM
    * 2004 4th Edition Content Aggregation Package was imported into the Sample RTE    
    */ 
   public String mImportDateTime = null;

   /**
    * The satisfied value for the course.
    */ 
   public String mSatisfied = null;

   /**
    * The measure for the course.
    */ 
   public String mMeasure = null;

   /**
    * The completion status for the course.    
    */ 
   public String mCompleted = null;
   
   /**
    * The progress measure for the course.
    */
   public String mProgMeasure = null;
   
   /**
    * The start indicator for the course.
    */
   public boolean mStart = false;
   
   /**
    * The view TOC indicator for the course.
    */
   public boolean mTOC = false;
   
   /**
    * The suspend indicator for the course.
    */
   public boolean mSuspend = false;
   
   /**
    * The registration indicator for the course.
    */
   public boolean mRegistered = false;
   
   /**
    * List of items (SCOs) that make up this course 
    */
   public List<ItemData> items;
   
   /**
    * If this status was determined by an xAPI Statement, 
    * this is the statement id
    */
   public String refStmtID;
   
   public int active = 0;
   
   /**
    * The key for this record
    */
   public int activityID = 0;
   
   public CourseData() {}
   
   public CourseData(String courseID, String courseTitle)
   {
      mCourseID = courseID;
      mCourseTitle = courseTitle;
   }
   
   public String toString() 
   {
      return mCourseID + ": " + mCourseTitle;
   }
}


