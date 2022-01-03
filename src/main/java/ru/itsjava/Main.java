package ru.itsjava;

import ru.itsjava.services.ClientService;
import ru.itsjava.services.ClientServiceImpl;

public class Main {

    public static void main(String[] args) {
        // создадим наш ClientService и будем его запускать
        ClientService clientService = new ClientServiceImpl();
        clientService.start();
    }
}
