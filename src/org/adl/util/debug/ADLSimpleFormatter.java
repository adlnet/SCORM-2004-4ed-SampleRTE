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

package org.adl.util.debug;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * <strong>Filename: </strong> <br>
 * ADLSimpleFormatter.java <br>
 * <br>
 * <strong>Description: </strong> <br>
 * A <code>ADLSimpleFormatter</code> extends Java's SimpleFormatter class and
 * overrides that class's format function. This is so we can modify the messages
 * that are output using Java's logging output messages. Specifically, we do not
 * want the date/timestamp written on each and every message written to the
 * Console. <br>
 * <br>
 * 
 * @author ADL Technical Team <br>
 */
public class ADLSimpleFormatter extends SimpleFormatter
{
   /**
    * A line separator used to separate messages sent to the log.
    */
   private String mLineSeparator = (String)java.security.AccessController
      .doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

   /**
    * Overrides SimpleFormatter format function. Writes the output without
    * displaying the date/timestamp.
    * 
    * @param iRecord The log record that needs formatted.
    * @return A string formatted for a logging message
    */
   public synchronized String format(LogRecord iRecord)
   {
      StringBuffer sb = new StringBuffer();

      if( iRecord.getSourceClassName() != null )
      {
         sb.append(iRecord.getSourceClassName());
      }
      else
      {
         sb.append(iRecord.getLoggerName());
      }
      if( iRecord.getSourceMethodName() != null )
      {
         sb.append(" ");
         sb.append(iRecord.getSourceMethodName());
      }
      sb.append(" ");

      String message = formatMessage(iRecord);
      sb.append(iRecord.getLevel().getLocalizedName());
      sb.append(": ");
      sb.append(message);
      sb.append(mLineSeparator);

      return sb.toString();
   }
}