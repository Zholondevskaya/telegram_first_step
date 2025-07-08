package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class Application {

    public static void main(String[] args) {
//        HistoryService historyService = new InMemoryHistoryService();

        ConfigurationService configurationService = new BufferedConfigurationServiceImpl();
        DatabaseConnectionPoolService databaseConnectionPoolService = new DatabaseConnectionPoolServiceImpl(configurationService);
        HistoryService historyService = new PostgresHistoryService(databaseConnectionPoolService);
        TelegramClient telegramClient = new OkHttpTelegramClient(configurationService.getConfigurationProperty("telegram.token"));
        ResponseSenderService responseSenderService = new ResponseSenderServiceImpl(telegramClient);
        DialogueService dialogueService = new DialogueServiceImpl(historyService, responseSenderService);
        TelegramUpdateConsumer telegramUpdateConsumer = new TelegramUpdateConsumer(dialogueService);

        DatabaseMigrationService databaseMigrationService = new DatabaseMigrationServiceImpl(databaseConnectionPoolService);
        databaseMigrationService.initSchema();

        try (TelegramBotsLongPollingApplication telegramBotsApplication = new TelegramBotsLongPollingApplication()) {
            telegramBotsApplication.registerBot(configurationService.getConfigurationProperty("telegram.token"), telegramUpdateConsumer);
            while (true) {}
        } catch (Exception e) {
            System.out.println("Failed to register bot " + e.getLocalizedMessage());
        }
    }


}

