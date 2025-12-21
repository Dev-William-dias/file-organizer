package controller;

import config.Connection;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import view.util.Tools;

public class SynchronizeController implements Initializable {

    private Connection conn;

    private Task<Void> clientTask;
    private Task<Void> serverTask;

    private boolean clientRunning = false;
    private boolean serverRunning = false;

    @FXML
    private Button btnConn;

    @FXML
    private Button btnStart;

    @FXML
    private Label labelAlert;

    @FXML
    private TextField txtDoor;

    @FXML
    private TextField txtMyDoor;

    @FXML
    private TextField txtMyIp;

    @FXML
    private TextField txtServerIp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtMyIp.setText(myLocalHost());
    }

    public void onBtnStartConnection() {
        if (txtDoor.getText().equals("")) {
            labelAlert.setText("Digite a porta. Exemplo: 5000");
        } else if (txtServerIp.getText().equals("")) {
            labelAlert.setText("Digite o Ip. Exemplo: 000.000.0.0");
        } else {
            btnConn.setText("Cancelar");

            if (conn != null) {
                conn.close();
            }

            conn = new Connection();

            String ip = txtServerIp.getText();
            int door = Integer.parseInt(txtDoor.getText());

            clientTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    updateMessage("Conectando...");      
                    conn.connectAsClient(ip, door);
                    
                    return null;
                }
            };

            labelAlert.textProperty().bind(clientTask.messageProperty());

            clientTask.setOnFailed(e -> {
                labelAlert.textProperty().unbind();
                labelAlert.setText("Erro: " + clientTask.getException().getMessage());
                clientRunning = false;
                btnStart.setDisable(false);
            });

            clientTask.setOnSucceeded(e -> {
                labelAlert.textProperty().unbind();
                labelAlert.setText("Conectado com sucesso!");
            });

            if (clientRunning) {

                btnStart.setText("Iniciar");
                btnConn.setText("Conectar");
                labelAlert.textProperty().unbind();

                clientTask.cancel();

                if (conn.close()) {
                    labelAlert.setText("Conexão finalizada.");
                } else {
                    labelAlert.setText("Erro ao finalizar a Conexão.");
                }

                clientRunning = false;
                btnStart.setDisable(false);
            } else {
                clientRunning = true;
                btnStart.setDisable(true);
                new Thread(clientTask, "Client-Connection-Thread").start();
            }
        }
    }

    public void onBtnReceiveConnection() {
        if (txtMyDoor.getText().equals("")) {
            labelAlert.setText("Escolha uma porta. Exemplo: 5000");
        } else {
            btnStart.setText("Cancelar");

            if (conn != null) {
                conn.close();
            }

            conn = new Connection();

            int door = Integer.parseInt(txtMyDoor.getText());

            serverTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    updateMessage("Aguardando conexão...");

                    conn.waitForClient(door);

                    
                    if (isCancelled()) {
                        return null;
                    }

                    return null;
                }
            };

            labelAlert.textProperty().bind(serverTask.messageProperty());

            serverTask.setOnFailed(e -> {
                labelAlert.textProperty().unbind();
                labelAlert.setText("Erro: " + serverTask.getException().getMessage());
                serverRunning = false;
                btnStart.setDisable(false);
            });

            serverTask.setOnSucceeded(e -> {
                labelAlert.textProperty().unbind();
                labelAlert.setText("Cliente conectado!");
            });

            if (serverRunning) {

                btnConn.setText("Conectar");
                btnStart.setText("Iniciar");
                labelAlert.textProperty().unbind();

                serverTask.cancel();

                if (conn.close()) {
                    labelAlert.setText("Conexão finalizada.");
                } else {
                    labelAlert.setText("Erro ao finalizar a Conexão.");
                }

                serverRunning = false;
                btnConn.setDisable(false);
            } else {
                serverRunning = true;
                btnConn.setDisable(true);
                new Thread(serverTask, "Server-wait-Thread").start();
            }
        }

    }

    private String myLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            Tools.log(e.getMessage());
            return "Erro";
        }
    }

}
