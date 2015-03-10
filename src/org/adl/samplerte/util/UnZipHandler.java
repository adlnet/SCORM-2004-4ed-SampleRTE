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
package org.adl.samplerte.util;

import java.util.zip.ZipException;

import java.io.IOException;
import java.io.File;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import org.adl.util.debug.DebugIndicator;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipEntry;

/**
 *
 * <strong>Filename:</strong><br>UnZipHandler.java<br><br>
 *
 * <strong>Description:</strong><br>
 * A <code>UnZipHandler</code> provides the ability to extract the contents
 * of a zipped file to a given directory.<br><br>
 * 
 * <strong>Side Effects:</strong><br> The files will be extracted to the given
 * directory
 * 
 * @author ADL Technical Team
 */
public class UnZipHandler
{
   /**
    * Buffer size for use in extracting Zip File
    */
   public static final int BUFFER_SIZE = 16384;
   
   /**
    * A reference to the Zip File to be extracted
    */
   private File mZipFile;

   /**
    * The directory to which the Zip File will be extracted to
    */
   private String mExtractToDir;
   
   /**
    * Constructor for the <code>UnZipHandler</code> class.
    *
    * @param iZipFileName Name and path of the <code>.zip</code> file.
    * @param iTargetDirName Name and path of the directory to extract the 
    *                       contents of the Zip File to.
    */
   public UnZipHandler(String iZipFileName, String iTargetDirName )
   {
      setFile( iZipFileName );
      setTargetDirectory( iTargetDirName );
   }

   /**
    * Set the name and location of the Zip File to be extracted.
    *
    * @param iFileName Name and location of the Zip File to be extracted.
    */
   private void setFile(String iFileName)
   {
      try
      {
         mZipFile = new File( iFileName ); 
      }
      catch ( NullPointerException npe )
      {
         npe.printStackTrace();
      }
   }

   /**
    * Set the target directory of the extracted contents of the Zip File.
    *
    * @param iTargetDirPath The target directory of the extracted contents of 
    *                       the Zip File.
    *
    */
   private void setTargetDirectory( String iTargetDirPath )
   {
      try
      {
         mExtractToDir = iTargetDirPath;
      }
      catch ( NullPointerException npe )
      {
         npe.printStackTrace();
      }
   }

   /**
    * Returns the target directory of the extracted contents of the Zip 
    * File.
    *
    * @return target The target directory of the extracted contents of the Zip 
    *                File.
    */
   public String getTargetDirectory()
   {
      return mExtractToDir;
   }

   /**
    * Extracts the Zip File into the destination directory
    *
    */
   public boolean extract()
   {
      boolean result = true;
      String fileName = new String();
      String destFileName = new String();
      InputStream in = null;
      OutputStream out = null;      

      // Create a byte buffer
      byte[] buffer = new byte[BUFFER_SIZE];

      try
      {  
    	 ZipFile archive;
    	 
       // protect against the case that the user only installs the JRE
       // with no language support
       try
    	 {
    	    archive = new ZipFile( mZipFile, "CP437");
    	 }
    	 catch ( ZipException ZE )
    	 {
    		 archive = new ZipFile( mZipFile );
    	 }
    	 
         for ( Enumeration e = archive.getEntries(); e.hasMoreElements(); )
         {
            // Get the next entry in the Zip File
            ZipEntry entry = (ZipEntry)e.nextElement();

            if ( !entry.isDirectory() )
            {
               fileName = entry.getName();
               fileName = fileName.replace('/', File.separatorChar);

               destFileName = mExtractToDir + fileName;

               File destFile = new File(destFileName);

               // Create the destination path, if needed
               String parent = destFile.getParent();
               if ( parent != null )
               {
                  File parentFile = new File(parent);
                  if ( !parentFile.exists() )
                  {
                     // Create the chain of sub-directories to the file
                     parentFile.mkdirs();
                  }
               }

               // Get a stream of the archive entry's bytes
               in = archive.getInputStream(entry);

               // Open a stream to the destination file
               out = new FileOutputStream(destFileName);

               // Repeat reading into buffer and writing buffer to file,
               // until done.  Count will always be # bytes read, until
               // EOF when it is -1.
               int count;
               while ( (count = in.read(buffer)) != -1 )
               {
                  out.write(buffer, 0, count );
               }
               
               // Close the input stream and output stream
               in.close();
               out.close();
            }
         }
         archive.close();
      }
      catch ( ZipException ze )
      {
          result = false;
          if ( DebugIndicator.ON )
          {
             ze.printStackTrace(); 
          }          
      }
      catch ( NullPointerException npe )
      {
         result = false;
         if ( DebugIndicator.ON )
         {
            npe.printStackTrace(); 
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
      catch ( SecurityException se )
      {
         result = false;
         if ( DebugIndicator.ON )
         {
            se.printStackTrace(); 
         }
      }
      catch ( Exception e )
      {
         result = false;
         if ( DebugIndicator.ON )
         {
            e.printStackTrace(); 
         }
      }
      finally
      {
         // In case an exception is thrown prior to closing the input stream
         // and output stream, close the streams
         
         // Check to make sure the input stream has not been closed
         if (in != null)
         {
            try
            { 
               in.close();
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }
         }

         // Check to make sure the output stream has not been closed
         if (out != null)
         {
            try
            {
               out.close();
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }
         }        
      } // end finally
      
      return result;
      
   }  // end extract()
}