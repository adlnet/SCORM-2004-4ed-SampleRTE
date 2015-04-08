import java.lang.String;
import java.lang.System;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

// Create dbs and tables
public class SQLiteJDBC{
    public static void main(String args[]){
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./databases/SampleRTE.db");
            stmt = c.createStatement();

            ArrayList<String> sqlStuff1 = new ArrayList<String>();
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS ApplicationData(dataName CHAR(50) PRIMARY KEY,textValue CHAR(50),numberValue BIGINT);");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS CourseInfo(CourseID CHAR(50) PRIMARY KEY,CourseTitle CHAR(200),ImportDateTime CHAR(50),Active INTEGER CHECK(Active IN (0,1)),Start INTEGER CHECK(Start IN (0,1)),TOC INTEGER CHECK(TOC IN (0,1)));");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS ItemInfo(ActivityID INTEGER PRIMARY KEY AUTOINCREMENT,CourseID CHAR(255),OrganizationIdentifier CHAR(255),ItemIdentifier CHAR(255),ResourceIdentifier CHAR(255),Launch LONGTEXT,Type CHAR(50),Title CHAR(255),ParameterString LONGTEXT,DataFromLMS LONGTEXT,MinNormalizedMeasure CHAR(50),AttemptAbsoluteDurationLimit CHAR(255),TimeLimitAction CHAR(255),CompletionThreshold CHAR(255),Next INTEGER CHECK(Next IN (0,1)),Previous INTEGER CHECK(Previous IN (0,1)),Exit INTEGER CHECK(Exit IN (0,1)),ExitAll INTEGER CHECK(ExitAll IN (0,1)),Abandon INTEGER CHECK(Abandon IN (0,1)),Suspend INTEGER CHECK(Suspend IN (0,1)));");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS SCOComments(CommentID INTEGER PRIMARY KEY AUTOINCREMENT,ActivityID BIGINT NOT NULL,Comment LONGTEXT,CommentDateTime CHAR(50),CommentLocation LONGTEXT);");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS UserCourseInfo(UserID CHAR(50),CourseID CHAR(50),SuspendAll INTEGER CHECK(SuspendAll IN (0,1)));");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS UserInfo(UserId CHAR(50) PRIMARY KEY,LastName CHAR(50),FirstName CHAR(50),Admin INTEGER CHECK(Admin IN (0,1)),Password CHAR(102),Active INTEGER CHECK(Active IN (0,1)),AudioLevel CHAR(50),AudioCaptioning BIGINT DEFAULT 0,DeliverySpeed CHAR(50),Language CHAR(50) DEFAULT '\"\"');");
            sqlStuff1.add("INSERT OR IGNORE INTO UserInfo VALUES ('admin', 'Admin', 'Joe', 1, 'admin', 1, '1.0', 0, '1.0', '');");
            sqlStuff1.add("INSERT OR IGNORE INTO ApplicationData VALUES ('nextCourseID', '', 1);");
            for (String sql: sqlStuff1){
                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.close();


            c = DriverManager.getConnection("jdbc:sqlite:./databases/GlobalObjectives.db");
            stmt = c.createStatement();
            ArrayList<String> sqlStuff2 = new ArrayList<String>();
            sqlStuff2.add("CREATE TABLE IF NOT EXISTS CourseStatus(courseID CHAR(255),learnerID CHAR(255),satisfied CHAR(50) DEFAULT 'unknown',measure CHAR(50) DEFAULT 'unknown',completed CHAR(50) DEFAULT 'unknown',progmeasure CHAR(50) DEFAULT 'unknown',PRIMARY KEY (courseID, learnerID));");
            sqlStuff2.add("CREATE TABLE IF NOT EXISTS Objectives(objID LONGTEXT NOT NULL,learnerID CHAR(255) NOT NULL,satisfied CHAR(50),measure CHAR(50) DEFAULT 'unknown',scopeID CHAR(255),rawscore CHAR(50) DEFAULT 'unknown',minscore CHAR(50) DEFAULT 'unknown',maxscore CHAR(50) DEFAULT 'unknown',progressmeasure CHAR(50) DEFAULT 'unknown',completion CHAR(50) DEFAULT 'unknown',PRIMARY KEY (objID, learnerID, scopeID));");
            for (String sql: sqlStuff2){
                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.close();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created tables successfully");
    }
}