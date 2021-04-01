package ru.netology.client;

import ru.netology.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final String SETTINGS_FILE_NAME = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "Settings.txt";

    public static void main(String[] args) {
        Setting setting = getSetting(SETTINGS_FILE_NAME);
        if (setting == null) return;
        Scanner scanner = new Scanner(System.in);
        String name = getUserName(scanner);
        InetSocketAddress socketAddress = new InetSocketAddress(setting.getAddress(), setting.getPort());
        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(socketAddress);
            Logger.getInstance().log("Подключен к серверу;");
            sendName(socketChannel, name);
            Thread thread = new ClientThread(socketChannel);
            while (chatting(scanner, socketChannel));
            thread.interrupt();
        } catch (IOException ex) {
            Logger.getInstance().log(ex.getMessage());
            ex.printStackTrace();
        }
    }

    static Setting getSetting(String path) {
        StringBuilder settingsAsString = new StringBuilder();
        try (FileReader fileReader = new FileReader(path)) {
            int c;
            while ((c = fileReader.read()) != -1) {
                settingsAsString.append((char) c);
            }
            String[] settingParts = settingsAsString.toString().split(";");
            String address = settingParts[0];
            int port = Integer.parseInt(settingParts[1]);
            Logger.getInstance().log("Прочитаны настройки;");
            return new Setting(address, port);
        } catch (FileNotFoundException ex) {
            Logger.getInstance().log("Файл с настройками не обнаружен;");
        } catch (IOException ex) {
            Logger.getInstance().log(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private static void sendName(SocketChannel socketChannel, String name) throws IOException{
        String message = "<SetName>" + name;
        socketChannel.write(
                ByteBuffer.wrap(
                        message.getBytes(StandardCharsets.UTF_8)));
    }

    private static void sendMessage(SocketChannel socketChannel, String message) throws IOException{
        message = "<Message>" + message;
        socketChannel.write(
                ByteBuffer.wrap(
                        message.getBytes(StandardCharsets.UTF_8)));
    }

    private static String getUserName(Scanner scanner) {
        System.out.print("Введите Ваше имя: ");
        return scanner.nextLine();
    }

    static boolean chatting(Scanner scanner, SocketChannel socketChannel) throws IOException{
        String message;
        message = scanner.nextLine();
        if (message.equals("/exit")) return false;
        sendMessage(socketChannel, message);
        Logger.getInstance().log("Отправил сообщение: " + message + ";");
        return true;
    }
}
