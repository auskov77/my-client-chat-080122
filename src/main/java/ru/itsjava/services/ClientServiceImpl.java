package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    // создаем константы
    public final static int PORT = 8081; // порт подключения к серверу
    public final static String HOST = "localhost"; // строка подключения е серверу
    boolean isExit = false; // создание переменной isExit для проверки ввода "exit" для выхода из чата
    private int statusMenu = 0;

    @SneakyThrows
    @Override
    public void start() {
        // хотим подключиться к серверу
        // чтобы работать на стороне Клиента необходим Socket
        Socket socket = new Socket(HOST, PORT); // в Socket необходимо передать HOST - где находится сервер и PORT - порт на котором он слушает

        // у socket'a проверить, если он подключился
        if (socket.isConnected()) {
            // как только подсоединились, создаем фоновый поток
            new Thread(new SocketRunnable(socket)).start();

            // у socket'a есть InputStream (что-то писать) и OutputStream (что-то получать)
            // в данном случае берем InputStream и отправляем что-то на сервер
            // чтобы что-то отправить нам подойдет PrintWriter оболочка над OutputStream
            // теперь должны считать с консоли, слушать сообщения с сервера и делать это одновременно
            // реализуем с помошью потоков
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream());

            // считываем с консоли
            MessageInputService messageInputService = new MessageInputServiceImpl(System.in);

            MenuService menuService = new MenuServiceImpl(this);
            menuService.menu();

            System.out.println("Введите свой логин:");
            String login = messageInputService.getMessage();
            System.out.println("Введите свой пароль:");
            String password = messageInputService.getMessage();

            // после ввода логина и пароля - их нужно отправить на сервер
            // !autho!login:password
            // теперь конкатенируем - все собираем
            if (statusMenu == 1) {
                serverWriter.println("!autho!" + login + ":" + password);
                // !reg!login:password
            } else if (statusMenu == 2) {
                serverWriter.println("!reg!" + login + ":" + password);
            }
            // теперь отправляем все это на сервер
            serverWriter.flush();

            // считывать в цикле и отправлять сообщения
            while (!isExit) {
//                System.out.println("Введите сообщение");
                // из messageInputService должны получать сообщение, которое написал пользователь
                String consoleMessage = messageInputService.getMessage();

                isExit = consoleMessage.equals("exit");
                // проверка ввода клиентом слова "exit"
                if (isExit) {
                    serverWriter.println("Всем пока!");
                    serverWriter.flush(); // скинуть буфферезированные данные в поток
                    System.exit(0);
                }

                // кто-то ввел сообщение consoleMessage, и мы должны отправить - serverWriter
                serverWriter.println(consoleMessage);
                serverWriter.flush(); // скинуть буфферезированные данные в поток
            }
        }
    }

    @SneakyThrows
    @Override
    public void authorizationUser() {
        statusMenu = 1;
    }

    @SneakyThrows
    @Override
    public void registrationNewUser() {
        statusMenu = 2;
    }

}
