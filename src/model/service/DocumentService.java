package model.service;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.DocumentDao;
import model.entities.Document;

public class DocumentService {
    
    private DocumentDao dao = DaoFactory.createDocumentDao();
    
    public byte[] findByFileId(int id) {
        return dao.findByFileId(id);
    }
    
    public List<Document> findAllFileDate() {
        return dao.findAllFileDate();
    }
    
    public void saveOrUpdate(Document obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }
    
    public void deleteById(int id) {
        dao.deleteById(id);
    }
    
}
