
<%
   /***************************************************************************
   **
   ** Filename:  importUtil.jsp
   **
   ** File Description:   This file implements utility classes for   
   **                     importCourse.jsp.
   **
   **
   **
   **
   ** Author: ADL Technical Team
   **
   ** Contract Number:
   ** Company Name: CTC
   **
   ** Module/Package Name:
   ** Module/Package Description:
   **
   ** Design Issues:
   **
   ** Implementation Issues:
   ** Known Problems:
   ** Side Effects:
   **
   ** References: ADL SCORM
   **
   /***************************************************************************
      
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

   ***************************************************************************/
%>


<%!
   /***************************************************************************
   ** Method:  replace
   ** 
   ** Description:
   **    This method will perform a search and replace on the input string and
   **    return the resulting string.
   ** 
   ** @param ioString - The string to be formatted.
   ** @param iFrom - The string to search for.
   ** @param iTo - The newly formatted string.
   ** 
   ** @return - The newly formatted string.
   ***************************************************************************/
   private String replace( String ioString, String iFrom, String iTo )
   {
      int startPos = 0;
      int indexPos = -1;
      String tempString;

      while ( (indexPos = ioString.indexOf( iFrom, startPos)) != -1 )
      {
         tempString = ioString.substring(0, indexPos);
         tempString += iTo;
         tempString += ioString.substring(indexPos + iFrom.length());
         ioString = tempString;
         startPos = indexPos + iTo.length();
      }

      return ioString;
   }

   /***************************************************************************
   ** Method:  makeReadyForPrint
   ** 
   ** Description:
   **    This method will call the replace method on the input string to replace
   **    special characters that cannot appear in html.
   **    Note that not all special characters are being handled, only those
   **    that the validating XML parser is know to use in its error messages.
   ** 
   ** @param ioString - The String to be formatted.
   ** 
   ** @return String - The newly formatted string.
   ***************************************************************************/
   private String makeReadyForPrint( String ioString )
   {
      String tempString;

      // call replace to deal with special characters
      tempString = this.replace( ioString, "&", "&amp;" );
      tempString = this.replace( tempString, "\"", "&quot;" );
      tempString = this.replace( tempString, "<", "&lt;" );
      tempString = this.replace( tempString, ">", "&gt;" );
      tempString = this.replace( tempString, "[", "&#91;" );
      tempString = this.replace( tempString, "]", "&#93;" );
      tempString = this.replace( tempString, "\'", "&#39;" );
      tempString = this.replace( tempString, "\\", "\\\\" );

      return tempString;
   }
%>

