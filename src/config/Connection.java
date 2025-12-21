package config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import view.util.Tools;

public class Connection {

    private Socket socket;
    private ServerSocket serverSocket;

    public Connection() {
    }

    public Socket connectAsClient(String ip, int port) {
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            Tools.log(e.getMessage());
            return null;
        }
        return socket;
    }

    public Socket waitForClient(int port) {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept(); // BLOQUEANTE
        } catch (IOException e) {
            Tools.log(e.getMessage());
            return null;
        }
        return socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
