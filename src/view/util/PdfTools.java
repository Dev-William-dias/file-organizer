package view.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfTools {

    public static int getPageCounter(File file) {
        try {
            int counter;
            try (PDDocument document = PDDocument.load(file)) {
                counter = document.getNumberOfPages();
            }
            return counter;
        } catch (IOException e) {
            Tools.log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static File loadFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar arquivo");

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf");
        
        fileChooser.getExtensionFilters().add(filter);

        return fileChooser.showOpenDialog(stage);
    }

    public static boolean existFile(String name) {
        Path base = Path.of("").toAbsolutePath().resolve("AllFiles").resolve(name+".pdf");
        return Files.exists(base);
    }
    
    public static boolean saveFile(File file) {
        try {
            Path base = Path.of("").toAbsolutePath().resolve("AllFiles");

            Path dest = base.resolve(file.getName());
            
            Files.copy(file.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            
            return true;
        } catch (IOException e) {
            Tools.log(e.getMessage());
            return false;
        }
    }
    
    
    public static void getFile(String filePath, Stage stage) {
        try {
            Path origem = Path.of("").toAbsolutePath().resolve(filePath);

            if (!Files.exists(origem)) {
                Alert.showAlert("Erro", "", "Arquivo não encontrado!", javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar arquivo");
            fileChooser.setInitialFileName(origem.getFileName().toString());
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {

                Files.copy(origem, file.toPath());

                Alert.showAlert("Info", "", "Salvo com Sucesso!", javafx.scene.control.Alert.AlertType.INFORMATION);
            }
        } catch (IOException e) {
            Tools.log(e.getMessage());
        }
    }

    public static boolean deleteFile(String filePath) {
        try {
            Path path = Path.of("").toAbsolutePath();

            Path file = path.resolve(filePath);

            if (!Files.exists(file)) {
                return false;
            }

            Files.delete(file);
            return true;
        } catch (IOException e) {
            Tools.log(e.getMessage());
            return false;
        }
    }
}
