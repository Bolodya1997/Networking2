package ru.nsu.fit.bolodya;

import ru.nsu.fit.bolodya.networking.client.Client;
import ru.nsu.fit.bolodya.networking.server.Server;

import java.io.IOException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                new Server(2048);
            }
            catch (IOException e) {
                e.printStackTrace();
                exit(-1);
            }
        }).start();

        try {
            Thread.sleep(100);
        }
        catch (InterruptedException ignored) {}

        new Thread(() -> {
            try {
                new Client(2048, "127.0.0.1");
            }
            catch (IOException e) {
                e.printStackTrace();
                exit(-1);
            }
        }).start();
    }
}
