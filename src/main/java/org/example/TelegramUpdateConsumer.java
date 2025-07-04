package org.example;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramUpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final DialogueService dialogueService;

    public TelegramUpdateConsumer(DialogueService dialogueService) {
        this.dialogueService = dialogueService;
    }

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getChatId() != null) {
            long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            dialogueService.processMessage(chatId, message);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();

        if ("button1_pressed".equals(data)) {
            dialogueService.processMessage(chatId, "Показать историю");
        } else if ("button2_pressed".equals(data)) {
            dialogueService.processMessage(chatId, "Очистить историю");
        }
    }
}
