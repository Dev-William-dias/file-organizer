package view.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PdfTools {

    public static byte[] convertToByte(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Error pdfTools: " + e);
            return null;
        }
    }

    public static Image renderFirstPage(int page,byte[] pdfByte) {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfByte))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage bi = renderer.renderImageWithDPI(page, 150);
            Image image = SwingFXUtils.toFXImage(bi, null);
            bi.flush();
            document.close();
            return image;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar a pagina.");
        }
    }

    public static int getPageCounter(File file) {
        try {
            int counter;
            try (PDDocument document = PDDocument.load(file)) {
                counter = document.getNumberOfPages();
            }
            return counter;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o numero de paginas.");
        } 
    }
    
    public static File LoadFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar arquivo");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf");
        fileChooser.getExtensionFilters().add(filter);
        return fileChooser.showOpenDialog(stage);
    }

    public static void downloadFile(byte[] pdf, String name, Stage stage) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar arquivo");
            fileChooser.setInitialFileName(name + ".pdf");
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(pdf);
                fos.close();
                Alert.showAlert("Info", "", "Salvo com Sucesso!", javafx.scene.control.Alert.AlertType.INFORMATION);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao tentar realizar o download do arquivo. " + e);
        }
    }
}
