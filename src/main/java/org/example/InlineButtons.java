package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

public class InlineButtons implements TelegramButtons {

    @Override
    public SendMessage createKeyboardMessage(long chatId) {
        SendMessage message = new SendMessage(String.valueOf(chatId), "Что вы хотите сделать с историей");

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

    public String getButtonText(String data) {
        return switch (data) {
            case "button1_pressed" -> "Показать историю";
            case "button2_pressed" -> "Очистить историю";
            default -> "Обработчик кнопки отсутствует";
        };
    }
}
