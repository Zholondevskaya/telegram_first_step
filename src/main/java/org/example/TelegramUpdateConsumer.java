package org.example;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramUpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final DialogueService dialogueService;
    private final BlockStatisticsService blockStatisticsService;

    public TelegramUpdateConsumer(DialogueService dialogueService, BlockStatisticsService blockStatisticsService) {
        this.dialogueService = dialogueService;
        this.blockStatisticsService = blockStatisticsService;
    }

    @Override
    public void consume(Update update) {
        //If the inline button was pressed
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        //If user block/unblock bot
        } else if(update.hasMyChatMember() && update.getMyChatMember().getChat().getId() != null) {
            long chatId = update.getMyChatMember().getChat().getId();
            String status = update.getMyChatMember().getNewChatMember().getStatus();
            blockStatisticsService.addBlockStatistic(chatId, status);
        //If user communicating with bot
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
