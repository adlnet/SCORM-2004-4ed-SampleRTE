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
package org.adl.samplerte.server.packageimport;

import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.adl.samplerte.server.packageimport.parsers.dom.DOMTreeUtility;
import org.adl.samplerte.util.UnZipHandler;

/**
 * <strong>Filename: </strong>LMSImportHandler.java<br><br>
 *
 * <strong>Description: </strong> The <code>LMSImportHandler</code> object
 * is returned upon request by the user via the use of the public method
 * available by the ADLSCORMValidator object.  The ADLValidatorOutcome object
 * serves as the central storage of the status of checks performed during the
 * validation activities, including the stored DOM. This object serves as an
 * efficient means for passing the outcome of the validation activites
 * throughout the utilizing system.<br><br>
 *
 * @author ADL Technical Team
 */

public class LMSImportHandler
{

   /**
    * The <code>Document</code> object is an electronic representation of the
    * XML produced if the parse was successful. A parse for wellformedness
    * creates a document object while the parse for validation against the
    * controlling documents creates a document object as well.  This attribute
    * houses the document object that is created last.  In no document object is
    * created, the value remains null.
    */
   private Document mDocument;

   /**
    * This attribute serves as the data structure used to store the Launch Data
    * information of SCOs and Metadata referenced within the content package. 
    */
   private ManifestHandler mManifestHandler;

   /**
    * Logger object used for debug logging.
    */
   private Logger mLogger;

   
   /**
    * Default constructor. Sets the attributes to their initial values.
    */
   public LMSImportHandler ( )
   {
      mManifestHandler = new ManifestHandler();
   }
   
   /**
    * Default constructor. Sets the attributes to their initial values.
    * @param iDoc The Test Subject Document
    */
   public LMSImportHandler( Document iDoc )
   {
      mLogger = Logger.getLogger("org.adl.util.debug.samplerte");
      mLogger.entering( "LMSImportHandler", "LMSImportHandler()" );

      mDocument = iDoc;
      mManifestHandler = new ManifestHandler();

      mLogger.exiting( "LMSImportHandler", "LMSImportHandler()" );
   }

   /**
    * This method returns the document created during a parse. A parse for
    * wellformedness creates a document object while the parse for validation
    * against the controlling documents creates a seperate document object.
    *
    * @return Document -  An electronic representation of the XML produced by
    * the parse.
    */
   public Document getDocument()
   {
      return mDocument;
   }

   /**
    * This method returns the root node of the document created during a parse.
    * A parse for wellformedness creates a document object while the parse for
    * validation against the controlling documents creates a seperate document
    * object.
    *
    * @return Node - the root node of the DOM stored in memory
    */
   public Node getRootNode()
   {
      return mDocument.getDocumentElement();
   }


   /**
    * This method will find the xml:base attribute of the node passed into it
    * and return it if it has one, if it doesn't, it will return an empty
    * string.  If the node does have an xml:base attribute, this method will
    * also set that attribute to an empty string after retrieving it's value.
    *
    * @param iNode - the node whose xml:base attribute value is needed.
    * @return Returns the value of the xml:base attribute of this node.
    */
   public String getXMLBaseValue( Node iNode)
   {
      mLogger.entering( "LMSImportHandler", "getXMLBaseValue()" );
      String result = new String();

      if ( iNode != null )
      {
         Attr baseAttr = DOMTreeUtility.getAttribute( iNode, "base" );
         if( baseAttr != null )
         {
            result = baseAttr.getValue();
            DOMTreeUtility.removeAttribute( iNode, "xml:base" );
         }
      }
      mLogger.exiting( "LMSImportHandler", "getXMLBaseValue()" );
      return result;
   }

   /**
    * This method will apply the value of any xml:base attributes of a root
    * manifest to any file elements in it's resource elements.
    *
    * @param iManifestNode - The root <code>&lt;manfiest$gt;</code> node of a 
    * manifest.
    */
   public void applyXMLBase( Node iManifestNode)
   {
      mLogger.entering( "LMSImportHandler", "applyXMLBase()" );
      String x = new String();
      String y = new String();
      Node currentNode;
      String currentNodeName = new String();
      String currentHrefValue = new String();
      Attr currentHrefAttr = null;
      Node currentFileNode;
      String fileNodeName = new String();
      String fileHrefValue = new String();
      //Get base of manifest node
      x = getXMLBaseValue(iManifestNode);

      //get base of resources node          
      Node resourcesNode = DOMTreeUtility.getNode( iManifestNode, "resources" );
      
      String resourcesBase = getXMLBaseValue(resourcesNode);
      if( (!x.equals( "" )) &&
          (!resourcesBase.equals( "" )) &&
          (!x.endsWith("/")) )
      {
         //x += File.separator;
         x += "/";
      }
      x += resourcesBase;

      NodeList resourceList = resourcesNode.getChildNodes();
      if( resourceList != null )
      {
         String resourceBase = new String();
         for (int i = 0; i < resourceList.getLength(); i++)
         {
            currentNode = resourceList.item(i);
            currentNodeName = currentNode.getLocalName();

            //Apply to resource level
            if ( currentNodeName.equals("resource") )
            {
               resourceBase = getXMLBaseValue(currentNode);

               if( (!x.equals( "" )) &&
                   (!resourceBase.equals( "" )) &&
                   (!x.endsWith("/")) )
               {
                  //y = x + File.separator + resourceBase;
                   y = x + "/" + resourceBase;
               }
               else
               {
                  y = x + resourceBase;
               }

               currentHrefAttr = DOMTreeUtility.
                  getAttribute( currentNode, "href" );
               if( currentHrefAttr != null )
               {
                  currentHrefValue = currentHrefAttr.getValue();
                  if( (!y.equals( "" )) &&
                      (!currentHrefValue.equals( "" )) &&
                      (!y.endsWith("/")) )
                  {
                     currentHrefAttr.setValue( y + "/" + currentHrefValue );
                  }
                  else
                  {
                     currentHrefAttr.setValue( y + currentHrefValue );
                  }
               }

               NodeList fileList = currentNode.getChildNodes();
               if( fileList != null )
               {
                  for( int j = 0; j < fileList.getLength(); j++ )
                  {
                     currentFileNode = fileList.item(j);
                     fileNodeName = currentFileNode.getLocalName();
                     if( fileNodeName.equals("file") )
                     {
                        Attr fileHrefAttr = DOMTreeUtility.
                                        getAttribute( currentFileNode, "href" );
                        fileHrefValue = fileHrefAttr.getValue();
                        if( (!y.equals( "" )) &&
                            (!fileHrefValue.equals( "" )) &&
                            (!y.endsWith("/")) )
                        {
                           fileHrefAttr.setValue( y + "/" +
                                                  fileHrefValue );
                        }
                        else
                        {
                            fileHrefAttr.setValue( y + fileHrefValue );
                        }
                     }
                  }
               }
            }
         }
      }
      mLogger.exiting( "LMSImportHandler", "applyXMLBase()" );
   }

   /**
    * Returns a vector of any sub-items of a given item Node.
    *
    * @param iItem - the item Node whose sub-items you wish to obtain.
    *
    * @return Returns a Vector of all sub-items of the given item.
    *
    */
   public Vector getItems( Node iItem )
   {
      mLogger.entering( "LMSImportHandler", "getItems()" );
      Vector result = new Vector();
      Vector itemList = new Vector();
      Node currentItem = null;
      if ( iItem != null )
      {
         itemList = DOMTreeUtility.getNodes( iItem, "item" );
      }
      result.addAll(itemList);
      for( int itemCount = 0; itemCount < itemList.size(); itemCount++ )
      {
         currentItem = (Node)itemList.elementAt(itemCount);
         result.addAll( getItems(currentItem) );
      }
      mLogger.exiting( "LMSImportHandler", "getItems()" );
      return result;
   }


   /**
    * Returns a Vector filled with all of the item Nodes in a manifest node.
    * This method is scoped only to one level of manifest, as such, it does not
    * return items in sub-manifests.
    *
    * @param iManifest The manifest node you wish to perform this operation on
    *
    * @return Returns Vector containing all of the item nodes in the
    * manifest.
    */
   public Vector getItemsInManifest( Node iManifest )
   {
      mLogger.entering( "LMSImportHandler", "getItemsInManifest()" );
      Node organizationsNode = null;
      Vector organizationList = new Vector();
      Vector itemList = new Vector();
      Vector resultList = new Vector();
      Node currentOrg = null;
      Node currentItem = null;
      organizationsNode = DOMTreeUtility.getNode( iManifest, "organizations" );
      if( organizationsNode != null )
      {
         organizationList =
            DOMTreeUtility.getNodes( organizationsNode, "organization" );
      }

      for( int orgCount = 0; orgCount < organizationList.size(); orgCount++ )
      {
         currentOrg = (Node)organizationList.elementAt(orgCount);
         itemList = DOMTreeUtility.getNodes( currentOrg, "item" );
         for( int itemCount = 0; itemCount < itemList.size(); itemCount++ )
         {
            currentItem = (Node)itemList.elementAt(itemCount);
            resultList.addElement(currentItem);
            resultList.addAll( getItems( currentItem ) );
         }
      }
      mLogger.exiting( "LMSImportHandler", "getItemsInManifest()" );
      return resultList;
   }


   /**
    * Returns an item Node whose identifier matches the ID passed in.
    *
    * @param iItemID The value of the identifier attribute of the item to be
    *                 found.
    *
    * @return Returns the item Node whose identifier matches the ID passed in.
    */
   public Node getItemWithID( String iItemID )
   {
      mLogger.entering( "LMSImportHandler", "getItemsWithID()" );
      Node manifestNode = mDocument.getDocumentElement();

      Vector manifestList = getAllManifests(manifestNode);

      Node currentItem = null;
      Node currentManifest = null;
      Node theNode = null;
      String currentItemID = new String();
      Vector itemList = new Vector();
      boolean isFound = false;

      itemList = getItemsInManifest( manifestNode );
      for( int itemCount = 0; itemCount < itemList.size(); itemCount++ )
      {
         currentItem = (Node)itemList.elementAt( itemCount );
         if( currentItem != null )
         {
            currentItemID =
               DOMTreeUtility.getAttributeValue( currentItem, "identifier" );
            if( currentItemID.equalsIgnoreCase(iItemID) )
            {
               theNode = currentItem;
               isFound = true;
            }
         }
      }
      if( !isFound )
      {
         for( int manCount = 0; manCount < manifestList.size(); manCount++ )
         {
            currentManifest = (Node)manifestList.elementAt( manCount );
            if( currentManifest != null )
            {
               itemList = getItemsInManifest( currentManifest );
               for( int count = 0; count < itemList.size(); count++ )
               {
                  currentItem = (Node)itemList.elementAt( count );
                  if( currentItem != null )
                  {
                     currentItemID =
                        DOMTreeUtility.getAttributeValue( currentItem,
                                                          "identifier" );
                     if( currentItemID.equalsIgnoreCase(iItemID) )
                     {
                        theNode = currentItem;
                        isFound = true;
                        break;
                     }
                  }
               }
            }
         }
      }
      mLogger.exiting( "LMSImportHandler", "getItemWithID()" );
      return theNode;
   }

   /**
    * Returns a Vector filled with all of the resource Nodes in a DOM
    *
    * @param iManifest The manifest node you wish to perform this operation on
    *
    * @return Returns a Vector containing all of the resource nodes in the
    * manifest.
    */
   public Vector getAllResources( Node iManifest )
   {
      mLogger.entering( "LMSImportHandler", "getAllResources()" );
      Vector resourceList = new Vector();
      Vector manifestList = new Vector();
      Node resourcesNode = DOMTreeUtility.getNode( iManifest, "resources" );
      resourceList = DOMTreeUtility.getNodes( resourcesNode, "resource" );
      manifestList = DOMTreeUtility.getNodes( iManifest, "manifest" );
      Node currentManifest = null;

      for( int i = 0; i < manifestList.size(); i++ )
      {
         currentManifest = (Node) manifestList.elementAt(i);
         resourceList.addAll( getAllResources( currentManifest ) );
      }
      mLogger.exiting( "LMSImportHandler", "getAllResources()" );
      return resourceList;
   }


   /**
    * Returns a Vector filled with all of the manifest Nodes in a DOM
    *
    * @param iManifest The manifest node you wish to perform this operation on
    *
    * @return Returns a Vector containing all of the manifest nodes in the
    * manifest.
    */
   public Vector getAllManifests( Node iManifest )
   {
      mLogger.entering( "LMSImportHandler", "getAllManifests()" );
      Vector resultList = new Vector();
      Vector manifestList = new Vector();
      Node currentManifest = null;
      if( iManifest != null)
      {
         manifestList = DOMTreeUtility.getNodes( iManifest, "manifest" );
         resultList = new Vector(manifestList);
      }

      for( int manifestCount = 0; 
               manifestCount < manifestList.size(); 
               manifestCount++ )
      {
         currentManifest = (Node) manifestList.elementAt(manifestCount);
         resultList.addAll( getAllManifests( currentManifest ) );
      }
      mLogger.exiting( "LMSImportHandler", "getAllManifests()" );
      return resultList;
   }


   /**
    * Returns the resource or manifest node in a manifest whose identifier
    * attribute matches the ID passed in.
    *
    * @param iManifest The manifest node you wish to perform this operation on
    *
    * @param iID The value of the identifier of the node you are looking
    * for.
    *
    * @return Returns the Node that has the identifier matching the ID passed 
    * in.
    */
   public Node getNodeWithID( Node iManifest, String iID )
   {
      mLogger.entering( "LMSImportHandler", "getNodeWithID()" );
      boolean isFound = false;
      Node theNode = null;
      Node currentManifest = null;
      Node currentResource = null;
      Vector allManifests = getAllManifests( iManifest );
      int i = 0;
      int j = 0;
      String manifestID = new String();
      String resourceID = new String();

      while( (i < allManifests.size()) && (!isFound) )
      {
         currentManifest = (Node) allManifests.elementAt( i );
         manifestID = DOMTreeUtility.
                 getAttributeValue( currentManifest, "identifier" );
         if( manifestID.equalsIgnoreCase( iID ) )
         {
            isFound = true;
            theNode = (Node) allManifests.elementAt( i );
            break;
         }
      
         // Manifest not found increment counter and check the next 
         // manifest identifier
         i++;
      }
      
      if( !isFound )
      {
         //mLogger.info("NOT FOUND" + iID);
         Vector allResources = getAllResources( iManifest );
         while( (j < allResources.size()) && (!isFound) )
         {
            currentResource = (Node) allResources.elementAt( j );
            resourceID = DOMTreeUtility.
                 getAttributeValue( currentResource, "identifier");
            if( resourceID.equalsIgnoreCase( iID ) )
            {
               isFound = true;
               theNode = (Node) allResources.elementAt( j );
               break;
            }
            
            // Manifest not found increment counter and check the next 
            // manifest identifier
            j++;          
         }
      }
      mLogger.exiting( "LMSImportHandler", "getNodeWithID()" );
      return theNode;
   }

   /**
    * This method loops through the elements in the mItemIdrefs vector of the
    * ManifestMap object. If the element doesn't match an element in the
    * mResourceIds vector, it searches the DOM tree of the given manifest Node
    * for the node with the ID matching the itemIdrefs value.  If the node found
    * to have the matching ID is a manifest node, the sub-manifest is rolled up,
    * merging the organization node of the sub-manifest with the item node that
    * referenced the sub-manifest.  It then recursivly loops through the
    * mManifestMaps vector, performing these operations on each element.
    *
    * @param iManifestMap The ManifestMap that this method is to be performed
    * on.
    * @param iManifestNode The root manifest node of the DOM tree.
    *
    */
   public void processManifestMap( ManifestMap iManifestMap, 
                                   Node iManifestNode )
   {
      mLogger.entering( "LMSImportHandler", "processManifestMap()" );
      boolean isInResources = false;
      Node theNode = null;
      String theNodeName = new String();

      Vector resourceIDs = iManifestMap.getResourceIds();
      Vector itemIDs = iManifestMap.getItemIds();
      Vector itemIdrefs = iManifestMap.getItemIdrefs();

      String itemIdref = new String();
      String resourceID = new String();
      Node organizationsNode = null;
      Vector organizationNodes = new Vector();
      int organizationIndex = 0;
      String defaultOrgID = new String();
      String tempOrgID = new String();
      Node tempOrgNode = null;
      Node organizationNode = null;
      String organizationID = new String();
      NodeList orgChildren = null;
      String identifierToReplace = new String();
      Node oldItem = null;
      NodeList oldItemChildren;
      Node currentOldChild = null;
      Node currentChild = null;
      Attr identifierAttr = null;
      ManifestMap currentManifestMap;

      //Loop through the itemIdref Vector
      for( int idRefCount = 0; idRefCount < itemIdrefs.size(); idRefCount++ )
      {
         itemIdref = (String) itemIdrefs.elementAt(idRefCount);
         if( !itemIdref.equals("") )
         {
            theNode = null;
            isInResources = false;
            for( int resourceIDCount = 0; 
                 resourceIDCount < resourceIDs.size(); 
                 resourceIDCount++ )
            {
               resourceID = (String) resourceIDs.elementAt(resourceIDCount);
               if( itemIdref.equals(resourceID) )
               {
                  isInResources = true;
                  break;
               }
            }

            //If the itemIdref is not in the resourceIDs vector
            if( !isInResources )
            {
               //Get the node whose identifier is in this position of the
               //itemIdref vector.
               theNode = getNodeWithID(iManifestNode, itemIdref);

               if( theNode != null )
               {
                  theNodeName = theNode.getLocalName();

                  //If theNode is a manifest node
                  if( theNodeName.equals("manifest") )
                  {
                     organizationsNode = null;

                     organizationsNode = DOMTreeUtility.
                                            getNode( theNode, "organizations" );
                     organizationNodes = DOMTreeUtility.
                                  getNodes( organizationsNode, "organization" );

                     organizationIndex = 0;

                     //Find the organization node referanced by the "default"
                     //attribute of the organizations node.  If no match is
                     //found, use the first organization.
                     defaultOrgID = DOMTreeUtility.
                              getAttributeValue( organizationsNode, "default" );
                     for( int orgCount = 0;
                            orgCount < organizationNodes.size(); orgCount++ )
                     {
                        tempOrgNode = 
                           (Node)organizationNodes.elementAt(orgCount);
                        
                        tempOrgID = DOMTreeUtility.
                                  getAttributeValue(tempOrgNode, "identifier" );
                        if( tempOrgID.equals(defaultOrgID) )
                        {
                           organizationIndex = orgCount;
                           break;
                        }
                     }

                     organizationNode = (Node)organizationNodes.
                                        elementAt(organizationIndex);

                     organizationID = DOMTreeUtility.
                            getAttributeValue( organizationNode, "identifier" );

                     orgChildren = organizationNode.getChildNodes();

                     identifierToReplace = 
                        (String)itemIDs.elementAt(idRefCount);
                     oldItem = getItemWithID( identifierToReplace );

                     oldItemChildren = oldItem.getChildNodes();

                     for ( int oldChildCount = 0; 
                           oldChildCount < oldItemChildren.getLength(); 
                           oldChildCount++ )
                     {
                        currentOldChild = oldItemChildren.item(oldChildCount);
                        oldItem.removeChild(currentOldChild);
                     }

                     // Loop through all of the children of the sub-manifest
                     // organization and append them to the item (oldItem) that
                     // referenced the sub-manifest.
                     for ( int childCount = 0; 
                           childCount < orgChildren.getLength();  )
                     {
                        currentChild = orgChildren.item(childCount);
                        try
                        {
                           oldItem.appendChild( currentChild );
                        }
                        catch(DOMException domExcep)
                        {
                           domExcep.printStackTrace();
                        }
                     }

                     oldItemChildren = oldItem.getChildNodes();

                     identifierAttr =
                        DOMTreeUtility.getAttribute( oldItem, "identifier");

                     identifierAttr.setValue(organizationID);

                     DOMTreeUtility.removeAttribute( oldItem, "identifierref");
                  }
               }
            }
         }
      }
      mLogger.exiting( "LMSImportHandler", "processManifestMap()" );
   }

   /**
    * This method processes the xml:base values of the manifest
    *
    */
   public void applyXMLBase()
   {
      mLogger.entering( "LMSImportHandler", "applyXMLBase()" );
      Node manifest = mDocument.getDocumentElement();
      ManifestMap manifestMap = new ManifestMap();

      manifestMap.populateManifestMap(manifest);
      applyXMLBase(manifest);

      mLogger.exiting( "LMSImportHandler", "applyXMLBase()" );
   }

   
   /**
    * This method accesses the manifest handler to extract all SCO launch
    * information
    * 
    * @param iRolledUpDocument - Dom with manifest rollup
    * @param iDefaultOrganizationOnly - boolean describing which organization to
    *           read from
    * @param iRemoveAssets - boolean describing if to include assets in launch
    *           locatation
    * @return Returns a vector of launch data
    */
   public Vector getLaunchData(Document iRolledUpDocument, boolean iDefaultOrganizationOnly, boolean iRemoveAssets)
   {
      return mManifestHandler.getLaunchData(iRolledUpDocument.getDocumentElement(), iDefaultOrganizationOnly,
         iRemoveAssets);
   }
   
   /**
    * This method cleans up the temporary folder used by the CPValidator
    * for extraction of the test subject package.  This method loops through
    * the temporary PackageImport folder to remove all files that have been
    * extracted during the content package extract.
    *
    * @param iPath Temporary directory location where package was
    * extracted and in need of cleanup.
    * 
    */
   public static void cleanImportDirectory( String iPath )
   {
      try
      {
         File theFile = new File( iPath );
         
         if ( theFile.exists() )
         {
            File allFiles[] = theFile.listFiles();
   
            for ( int i = 0; i < allFiles.length; i++ )
            {
               if ( allFiles[i].isDirectory() )
               {
                  cleanImportDirectory( allFiles[i].toString() );
                  allFiles[i].delete();
               }
               else
               {
                  allFiles[i].delete();
               }
            }
         }
      }
      catch ( NullPointerException npe )
      {
         npe.printStackTrace();
      }
   }
   
   /**
    * This method extracts the selected test subject, in the form of a zip file,
    * to a temporary "/PackageImport" directory in order to perform validation.
    * 
    * @param iEnVar The location of the SRTE installation
    * @param iPIF The content package zip test file URI
    * @return extractDir returnst the path of the directory the file is
    *         extracted to
    */
   public static String importContentPackage(String iEnVar, String iPIF)
   {
      String extractDir = "";
      try
      {
      // get the extract dir

      extractDir = iEnVar + File.separator + "PackageImport" + File.separator;

      // Unzip the content package into a local directory for processing
      UnZipHandler uzh = new UnZipHandler(iPIF, extractDir);
      boolean mZipExtractionResult = uzh.extract();

      if ( !mZipExtractionResult )
      {
         extractDir = "";
      }
      }
      catch (NullPointerException npe )
      {
         npe.printStackTrace();
      }
      return extractDir;
   }
}
