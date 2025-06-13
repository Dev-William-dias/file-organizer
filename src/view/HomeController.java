package view;

import application.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Document;
import model.service.DocumentService;
import view.listeners.DataChangeListener;
import view.util.Alert;
import view.util.PdfTools;
import view.util.Tools;

public class HomeController extends DataChangeListener implements Initializable {

    private final DocumentService service = new DocumentService();
    private Stage stage;
    private Document obj = null;

    @FXML
    private TableView<Document> tableFiles;

    @FXML
    private TableColumn<Document, Integer> columnId;

    @FXML
    private TableColumn<Document, String> columnName;

    @FXML
    private TableColumn<Document, String> columnCategory;

    @FXML
    private TableColumn<Document, Document> columnBtView;

    @FXML
    private TableColumn<Document, Document> columnBtDownload;

    @FXML
    private TableColumn<Document, Document> columnBtRemover;

    @FXML
    private ComboBox<String> categories;
    
    @FXML
    private ProgressIndicator progressView;
    
    @FXML
    private Label labelTotalFiles;

    @FXML
    private Label labelNumberPage;

    @FXML
    private Label labelFileSize;

    @FXML
    private Label labelDescription;

    @FXML
    private TextField txtSearch;

    private ObservableList<Document> obsList;

    //Show Windows
    public void onBtWindowSave() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddFile.fxml"));
            AnchorPane anchorPane = loader.load();

            AddEditController controller = loader.getController();
            controller.setDatas(service,null);
            controller.subscriberDataChangeListener(this);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Adicionar novo arquivo");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/imgs/icons/researchBooks.png")));
            dialogStage.setScene(new Scene(anchorPane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alert.showAlert("Erro", "", e.toString(), javafx.scene.control.Alert.AlertType.WARNING);
        }
    }

    public void onBtWindowsEdit() {
        if (obj != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditDescription.fxml"));
                AnchorPane anchorPane = loader.load();

                AddEditController controller = loader.getController();
                controller.setDatas(service,obj);
                controller.subscriberDataChangeListener(this);
                
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Editar a arquivo");
                dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/imgs/icons/researchBooks.png")));
                dialogStage.setScene(new Scene(anchorPane));
                dialogStage.setResizable(false);
                dialogStage.initOwner(stage);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.showAndWait();
            } catch (IOException e) {
                Alert.showAlert("Erro", "", e.toString(), javafx.scene.control.Alert.AlertType.WARNING);
            }
        }
    }

    public void onBtWindowSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Configurações");
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/imgs/icons/researchBooks.png")));
            dialogStage.setScene(new Scene(anchorPane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alert.showAlert("Erro", "", e.toString(), javafx.scene.control.Alert.AlertType.WARNING);
        }
    }

    public void onBtSearch() {
        tableFiles.getItems().clear();
        progressView.setVisible(true);
        String search = txtSearch.getText();
        String categorie = categories.getValue();
        
        List<Document> documents = service.findAllFileDate();
        
        List<Document> list = new ArrayList<>();
        
        if (categorie != null && !"Categoria".equals(categorie)) {
            for (Document d : documents) {
                if (categorie.equals(d.getCategory())) {
                    list.add(d);
                }
            }
        } else if (search.isEmpty()) {
            for (Document d : documents) {
                
                if (d.getName().equals(search)) {
                    list.add(d);
                }
                
            }
        }
        
        if (!list.isEmpty()) {
            obsList = FXCollections.observableArrayList(list);
            tableFiles.setItems(obsList);
        } else {
           Alert.showAlert("Info", "", "Arquivo não emcontrado.", javafx.scene.control.Alert.AlertType.INFORMATION);
           addDataTable();
        }
        
        progressView.setVisible(false);
    }
    
    public void onBtupdateTable() {
        addDataTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializerNodes();
        stage = Main.getStage();

        tableFiles.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selection) -> {
            if (selection != null) {
                labelNumberPage.setText("Numero de Paginas: " + selection.getNumberPages());
                labelFileSize.setText("Tamanho do Arquivo: " + Tools.convertionSize(selection.getFileSize()));
                labelDescription.setText("" + selection.getDescription());
                obj = selection;
            }
        });
    }

    private void initializerNodes() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        addDataTable();
        progressView.setVisible(false);
    }

    private void addDataTable() {
        categories.getItems().clear();
        List<Document> list = service.findAllFileDate();
        labelTotalFiles.setText("Total de Arquivos: " + list.size());
        obsList = FXCollections.observableArrayList(list);
        tableFiles.setItems(obsList);
        initBtShowFile();
        initBtDownload();
        initBtDelete();
        String oldCategory = "";
        for (Document document: list) {       
            if (!oldCategory.equals(document.getCategory())) {     
                oldCategory = document.getCategory();
                categories.getItems().add(document.getCategory());
            }     
        }
    }
    
    //buttons functionsç
    private void initBtShowFile() {
        columnBtView.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        columnBtView.setCellFactory(param -> new TableCell<Document, Document>() {
            private final Button button = new Button();
            private final ImageView icon = new ImageView(new Image("/view/imgs/icons/view.png"));

            {
                icon.setFitWidth(20);
                icon.setFitHeight(20);

                button.setGraphic(icon);
                button.setPrefWidth(30);
                button.setPrefHeight(30);
            }

            @Override
            protected void updateItem(Document obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setAlignment(Pos.CENTER);
                setGraphic(button);
                button.setOnAction(event -> createDialogFormFile(obj));
            }
        });
    }

    private void initBtDownload() {
        columnBtDownload.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        columnBtDownload.setCellFactory(param -> new TableCell<Document, Document>() {
            private final Button button = new Button();
            private final ImageView icon = new ImageView(new Image("/view/imgs/icons/download.png"));

            {
                icon.setFitWidth(20);
                icon.setFitHeight(20);

                button.setGraphic(icon);
                button.setPrefWidth(30);
                button.setPrefHeight(30);
            }

            @Override
            protected void updateItem(Document obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setAlignment(Pos.CENTER);
                setGraphic(button);
                button.setOnAction((event) -> PdfTools.downloadFile(service.findByFileId(obj.getId()), obj.getName(), stage));
            }
        });
    }

    private void initBtDelete() {
        columnBtRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        columnBtRemover.setCellFactory(param -> new TableCell<Document, Document>() {
            private final Button button = new Button();
            private final ImageView icon = new ImageView(new Image("/view/imgs/icons/delete.png"));

            {
                icon.setFitWidth(20);
                icon.setFitHeight(20);

                button.setGraphic(icon);
                button.setPrefWidth(30);
                button.setPrefHeight(30);
            }

            @Override
            protected void updateItem(Document obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setAlignment(Pos.CENTER);
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void createDialogFormFile(Document obj) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowFile.fxml"));
            AnchorPane anchorPane = loader.load();

            ShowFileController controller = loader.getController();
            controller.setDocument(obj, service.findByFileId(obj.getId()));

            Stage dialogStage = new Stage();
            dialogStage.setTitle(obj.getName());
            dialogStage.setScene(new Scene(anchorPane));
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/imgs/icons/researchBooks.png")));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alert.showAlert("Erro", "", e.toString(), javafx.scene.control.Alert.AlertType.WARNING);
        }
    }

    private void removeEntity(Document obj) {
        Optional<ButtonType> result = Alert.showConfirmation("Confirmação", "Tem certeza de que deseja excluir?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }

            service.deleteById(obj.getId());
            addDataTable();
        }
    }
    
    @Override
    public void onDataChanged() {
        addDataTable();
    }
}
