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

*******************************************************************************/

package org.adl.datamodels.datatypes;

/**
 * <strong>Filename:</strong> InteractionTrunc.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * Provides support for the SCORM Data Model Interaction data types, as defined 
 * in the SCORM 2004.
 * 
 * @author ADL Technical Team
 */
public class InteractionTrunc
{
   /**
    * Truncates all parts of an interaction datatype to their SPMs
    * 
    * @param iValue The value being truncated
    * @param iType  The type of the value being truncated
    * 
    * @return Returns the Truncated value
    */
   public static String trunc(String iValue, int iType)
   {
      String trunc = new String("");

      // SCORM defined separators
      String comma  = new String("\\[,\\]");
      
      // Ensure value contains seperators before processing
      if ( iValue.equals("") || iValue.indexOf(comma) == -1 )
      {
         return iValue;
      }

      int idx = -1;

      // Switch on the interaction type
      switch ( iType )
      {
         case InteractionValidator.MULTIPLE_CHOICE :
         {
            // Check for an empty set
            if ( iValue.trim().length() == 0 )
            {
               // Value OK
               break;
            }

            String choices[] = iValue.split(comma);
            trunc = "";

            // Check to determine if each choice is within the SPM range  
            for ( int i = 0; i < 36; i++ )
            {
               if ( choices[i].length() > 250 )
               {
                  trunc = trunc + choices[i].substring(0, 250);
               }
               else
               {
                  trunc = trunc + choices[i];
               }

               if ( i != 35 )
               {
                  trunc = trunc + "[,]";
               }
            }

            break;
         }
         case InteractionValidator.FILL_IN :
         { 
            // Extract each part of the match_text
            String matchText[] = iValue.split(comma);
            trunc = "";

            for ( int i = 0; i < 10; i++ )
            {
               String matchString = null;
               String langString = null;

               // Look for the 'lang' delimiter
               if ( matchText[i].startsWith("{lang=") )
               {
                  // Find the closing '}'
                  idx = matchText[i].indexOf('}');
                  if ( idx != -1 )
                  {
                     matchString = matchText[i].substring(idx + 1);
                     langString = matchText[i].substring(6, idx);
                  }
                  else
                  {
                     matchString = matchText[i];
                  }
               }
               else
               {
                  matchString = matchText[i];
               }

               if ( langString.length() > 250)
               {
                  trunc = trunc + "{lang=" + 
                     langString.substring(0, 250) + "}";
               }
               else
               {
                  trunc = trunc + "{lang=" + 
                     langString + "}"; 
               }


               if ( matchString.length() > 250 )
               {               
                  trunc = trunc + matchString.substring(0, 250);
               }
               else
               {
                  trunc = trunc + matchString;
               }

               if ( i != 9 )
               {
                  trunc = trunc + "[,]";
               }
            }

            break;
         }
         case InteractionValidator.LONG_FILL_IN :
         {
            if ( iValue.length() > 4000 )
            {
               trunc = iValue.substring(0, 4000);
            }
            else
            {
               trunc = iValue;
            }

            break;
         }
         case InteractionValidator.LIKERT :
         {
            if ( iValue.length() > 250 )
            {
               trunc = iValue.substring(0, 250);
            }

            break;
         }
         case InteractionValidator.MATCHING :
         {
            if ( iValue.trim().length() == 0 )
            {
               // Value OK
               break;
            }

            String commas[] = iValue.split(comma);
            trunc = "";

            for ( int i = 0; i < 36; i++ )
            {
               idx = commas[i].indexOf("[.]");

               String target = commas[i].substring(0, idx);
               String source = commas[i].substring(idx + 3, 
                                                   commas[i].length());

               if ( target.length() > 250 )
               {
                  trunc = trunc + target.substring(0, 250);
               }
               else
               {
                  trunc = trunc + target;
               }

               trunc = trunc + "[.]";

               if ( source.length() > 250 )
               {
                  trunc = trunc + source.substring(0, 250);  
               }
               else
               {
                  trunc = trunc + source;
               }
                 
               if ( i != 35 )
               {
                  trunc = trunc + "[,]";
               }
            } 

            break;
         }
         case InteractionValidator.PERFORMANCE : 
         {
            String commaCheck[] = iValue.split(comma);  
            trunc = "";

            for ( int i = 0; i < 125; i++ )
            {
               idx = commaCheck[i].indexOf("[.]");

               String sn = commaCheck[i].substring(0, idx);
               String sa = commaCheck[i].substring(idx + 3, 
                              commaCheck[i].length());


               if ( sn.length() > 250 )
               {
                  trunc = trunc + sn.substring(0, 250);
               }
               else
               {
                  trunc = trunc + sn;
               }

               trunc = trunc + "[.]";

               if ( sa.length() > 250 )
               {
                  trunc = trunc + sa.substring(0, 250);  
               }
               else
               {
                  trunc = trunc + sa;
               } 

               if ( i != 124 )
               {
                  trunc = trunc + "[,]";
               }
            } 

            break;
         }
         case InteractionValidator.SEQUENCING :
         {
            String array[] = iValue.split(comma);
            trunc = "";
        
            for ( int i = 0; i < 36; i++ )
            {
 
               if ( array[i].length() > 250 )
               {
                  trunc = trunc + array[i].substring(0, 250);
               }
               else
               {
                  trunc = array[i];
               }

               if ( i != 35 )
               {
                  trunc = trunc + "[,]";
               }
            }

            break;
         }
         case InteractionValidator.NUMERIC :  
         {
            trunc = iValue;

            break;
         }
         default :
         {
            break;
         }
      }

      return trunc;
   }

} // end InteractionTrunc
