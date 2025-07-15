package org.example;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageChannelService {

    void sendMessage(long chatId, String message);
    void deleteMessage(long chatId, @NotNull Integer messageId);
    void showButtons(SendMessage buttonMessage);
}
