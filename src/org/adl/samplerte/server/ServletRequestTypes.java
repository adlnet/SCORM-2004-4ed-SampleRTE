
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

package org.adl.samplerte.server;

import java.io.Serializable;

/**
 * Enumeration of abstract servlet request types.<br><br>
 * 
 * <strong>Filename:</strong> ServletRequestTypes.java<br><br>
 * 
 * <strong>References:</strong><br>
 * <ul>
 *     <li>SCORM 2004 4th Edition
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class ServletRequestTypes implements Serializable
{

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>GET_COURSES
    * <br><b>1</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int GET_COURSES       =  1;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>GET_SCOS
    * <br><b>2</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int GET_SCOS          =  2;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>GET_COMMENTS
    * <br><b>3</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int GET_COMMENTS      =  3;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>UPDATE_SCO
    * <br><b>4</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int UPDATE_SCO        =  4;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>GET_USERS
    * <br><b>5</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int GET_USERS         =  5;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>GET_PREF
    * <br><b>6</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int GET_PREF          =  6;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>UPDATE_PREF
    * <br><b>7</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int UPDATE_PREF       =  7;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>ADD_USERS
    * <br><b>8</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int ADD_USERS         =  8;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>DELETE_USERS
    * <br><b>9</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int DELETE_USERS      =  9;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>DELETE_COURSE
    * <br><b>10</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int DELETE_COURSE     =  10;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>REG_COURSE
    * <br><b>11</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int GO_HOME        =  11;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>PROC_REG_COURSE
    * <br><b>12</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int PROC_REG_COURSE   =  12;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>PROC_UNREG_COURSE
    * <br><b>120</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int PROC_UNREG_COURSE   =  120;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>VIEW_REG_COURSE
    * <br><b>13</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int VIEW_REG_COURSE   =  13;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>SELECT_MY_COURSE
    * <br><b>14</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int SELECT_MY_COURSE  =  14;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>VIEW_MY_STATUS
    * <br><b>15</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int VIEW_MY_STATUS    =  15;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>CLEAR_DB
    * <br><b>16</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int CLEAR_DB          =  16;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>NEW_OBJ
    * <br><b>17</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int NEW_OBJ           =  17;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>SELECT_OBJ_USER
    * <br><b>18</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int SELECT_OBJ_USER          =  18;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>ADD_OBJ
    * <br><b>19</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int ADD_OBJ           =  19;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>COURSE_OBJ
    * <br><b>20</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int COURSE_OBJ        =  20;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>OBJ_ADMIN
    * <br><b>21</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int OBJ_ADMIN         =  21;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>EDIT_OBJ
    * <br><b>22</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int EDIT_OBJ          =  22;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>IMPORT_COURSE
    * <br><b>23</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int IMPORT_COURSE     =  23;

    /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>NEW_USER
    * <br><b>24</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int NEW_USER          =  24;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>NEW_SIGN_UP
    * <br><b>25</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int NEW_SIGN_UP          =  25;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>LOG_IN
    * <br><b>26</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int LOG_IN          =  26;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>CHANGE_PASSWORD
    * <br><b>27</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int CHANGE_PASSWORD          =  27;

   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>IMPORT_MULTIPLE_COURSES
    * <br><b>29</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int IMPORT_MULTIPLE_COURSES     =  29;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>PROC_REG_DELETE
    * <br><b>30</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int PROC_REG_DELETE     =  30;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>PROC_UNREG_DELETE
    * <br><b>31</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int PROC_UNREG_DELETE     =  31;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>PROC_RESET_COURSE
    * <br><b>32</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int PROC_RESET_COURSE     =  32;
   
   /**
    * Enumeration of possible Servlet Request Types.<br>
    * <br>PROC_SORT_COURSE
    * <br><b>33</b>
    * <br><br>[SERVLET REQUEST TYPE CONSTANT]
    */
   public static final int PROC_SORT_COURSE     =  33;
   
   public static final int CREATE_NEW_COURSE = 40;
   public static final int UPDATE_EXT_COURSE = 41;
   public static final int ADD_EXT_ITEM = 42;
   public static final int UPDATE_EXT_ITEM = 43;
   public static final int EXT_COURSE_DETAILS = 44;
   public static final int UPDATE_EXT_COURSE_STATUS = 45;
   public static final int SET_LRS_INFO = 46;
   public static final int GET_EDIT_COURSES = 47;
   public static final int PUBLISH_EXT_ITEM = 49;
   public static final int UNPUBLISH_EXT_ITEM = 50;

} // end ServletRequestTypes