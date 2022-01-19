package ru.itsjava.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
@Getter
public class MenuServiceImpl implements MenuService {
    private final ClientService clientService;
    private int menuNum;

    @Override
    public void menu() {
        printMenu();
        Scanner console = new Scanner(System.in);
        System.out.println("Выберите пункт из меню");
//        MessageInputService messageInputService = new MessageInputServiceImpl(System.in);
//        String menuNum = messageInputService.getMessage();
        menuNum = console.nextInt();
//        System.out.println(menuNum);

        if (menuNum == 1) {
            System.out.println("Вы выбрали авторизацию");
            clientService.authorizationUser();
        } else if (menuNum == 2) {
            System.out.println("Вы выбрали регистрацию");
            clientService.registrationNewUser();
        }
    }

    @Override
    public void printMenu() {
        System.out.println("1 - авторизация; 2 - регистрация");
    }

}
