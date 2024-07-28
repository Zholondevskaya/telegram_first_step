package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.HashMap;


public class Main {

    public static void main(String[] args) {
        HistoryService historyService = new HistoryServiceImpl(new HashMap<>());
        ConfigurationServiceImpl configurationService = new ConfigurationServiceImpl("removed");
        TelegramClient telegramClient = new OkHttpTelegramClient(configurationService.getToken());
        ResponseSenderService responseSenderService = new ResponseSenderServiceImpl(telegramClient);
        DialogueService dialogueService = new DialogueServiceImpl(historyService, responseSenderService);
        TelegramUpdateConsumer telegramUpdateConsumer = new TelegramUpdateConsumer(dialogueService);
        try (TelegramBotsLongPollingApplication telegramBotsApplication = new TelegramBotsLongPollingApplication()) {
            telegramBotsApplication.registerBot(configurationService.getToken(), telegramUpdateConsumer);
            while (true) {}
        } catch (Exception e) {
            System.out.println("Failed to register bot " + e.getLocalizedMessage());
        }
    }


}

