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

package org.adl.samplerte.client;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;

import org.adl.samplerte.server.LMSCMIServletRequest;
import org.adl.samplerte.server.LMSCMIServletResponse;
import org.adl.util.debug.DebugIndicator;
import org.adl.util.servlet.ServletWriter;

/**
 * 
 * <strong>Filename:</strong> ServletProxy<br><br>
 *
 * <strong>Description:</strong><br>
 * This class encapsulates communication between the API Adapter applet and
 * the <code>LMSCMIServlet</code>.<br><br>
 *
 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the SCORM 2004 4th Edition Sample RTE. <br>
 * <br>
 *
 * <strong>Implementation Issues:</strong><br><br>
 *
 * <strong>Known Problems:</strong><br><br>
 *
 * <strong>Side Effects:</strong><br><br>
 *
 * <strong>References:</strong><br>
 * <ul>
 *     <li>SCORM 2004 4th Edition</li>
 * </ul>
 *
 * @author ADL Technical Team
 */
public class ServletProxy
{
   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;

   /**
    * The URL of the target servlet.
    */
   private URL mServletURL = null;

   /**
    * Constructor
    *
    * @param iURL  The URL of the target servlet.
    */
   public ServletProxy(URL iURL)
   {
      mServletURL = iURL;
   }

   /**
    * Reads from the LMS server via the <code>LMSCMIServlet</code>; the
    * <code>SCODataManager</code> object containing all of the run-time data
    *  model elements relevant for the current user (student) and current SCO.
    *
    * @param iRequest A <code>LMSCMIServletRequest</code> object that
    *                 provides all the data neccessary to POST a call to
    *                 the <code>LMSCMIServlet</code>.
    *
    * @return The <code>LMSCMIServletResponse</code> object provided by the
    *         <code>LMSCMIServlet</code>.
    */
   public LMSCMIServletResponse postLMSRequest(LMSCMIServletRequest iRequest)
   {

      if( _Debug )
      {
         System.out.println("In ServletProxy::postLMSRequest()");
      }

      LMSCMIServletResponse response = new LMSCMIServletResponse();

      try
      {
         if( _Debug )
         {
            System.out.println("In ServletProxy::postLMSRequest()");
         }

         Serializable[] data = { iRequest };

         if( _Debug )
         {
            System.out.println("Before postObjects()");
         }

         ObjectInputStream in = ServletWriter.postObjects(mServletURL, data);

         if( _Debug )
         {
            System.out.println("Back In ServletProxy::postLMSRequest()");
            System.out.println("Attempting to read servlet " + "response now...");
         }

         response = (LMSCMIServletResponse)in.readObject();

         in.close();
         response.mError = "OK";
      }
      catch( Exception e )
      {
         if( _Debug )
         {
            System.out.println("Exception caught in " + "ServletProxy::postLMSRequest()");
            System.out.println(e.getMessage());
         }

         e.printStackTrace();
         response.mError = "FAILED";
      }

      return response;
   }
} // ServletProxy
