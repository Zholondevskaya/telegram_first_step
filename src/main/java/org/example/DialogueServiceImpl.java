package org.example;

import java.time.LocalTime;
import java.util.List;

public class DialogueServiceImpl implements DialogueService {
    private final HistoryService historyService;
    private final ResponseSenderService responseSenderService;

    public DialogueServiceImpl (HistoryService historyService, ResponseSenderService responseSenderService) {
        this.historyService = historyService;
        this.responseSenderService = responseSenderService;
    }

    @Override
    public void processMessage(long chatId, String message) {
        historyService.addHistory(chatId, message);
        String responseMessage = createResponse(chatId, message);
        historyService.addHistory(chatId, responseMessage);
        responseSenderService.sendResponse(chatId, responseMessage);
    }

    private String createResponse(long chatId, String message) {

        String text;
        switch (message) {
            case ("время"), ("Время") -> {
                LocalTime currentTime = LocalTime.now();
                text = "Сейчас: " + currentTime;
            }
            case ("история"), ("История") -> {
                text = "История";
            }
            case ("Показать историю") -> {
                List<String> history = historyService.getHistory(chatId);
                if (history.isEmpty()) {
                    text = "История пуста";
                } else {
                    text = String.join(", ", history);
                }
            }
            case ("очистить историю"), ("Очистить историю") -> {
                historyService.deleteHistory(chatId);
                text = "Очистка выполнена";
            }
            default -> {
                text = "Вы сказали: " + message;
            }
        }
        return text;
    }
}
