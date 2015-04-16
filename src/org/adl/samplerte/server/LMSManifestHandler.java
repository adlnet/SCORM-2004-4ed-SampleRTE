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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.adl.samplerte.server.packageimport.LMSImportHandler;
import org.adl.samplerte.server.packageimport.LaunchData;
import org.adl.samplerte.server.packageimport.ManifestHandler;
import org.adl.samplerte.server.packageimport.parsers.dom.ADLDOMParser;
import org.adl.samplerte.server.packageimport.parsers.dom.DOMTreeUtility;
import org.adl.samplerte.util.LMSDatabaseHandler;
import org.adl.samplerte.util.RTEFileHandler;
import org.adl.sequencer.ADLLaunch;
import org.adl.sequencer.ADLSeqUtilities;
import org.adl.sequencer.ADLSequencer;
import org.adl.sequencer.SeqActivityTree;
import org.adl.sequencer.SeqNavRequests;
import org.adl.util.debug.DebugIndicator;
import org.adl.util.decode.decodeHandler;
import org.adl.validator.Validator;
import org.adl.validator.util.Result;
import org.adl.validator.util.ResultCollection;
import org.adl.validator.util.ValidatorCheckerNames;
import org.adl.validator.util.ValidatorMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class contains methods to parse an imsmanifest.xml file, process a 
 * content package,and simplify the access of data in a DOM tree that 
 * corresponds to the 
 * imsmanifest.xml file from a PIF.  <br><br>
 * 
 * <strong>Filename:</strong> LMSManifestHandler.java<br><br>
 * 
 * <strong>Description:</strong> <br><br>
 * This class contains methods used by the Sample Run-Time Environment to parse
 * the imsmanifest.xml file and process the content package using the ADL SCORM
 * Validator.  It also contains methods used by both the Sample Run-Time 
 * Environment and the ADL Sequencer to gain access to information in the 
 * imsmanifest.xml file.
 * <br><br>
 * 
 * <strong>Design Issues:</strong><br><br>
 * 
 * <strong>Implementation Issues:</strong><br><br>
 * 
 * <strong>Known Problems:</strong> <br><br>
 * 
 * <strong>Side Effects:</strong> <br><br>
 * 
 * <strong>References:</strong> SCORM <br><br>
 * 
 * @author ADL Technical Team
 */

public class LMSManifestHandler implements Serializable 
{
   /**
    * This is the title of the course.  It will be populated with the value of
    * the title attribute of an organization element.
    */
   private String mCourseTitle;

   /**
    * This is the ID of the course.  It will be given the value of the
    * nextCourseID stored in the Application Data in the SampleRTE database.
    */
   private String mCourseID;

   /**
    * This is the path that will be used when copying files.
    */
   private String mWebPath;

   /**
    * This is a list of the &lt;organization&gt; elements in the manifest.
    */
   private Vector mOrganizationList;

   /**
    * This vector will consist of LaunchData objects containing information
    * from the organization and resource elements which will be stored in the
    * SampleRTE database.
    */
   private Vector mLaunchDataList;
   
   /**
    * A boolean indicating the success of the file copying
    */
   private boolean mFileCopyResult = true;  
   
   /**
    * A boolean indicating the whether the course contains selectable activities
    */
   private boolean mLaunchableCourse = true;
   
   /**
    * Logger object used for debug logging.
    */
   private Logger mLogger = Logger.getLogger("org.adl.util.debug.samplerte");

   /**
    * The manifest element of the imsmanifest.xml file.
    */
   private Node mManifest;

   /**
    * The location of the schema xsd files.
    */
   private String mXSDLocation;
   
   /**
    * Whether or not to validate completely based on internet connection
    */
   private boolean mOnlineValidation = true;
   
   /**
    * Default constructor method which initializes member variables
    * 
    * @param iXSDLocation
    *           The location where the XSDs can be found for use 
    *           during validation.
    * 
    */ 
   public LMSManifestHandler( String iXSDLocation )
   {
      mOrganizationList = new Vector();
      mLaunchDataList = new Vector();
      mCourseTitle = "";
      mCourseID = "";
      mManifest = null;
      mXSDLocation = iXSDLocation;
      mWebPath = "";
   }



   /**
    * Uses the CPValidator and ADLValidatorOutcome classes of the 
    * <code>ADLValidator</code> to parse a manifest file and to create the 
    * corresponding DOM tree.  This tree is then traversed (with the use of 
    * additional <code>LMSManifestHandler</code> methods, appropriate 
    * database inserts are performed, a template activity tree is created
    * using the ADLSeqUtilities class, and serialized files are created for 
    * each organization element in the manfest.  
    * <br><br>
    * 
    * @param iFilePath - A string representing the path of the file to be 
    *                   validated.
    * @param iOnline - A boolean value representing whether or not validation
    *                    should be performed.
    * 
    * @return An ADLValidator object containing the DOM object as well as 
    * validation results.
    */
   public ResultCollection processPackage( String iFilePath, 
                                              boolean iOnlineValidation )
   {
      mOnlineValidation = iOnlineValidation;
      
      RTEFileHandler fileHandler = new RTEFileHandler();
      ResultCollection results = new ResultCollection();
      LMSImportHandler importHandler = new LMSImportHandler();
      
      boolean dbUpdateResult = true;      
      boolean zipExtractionResult = true;
      
       // This is the DOM structure that will be returned with the 
       // ADLValidatorOutcome class.  It will contain all of the 
       // information contained in the imsmanifest file and will serve as a means
       // of accessing that information.       
      Document document = null;      
      
      mLogger.entering("---LMSManifestHandler", "processManifest()");

      // Configure validator and execute validator parse
      Validator scormValidator = new Validator(iFilePath, "contentaggregation", false);

      // Setup required checkers for off-line and online import
      List checkerList = new ArrayList();
      checkerList.add("org.adl.validator.packagechecker.checks.IMSManifestAtRootChecker");
      checkerList.add("org.adl.validator.packagechecker.checks.WellformednessChecker");
      checkerList.add("org.adl.validator.packagechecker.checks.ManifestRootElementChecker");
      
      // Only run checker if off-line validation is selected
      if ( !iOnlineValidation )
      {
         checkerList.add("org.adl.samplerte.server.packageimport.sequencingOrderChecker.SequencingOrderChecker");
      }

      // Setup the rest of the checkers for off-line and online import
      checkerList.add("org.adl.validator.packagechecker.checks.SubmanifestChecker");
      checkerList.add("org.adl.validator.packagechecker.checks.RequiredFilesChecker");
      checkerList.add("org.adl.validator.packagechecker.checks.SchemaValidationChecker");
      checkerList.add("org.adl.validator.packagechecker.checks.ResourceHrefChecker");
      checkerList.add("org.adl.validator.packagechecker.checks.ApplicationProfileChecker");
      checkerList.add("org.adl.validator.packagechecker.checks.ExcessBaggageChecker");

      scormValidator.setCheckerList(checkerList);
      
      scormValidator.validate();
      results = scormValidator.getResultCollection();
      
      boolean validationPassed = determineValidationStatus(results, iOnlineValidation);

      mLogger.info( "Document parsing complete." ); 
      
      String packageLocation = "";
      String manifestFile = "";
      if ( validationPassed )
      {
         // Extract the package, exit if an error occurs
         packageLocation = LMSImportHandler.importContentPackage(mXSDLocation, iFilePath);
         if ( packageLocation.equals("") )               
         {
            // Add zip error message to validation results
            Result zipResult = new Result();
            zipResult.setPackageCheckerPassed(false);
            String zipMessage = "Zip Extraction Error Has Occurred"; 
            zipResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.FAILED, zipMessage));
            results.addPackageResult( zipResult );               
            return results;
         }

         if (!System.getProperty("os.name").toLowerCase().startsWith("windows")){
             manifestFile = packageLocation + "//imsmanifest.xml";
         }
         else{
             manifestFile = packageLocation + "\\imsmanifest.xml";
         }

         
         // Parse and create domument object from manifest
         ADLDOMParser domParser = new ADLDOMParser();
         
         boolean success = domParser.createDocument(manifestFile, true, false);
         
         // The document creating parse failed
         if ( !success )
         {
            // Add parse error message to validation results
            Result parseResult = new Result();
            parseResult.setPackageCheckerPassed(false);
            String zipMessage = "Parser Error Has Occurred"; 
            parseResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.FAILED, zipMessage));
            results.addPackageResult( parseResult );               
            return results;
         }
         
         // Create LMSImportHandler
         importHandler = new LMSImportHandler( domParser.getDocument() );
         
         // Apply xml:bases, roll-up the manifest, and obtain the modified document
         importHandler.applyXMLBase();
         document = importHandler.getDocument();

         mLaunchDataList = importHandler.getLaunchData(
                     document, false, false);
         
         this.mManifest = document.getDocumentElement();

         // get information from manifest and update database
         mOrganizationList = this.getOrganizationList();
         dbUpdateResult = updateDB();
      }
      else
      {
         if ( results.getPackageResult(ValidatorCheckerNames.WELLFORM).isPackageCheckerPassed() ) 
         {
            mLogger.info("NOT WELL FORMED!!!");
         }
         if ( results.getPackageResult(ValidatorCheckerNames.MAN_AT_ROOT).isPackageCheckerPassed() )
         {
            mLogger.info("INVALID ROOT!!!");
         }
         if ( results.getPackageResult(ValidatorCheckerNames.SCHEMA_VAL).isPackageCheckerPassed() )
         {
            mLogger.info("NOT VALID TO SCHEMA!!!");
         }
         if ( results.getPackageResult(ValidatorCheckerNames.APP_PROFILE).isPackageCheckerPassed() )
         {
            mLogger.info("NOT VALID TO APP PROFILE!!!");
         }
         if ( results.getPackageResult(ValidatorCheckerNames.REQ_FILES).isPackageCheckerPassed() )
         {
            mLogger.info("REQUIRED FILES DO NOT EXIST!!!");
         }

         mLogger.info("-----NOT CONFORMANT!!!----");
      }          
      
      // Create a Result object to hold the results of the file and dBase updates
      String message = "";
      Result processingResult = new Result();
      processingResult.setPackageCheckerName("PACKAGE_PROCESSING");         
      
      if ( !dbUpdateResult )
      {
         processingResult.setPackageCheckerPassed(false);            
         message = "Database Update Error Has Occurred"; 
         processingResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.FAILED, message));
      }
      if ( !mFileCopyResult )
      {
         processingResult.setPackageCheckerPassed(false);
         message = "File Copy Error Has Occurred"; 
         processingResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.FAILED, message));
      }
      if ( !zipExtractionResult )
      {
         processingResult.setPackageCheckerPassed(false);
         message = "Zip Extraction Error Has Occurred"; 
         processingResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.FAILED, message));
      }
      if ( !mLaunchableCourse )
      {
         processingResult.setPackageCheckerPassed(false);
         message = "Control Modes defined in this course are not supported for launch in the SRTE"; 
         processingResult.addPackageCheckerMessage(new ValidatorMessage(ValidatorMessage.WARNING, message));
      }

      results.addPackageResult(processingResult);
     
      LMSImportHandler.cleanImportDirectory( mXSDLocation + File.separator 
                                           + "PackageImport" );

      fileHandler.deleteTempUloadFiles();

      mLogger.exiting( "---LMSManifestHandler", "processManifest()" ); 
      //  Return boolean signifying whether or not the parsing was successful
      return results;   
   }


   /**
    * This method will copy a course from the specified directory where it
    * already exists, to a new specified directory where it is to be copied to.
    * 
    * @param iInFilePath - The path of the current file or directory that needs
    *                      to be copied.
    * @param iOutFilePath - The path of the directory that the file is to be
    *                       copied to.
    * @return boolen - A boolean indicating the success of the course copying                      
    */
   private boolean copyCourse( String iInFilePath, String iOutFilePath ) 
   {
      boolean result = true;
      try
      {
         String inDirName = iInFilePath;
         inDirName.replace('/',java.io.File.separatorChar);

         File tempFile = new File(inDirName);
         File[] fileNames = tempFile.listFiles();

         String outDirName = iOutFilePath;

         outDirName = outDirName.replace('/',java.io.File.separatorChar);
         File tempDir = new File(outDirName);
         tempDir.mkdirs();
         
         FileInputStream fi = null;
         FileOutputStream fo = null;
         BufferedInputStream in = null;
         BufferedOutputStream out = null;
           
         for ( int i=0; i < fileNames.length; i++ )
         {
            String tempString = outDirName + java.io.File.separatorChar + 
                                                fileNames[i].getName();
            if ( fileNames[i].isDirectory() )
            {
               File dirToCreate = new File(tempString);
               dirToCreate.mkdirs();
               result = copyCourse( fileNames[i].getAbsolutePath(), tempString );
            }
            else
            {
               fi = new FileInputStream(fileNames[i]);
               fo = new FileOutputStream(tempString);
               in = new BufferedInputStream( fi );
               out = new BufferedOutputStream( fo );
               int c;
               while ((c = in.read()) != -1) 
               {
                  out.write(c);
               }

               in.close();
               fi.close();
               out.close();
               fo.close();
            }
         }
      }
      catch ( IOException ioe )
      {
         result = false;
         if ( DebugIndicator.ON )
         {
            ioe.printStackTrace(); 
         }
      }
      return result;
         
   }

   /**
    *
    * This method will return the course ID.
    * 
    * @return A string containing the course ID.       
    */
   public String getCourseID()
   {
      return this.mCourseID;
   }

   /**
    * This method gets a list of the &lt;organization&gt; elements in the manifest.
    * 
    * @return A vector containing the &ltorganization&gt nodes.
    */
   protected Vector getOrganizationList()
   {
      return ManifestHandler.getOrganizationNodes(mManifest, false);
   }

   /**
    * This method gets the sequencingCollection node from the DOM tree.
    * 
    * @return The sequencingCollection node from the DOM tree.
    */
   public Node getSeqCollection()
   {
      return DOMTreeUtility.getNode( mManifest, "sequencingCollection" );
   }
   
   /**
    * This method sets mCourseTitle to the value passed in with courseTitle.
    * 
    * @param iCourseTitle The title of the course.
    */
   public void setCourseName( String iCourseTitle )
   {
      this.mCourseTitle = iCourseTitle;
   }

   /**
    * This method sets the web path.
    * 
    * @param iWebPath The web path.
    */
   public void setWebPath( String iWebPath )
   {
      this.mWebPath = iWebPath;
      mLogger.info("***MWEBPATH IS " + mWebPath + "***");
   }
   
   /**
    * This method takes the relevant information from the
    * populated parser structure and writes it to a related 
    * database.  This is done so that the JSP coding is 
    * more straight forward.
    * 
    * @return boolean - A boolean indicating the status of the database update
    */   
   protected boolean updateDB() 
   {         
      mLogger.entering("---LMSManifestHandler", "updateDB()  *********");

      boolean result = true;
      SeqActivityTree mySeqActivityTree;
    
      try
      {       
         // Set up the database connection information.
                 
         Connection conn = LMSDatabaseHandler.getConnection();
         // Prepare statements
         PreparedStatement stmtSelectCID;
         PreparedStatement stmtUpdateCID;
         PreparedStatement stmtInsertCourse;
         PreparedStatement stmtInsertItem;
         PreparedStatement stmtUpdateCourse;

         String sqlSelectCourseID
         = "SELECT * FROM ApplicationData WHERE dataName = ?";

         String sqlUpdateCourseID
         = "UPDATE ApplicationData SET numberValue = ? WHERE dataName = ?";
         
         String sqlUpdateCourse
         = "UPDATE CourseInfo SET Start = ?, TOC = ? WHERE CourseID = ?";
                     
         String sqlInsertCourse
         = "INSERT INTO CourseInfo (CourseID, CourseTitle, Active, " + 
            "ImportDateTime) VALUES(?, ?, ?, ?)";

         String sqlInsertItem
            = "INSERT INTO ItemInfo (CourseID, OrganizationIdentifier, " +
              "ItemIdentifier, Type, Title, " + "Launch, ParameterString, " + 
              "DataFromLMS, TimeLimitAction, " + 
              "MinNormalizedMeasure, AttemptAbsoluteDurationLimit, " +
              "CompletionThreshold, Next, Previous, Exit, ExitAll, Abandon, " +
              "ResourceIdentifier, Suspend) " +
              "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

         stmtSelectCID = conn.prepareStatement( sqlSelectCourseID );
         stmtUpdateCID = conn.prepareStatement( sqlUpdateCourseID );
         stmtInsertCourse = conn.prepareStatement( sqlInsertCourse );
         stmtInsertItem = conn.prepareStatement( sqlInsertItem );
         stmtUpdateCourse = conn.prepareStatement( sqlUpdateCourse );
         
         ResultSet selectCourseIDRS = null;

         // loop through all organizations performing the database updates and
         // sequencing actions for each one.      
         for ( int j=0; j < mOrganizationList.size(); j++ ) 
         {           
            Node tempOrganization = (Node)mOrganizationList.elementAt(j);
            String tempOrgIdentifier = DOMTreeUtility.getAttributeValue
               (tempOrganization, "identifier" );
            Node tempOrgTitleNode = DOMTreeUtility.getNode
               ( tempOrganization, "title" );
            String tempOrgTitle = DOMTreeUtility.getNodeValue
               ( tempOrgTitleNode );

            // Get the next course id from the application data table
            synchronized( stmtSelectCID )
            {
               stmtSelectCID.setString(1, "nextCourseID" );
               selectCourseIDRS = stmtSelectCID.executeQuery();  
            }


            if ( selectCourseIDRS.next() )
            {
               int idvalue = selectCourseIDRS.getInt( "numberValue" );
               mCourseID = "Course-" + idvalue;

               idvalue++;

               // increase the course id by one
               synchronized( stmtUpdateCID )
               {
                  stmtUpdateCID.setInt( 1, idvalue );
                  stmtUpdateCID.setString( 2, "nextCourseID" );
                  stmtUpdateCID.executeUpdate();
               }
               
               SimpleDateFormat date = new SimpleDateFormat ("yyyy-MM-dd kk:mm:ss");
               
               // Insert the course into the course Info table
               synchronized( stmtInsertCourse )
               {
                  stmtInsertCourse.setString( 1, mCourseID );
                  stmtInsertCourse.setString( 2, tempOrgTitle );
                  stmtInsertCourse.setBoolean(3, true);
                   
                  // Encode the timestamp to allow foreign characters
                  String temp = date.format(new Date());
                  temp = new String(temp.getBytes("UTF-16"), "UTF-16");                  
                  stmtInsertCourse.setString( 4, temp);
                  
                  stmtInsertCourse.executeUpdate();
               }

               // Create a temporary LaunchData object
               LaunchData ld = new LaunchData();
                
               // Loop through each item in the course adding it to the database
               for ( int i = 0; i < mLaunchDataList.size(); i++ )
               {  
                  ld = (LaunchData)mLaunchDataList.elementAt(i);

                  // If the organization identifier of the current launch data
                  // equals the identifier of the current entry of the
                  // organization list, perform the database updates.
                  if (ld.getOrganizationIdentifier().equals(tempOrgIdentifier))
                  {           
                     // Decode the URL before inserting into the database
                     decodeHandler decoder = new decodeHandler( ld.getLocation(), "UTF-8");
                     decoder.decodeName();
                     String alteredLocation = new String();
                
                     //If it's blank or it's external, don't concatenate to the 
                     //local Web root.
                     if ((ld.getLocation().equals(""))||
                         (ld.getLocation().startsWith("http://"))||
                         (ld.getLocation().startsWith("https://")))
                     {
                        alteredLocation = decoder.getDecodedFileName(); 
                        if ( !(ld.getParameters().equals("")) && 
                              !(ld.getParameters() == null ) )
                        {
                           alteredLocation = addParameters(alteredLocation, 
                                                        ld.getParameters());
                        }
                           
                     }
                     else
                     { 
                        // Create the altered location (with decoded url)
                        alteredLocation = "/adl/CourseImports/" + mCourseID +"/" 
                                          + decoder.getDecodedFileName();
                    
                        if ( !(ld.getParameters().equals("")) && 
                              !(ld.getParameters() == null ) )
                        {
                           alteredLocation = addParameters(alteredLocation, 
                                                        ld.getParameters());
                        }

                     }
                     
                     // Insert into the database
                     synchronized( stmtInsertItem )
                     {
                        stmtInsertItem.setString(1, mCourseID);
                        stmtInsertItem.setString(2, ld.
                                                 getOrganizationIdentifier());
                        stmtInsertItem.setString(3, ld.getItemIdentifier());
                        
                        stmtInsertItem.setString(4, ld.getSCORMType());
                        stmtInsertItem.setString(5, ld.getItemTitle());
                        stmtInsertItem.setString(6, alteredLocation);
                        stmtInsertItem.setString(7, ld.getParameters());                        

                        stmtInsertItem.setString(8, ld.getDataFromLMS());
                        stmtInsertItem.setString(9, ld.getTimeLimitAction());
                        stmtInsertItem.setString(10, 
                                                 ld.getMinNormalizedMeasure());
                        stmtInsertItem.setString(11, ld.
                                             getAttemptAbsoluteDurationLimit());
                        stmtInsertItem.setString(12, ld.getCompletionThreshold()
                                                 );
                        stmtInsertItem.setBoolean(13, ld.getContinue() );
                        stmtInsertItem.setBoolean(14, ld.getPrevious() );
                        stmtInsertItem.setBoolean(15, ld.getExit() );
                        stmtInsertItem.setBoolean(16, ld.getExitAll() );
                        stmtInsertItem.setBoolean(17, ld.getAbandon() );
                        stmtInsertItem.setString(18, 
                                                 ld.getResourceIdentifier() );
                        stmtInsertItem.setBoolean(19, ld.getSuspendAll() );
                        stmtInsertItem.executeUpdate();
                     }    
                  }
               } 
            }

            //Copy course files from the temp directory and serialize
            String copyInDirName = mWebPath + "PackageImport";
            String copyOutDirName = mWebPath + "CourseImports" + 
                                         java.io.File.separatorChar + mCourseID;

            mFileCopyResult = copyCourse( copyInDirName, copyOutDirName );
            
            // If files failed to copy, delete the course from the database
            if ( !mFileCopyResult )
            {               
               PreparedStatement stmtDeleteCourse1;
               String sqlDeleteCourse1 = "DELETE FROM CourseInfo WHERE CourseID = ?";
               PreparedStatement stmtDeleteCourse2;
               String sqlDeleteCourse2 = "DELETE FROM ItemInfo WHERE CourseID = ?";
               conn = LMSDatabaseHandler.getConnection();
               stmtDeleteCourse1 = conn.prepareStatement(sqlDeleteCourse1);
               stmtDeleteCourse2 = conn.prepareStatement(sqlDeleteCourse2);
               synchronized (stmtDeleteCourse1)
               {                  
                  stmtDeleteCourse1.setString(1, mCourseID);
                  stmtDeleteCourse1.executeUpdate();
               }
               stmtDeleteCourse1.close();
               synchronized (stmtDeleteCourse2)
               {                  
                  stmtDeleteCourse2.setString(1, mCourseID);
                  stmtDeleteCourse2.executeUpdate();
               }
               stmtDeleteCourse2.close();
            }
            
            //create a SeqActivityTree and serialize it
            mySeqActivityTree = new SeqActivityTree();

            String tempObjectivesGlobalToSystem = DOMTreeUtility.
                getAttributeValue( tempOrganization,"objectivesGlobalToSystem" );
            
            String tempDataScopeVal = DOMTreeUtility.getAttributeValue(tempOrganization, "sharedDataGlobalToSystem");
            
            // include sequencing collection as a parameter as well as 
            // the organization node.
            mySeqActivityTree = ADLSeqUtilities.buildActivityTree(tempOrganization, 
                                                                  getSeqCollection());

            if( tempObjectivesGlobalToSystem.equals("false") )
            {
               mySeqActivityTree.setScopeID(mCourseID);
            }

            mySeqActivityTree.setCourseID(mCourseID);
            
            // this assumes we're going to get true/false or ""
            // and that if the value doesn't exist, the default 
            // value will be true
            mySeqActivityTree.setDataScopedForAllAttempts(tempDataScopeVal == null || !tempDataScopeVal.equals("false"));
            
            String serializeFileName = mWebPath + "CourseImports" + File.separator + mCourseID 
                                                 + File.separator + "serialize.obj";
            java.io.File serializeFile = new java.io.File(serializeFileName);
            
            FileOutputStream outFile = new FileOutputStream(serializeFile);
            ObjectOutputStream s = new ObjectOutputStream(outFile);
            s.writeObject(mySeqActivityTree);
            s.flush(); 
            s.close();
            outFile.close();
            
            /////////////////////////////////////////////////////////
            ADLSequencer theSequencer = new ADLSequencer();
            ADLLaunch launch = new ADLLaunch();

            theSequencer.setActivityTree(mySeqActivityTree);
            
            launch = theSequencer.navigate(SeqNavRequests.NAV_NONE);
            
            // If the TOC is empty and the course cannot be started, 
            // there is nothing the course can start on
            // Only do the check if we want to validate
            if ( mOnlineValidation && launch.mNavState.mTOC == null && launch.mNavState.mStart == false )
            {
               mLaunchableCourse = false;
               
               PreparedStatement stmtDeleteCourse1;
               String sqlDeleteCourse1 = "DELETE FROM CourseInfo WHERE CourseID = ?";
               PreparedStatement stmtDeleteCourse2;
               String sqlDeleteCourse2 = "DELETE FROM ItemInfo WHERE CourseID = ?";
               stmtDeleteCourse1 = conn.prepareStatement(sqlDeleteCourse1);
               stmtDeleteCourse2 = conn.prepareStatement(sqlDeleteCourse2);
               synchronized (stmtDeleteCourse1)
               {                  
                  stmtDeleteCourse1.setString(1, mCourseID);
                  stmtDeleteCourse1.executeUpdate();
               }
               stmtDeleteCourse1.close();
               synchronized (stmtDeleteCourse2)
               {                  
                  stmtDeleteCourse2.setString(1, mCourseID);
                  stmtDeleteCourse2.executeUpdate();
               }
               stmtDeleteCourse2.close();
            }
           
            synchronized( stmtUpdateCourse )
            {
               stmtUpdateCourse.setBoolean(1, launch.mNavState.mStart);
               stmtUpdateCourse.setBoolean(2, (launch.mNavState.mTOC != null));
               stmtUpdateCourse.setString(3, mCourseID);
               stmtUpdateCourse.executeUpdate();
            }
         }

         // Close the statements
         stmtSelectCID.close();
         stmtUpdateCID.close();
         stmtInsertCourse.close();
         stmtInsertItem.close();
         stmtUpdateCourse.close();

         conn.close();
        
      }  
      catch ( SQLException se )
      {
         result = false;
         if ( DebugIndicator.ON )
         {
            System.out.println(se.getSQLState());
            System.out.println("error code: " + se.getErrorCode());
            se.printStackTrace();
         }
      }
      catch ( FileNotFoundException fne )
      {
         result = false;
         if ( DebugIndicator.ON )
         {
            fne.printStackTrace(); 
         }
      }
      catch ( IOException ioe )
      {
         result = false;
         if ( DebugIndicator.ON )
         {
            ioe.printStackTrace(); 
         }         
      }
      return result;
   }


   /**
    * This method adds parameters to a URL using the following algorithm 
    * from the SCORM CAM Version 1.3:
    * While first char of parameters is in "?&"
    *    Clear first char of parameters
    * If first char of parameters is "#"
    *    If URL contains "#" or "?"
    *        Discard parameters
    *        Done processing URL
    * If URL contains "?"
    *    Append "&" to the URL
    * Else
    *    Append "?" to the URL
    * Append parameters to URL
    *
    * 
    * @param iURL  URL of content
    * 
    * @param iParameters  Parameters to be appended
    * 
    * @return URL with added parameters
    */                                                      
   public String addParameters(String iURL, String iParameters)
   {
       if ( (iURL.length() == 0) || (iParameters.length() == 0) )
       {
          return iURL;
       }
        while ( (iParameters.charAt(0) == '?') || 
                                    (iParameters.charAt(0) == '&') )
        {
           iParameters = iParameters.substring(1);
        }
        if ( iParameters.charAt(0) == '#' )
        {
             if ( (iURL.indexOf('#') != -1) || (iURL.indexOf('?') != -1) )
             {
                return iURL;
             }
        }
        if ( iURL.indexOf('?') != -1 )
        {   
           iURL = iURL + '&';
        }
        else
        {
           iURL = iURL + '?';
        }
        iURL = iURL + iParameters;

        return iURL;
   }
  
   /**
    * This method analyzes validation results and determines the overall validation
    * status
    * 
    * @param iResCollection contains the validation results to be analyzed
    * @return a boolean indicating the overall validation status
    */
   private boolean determineValidationStatus( ResultCollection iResCollection, boolean iOnline )
   {
      List resList = iResCollection.getPackageResultsCollection();
      Iterator resIter = resList.iterator();
      boolean validationStatus = true;
      boolean passed = true;
      
      // Check submanifest and external files
      Result subRes = iResCollection.getPackageResult(ValidatorCheckerNames.SUBMANIFEST);
      Result extRes = iResCollection.getPackageResult(ValidatorCheckerNames.RES_HREF);
      
      boolean stopImport = false;
      if ( subRes != null && extRes != null )
      {
         stopImport = subRes.isTestStopped() || extRes.isTestStopped();
      }
      
      // If the submanifest or external files are used, we do not want to import
      validationStatus = !stopImport;
      
      while ( resIter.hasNext() && !stopImport )
      {
         Result tempRes = (Result)resIter.next();
         if ( !iOnline && (tempRes.getPackageCheckerName().equals(ValidatorCheckerNames.SCHEMA_VAL) ||
              tempRes.getPackageCheckerName().equals(ValidatorCheckerNames.RES_HREF) ))
         {
            passed = true;
         }
         else
         {
            passed = tempRes.isPackageCheckerPassed();
         }
         
         if ( !passed )
         {
            validationStatus = false;
         }
      }
      return validationStatus;
   }
}
