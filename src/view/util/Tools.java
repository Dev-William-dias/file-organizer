package view.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.jdbc.UtilJDBC;

public class Tools {

    public static List<String> getCategory() {
        UtilJDBC utilJDBC = new UtilJDBC();
        List<String> list = utilJDBC.findAllCategory();
        return list;
    }
    
    public static int getNumberFiles() {
       UtilJDBC utilJDBC = new UtilJDBC();
       return utilJDBC.numberFiles();
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
    
    public static void log(String error) {
        File fileLog = new File("fileLog.txt");  
        try {
            FileWriter saveLog = new FileWriter(fileLog, true);
            String data = java.time.LocalDateTime.now().toString();
            saveLog.write("\n" + data + ": "+ error);
            saveLog.close();
        } catch (IOException e) {
            throw new RuntimeException("Error creating log file. "+e.getMessage());
        }
    }
}
