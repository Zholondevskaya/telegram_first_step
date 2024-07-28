package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RestController
@SpringBootApplication
public class Main {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }
    static String tokenTg = "removed";

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

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

