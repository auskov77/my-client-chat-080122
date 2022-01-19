package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.Socket;

@RequiredArgsConstructor
public class SocketRunnable implements Runnable {
    private final Socket socket;
    private final ClientService clientService;

    @SneakyThrows
    @Override
    public void run() {
        // задаем объект serverReader для идентификации, считывания сообщения с сервера
        // serverReader зависит от socket.getInputStream()
        MessageInputService serverReader = new MessageInputServiceImpl(socket.getInputStream());

        // получение сообщения от сервера
        while (true) {
//            System.out.println(serverReader.getMessage());
            if (serverReader.getMessage().contains("!NON autho!")) {
                clientService.statusMsgSrv();
            } else if (serverReader.getMessage().contains("!reg!")) {
                clientService.statusMsgSrvReg();
            } else if (serverReader.getMessage().contains("!autho!")){
                clientService.statusMsgSrvAutho();
            }
            System.out.println(serverReader.getMessage());
        }
    }
}


