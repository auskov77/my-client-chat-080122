package ru.itsjava.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MenuServiceImpl implements MenuService {
    private final ClientService clientService;

    @Override
    public void menu() {
//        while (true) {
        printMenu();
        System.out.println("Выберите пункт из меню");
        MessageInputService messageInputService = new MessageInputServiceImpl(System.in);
        String menuNum = messageInputService.getMessage();

        if (menuNum.equals("1")) {
            System.out.println("Вы выбрали авторизацию");
            clientService.authorizationUser();
        } else if (menuNum.equals("2")) {
            System.out.println("Вы выбрали регистрацию");
            clientService.registrationNewUser();
        }
//    }
    }

    @Override
    public void printMenu() {
        System.out.println("1 - авторизация; 2 - регистрация");
    }
}
