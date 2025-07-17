package org.example;

import java.util.List;

public interface HistoryService {
    void addHistory(long chatId, String message, boolean isUserRequest, boolean isInlineButtonClick);
    List<String> getHistory(long chatId);
    void deleteHistory(long chatId);
}
