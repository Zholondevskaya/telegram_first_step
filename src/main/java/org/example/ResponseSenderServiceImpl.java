package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class ResponseSenderServiceImpl implements ResponseSenderService {
    private final TelegramClient telegramClient;

    public ResponseSenderServiceImpl(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public void sendResponse(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);

        try {
            if (message.equals("История")) {
                SendMessage buttonInlineMessages = InlineButtons.createMessageWithInlineKeyboard(chatId);
                telegramClient.execute(buttonInlineMessages);

            } else {
                telegramClient.execute(sendMessage);
                SendMessage buttonMessages = RegularButtons.createMessageWithKeyboard(sendMessage);
                telegramClient.execute(buttonMessages);
            }
        } catch (TelegramApiException e) {
            System.out.println("Failed send message to Telegram " + e.getLocalizedMessage());
        }


    }
}
