package ru.nsu.fit.bolodya.networking.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Server {

    private Selector selector;

    private ByteBuffer buffer = ByteBuffer.allocate(1024 * 5000);

    private Map<SocketChannel, Speed> speedMap = new HashMap<>();

    public Server(int port) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
        channel.configureBlocking(false);

        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);

        new View(this);

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable())
                    acceptRoutine(key);
                else
                    readRoutine(key);
                iterator.remove();
            }
        }
    }

    private void acceptRoutine(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = channel.accept();

        clientChannel.configureBlocking(false);
        speedMap.put(clientChannel, new Speed());

        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void readRoutine(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        int written = channel.read(buffer);
        buffer.clear();

        if (written < 0) {
            key.cancel();
            speedMap.remove(channel);
            channel.close();
            return;
        }

        speedMap.get(channel).put(written);
    }

    Map<String, Speed> getSpeedMap() {
        return speedMap.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> {
                    String host = null;
                    try {
                        host = entry.getKey().getRemoteAddress().toString();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    return host;
                }, (Function<Map.Entry<SocketChannel, Speed>, Speed>) Map.Entry::getValue));
    }
}
