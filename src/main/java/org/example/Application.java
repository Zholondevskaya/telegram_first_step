package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

//        HistoryService historyService = new InMemoryHistoryService();
        ConfigurationService configurationService = new BufferedConfigurationServiceImpl();
        DatabaseConnectionPoolService databaseConnectionPoolService = new DatabaseConnectionPoolServiceImpl(configurationService);
        HistoryService historyService = new PostgresHistoryService(databaseConnectionPoolService);
        TelegramClient telegramClient = new OkHttpTelegramClient(configurationService.getConfigurationProperty("telegram.token"));
        MessageChannelService messageChannelService = new TelegramMessageChannelService(telegramClient);
        DialogueService dialogueService = new DialogueServiceImpl(historyService, messageChannelService);
        TelegramUpdateConsumer telegramUpdateConsumer = new TelegramUpdateConsumer(dialogueService);

        DatabaseMigrationService databaseMigrationService = new DatabaseMigrationServiceImpl(databaseConnectionPoolService);
        databaseMigrationService.initSchema();

        try (TelegramBotsLongPollingApplication telegramBotsApplication = new TelegramBotsLongPollingApplication()) {
            telegramBotsApplication.registerBot(configurationService.getConfigurationProperty("telegram.token"), telegramUpdateConsumer);
            while (true) {}
        } catch (Exception e) {
            logger.info("Failed to register bot: {}", e.getLocalizedMessage());
        }
    }


}

