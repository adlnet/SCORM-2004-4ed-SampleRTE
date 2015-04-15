<%@page import = "java.sql.PreparedStatement,java.sql.ResultSet,java.sql.Connection,
   java.io.FileOutputStream, java.io.ObjectOutputStream, org.adl.sequencer.SeqActivityTree"%>
<%
   /***************************************************************************
   **
   ** Filename:  saveFileUtil.jsp
   **
   ** File Description:
   **
   **
   **
   ** Author: ADL Technical Team
   **
   ** Contract Number:
   ** Company Name: CTC
   **
   ** Module/Package Name:
   ** Module/Package Description:
   **
   ** Design Issues:
   **
   ** Implementation Issues:
   ** Known Problems:
   ** Side Effects: 
   **
   ** References: ADL SCORM
   **
   /***************************************************************************
    
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

   ***************************************************************************/
%>

<%!
   private static String ERROR_PAGE = "../specialstate/error.htm";

   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>ERROR
   * <br><b>"_ERROR_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_ERROR = "../specialstate/error.htm";

   /**
   *  Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>Blocked
   * <br><b>"_Blocked_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_BLOCKED = "../specialstate/blocked.htm";

   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>View Table of Contents
   * <br><b>"_TOC_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_TOC = "../specialstate/viewTOC.jsp";

   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>Course Complete
   * <br><b>"_COURSECOMPLETE_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_COURSECOMPLETE=
                        "../specialstate/coursecomplete.htm";
                        
   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>No Available Activities
   * <br><b>"_INVALIDNAVEVENT_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_INVALIDNAVEVENT   = 
                        "../specialstate/invalidevent.htm";
   
   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>No Available Activities
   * <br><b>"_ENDSESSION_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_ENDSESSION = "../specialstate/endsession.jsp";
   
   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>Nothing
   * <br><b>"_NOTHING_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_NOTHING = "../specialstate/nothing.htm";
   
   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>No Available Activities
   * <br><b>"_DEADLOCK_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_DEADLOCK = "../specialstate/deadlock.htm";
   
   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>Abandon
   * <br><b>"_SEQABANDON_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_SEQ_ABANDON = "../specialstate/abandon.htm";
                        
   /**
   * Enumeration of possible results of of the sequencing process that do not
   * provide the 'next' activity to launch.
   * <br>Abandon
   * <br><b>"_SEQABANDONALL_"</b>
   * <br>[SEQUENCING SUBSYSTEM CONSTANT]
   */
   public static String LAUNCH_SEQ_ABANDONALL = "../specialstate/abandonAll.jsp";


   /****************************************************************************
   **
   ** Function:  persistActivityTree()
   ** Input:   iTree - SeqActivityTree
   ** Output:  boolean
   **
   ** Description:  This function returns the input source.
   **
   ***************************************************************************/
   private boolean persistActivityTree( SeqActivityTree iTree, String iUser, 
                                        String iCourse )
   {
    boolean result = true;
    String sampleRTERoot = System.getProperty("user.home") + File.separator + "SCORM4EDSampleRTE111Files";
    String userDir = sampleRTERoot + File.separator + iUser + File.separator +
       iCourse;
    String theWebPath = getServletConfig().getServletContext().
                        getRealPath( "/" );
    String serializeFileName = userDir + File.separator + "serialize.obj";
   
    // Clear the tree session state
    iTree.clearSessionState();
    
    try
    {
       FileOutputStream fo = new FileOutputStream( serializeFileName );
       ObjectOutputStream out_file = new ObjectOutputStream( fo );
       out_file.writeObject( iTree );
       out_file.close();
       fo.close();
    }
    catch ( Exception e )
    {
       result = false;
    }
    
    return result;
   }

   /****************************************************************************
   **
   ** Function:  getLaunchLocation()
   ** Input:   
   ** Output:  
   **
   ** Description:  This function queries the RTE database for the resource
   **               launch location.  The activity id is used as the key.
   **
   ***************************************************************************/
   private String getLaunchLocation( PreparedStatement iStmtLaunchLocation,
                                  String iCourseID, String iActivityID )
   {
    String nextItem = new String();
    ResultSet launchInfo = null;
   
    try 
    {
       synchronized( iStmtLaunchLocation )
       {
          iStmtLaunchLocation.setString( 1, iCourseID );
          iStmtLaunchLocation.setString( 2, iActivityID );
   
          launchInfo = iStmtLaunchLocation.executeQuery();
       }
       if ( launchInfo.next() )
       {
          nextItem = launchInfo.getString("Launch");
       }
       else
       {
          nextItem = ERROR_PAGE;
       }
       launchInfo.close();
    }
    catch ( Exception e )
    {
       System.out.println("CAUGHT EXCEPTION IN UTIL");
       nextItem = ERROR_PAGE;
    }
    return nextItem;
   }

   /****************************************************************************
   **
   ** Function:  getSpecialState()
   ** Input:   
   ** Output:  
   **
   ** Description:  This method looks at the mActivityID member of the current
   **               ADLLaunch object.  If it is in a special sequencing state
   **               then the appropriate String is returned.  If its not in
   **               a special state, null is returned.
   **
   ***************************************************************************/
   private String getSpecialState( String iActivity )
   {
    String theState = new String();
   
    if ( iActivity != null )
    {
      if ( iActivity.equals("_ERROR_") )
      {
         theState = LAUNCH_ERROR;
      }
      else if ( iActivity.equals("_SEQBLOCKED_") )
      {
         theState = LAUNCH_BLOCKED;
      }
      else if ( iActivity.equals("_TOC_") )
      {
         theState = LAUNCH_TOC;
      }      
      else if ( iActivity.equals("_COURSECOMPLETE_") )
      {
         theState = LAUNCH_COURSECOMPLETE;
      }      
      else if ( iActivity.equals("_INVALIDNAVREQ_") )
      {
         theState = LAUNCH_INVALIDNAVEVENT;
      }     
      else if ( iActivity.equals("_ENDSESSION_") )
      {
         theState = LAUNCH_ENDSESSION;
      }
      else if ( iActivity.equals("_NOTHING_") )
      {
         theState = LAUNCH_NOTHING;
      }
      else if ( iActivity.equals("_DEADLOCK_") )
      {
         theState = LAUNCH_DEADLOCK;
      }
      else if ( iActivity.equals("_SEQABANDON_") )
      {
         theState = LAUNCH_SEQ_ABANDON;
      }
      else if ( iActivity.equals("_SEQABANDONALL_") )
      {
         theState = LAUNCH_SEQ_ABANDONALL;
      }
      else
      {
         theState = ERROR_PAGE;
      }
     }
     else
     {
       theState = ERROR_PAGE;
     }
    return theState;

   }

   /****************************************************************************
   ** 
   ** Function:  insertComp()
   ** Input:   
   ** Output:  
   **
   ** Description:  This method inserts a list of competencies into the
   **               database.  
   **
   ***************************************************************************/
   public static void insertComp(String iStudentID,
                                 Vector iCompList,
                                 Connection iConn, 
                                 String iCourseID)
   {
      try
      {
         PreparedStatement stmtCreateRecord = null;

         // Create the SQL string, convert it to a prepared statement
         String sqlCreateRecord = "INSERT INTO Competency " +
                                  "(CompID, UserID, CourseID, PassFail, " +
                                  "Score) VALUES (?, ?, ?, ?, ?)";

         stmtCreateRecord = iConn.prepareStatement(sqlCreateRecord);

         for ( int i = 0; i < iCompList.size(); i++ )
         {

            String compID = (String)iCompList.elementAt(i);

            // Insert values into the prepared statement and execute
            // query.
           
            synchronized(stmtCreateRecord)
            {
               stmtCreateRecord.setString(1, compID);
               stmtCreateRecord.setString(2, iStudentID);
               stmtCreateRecord.setString(3, iCourseID);
               stmtCreateRecord.setString(4, "unknown");
               stmtCreateRecord.setString(5, "unknown");

               stmtCreateRecord.executeUpdate();
            }
         }
         // Close the prepared statement 
         stmtCreateRecord.close();
        
      }
      catch ( Exception e )
      {
            System.out.println("insert Comp");
            e.printStackTrace();
      }
   }
%>