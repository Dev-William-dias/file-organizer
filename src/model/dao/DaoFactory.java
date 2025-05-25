package model.dao;

import config.Db;
import model.jdbc.DocumentJDBC;

public class DaoFactory {
    
    public static DocumentDao createDocumentDao() {
        return new DocumentJDBC(Db.getConnection());
    }
}
