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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.adl.util.debug.DebugIndicator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


/**
 * <strong>Filename:</strong> LMSDBHandler.java<br><br>
 * 
 * <strong>Description:</strong><br>
 * Provides database connection utility functions to the shared global
 * objective DB.<br><br> 
 * 
 * <strong>Design Issues:</strong><br>
 * This implementation is intended to be used by the 
 * SCORM 2004 4th Edition Sample RTE 1.1.1. <br>
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
 *     <li>IMS SS Specification
 *     <li>SCORM 2004
 * </ul>
 * 
 * @author ADL Technical Team
 */
public class LMSDBHandler 
{

    /**
    * This controls display of log messages to the java console
    */
    private static boolean _Debug = DebugIndicator.ON;
   
   
    /**
    * Connection to the DB.
    */
    private static Connection conn = null;

    /**
    * Default Constructor
    *
    */
    public LMSDBHandler()
    {
        LMSDBHandler.getConnection();
    }

/**
* Initializes the database connection.
*
* @return A connection to the DB or <code>null</code> if the connection can
*         not be established.
*/
public static Connection getConnection()
{
    if ( conn == null )
    {
        try
        {
            if ( _Debug ) {
                System.out.println("  ::--> Connecting to the Obj DB");
            }
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/GlobalObjectives");
            conn = ds.getConnection();
            if ( _Debug )
            {
                System.out.println("  ::--> Connection successful");
            }
        }
        catch ( SQLException ex )
        {
            if ( _Debug )
            {
                System.out.println("  ::--> ERROR:  Could not connect to " + "Obj DB");
                System.out.println("  ::-->  " + ex.getSQLState());
            }
            ex.printStackTrace();
        }
        catch ( Exception e )
        {
            if ( _Debug )
            {
                System.out.println("  ::--> ERROR:  Unexpected exception");
            }
            e.printStackTrace();
        }
    }

    return conn;
}


    /**
    * Closes the connection to the global objectives DB.
    */
    public static void closeConnection()
    {
        try{

            if ( ( conn != null) && ( ! conn.isClosed() )   )
            {
                try
                {
                    if ( _Debug )
                    {
                        System.out.println("  ::--> Closing Obj DB connection.");
                    }

                    //conn.commit();
                    conn.close();
                }
                catch ( SQLException ex )
                {
                    if ( _Debug )
                {
                    System.out.println("  ::--> ERROR:  Could not close Obj DB");
                    System.out.println("  ::-->  " + ex.getSQLState());
                }
                    ex.printStackTrace();
                }
            }

            conn = null;
        }
        catch ( SQLException ex ) {
            if ( _Debug )
            {
                System.out.println("  ::--> ERROR: in conn.IsClosed Obj DB ");
                System.out.println("  ::-->  " + ex.getSQLState());
            }
            ex.printStackTrace();
        }
    }

}  // LMSDBHandler
