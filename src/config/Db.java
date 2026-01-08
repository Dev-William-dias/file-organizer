package config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Db {
    
    private static Connection conn = null;
    
    public static Connection getConnection() {
        File fileDb = new File("ListFile.sqlite");

        if (fileDb.exists()) {
            if (conn == null) {
                try {
                    conn = DriverManager.getConnection("jdbc:sqlite:ListFile.sqlite");
                    return conn;
                } catch (SQLException e) {
                    System.err.println("Error the connection: "+e);
                }
            } else {
                return conn;
            }
        } else {
            System.out.println("error");
            System.exit(0);
        }
        return null;
    }
    
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Alert error when closing: "+ e);
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                System.err.println("Alert error when closing: "+ e);
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Alert error when closing: "+ e);
            }
        }
    }
}
