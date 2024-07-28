package org.example;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Main {

    static String tokenTg = "removed";

    public static void main(String[] args) {

        System.out.println("Hello world!");
        TelegramBotsLongPollingApplication telegramBotsApplication = new TelegramBotsLongPollingApplication();
        try {
            MyAmazingBot myAmazingBot = new MyAmazingBot();


            telegramBotsApplication.registerBot(tokenTg, myAmazingBot);
        } catch (TelegramApiException e) {
            System.out.println("Error" + e.getLocalizedMessage());
        }
    }


}

