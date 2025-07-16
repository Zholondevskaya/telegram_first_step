package org.example;

import java.sql.*;
import java.time.LocalDateTime;

public class PostgresStatisticsDao implements StatisticsDao {

    private static final String BLOCKING_HISTORY_SELECT_SQL_QUERY = """
            SELECT max(blocking_count) as current_blocking_count
            FROM blocking_history
            WHERE chat_id = ?
            """;
    private static final String BLOCKING_HISTORY_INSERT_SQL_QUERY = """
            INSERT into blocking_history (chat_id, blocking_status, blocking_count, sys_date, id)
            VALUES (?, ?, ?, ?, nextval('seq_blocking_history'))
            """;

    private final DatabaseConnectionPoolService databaseConnectionPoolService;

    public PostgresStatisticsDao(DatabaseConnectionPoolService databaseConnectionPoolService) {
        this.databaseConnectionPoolService = databaseConnectionPoolService;
    }

    @Override
    public void saveBlockStatistics(long chatId, String blocking_status, Integer blocking_count) {
        try {
            Connection connection = databaseConnectionPoolService.getConnection();
            PreparedStatement preparedStatement = getPreparedStatementInsert(connection, chatId, blocking_status, blocking_count);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            databaseConnectionPoolService.returnToPool(connection);
        } catch (Exception e) {
            throw new RuntimeException("Cannot add blocking history", e);
        }

    }

    @Override
    public Integer getBlockStatistic(long chatId) {
        try {
            Connection connection = databaseConnectionPoolService.getConnection();
            PreparedStatement preparedStatement = getPreparedStatement(connection, chatId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new RuntimeException("resultSet is empty");
            }
            Integer blockingCount = resultSet.getInt("current_blocking_count");
            resultSet.close();
            preparedStatement.close();
            databaseConnectionPoolService.returnToPool(connection);
            return blockingCount;
        } catch (Exception e) {
            throw new RuntimeException("Cannot get message history", e);
        }
    }

    private PreparedStatement getPreparedStatementInsert(Connection connection, long chatId, String blocking_status, Integer blocking_count) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(BLOCKING_HISTORY_INSERT_SQL_QUERY);
        preparedStatement.setInt(1, Long.valueOf(chatId).intValue());
        preparedStatement.setString(2, blocking_status);
        preparedStatement.setInt(3, blocking_count);
        preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        return preparedStatement;
    }

    private PreparedStatement getPreparedStatement(Connection connection, long chatId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(BLOCKING_HISTORY_SELECT_SQL_QUERY);
        preparedStatement.setInt(1, Long.valueOf(chatId).intValue());
        return preparedStatement;
    }
}
