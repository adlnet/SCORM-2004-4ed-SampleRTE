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
package org.adl.sequencer;

import org.adl.util.debug.DebugIndicator;

import java.io.Serializable;

/**
 * <strong>Filename:</strong> ADLDuration.java<br><br>
 *
 * <strong>Description:</strong>This class represents a duration.  The class
 * represents the duration as a <code>long</code>.  All durations are 
 * converted to a <code>long</code> upon creation.<br><br>
 * 
 * @author ADL Technical Team
 */
public class ADLDuration implements Serializable
{

   /**
    * Enumeration of possible relations between two <code>ADLDuration</code>
    * objects.
    * <br>Unknown
    * <br><b>-999</b>
    */
   public static final int UNKNOWN   =  -999;

   /**
    * Enumeration of possible relations between two <code>ADLDuration</code>
    * objects.
    * <br>Less Than
    * <br><b>-1</b>
    */
   public static final int LT   =  -1;

   /**
    * Enumeration of possible relations between two <code>ADLDuration</code>
    * objects.
    * <br>Less Than
    * <br><b>0</b>
    */
   public static final int EQ   =   0;

   /**
    * Enumeration of possible relations between two <code>ADLDuration</code>
    * objects.
    * <br>Greater Than
    * <br><b>1</b>
    */
   public static final int GT   =   1;

   /**
    * Enumeration of possible formats for duration information.
    * <br>Seconds /w one tenth second accuracy
    * <br><b>0</b>
    */
   public static final int FORMAT_SECONDS      =  0;

   /**
    * Enumeration of possible formats for duration information.
    * <br>XML Schema -- Duration Type
    * <br><b>1</b>
    */
   public static final int FORMAT_SCHEMA       =  1;


   /**
    * This controls display of log messages to the java console
    */
   private static boolean _Debug = DebugIndicator.ON;


   /**
    * The duration being tracked in milliseconds
    */
   public double mDuration = 0.0;

   /**
    * Default constructor for the <code>ADLDuration</code> class.  Sets the 
    * duration to zero.
    *
    */
   public ADLDuration()
   {
      mDuration = 0;
   }

   /**
    * Constructor for the <code>ADLDuration</code> class.  Based on the format 
    * (<code>iFormat</code> to be used, this constructor takes the string 
    * representation of the duration (<code>iValue</code>) and creates an
    * <code>ADLDuration</code>.
    * 
    * @param iFormat Indicates the format for the duration
    * @param iValue String value that holds the duration to be used
    */
   public ADLDuration(int iFormat, String iValue)
   {

      String hours = null;
      String min = null;
      String sec = null;
      
      switch ( iFormat )
      {
         
         case FORMAT_SECONDS:
         {
            double secs = 0.0;

            try
            {
               secs = Double.parseDouble(iValue);
            }
            catch ( Exception e )
            {
               if ( _Debug )
               {
                  System.out.print("  Invalid Format ::  " + iFormat +  
                                   " // " + iValue);
               }
            }

            mDuration = secs * 1000.0;

            break;

         }
         case FORMAT_SCHEMA:
         {           
            
            int locStart = iValue.indexOf('T');
            int loc = 0;

            try 
            {
               if ( locStart != -1 )
               {
                  locStart++;
   
                  loc = iValue.indexOf("H", locStart);
   
                  if ( loc != -1 )
                  {
                     hours = iValue.substring(locStart, loc);
                     mDuration = Double.parseDouble(hours) * 3600;
   
                     locStart = loc + 1;
                  }
   
                  loc = iValue.indexOf("M", locStart);
                  if ( loc != -1 )
                  {
                     min = iValue.substring(locStart, loc);
                     mDuration += Double.parseDouble(min) * 60;
   
                     locStart = loc + 1;
                  }
   
                  loc = iValue.indexOf("S", locStart);
                  if ( loc != -1 )
                  {
                     sec = iValue.substring(locStart, loc);
                     mDuration += Double.parseDouble(sec);
                  }
               }
               else
               {
                  if ( _Debug )
                  {
                     System.out.println(" ERROR : Invalid format  --> " +
                                        iValue);
                  }
               }
   
               break;
            }
            catch (NumberFormatException nfe)
            {
               System.out.println("ERROR");
               nfe.printStackTrace();
            }

         }
         default:
         {
            // Do nothing
         }
      }
   }

   /**
    * This method rounds the given value to one decimal place
    * 
    * @param iValue The value which will be rounded.
    * 
    * @return Returns a value of type double containing the
    *         rounded value
    */
   private double round(double iValue)
   {
      iValue = iValue * 10;
      iValue = Math.rint(iValue);
      iValue = iValue / 10;
      return iValue;
   }
   
   /**
    * This method formats the duration value according to the format type
    * passed in (<code>iFormat</code>).
    * 
    * @param iFormat Indicates the format for which this method should convert
    * the duration value to.
    * 
    * @return Returns a string representation of the duration, formatted
    * accordingly.
    */
   public String format(int iFormat)
   {

      String out = null;

      long countHours = 0;
      long countMin = 0;
      double countSec = 0;

      double temp = 0;

      switch ( iFormat )
      {
         
         case FORMAT_SECONDS:
         {
            double sec = mDuration / 1000.0;

            out = (new Double(sec)).toString();

            break;

         }
         case FORMAT_SCHEMA:
         {
            out = "";

            countHours = 0;
            countMin = 0;
            countSec = 0;

              temp = mDuration;

            if ( temp >= .1 )
            {
               temp = round(temp);
               
               if ( temp >= 3600 )
               {
                  countHours = (long)(temp / 3600);
                  temp %= 3600;
               }

               if ( temp > 60 )
               {
                  countMin = (long)(temp / 60);
                  temp %= 60;
               }
               
               countSec = round(temp);
            }

            out = "PT";

            if ( countHours > 0 )
            {
               out += Long.toString(countHours, 10);
               out +="H";
            }

            if ( countMin > 0 )
            {
               out += Long.toString(countMin, 10);
               out +="M";
            }

            if ( countSec > 0 )
            {
               out += countSec;
               out +="S";
            }

            break;

         }
         default:
         {
            // Do nothing
         }
      }

      return out;
   }

   /**
    * This method adds the duration value passed in (<code>iDur</code>) to the
    * duration value being held by <code>mDuration</code>.
    * 
    * @param iDur The duration value to add.
    */
   public void add(ADLDuration iDur)
   {
      mDuration += iDur.mDuration;
   }

   /**
    * This method compares to duration values.  The input duration value 
    * (<code>iDur</code> is compared against the <code>mDuration</code> value.
    * 
    * @param iDur The duration value to compare.
    * 
    * @return Returns an integer value that represents the following:
    * <ul>
    *  <li> -1 if <code>mDuration</code> is less than <code>iDur</code></li>
    *  <li> 0 if <code>mDuration</code> is equal to <code>iDur</code></li>
    *  <li> 1 if <code>mDuration</code> is greater than <code>iDur</code></li>
    *  <li> -999 if unknown</li>
    * </ul>
    */
   public int compare(ADLDuration iDur)
   {
      int relation = ADLDuration.UNKNOWN;

      if ( mDuration < iDur.mDuration )
      {
         relation = ADLDuration.LT;
      }
      else if ( mDuration == iDur.mDuration )
      {
         relation = ADLDuration.EQ;
      }
      else if ( mDuration > iDur.mDuration )
      {
         relation = ADLDuration.GT;
      }

      return relation;
   }
}  // end ADLDuration
