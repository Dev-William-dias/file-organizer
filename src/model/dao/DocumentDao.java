package model.dao;

import java.util.List;
import model.entities.Document;

public interface DocumentDao {
    
    void insert(Document obj);
    void update(Document obj);
    void deleteById(Integer id);
    String findByFileId(Integer id);
    List<Document> findAllFileDate();
    List<Document> findQuantityFileDate(int quantity, int offset);
    List<Document> searchFiles(String searchFor);
    
}
