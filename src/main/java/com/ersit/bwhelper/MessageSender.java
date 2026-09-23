package com.ersit.bwhelper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageSender {
    private final String host;
    private final int port;

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();

    private volatile boolean running = true;
    private Thread networkThread;

    private Socket socket;
    private PrintWriter out;

    public MessageSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        networkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                networkLoop();
            }
        }, "BWHelper-Network");
        networkThread.start();
    }

    private void networkLoop() {
        while (running) {
            if (!isConnected()) {
                try {
                    connect();

                    System.out.println(
                            "[BWHelper] Connected to " + host + ":" + port
                    );
                    this.send(Messages.connected());
                } catch (IOException e) {
                    sleep(10000);
                    continue;
                }
            }

            try {
                String message = messageQueue.take();

                if (!sendInternal(message)) {
                    messageQueue.add(message);
                }
            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }

                Thread.currentThread().interrupt();
                break;
            }
        }

        disconnect();
    }

    private void connect() throws IOException {
        Socket newSocket = new Socket();

        newSocket.connect(new java.net.InetSocketAddress(host, port), 5000);

        PrintWriter newOut =
                new PrintWriter(newSocket.getOutputStream(), true);

        socket = newSocket;
        out = newOut;
    }

    private boolean sendInternal(String message) {
        if (!isConnected()) {
            return false;
        }

        out.println(message);

        if (out.checkError()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText(EnumChatFormatting.RED + "[BWHelper] Connection lost.")
            );

            disconnect();
            return false;
        }

        return true;
    }

    public void send(String message) {
        if (!running) {
            return;
        }

        messageQueue.add(message);
    }

    public boolean isConnected() {
        return socket != null
                && socket.isConnected()
                && !socket.isClosed();
    }

    private void disconnect() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }

        socket = null;
        out = null;
    }

    public void shutdown() {
        running = false;
        disconnect();

        if (networkThread != null) {
            networkThread.interrupt();
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
