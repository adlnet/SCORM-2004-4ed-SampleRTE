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

package org.adl.samplerte.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.adl.datamodels.DMErrorCodes;
import org.adl.datamodels.DMInterface;
import org.adl.datamodels.DMProcessingInfo;
import org.adl.datamodels.DMRequest;
import org.adl.datamodels.SCODataManager;
import org.adl.datamodels.ieee.SCORM_2004_DM;
import org.adl.datamodels.nav.SCORM_2004_NAV_DM;
import org.adl.samplerte.util.RTEFileHandler;
import org.adl.sequencer.ADLObjStatus;
import org.adl.sequencer.ADLSeqUtilities;
import org.adl.sequencer.ADLSequencer;
import org.adl.sequencer.ADLTracking;
import org.adl.sequencer.ADLValidRequests;
import org.adl.sequencer.SeqActivity;
import org.adl.sequencer.SeqActivityTree;
import org.adl.sequencer.SeqObjective;
import org.adl.sequencer.SeqActivity.DataStore;

import com.google.gson.Gson;

/**
 * <strong>Filename:</strong> LMSCMIServletjava<br>
 * <br>
 * <strong>Description:</strong><br>
 * The LMSCMIServlet class handles the server side data model communication of
 * the Sample RTE.<br>
 * This servlet handles persistence of the SCORM Run-Time Environment Data Model
 * elements. Persistence is being handled via flat files and java object
 * serialization rather than through a database.<br>
 * <br>
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM Sample RTE 1.3. <br>
 * <br>
 * <strong>Implementation Issues:</strong><br>
 * This servlet works in conjunction with the <code>LMSRTEClient</code> applet
 * in the <code>org.adl.lms.client</code> package.<br>
 * <br>
 * <strong>Known Problems:</strong><br>
 * <br>
 * <strong>Side Effects:</strong><br>
 * <br>
 * <strong>References:</strong><br>
 * <ul>
 * <li>IMS SS Specification</li>
 * <li>SCORM 2004</li>
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class LMSCMIServlet extends HttpServlet
{
   /**
    * This is the value used for the primary objective ID
    */
   private final String mPRIMARY_OBJ_ID = null;

   /**
    * This string contains the name of the SampleRTEFiles directory.
    */
   private final String SRTEFILESDIR = System.getProperty("user.home") + File.separator + "SCORM4EDSampleRTE111Files";
   
   private final String INITIALIZED_DM_EXT = "initialized";

   /**
    * This method handles the 'POST' message sent to the servlet. This servlet
    * will handle <code>LMSServletRequest</code> objects and respond with a
    * <code>LMSServletResponse</code> object.
    * 
    * @param iRequest The request 'POST'ed to the servlet.
    * @param oResponse The response returned by the servlet.<br>
    *           <br>
    * @exception ServletException
    * @exception IOException <br>
    *               <br>
    * @see org.adl.samplerte.server#LMSServletRequest
    * @see org.adl.samplerte.server#LMSServletResponse
    * 
    * @throws ServletException
    */
   public void doPost(HttpServletRequest iRequest, HttpServletResponse oResponse) throws ServletException, IOException
   {
      Logger logger = Logger.getLogger("org.adl.util.debug.samplerte");
      
      logger.entering("---LMSCMIServlet", "doPost()");
      logger.info("POST received by LMSCMIServlet");

      String scoFile = null;
      String userID = null;
      String userName = null;
      String courseID = null;
      String scoID = null;
      String numAttempt = null;
      String activityID = null;
      
      LMSCMIServletRequest request = null;
      LMSCMIServletResponse response = null;
      
      SCODataManager mSCOData = null;
      
      try
      {
         logger.info("Requested session: " + iRequest.getRequestedSessionId());
         logger.info("query string: " + iRequest.getQueryString());
         logger.info("header string: " + iRequest.getContextPath());

         for( Enumeration e = iRequest.getHeaderNames(); e.hasMoreElements(); )
         {
            logger.info(e.nextElement().toString());
         }

         oResponse.setContentType("application/json; charset=utf-8");
         oResponse.setCharacterEncoding("UTF-8");
         // Get the printwriter object from response to write the required json object to the output stream      
         PrintWriter out = oResponse.getWriter();
         
         StringBuffer jb = new StringBuffer();
         String line = null;
         try {
           BufferedReader reader = iRequest.getReader();
           while ((line = reader.readLine()) != null)
             jb.append(line);
         } catch (Exception e) { /*report an error*/ }

         Gson gson = new Gson();
         request = gson.fromJson(jb.toString(),LMSCMIServletRequest.class);

         // Set servlet state
         scoID = request.mStateID;
         activityID = request.mActivityID;
         courseID = request.mCourseID;
         userID = request.mStudentID;
         numAttempt = request.mNumAttempt;
         userName = request.mUserName;

         logger.info("ScoID: " + scoID);

         // Set the run-time data model path
         if( numAttempt != null )
         {
            scoFile = SRTEFILESDIR + File.separator + userID + File.separator + courseID
               + File.separator + scoID + "__" + numAttempt;
         }
         else
         {
            logger.fine("  ERROR: NULL # attempt");

            scoFile = SRTEFILESDIR + File.separator + userID + File.separator + courseID
               + File.separator + scoID;
         }

         logger.info("Data model path:  " + scoFile);
         
         FileInputStream fi = null;
         ObjectInputStream fileIn = null;

         // Handle the request
         switch( request.mRequestType )
         {

            case LMSCMIServletRequest.TYPE_INIT:
               
               logger.info("CMI Servlet - doPost() - entering case INIT ");

               logger.info("Processing 'init' request");

               // create response object to return
               response = new LMSCMIServletResponse();

               // Serialize the users activity tree for the selected course

               SeqActivityTree mSeqActivityTree = new SeqActivityTree();
               
               // location of the stored data store, assuming there is one
               SeqActivityTree.dataStoreLoc = SRTEFILESDIR + File.separator + userID + File.separator + courseID
               + File.separator + "DS.obj";
               
               String mTreePath = SRTEFILESDIR + File.separator + userID + File.separator + courseID
                  + File.separator + "serialize.obj";

               FileInputStream mFileIn = new FileInputStream(mTreePath);
               ObjectInputStream mObjectIn = new ObjectInputStream(mFileIn);
               mSeqActivityTree = (SeqActivityTree)mObjectIn.readObject();
               mObjectIn.close();
               mFileIn.close();
               
               boolean newFile = true;
               RTEFileHandler fileHandler = new RTEFileHandler();

               // Try to open the state file
               try
               {
                  fi = new FileInputStream(scoFile);
                  newFile = false;

               }
               catch( FileNotFoundException fnfe )
               {
                  logger.info("State file does not exist...");

                  // data model file does not exist so initialize values

                  logger.info("Created file handler");
                  logger.info("About to create file");

                  fileHandler.initializeStateFile(numAttempt, userID, userName, courseID, scoID, scoID);

                  logger.info("after initialize state file");
                  logger.info("State File Created");

                  fi = new FileInputStream(scoFile);
               }

               logger.info("Created LMSSCODataFile File input stream " + "successfully");

               fileIn = new ObjectInputStream(fi);

               logger.info("Created OBJECT input stream successfully");

               // Initialize the new attempt
               mSCOData = (SCODataManager)fileIn.readObject();

               fileIn.close();
               fi.close();

               // Create the sequencer and set the tree
               ADLSequencer mSequencer = new ADLSequencer();
               ADLValidRequests mState = new ADLValidRequests();
               SeqActivity mSeqActivity = mSeqActivityTree.getActivity(scoID);
               mSequencer.setActivityTree(mSeqActivityTree);
               
               
               // get UIState
               mSequencer.getValidRequests(mState);
               response.mValidRequests = mState;
               logger.info("continue  " + response.mValidRequests.mContinue);
               logger.info("previous  " + response.mValidRequests.mPrevious);

               Vector mStatusVector = new Vector();

               mStatusVector = mSequencer.getObjStatusSet(scoID);

               ADLObjStatus mObjStatus = new ADLObjStatus();
               
               // Temporary variables for obj initialization
               int err = 0;
               String obj = new String();

               // Initialize Objectives based on global objectives
               if( mStatusVector != null )
               {
                  if( newFile )
                  {
                     for( int i = 0; i < mStatusVector.size(); i++ )
                     {
                        // initialize objective status from sequencer
                        mObjStatus = (ADLObjStatus)mStatusVector.get(i);
                       
                        initializeObjectives(mSCOData, mObjStatus, i);
                     }
                  }
                  else
                  {
                     for( int i = 0; i < mStatusVector.size(); i++ )
                     {
                        int idx = -1;

                        // initialize objective status from sequencer
                        mObjStatus = (ADLObjStatus)mStatusVector.get(i);
                        
                        // get the count of current objectives
                        DMProcessingInfo pi = new DMProcessingInfo();
                        int result = DMInterface.processGetValue("cmi.objectives._count", true, mSCOData, pi);

                        int objCount = ( new Integer(pi.mValue) ).intValue();

                        // Find the current index for this objective
                        for( int j = 0; j < objCount; j++ )
                        {
                           pi = new DMProcessingInfo();
                           obj = "cmi.objectives." + j + ".id";

                           result = DMInterface.processGetValue(obj, true, mSCOData, pi);

                           if( pi.mValue.equals(mObjStatus.mObjID) )
                           {
                              
                              idx = j;
                              break;
                           }
                        }

                        if( idx != -1 )
                        {
                           initializeObjectives(mSCOData, mObjStatus, i);
                        }
                        else
                        {
                           System.out.println("  OBJ NOT FOUND --> " + mObjStatus.mObjID);
                        }

                     }
                  }
               }

               // try to read from file 
               // if successful, use that set for initialization of dm
               // otherwise initialize from default list in activity
               try
               {
                  readDataStores(mSCOData, mSeqActivity);
               }
               catch(FileNotFoundException fnfe)
               {
                  initializeDataStores(mSCOData, mSeqActivity.getDataStores());
               }

               String cmidmstr = ((SCORM_2004_DM)mSCOData.getDataModel("cmi")).toJSONString();
               String adldmstr =  ((SCORM_2004_NAV_DM)mSCOData.getDataModel("adl")).toJSONString();
               
               Gson gsondm = new Gson();
               HashMap<String, String> cmiobj = gsondm.fromJson(cmidmstr, HashMap.class);
               cmiobj.putAll(gsondm.fromJson(adldmstr, HashMap.class));
               
               StringBuilder sb = new StringBuilder();
               sb.append("{\"elems\":");
               sb.append(gsondm.toJson(cmiobj));
               sb.append(",\"validrequests\":");
               sb.append(gsondm.toJson(gsondm.fromJson(mState.toJSONString(), HashMap.class)));
               sb.append("}");
               out.print(sb.toString());
               
               // shrug.. this was done in clientrts.. now that it's removed, i moved this here
               SCORM_2004_NAV_DM navDM = (SCORM_2004_NAV_DM)mSCOData.getDataModel("adl");
               navDM.setValidRequests(mState);
               // finally write initialized dm to file so we can read it on set
               writeDM(mSCOData, scoFile + "_" + INITIALIZED_DM_EXT);
               break;

            case LMSCMIServletRequest.TYPE_SET:
               SCODataManager data = updateDM(request, scoFile, logger);
               response = handleData(data, userID, courseID,
                                       response, request, activityID, scoID, scoFile);

               Gson set_dm = new Gson();
               StringBuilder set_sb = new StringBuilder("{\n\"mError\":\"OK\"");
               if (response.mValidRequests != null)
               {
                  set_sb.append(",\n\"validrequests\":");
                  set_sb.append(set_dm.toJson(set_dm.fromJson(response.mValidRequests.toJSONString(), HashMap.class)));
               }
               set_sb.append("\n}");
               out.print(set_sb.toString());
               
               // shrug.. this was done in clientrts.. now that it's removed, i moved this here
               SCORM_2004_NAV_DM setnavDM = (SCORM_2004_NAV_DM)data.getDataModel("adl");
               setnavDM.setValidRequests(response.mValidRequests);
               writeDM(data, scoFile + "_" + INITIALIZED_DM_EXT);

               break;
         }

         out.flush();
         out.close();

      }
      catch( Exception e )
      {
         logger.severe(" :: doPost :: EXCEPTION");
         logger.severe(e.toString());
         e.printStackTrace();
      }
   }

   private SCODataManager updateDM(LMSCMIServletRequest request, String scoFile, Logger logger) throws IOException, ClassNotFoundException 
   {
      // don't handle error.. if this doesn't exist, all's wrong
      ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(scoFile + "_" + INITIALIZED_DM_EXT));

      // Initialize the new attempt
      SCODataManager SCOData = (SCODataManager)fileIn.readObject();
      fileIn.close();
      
      //loop request data calls
      for (String[] call : request.mActivityData )
      {
         SCOData.setValue(new DMRequest(call[0],call[1]));
      }
      return SCOData;
   }
   

   private void writeDM(SCODataManager dm, String iScoFile) throws FileNotFoundException, IOException 
   {
      FileOutputStream fo = new FileOutputStream(iScoFile);
      ObjectOutputStream outFile = new ObjectOutputStream(fo);
      outFile.writeObject(dm);
      outFile.close();
      fo.close();
   }

   /**
    * This method handles processing of the core data being sent from the client
    * to the LMS. The data needs to be processed and made persistent.
    * 
    * @param iSCOData The run-time data to be processed.
    * @param iUserID The ID of the learner associated with the persisted run-time data.
    * @param iCourseID The ID of the course associated with the persisted run-time data.
    * @param iResponse The response of this servlet.
    * @param iRequest request issued by the LMS Client.
    * @param iActivityID The ID of the activity associated with the persisted run-time data.
    * @param iScoID The ID of the SCO associated with the persisted run-time data.
    * @param iScoFile The name of the target persisted run-time data model file.
    * @return An updated LMSCMIServletResponse response
    */
   private LMSCMIServletResponse handleData(SCODataManager scoData,
                           String iUserID,
                           String iCourseID,
                           LMSCMIServletResponse iResponse,
                           LMSCMIServletRequest iRequest,
                           String iActivityID,
                           String iScoID,
                           String iScoFile)
   {
      Logger logger = Logger.getLogger("org.adl.util.debug.samplerte");
      logger.info("LMSCMIServlet - Entering handleData()");
      
      iResponse = new LMSCMIServletResponse();

      String userDir = SRTEFILESDIR + File.separator + iUserID + File.separator + iCourseID;
      
      boolean setPrimaryObjScore = false;
      boolean suspended = false;

      try
      {
         String completionStatus = null;
         String progressMeasure = null;
         String scoEntry = null;
         double normalScore = -1.0;
         String masteryStatus = null;
         String sessionTime = null;
         String score = null;

//         SCODataManager scoData = iRequest.mActivityData;

         int err = 0;
         DMProcessingInfo dmInfo = new DMProcessingInfo();        
         
         // Get the current entry
         err = DMInterface.processGetValue("cmi.exit", true, true, scoData, dmInfo);
         scoEntry = dmInfo.mValue;         
         
         // call terminate on the sco data
         scoData.terminate();

         // Get the current completion_status
         err = DMInterface.processGetValue("cmi.completion_status", true, scoData, dmInfo);
         completionStatus = dmInfo.mValue;
         boolean completionSetBySCO = dmInfo.mSetBySCO;

         if( completionStatus.equals("not attempted") )
         {
            completionStatus = "incomplete";
         }
         
         dmInfo = new DMProcessingInfo();
         err = DMInterface.processGetValue("cmi.progress_measure", true, scoData, dmInfo);
         if ( err == DMErrorCodes.NO_ERROR )
         {
            progressMeasure = dmInfo.mValue;
         }

         // Get the current success_status
         err = DMInterface.processGetValue("cmi.success_status", true, scoData, dmInfo);
         masteryStatus = dmInfo.mValue;
         boolean masterySetBySCO = dmInfo.mSetBySCO;

         // Get the current entry
         err = DMInterface.processGetValue("cmi.entry", true, true, scoData, dmInfo);
         scoEntry = dmInfo.mValue;        

         // Get the current scaled score
         err = DMInterface.processGetValue("cmi.score.scaled", true, scoData, dmInfo);

         if( err == DMErrorCodes.NO_ERROR )
         {
            logger.info("Got score, with no error");
            score = dmInfo.mValue;
         }
         else
         {
            logger.info("Failed getting score, got err: " + err);
            score = "";
         }

         // Get the current session time
         err = DMInterface.processGetValue("cmi.session_time", true, scoData, dmInfo);
         if( err == DMErrorCodes.NO_ERROR )
         {
            sessionTime = dmInfo.mValue;
         }

         logger.info("Saving Data to the File ...  PRIOR TO SAVE");
         logger.info("The SCO Data Manager for the current SCO contains the " + "following:");

         // Open the Activity tree flat file associated with the
         // logged in user
         String theWebPath = getServletConfig().getServletContext().getRealPath("/");

         String actFile = userDir + File.separator + "serialize.obj";
         // Only perform data mapping on Terminate or if the Quit button was pushed.
         if( iRequest.mIsFinished || iRequest.mQuitPushed || iRequest.mSuspendPushed)
         {
            logger.info("About to get and update activity tree");
            FileInputStream fi;
            try
            {
               fi = new FileInputStream(actFile);
            }
            catch( FileNotFoundException fnfe )
            {
               logger.severe("Can not open Activity tree file");
               fi = new FileInputStream(actFile);
            }
            logger.info("Created Activity FILE input stream successfully");

            ObjectInputStream fileIn = new ObjectInputStream(fi);

            logger.info("Created Activity Tree OBJECT input stream " + "successfully");

            SeqActivityTree theTree = (SeqActivityTree)fileIn.readObject();
            fileIn.close();
            fi.close();
            logger.info("(*********DUMPING ActivityTree***********)");
            if( theTree == null )
            {

               logger.info("The activity tree is NULL");
            }
            else
            {
               theTree.dumpState();
            }
            if( theTree != null )
            {
               // Create the sequencer and set the tree
               ADLSequencer theSequencer = new ADLSequencer();
               theSequencer.setActivityTree(theTree);

               SeqActivity act = theTree.getActivity(iActivityID);
               populateMap(scoData, act);

               // Only modify the TM if the activity is tracked
               if( act.getIsTracked() )
               {

                  // Update the activity's status
                  logger.info(act.getID() + " is TRACKED -- ");
                  logger.info("Performing default mapping to TM");

                  String primaryObjID = null;
                  boolean foundPrimaryObj = false;
                  boolean setPrimaryObjSuccess = false;
                  boolean sesPrimaryObjScore = false;

                  // Find the primary objective ID
                  Vector objs = act.getObjectives();

                  if( objs != null )
                  {
                     for( int j = 0; j < objs.size(); j++ )
                     {
                        SeqObjective obj = (SeqObjective)objs.elementAt(j);
                        if( obj.mContributesToRollup )
                        {
                           if( obj.mObjID != null )
                           {
                              primaryObjID = obj.mObjID;
                           }
                           break;
                        }
                     }
                  }

                  // Get the activities objective list
                  // Map the DM to the TM
                  err = DMInterface.processGetValue("cmi.objectives._count", true, scoData, dmInfo);
                  Integer size = new Integer(dmInfo.mValue);
                  int numObjs = size.intValue();
                  // Loop through objectives updating TM
                  for( int i = 0; i < numObjs; i++ )
                  {
                     logger.info("CMISerlet - IN MAP OBJ LOOP");
                     String objID = new String("");
                     String objMS = new String("");
                     String objScore = new String("");
                     String obj = new String("");
                     // Get this objectives id
                     obj = "cmi.objectives." + i + ".id";
                     err = DMInterface.processGetValue(obj, true, scoData, dmInfo);
                     objID = dmInfo.mValue;

                     if( primaryObjID != null && objID.equals(primaryObjID) )
                     {
                        foundPrimaryObj = true;
                     }
                     else
                     {
                        foundPrimaryObj = false;
                     }

                     // Get this objectives mastery
                     obj = "cmi.objectives." + i + ".success_status";
                     err = DMInterface.processGetValue(obj, true, scoData, dmInfo);
                     objMS = dmInfo.mValue;

                     // Report the success status
                     if( objMS.equals("passed") )
                     {
                        theSequencer.setAttemptObjSatisfied(iActivityID, objID, "satisfied");
                        if( foundPrimaryObj )
                        {
                           act.primaryStatusSetBySCO(dmInfo.mSetBySCO);
                           setPrimaryObjSuccess = true;
                           masteryStatus = objMS;
                        }
                     }
                     else if( objMS.equals("failed") )
                     {
                        theSequencer.setAttemptObjSatisfied(iActivityID, objID, "notSatisfied");

                        if( foundPrimaryObj )
                        {
                           act.primaryStatusSetBySCO(dmInfo.mSetBySCO);
                           setPrimaryObjSuccess = true;
                           masteryStatus = objMS;
                        }
                     }
                     else
                     {
                        if ( dmInfo.mSetBySCO )
                        {
                           theSequencer.setAttemptObjSatisfied(iActivityID, objID, "unknown");
                           
                           Vector globs = act.getObjIDs(objID, false);

                           if ( globs != null )
                           {
                              for ( int w = 0; w < globs.size(); w++)
                              {
                                 ADLSeqUtilities.setGlobalObjSatisfied((String)globs.get(w), 
                                    iUserID,
                                    act.getScopeID(),
                                    ADLTracking.TRACK_UNKNOWN);
                              }
                           }
                           if ( foundPrimaryObj )
                           {
                              act.primaryStatusSetBySCO(dmInfo.mSetBySCO);
                              setPrimaryObjSuccess = true;
                              masteryStatus = objMS;
                           }
                        }
                     }

                     // Get this objectives measure
                     obj = "cmi.objectives." + i + ".score.scaled";
                     err = DMInterface.processGetValue(obj, true, scoData, dmInfo);
                     if( err == DMErrorCodes.NO_ERROR )
                     {
                        objScore = dmInfo.mValue;
                     }

                     // Report the measure
                     if( !objScore.equals("") && !objScore.equals("unknown") )
                     {
                        try
                        {
                           normalScore = ( new Double(objScore) ).doubleValue();
                           theSequencer.setAttemptObjMeasure(iActivityID, objID, normalScore);

                           if( foundPrimaryObj )
                           {
                              setPrimaryObjScore = true;
                           }
                        }
                        catch( Exception e )
                        {
                           logger.severe("  ::--> ERROR: Invalid score");
                           logger.severe("  ::  " + normalScore);

                           logger.severe(e.toString());
                           e.printStackTrace();
                        }
                     }
                     else
                     {
                        theSequencer.clearAttemptObjMeasure(iActivityID, objID);
                     }
                     
                     
                        
                     // Get this objectives completion status
                     dmInfo = new DMProcessingInfo();
                     obj = "cmi.objectives." + i + ".completion_status";
                     err = DMInterface.processGetValue(obj, true, scoData, dmInfo);
                     String completion = ADLTracking.TRACK_UNKNOWN;
                     if( err == DMErrorCodes.NO_ERROR )
                     {
                        completion = dmInfo.mValue;
                     }

                     // Report the completion status
                     if( (!completion.equals("") && !completion.equals(ADLTracking.TRACK_UNKNOWN)) ||
                           (completion.equals(ADLTracking.TRACK_UNKNOWN) && dmInfo.mSetBySCO) )
                     {
                        
                        if ( foundPrimaryObj && ! completionSetBySCO )
                        {
                           completionStatus = (completion.equals("not attempted"))?"incomplete":completion;
                           completionSetBySCO = dmInfo.mSetBySCO;
                        }
                        else
                        {
                           theSequencer.setAttemptObjCompletionStatus(iActivityID, objID, completion);
                        }
                     }
                     else
                     {
                        theSequencer.clearAttemptObjCompletionStatus(iActivityID, objID);
                     }
                     
                     
                     
                     // Get this objectives progress measure
                     dmInfo = new DMProcessingInfo();
                     objScore = "";
                     obj = "cmi.objectives." + i + ".progress_measure";
                     err = DMInterface.processGetValue(obj, true, scoData, dmInfo);
                     if( err == DMErrorCodes.NO_ERROR )
                     {
                        objScore = dmInfo.mValue;
                     }

                     // Report the measure
                     if( !objScore.equals("") && !objScore.equals("unknown") )
                     {
                        try
                        {
                           normalScore = Double.parseDouble(objScore);
                           
                           if ( foundPrimaryObj && progressMeasure == null )
                           {
                              progressMeasure = objScore;
                           }
                           else
                           {
                              theSequencer.setAttemptObjProgressMeasure(iActivityID, objID, normalScore);
                           }
                        }
                        catch( NumberFormatException e )
                        {
                           logger.severe("  ::--> ERROR: Invalid min score");
                           logger.severe("  ::  " + normalScore);

                           logger.severe(e.toString());
                           e.printStackTrace();
                        }
                     }
                     else
                     {
                        theSequencer.clearAttemptObjProgressMeasure(iActivityID, objID);
                     }
                     
                     
                     

                     setObjectiveValues(theSequencer, logger, iActivityID, objID, scoData, i);
                  }
                  // Report the completion status
                  act.primaryProgressSetBySCO(completionSetBySCO);
                  theSequencer.setAttemptProgressStatus(iActivityID, completionStatus);
                  if ( progressMeasure != null )
                  {
                     theSequencer.setAttemptProgressMeasure(iActivityID, progressMeasure);
                  }

                  theSequencer.reportSuspension(iActivityID, scoEntry.equals("resume"));
                  
                  // Report the success status
                  if( masteryStatus.equals("passed") )
                  {
                     theSequencer.setAttemptObjSatisfied(iActivityID, mPRIMARY_OBJ_ID, "satisfied");
                  }
                  else if( masteryStatus.equals("failed") )
                  {
                     theSequencer.setAttemptObjSatisfied(iActivityID, mPRIMARY_OBJ_ID, "notSatisfied");
                  }
                  else
                  {
                     if( masterySetBySCO )
                     {
                        act.primaryStatusSetBySCO(masterySetBySCO);
                        theSequencer.setAttemptObjSatisfied(iActivityID, mPRIMARY_OBJ_ID, "unknown");
                        
                        Vector globs = act.getObjIDs(mPRIMARY_OBJ_ID, false);
                        if ( globs != null )
                        {
                           for ( int w = 0; w < globs.size(); w++)
                           {
                              ADLSeqUtilities.setGlobalObjSatisfied((String)globs.get(w), 
                                 iUserID,
                                 act.getScopeID(),
                                 ADLTracking.TRACK_UNKNOWN);
                           }
                        }
                     }                             
                  }

                  // Report the measure
                  if( !score.equals("") && !score.equals("unknown") )
                  {
                     try
                     {
                        normalScore = ( new Double(score) ).doubleValue();
                        theSequencer.setAttemptObjMeasure(iActivityID, mPRIMARY_OBJ_ID, normalScore);
                     }
                     catch( Exception e )
                     {
                        logger.severe("  ::--> ERROR: Invalid score");
                        logger.severe("  ::  " + normalScore);

                        logger.severe(e.toString());
                        e.printStackTrace();
                     }
                  }
                  else
                  {
                     if( !setPrimaryObjScore )
                     {
                        theSequencer.clearAttemptObjMeasure(iActivityID, mPRIMARY_OBJ_ID);
                     }
                  }
               }
               else
               {
                  err = DMInterface.processGetValue("cmi.objectives._count", true, scoData, dmInfo);
                  Integer size = new Integer(dmInfo.mValue);
                  int numObjs = size.intValue();

                  // Loop through objectives updating TM
                  for( int i = 0; i < numObjs; i++ )
                  {
                     logger.info("CMISerlet - IN MAP OBJ LOOP - tracking == false");
                     String objID = new String("");
                     String objMS = new String("");
                     String objScore = new String("");
                     String obj = new String("");

                     // Get this objectives id
                     obj = "cmi.objectives." + i + ".id";
                     err = DMInterface.processGetValue(obj, true, scoData, dmInfo);

                     objID = dmInfo.mValue;

                     setObjectiveValues(theSequencer, logger, iActivityID, objID, scoData, i);
                  }
     
               }

               // May need to get the current valid requests
               iResponse.mValidRequests = new ADLValidRequests();
               theSequencer.getValidRequests(iResponse.mValidRequests);

               logger.info("Sequencer is initialized and statuses have been " + "set");
               logger.info("Now re-serialize the file");

               FileOutputStream fo = new FileOutputStream(actFile);

               ObjectOutputStream outFile = new ObjectOutputStream(fo);

               SeqActivityTree theTempTree = theSequencer.getActivityTree();

               theTempTree.clearSessionState();

               outFile.writeObject(theTempTree);
               outFile.close();

            }
         }

         // Persist the run-time data model
         writeDM(scoData, iScoFile);

      }
      catch( FileNotFoundException fnfe )
      {
         logger.severe(fnfe.toString());
         fnfe.printStackTrace();
      }
      catch( IOException ioe )
      {
         logger.severe(ioe.toString());
         ioe.printStackTrace();
      }
      catch( ClassNotFoundException cnfe )
      {
         logger.severe(cnfe.toString());
         cnfe.printStackTrace();
      }
      
      return iResponse;
   }

   
   /**
    * Takes the sco data and sets the values in the sequencer and database.
    * 
    * @param ioSequencer The sequencer.
    * @param ioLogger The logger.
    * @param iActivityID The activity id.
    * @param iObjID The objective id.
    * @param ioScoData The sco data.
    * @param iIndex The index.
    */
   private void setObjectiveValues(ADLSequencer ioSequencer, Logger ioLogger, String iActivityID, String iObjID, SCODataManager ioScoData, int iIndex)
   {
      // Get this objectives raw score
      DMProcessingInfo dmInfo = new DMProcessingInfo();
      String objScore = "";
      String obj = "cmi.objectives." + iIndex + ".score.raw";
      int err = DMInterface.processGetValue(obj, true, ioScoData, dmInfo);
      if( err == DMErrorCodes.NO_ERROR )
      {
         objScore = dmInfo.mValue;
      }

      double normalScore = -999;
      
      // Report the raw score
      if( !objScore.equals("") && !objScore.equals("unknown") )
      {
         try
         {
            normalScore = Double.parseDouble(objScore);
            ioSequencer.setAttemptObjRawScore(iActivityID, iObjID, normalScore);
         }
         catch( NumberFormatException e )
         {
            ioLogger.severe("  ::--> ERROR: Invalid raw score");
            ioLogger.severe("  ::  " + normalScore);

            ioLogger.severe(e.toString());
            e.printStackTrace();
         }
      }
      else
      {
         ioSequencer.clearAttemptObjRawScore(iActivityID, iObjID);
      }
      
      // Get this objectives min score
      dmInfo = new DMProcessingInfo();
      objScore = "";
      obj = "cmi.objectives." + iIndex + ".score.min";
      err = DMInterface.processGetValue(obj, true, ioScoData, dmInfo);
      if( err == DMErrorCodes.NO_ERROR )
      {
         objScore = dmInfo.mValue;
      }

      // Report the min score
      if( !objScore.equals("") && !objScore.equals("unknown") )
      {
         try
         {
            normalScore = Double.parseDouble(objScore);
            ioSequencer.setAttemptObjMinScore(iActivityID, iObjID, normalScore);
         }
         catch( NumberFormatException e )
         {
            ioLogger.severe("  ::--> ERROR: Invalid min score");
            ioLogger.severe("  ::  " + normalScore);

            ioLogger.severe(e.toString());
            e.printStackTrace();
         }
      }
      else
      {
         ioSequencer.clearAttemptObjMinScore(iActivityID, iObjID);
      }
      
      // Get this objectives max score
      dmInfo = new DMProcessingInfo();
      objScore = "";
      obj = "cmi.objectives." + iIndex + ".score.max";
      err = DMInterface.processGetValue(obj, true, ioScoData, dmInfo);
      if( err == DMErrorCodes.NO_ERROR )
      {
         objScore = dmInfo.mValue;
      }

      // Report the max score
      if( !objScore.equals("") && !objScore.equals("unknown") )
      {
         try
         {
            normalScore = Double.parseDouble(objScore);
            ioSequencer.setAttemptObjMaxScore(iActivityID, iObjID, normalScore);
         }
         catch( NumberFormatException e )
         {
            ioLogger.severe("  ::--> ERROR: Invalid min score");
            ioLogger.severe("  ::  " + normalScore);

            ioLogger.severe(e.toString());
            e.printStackTrace();
         }
      }
      else
      {
         ioSequencer.clearAttemptObjMaxScore(iActivityID, iObjID);
      }
      
   }
   
   /**
    * Initializes the run-time objectives collection based on the objective status from the sequencer.
    * 
    * @param ioSCOData The run-time data to be processed.
    * 
    * @param ioObjStatus The objective status from the sequencer.
    * 
    * @param iIndex The location within the objectives collection in which to store the status.
    */
   private void initializeObjectives(SCODataManager ioSCOData, ADLObjStatus ioObjStatus, int iIndex)
   {
      int err;
      String obj;
      
      // Set the objectives id
      obj = "cmi.objectives." + iIndex + ".id";

      err = DMInterface.processSetValue(obj, ioObjStatus.mObjID, true, ioSCOData);

      // Set the objectives success status
      obj = "cmi.objectives." + iIndex + ".success_status";

      if( ioObjStatus.mStatus.equalsIgnoreCase("satisfied") )
      {
         err = DMInterface.processSetValue(obj, "passed", true, ioSCOData);
      }
      else if( ioObjStatus.mStatus.equalsIgnoreCase("notSatisfied") )
      {
         err = DMInterface.processSetValue(obj, "failed", true, ioSCOData);
      }
      
      // Set the objectives completion status
      obj = "cmi.objectives." + iIndex + ".completion_status";

      err = DMInterface.processSetValue(obj, ioObjStatus.mCompletionStatus, true, ioSCOData);

      // Set the objectives scaled score
      obj = "cmi.objectives." + iIndex + ".score.scaled";

      if( ioObjStatus.mHasMeasure )
      {
         Double norm = new Double(ioObjStatus.mMeasure);
         err = DMInterface.processSetValue(obj, norm.toString(), true, ioSCOData);
      }
      
      // Set the objectives raw score
      obj = "cmi.objectives." + iIndex + ".score.raw";
      
      if( ioObjStatus.mHasRawScore )
      {
         Double norm = new Double(ioObjStatus.mRawScore);
         err = DMInterface.processSetValue(obj, norm.toString(), true, ioSCOData);
      }
      
      // Set the objectives min score
      obj = "cmi.objectives." + iIndex + ".score.min";
      
      if( ioObjStatus.mHasMinScore )
      {
         Double norm = new Double(ioObjStatus.mMinScore);
         err = DMInterface.processSetValue(obj, norm.toString(), true, ioSCOData);
      }
      
      // Set the objectives max score
      obj = "cmi.objectives." + iIndex + ".score.max";
      
      if( ioObjStatus.mHasMaxScore )
      {
         Double norm = new Double(ioObjStatus.mMaxScore);
         err = DMInterface.processSetValue(obj, norm.toString(), true, ioSCOData);
      }
      
      // Set the objectives progress measure
      obj = "cmi.objectives." + iIndex + ".progress_measure";
      
      if( ioObjStatus.mHasProgressMeasure )
      {
         Double norm = new Double(ioObjStatus.mProgressMeasure);
         err = DMInterface.processSetValue(obj, norm.toString(), true, ioSCOData);
      }
   }

   /**
    * Populate the collection of data stores.
    * 
    * @param iSCOData The run-time data to be processed.
    * @param iSeqActivity The current activity.
    */
   private void populateMap(SCODataManager iSCOData, SeqActivity iSeqActivity)
   {
      // get list of data ids for this activity
      if ( iSeqActivity != null && SeqActivityTree.dsMap != null )
      {
         List dataStores = iSeqActivity.getDataStores();
         for ( int i = 0; i < dataStores.size(); i++ )
         {
            DataStore ds = (DataStore)dataStores.get(i);
            // get the value for that store if it's writeable
            if ( ds.isWriteable() )
            {            
               // write to maps
               String val = getValue(iSCOData, ds.getId());
               SeqActivityTree.dsMap.put(ds, val);
            }
         }
      }
   }

   /**
    * Gets the value of the data store element based on the supplied ID.
    * 
    * @param iSCOData The run-time data to be processed.
    * @param iId The ID of the data store to access.
    * 
    * @return The value stored in the data store data model element.
    */
   private String getValue(SCODataManager iSCOData, String iId)
   {
      String returnthis = null;
      DMProcessingInfo dminfo = new DMProcessingInfo();
      iSCOData.getValue(new DMRequest("adl.data._count"), dminfo);
      int count = Integer.parseInt(dminfo.mValue);
      for ( int i = 0; i < count; i++ )
      {
         dminfo = new DMProcessingInfo();
         iSCOData.getValue(new DMRequest("adl.data." + i + ".id"), dminfo);
         if ( iId.equals(dminfo.mValue) )
         {
            dminfo = new DMProcessingInfo();
            iSCOData.getValue(new DMRequest("adl.data." + i + ".store", true, false), dminfo);
            returnthis = dminfo.mValue;
            break;
         }
      }
         
      return returnthis;
   }


   /**
    * Repopulates the data from the DM to the data stores.
    * 
    * @param ioSCOData The run-time data to be processed
    * @param iSeqActivity The current activity.
    */
   private void restoreDataStores(SCODataManager ioSCOData, SeqActivity iSeqActivity)
   {
      List dataStores = iSeqActivity.getDataStores();
      initializeDataStores(ioSCOData, dataStores);
      for ( int i = 0; i < dataStores.size(); i++ )
      {
         if ( SeqActivityTree.dsMap.get(dataStores.get(i)) != null )
         {
            ioSCOData.setValue(new DMRequest("adl.data." + i + ".store", (String)SeqActivityTree.dsMap.get(dataStores.get(i)), true));
         }
      }
   }

   /**
    * Sets up the data data model element's ids and access rights and creates the data store tracking 
    * collection.
    * 
    * @param ioSCOData The run-time data to be processed.
    * @param iDataStores The list of data stores for the current activity.
    */
   private void initializeDataStores(SCODataManager ioSCOData, List iDataStores)
   {
      if ( SeqActivityTree.dsMap == null )
      {
         SeqActivityTree.dsMap = new HashMap();
      }

      // loop through
      for ( int i = 0; i < iDataStores.size(); i++ )
      {
         // initialize id and r/w values per record
         DataStore ds = (DataStore)iDataStores.get(i);
         ioSCOData.setValue(new DMRequest("adl.data." + i + ".id", ds.getId(), true));
         ioSCOData.setValue(new DMRequest("adl.data." + i + ".store._access", ds.isReadable() + "<>" + ds.isWriteable(), true));
         if ( ! SeqActivityTree.dsMap.containsKey(ds) )
         {
            SeqActivityTree.dsMap.put(ds, null);
         }
      }
   }

   /**
    * Read in the data store tracking collection from the file system.
    * 
    * @param iSCOData The run-time data to be processed
    * @param iSeqActivity The current activity.
    * 
    * @throws FileNotFoundException Thrown if the data store is not found at the
    *                               expected location. 
    * @throws IOException Thrown if there's an i/o exception.
    * @throws ClassNotFoundException Thrown if read object can't find the specified class.
    */
   private void readDataStores(SCODataManager iSCOData, SeqActivity iSeqActivity) throws FileNotFoundException, IOException, ClassNotFoundException
   {
      FileInputStream fin = new FileInputStream(SeqActivityTree.dataStoreLoc);
      ObjectInputStream oin = new ObjectInputStream(fin);
      SeqActivityTree.dsMap = (HashMap)oin.readObject();
      restoreDataStores(iSCOData, iSeqActivity);
      oin.close();
      fin.close();
   }

} // LMSCMIServlet
