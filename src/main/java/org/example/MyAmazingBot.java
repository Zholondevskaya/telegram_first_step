package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {

    TelegramClient tgClient = new OkHttpTelegramClient(Main.tokenTg);
    Map<Long, List<String>> table = new HashMap<>();

    @Override
    public void consume(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().getText());

            Long chatId = update.getMessage().getChatId();
            if (chatId != null) {
                SendMessage sendMessage;

                List<String> myList = table.get(chatId);

                if (myList == null) {
                    myList = new ArrayList<>();
                    table.put(chatId, myList);
                }
                myList.add(update.getMessage().getText());
                String text;
                switch (update.getMessage().getText()) {
                    case ("время"), ("Время") -> {
                        LocalTime currentTime = LocalTime.now();
                        text = "Сейчас: " + currentTime;
                    }
                    case ("история"), ("История") -> {
                        text = String.join(", ", myList);
                    }
                    default -> {
                        text = "Вы сказали: " + update.getMessage().getText();
                    }
                }
                myList.add(text);
                sendMessage = new SendMessage(chatId.toString(), text);


                try {
                    tgClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    System.out.println("Error 2" + e.getLocalizedMessage());
                }
            }
        }

    }
}
