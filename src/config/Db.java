package config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import view.util.Tools;

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
                    Tools.log("Db getConnection: "+e.getMessage());
                }
            } else {
                return conn;
            }
        } else {
            Tools.log("Banco de dados nao encontrado");
        }
        return null;
    }
    
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            Tools.log("Db closeConnection: "+e.getMessage());
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                Tools.log("Db closeStatement: "+e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Tools.log("Db closeResultSet: "+e.getMessage());
            }
        }
    }
}
