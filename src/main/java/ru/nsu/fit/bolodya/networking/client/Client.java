package ru.nsu.fit.bolodya.networking.client;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public Client(int port, String host) throws IOException {
        Socket socket = new Socket(host, port);
        byte message[] = new byte[1024 * 1024 * 200];
        while (true) {
//            try {
//                Thread.sleep(1000);
//            }
//            catch (InterruptedException ignored) {}
            socket.getOutputStream().write(message);
        }
    }
}
