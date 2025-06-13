package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.entities.Document;
import view.util.PdfTools;

public class ShowFileController implements Initializable {

    private int page = 0;
    private Document document;
    private byte[] pdfByte;
    
    @FXML
    private AnchorPane rootPanel;

    @FXML
    private ImageView backgroundImage;
    
    @FXML
    private ImageView showPage;  
    
    @FXML
    private ScrollPane scrollPaneView;
    
    @FXML
    private Label labelPageNumber;
 
    public void setDocument(Document document, byte[] pdfByte) {
        this.document = document;
        this.pdfByte = pdfByte;
        
        try {
            showFile();
        } catch (Exception e) {
            System.err.println("Erro ao carregar documento: " + e.getMessage());
        }
    }

    public void onBtShowFile() {
        showFile();
    }

    public void onBtNextPage() {
        page += 1;
        showFile();
    }

    public void onBtPreviousPage() {
        if (page > -0) {
            page -= 1;
        }  
        showFile();
    }

    
    public void onBtAdjust() {
        showPage.setFitWidth(scrollPaneView.getWidth());
        showPage.setFitHeight(scrollPaneView.getHeight());
    }
            
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializerNodes();
    }

    private void showFile() {
        if (page >= 0 && page < document.getNumberPages()) {
            Image image = PdfTools.renderFirstPage(page, pdfByte);
            showPage.setImage(image);
            showPage.setPreserveRatio(false);
            
            labelPageNumber.setText((page+1)+"/"+document.getNumberPages());
     
            showPage.setFitHeight(800);
        }
    }

    private void initializerNodes() {
        Image image = new Image("view/imgs/old-books.jpg");
        backgroundImage.setImage(image);
        backgroundImage.setPreserveRatio(false);
        backgroundImage.fitWidthProperty().bind(rootPanel.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPanel.heightProperty());
    }
}
