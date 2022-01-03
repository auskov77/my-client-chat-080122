package ru.itsjava.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.Socket;

@RequiredArgsConstructor
public class SocketRunnable implements Runnable{
    private final Socket socket;

    @SneakyThrows
    @Override
    public void run() {
        // задаем объект serverReader для идентификации, считывания сообщений с сервера
        // serverReader зависит от socket.getInputStream()
        MessageInputService serverReader = new MessageInputServiceImpl(socket.getInputStream());

        // получение сообщения от сервера
        while (true){
            System.out.println(serverReader.getMessage());

        }
    }
}
