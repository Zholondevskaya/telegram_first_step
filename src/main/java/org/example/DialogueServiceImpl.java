package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.List;

public class DialogueServiceImpl implements DialogueService {
    private static final Logger logger = LoggerFactory.getLogger(DialogueServiceImpl.class);

    private final HistoryService historyService;
    private final MessageChannelService messageChannelService;

    public DialogueServiceImpl (HistoryService historyService, MessageChannelService messageChannelService) {
        this.historyService = historyService;
        this.messageChannelService = messageChannelService;
    }

    @Override
    public void processMessage(RequestMessageDto requestMessageDto) {
        long chatId = requestMessageDto.chatId();
        String requestMessage = requestMessageDto.requestMessage();

        historyService.addHistory(chatId, requestMessage);
        if (requestMessage.equalsIgnoreCase("/start")) {
            TelegramButtons regularButtons = new RegularButtons();
            messageChannelService.showButtons(regularButtons.createKeyboardMessage(chatId));
        } else if (requestMessage.equalsIgnoreCase("история")) {
            TelegramButtons inlineButtons = new InlineButtons();
            messageChannelService.showButtons(inlineButtons.createKeyboardMessage(chatId));
        } else {
            String responseMessage = createResponse(chatId, requestMessage);
            historyService.addHistory(chatId, responseMessage);
            Integer messageId = requestMessageDto.messageId();
            if (requestMessage.contains("историю") && messageId != null) {
                logger.info("Пользователь воспользовался inline кнопкой");
                messageChannelService.deleteMessage(chatId, messageId);
            }
            messageChannelService.sendMessage(chatId, responseMessage);
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
