package model.entities;

public class Document {
    
    private Integer id;
    private String name;
    private String category;
    private String description;
    private byte[] file;
    private Integer numberPages;
    private Double fileSize;
    
    public Document() {
    }

    public Document(Integer id, String name, String category, String description, byte[] file, Integer numberPages, Double fileSize) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.file = file;
        this.numberPages = numberPages;
        this.fileSize = fileSize;
    }
    
    public Document(Integer id, String name, String category, String description, Integer numberPages, Double fileSize) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.numberPages = numberPages;
        this.fileSize = fileSize;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Integer getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(Integer numberPages) {
        this.numberPages = numberPages;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }
    
}
