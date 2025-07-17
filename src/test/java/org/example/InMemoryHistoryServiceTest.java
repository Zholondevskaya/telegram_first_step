package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InMemoryHistoryServiceTest {

    //what_should_when

    @Test
    void addHistory_shouldNotThrow_whenGoodParams() {
        InMemoryHistoryService service = new InMemoryHistoryService();
        assertDoesNotThrow(() -> service.addHistory(123, "Hello", true, true));
    }

    @Test
    void getHistory_shouldNotThrow_whenGoodParams() {
        InMemoryHistoryService service = new InMemoryHistoryService();
        assertDoesNotThrow(() -> service.getHistory(123));
    }

    @Test
    void deleteHistory_shouldNotThrow_whenGoodParams() {
        InMemoryHistoryService service = new InMemoryHistoryService();
        assertDoesNotThrow(() -> service.deleteHistory(123));
    }
}