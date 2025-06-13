package model.jdbc;

import config.Db;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryJDBC {
    
    private java.sql.Connection conn;
    
    public CategoryJDBC () {
        if (conn == null) {
            conn = Db.getConnection();
        }
    }
    
    public void insert(String category) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO category(name) VALUES (?)");
            st.setString(1,category);

            st.execute();
        } catch (SQLException e) {
            System.err.println("Error JDBC: "+e);
        } finally {
            Db.closeStatement(st);
        }
    }
    
    public void deleteById(String id) {
        PreparedStatement st = null;
        
        try {
            st = conn.prepareStatement("DELETE FROM category WHERE id = ?");
            st.setString(1, id);
            st.execute();
        } catch (SQLException e) {
            System.err.println("Error JDBC: "+e);
        } finally {
            Db.closeStatement(st);
        }
    }
    
     public List<String> findAllCategory() {
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            st = conn.prepareStatement("SELECT id, name FROM category");
            
            rs =  st.executeQuery();
            List<String> objList = new ArrayList<>();
            while(rs.next()) {
                String categoryList = rs.getInt("id")+" - "+ rs.getString("name");
                objList.add(categoryList);
            }
            return objList;
        } catch (SQLException e) {
            System.err.println("Error JDBC: "+e);
        } finally {
            Db.closeResultSet(rs);
            Db.closeStatement(st);
        }
        return null;
    }
    
}
