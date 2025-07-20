package view;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.jdbc.UtilJDBC;
import view.util.Alert;
import view.util.Tools;

public class SettingsController implements Initializable {

    private final UtilJDBC utilJDBC = new UtilJDBC();
    
    @FXML
    private ComboBox<String> cbCategory;

    @FXML
    private TextField txtAddCategory;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addCategoryComboBox();
    }  
    
    //Category
    public void onBtAddCategory() {
        if (txtAddCategory.getText().equals("")) {
          txtAddCategory.setStyle("-fx-border-color: red;");
        } else {
            txtAddCategory.setStyle("-fx-border-color: #000;");
            String category = txtAddCategory.getText().toLowerCase();
            utilJDBC.insert(category);
            addCategoryComboBox();
        }
    }
     
    public void onBtRemoveCategory() {
        if (!cbCategory.getValue().equals("Categorias")) {
            Optional<ButtonType> result = Alert.showConfirmation("Confirmação", "Tem certeza de que deseja excluir?");
            if (result.get() == ButtonType.OK) {
                utilJDBC.deleteById(cbCategory.getValue().split("-")[0]);
                addCategoryComboBox();
            }
        } else {
            Alert.showAlert("Info", "", "Selecione uma categoria.", javafx.scene.control.Alert.AlertType.INFORMATION);
        }
    }
    
    private void addCategoryComboBox() {
        cbCategory.getItems().clear();
        
        for (String s: Tools.getCategory()) {
            cbCategory.getItems().add(s);
        }
    } 
}
