package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Document;
import model.service.DocumentService;
import view.listeners.DataChangeListener;
import view.util.PdfTools;
import view.util.Tools;

public class AddEditController implements Initializable {

    private final List<DataChangeListener> DataChangeListeners = new ArrayList<>();

    private File file = null;
    private DocumentService service;
    private Stage stage;
    private Document document;

    private double fileSize;
    private int pageCounter;

    //add data
    @FXML
    private ComboBox<String> cbFileCategories;

    @FXML
    private Label labelAlert;

    @FXML
    private TextField txtFileName;

    @FXML
    private TextArea txtaFileDescription;

    @FXML
    private Label labelEditCategories;

    @FXML
    private TextField txtEditName;

    @FXML
    private TextArea txtaEditDescription;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDatas(DocumentService service, Document document) {
        this.service = service;
        this.document = document;
        if (document != null) {
            txtEditName.setText(document.getName());
            labelEditCategories.setText("Categoria Atual: " + document.getCategory());
            txtaEditDescription.setText(document.getDescription());
        }
    }

    public void onBtLoadFile() {
        file = PdfTools.loadFile(stage);
        if (file != null) {
            fileSize = file.length();
            txtFileName.setText(file.getName().replaceFirst("\\.pdf$", ""));
            pageCounter = PdfTools.getPageCounter(file);
            labelAlert.setText("Arquivo carregado");
        }
    }

    public void onBtSave(ActionEvent event) {
        if (txtFileName.getText().equals("") || file == null || cbFileCategories.getValue() == null || cbFileCategories.getValue() == "Categoria") {
            labelAlert.setText("Campo não preenchido.");
        } else {
            String title = txtFileName.getText().toLowerCase();
            String categories = cbFileCategories.getValue();
            String description = txtaFileDescription.getText().toLowerCase();
            Document obj = new Document(null, title, categories, description, pageCounter, fileSize);

            if (!PdfTools.existFile(title)) {
                if (PdfTools.saveFile(file)) {
                    service.saveOrUpdate(obj);
                    notifyDataChangeListeners();
                    Tools.currentStage(event).close();
                } else {
                    labelAlert.setText("Não foi possivel salvar o arquivo");
                }
            } else {
                labelAlert.setText("Arquivo já existente");
            }
        }
    }

    public void onBtEidt(ActionEvent event) {
        if (txtEditName.getText().equals("")) {
            labelAlert.setText("Erro ao Atualizar.");
        } else {
            document.setName(txtEditName.getText());
            document.setDescription(txtaEditDescription.getText());
            if (cbFileCategories.getValue() != null) {
                document.setCategory(cbFileCategories.getValue());
            }

            service.saveOrUpdate(document);
            notifyDataChangeListeners();
            Tools.currentStage(event).close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addCategoryComboBox();
    }

    private void addCategoryComboBox() {
        cbFileCategories.getItems().clear();

        for (String category : Tools.getCategory()) {
            cbFileCategories.getItems().add(category);
        }
    }

    public void subscriberDataChangeListener(DataChangeListener listener) {
        DataChangeListeners.add(listener);
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : DataChangeListeners) {
            listener.onDataChanged();
        }
    }
}
