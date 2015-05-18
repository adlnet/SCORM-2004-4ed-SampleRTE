package org.adl.databases;

import java.lang.String;
import java.lang.System;
import java.sql.*;
import java.util.ArrayList;

import org.adl.samplerte.server.PasswordHash;
import org.adl.samplerte.util.Config;

// Create dbs and tables
public class SQLiteJDBC{
    public static void main(String args[]){
       Config.getProp("foo");
        Connection c = null;
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:./databases/SampleRTE.db");
            stmt = c.createStatement();
            
            String admin = Config.getProp("rte.admin.username");
            String pass = Config.getProp("rte.admin.password");
            String adminFName = Config.getProp("rte.admin.first.name");
            String adminLName = Config.getProp("rte.admin.last.name");
            
            ArrayList<String> sqlStuff1 = new ArrayList<String>();
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS ApplicationData(dataName CHAR(50) PRIMARY KEY,textValue CHAR(50),numberValue BIGINT);");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS CourseInfo(CourseID CHAR(255) PRIMARY KEY,CourseTitle CHAR(200),ImportDateTime CHAR(50),Active INTEGER CHECK(Active IN (0,1)) DEFAULT 0,Start INTEGER CHECK(Start IN (0,1)),TOC INTEGER CHECK(TOC IN (0,1)));");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS ItemInfo(ActivityID INTEGER PRIMARY KEY AUTOINCREMENT,CourseID CHAR(255),OrganizationIdentifier CHAR(255),ItemIdentifier CHAR(255),ResourceIdentifier CHAR(255),Launch LONGTEXT,Type CHAR(50),Title CHAR(255),ParameterString LONGTEXT, ItemOrder INTEGER,DataFromLMS LONGTEXT,MinNormalizedMeasure CHAR(50),AttemptAbsoluteDurationLimit CHAR(255),TimeLimitAction CHAR(255),CompletionThreshold CHAR(255),Next INTEGER CHECK(Next IN (0,1)),Previous INTEGER CHECK(Previous IN (0,1)),Exit INTEGER CHECK(Exit IN (0,1)),ExitAll INTEGER CHECK(ExitAll IN (0,1)),Abandon INTEGER CHECK(Abandon IN (0,1)),Suspend INTEGER CHECK(Suspend IN (0,1)));");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS SCOComments(CommentID INTEGER PRIMARY KEY AUTOINCREMENT,ActivityID BIGINT NOT NULL,Comment LONGTEXT,CommentDateTime CHAR(50),CommentLocation LONGTEXT);");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS UserCourseInfo(UserID CHAR(50),CourseID CHAR(50),SuspendAll INTEGER CHECK(SuspendAll IN (0,1)));");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS UserInfo(UserId CHAR(50) PRIMARY KEY,LastName CHAR(50),FirstName CHAR(50),Admin INTEGER CHECK(Admin IN (0,1)),Password CHAR(102),Active INTEGER CHECK(Active IN (0,1)),AudioLevel CHAR(50),AudioCaptioning BIGINT DEFAULT 0,DeliverySpeed CHAR(50),Language CHAR(50) DEFAULT '\"\"');");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS LRSInfo(id INTEGER PRIMARY KEY AUTOINCREMENT, UserID CHAR(50), LRSName CHAR(50), Endpoint CHAR(2047), UserName CHAR(255), Password CHAR(255));");
            sqlStuff1.add("CREATE TABLE IF NOT EXISTS UserAgentInfo(id INTEGER PRIMARY KEY AUTOINCREMENT, UserID CHAR(50), AgentAlias CHAR(255), Mbox CHAR(255), HomePage CHAR(2047), AccName CHAR(255));");
            sqlStuff1.add("INSERT OR IGNORE INTO UserInfo VALUES ('"+admin+"', '"+adminLName+"', '"+adminFName+"', 1, '"+pass+"', 1, '1.0', 0, '1.0', '');");
            sqlStuff1.add("INSERT OR IGNORE INTO ApplicationData VALUES ('nextCourseID', '', 1);");
            for (String sql: sqlStuff1){
                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.close();


            c = DriverManager.getConnection("jdbc:sqlite:./databases/GlobalObjectives.db");
            stmt = c.createStatement();
            ArrayList<String> sqlStuff2 = new ArrayList<String>();
            sqlStuff2.add("CREATE TABLE IF NOT EXISTS CourseStatus(courseID CHAR(255),learnerID CHAR(255),satisfied CHAR(50) DEFAULT 'unknown',measure CHAR(50) DEFAULT 'unknown',completed CHAR(50) DEFAULT 'unknown',progmeasure CHAR(50) DEFAULT 'unknown', refStmtID CHAR(50),PRIMARY KEY (courseID, learnerID));");
            sqlStuff2.add("CREATE TABLE IF NOT EXISTS ItemStatus(activityID INTEGER, courseID CHAR(255),learnerID CHAR(255), scaled FLOAT DEFAULT 0, raw FLOAT DEFAULT 0, min FLOAT DEFAULT 0, max FLOAT DEFAULT 0, success BOOLEAN DEFAULT FALSE, completion BOOLEAN DEFAULT FALSE, response CHAR(1024), duration CHAR(512), refStmtID CHAR(50), statement CHAR(4095), PRIMARY KEY (activityID, courseID, learnerID));");
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