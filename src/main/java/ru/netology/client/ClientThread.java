package ru.netology.client;

import ru.netology.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ClientThread extends Thread {

    private SocketChannel socketChannel;

    public ClientThread(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
        while (!Thread.currentThread().isInterrupted())
            try {
                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                Thread.sleep(100);
                inputBuffer.clear();
            } catch (InterruptedException | IOException ex) {
                Logger.getInstance().log(ex.getMessage());
                return;
            }
    }
}

