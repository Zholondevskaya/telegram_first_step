package org.example;

public class BlockStatisticsServiceImpl implements BlockStatisticsService {

    private final StatisticsDao statisticsDao;

    public BlockStatisticsServiceImpl(StatisticsDao statisticsDao) {
        this.statisticsDao = statisticsDao;
    }

    @Override
    public void addBlockStatistic(long chatId, String blockingStatus) {
        if (blockingStatus.equals("kicked")) {
            Integer blockingCount = statisticsDao.getBlockStatistic(chatId) + 1;
            statisticsDao.saveBlockStatistics(chatId, "blocked", blockingCount);
        } else if (blockingStatus.equals("member")) {
            Integer blockingCount = statisticsDao.getBlockStatistic(chatId);
            statisticsDao.saveBlockStatistics(chatId, "unblocked", blockingCount);
        }
    }
}
