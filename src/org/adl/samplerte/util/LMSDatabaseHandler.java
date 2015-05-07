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

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.adl.util.debug.DebugIndicator;


/**
 * <strong>Filename:</strong> LMSDatabaseHandler.java<br><br>
 *
 * <strong>Description:</strong><br>
 * The <code>LMSDatabaseHandler</code> provides database connection and utility
 *
 * <br><br>
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
 *     <li>IMS SS Specification</li>
 *     <li>SCORM 2004</li>
 * </ul>
 *
 * @author ADL Technical Team
 */
public class LMSDatabaseHandler
{
    /**
     * the data source for the global objectives database
     */
    public static final String GLOBAL_OBJECTIVES = "jdbc/GlobalObjectives";
    
    /**
     * the data source for the global objectives database
     */
    public static final String SRTE_DATASOURCE = "jdbc/SampleRTE";
    
    /**
     * This controls display of log messages to the java console
     */
    private static boolean _Debug = DebugIndicator.ON;

    /**
     * Default Constructor
     */
    public LMSDatabaseHandler()
    {
      /* default constructor*/
    }


    /**
     * Initializes the database connection.
     *
     *
     * @return  Returns a database connection.
     */
    public static Connection getConnection()
    {

        Connection conn = null;

        try
        {
            if ( _Debug )
            {
                System.out.println("  ::--> Connecting to the DB");
            }
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup(SRTE_DATASOURCE);
            conn = ds.getConnection();
        }
        catch ( SQLException ex )
        {
            System.out.println(" database handler sql exception " +  ex.getSQLState());
            ex.printStackTrace();
        }
        catch ( Exception e )
        {
            System.out.println("  LMSDatabaseHandler exception");
            e.printStackTrace();
        }

        return conn;
    }
    
    public static Connection getConnection(String datasource)
    {

        Connection conn = null;

        try
        {
            if ( _Debug )
            {
                System.out.println("  ::--> Connecting to the DB");
            }
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup(datasource);
            conn = ds.getConnection();
        }
        catch ( SQLException ex )
        {
            System.out.println(" database handler sql exception " +  ex.getSQLState());
            ex.printStackTrace();
        }
        catch ( Exception e )
        {
            System.out.println("  LMSDatabaseHandler exception");
            e.printStackTrace();
        }

        return conn;
    }
}