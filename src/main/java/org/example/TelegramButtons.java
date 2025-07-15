package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TelegramButtons {
    SendMessage createKeyboardMessage(long chatId);
}
