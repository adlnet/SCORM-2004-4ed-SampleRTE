/******************************************************************************

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

******************************************************************************/

package org.adl.sequencer;

import org.adl.util.debug.DebugIndicator;
import org.adl.util.decode.decodeHandler;

import org.adl.samplerte.util.LMSDBHandler;

import java.util.HashMap;
import java.util.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import org.apache.xpath.XPathAPI;

/**
 * <strong>Filename:</strong> ADLSeqUtilities.java<br><br>
 * 
 * <strong>Description:</strong><br><br>
 * This class contains several static utility methods utilized by the
 * sequencing subsystem.<br><br>
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM 2004 4th
 * Edition Sample RTE.<br>
 * <br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>IMS SS Specification</li>
 *     <li>SCORM 2004 4th Edition</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class ADLSeqUtilities
{

   private static final String ADLSEQ_NS = "http://www.adlnet.org/xsd/adlseq_v1p3";
   private static final String IMSSS_NS = "http://www.imsglobal.org/xsd/imsss";
   /**
   * This controls display of log messages to the java console
   */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * Initializes an activity tree (<code>SeqActivityTree</code>) from the
    * contents of a content package.<br><br>
    * 
    * Currently, only this method exists. It accepts an &lt;organization&gt; 
    * XML node -- the default &lt;organization&gt; element from the parsed CP 
    * DOM.
    * 
    * This method constructs a completely initialized activity tree.  This
    * implementation is not optimized and is known to have scalablity issues.
    * <br><br>
    * 
    * NOTE: The construction of an activity tree is not coupled to the
    * implementation of the sequencer; the sequencer only requires that an
    * activity tree exists.
    * 
    * @param iOrg The <code>Node</code> object corresponding to the
    *              default <code>&lt;organization&gt;</code> element of the CP.
    * 
    * @param iColl The <code>Node</code> object corresponding to the set of
    * reusable sequencing definitions.
    *- 
    * @return An initialized activity tree (<code>SeqActivityTree</code>), or
    *         <code>null</code> if initialization fails.
    */
   public static SeqActivityTree buildActivityTree(Node iOrg, 
                                                   Node iColl)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " + 
                            "buildActivityTree");
      }

      SeqActivityTree tree = new SeqActivityTree();

      // Build and set the root of the activity tree
      SeqActivity root = ADLSeqUtilities.bulidActivityNode(iOrg, iColl);

      // Make sure the root was created successfully
      if ( root != null )
      {
         tree.setRoot(root);
         tree.setDepths();
         tree.setTreeCount();
         
         // For single activity packages the desired behavior is that starting the package 
         // will launch that activity and that no TOC will be displayed.
         // See PT 11524      
        
         if(root.getChildren(true).size() == 1 && 
               !((SeqActivity)root.getChildren(true).firstElement()).hasChildren(true)) 
         {
            if(root.getControlModeChoice() && !root.getControlModeFlow())
            {
               root.setControlModeChoice(false);
               root.setControlModeFlow(true);
            }
         }
      }
      else
      {
         // If any activity failed to initialize, the activity tree is invalid
         tree = null;
      }
      
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " + 
                            "buildActivityTree");
      }

      return tree;
   }

   /**
    * Displays the values of the <code>ADLTOC</code> objects that constitute a
    * table of contents.  This method is used for diagnostic purposes.
    * 
    * @param iTOC   A vector of <code>ADLTOC</code> objects describing the TOC.
    */
   public static void dumpTOC(Vector iTOC)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - dumpTOC");

         if ( iTOC != null )
         {

            System.out.println("  ::-->  " + iTOC.size());

            ADLTOC temp = null;

            for ( int i = 0; i < iTOC.size(); i++ )
            {
               temp = (ADLTOC)iTOC.elementAt(i);

               temp.dumpState();
            }
         }
         else
         {
            System.out.println("  ::--> NULL");
         }

         System.out.println("  :: ADLSeqUtilities  --> END   - dumpTOC");

      }
   }                      

   /**
    * This method ensures that global objective information exists for a set of
    * referenced global objectives.
    * 
    * @param iLearnerID  The identifier of the student being tracked.
    * 
    * @param iScopeID    The identifier of the objective's scope.
    * 
    * @param iObjList    A list of global objective IDs.
    */
   public static void createGlobalObjs(String iLearnerID,
                                       String iScopeID,
                                       Vector iObjList)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "createGlobalObjs");
         System.out.println("  ::-->  " + iLearnerID);
         System.out.println("  ::-->  " + iScopeID);
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iObjList != null )
            {
               try
               {
                  PreparedStatement stmtCheckRecord = null;

                  // Create the SQL string, convert it to a prepared stat.
                  String sqlCheckRecord = "SELECT * FROM Objectives WHERE " +
                                          "objID = ? AND " + 
                                          "learnerID = ? AND scopeID = ?";

                  stmtCheckRecord = conn.prepareStatement( sqlCheckRecord );
                  ResultSet objRS = null;

                  for ( int i = 0; i < iObjList.size(); i++ )
                  {

                     String objID = 
                        decodeHandler.encodeObjectiveID((String)iObjList.elementAt(i));

                     if ( _Debug )
                     {
                        System.out.println("  ::--> Checking for objective " +
                                           "--> " + iLearnerID +
                                           " [" + iScopeID + "]" +
                                           " // " + objID);
                     }

                     synchronized(stmtCheckRecord)
                     {
                        stmtCheckRecord.setString(1, objID);
                        stmtCheckRecord.setString(2, iLearnerID);

                        if ( iScopeID == null )
                        {
                           stmtCheckRecord.setString(3, "");     
                        }
                        else
                        {
                           stmtCheckRecord.setString(3, iScopeID);
                        }

                        objRS = stmtCheckRecord.executeQuery();
                     }

                     PreparedStatement stmtCreateRecord = null;

                     // the objective does not exist, add it
                     if ( !objRS.next() )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Creating objective " +
                                              "--> " + iLearnerID +
                                              " [" + iScopeID + "]" +
                                              " // " + objID);
                        }

                        // Create the SQL string, 
                        //   convert it to a prepared statement
                        String sqlCreateRecord = "INSERT INTO Objectives " +
                        "(objID, learnerID, scopeID, satisfied, measure, " +
                        "rawscore, minscore, maxscore, completion, progressmeasure) " +
                        "VALUES (?, ?, ? ,?, ?, ?, ?, ?, ?, ?)";

                        stmtCreateRecord =
                        conn.prepareStatement(sqlCreateRecord);

                        // Insert values into the prepared statement and 
                        // execute the query.
                        synchronized(stmtCreateRecord)
                        {
                           stmtCreateRecord.setString(1, objID);
                           stmtCreateRecord.setString(2, iLearnerID);

                           if ( iScopeID == null )
                           {
                              stmtCreateRecord.setString(3, "");
                           }
                           else
                           {
                              stmtCreateRecord.setString(3, iScopeID);
                           }

                           stmtCreateRecord.setString(4, "unknown");
                           stmtCreateRecord.setString(5, "unknown");
                           stmtCreateRecord.setString(6, "unknown");
                           stmtCreateRecord.setString(7, "unknown");
                           stmtCreateRecord.setString(8, "unknown");
                           stmtCreateRecord.setString(9, "unknown");
                           stmtCreateRecord.setString(10, "unknown");

                           stmtCreateRecord.executeUpdate();
                        }

                        // Close the prepared statement
                        stmtCreateRecord.close();
                     }
                  }

                  // Close the result set and prepared statement
                  objRS.close();
                  stmtCheckRecord.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL Objective List");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL StudentID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "createGlobalObjs");
      }
   }

   /**
    * This method deletes a set global objective information from the database
    * 
    * @param iLearnerID  The identifier of the student being tracked.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iObjList    A list of global objective IDs.
    */   
   public static void deleteGlobalObjs(String iLearnerID,
                                       String iScopeID,
                                       Vector iObjList)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "deleteGlobalObjs");
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iObjList != null )
            {
               try
               {
                  PreparedStatement stmtDeleteRecord = null;

                  // Create the SQL string, convert it to a prepared statement. 
                  String sqlDeleteRecord = "DELETE FROM Objectives " +
                                           "WHERE objID = ? AND " + 
                                           "learnerID = ? AND scopeID = ?";

                  stmtDeleteRecord = conn.prepareStatement(sqlDeleteRecord);


                  for ( int i = 0; i < iObjList.size(); i++ )
                  {

                     String objID = 
                        decodeHandler.encodeObjectiveID((String)iObjList.elementAt(i));

                     if ( _Debug )
                     {
                        System.out.println("  ::--> Attempting to delete " +
                                           "record for --> " + iLearnerID +
                                           " [" + iScopeID + "]" +
                                           " // " + objID);
                     }

                     // Insert values into the prepared statement and 
                     // execute the query.
                     synchronized(stmtDeleteRecord)
                     {
                        stmtDeleteRecord.setString(1, objID);
                        stmtDeleteRecord.setString(2, iLearnerID);

                        if ( iScopeID == null )
                        {
                           stmtDeleteRecord.setString(3, "");
                        }
                        else
                        {
                           stmtDeleteRecord.setString(3, iScopeID);
                        }

                        stmtDeleteRecord.executeUpdate();
                     }
                  }

                  // Close the prepared statement
                  stmtDeleteRecord.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL objective list");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL StudentID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "deleteGlobalObjs");
      }
   }

   /**
    * This method resets the global objective information for a set of
    * referenced global objectives.
    * 
    * @param iLearnerID  The identifier of the student being tracked.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iObjList    A list of global objective IDs.
    */
   public static void clearGlobalObjs(String iLearnerID,
                                      String iScopeID,
                                      Vector iObjList)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "clearGlobalObjs");
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iObjList != null )
            {

               try
               {
                  PreparedStatement stmtClearRecord = null;

                  // Create the SQL string, convert it to a prepared statement. 
                  String sqlClearRecord = "UPDATE Objectives " +
                                          "SET satisfied = ?, measure = ?, " +
                                          " rawscore = ?, minscore = ?, " +
                                          " maxscore = ?, completion = ?, " +
                                          " progressmeasure = ? " +
                                          "WHERE objID = ? AND " +  
                                          "learnerID = ? AND scopeID = ?";

                  stmtClearRecord = conn.prepareStatement(sqlClearRecord);

                  for ( int i = 0; i < iObjList.size(); i++ )
                  {

                     String objID = 
                        decodeHandler.encodeObjectiveID((String)iObjList.elementAt(i));

                     if ( _Debug )
                     {
                        System.out.println("  ::--> Attempting to clear " +
                                           "record for --> " + iLearnerID +
                                           " [" + iScopeID + "]" +
                                           " // " + objID);
                     }

                     // Insert values into the prepared statement and execute 
                     // the query.
                     synchronized(stmtClearRecord)
                     {
                        stmtClearRecord.setString(1, "unknown");
                        stmtClearRecord.setString(2, "unknown");
                        stmtClearRecord.setString(3, "unknown");
                        stmtClearRecord.setString(4, "unknown");
                        stmtClearRecord.setString(5, "unknown");
                        stmtClearRecord.setString(6, "unknown");
                        stmtClearRecord.setString(7, "unknown");
                        stmtClearRecord.setString(8, objID);
                        stmtClearRecord.setString(9, iLearnerID);

                        if ( iScopeID == null )
                        {
                           stmtClearRecord.setString(10, "");
                        }
                        else
                        {
                           stmtClearRecord.setString(10, iScopeID);
                        }

                        stmtClearRecord.executeUpdate();
                     }
                  }

                  // Close the prepared statement.
                  stmtClearRecord.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL objectives list");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL learnerID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }
      }


      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "clearGlobalObjs");
      }
   }

   /**
    * Retrieves the satisfied status associated with the global objective and
    * the student.
    * 
    * @param iObjID     The ID identifying the global shared objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @return The satisfied status associated with the global objective,
    *         or <code>null</code> if either the obj or learner ID is invalid.
    */
   public static String getGlobalObjSatisfied(String iObjID,
                                              String iLearnerID,
                                              String iScopeID)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getGlobalObjSatisfied");
         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
      }

      String satisfiedStatus = null;

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iObjID != null )
            {

               try
               {
                  PreparedStatement stmtSelectSatisfied= null;
                  ResultSet objRS = null;

                  // Create the SQL string, convert it to a prepared statement.
                  String sqlSelectSatisfied = "SELECT satisfied FROM " + 
                                              "Objectives WHERE " +
                                              "objID = ? AND " + 
                                              "learnerID = ? AND scopeID = ?";

                  stmtSelectSatisfied = 
                  conn.prepareStatement(sqlSelectSatisfied);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement 
                  //  and execute the query.
                  synchronized(stmtSelectSatisfied)
                  {
                     stmtSelectSatisfied.setString(1, objID);
                     stmtSelectSatisfied.setString(2, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtSelectSatisfied.setString(3, ""); 
                     }
                     else
                     {
                        stmtSelectSatisfied.setString(3, iScopeID);
                     }

                     objRS = stmtSelectSatisfied.executeQuery();
                  }

                  // Make sure a result set is returned
                  if ( objRS.next() )
                  {
                     satisfiedStatus = objRS.getString("satisfied");
                  }
                  else
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> No result set");
                     }

                     satisfiedStatus = null;
                  }

                  // Close result set
                  objRS.close();

                  // Close the prepared statement
                  stmtSelectSatisfied.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR : DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR : NULL comp ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR : NULL learnerID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + satisfiedStatus);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getGlobalObjSatisfied");
      }

      return satisfiedStatus;
   }

   /**
    * Sets the satisfied status associated with a global objective and student.
    * 
    * @param iObjID     The ID identifying the global objective information.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iSatisfied The desired satisfied status.
    * 
    * @return <code>true</code> if the set was successful; if an error occurred
    *         <code>false</code>.
    */
   public static boolean setGlobalObjSatisfied(String iObjID, 
                                               String iLearnerID,
                                               String iScopeID,
                                               String iSatisfied)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "setGlobalObjSatisfied");

         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
         System.out.println("  ::--> " + iSatisfied);
      }

      boolean success = true;


      // Validate vocabulary
      if ( !(iSatisfied.equals("unknown") || iSatisfied.equals("satisfied") || 
             iSatisfied.equals("notSatisfied")) )
      {

         success = false;

         if ( _Debug )
         {
            System.out.println("  ::--> Invalid value: " + iSatisfied);
            System.out.println("  ::-->  " + success);
            System.out.println("  :: ADLSeqUtilities  --> END   - " +
                               "setGlobalObjSatisfied");
         }

         return success;
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iObjID != null )
            {
               try
               {

                  PreparedStatement stmtUpdateSatisfied = null;

                  // Create the SQL string
                  String sqlUpdateSatisfied = "UPDATE Objectives SET " +
                                              "satisfied = ? " +
                                              "WHERE objID = ? AND " +
                                              "learnerID = ? AND scopeID = ?";

                  stmtUpdateSatisfied = 
                  conn.prepareStatement(sqlUpdateSatisfied);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Execute the query
                  synchronized(stmtUpdateSatisfied)
                  {
                     stmtUpdateSatisfied.setString(1, iSatisfied);
                     stmtUpdateSatisfied.setString(2, objID);
                     stmtUpdateSatisfied.setString(3, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtUpdateSatisfied.setString(4, "");
                     }
                     else
                     {
                        stmtUpdateSatisfied.setString(4, iScopeID);
                     }

                     stmtUpdateSatisfied.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtUpdateSatisfied.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> ERROR: DB Failure");
                     System.out.println(e.getMessage());

                     e.printStackTrace();
                  }

                  success = false;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL objective ID");
               }

               success = false;
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL learnerID");
            }

            success = false;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }

         success = false;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + success);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "setGlobalObjSatisfied");
      }

      return success;
   }

   /**
    * Retrieves the measure associated with the global objective and
    * the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @return The score associated with the shared competency, or
    *         <code>null</code> if either the obj or learner ID is invalid.
    */
   public static String getGlobalObjMeasure(String iObjID, 
                                            String iLearnerID,
                                            String iScopeID)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getGlobalObjMeasure");
      }

      String measure = null;

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtSelectMeasure = null;
                  ResultSet objRS = null;

                  // Create the SQL string and convert it to 
                  // a prepared statement.
                  String sqlSelectMeasure = "SELECT measure FROM Objectives " + 
                                            "WHERE objID = ? AND " +
                                            "learnerID = ? AND scopeID = ?";

                  stmtSelectMeasure = conn.prepareStatement(sqlSelectMeasure);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // query
                  synchronized( stmtSelectMeasure)
                  {
                     stmtSelectMeasure.setString(1, objID);
                     stmtSelectMeasure.setString(2, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtSelectMeasure.setString(3, "");    
                     }
                     else
                     {
                        stmtSelectMeasure.setString(3, iScopeID);
                     }

                     objRS = stmtSelectMeasure.executeQuery();
                  }
                  // Make sure a result set is returned
                  if ( objRS.next() )
                  {
                     measure = objRS.getString("measure");
                  }
                  else
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> No resultset");
                     }
                  }

                  // Close result set
                  objRS.close();

                  // Close the prepared statement 
                  stmtSelectMeasure.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR : DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR : NULL student ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR : NULL objective ID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + measure);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getGlobalObjMeasure");
      }

      return measure;
   }

   /**
    * Sets the measure associated with the global objective and the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iMeasure   The desired measure.
    * 
    * @return <code>true</code> if the set was successful; if an error occurred
    *         <code>false</code>.     
    */
   public static boolean setGlobalObjMeasure(String iObjID, 
                                             String iLearnerID,
                                             String iScopeID,
                                             String iMeasure)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "setGlobalObjMeasure");

         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
         System.out.println("  ::--> " + iMeasure);
      }

      boolean goodMeasure = true;
      boolean success = true;

      // Validate score
      if ( !iMeasure.equals("unknown") )
      {
         try
         {
            Double tempMeasure = new Double(iMeasure);
            double range = tempMeasure.doubleValue();

            if ( range < -1.0 || range > 1.0 )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Invalid range:  " + iMeasure);
               }

               // The measure is out of range -- ignore
               goodMeasure = false;
            }
         }
         catch ( NumberFormatException e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Invalid value:  " + iMeasure);
            }

            // Invalid format or 'Unknown'
            goodMeasure = false;
         }

         if ( !goodMeasure )
         {
            success = false;

            if ( _Debug )
            {
               System.out.println("  ::--> " + success);
               System.out.println("  :: ADLSeqUtilities  --> END   - " +
                                  "setGlobalObjMeasure");
            }

            return success;
         }
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtUpdateMeasure = null;

                  // Create the SQL string and covert it to a prepared statement
                  String sqlUpdateMeasure = "UPDATE Objectives SET " + 
                                            "measure = ? " +
                                            "WHERE objID = ? AND " + 
                                            "learnerID = ? AND scopeID = ?";

                  stmtUpdateMeasure = conn.prepareStatement(sqlUpdateMeasure);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // update query
                  synchronized( stmtUpdateMeasure )
                  {
                     stmtUpdateMeasure.setString(1, iMeasure);
                     stmtUpdateMeasure.setString(2, objID);
                     stmtUpdateMeasure.setString(3, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtUpdateMeasure.setString(4, "");
                     }
                     else
                     {
                        stmtUpdateMeasure.setString(4, iScopeID);
                     }

                     stmtUpdateMeasure.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtUpdateMeasure.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }

                  success = false;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL learnerID");
               }

               success = false;
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL obj ID");
            }

            success = false;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }

         success = false;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + success);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "setGlobalObjMeasure");
      }

      return success;
   }

   /**
    * Retrieves the raw score associated with the global objective and
    * the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @return The raw score associated with the shared competency, or
    *         <code>null</code> if either the obj or learner ID is invalid.
    */
   public static String getGlobalObjRawScore(String iObjID, 
                                            String iLearnerID,
                                            String iScopeID)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getGlobalObjRawScore");
      }

      String rawscore = null;

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtSelectRawScore = null;
                  ResultSet objRS = null;

                  // Create the SQL string and convert it to 
                  // a prepared statement.
                  String sqlSelectRawScore = "SELECT rawscore FROM Objectives " + 
                                            "WHERE objID = ? AND " +
                                            "learnerID = ? AND scopeID = ?";

                  stmtSelectRawScore = conn.prepareStatement(sqlSelectRawScore);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // query
                  synchronized( stmtSelectRawScore )
                  {
                     stmtSelectRawScore.setString(1, objID);
                     stmtSelectRawScore.setString(2, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtSelectRawScore.setString(3, "");    
                     }
                     else
                     {
                        stmtSelectRawScore.setString(3, iScopeID);
                     }

                     objRS = stmtSelectRawScore.executeQuery();
                  }
                  // Make sure a result set is returned
                  if ( objRS.next() )
                  {
                     rawscore = objRS.getString("rawscore");
                  }
                  else
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> No resultset");
                     }
                  }

                  // Close result set
                  objRS.close();

                  // Close the prepared statement 
                  stmtSelectRawScore.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR : DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR : NULL student ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR : NULL objective ID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + rawscore);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getGlobalObjRawScore");
      }

      return rawscore;
   }

   /**
    * Sets the raw score associated with the global objective and the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iRawScore   The desired raw score.
    * 
    * @return <code>true</code> if the set was successful; if an error occurred
    *         <code>false</code>.     
    */
   public static boolean setGlobalObjRawScore(String iObjID, 
                                             String iLearnerID,
                                             String iScopeID,
                                             String iRawScore)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "setGlobalObjRawScore");

         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
         System.out.println("  ::--> " + iRawScore);
      }

      boolean success = true;

      // Validate score
      if ( !iRawScore.equals("unknown") )
      {
         try
         {
            Double.parseDouble(iRawScore);
         }
         catch ( NumberFormatException e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Invalid value:  " + iRawScore);
            }

            // Invalid format
            success = false;

            if ( _Debug )
            {
               System.out.println("  ::--> " + success);
               System.out.println("  :: ADLSeqUtilities  --> END   - " +
                                  "setGlobalObjRawScore");
            }

            return success;
         }
         
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtUpdateRawScore = null;

                  // Create the SQL string and covert it to a prepared statement
                  String sqlUpdateRawScore = "UPDATE Objectives SET " + 
                                            "rawscore = ? " +
                                            "WHERE objID = ? AND " + 
                                            "learnerID = ? AND scopeID = ?";

                  stmtUpdateRawScore = conn.prepareStatement(sqlUpdateRawScore);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // update query
                  synchronized( stmtUpdateRawScore )
                  {
                     stmtUpdateRawScore.setString(1, iRawScore);
                     stmtUpdateRawScore.setString(2, objID);
                     stmtUpdateRawScore.setString(3, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtUpdateRawScore.setString(4, "");
                     }
                     else
                     {
                        stmtUpdateRawScore.setString(4, iScopeID);
                     }

                     stmtUpdateRawScore.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtUpdateRawScore.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }

                  success = false;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL learnerID");
               }

               success = false;
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL obj ID");
            }

            success = false;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }

         success = false;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + success);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "setGlobalObjRawScore");
      }

      return success;
   }
   
   /**
    * Retrieves the min score associated with the global objective and
    * the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @return The min score associated with the shared competency, or
    *         <code>null</code> if either the obj or learner ID is invalid.
    */
   public static String getGlobalObjMinScore(String iObjID, 
                                            String iLearnerID,
                                            String iScopeID)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getGlobalObjMinScore");
      }

      String minscore = null;

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtSelectMinScore = null;
                  ResultSet objRS = null;

                  // Create the SQL string and convert it to 
                  // a prepared statement.
                  String sqlSelectMinScore = "SELECT minscore FROM Objectives " + 
                                            "WHERE objID = ? AND " +
                                            "learnerID = ? AND scopeID = ?";

                  stmtSelectMinScore = conn.prepareStatement(sqlSelectMinScore);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // query
                  synchronized( stmtSelectMinScore)
                  {
                     stmtSelectMinScore.setString(1, objID);
                     stmtSelectMinScore.setString(2, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtSelectMinScore.setString(3, "");    
                     }
                     else
                     {
                        stmtSelectMinScore.setString(3, iScopeID);
                     }

                     objRS = stmtSelectMinScore.executeQuery();
                  }
                  // Make sure a result set is returned
                  if ( objRS.next() )
                  {
                     minscore = objRS.getString("minscore");
                  }
                  else
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> No resultset");
                     }
                  }

                  // Close result set
                  objRS.close();

                  // Close the prepared statement 
                  stmtSelectMinScore.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR : DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR : NULL student ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR : NULL objective ID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + minscore);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getGlobalObjMinScore");
      }

      return minscore;
   }

   /**
    * Sets the min score associated with the global objective and the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iMinScore   The desired min score.
    * 
    * @return <code>true</code> if the set was successful; if an error occurred
    *         <code>false</code>.     
    */
   public static boolean setGlobalObjMinScore(String iObjID, 
                                             String iLearnerID,
                                             String iScopeID,
                                             String iMinScore)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "setGlobalObjMinScore");

         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
         System.out.println("  ::--> " + iMinScore);
      }

      boolean success = true;

      // Validate score
      if ( !iMinScore.equals("unknown") )
      {
         try
         {
            Double.parseDouble(iMinScore);
         }
         catch ( NumberFormatException e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Invalid value:  " + iMinScore);
            }

            // Invalid format
            success = false;

            if ( _Debug )
            {
               System.out.println("  ::--> " + success);
               System.out.println("  :: ADLSeqUtilities  --> END   - " +
                                  "setGlobalObjMinScore");
            }

            return success;
         }
         
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtUpdateMinScore = null;

                  // Create the SQL string and covert it to a prepared statement
                  String sqlUpdateMinScore = "UPDATE Objectives SET " + 
                                            "minscore = ? " +
                                            "WHERE objID = ? AND " + 
                                            "learnerID = ? AND scopeID = ?";

                  stmtUpdateMinScore = conn.prepareStatement(sqlUpdateMinScore);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // update query
                  synchronized( stmtUpdateMinScore )
                  {
                     stmtUpdateMinScore.setString(1, iMinScore);
                     stmtUpdateMinScore.setString(2, objID);
                     stmtUpdateMinScore.setString(3, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtUpdateMinScore.setString(4, "");
                     }
                     else
                     {
                        stmtUpdateMinScore.setString(4, iScopeID);
                     }

                     stmtUpdateMinScore.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtUpdateMinScore.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }

                  success = false;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL learnerID");
               }

               success = false;
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL obj ID");
            }

            success = false;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }

         success = false;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + success);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "setGlobalObjMinScore");
      }

      return success;
   }
   
   /**
    * Retrieves the max score associated with the global objective and
    * the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @return The max score associated with the shared competency, or
    *         <code>null</code> if either the obj or learner ID is invalid.
    */
   public static String getGlobalObjMaxScore(String iObjID, 
                                            String iLearnerID,
                                            String iScopeID)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getGlobalObjMaxScore");
      }

      String maxscore = null;

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtSelectMaxScore = null;
                  ResultSet objRS = null;

                  // Create the SQL string and convert it to 
                  // a prepared statement.
                  String sqlSelectMaxScore = "SELECT maxscore FROM Objectives " + 
                                            "WHERE objID = ? AND " +
                                            "learnerID = ? AND scopeID = ?";

                  stmtSelectMaxScore = conn.prepareStatement(sqlSelectMaxScore);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // query
                  synchronized( stmtSelectMaxScore)
                  {
                     stmtSelectMaxScore.setString(1, objID);
                     stmtSelectMaxScore.setString(2, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtSelectMaxScore.setString(3, "");    
                     }
                     else
                     {
                        stmtSelectMaxScore.setString(3, iScopeID);
                     }

                     objRS = stmtSelectMaxScore.executeQuery();
                  }
                  // Make sure a result set is returned
                  if ( objRS.next() )
                  {
                     maxscore = objRS.getString("maxscore");
                  }
                  else
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> No resultset");
                     }
                  }

                  // Close result set
                  objRS.close();

                  // Close the prepared statement 
                  stmtSelectMaxScore.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR : DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR : NULL student ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR : NULL objective ID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + maxscore);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getGlobalObjMaxScore");
      }

      return maxscore;
   }

   /**
    * Sets the max score associated with the global objective and the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iMaxScore   The desired max score.
    * 
    * @return <code>true</code> if the set was successful; if an error occurred
    *         <code>false</code>.     
    */
   public static boolean setGlobalObjMaxScore(String iObjID, 
                                             String iLearnerID,
                                             String iScopeID,
                                             String iMaxScore)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "setGlobalObjMaxScore");

         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
         System.out.println("  ::--> " + iMaxScore);
      }

      boolean success = true;

      // Validate score
      if ( !iMaxScore.equals("unknown") )
      {
         try
         {
            Double.parseDouble(iMaxScore);
         }
         catch ( NumberFormatException e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Invalid value:  " + iMaxScore);
            }

            // Invalid format
            success = false;

            if ( _Debug )
            {
               System.out.println("  ::--> " + success);
               System.out.println("  :: ADLSeqUtilities  --> END   - " +
                                  "setGlobalObjMaxScore");
            }

            return success;
         }
         
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtUpdateMaxScore = null;

                  // Create the SQL string and covert it to a prepared statement
                  String sqlUpdateMaxScore = "UPDATE Objectives SET " + 
                                            "maxscore = ? " +
                                            "WHERE objID = ? AND " + 
                                            "learnerID = ? AND scopeID = ?";

                  stmtUpdateMaxScore = conn.prepareStatement(sqlUpdateMaxScore);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // update query
                  synchronized( stmtUpdateMaxScore )
                  {
                     stmtUpdateMaxScore.setString(1, iMaxScore);
                     stmtUpdateMaxScore.setString(2, objID);
                     stmtUpdateMaxScore.setString(3, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtUpdateMaxScore.setString(4, "");
                     }
                     else
                     {
                        stmtUpdateMaxScore.setString(4, iScopeID);
                     }

                     stmtUpdateMaxScore.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtUpdateMaxScore.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }

                  success = false;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL learnerID");
               }

               success = false;
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL obj ID");
            }

            success = false;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }

         success = false;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + success);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "setGlobalObjMaxScore");
      }

      return success;
   }
   
   /**
    * Retrieves the completion status associated with the global objective and
    * the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @return The completion status associated with the global objective, or
    *         <code>null</code> if either the obj or learner ID is invalid.
    */
   public static String getGlobalObjCompletion(String iObjID, 
                                            String iLearnerID,
                                            String iScopeID)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getGlobalObjCompletion");
      }

      String completion = null;

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtSelectCompletion = null;
                  ResultSet objRS = null;

                  // Create the SQL string and convert it to 
                  // a prepared statement.
                  String sqlSelectCompletion = "SELECT completion FROM Objectives " + 
                                            "WHERE objID = ? AND " +
                                            "learnerID = ? AND scopeID = ?";

                  stmtSelectCompletion = conn.prepareStatement(sqlSelectCompletion);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // query
                  synchronized( stmtSelectCompletion )
                  {
                     stmtSelectCompletion.setString(1, objID);
                     stmtSelectCompletion.setString(2, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtSelectCompletion.setString(3, "");    
                     }
                     else
                     {
                        stmtSelectCompletion.setString(3, iScopeID);
                     }

                     objRS = stmtSelectCompletion.executeQuery();
                  }
                  // Make sure a result set is returned
                  if ( objRS.next() )
                  {
                     completion = objRS.getString("completion");
                  }
                  else
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> No resultset");
                     }
                  }

                  // Close result set
                  objRS.close();

                  // Close the prepared statement 
                  stmtSelectCompletion.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR : DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR : NULL student ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR : NULL objective ID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + completion);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getGlobalObjCompletion");
      }

      return completion;
   }

   /**
    * Sets the completion status associated with the global objective and the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iCompletion   The desired completion status.
    * 
    * @return <code>true</code> if the set was successful; if an error occurred
    *         <code>false</code>.     
    */
   public static boolean setGlobalObjCompletion(String iObjID, 
                                             String iLearnerID,
                                             String iScopeID,
                                             String iCompletion)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "setGlobalObjCompletion");

         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
         System.out.println("  ::--> " + iCompletion);
      }

      boolean success = true;


      // Validate vocabulary
      if ( !(iCompletion.equals("unknown") || iCompletion.equals("completed") || 
            iCompletion.equals("incomplete") || iCompletion.equals("not attempted")) )
      {

         success = false;

         if ( _Debug )
         {
            System.out.println("  ::--> Invalid value: " + iCompletion);
            System.out.println("  ::-->  " + success);
            System.out.println("  :: ADLSeqUtilities  --> END   - " +
                               "setGlobalObjCompletion");
         }

         return success;
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iObjID != null )
            {
               try
               {

                  PreparedStatement stmtUpdateCompletion = null;

                  // Create the SQL string
                  String sqlUpdateCompletion = "UPDATE Objectives SET " +
                                              "completion = ? " +
                                              "WHERE objID = ? AND " +
                                              "learnerID = ? AND scopeID = ?";

                  stmtUpdateCompletion = 
                  conn.prepareStatement(sqlUpdateCompletion);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Execute the query
                  synchronized(stmtUpdateCompletion)
                  {
                     stmtUpdateCompletion.setString(1, (iCompletion.equals("not attempted"))?"incomplete":iCompletion);
                     stmtUpdateCompletion.setString(2, objID);
                     stmtUpdateCompletion.setString(3, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtUpdateCompletion.setString(4, "");
                     }
                     else
                     {
                        stmtUpdateCompletion.setString(4, iScopeID);
                     }

                     stmtUpdateCompletion.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtUpdateCompletion.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> ERROR: DB Failure");
                     System.out.println(e.getMessage());

                     e.printStackTrace();
                  }

                  success = false;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL objective ID");
               }

               success = false;
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL learnerID");
            }

            success = false;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }

         success = false;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + success);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "setGlobalObjCompletion");
      }

      return success;
   }
   
   /**
    * Retrieves the progress measure associated with the global objective and
    * the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @return The progress measure associated with the shared competency, or
    *         <code>null</code> if either ID is invalid.
    */
   public static String getGlobalObjProgressMeasure(String iObjID, 
                                            String iLearnerID,
                                            String iScopeID)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getGlobalObjProgressMeasure");
      }

      String measure = null;

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtSelectMeasure = null;
                  ResultSet objRS = null;

                  // Create the SQL string and convert it to 
                  // a prepared statement.
                  String sqlSelectMeasure = "SELECT progressmeasure FROM Objectives " + 
                                            "WHERE objID = ? AND " +
                                            "learnerID = ? AND scopeID = ?";

                  stmtSelectMeasure = conn.prepareStatement(sqlSelectMeasure);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // query
                  synchronized( stmtSelectMeasure)
                  {
                     stmtSelectMeasure.setString(1, objID);
                     stmtSelectMeasure.setString(2, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtSelectMeasure.setString(3, "");    
                     }
                     else
                     {
                        stmtSelectMeasure.setString(3, iScopeID);
                     }

                     objRS = stmtSelectMeasure.executeQuery();
                  }
                  // Make sure a result set is returned
                  if ( objRS.next() )
                  {
                     measure = objRS.getString("progressmeasure");
                  }
                  else
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> No resultset");
                     }
                  }

                  // Close result set
                  objRS.close();

                  // Close the prepared statement 
                  stmtSelectMeasure.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR : DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR : NULL student ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR : NULL objective ID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR : NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + measure);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getGlobalObjProgressMeasure");
      }

      return measure;
   }

   /**
    * Sets the progress measure associated with the global objective and the student.
    * 
    * @param iObjID     The ID identifying the desired global objective.
    * 
    * @param iLearnerID The ID identifying the student.
    * 
    * @param iScopeID   The identifier of the objective's scope.
    * 
    * @param iProgMeasure   The desired progress measure.
    * 
    * @return <code>true</code> if the set was successful; if an error occurred
    *         <code>false</code>.     
    */
   public static boolean setGlobalObjProgressMeasure(String iObjID, 
                                             String iLearnerID,
                                             String iScopeID,
                                             String iProgMeasure)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "setGlobalObjProgressMeasure");

         System.out.println("  ::--> " + iObjID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iScopeID);
         System.out.println("  ::--> " + iProgMeasure);
      }

      boolean goodMeasure = true;
      boolean success = true;

      // Validate score
      if ( !iProgMeasure.equals("unknown") )
      {
         try
         {
            double range = Double.parseDouble(iProgMeasure);

            if ( range < 0 || range > 1.0 )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Invalid range:  " + iProgMeasure);
               }

               // The measure is out of range -- ignore
               goodMeasure = false;
            }
         }
         catch ( NumberFormatException e )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Invalid value:  " + iProgMeasure);
            }

            // Invalid format or 'Unknown'
            goodMeasure = false;
         }

         if ( !goodMeasure )
         {
            success = false;

            if ( _Debug )
            {
               System.out.println("  ::--> " + success);
               System.out.println("  :: ADLSeqUtilities  --> END   - " +
                                  "setGlobalObjProgressMeasure");
            }

            return success;
         }
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iObjID != null )
         {
            if ( iLearnerID != null )
            {
               try
               {
                  PreparedStatement stmtUpdateMeasure = null;

                  // Create the SQL string and covert it to a prepared statement
                  String sqlUpdateMeasure = "UPDATE Objectives SET " + 
                                            "progressmeasure = ? " +
                                            "WHERE objID = ? AND " + 
                                            "learnerID = ? AND scopeID = ?";

                  stmtUpdateMeasure = conn.prepareStatement(sqlUpdateMeasure);

                  String objID = 
                     decodeHandler.encodeObjectiveID(iObjID);
                  
                  // Insert values into the prepared statement and execute the 
                  // update query
                  synchronized( stmtUpdateMeasure )
                  {
                     stmtUpdateMeasure.setString(1, iProgMeasure);
                     stmtUpdateMeasure.setString(2, objID);
                     stmtUpdateMeasure.setString(3, iLearnerID);

                     if ( iScopeID == null )
                     {
                        stmtUpdateMeasure.setString(4, "");
                     }
                     else
                     {
                        stmtUpdateMeasure.setString(4, iScopeID);
                     }

                     stmtUpdateMeasure.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtUpdateMeasure.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }

                  success = false;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL learnerID");
               }

               success = false;
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL obj ID");
            }

            success = false;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }

         success = false;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + success);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "setGlobalObjProgressMeasure");
      }

      return success;
   }

   /**
    * Creates a status record associated with a given activity tree's root
    * and a given learner.
    * 
    * @param iCourseID  The ID identifying the activity tree.
    * 
    * @param iLearnerID The ID identifying the student.
    */
   public static void createCourseStatus(String iCourseID,
                                         String iLearnerID)
   {  
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "createCourseStatus");
         System.out.println("  ::-->  " + iCourseID);
         System.out.println("  ::-->  " + iLearnerID);
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iCourseID != null )
            {
               try
               {
                  PreparedStatement stmtCheckRecord = null;

                  // Create the SQL string, convert it to a prepared stat.
                  String sqlCheckRecord = "SELECT * FROM CourseStatus WHERE " +
                                          "courseID = ? AND " + 
                                          "learnerID = ?";

                  stmtCheckRecord = conn.prepareStatement( sqlCheckRecord );
                  ResultSet objRS = null;

                  synchronized(stmtCheckRecord)
                  {
                     stmtCheckRecord.setString(1, iCourseID);
                     stmtCheckRecord.setString(2, iLearnerID);

                     objRS = stmtCheckRecord.executeQuery();
                  }

                  PreparedStatement stmtCreateRecord = null;

                  // the objective does not exist, add it
                  if ( !objRS.next() )
                  {

                     if ( _Debug )
                     {
                        System.out.println("  ::--> Creating course status " +
                                           "--> " + iCourseID +
                                           "--> " + iLearnerID);
                     }

                     // Create the SQL string, 
                     //   convert it to a prepared statement
                     String sqlCreateRecord = "INSERT INTO CourseStatus " +
                                              "(courseID, learnerID, " + 
                                              "satisfied, measure, " +
                                              "completed, progmeasure) " +
                                              "VALUES (?, ?, ? ,?, ?, ?)";

                     stmtCreateRecord =
                     conn.prepareStatement(sqlCreateRecord);

                     // Insert values into the prepared statement and 
                     // execute the query.
                     synchronized(stmtCreateRecord)
                     {
                        stmtCreateRecord.setString(1, iCourseID);
                        stmtCreateRecord.setString(2, iLearnerID);

                        stmtCreateRecord.setString(3, "unknown");
                        stmtCreateRecord.setString(4, "unknown");
                        stmtCreateRecord.setString(5, "unknown");
                        stmtCreateRecord.setString(6, "unknown");

                        stmtCreateRecord.executeUpdate();
                     }

                     // Close the prepared statement
                     stmtCreateRecord.close();
                  }


                  // Close the result set and prepared statement
                  objRS.close();
                  stmtCheckRecord.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL Course ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL Student ID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "createCourseStatus");
      }
   }


   /**
    * Sets the status associated with a given activity tree's root and a given
    * learner.
    * 
    * @param iCourseID  The ID identifing the activity tree.
    * 
    * @param iLearnerID The ID identifing the student.
    * 
    * @param iSatisfied The course's satisfied status.
    * 
    * @param iMeasure   The course's measure.
    * 
    * @param iCompleted The course's completion status.
    * 
    * @return <code>true</code> if the set was successful; if an error occured
    *         <code>false</code>.
    */
   public static boolean setCourseStatus(String iCourseID, 
                                         String iLearnerID,
                                         String iSatisfied,
                                         String iMeasure,
                                         String iCompleted,
                                         String iProgMeasure)
   {  

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "setCourseStatus");

         System.out.println("  ::--> " + iCourseID);
         System.out.println("  ::--> " + iLearnerID);
         System.out.println("  ::--> " + iSatisfied);
         System.out.println("  ::--> " + iMeasure);
         System.out.println("  ::--> " + iCompleted);
         System.out.println("  ::--> " + iProgMeasure);
      }

      boolean success = true;


      // Validate vocabulary
      if ( !(iSatisfied.equals("unknown") || iSatisfied.equals("satisfied") || 
             iSatisfied.equals("notSatisfied")) )
      {

         success = false;

         if ( _Debug )
         {
            System.out.println("  ::--> Invalid value: " + iSatisfied);
            System.out.println("  ::-->  " + success);
            System.out.println("  :: ADLSeqUtilities  --> END   - " +
                               "setCourseStatus");
         }

         return success;
      }

      // Validate vocabulary
      if ( !(iCompleted.equals("unknown") || iCompleted.equals("completed") || 
             iCompleted.equals("incomplete")) )
      {

         success = false;

         if ( _Debug )
         {
            System.out.println("  ::--> Invalid value: " + iCompleted);
            System.out.println("  ::-->  " + success);
            System.out.println("  :: ADLSeqUtilities  --> END   - " +
                               "setCourseStatus");
         }

         return success;
      }

      // Validate measure range
      try
      {
         double measure = Double.parseDouble(iMeasure);

         if (measure < -1.0 || measure > 1.0)
         {
            success = false;
         }
      }
      catch (Exception e)
      {
         success = false;
      }

      if ( !success )
      {

         if ( _Debug )
         {
            System.out.println("  ::--> Invalid value: " + iMeasure);
            System.out.println("  ::-->  " + success);
            System.out.println("  :: ADLSeqUtilities  --> END   - " +
                               "setCourseStatus");
         }

         return success;
      }
      
      // Validate measure range
      try
      {
         double progmeasure = Double.parseDouble(iProgMeasure);

         if (progmeasure < 0.0 || progmeasure > 1.0)
         {
            success = false;
         }
      }
      catch (Exception e)
      {
         success = false;
      }

      if ( !success )
      {

         if ( _Debug )
         {
            System.out.println("  ::--> Invalid value: " + iProgMeasure);
            System.out.println("  ::-->  " + success);
            System.out.println("  :: ADLSeqUtilities  --> END   - " +
                               "setCourseStatus");
         }

         return success;
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iCourseID != null )
            {
               try
               {

                  PreparedStatement stmtUpdateSatisfied = null;

                  // Create the SQL string
                  String sqlUpdateSatisfied = "UPDATE CourseStatus " + 
                                              "SET " +
                                              "satisfied = ? ," +
                                              "measure = ? ," +
                                              "completed = ? ," +
                                              "progmeasure = ? " +
                                              "WHERE courseID = ? AND " +
                                              "learnerID = ?";

                  stmtUpdateSatisfied = 
                  conn.prepareStatement(sqlUpdateSatisfied);

                  // Execute the query
                  synchronized(stmtUpdateSatisfied)
                  {
                     stmtUpdateSatisfied.setString(1, iSatisfied);
                     stmtUpdateSatisfied.setString(2, iMeasure);
                     stmtUpdateSatisfied.setString(3, iCompleted);
                     stmtUpdateSatisfied.setString(4, iProgMeasure);
                     stmtUpdateSatisfied.setString(5, iCourseID);
                     stmtUpdateSatisfied.setString(6, iLearnerID);

                     stmtUpdateSatisfied.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtUpdateSatisfied.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> ERROR: DB Failure");
                     System.out.println(e.getMessage());

                     e.printStackTrace();
                  }

                  success = false;
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL course ID");
               }

               success = false;
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL learner ID");
            }

            success = false;
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }

         success = false;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> " + success);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "setCourseStatus");
      }

      return success;
   }

   /**
    * This method deletes a course associated with a learner from the DB
    * 
    * @param iCourseID  The ID identifing the activity tree.
    *  
    * @param iLearnerID  The identifier of the student being tracked.
    * 
    */   
   public static void deleteCourseStatus(String iCourseID,
                                         String iLearnerID)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "deleteCourseStatus");
      }

      // Get a connection to the global objective DB
      Connection conn = LMSDBHandler.getConnection();

      if ( conn != null )
      {
         if ( iLearnerID != null )
         {
            if ( iCourseID != null )
            {
               try
               {
                  PreparedStatement stmtDeleteRecord = null;

                  // Create the SQL string, convert it to a prepared statement. 
                  String sqlDeleteRecord = "DELETE * FROM CourseStatus " +
                                           "WHERE courseID = ? AND " + 
                                           "learnerID = ?";

                  stmtDeleteRecord = conn.prepareStatement(sqlDeleteRecord);

                  // Insert values into the prepared statement and 
                  // execute the query.
                  synchronized(stmtDeleteRecord)
                  {
                     stmtDeleteRecord.setString(1, iCourseID);
                     stmtDeleteRecord.setString(2, iLearnerID);

                     stmtDeleteRecord.executeUpdate();
                  }

                  // Close the prepared statement
                  stmtDeleteRecord.close();
               }
               catch ( Exception e )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::-->  ERROR: DB Failure");
                     e.printStackTrace();
                  }
               }
            }
            else
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> ERROR: NULL course ID");
               }
            }
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::--> ERROR: NULL student ID");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::--> ERROR: NULL connection");
         }
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "deleteCourseStatus");
      }
   }

   /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
   
    Private Methods
   
   -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

   /**
    * Initializes one activity (<code>SeqActivity</code>) that will be added to
    * an activity tree.
    * 
    * @param iNode   A node from the DOM tree of an element containing
    *                sequencing information.
    * 
    * @param iColl   The collection of reusable sequencing information.
    * 
    * @return An initialized activity (<code>SeqActivity</code>), or <code>
    *         null</code> if there was an error initializing the activity.
    */
   private static SeqActivity bulidActivityNode(Node iNode, Node iColl)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " + 
                            "buildActivityNode");
      }

      SeqActivity act = new SeqActivity();

      boolean error = false;

      String tempVal = null;

      // Set the activity's ID -- this is a required attribute
      act.setID(ADLSeqUtilities.getAttribute(iNode, "identifier"));

      // Get the activity's resource ID -- if it exsits
      tempVal = ADLSeqUtilities.getAttribute(iNode, "identifierref");
      if ( tempVal != null )
      {
         if ( !isEmpty(tempVal) )
         {
            act.setResourceID(tempVal);
         }
      }

      // Check if the activity is visible
      tempVal = ADLSeqUtilities.getAttribute(iNode, "isvisible");
      if ( tempVal != null )
      {
         if ( !isEmpty(tempVal) )
         {
            act.setIsVisible((new Boolean(tempVal)).booleanValue());
         }
      }

      // Get the children elements of this activity 
      NodeList children = iNode.getChildNodes();

      // Initalize this activity from the information in the DOM  
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("item") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found an <item> element");
               }

               // Initialize the nested activity
               SeqActivity nestedAct = ADLSeqUtilities.
                                       bulidActivityNode(curNode,
                                                         iColl);

               // Make sure this activity was created successfully
               if ( nestedAct != null )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> Adding child");
                  }

                  act.addChild(nestedAct);

               }
               else
               {
                  error = true;
               }
            }
            else if ( curNode.getLocalName().equals("title") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <title> element");
               }

               act.setTitle(ADLSeqUtilities.getElementText(curNode, null));
            }
            else if ( curNode.getLocalName().equals("completionThreshold") )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <adlcp:completionThreshold> element");
               }
               
               getCompThresholdAttributes(act, curNode);
            }
            else if ( curNode.getLocalName().equals("data") )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <adlcp:data> element");
               }
               
               getDataStoreMaps(act, curNode);
            }
            else if ( curNode.getLocalName().equals("sequencing") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <sequencing> element");
               }

               Node seqInfo = curNode;

               // Check to see if the sequencing information is referenced in 
               // the <sequencingCollection>
               tempVal = ADLSeqUtilities.getAttribute(curNode, "IDRef");
               if ( tempVal != null )
               {
                  // Combine local and global sequencing information
                  // Get the referenced Global sequencing information
                  String search = "imsss:sequencing[normalize-space(@ID)=normalize-space('" + tempVal + "')]";

                  if ( _Debug )
                  {
                     System.out.println("  ::--> Looking for XPATH --> " +
                                        search);
                  }

                  // Use the referenced set of sequencing information
                  Node seqGlobal = null;

                  try
                  {
                     seqGlobal = XPathAPI.selectSingleNode(iColl, search);
                  }
                  catch ( Exception e )
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> ERROR : In transform");
                        e.printStackTrace();
                     }
                  }

                  if ( seqGlobal != null )
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> FOUND");
                     }
                  }
                  else
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> ERROR: Not Found");
                     }

                     seqInfo = null;
                     error = true;
                  }

                  if ( !error )
                  {
                     // Get the local sequencing element children
                     NodeList seqChildren = seqInfo.getChildNodes();
                     
                     //Loop through the collection
                     NodeList seqColChildren = seqGlobal.getChildNodes();
                     boolean localExists = false;
                     for ( int k = 0; k < seqColChildren.getLength(); k++ )
                     {
                        Node scNode = seqColChildren.item(k);
                        if (scNode.getNodeType() == Node.ELEMENT_NODE )
                        {
                           if ( _Debug )
                           {
                              System.out.println("  ::--> SC definition");
                              System.out.println("  ::-->   " + k);
                              System.out.println("  ::-->  <" +
                                    scNode.getLocalName() +
                              ">");
                           }
                           // Search the local nodes to see if the collection node is already there
                           for ( int l = 0; l < seqChildren.getLength(); l++ )
                           {
                              Node localNode = seqChildren.item(l);
                              if ( localNode.getNodeType() == Node.ELEMENT_NODE )
                              {
                                 if ( _Debug )
                                 {
                                    System.out.println("  ::--> Local definition");
                                    System.out.println("  ::-->   " + l);
                                    System.out.println("  ::-->  <" +
                                          localNode.getLocalName() +
                                    ">");
                                 }
                                 // If the element already exists locally
                                 if ( localNode.getNodeName().equals(scNode.getNodeName()) &&
                                       localNode.getNamespaceURI().equals(scNode.getNamespaceURI()) )
                                 {
                                    localExists = true;
                                    break;
                                 }
                              }
                           }
                           // It didn't exist locally, append it
                           if ( !localExists )
                           {
                              // Add this to the global sequencing info
                              try
                              {
                                 Node newLocal = scNode.cloneNode(true);
                                 seqInfo.appendChild(newLocal);
                              }
                              catch ( org.w3c.dom.DOMException e )
                              {
                                 if ( _Debug )
                                 {
                                    System.out.println("  ::--> ERROR: ");
                                    e.printStackTrace();
                                 }

                                 error = true;
                                 seqInfo = null;
                              }
                              
                           }
                           localExists = false;
                        }
                     }
                  }
               }

               // If we have an node to look at, extract its sequencing info
               if ( seqInfo != null )
               {
                  // Record this activity's sequencing XML fragment
//                  XMLSerializer serializer = new XMLSerializer();

// -+- TODO -+-
//                  serializer.setNewLine("CR-LF");
//                  act.setXMLFragment(serializer.writeToString(seqInfo));

                  // Extract the sequencing information for this activity
                  error = !ADLSeqUtilities.extractSeqInfo(seqInfo, act);

                  if ( _Debug )
                  {
                     System.out.println("  ::--> Extracted Sequencing Info");
                  }
               }
            }
         }
      }

      // Make sure this activity either has an associated resource or children
      if ( act.getResourceID() == null && !act.hasChildren(true) )
      {
         // This is not a vaild activity -- ignore it
         error = true;
      }

      // If the activity failed to initialize, clear the variable
      if ( error )
      {
         act = null;
      }

      if ( _Debug )
      {
         System.out.println("  ::--> error == " + error);
         System.out.println("  :: ADLSeqUtilities  --> END   - " + 
                            "buildActivityNode");
      }

      return act;
   }

   /**
    * Walks through the completionThreshold node collecting the attributes.
    * 
    * @param ioAct The associated activity being initialized. 
    * @param iCurNode The current node to process.
    */
   private static void getCompThresholdAttributes(SeqActivity ioAct, Node iCurNode)
   {
      String tempval = ADLSeqUtilities.getAttribute(iCurNode, "completedByMeasure");
      
      if ( tempval != null )
      {
         ioAct.setProgressDeterminedByMeasure(Boolean.valueOf(tempval).booleanValue());
      }
      
      tempval = ADLSeqUtilities.getAttribute(iCurNode, "minProgressMeasure");
      
      if ( tempval != null )
      {
         try
         {
            ioAct.setProgressThreshold(Double.parseDouble(tempval));
         }
         catch ( NumberFormatException nfe )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Error - minProgressMeasure was not a number: " + tempval);
            }
         }
      }
      
      tempval = ADLSeqUtilities.getAttribute(iCurNode, "progressWeight");
      
      if ( tempval != null )
      {
         try
         {
            ioAct.setProgressWeight(Double.parseDouble(tempval));
         }
         catch ( NumberFormatException nfe )
         {
            if ( _Debug )
            {
               System.out.println("  ::--> Error - progressWeight was not a number: " + tempval);
            }
         }
      }
   }

   /**
    * Walks through the data node collecting the maps and the attributes.
    * 
    * @param ioAct The associated activity being initialized. 
    * @param iCurNode The current node to process.
    */
   private static void getDataStoreMaps(SeqActivity ioAct, Node iCurNode)
   {
      NodeList children = iCurNode.getChildNodes();
      
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("map") )
            {
               String tempval = ADLSeqUtilities.getAttribute(curNode, "targetID");
               String id = (tempval != null) ? tempval : "";
               
               tempval = ADLSeqUtilities.getAttribute(curNode, "readSharedData"); 
               boolean readable = (tempval == null || tempval.equalsIgnoreCase("true"));
               
               tempval = ADLSeqUtilities.getAttribute(curNode, "writeSharedData");
               boolean writeable = (tempval == null || tempval.equalsIgnoreCase("true"));
               
               ioAct.addDataStore(id, readable, writeable);
            }
         }
      }
   }

   /**
    * Extracts the contents of the IMS SS <code>&lt;sequencing&gt;</code> 
    * element and initializes the associated activity.
    * 
    * @param iNode The DOM node associated with the IMS SS 
    *        <code>&lt;sequencing&gt;</code>) element.
    * 
    * @param ioAct The associated activity being initialized.
    * 
    * @return <code>true</code> if the sequencing information extracted
    *         successfully, otherwise <code>false</code>.
    */
   private static boolean extractSeqInfo(Node iNode, SeqActivity ioAct)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " + 
                            "extractSeqInfo");
      }

      boolean ok = true;
      String tempVal = null;

      // Get the children elements of <sequencing>
      NodeList children = iNode.getChildNodes();

      // Initalize this activity's sequencing information  
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("controlMode") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <controlMode> element");
               }

               // Look for 'choice'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "choice");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.
                     setControlModeChoice(
                                         (new 
                                          Boolean(tempVal)).booleanValue());
                  }
               }

               // Look for 'choiceExit'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "choiceExit");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.
                     setControlModeChoiceExit((new Boolean(tempVal)).
                                               booleanValue());
                  } 
               }

               // Look for 'flow'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "flow");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.
                     setControlModeFlow(
                                       (new Boolean(tempVal)).booleanValue());   
                  }
               }

               // Look for 'forwardOnly'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "forwardOnly");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.
                     setControlForwardOnly(
                                          (new
                                           Boolean(tempVal)).booleanValue());
                  }
               }

               // Look for 'useCurrentAttemptObjectiveInfo'
               tempVal = 
               ADLSeqUtilities.
               getAttribute(curNode, "useCurrentAttemptObjectiveInfo");

               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.
                     setUseCurObjective((new Boolean(tempVal)).booleanValue()); 
                  }
               }

               // Look for 'useCurrentAttemptProgressInfo'
               tempVal = 
               ADLSeqUtilities.
               getAttribute(curNode, "useCurrentAttemptProgressInfo");

               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.
                     setUseCurProgress((new Boolean(tempVal)).booleanValue());
                  }
               }
            }
            else if ( curNode.getLocalName().equals("sequencingRules") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <sequencingRules> " + 
                                     "element");
               }

               ok = ADLSeqUtilities.getSequencingRules(curNode, ioAct);
            }
            else if ( curNode.getLocalName().equals("limitConditions") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <limitConditions> " + 
                                     "element");
               }

               // Look for 'attemptLimit'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "attemptLimit");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setAttemptLimit(new Long(tempVal));
                  }
               }

               // Look for 'attemptAbsoluteDurationLimit'
               tempVal = 
               ADLSeqUtilities.
               getAttribute(curNode, "attemptAbsoluteDurationLimit");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setAttemptAbDur(tempVal);
                  }
               }

               // Look for 'attemptExperiencedDurationLimit'
               tempVal = 
               ADLSeqUtilities.
               getAttribute(curNode, "attemptExperiencedDurationLimit");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setAttemptExDur(tempVal);   
                  }
               }

               // Look for 'activityAbsoluteDurationLimit'
               tempVal = 
               ADLSeqUtilities.
               getAttribute(curNode, "activityAbsoluteDurationLimit");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setActivityAbDur(tempVal);
                  }
               }

               // Look for 'activityExperiencedDurationLimit'
               tempVal = 
               ADLSeqUtilities.
               getAttribute(curNode, "activityExperiencedDurationLimit");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setActivityExDur(tempVal);
                  }
               }

               // Look for 'beginTimeLimit'
               tempVal = 
               ADLSeqUtilities.getAttribute(curNode, "beginTimeLimit");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setBeginTimeLimit(tempVal);
                  }
               }

               // Look for 'endTimeLimit'
               tempVal = 
               ADLSeqUtilities.getAttribute(curNode, "endTimeLimit");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setEndTimeLimit(tempVal);
                  }
               }
            }
            else if ( curNode.getLocalName().equals("auxiliaryResources") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <auxiliaryResourcees> " 
                                     + "element");
               }

               ok = ADLSeqUtilities.getAuxResources(curNode, ioAct);
            }
            else if ( curNode.getLocalName().equals("rollupRules") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <rollupRules> " + 
                                     "element");
               }

               ok = ADLSeqUtilities.getRollupRules(curNode, ioAct);

            }
            else if ( curNode.getLocalName().equals("objectives") )
            {
               String ns = curNode.getNamespaceURI();
               if ( ns != null )
               {
                  // check which namespace we are under... is this an ims objective?
                  if ( ns.equals(IMSSS_NS) )
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> Found the <imsss:objectives> " + 
                                           "element");
                     }
      
                     ok = ADLSeqUtilities.getIMSObjectives(curNode, ioAct);
                  }
                  // check which namespace we are under... is this an adl objective?
                  else if ( ns.equals(ADLSEQ_NS) )
                  {
                     if ( _Debug )
                     {
                        System.out.println("  ::--> Found the <adlseq:objectives> " + 
                                           "element");
                     }
      
                     ok = ADLSeqUtilities.getADLObjectives(curNode, ioAct);
                  }
               }
            }
            else if ( curNode.getLocalName().equals("randomizationControls") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the " +                     
                                     "<randomizationControls> element");
               }

               // Look for 'randomizationTiming'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "randomizationTiming");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setRandomTiming(tempVal);
                  }
               }

               // Look for 'selectCount'
               tempVal = ADLSeqUtilities.getAttribute(curNode, 
                                                      "selectCount");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setSelectCount((new Integer(tempVal)).intValue());
                  }
               }

               // Look for 'reorderChildren'
               tempVal = ADLSeqUtilities.getAttribute(curNode, 
                                                      "reorderChildren");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setReorderChildren((new Boolean(tempVal)).
                                              booleanValue());
                  }
               }

               // Look for 'selectionTiming'
               tempVal = ADLSeqUtilities.getAttribute(curNode, 
                                                      "selectionTiming");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setSelectionTiming(tempVal);
                  }
               }
            }
            else if ( curNode.getLocalName().equals("deliveryControls") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the <deliveryControls> " + 
                                     "element");
               }

               // Look for 'tracked'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "tracked");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setIsTracked((new Boolean(tempVal)).booleanValue());
                  }
               }

               // Look for 'completionSetByContent'
               tempVal = ADLSeqUtilities.getAttribute(curNode, 
                                                      "completionSetByContent");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setSetCompletion((new Boolean(tempVal)).booleanValue());
                  }
               }

               // Look for 'objectiveSetByContent'
               tempVal = ADLSeqUtilities.getAttribute(curNode, 
                                                      "objectiveSetByContent");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setSetObjective((new Boolean(tempVal)).booleanValue());
                  }
               }
            }
            else if ( curNode.getLocalName().
                      equals("constrainedChoiceConsiderations") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the " + 
                                     "<constrainedChoiceConsiderations> " + 
                                     "element");
               }

               // Look for 'preventActivation'
               tempVal = ADLSeqUtilities.getAttribute(curNode,
                                                      "preventActivation");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setPreventActivation(
                                               (new Boolean(tempVal)).
                                               booleanValue());
                  }
               }

               // Look for 'constrainChoice'
               tempVal = ADLSeqUtilities.getAttribute(curNode, 
                                                      "constrainChoice");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setConstrainChoice(
                                             (new Boolean(tempVal)).
                                             booleanValue());
                  }
               }
            }
            else if ( curNode.
                      getLocalName().equals("rollupConsiderations") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found the " +
                                     "<rollupConsiderations> " + 
                                     "element");
               }

               // Look for 'requiredForSatisfied'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "requiredForSatisfied");

               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setRequiredForSatisfied(tempVal);
                  }
               }

               // Look for 'requiredForNotSatisfied'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "requiredForNotSatisfied");

               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setRequiredForNotSatisfied(tempVal);
                  }
               }

               // Look for 'requiredForCompleted'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "requiredForCompleted");

               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setRequiredForCompleted(tempVal);
                  }
               }

               // Look for 'requiredForIncomplete'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "requiredForIncomplete");

               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setRequiredForIncomplete(tempVal);
                  }
               }

               // Look for 'measureSatisfactionIfActive'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "measureSatisfactionIfActive");

               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     ioAct.setSatisfactionIfActive(
                                                  (new Boolean(tempVal)).
                                                  booleanValue());
                  }
               }
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + ok);
         System.out.println("  :: ADLSeqUtilities  --> END   - " + 
                            "extractSeqInfo");
      }

      return ok;
   }

   /**
    * Extracts the objectives associated with the activity from the
    * <code>&lt;adlseq:objectives&gt;</code> element of the DOM.
    *
    * @param iNode The DOM node associated with the ADL Seq <code>
    *              &lt;objectives&gt;</code> element.
    *
    * @param ioAct The associated activity being initialized
    *
    * @return <code>true</code> if the sequencing information extracted
    *         successfully, otherwise <code>false</code>.
    */
   private static boolean getADLObjectives(Node iNode, SeqActivity ioAct)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getADLObjectives");
      }

      boolean ok = true;
      String tempVal = null;
      Vector objectives = new Vector();

      // Get the children elements of <objectives>
      NodeList children = iNode.getChildNodes();

      // Initalize this activity's objectives
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("objective") )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <adlseq:objective> " +
                                     "element");
               }

               SeqObjective obj = new SeqObjective();

               // Look for 'objectiveID'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "objectiveID");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     obj.mObjID = tempVal;
                  }
               }

               Vector maps = getADLObjectiveMaps(curNode);

               if ( maps != null )
               {
                  obj.mMaps = maps;
               }

               objectives.add(obj);
            }
         }
      }

      // Set the Activity's objectives
      ioAct.setObjectives(objectives);

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getObjectives");
      }

      return ok;
   }

   /**
    * Gets the attributes related to the adlseq objectives map info.
    * 
    * @param iNode The node.
    * 
    * @return List of map info.
    */
   private static Vector getADLObjectiveMaps(Node iNode)
   {
      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getADLObjectiveMaps");
      }
      String tempVal = null;
      Vector maps = new Vector();

      // Get the children elements of this objective
      NodeList children = iNode.getChildNodes();

      // Initalize this objective's objective maps
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("mapInfo") )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <mapInfo> " +
                                     "element");
               }

               SeqObjectiveMap map = new SeqObjectiveMap();
               
               // the adl objectives stuff never has read 
               // set for success_status or score.scaled
               map.mReadMeasure = false;
               map.mReadStatus = false;
               
               // Look for 'targetObjectiveID'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "targetObjectiveID");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
                     map.mGlobalObjID = decodeHandler.processWhitespace(tempVal);
                  }
               }

               // Look for 'readRawScore'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "readRawScore");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mReadRawScore = Boolean.valueOf(tempVal).booleanValue();
                  }
               }

               // Look for 'readMinScore'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "readMinScore");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mReadMinScore = Boolean.valueOf(tempVal).booleanValue();
                  }
               }
               
               // Look for 'readMaxScore'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "readMaxScore");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mReadMaxScore = Boolean.valueOf(tempVal).booleanValue();
                  }
               }
               
               // Look for 'readCompletionStatus'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "readCompletionStatus");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mReadCompletionStatus = Boolean.valueOf(tempVal).booleanValue();
                      
                  }
               }
               
               // Look for 'readProgressMeasure'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "readProgressMeasure");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mReadProgressMeasure = Boolean.valueOf(tempVal).booleanValue();
                  }
               }

               // Look for 'writeRawScore'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "writeRawScore");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mWriteRawScore = Boolean.valueOf(tempVal).booleanValue();
                  }
               }

               // Look for 'writeMinScore'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "writeMinScore");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mWriteMinScore = Boolean.valueOf(tempVal).booleanValue();
                  }
               }
               
               // Look for 'writeMaxScore'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "writeMaxScore");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mWriteMaxScore = Boolean.valueOf(tempVal).booleanValue();
                  }
               }
               
               // Look for 'writeCompletionStatus'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "writeCompletionStatus");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mWriteCompletionStatus = Boolean.valueOf(tempVal).booleanValue();
                  }
               }
               
               // Look for 'writeProgressMeasure'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "writeProgressMeasure");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mWriteProgressMeasure = Boolean.valueOf(tempVal).booleanValue();
                  }
               }

               maps.add(map);
            }
         }
      }

      // Don't return an empty set.
      if ( maps.size() == 0 )
      {
         maps = null;
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getObjectiveMaps");
      }

      return maps;
   }

   /**
    * Extracts the descriptions of auxiliary resoures associated with the       
    * activity from the activiy's associated element in the DOM.
    * 
    * @param iNode The DOM node associated with the IMS SS <code>
    *              &lt;dataMap&gt;</code>) element.
    * 
    * @param ioAct The associated activity being initialized
    * 
    * @return <code>true</code> if the sequencing information extracted
    *         successfully, otherwise <code>false</code>.
    */
   private static boolean getAuxResources(Node iNode, SeqActivity ioAct)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " + 
                            "getAuxResources");
      }

      boolean ok = true;
      String tempVal = null;

      // Vector of auxiliary resources
      Vector auxRes = new Vector();

      // Get the children elements of <auxiliaryResources>
      NodeList children = iNode.getChildNodes();

      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("auxiliaryResource") )
            {
               // Build a new data mapping rule
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <auxiliaryResource> " + 
                                     "element");
               }

               ADLAuxiliaryResource res = new ADLAuxiliaryResource();

               // Get the resource's purpose
               tempVal = ADLSeqUtilities.getAttribute(curNode, "purpose");
               if ( tempVal != null )
               {
                  res.mType = tempVal;
               }

               // Get the resource's ID
               tempVal = ADLSeqUtilities.getAttribute(curNode, 
                                                      "auxiliaryResourceID");
               if ( tempVal != null )
               {
                  // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
                  res.mResourceID = decodeHandler.processWhitespace(tempVal);
               }

               // Add this datamap to the list associated with this activity
               auxRes.add(res);
            }
         }
      }  

      // Add the set of auxiliary resources to the activity
      ioAct.setAuxResources(auxRes);

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " + 
                            "getAuxResources");
      }

      return ok;
   }


   /**
    * Determines if the value is empty
    * 
    * @param iValue The value from a DOM node
    * 
    * @return <code>true</code> the value is empty or consists entirely of 
    *         white space
    */
   private static boolean isEmpty(String iValue)
   {

      return (iValue.trim().length() == 0);
   }


   /**
    * Extracts the sequencing rules associated with the activity from the
    * <code>&lt;sequencingRules&gt;</code> element of the DOM.
    * 
    * @param iNode The DOM node associated with the IMS SS <code>
    *              &lt;sequencingRules&gt;</code> element.
    * 
    * @param ioAct The associated activity being initialized
    * 
    * @return <code>true</code> if the sequencing information extracted
    *         successfully, otherwise <code>false</code>.
    */
   private static boolean getSequencingRules(Node iNode, SeqActivity ioAct)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " + 
                            "getSequencingRules");
      }

      boolean ok = true;
      String tempVal = null;

      Vector preRules = new Vector();
      Vector exitRules = new Vector();
      Vector postRules = new Vector();


      // Get the children elements of <sequencingRules>
      NodeList children = iNode.getChildNodes();

      // Initalize this activity's sequencing rules 
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("preConditionRule") )
            {
               // Extract all of the precondition rules
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <preConditionRule> " +
                                     "element");
               }

               SeqRule rule = new SeqRule();

               NodeList ruleInfo = curNode.getChildNodes();

               for ( int j = 0; j < ruleInfo.getLength(); j++ )
               {

                  Node curRule = ruleInfo.item(j);

                  // Check to see if this is an element node.
                  if ( curRule.getNodeType() == Node.ELEMENT_NODE )
                  {
                     if ( curRule.getLocalName().equals("ruleConditions") )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Found a " + 
                                              "<ruleConditions> element");
                        }

                        // Extract the condition set
                        rule.mConditions = extractSeqRuleConditions(curRule);

                     }
                     else if ( curRule.getLocalName().equals("ruleAction") )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Found a <ruleAction> " +
                                              "element");
                        }

                        // Look for 'action'
                        tempVal = ADLSeqUtilities.
                                  getAttribute(curRule, "action");

                        if ( tempVal != null )
                        {
                           if ( !isEmpty(tempVal) )
                           {
                              rule.mAction = tempVal;
                           }
                        }
                     }
                  }
               }

               if ( rule.mConditions != null && rule.mAction != null )
               {
                  preRules.add(rule);
               }
               else
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> ERROR : Invaild Pre SeqRule");
                  }
               }
            }
            else if ( curNode.getLocalName().equals("exitConditionRule") )
            {
               // Extract all of the exit action rules
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a " + 
                                     "<exitConditionRule> element");
               }

               SeqRule rule = new SeqRule();

               NodeList ruleInfo = curNode.getChildNodes();

               for ( int k = 0; k < ruleInfo.getLength(); k++ )
               {

                  Node curRule = ruleInfo.item(k);

                  // Check to see if this is an element node.
                  if ( curRule.getNodeType() == Node.ELEMENT_NODE )
                  {
                     if ( curRule.getLocalName().equals("ruleConditions") )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Found a " + 
                                              "<ruleConditions> element");
                        }

                        // Extract the condition set
                        rule.mConditions = extractSeqRuleConditions(curRule);

                     }
                     else if ( curRule.getLocalName().equals("ruleAction") )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Found a <ruleAction> " +
                                              "element");
                        }

                        // Look for 'action'
                        tempVal = ADLSeqUtilities.
                                  getAttribute(curRule, "action");

                        if ( tempVal != null )
                        {
                           rule.mAction = tempVal;
                        }
                     }
                  }
               }

               if ( rule.mConditions != null && rule.mAction != null )
               {
                  exitRules.add(rule);
               }
               else
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> ERROR : Invaild Exit SeqRule");
                  }
               }
            }
            else if ( curNode.getLocalName().equals("postConditionRule") )
            {
               // Extract all of the post condition action rules
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a " + 
                                     "<postConditionRule> element");
               }

               SeqRule rule = new SeqRule();

               NodeList ruleInfo = curNode.getChildNodes();

               for ( int j = 0; j < ruleInfo.getLength(); j++ )
               {

                  Node curRule = ruleInfo.item(j);

                  // Check to see if this is an element node.
                  if ( curRule.getNodeType() == Node.ELEMENT_NODE )
                  {
                     if ( curRule.getLocalName().equals("ruleConditions") )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Found a " + 
                                              "<ruleConditions> element");
                        }

                        // Extract the condition set
                        rule.mConditions = extractSeqRuleConditions(curRule);

                     }
                     else if ( curRule.getLocalName().equals("ruleAction") )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Found a <ruleAction> " +
                                              "element");
                        }

                        // Look for 'action'
                        tempVal = ADLSeqUtilities.
                                  getAttribute(curRule, "action");

                        if ( tempVal != null )
                        {
                           rule.mAction = tempVal;
                        }
                     }
                  }
               }

               if ( rule.mConditions != null && rule.mAction != null )
               {
                  postRules.add(rule);
               }
               else
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> ERROR : Invaild Post SeqRule");
                  }
               }
            }
         }
      }

      if ( preRules.size() > 0 )
      {
         SeqRuleset rules = new SeqRuleset(preRules);

         ioAct.setPreSeqRules(rules);
      }

      if ( exitRules.size() > 0 )
      {
         SeqRuleset rules = new SeqRuleset(exitRules);

         ioAct.setExitSeqRules(rules);
      }

      if ( postRules.size() > 0 )
      {
         SeqRuleset rules = new SeqRuleset(postRules);

         ioAct.setPostSeqRules(rules);
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getSequencingRules");
      }

      return ok;
   }


   /**
    * Extracts the conditions assoicated with a sequencing rule.
    * 
    * @param iNode  The DOM node associated with one of the IMS SS <code>
    *               &lt;ruleConditions&gt;</code> element.
    * 
    * @return The condition set (<code>SeqConditionSet</code>) assoicated with
    *         the rule.
    */
   private static SeqConditionSet extractSeqRuleConditions(Node iNode)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "extractSeqRuleConditions");
      }

      String tempVal = null;
      SeqConditionSet condSet = new SeqConditionSet();

      Vector conditions = new Vector();

      // Look for 'conditionCombination'
      tempVal = ADLSeqUtilities.getAttribute(iNode, "conditionCombination");
      if ( tempVal != null )
      {
         if ( !isEmpty(tempVal) )
         {
            condSet.mCombination = tempVal;
         }
      }
      else
      {
         // Enforce Default
         condSet.mCombination = SeqConditionSet.COMBINATION_ALL;
      }

      NodeList condInfo = iNode.getChildNodes();

      for ( int i = 0; i < condInfo.getLength(); i++ )
      {

         Node curCond = condInfo.item(i);

         // Check to see if this is an element node.
         if ( curCond.getNodeType() == Node.ELEMENT_NODE )
         {

            if ( curCond.getLocalName().equals("ruleCondition") )
            {

               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <Condition> " +
                                     "element");
               }

               SeqCondition cond = new SeqCondition();

               // Look for 'condition'
               tempVal = ADLSeqUtilities.getAttribute(curCond, "condition");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     cond.mCondition = tempVal;
                  }
               }

               // Look for 'referencedObjective'
               tempVal = ADLSeqUtilities.
                         getAttribute(curCond, "referencedObjective");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     cond.mObjID = tempVal;
                  }
               }

               // Look for 'measureThreshold'
               tempVal = ADLSeqUtilities.
                         getAttribute(curCond, "measureThreshold");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     cond.mThreshold = (new Double(tempVal)).doubleValue();
                  }
               }

               // Look for 'operator'
               tempVal = ADLSeqUtilities.getAttribute(curCond, "operator");
               if ( tempVal != null )
               {
                  if ( tempVal.equals("not") )
                  {
                     cond.mNot = true;
                  }
                  else
                  {
                     cond.mNot = false;
                  }
               }

               conditions.add(cond);
            }
         }
      }


      if ( conditions.size() > 0 )
      {
         // Add the conditions to the condition set 
         condSet.mConditions = conditions;         
      }
      else
      {
         condSet = null;
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "extractSeqRuleConditions");
      }

      return condSet;
   }

   /**
    * Extracts the rollup rules associated with the activity from the
    * <code>&lt;sequencingRules&gt;</code> element of the DOM.
    *
    * @param iNode The DOM node associated with the IMS SS <code>
    *              &lt;sequencingRules&gt;</code> element.
    *
    * @param ioAct The associated activity being initialized
    *
    * @return <code>true</code> if the sequencing information extracted
    *         successfully, otherwise <code>false</code>.
    */
   private static boolean getRollupRules(Node iNode, SeqActivity ioAct)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getRollupRules");
      }

      boolean ok = true;
      String tempVal = null;
      Vector rollupRules = new Vector();


      // Look for 'rollupObjectiveSatisfied'
      tempVal = ADLSeqUtilities.getAttribute(iNode,
                                             "rollupObjectiveSatisfied");
      if ( tempVal != null )
      {
         if ( !isEmpty(tempVal) )
         {
            ioAct.setIsObjRolledUp((new Boolean(tempVal)).booleanValue());
         }
      }

      // Look for 'objectiveMeasureWeight'
      tempVal = ADLSeqUtilities.getAttribute(iNode,
                                             "objectiveMeasureWeight");
      if ( tempVal != null )
      {
         if ( !isEmpty(tempVal) )
         {
            ioAct.setObjMeasureWeight((new Double(tempVal)).doubleValue());
         }
      }

      // Look for 'rollupProgressCompletion'
      tempVal = ADLSeqUtilities.getAttribute(iNode,
                                             "rollupProgressCompletion");
      if ( tempVal != null )
      {
         if ( !isEmpty(tempVal) )
         {
            ioAct.setIsProgressRolledUp((new Boolean(tempVal)).booleanValue());
         }
      }

      // Get the children elements of <rollupRules>
      NodeList children = iNode.getChildNodes();

      // Initalize this activity's rollup rules 
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("rollupRule") )
            {
               // Extract all of the rollup Rules
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <rollupRule> " +
                                     "element");
               }

               SeqRollupRule rule = new SeqRollupRule();

               // Look for 'childActivitySet'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "childActivitySet");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     rule.mChildActivitySet = tempVal;
                  }
               }

               // Look for 'minimumCount'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "minimumCount");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     rule.mMinCount = (new Long(tempVal)).longValue();
                  }
               }

               // Look for 'minimumPercent'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "minimumPercent");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     rule.mMinPercent = (new Double(tempVal)).doubleValue();
                  }
               }

               rule.mConditions = new SeqConditionSet(true);
               Vector conditions = new Vector();

               NodeList ruleInfo = curNode.getChildNodes();

               // Initalize this rollup rule
               for ( int j = 0; j < ruleInfo.getLength(); j++ )
               {

                  Node curRule = ruleInfo.item(j);

                  // Check to see if this is an element node.
                  if ( curRule.getNodeType() == Node.ELEMENT_NODE )
                  {
                     if ( curRule.getLocalName().equals("rollupConditions") )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Found a " + 
                                              "<rollupConditions> " +
                                              "element");
                        }

                        // Look for 'conditionCombination'
                        tempVal = ADLSeqUtilities.
                                  getAttribute(curRule, "conditionCombination");

                        if ( tempVal != null )
                        {
                           if ( !isEmpty(tempVal) )
                           {
                              rule.mConditions.mCombination = tempVal;
                           }
                        }
                        else
                        {
                           // Enforce Default
                           rule.mConditions.mCombination = 
                           SeqConditionSet.COMBINATION_ANY;
                        }

                        NodeList conds = curRule.getChildNodes();

                        for ( int k = 0; k < conds.getLength(); k++ )
                        {
                           Node con = conds.item(k);

                           // Check to see if this is an element node.
                           if ( con.getNodeType() == Node.ELEMENT_NODE )
                           {
                              if ( con.getLocalName().
                                   equals("rollupCondition") )
                              {

                                 if ( _Debug )
                                 {
                                    System.out.println("  ::--> Found a " +
                                                       "<rollupCondition> " +
                                                       "element");
                                 }

                                 SeqCondition cond = new SeqCondition();

                                 // Look for 'condition'
                                 tempVal =
                                 ADLSeqUtilities.
                                 getAttribute(con, "condition");
                                 if ( tempVal != null )
                                 {
                                    if ( !isEmpty(tempVal) )
                                    {
                                       cond.mCondition = tempVal;
                                    }
                                 }

                                 // Look for 'operator'
                                 tempVal =
                                 ADLSeqUtilities.
                                 getAttribute(con, "operator");
                                 if ( tempVal != null )
                                 {
                                    if ( tempVal.equals("not") )
                                    {
                                       cond.mNot = true;
                                    }
                                    else
                                    {
                                       cond.mNot = false;
                                    }
                                 }

                                 conditions.add(cond);
                              }
                           }
                        }
                     }
                     else if ( curRule.
                               getLocalName().equals("rollupAction") )
                     {

                        if ( _Debug )
                        {
                           System.out.println("  ::--> Found a " + 
                                              "<rollupAction> " +
                                              "element");
                        }
                        // Look for 'action'
                        tempVal =
                        ADLSeqUtilities.getAttribute(curRule, "action");
                        if ( tempVal != null )
                        {
                           if ( !isEmpty(tempVal) )
                           {
                              rule.setRollupAction(tempVal);
                           }
                        }
                     }
                  }
               }

               // Add the conditions to the condition set for the rule
               rule.mConditions.mConditions = conditions;

               // Add the rule to the ruleset
               rollupRules.add(rule);
            }
         }
      }

      if ( rollupRules != null )
      {
         SeqRollupRuleset rules = new SeqRollupRuleset(rollupRules);

         // Set the Activity's rollup rules
         ioAct.setRollupRules(rules);
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getRollupRules");
      }

      return ok;
   }

   /**
    * Extracts the objectives associated with the activity from the
    * <code>&lt;imsss:objectives&gt;</code> element of the DOM.
    *
    * @param iNode The DOM node associated with the IMS SS <code>
    *              &lt;objectives&gt;</code> element.
    *
    * @param ioAct The associated activity being initialized
    *
    * @return <code>true</code> if the sequencing information extracted
    *         successfully, otherwise <code>false</code>.
    */
   private static boolean getIMSObjectives(Node iNode, SeqActivity ioAct)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getIMSObjectives");
      }

      boolean ok = true;
      String tempVal = null;
      Vector objectives = new Vector();

      // Get the children elements of <objectives>
      NodeList children = iNode.getChildNodes();

      // Initalize this activity's objectives
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("primaryObjective") )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <primaryObjective> " +
                                     "element");
               }

               SeqObjective obj = new SeqObjective();

               obj.mContributesToRollup = true;

               // Look for 'objectiveID'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "objectiveID");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     obj.mObjID = tempVal;
                  }
               }

               // Look for 'satisfiedByMeasure'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "satisfiedByMeasure");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     obj.mSatisfiedByMeasure = 
                     (new Boolean(tempVal)).booleanValue();
                  }
               }

               // Look for 'minNormalizedMeasure'
               tempVal = getElementText(curNode, "minNormalizedMeasure");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     obj.mMinMeasure = (new Double(tempVal)).doubleValue();
                  }
               }

               Vector maps = getIMSObjectiveMaps(curNode);

               if ( maps != null )
               {
                  obj.mMaps = maps;
               }

               obj.mContributesToRollup = true;
               objectives.add(obj);
            }
            else if ( curNode.getLocalName().equals("objective") )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <imsss:objective> " +
                                     "element");
               }

               SeqObjective obj = new SeqObjective();

               // Look for 'objectiveID'
               tempVal = ADLSeqUtilities.getAttribute(curNode, "objectiveID");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     obj.mObjID = tempVal;
                  }
               }

               // Look for 'satisfiedByMeasure'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "satisfiedByMeasure");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     obj.mSatisfiedByMeasure = 
                        (new Boolean(tempVal)).booleanValue();
                  }
               }

               // Look for 'minNormalizedMeasure'
               tempVal = getElementText(curNode, "minNormalizedMeasure");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     obj.mMinMeasure = (new Double(tempVal)).doubleValue();
                  }
               }

               Vector maps = getIMSObjectiveMaps(curNode);

               if ( maps != null )
               {
                  obj.mMaps = maps;
               }

               objectives.add(obj);
            }
         }
      }

      // Set the Activity's objectives
      ioAct.setObjectives(objectives);

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getObjectives");
      }

      return ok;
   }

   /**
    * Extracts the objective maps associated with a specific objective from the
    * <code>&lt;objectives&gt;</code> element of the DOM.
    *
    * @param iNode The DOM node associated with an objective.=
    *
    * @return The set (<code>Vector</code>) of objective maps extracted
    *         successfully, otherwise <code>null</code>.
    */
   private static Vector getIMSObjectiveMaps(Node iNode)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getIMSObjectiveMaps");
      }

      String tempVal = null;
      Vector maps = new Vector();

      // Get the children elements of this objective
      NodeList children = iNode.getChildNodes();

      // Initalize this objective's objective maps
      for ( int i = 0; i < children.getLength(); i++ )
      {
         Node curNode = children.item(i);

         // Check to see if this is an element node.
         if ( curNode.getNodeType() == Node.ELEMENT_NODE )
         {
            if ( curNode.getLocalName().equals("mapInfo") )
            {
               if ( _Debug )
               {
                  System.out.println("  ::--> Found a <mapInfo> " +
                                     "element");
               }

               SeqObjectiveMap map = new SeqObjectiveMap();

               // Look for 'targetObjectiveID'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "targetObjectiveID");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
                     map.mGlobalObjID = decodeHandler.processWhitespace(tempVal);
                  }
               }

               // Look for 'readSatisfiedStatus'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "readSatisfiedStatus");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mReadStatus = (new Boolean(tempVal)).booleanValue();
                  }
               }

               // Look for 'readNormalizedMeasure'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "readNormalizedMeasure");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mReadMeasure = (new Boolean(tempVal)).booleanValue();
                  }
               }

               // Look for 'writeSatisfiedStatus'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "writeSatisfiedStatus");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mWriteStatus = (new Boolean(tempVal)).booleanValue();
                  }
               }

               // Look for 'writeNormalizedMeasure'
               tempVal = ADLSeqUtilities.
                         getAttribute(curNode, "writeNormalizedMeasure");
               if ( tempVal != null )
               {
                  if ( !isEmpty(tempVal) )
                  {
                     map.mWriteMeasure = (new Boolean(tempVal)).booleanValue();
                  }
               }

               maps.add(map);
            }
         }
      }

      // Don't return an empty set.
      if ( maps.size() == 0 )
      {
         maps = null;
      }

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getObjectiveMaps");
      }

      return maps;
   }


   /**
    * Attempts to find the indicated subelement of the current node and
    * extact its value.
    *
    * @param iNode    The DOM node of the target element.
    *
    * @param iElement The requested subelement.
    *
    * @return The value of the requested subelement of target element, or
    *         <code>null</code> if the subelement does not exist.
    */
   private static String getElementText(Node iNode, String iElement)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - " +
                            "getElementText");
         System.out.println("  ::-->  " + iElement);
      }

      String value = null;
      Node curNode = null;
      NodeList children = null;

      if ( iElement != null && iNode != null )
      {
         children = iNode.getChildNodes();

         // Locate the target subelement
         for ( int i = 0; i < children.getLength(); i++ )
         {
            curNode = children.item(i);

            // Check to see if this is an element node.
            if ( curNode.getNodeType() == Node.ELEMENT_NODE )
            {
               if ( _Debug )
               {
                  System.out.println("  ::-->   " + i);
                  System.out.println("  ::-->  <" + curNode.getLocalName() +
                                     ">");
               }

               if ( curNode.getLocalName().equals(iElement) )
               {
                  if ( _Debug )
                  {
                     System.out.println("  ::--> Found <" +
                                        iElement + ">");
                  }

                  break;
               }
            }
         }


         if ( curNode != null )
         {
            String comp = curNode.getLocalName();

            if ( comp != null )
            {
               // Make sure we found the subelement
               if ( !comp.equals(iElement) )
               {
                  curNode = null;
               }
            }
            else
            {
               curNode = null;
            }
         }
      }
      else
      {
         curNode = iNode;
      }

      if ( curNode != null )
      {
         if ( _Debug )
         {
            System.out.println("  ::--> Looking at children");
         }

         // Extract the element's text value.
         children = curNode.getChildNodes();

         // Cycle through all children of node to get the text
         if ( children != null )
         {
            // There must be a value
            value = new String("");

            for ( int i = 0; i < children.getLength(); i++ )
            {
               curNode = children.item(i);

               // make sure we have a 'text' element
               if ( (curNode.getNodeType() == Node.TEXT_NODE) ||
                    (curNode.getNodeType() == Node.CDATA_SECTION_NODE) )
               {
                  value = value + curNode.getNodeValue();
               }
            }
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + value);
         System.out.println("  :: ADLSeqUtilities  --> END   - " +
                            "getElementText");
      }

      return value;
   }

   /**
    * Attempts to find the indicated attribute of the target element.
    *
    * @param iNode      The DOM node of the target element.
    *
    * @param iAttribute The requested attribute.
    *
    * @return The value of the requested attribute on the target element,
    *         <code>null</code> if the attribute does not exist.
    */
   private static String getAttribute(Node iNode, String iAttribute)
   {

      if ( _Debug )
      {
         System.out.println("  :: ADLSeqUtilities  --> BEGIN - getAttribute");
         System.out.println("  ::-->  " + iAttribute);
      }

      String value = null;

      // Extract the node's attribute list and check if the requested
      // attribute is contained in it.
      NamedNodeMap attrs = iNode.getAttributes();

      if ( attrs != null )
      {
         // Attempt to get the requested attribute
         Node attr = attrs.getNamedItem(iAttribute);

         if ( attr != null )
         {
            // Extract the attributes value
            value = attr.getNodeValue();
         }
         else
         {
            if ( _Debug )
            {
               System.out.println("  ::-->  The attribute \"" +
                                  iAttribute + "\" does not exist.");
            }
         }
      }
      else
      {
         if ( _Debug )
         {
            System.out.println("  ::-->  This node has no attributes.");
         }
      }

      if ( _Debug )
      {
         System.out.println("  ::-->  " + value);
         System.out.println("  :: ADLSeqUtilities  --> END - getAttribute");
      }
      
      // If the attribute is of type xs:ID, ID, or IDREF we must process it first
      if ( iAttribute.equals("identifier") || iAttribute.equals("default") ||
           iAttribute.equals("IDRef") || iAttribute.equals("ID") || 
           iAttribute.equals("referencedObjective") || iAttribute.equals("objectiveID") )
      {
         value = decodeHandler.processWhitespace(value);
      }

      return value;
   }

   /**
    * Tells this utility whether to delete the data store or to save it.
    * 
    * @param iDelete Value whether for not to delete the data store.
    */
   public static void deleteDataStore(boolean iDelete)
   {
      if ( SeqActivityTree.dataStoreLoc != null )
      {
         String location = SeqActivityTree.dataStoreLoc;
         HashMap tempmap = SeqActivityTree.dsMap;
         File map = new File(location);
         boolean success = false;
         for ( int i = 0; i < 100; i++ )
         {
            if ( map.exists() && map.canWrite() )
            {
               success = true;
               break;
            }
            try
            {
               Thread.sleep(100);
               map.createNewFile();
            }
            catch (IOException ioe)
            {
               // loop around
            }
            catch ( InterruptedException e )
            {
               // loop around
            }
         }
         if ( success )
         {
            if ( iDelete )
            {
               map.delete();
            }
            else
            {
               FileOutputStream dsfo;
               try
               {
                  dsfo = new FileOutputStream(map);
                  ObjectOutputStream dsoutFile = new ObjectOutputStream(dsfo);
                  dsoutFile.writeObject(tempmap);
                  dsoutFile.close();
                  dsfo.close();
               }
               catch ( FileNotFoundException e )
               {
                  e.printStackTrace();
               }
               catch ( IOException e )
               {
                  e.printStackTrace();
               }
            }
         }
      }
      // clearing map
      SeqActivityTree.dsMap = null;
   }

}  // end ADLSeqUtilities
