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
            RequestMessageDto requestMessageDto = new RequestMessageDto(chatId, message, null);
            dialogueService.processMessage(requestMessageDto);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        RequestMessageDto requestMessageDto;
        String requestMessage;

        requestMessage = switch (data) {
            case "button1_pressed" -> "Показать историю";
            case "button2_pressed" -> "Очистить историю";
            default -> "Обработчик кнопки отсутствует";
        };

        requestMessageDto = new RequestMessageDto(chatId, requestMessage, messageId);
        dialogueService.processMessage(requestMessageDto);
    }
}
