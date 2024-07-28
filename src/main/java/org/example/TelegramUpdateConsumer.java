package org.example;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramUpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final DialogueService dialogueService;

    public TelegramUpdateConsumer(DialogueService dialogueService) {
        this.dialogueService = dialogueService;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getChatId() != null) {
            long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            dialogueService.processMessage(chatId, message);
        }
    }

}
