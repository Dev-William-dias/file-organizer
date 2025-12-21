package controller;

import application.Main;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
    private Document obj = null;
    private boolean toUpdate = true;

    private int offset = 0;

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
    private TextArea txtaDescription;

    @FXML
    private TextField txtSearch;

    private ObservableList<Document> obsList = FXCollections.observableArrayList();
    
    //Show Windows
    public void onBtWindowSave() {
        showWindow("/view/Add.fxml", "Adicionar novo arquivo", "add", null);
    }

    public void onBtWindowsEdit() {
        showWindow("/view/Edit.fxml", "Editar a arquivo","edit" , null);
    }

    public void onBtWindowAddCategory() {
        showWindow("/view/AddCategory.fxml", "Adicionar categorias", "category", null);
    }

    public void onBtWindowSynchronize() {
        showWindow("/view/Synchronize.fxml", "Sincronizar arquivos", "synchronize", null);
    }
    
    public void onBtSearch() {
        if (!txtSearch.getText().equals("")) {
            toUpdate = false;
            updateTable(txtSearch.getText());
        }
    }

    public void onBtnUpdate() {
        tableFiles.getItems().clear();
        offset = 0;
        toUpdate = true;
        updateData();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializerNodes();
        
        //pega os dados do objeto da tabela
        tableFiles.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selection) -> {
            if (selection != null) {
                labelNumberPage.setText("Numero de Paginas: " + selection.getNumberPages());
                labelFileSize.setText("Tamanho do Arquivo: " + Tools.convertionSize(selection.getFileSize()));
                txtaDescription.setText(" " + selection.getDescription());
                obj = selection;
            }
        });

        categories.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selection) -> {
            if (selection != null) {
                toUpdate = false;
                updateTable(selection);
            }
        });
        
        //carrega mais dados na tabela
        tableFiles.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            ScrollBar scrollBar = (ScrollBar) tableFiles.lookup(".scroll-bar:vertical");
            scrollBar.valueProperty().addListener((o, oldVal, newVal) -> {
                if (newVal.doubleValue() == scrollBar.getMax()) {
                    updateTable("");
                }
            });
        });
    }

    private void initializerNodes() {   
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        updateData();
    }

    private void updateData() {
        updateCategories();
        updateTable("");
        labelTotalFiles.setText("Total de Arquivos: " + Tools.getNumberFiles());
    }
    
    private void updateCategories() {
        Task<List<String>>  loadCategories = new Task<>() {
            @Override
            protected List<String> call() {
                return Tools.getCategory();
            }
        };
        
        loadCategories.setOnSucceeded(e -> {
            Set<String> existCategorie = new HashSet<>(categories.getItems());
            for (String categorieList : loadCategories.getValue()) {
                if (existCategorie.add(categorieList)) {
                    categories.getItems().add(categorieList);
                }
            }
        });
        
        new Thread(loadCategories).start();
    }
    
    private void updateTable(String searchFor) {
        if (toUpdate == true || !searchFor.equals("")) {
           progressView.setVisible(true); 
        }
        
        Task<List<Document>> loadDataFile = new Task<>() {
            @Override
            protected List<Document> call() {    
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                return service.findQuantityFileDate(20, offset);
            }
        };

        loadDataFile.setOnSucceeded(e -> {
            List<Document> list = loadDataFile.getValue();

            if (list != null && !list.isEmpty()) {
                obsList.addAll(list);
                offset += 20;
                tableFiles.setItems(obsList);
            }

            initBtShowFile();
            initBtDownload();
            initBtDelete();
            labelTotalFiles.setText("Total de Arquivos: " + Tools.getNumberFiles());
            progressView.setVisible(false);
        });
        
        Task<List<Document>> loadSurveyData = new Task<>() {
            @Override
            protected List<Document> call() {    
                try {
                    tableFiles.getItems().clear();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                return service.searchFiles(searchFor);
            }
        };
        
        loadSurveyData.setOnSucceeded(e -> {
            List<Document> list = loadSurveyData.getValue();
            
            if (list != null && !list.isEmpty()) {
                obsList.addAll(list);
                tableFiles.setItems(obsList);
            }
            
            initBtShowFile();
            initBtDownload();
            initBtDelete();
            progressView.setVisible(false);
        });
        
        if (searchFor.equals("")) {
            if (toUpdate) {
                new Thread(loadDataFile).start();
            } 
        } else {
            new Thread(loadSurveyData).start();
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
                button.setOnAction(event -> showWindow("/view/Show.fxml", "show", obj.getName(), obj));
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
                button.setOnAction((event) -> PdfTools.downloadFile(service.findByFileId(obj.getId()), obj.getName(), Main.getStage()));
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

    private void removeEntity(Document obj) {
        Optional<ButtonType> result = Alert.showConfirmation("Confirmação", "Tem certeza de que deseja excluir?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }
            service.deleteById(obj.getId());
        }
        updateTable("");
    }

    private void showWindow(String fileName, String title, String option, Document doc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
            AnchorPane anchorPane = loader.load();

            Object controller = loader.getController();
            boolean resizable = false;
            
            if (controller instanceof AddEditController aec) {
                if (obj != null && option.equals("edit")) {
                    aec.setDatas(service, obj);
                } else {
                    aec.setDatas(service, null);
                }
                aec.subscriberDataChangeListener(this);
            } else if (controller instanceof ShowController sfc) {
                sfc.setDocument(doc, service.findByFileId(doc.getId()));
                resizable = true;
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/imgs/icons/researchBooks.png")));
            dialogStage.setScene(new Scene(anchorPane));
            dialogStage.setResizable(resizable);
            dialogStage.initModality(Modality.NONE);
            dialogStage.show();
        } catch (IOException e) {
            Alert.showAlert("Erro", "", "Error ao mostrar arquivo.", javafx.scene.control.Alert.AlertType.WARNING);
            Tools.log(e.getMessage());
        }
    }

    @Override
    public void onDataChanged() {
        updateData();
    }
}
