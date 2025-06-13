package view.util;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.jdbc.CategoryJDBC;

public class Tools {

    
    public static List<String> getCategory() {
        CategoryJDBC categoryJDBC = new CategoryJDBC();
        List<String> list = categoryJDBC.findAllCategory();
        return list;
    }
    
    public static Stage currentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static String convertionSize(Double bytes) {
        if (bytes >= 1024 * 1024) {
            return String.format("%.2f MB", (double) bytes / (1024 * 1024));
        } else if (bytes >= 1024) {
            return String.format("%.2f KB", (double) bytes / 1024);
        } else {
            return bytes + " Bytes";
        }
    }
}
