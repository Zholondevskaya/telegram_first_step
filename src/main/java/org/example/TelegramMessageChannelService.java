package org.example;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramMessageChannelService implements MessageChannelService {
    private final TelegramClient telegramClient;

    public TelegramMessageChannelService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
        executeInternal(sendMessage, "Failed send message to Telegram");
    }

    @Override
    public void deleteMessage(long chatId, @NotNull Integer messageId) {
        DeleteMessage message = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();

        executeInternal(message, "Failed delete message from Telegram");
    }

    @Override
    public void showButtons(SendMessage buttonMessage) {
        executeInternal(buttonMessage, "Failed show buttons");
    }

    private void executeInternal(BotApiMethod<?> botApiMethod, String errorMessage) {
        try {
            telegramClient.execute(botApiMethod);
        } catch (TelegramApiException e) {
            System.out.printf("%s %s%n", errorMessage, e.getLocalizedMessage());
        }
    }
}
