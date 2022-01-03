package ru.itsjava.services;

import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final ClientService clientService;
    private final Scanner scanner;

    @Override
    public void menu() {
        while (true) {
            printMenu();
            System.out.println("Выберите пункт из меню");
            int menuNum = scanner.nextInt();
            switch (menuNum) {
                case 1:
                    clientService.toString("!autho!");
                    break;
                case 2:

                    break;
            }
        }
    }

    @Override
    public void printMenu() {
        System.out.println("1 - авторизация; 2 - регистрация");
    }
}
