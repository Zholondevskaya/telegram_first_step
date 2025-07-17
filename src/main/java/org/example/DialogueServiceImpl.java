package org.example;

import java.time.LocalTime;
import java.util.List;

public class DialogueServiceImpl implements DialogueService {

    private final HistoryService historyService;
    private final MessageChannelService messageChannelService;

    public DialogueServiceImpl(HistoryService historyService, MessageChannelService messageChannelService) {
        this.historyService = historyService;
        this.messageChannelService = messageChannelService;
    }

    @Override
    public void processMessage(RequestMessageDto requestMessageDto) {
        long chatId = requestMessageDto.chatId();
        String requestMessage = requestMessageDto.requestMessage();
        boolean isInlineButtonClick = false;
        String responseMessage;

        if (requestMessage.equalsIgnoreCase("/start")) {
            historyService.addHistory(chatId, requestMessage, true, isInlineButtonClick);
            messageChannelService.showButtons(new RegularButtons().createKeyboardMessage(chatId));
            historyService.addHistory(chatId, "Отображена ReplyKeyboard", true, isInlineButtonClick);

        } else if (requestMessage.equalsIgnoreCase("история")) {
            historyService.addHistory(chatId, requestMessage, true, isInlineButtonClick);
            messageChannelService.showButtons(new InlineButtons().createKeyboardMessage(chatId));
            historyService.addHistory(chatId, "Отображена InlineKeyboard", true, isInlineButtonClick);

        } else {
            Integer messageId = requestMessageDto.messageId();
            if (requestMessage.contains("историю") && messageId != null) {
                isInlineButtonClick = true;
                messageChannelService.deleteMessage(chatId, messageId);
                historyService.addHistory(chatId, "Удалена InlineKeyboard", false, false);
            }
            historyService.addHistory(chatId, requestMessage, true, isInlineButtonClick);
            responseMessage = createResponse(chatId, requestMessage);
            messageChannelService.sendMessage(chatId, responseMessage);
            historyService.addHistory(chatId, responseMessage, false, false);
        }

    }

    private String createResponse(long chatId, String message) {

        String text;
        switch (message.toLowerCase()) {
            case ("время") -> {
                LocalTime currentTime = LocalTime.now();
                text = "Сейчас: " + currentTime;
            }
            case ("показать историю") -> {
                List<String> history = historyService.getHistory(chatId);
                if (history.isEmpty()) {
                    text = "История пуста";
                } else {
                    text = String.join(", ", history);
                }
            }
            case ("очистить историю") -> {
                historyService.deleteHistory(chatId);
                text = "Выполнена очистка истории";
            }
            default -> {
                text = "Вы сказали: " + message;
            }
        }
        return text;
    }
}
