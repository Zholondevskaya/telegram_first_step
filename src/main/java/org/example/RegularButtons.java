package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class RegularButtons implements TelegramButtons {
    @Override
    public SendMessage createKeyboardMessage(long chatId) {
        SendMessage message = new SendMessage(String.valueOf(chatId), "Выберите или введите действие");

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Время");
        row1.add("История");
        keyboard.add(row1);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboard);
        keyboardMarkup.setResizeKeyboard(true); // Делает клавиатуру компактной
        keyboardMarkup.setOneTimeKeyboard(false); // Клавиатура останется на экране

        message.setReplyMarkup(keyboardMarkup);

        return message;
    }
}
