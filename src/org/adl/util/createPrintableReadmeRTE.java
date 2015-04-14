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
package org.adl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.adl.util.EnvironmentVariable;

public class createPrintableReadmeRTE
{     
   public createPrintableReadmeRTE()
   {                
   } 
   public void startCopy()
   {
      String RTEReadmeLocation = EnvironmentVariable.getValue("SCORM4ED_SRTE111_HOME")
      + File.separatorChar + "Sample_RTE"
      + File.separatorChar + "source"
	  + File.separatorChar + "SampleRTE"
	  + File.separatorChar + "LMSClient" 
      + File.separatorChar + "RTE_Readme" + File.separatorChar ;
      
      // RTE files
      String[] filesToAppend = {"toc.html","rteIntroduction.html","rteInstallation.html",
            "revisionsToRTE.html","runSampleRTE.html","rteServerComponent.html",
            "rteClientComponentJava.html","rteClientComponentWeb.html","thirdPartyComponents.html",
            "buildingTheRTE.html","knownIssuesRTE.html","implementationDecisions.html",
            "environmentVariableRTE.html","odbcConnectionsRTE.html","systemRequirementsRTE.html",
            "licenseCopyright.html"};
     
      BufferedReader in;  
      BufferedWriter out;
     
      try
      {
          // Open an input stream from the header html file
          in = new BufferedReader(new InputStreamReader(new FileInputStream(RTEReadmeLocation + "printableHeader.html")));
                   
          // create the output stream writer to whichever file this is being compiled for
          out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(RTEReadmeLocation + "printReadmeRTE.html",false)));
          
          String q = in.readLine();          
          while(q!=null)
          {            
             // mark the file at each line, if the end body tag is found this will be directly before it
             // can read 1000 characters while keeping the mark.
             in.mark(1000);      
             // if q contains "<body" stop writing to output file
             if ( q.indexOf("</body>") != -1)
             {
              // reset to the mark and append the body contents from each other file here.
                in.reset();//back to the mark for appending
                
                // start looping through files, call copyBody(output filestream, string filename)
                for(int i=0; i<filesToAppend.length;i++)
                {
                   System.out.println(filesToAppend[i]);
                   copyBody(out, filesToAppend[i], RTEReadmeLocation);
                }
             }
             
             out.write(q);
             out.newLine();
             
             q = in.readLine();
          }
          // Close our streams
          in.close();     
          out.close();
      }
      // Catches any error conditions
      catch (IOException e)
      {
         System.err.println ("Unable to read from file " + e);
         e.printStackTrace();
         System.exit(-1);
      }      
   }    
   
   public void copyBody(BufferedWriter out, String file, String fileLoc)
   {
      boolean inBody = false;
      String line;
      
      try {
         FileInputStream fis = new FileInputStream(fileLoc + file);
         BufferedReader reader = new BufferedReader ( new InputStreamReader(fis));
         
         out.write("<div class=\"bg\" id=\"" + file + "\">");
         out.newLine();
         
         while((line = reader.readLine()) != null) 
         {
            if (line.indexOf("<body") != -1)
            {
               //skip that line
               line = reader.readLine();
               inBody = true;               
            }
            else if(line.indexOf("</body>") != -1)
            {
               inBody = false;
            }            
            if(inBody)
            {
               out.write(line);
               out.newLine();
            }            
         }
         
         reader.close();
         fis.close();         
         
         out.write("</div>");
         out.newLine();        
      }
      catch (Throwable e) 
      {
         e.printStackTrace();
         System.exit(-1);
      }
   }
   
   /**
    * @param args
    */
   public static void main(String[] args)
   {
      createPrintableReadmeRTE cpr = new createPrintableReadmeRTE();
      cpr.startCopy(); 
   }

}
