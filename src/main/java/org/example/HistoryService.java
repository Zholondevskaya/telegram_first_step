package org.example;

import java.util.List;

public interface HistoryService {
    void addHistory(long chatId, String message);
    List<String> getHistory(long chatId);
}
