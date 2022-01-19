package ru.itsjava.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

@RequiredArgsConstructor
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    // создаем константы
    public final static int PORT = 8081; // порт подключения к серверу
    public final static String HOST = "localhost"; // строка подключения е серверу
    boolean isExit = false; // создание переменной isExit для проверки ввода "exit" для выхода из чата
    private int statusMenu = 0;
    private int statusMsgSrv = 0;
    private int statusMsgSrvReg = 0;
    private int statusMsgSrvAutho = 0;
    private String login;
    private String password;
//    private SocketRunnable socketRunnable;

    @SneakyThrows
    @Override
    public void start() {
        // хотим подключиться к серверу
        // чтобы работать на стороне Клиента необходим Socket
        Socket socket = new Socket(HOST, PORT); // в Socket необходимо передать HOST - где находится сервер и PORT - порт на котором он слушает

        // у socket'a проверить, если он подключился
        if (socket.isConnected()) {
            // как только подсоединились, создаем фоновый поток
            Thread thread;
            SocketRunnable sct = new SocketRunnable(socket, this);
//            SocketRunnable sct = new SocketRunnable(socket);
            thread = new Thread(sct);
            thread.start();
//            new Thread(new SocketRunnable(socket, this)).start();

//            System.out.println(socketRunnable.getMsgSrv());

            // у socket'a есть InputStream (что-то писать) и OutputStream (что-то получать)
            // в данном случае берем InputStream и отправляем что-то на сервер
            // чтобы что-то отправить нам подойдет PrintWriter оболочка над OutputStream
            // теперь должны считать с консоли, слушать сообщения с сервера и делать это одновременно
            // реализуем с помощью потоков
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream());

            // считываем с консоли
            MessageInputService messageInputService = new MessageInputServiceImpl(System.in);

            MenuService menuService = new MenuServiceImpl(this);
            menuService.menu();
            displayTheMenu();

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

            Thread.sleep(3000);
//            thread.wait();

//            System.out.println(statusMsgSrv);

            while ((statusMsgSrv == 1) || (statusMsgSrvReg == 1) || (statusMsgSrvAutho == 1)) {
                // пришел ответ от сервера, что авторизация не прошла statusMsgSrv = 1
                if (statusMsgSrv == 1) {
                    System.out.println("Вы не прошли авторизацию - имя либо пароль в БД не совпадают");
                    System.out.println("Начинаю повторную авторизацию");

                    // повторная авторизация
//                menuService.menu();
                    displayTheMenu();
                    serverWriter.println("!autho!" + login + ":" + password);
                    serverWriter.flush();
                }

                if (statusMsgSrvReg == 1) {
                    System.out.println("Вы успешно зарегистрированы!");
                }

                if (statusMsgSrvAutho == 1) {
                    System.out.println("Вы успешно авторизованы!");
                }
            }

            // считывать в цикле и отправлять сообщения
            while (true) {
//                System.out.println();

                // из messageInputService должны получать сообщение, которое написал пользователь
                String consoleMessage = messageInputService.getMessage();

                isExit = consoleMessage.equals("exit");
                // проверка ввода клиентом слова "exit"
                if (isExit) {
                    serverWriter.println("Всем пока!");
                    serverWriter.flush(); // скинуть буферизированные данные в поток
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

    public void displayTheMenu() {
        Scanner console = new Scanner(System.in);
        System.out.println("Введите свой логин:");
        login = console.nextLine();
//        login = new MessageInputServiceImpl(System.in).getMessage();
//        login = messageInputService.getMessage();
        System.out.println("Введите свой пароль:");
        password = console.nextLine();
//        password = new MessageInputServiceImpl(System.in).getMessage();
//        password = messageInputService.getMessage();
    }

    @Override
    @SneakyThrows
    public void statusMsgSrv() {
        statusMsgSrv = 1;
    }

    @Override
    @SneakyThrows
    public void statusMsgSrvReg() {
        statusMsgSrvReg = 1;
    }

    @Override
    @SneakyThrows
    public void statusMsgSrvAutho() {
        statusMsgSrvAutho = 1;
    }

}
