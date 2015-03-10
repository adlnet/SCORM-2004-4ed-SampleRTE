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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.adl.samplerte.server.packageimport.parsers.dom.DOMTreeUtility;

/**
 *
 * <strong>Filename: </strong><br>ManifestMap.java<br><br>
 *
 * <strong>Description: </strong><br> A <code>ManifestMap</code> is a Data
 * Structure used to store manifest information
 *
 * @author ADL Technical Team
 */

public class ManifestMap
{
   /**
    * Logger object used for debug logging.
    */
   private Logger mLogger;

   /**
    * The identifier attribute of the &lt;manifest&gt; element.
    */
   private String mManifestId;

   /**
    * The identifier attributes of all &lt;resource&gt; elements that belong to the
    * &lt;manifest&gt; element of mManifestId.
    */
   private Vector mResourceIds;

   /**
    * The identifier attributes of all &lt;item&gt; elements that belong to the
    * &lt;manifest&gt; element of mManifestId.
    */
   private Vector mItemIds;

   /**
    * The identifier reference values of all &lt;item&gt; elements that belong to the
    * &lt;manifest&gt; element of mManifestId.
    */
   private Vector mItemIdrefs;

   /**
    * The identifier reference values of all &lt;dependency&gt; elements that belong to
    * the &lt;manifest&gt; element of mManifestId.
    */
   private Vector mDependencyIdrefs;

   /**
    * The default constructor.
    */
   public ManifestMap()
   {
      mLogger = Logger.getLogger("org.adl.util.debug.samplerte"); 

      mManifestId                   = new String();
      mResourceIds                  = new Vector();
      mItemIds                      = new Vector();
      mItemIdrefs                   = new Vector();
      mDependencyIdrefs             = new Vector();
   }


   /**
    * Gives access to the identifier value of the &lt;manifest&gt; element.
    *
    * @return - The identifier value of the &lt;manifest&gt; element.
    */
   public String getManifestId()
   {
      return mManifestId;
   }

   /**
    * Gives access to the identifier attributes of all &lt;resource&gt; elements that
    * belong to the &lt;manifest&gt; element of mManifestId.
    *
    * @return - The identifier attributes of all &lt;resource&gt; elements that
    * belong to the &lt;manifest&gt; element of mManifestId.
    */
   public Vector getResourceIds()
   {
      return mResourceIds;
   }

   /**
    * Gives access to the identifier attributes of all &lt;item&gt; elements that
    * belong to the &lt;manifest&gt; element of mManifestId.
    *
    * @return - The identifier attributes of all &lt;item&gt; elements that
    * belong to the &lt;manifest&gt; element of mManifestId.
    */
   public Vector getItemIds()
   {
      return mItemIds;
   }

   /**
    * Gives access to the identifier reference values of all &lt;item&gt; elements
    * that belong to the &lt;manifest&gt; element of mManifestId.
    *
    * @return - The identifier reference values of all &lt;item&gt; elements that
    * belong to the &lt;manifest&gt; element of mManifestId.
    */
   public Vector getItemIdrefs()
   {
      return mItemIdrefs;
   }

   /**
    * This method populates the ManifestMap object by traversing down
    * the document node and storing all information necessary for the validation
    * of manifests.  Information stored for each manifest element includes:
    * manifest identifiers,item identifers, item identifierrefs, and
    * resource identifiers
    *
    * @param iNode the node being checked. All checks will depend on the type of node
    * being evaluated
    * 
    * @return - The boolean describing if the ManifestMap object(s) has been
    * populated properly.
    */
   public boolean populateManifestMap( Node iNode )
   {
      // looks exactly like prunetree as we walk down the tree
      mLogger.entering( "ManifestMap", "populateManifestMap" );  

      boolean result = true;

      // is there anything to do?
      if ( iNode == null )
      {
         result = false;
         return result;
      }

      int type = iNode.getNodeType();

      switch ( type )
      {
         case Node.PROCESSING_INSTRUCTION_NODE:
         {
            break;
         }
         case Node.DOCUMENT_NODE:
         {
            Node rootNode = ((Document)iNode).getDocumentElement();

            result = populateManifestMap( rootNode ) && result;

            break;
         }
         case Node.ELEMENT_NODE:
         {
            String parentNodeName = iNode.getLocalName();

            if ( parentNodeName.equalsIgnoreCase("manifest") ) 
            {
               // We are dealing with an IMS <manifest> element, get the IMS
               // CP identifier for the <manifest> elememnt
               mManifestId =
                  DOMTreeUtility.getAttributeValue( iNode,
                                                    "identifier" ); 

               mLogger.finest( "ManifestMap:populateManifestMap, " + 
                               "Just stored a Manifest Id value of " + 
                                mManifestId );

               // Recurse to populate mItemIdrefs and mItemIds

               // Find the <organization> elements

               Node orgsNode = DOMTreeUtility.getNode( iNode, "organizations" ); 

               if( orgsNode != null )
               {
                  Vector orgElems = DOMTreeUtility.getNodes( orgsNode, "organization" ); 

                  mLogger.finest( "ManifestMap:populateManifestMap, " + 
                                  "Number of <organization> elements: " + 
                                   orgElems.size() );

                  if ( !orgElems.isEmpty() )
                  {
                     int orgElemsSize = orgElems.size();
                     for (int i = 0; i < orgElemsSize; i++ )
                     {
                        Vector itemElems = DOMTreeUtility.getNodes(
                                            (Node)orgElems.elementAt(i), "item" ); 

                        mLogger.finest( "ManifestMap:populateManifestMap, " + 
                                        "Number of <item> elements: " + 
                                         itemElems.size() );

                        if ( !itemElems.isEmpty() )
                        {
                           int itemElemsSize = itemElems.size();
                           for (int j = 0; j < itemElemsSize; j++ )
                           {
                              result = populateManifestMap(
                                 (Node)(itemElems.elementAt(j)) ) && result;
                           }
                        }
                     }
                  }
               }

               //recurse to populate mResourceIds

               Node resourcesNode = DOMTreeUtility.getNode( iNode, "resources" ); 

               if( resourcesNode != null )
               {
                  Vector resourceElems = DOMTreeUtility.getNodes(
                                                  resourcesNode, "resource" ); 

                  mLogger.finest( "ManifestMap:populateManifestMap, " + 
                               "Number of <resource> elements: " + 
                                resourceElems.size() );

                  int resourceElemsSize = resourceElems.size();
                  for (int k = 0; k < resourceElemsSize; k++ )
                  {
                     result = populateManifestMap(
                                 (Node)(resourceElems.elementAt(k)) ) && result;
                  }
               }
            }
            else if ( parentNodeName.equalsIgnoreCase("item") ) 
            {
               //store item identifier value
               String itemId =
                        DOMTreeUtility.getAttributeValue( iNode, "identifier" );
               
               mItemIds.add( itemId );

               mLogger.finest( "ManifestMap:populateManifestMap, " + 
                                  "Just stored an Item Id value of " + 
                                   itemId );

               //store item identifier reference value
               String itemIdref =
                     DOMTreeUtility.getAttributeValue( iNode, "identifierref" );
               
               mItemIdrefs.add( itemIdref );

               mLogger.finest( "ManifestMap:populateManifestMap, " + 
                                  "Just stored an Item Idref value of " + 
                                   itemIdref );

               //recurse to populate all child item elements
               Vector items = DOMTreeUtility.getNodes( iNode, "item" ); 
               if ( !items.isEmpty() )
               {
                  int itemsSize = items.size();
                  for ( int z = 0; z < itemsSize; z++ )
                  {
                     result = populateManifestMap(
                        (Node)items.elementAt(z) ) && result;
                  }
               }
            }
            else if ( parentNodeName.equalsIgnoreCase("resource") ) 
            {
               //store resource identifier value
               String resourceId =
                        DOMTreeUtility.getAttributeValue( iNode, "identifier" ); 
               // convert to lower so case sensativity does not play a role
               
               mResourceIds.add( resourceId  );

               mLogger.finest( "ManifestMap:populateManifestMap, " + 
                                  "Just stored a Resource Id value of " + 
                                   resourceId );

               // populate <dependency> element

               Vector dependencyElems = DOMTreeUtility.getNodes( iNode,
                                                                 "dependency" ); 

               int dependencyElemsSize= dependencyElems.size();

               for(int w=0; w < dependencyElemsSize; w++ )
               {
                  Node dependencyElem = (Node)dependencyElems.elementAt(w);

                  //store resource identifier value
                  String dependencyIdref =
                        DOMTreeUtility.getAttributeValue( dependencyElem,
                                                          "identifierref" ); 
                  
                  mDependencyIdrefs.add( dependencyIdref );

                  mLogger.finest( "ManifestMap:populateManifestMap, " + 
                                     "Just stored a Dependency Idref value of " + 
                                      mDependencyIdrefs );
               }
            }

            break;
         }
         // handle entity reference nodes
         case Node.ENTITY_REFERENCE_NODE:
         {
            break;
         }

         // text
         case Node.COMMENT_NODE:
         {
            break;
         }

         case Node.CDATA_SECTION_NODE:
         {
            break;
         }

         case Node.TEXT_NODE:
         {
            break;
         }
      }

      mLogger.exiting( "ManifestMap", "populateManifestMap" );  

      return result;
   }
}