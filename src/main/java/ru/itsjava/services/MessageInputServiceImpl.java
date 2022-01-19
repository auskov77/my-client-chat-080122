package ru.itsjava.services;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessageInputServiceImpl implements MessageInputService {
    private final BufferedReader bufferedReader;

    // вот наш конструктор от InputStream'a
    public MessageInputServiceImpl(InputStream inputStream) {
        // вот создание bufferedReader
        // создаем BufferedReader от InputStream'a
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    // внутри этого метода надо подключить наш BufferedReader и д.б. какой-то конструктор
    // конструктор будет от некоторого InputStream'a
    // а BufferedReader будет здесь создаваться
    @SneakyThrows
    @Override
    public String getMessage() {
        // внутри него нужно получать некоторое сообщение
        return bufferedReader.readLine();
    }
}
