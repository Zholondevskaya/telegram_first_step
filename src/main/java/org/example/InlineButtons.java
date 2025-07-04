package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class InlineButtons {
    public static SendMessage createMessageWithInlineKeyboard(Long chatId) {
        SendMessage message = new SendMessage(String.valueOf(chatId), "Что вы хотите сделать с историей");

//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//
//        List<InlineKeyboardButton> row1 = new ArrayList<>();
//        InlineKeyboardButton button1 = new InlineKeyboardButton("Показать историю");
//        button1.setCallbackData("button1_pressed");
//
//        InlineKeyboardButton button2 = new InlineKeyboardButton("Очистить историю");
//        button2.setCallbackData("button2_pressed");
//
//        row1.add(button1);
//        row1.add(button2);
//        rowsInline.add(row1);
//
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//
//        markupInline.setKeyboard(rowsInline);
//        message.setReplyMarkup(markupInline);

        // Создаем InlineKeyboardMarkup через builder()
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Показать историю")
                                .callbackData("button1_pressed")
                                .build()
                )))
                .keyboardRow(new InlineKeyboardRow(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Очистить историю")
                                .callbackData("button2_pressed")
                                .build()
                )))
                .build();

        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }
}
