package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryService implements HistoryService {

    private final Map<Long, List<String>> chatToMessagesMap;

    public InMemoryHistoryService(Map<Long, List<String>> chatToMessagesMap) {
        this.chatToMessagesMap = chatToMessagesMap;

    }

    @Override
    public void addHistory(long chatId, String message) {

        List<String> chatMessages = chatToMessagesMap.computeIfAbsent(chatId, k -> new ArrayList<>());
//        List<String> chatMessages = chatToMessagesMap.get(chatId);
//        if (chatMessages == null) {
//            chatMessages = new ArrayList<>();
//            chatToMessagesMap.put(chatId, chatMessages);
//        }
        chatMessages.add(message);
    }

    @Override
    public List<String> getHistory(long chatId) {
        return chatToMessagesMap.get(chatId);
    }

    @Override
    public void deleteHistory(long chatId) {
        // TODO implement
        throw new UnsupportedOperationException("deleteHistory method not implemented yet");
    }
}
