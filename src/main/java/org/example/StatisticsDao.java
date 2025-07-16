package org.example;

public interface StatisticsDao {
    void saveBlockStatistics(long chatId, String blocking_status, Integer blocking_count);
    Integer getBlockStatistic(long chatId);
}
