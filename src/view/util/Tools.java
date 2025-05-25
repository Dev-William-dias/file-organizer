package view.util;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Tools {

    private static File file = new File("category.txt");

    public static void crietListCategory() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }

    public static void addCategory(String category) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file, true));
            
            pw.println(category);
            pw.close();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }

    public static void removeCategory(String category) {
        try {   
            List<String> list = Files.readAllLines(file.toPath());
            
            for (int i = list.size() - 1; i >= 0; i--) {
                if (category.equals(list.get(i))) {
                   list.remove(i);
                }
            }
            
            Files.write(file.toPath(), list);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
    
    
    public static List<String> readListCategory() {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
        return null;
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
