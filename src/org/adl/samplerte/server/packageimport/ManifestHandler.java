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
package org.adl.samplerte.server.packageimport;

// native java imports
import java.util.Vector;
import java.util.logging.Logger;

// xerces imports
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

// adl imports
import org.adl.samplerte.server.packageimport.parsers.dom.DOMTreeUtility;
import org.adl.util.decode.decodeHandler;


/**
 * <strong>Filename: </strong><br>ManifestHandler.java<br><br>
 *
 * <strong>Description: </strong>This method tracks, stores and retrieves  the
 * Launch Data information of SCOs and the Metadata information, all of which is
 * found or referenced from within the content package test subject.
 *
 * @author ADL Technical Team
 */
public class ManifestHandler
{
   /**
    * This attribute serves as the Logger object used for debug logging.
    */
   private Logger mLogger;

   /**
    * This attribute describes whether or not SCO launch data has been
    * tracked.
    */
   private boolean mLaunchDataTracked;

   /**
    * This attribute serves as the storage list of the tracked SCO launch data.
    * This list uses the default organization and does not conform with given
    * sequencing rules.  This list can be used for default behavior and
    * testing purposes.
    */
   private Vector mLaunchDataList;

   /**
    * used in all areas where an empty string was checked for, or a string was set to ""
    */
   private String mEMPTY_STRING = "";

   /**
    * Default Constructor.  Sets the attributes to their initial values.
    */
   public ManifestHandler()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.samplerte");
      mLaunchDataTracked = false;      
      mLaunchDataList = new Vector();
   }

   /**
    * This method initiates the retrieval of the SCO launch data
    * information, if this information exists in the content package test
    * subject.
    *
    * @param iRootNode root node manipulated for retrieval of launch data.
    * 
    * @param iDefaultOrganizationOnly boolean describing the scope of the
    *        organization that should be traversed for SCO launch data. Specific 
    *        to SRTE uses - will no longer be needed in future development.
    * 
    * @param iRemoveAssets boolean describing whether or not to remove assets
    *        from the LaunchData list.  The SRTE needs this to be false in
    *        in order to import assets as well.
    *
    * @return Vector containing the launch data information for SCOs.
    */
   public Vector getLaunchData( Node iRootNode,
                                boolean iDefaultOrganizationOnly,
                                boolean iRemoveAssets )
   {
      mLogger.entering("ManifestHandler", "getLaunchData(iRootNode)");
      if ( ! mLaunchDataTracked )
      {
         setLaunchData( iRootNode, iDefaultOrganizationOnly, iRemoveAssets );
      }
      mLogger.exiting("ManifestHandler", "getLaunchData");
      return mLaunchDataList;
   }


   /**
    * This method performs the actual retrieval of the SCO launch data
    * information, if this information exists in the content package test
    * subject.  This method walks through the test subject dom, storing all the
    * SCO launch data information to the LaunchData data structure.
    *
    * @param iRootNode root node of test subject dom.
    * 
    * @param iDefaultOrganizationOnly boolean describing the scope of the
    *        organization that should be traversed for SCO launch data. Specific 
    *        to SRTE uses - will no longer be needed in future development.
    * 
    * @param iRemoveAssets boolean describing whether or not to remove assets
    *        from the LaunchData list.  The SRTE needs this to be false in
    *        in order to get LaunchData for assets as well.
    * 
    */
   private void setLaunchData( Node iRootNode,
                               boolean iDefaultOrganizationOnly,
                               boolean iRemoveAssets )
   {
      mLogger.entering("ManifestHandler", "SetLaunchData(iRootNode)");
      Vector organizationNodes = getOrganizationNodes( iRootNode,
                                                     iDefaultOrganizationOnly );

      int size = organizationNodes.size();

      // populate the Launch Data for the Organization level
      for ( int i = 0; i < size; i++ )
      {
         Node currentOrganization = (Node)organizationNodes.elementAt(i);

         String orgIdentifier = DOMTreeUtility.getAttributeValue( currentOrganization,
                                                           "identifier" );

         addItemInfo( currentOrganization, orgIdentifier );
      }

      Node xmlBaseNode = null;
      String manifestXMLBase = mEMPTY_STRING;
      String resourcesXMLBase = mEMPTY_STRING;

      // calculate the <manifest>s xml:base
      NamedNodeMap attributes = iRootNode.getAttributes();
      xmlBaseNode = attributes.getNamedItem("xml:base");

      if ( xmlBaseNode != null )
      {
         manifestXMLBase = xmlBaseNode.getNodeValue();
      }

      // calculate the <resources> xml:base
      Node resources = DOMTreeUtility.getNode( iRootNode, "resources" );
      attributes = resources.getAttributes();
      xmlBaseNode = attributes.getNamedItem("xml:base");

      if ( xmlBaseNode != null )
      {
         resourcesXMLBase = xmlBaseNode.getNodeValue();
      }

      // populate all Launch Data with the xml:base values
      size = mLaunchDataList.size();
      LaunchData currentLaunchData = null;

      for ( int j = 0; j < size; j++ )
      {
         currentLaunchData = (LaunchData)mLaunchDataList.elementAt(j);

         // update the xml:base data
         currentLaunchData.setManifestXMLBase( manifestXMLBase );
         currentLaunchData.setResourcesXMLBase( resourcesXMLBase );

         // replace the old LaunchData Object with the updated one
         mLaunchDataList.removeElementAt(j);
         mLaunchDataList.insertElementAt( currentLaunchData, j );
      }
      // populate the Launch Data for the Resource level
      addResourceInfo( iRootNode, iRemoveAssets );

      removeDuplicateLaunchData();

      mLaunchDataTracked = true;
      mLogger.exiting("ManifestHandler", "SetLaunchData(iRootNode)");
   }
 
   /**
    * This method removes the duplicate LaunchData elements that are stored in
    * the list during tracking.  This removal is based on the Resource
    * Identifier, XML Base directories, Location and Parameters.
    */
   private void removeDuplicateLaunchData()
   {
      mLogger.entering("ManifestHandler", "removeDuplicateLauchData()");
      int size = mLaunchDataList.size();
      LaunchData ldA;
      LaunchData ldB;
      String ldAid;
      String ldBid;
      String ldAll;
      String ldBll;

      for ( int i = 0; i < size; i++ )
      {
         ldA = (LaunchData)mLaunchDataList.elementAt(i);
         ldAid = ldA.getResourceIdentifier();

         for ( int j = i + 1; j < size; j++ )
         {
            ldB = (LaunchData)mLaunchDataList.elementAt(j);
            ldBid = ldB.getResourceIdentifier();

            if ( ldBid.equals(ldAid) )
            {
               ldAll = ldA.getItemIdentifier();
               ldBll = ldB.getItemIdentifier();

               if ( ldBll.equals(ldAll) )
               {
                  mLaunchDataList.removeElementAt(j);
                  j--;
                  size = mLaunchDataList.size();
               }
            }
         }
      }
      mLogger.exiting("ManifestHandler", "removeDuplicateLauchData()");
   }

   /**
    * This method retrieves all the organization nodes from the content package
    * manifest dom.  This method serves as a helper for retrieving SCO
    * launch data.
    *
    * @param iDefaultOrganizationOnly boolean describing the scope of the
    *        organization that should be traversed for SCO launch data. Specific 
    *        to SRTE uses - will no longer be needed in future development.
    * 
    * @param iRootNode root node of test subject dom.
    * 
    * @return Vector Containing a list of organization nodes.
    */
   public static Vector getOrganizationNodes( Node iRootNode,
                                              boolean iDefaultOrganizationOnly )
   {
      Vector result = new Vector();

      if ( iDefaultOrganizationOnly )
      {
         result.add( getDefaultOrganizationNode( iRootNode ) );
      }
      else
      {
         // get the list of organization nodes
         Node organizationsNode = DOMTreeUtility.getNode( iRootNode,
                                                          "organizations" );
         NodeList children = organizationsNode.getChildNodes();

         if ( children != null )
         {
            int numChildren = children.getLength();

            for ( int i = 0; i < numChildren; i++ )
            {
               Node currentChild = children.item(i);
               String currentChildName = currentChild.getLocalName();

               if ( currentChildName.equals( "organization" ) )
               {
                  // add the organization node to the resulting list
                  result.add( currentChild );
               }
            }
         }
      }
      return result;
   }

   /**
    * This method returns the default organization node that is flagged
    * by the default attribute. This method serves as a helper method.
    *
    * @param iRootNode root node of test subject dom.
    * 
    * @return Node default organization
    */
   public static Node getDefaultOrganizationNode( Node iRootNode )
   {
      Node result = null;

      // find the value of the "default" attribute of the <organizations> node
      Node organizationsNode = DOMTreeUtility.getNode( iRootNode,
                                                       "organizations" );
      NamedNodeMap attrList = organizationsNode.getAttributes();
      
      // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
      String defaultIDValue = 
         decodeHandler.processWhitespace((attrList.getNamedItem("default")).getNodeValue());

      // traverse the <organization> nodes and find the matching default ID
      NodeList children = organizationsNode.getChildNodes();

      if ( children != null )
      {
         int numChildren = children.getLength();

         for ( int i = 0; i < numChildren; i++ )
         {
            Node currentChild = children.item(i);
            String currentChildName = currentChild.getLocalName();

            if ( currentChildName.equals( "organization" ) )
            {
               // find the value of the "identifier" attribute of the
               // <organization> node
               NamedNodeMap orgAttrList = currentChild.getAttributes();
               
               // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
               String idValue =
                  decodeHandler.processWhitespace((orgAttrList.getNamedItem("identifier")).getNodeValue());

               if ( idValue.equals( defaultIDValue ) )
               {
                  result = currentChild;
                  break;
               }
            }
         }
      }

      return result;
   }

   /**
    * This method retrieves the minNormalizedMeasure element from the parent
    * sequencing element.
    *
    * @param iNode to be manipulated for minnormalizedmeasure value.
    *
    * @return String containing the minNormalizedMeasure value.
    */
   private String getMinNormalizedMeasure( Node iNode )
   {
      mLogger.entering("ManifestHandler", "getMinNormalizedMeasure");
      String minNormalizedMeasure = mEMPTY_STRING;
      String nodeName = iNode.getLocalName();

      if ( nodeName.equals("item") )
      {
         Node sequencingNode = DOMTreeUtility.getNode( iNode, "sequencing" );
         if ( sequencingNode != null )
         {
            Node objectivesNode = DOMTreeUtility.getNode( sequencingNode,
                                                            "objectives" );
            if ( objectivesNode != null )
            {
               Node primaryObjectiveNode = DOMTreeUtility.
                                            getNode( objectivesNode,
                                                     "primaryObjective" );
               if ( primaryObjectiveNode != null)
               {
                  String satisfiedByMeasureValue = DOMTreeUtility.getAttributeValue(
                                            primaryObjectiveNode,
                                            "satisfiedByMeasure" );
                  if( satisfiedByMeasureValue.equals("true") )
                  {
                     Node minNormalizedMeasureNode = DOMTreeUtility.getNode(
                                                    primaryObjectiveNode,
                                                    "minNormalizedMeasure" );
                     if( minNormalizedMeasureNode != null )
                     {
                       minNormalizedMeasure = DOMTreeUtility.getNodeValue(
                                                     minNormalizedMeasureNode );
                       if( minNormalizedMeasure.trim().equals(mEMPTY_STRING) )
                       {
                          minNormalizedMeasure = "1.0";
                       }
                     }
                     else
                     {
                       minNormalizedMeasure = "1.0";
                     }
                  }
               }
            }
         }
      }
      mLogger.exiting("ManifestHandler", "getMinNormalizedMeasure");
      return minNormalizedMeasure;
   }

   /**
    * This method retrieves the attemptAbsoluteDurationLimit element from the
    * parent sequencing element.
    *
    * @param iNode  node to be manipulated for attemptAbsoluteDurationLimit
    * value.
    *
    * @return String containing the attemptAbsoluteDurationLimit value.
    */
   private String getAttemptAbsoluteDurationLimit( Node iNode )
   {
      mLogger.entering("ManifestHandler", "getAttempteAbsoluteDurationLimit()");
     
      String attemptAbsoluteDurationLimit = mEMPTY_STRING;

      String nodeName = iNode.getLocalName();

      if ( nodeName.equals("item") )
      {
         Node sequencingNode = DOMTreeUtility.getNode( iNode, "sequencing" );
         if ( sequencingNode != null )
         {
            Node limitConditionsNode = DOMTreeUtility.getNode( sequencingNode,
                                                           "limitConditions" );
            if ( limitConditionsNode != null )
            {
               attemptAbsoluteDurationLimit = DOMTreeUtility.getAttributeValue(
                                              limitConditionsNode,
                                            "attemptAbsoluteDurationLimit" );
            }
         }
      }
      mLogger.exiting("ManifestHandler", "getAttempteAbsoluteDurationLimit()");
      return attemptAbsoluteDurationLimit;
   }

   /**
    * This method retrieves the information described by the &lt;item&gt;
    * element and saves it for SCO launch data information.  This method
    * traverses the &lt;item&gt;s of the &lt;organization&gt; recursively and
    * retrieves the identifiers, referenced identifier references and
    * corresponding parameters from the &lt;resources&gt; element.
    *
    * @param iNode The organization node.
    * 
    * @param iOrgID The ID of the organization.
    */
   private void addItemInfo( Node iNode, String iOrgID )
   {
      mLogger.entering("ManifestHandler", "addItemInfo()");
      if ( iNode == null )
      {
         return;
      }

      int type = iNode.getNodeType();
      String orgID = iOrgID;

      switch ( type )
      {
         // document node
         // this is a fail safe case to handle an error where a document node
         // is passed
         case Node.DOCUMENT_NODE:
         {
            Node rootNode = ((Document)iNode).getDocumentElement();

            addItemInfo( rootNode, orgID );

            break;
         }

         // element node
         case Node.ELEMENT_NODE:
         {
            String nodeName = iNode.getLocalName();

            // get the needed values of the attributes
            if ( nodeName.equals("item") )
            {
               String orgIdentifier   = mEMPTY_STRING;
               String identifier      = mEMPTY_STRING;
               String identifierref   = mEMPTY_STRING;
               String parameters      = mEMPTY_STRING;
               String title           = mEMPTY_STRING;
               String dataFromLMS     = mEMPTY_STRING;
               String timeLimitAction = mEMPTY_STRING;
               String completionThreshold = mEMPTY_STRING;
               String objectiveslist = mEMPTY_STRING;
               boolean previous       = false;
               boolean shouldContinue = false;
               boolean exit           = false;
               boolean exitAll        = false;
               boolean abandon        = false;
               boolean suspendAll     = false;

               //Assign orgIdentifier the value of the parameter iOrgID
               orgIdentifier = iOrgID;

               // get the value of the following attributes:
               // - identifier
               // - identifierref
               // - parameters
               //
               // leave the value at "" is the attribute does not exist
               NamedNodeMap attrList = iNode.getAttributes();
               int numAttr = attrList.getLength();
               Attr currentAttrNode;
               String currentNodeName;

               // loop through the attributes and get their values assuming that
               // the multiplicity of each attribute is 1 and only 1.
               for ( int i = 0; i < numAttr; i++ )
               {
                  currentAttrNode = (Attr)attrList.item(i);
                  currentNodeName = currentAttrNode.getLocalName();

                  // store the value of the attribute
                  if ( currentNodeName.equalsIgnoreCase("identifier") )
                  {
                     // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
                     identifier = decodeHandler.processWhitespace(currentAttrNode.getValue());
                  }
                  else if ( currentNodeName.equalsIgnoreCase("identifierref") )
                  {
                     identifierref = currentAttrNode.getValue();
                  }
                  else if ( currentNodeName.equalsIgnoreCase("parameters") )
                  {
                     parameters = currentAttrNode.getValue();
                  }

               }

               // get the value of the title element
               // assume that there is 1 and only 1 child named title
               title = DOMTreeUtility.getNodeValue(
                                      DOMTreeUtility.
                                      getNode( iNode, "title" ) );

               // get the value of the datafromlms element
               dataFromLMS = DOMTreeUtility.getNodeValue(
                                            DOMTreeUtility.
                                            getNode( iNode, "dataFromLMS" ) );

               // get the value of the timelimitaction element
               timeLimitAction = DOMTreeUtility.getNodeValue(
                                                DOMTreeUtility.
                                                getNode( iNode,
                                                        "timeLimitAction" ) );

               // get the value of the completionThreshold element
               completionThreshold = getCompletionThreshold(
                                                DOMTreeUtility.
                                                getNode( iNode,
                                                      "completionThreshold" ) );


          //Gets the sequencing objectives list for this item
          objectiveslist = getObjectivesList(DOMTreeUtility.getNode(iNode, "sequencing"));

               //get the hideRTSUI elements and set the previous, continue,
               //exit, exitAll, abandon and suspendAll variables accordingly.
               Node presentationNode =
                  DOMTreeUtility.getNode( iNode, "presentation" );
               if ( presentationNode != null )
               {
                  Node navInterfaceNode =
                     DOMTreeUtility.getNode( presentationNode,
                                             "navigationInterface" );
                  if ( navInterfaceNode != null )
                  {
                     NodeList children = navInterfaceNode.getChildNodes();
                     if (children != null)
                     {
                        int numChildren = children.getLength();
                        for ( int i = 0; i < numChildren; i++ )
                        {
                           Node currentChild = children.item( i );
                           String currentChildName =
                               currentChild.getLocalName();
                           if ( currentChildName.equals("hideLMSUI") )
                           {
                              String currentChildValue =
                                  DOMTreeUtility.getNodeValue( currentChild );
                              if (  currentChildValue.equals("previous") )
                              {
                                 previous = true;
                              }
                              else if ( currentChildValue.equals( "continue" ) )
                              {
                                 shouldContinue = true;
                              }
                              else if ( currentChildValue.equals( "exit" ) )
                              {
                                 exit = true;
                              }
                              else if ( currentChildValue.equals( "exitAll" ) )
                              {
                                 exitAll = true;
                              }
                              else if ( currentChildValue.equals( "abandon" ) )
                              {
                                 abandon = true;
                              }
                              else if ( currentChildValue.
                                        equals( "suspendAll" ) )
                              {
                                 suspendAll = true;
                              }
                           }
                        }
                     }
                  }
               }

               // make sure this item actually points to a <resource>
               if ( ! identifierref.equals(mEMPTY_STRING) )
               {
                  // create an instance of the LaunchData data structure and
                  // add it to the LaunchDataList
                  LaunchData launchData = new LaunchData();

                  launchData.setOrganizationIdentifier( orgIdentifier );
                  launchData.setItemIdentifier( identifier );
                  launchData.setResourceIdentifier( identifierref );
                  launchData.setParameters( parameters );
                  launchData.setItemTitle( title );
                  launchData.setDataFromLMS( dataFromLMS );
                  launchData.setTimeLimitAction( timeLimitAction );
                  launchData.setCompletionThreshold( completionThreshold );
                  launchData.setPrevious( previous );
                  launchData.setContinue( shouldContinue );
                  launchData.setExit( exit );
                  launchData.setExitAll( exitAll );
                  launchData.setAbandon( abandon );
                  launchData.setSuspendAll( suspendAll );
                  launchData.setMinNormalizedMeasure(
                                             getMinNormalizedMeasure( iNode ) );
                  launchData.setAttemptAbsoluteDurationLimit(
                                     getAttemptAbsoluteDurationLimit( iNode ) );
                  launchData.setObjectivesList(objectiveslist);

                  mLaunchDataList.add( launchData );
               }
            }

            // get the child nodes and add their items info
            NodeList children = iNode.getChildNodes();

            if ( children != null )
            {
               int numChildren = children.getLength();
               Node currentChild;

               for ( int z = 0; z < numChildren; z++ )
               {
                  currentChild = children.item(z);
                  addItemInfo( currentChild, orgID );
               }
            }
         }
         // handle all other node types
         default:
         {
            break;
         }
      }
      mLogger.exiting("ManifestHandler", "addItemInfo()");
   }


   /**
    * This method gets all the sequencing objectives associated with the
    * current item.
    *
    * @param iNode root item node.
    * 
    * @return String - returns a string contaniing the objectives data 
    */
   private String getObjectivesList(Node iNode)
   {
      mLogger.entering("ManifestHandler", "getObjectivesList");
      int j, k;
      NamedNodeMap attributesList = null;
      String result = mEMPTY_STRING;

      // Gets to the objectives node, if one exists
      if (iNode != null)
      {
         Node objNode = DOMTreeUtility.getNode(iNode, "objectives");

         if (objNode != null)
         {
            //Gets the primary objective id
            Node primaryObjNode = DOMTreeUtility.getNode( objNode, "primaryObjective" );
            if (primaryObjNode != null)
            {
               attributesList = primaryObjNode.getAttributes();

               // iterate through the NamedNodeMap and get the attribute names and values
               for(j = 0; j < attributesList.getLength(); j++)
               {
                  //Finds the schema location and parses out values
                  if (attributesList.item(j).getLocalName().equalsIgnoreCase("objectiveID"))
                  {
                     // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
                     result = decodeHandler.processWhitespace(attributesList.item(j).getNodeValue());
                  }
               }
            }

            //Gets all objective ids
            Vector objNodes = DOMTreeUtility.getNodes(objNode, "objective");

            for(j = 0; j < objNodes.size(); j++)
            {
               Node currNode = (Node)objNodes.elementAt(j);
               attributesList = currNode.getAttributes();

               // iterate through the NamedNodeMap and get the attribute names and values
               for(k = 0; k < attributesList.getLength(); k++)
               {
                  //Finds the schema location and parses out values
                  if (attributesList.item(k).getLocalName().equalsIgnoreCase("objectiveID"))
                  {
                     // Process the whitespace of the value because it is of type ID, xs:ID, or IDREF
                     result = result + "," + decodeHandler.processWhitespace(attributesList.item(k).getNodeValue());
                  }
               }
            } // end looping over nodes
         } // end if objNode != null
      } // end if iNode != null

      //returns objective list, if it was found.
      mLogger.exiting("ManifestHandler", "getObjectivesList");
      return result;
   }

   /**
    * This method uses the information stored in the SCO Launch Data List
    * to get the associated Resource level data.
    *
    * @param iRootNode root node of the DOM.
    * 
    * @param iRemoveAssets boolean representing whether or not the assets should
    *                      be removed.  (The Sample RTE will never want to
    *                      remove the assets, where as the TestSuite will.)
    */
   private void addResourceInfo( Node iRootNode, boolean iRemoveAssets )
   {
      mLogger.entering("ManifestHandler", "addResourceInfo()");
      // get the <resources> node
      Node resourcesNode = DOMTreeUtility.getNode( iRootNode, "resources" );

      String scormType = mEMPTY_STRING;
      String location  = mEMPTY_STRING;
      String xmlBase   = mEMPTY_STRING;

      // launch data processing stuff
      int size = mLaunchDataList.size();
      LaunchData currentLaunchData;
      String resourceIdentifier = mEMPTY_STRING;
      Node matchingResourceNode = null;

      // here we are dealing with a content aggregation package
      for ( int i = 0; i < size; i++ )
      {
         scormType = mEMPTY_STRING;
         location  = mEMPTY_STRING;
         xmlBase   = mEMPTY_STRING;
         
         currentLaunchData = (LaunchData)mLaunchDataList.elementAt(i);
         resourceIdentifier = currentLaunchData.getResourceIdentifier();
         
         matchingResourceNode =
            getResourceNodeWithIdentifier( resourcesNode, resourceIdentifier );
         
         // Ensure resource node exists
         if ( matchingResourceNode != null )
         {
            // get the value of the following attributes:
            // - adlcp:scormtype
            // - href
            // - xml:base
            //
            // leave the value at "" is the attribute does not exist
            scormType = DOMTreeUtility.getAttributeValue( matchingResourceNode,
                                                          "scormType" );
            location  = DOMTreeUtility.getAttributeValue( matchingResourceNode,
                                                          "href" );
            xmlBase   = DOMTreeUtility.getAttributeValue( matchingResourceNode,
                                                          "base" );
         }

         // populate the current Launch Data with the resource level values
         currentLaunchData.setSCORMType( scormType );
         currentLaunchData.setLocation( location );
         currentLaunchData.setResourceXMLBase( xmlBase );

         try
         {
            mLaunchDataList.set( i, currentLaunchData );
         }
         catch ( ArrayIndexOutOfBoundsException aioobe )
         {
            System.out.println( "ArrayIndexOutOfBoundsException caught on " +
                                "Vector currentLaunchData.  Attempted index " +
                                "access is " + i + "size of Vector is " +
                                mLaunchDataList.size() );
         }
      }

      if ( size == 0 ) // then we are dealing with a resource package
      {
         // loop through resources to retieve all resource information
         // loop through the children of <resources>
         NodeList children = resourcesNode.getChildNodes();
         int childrenSize = children.getLength();

         if ( children != null )
         {
            for ( int z = 0; z < childrenSize; z++ )
            {
                Node currentNode = children.item( z );
                String currentNodeName = currentNode.getLocalName();

                if ( currentNodeName.equals("resource") )
                {
                   // create an instance of the LaunchData data structure and
                   // add it to the LaunchDataList
                   LaunchData launchData = new LaunchData();

                   // get the value adlcp:scormtype, href, base attribute
                   // leave the value at "" is the attribute does not exist
                   scormType = DOMTreeUtility.getAttributeValue( currentNode,
                                                                 "scormType" );

                   location  = DOMTreeUtility.getAttributeValue( currentNode,
                                                                 "href" );

                   xmlBase   = DOMTreeUtility.getAttributeValue( currentNode,
                                                                 "base" );

                   resourceIdentifier = DOMTreeUtility.getAttributeValue(
                                                                 currentNode,
                                                                 "identifier" );

                   // populate the  Launch Data with the resource level values
                   launchData.setSCORMType( scormType );
                   launchData.setLocation( location );
                   launchData.setResourceXMLBase( xmlBase );
                   launchData.setResourceIdentifier( resourceIdentifier );

                   mLaunchDataList.add( launchData );
                } // end if current node == resource
            } // end looping over children
         } // end if there are no children
      } // end if size == 0

      if( iRemoveAssets )
      {
         removeAssetsFromLaunchDataList();
      }
      mLogger.exiting("ManifestHandler", "addResourceInfo()");
   }

   /**
    * This method retrieves the resource node that matches the passed in
    * identifier value.  This method serves as a helper method.
    *
    * @param iResourcesNode Parent resources node of the resource elements.
    * 
    * @param iResourceIdentifier identifier value of the resource node being
    * retrieved.
    *
    * @return Node resource element node that matches the identifier value.
    */
   private Node getResourceNodeWithIdentifier( Node iResourcesNode,
                                               String iResourceIdentifier )
   {
      mLogger.entering("ManifestHandler", "getResourceNodeWithIdentifier()");
      Node result = null;

      // loop through the children of <resources>
      NodeList children = iResourcesNode.getChildNodes();

      if ( children != null )
      {
         int numChildren = children.getLength();
         Node currentChild = null;
         String currentChildName = mEMPTY_STRING;
         String currentResourceIdentifier = mEMPTY_STRING;

         for ( int i = 0; i < numChildren; i++ )
         {
            currentChild = children.item(i);
            currentChildName = currentChild.getLocalName();

            // locate the <resource> Nodes
            if ( currentChildName.equals( "resource" ) )
            {
               // get the identifier attribute of the current <resource> Node
               currentResourceIdentifier =
                  DOMTreeUtility.getAttributeValue( currentChild, "identifier" );

               // match the identifier attributes and get the missing data
               if ( currentResourceIdentifier.equals(iResourceIdentifier)  )
               {
                  result = currentChild;
                  break;
               }
            } // end if currentChildName == resource
         } // end looping over children
      } // end if there are no children

      mLogger.exiting("ManifestHandler", "getResourceNodeWithIdentifier()");
      return result;
   }

   /**
    * This method removes the asset information from the launch data list.
    * Assets are not launchable resources.
    */
   private void removeAssetsFromLaunchDataList()
   {
      mLogger.entering("ManifestHandler", "removeAssetsFromLaunchDataList");
      int size = mLaunchDataList.size();
      LaunchData currentLaunchData;

      for ( int i = 0; i < size; )
      {
         currentLaunchData = (LaunchData)mLaunchDataList.elementAt(i);
         String scormType = currentLaunchData.getSCORMType();

         if ( scormType.equals( "asset" ) )
         {
            mLaunchDataList.removeElementAt( i );
            size = mLaunchDataList.size();
         }
         else
         {
            i++;
         }
      }
      mLogger.exiting("ManifestHandler", "removeAssetsFromLaunchDataList");
   }  
   
   /**
    * Gets the completion threshold.
    * 
    * @param iNode The node.
    * 
    * @return The value.
    */
   private String getCompletionThreshold(Node iNode)
   {
      String thresh = "";
      
      if ( iNode != null )
      {
         thresh = DOMTreeUtility.getNodeValue(iNode);
         if ( thresh.equals(mEMPTY_STRING) )
         {
            thresh = DOMTreeUtility.getAttributeValue(iNode, "minProgressMeasure");
         }
         if ( thresh.equals(mEMPTY_STRING) && DOMTreeUtility.getAttributeValue(iNode, "completedByMeasure").equals("true") )
         {
            // see REQ_60.4
            thresh = "1.0";
         }
            
      }
      
      return thresh;
   }
}
