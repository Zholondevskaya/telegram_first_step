package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryService implements HistoryService {

    private final Map<Long, List<String>> chatToMessagesMap;

    public InMemoryHistoryService() {
        this.chatToMessagesMap = new HashMap<>();
    }

    @Override
    public void addHistory(long chatId, String message, boolean isUserRequest, boolean isInlineButtonClick) {

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
        // Option 1
        //        chatToMessagesMap.remove(chatId);
        // Option 2
        //        List<String> chatMessages = new ArrayList<>();
        //        chatToMessagesMap.put(chatId, chatMessages);
        // Option 3
        List<String> chatMessages = chatToMessagesMap.get(chatId);
        if (chatMessages != null) {
            chatMessages.clear();
        }
        // Option 4
        //        chatToMessagesMap.computeIfPresent(chatId, (key, chatMessages) -> {
        //            chatMessages.clear();
        //            return chatMessages;
        //        });
    }
}
