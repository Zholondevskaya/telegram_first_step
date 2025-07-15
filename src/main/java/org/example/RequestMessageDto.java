package org.example;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record RequestMessageDto(
        @NotNull Long chatId,
        @NotNull String requestMessage,
        @Nullable Integer messageId
) {
}
