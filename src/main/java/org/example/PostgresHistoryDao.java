package org.example;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostgresHistoryDao implements HistoryService {

    private static final String INSERT_SQL_QUERY = """
            INSERT into request_history (chat_id, message, sys_date, id, is_user_request, is_inline_button_click)
            VALUES (?, ?, ?, nextval('seq_request_history'), ?, ?)
            """;
    private static final String SELECT_SQL_QUERY = """
            SELECT message
            FROM request_history
            WHERE chat_id = ?
            ORDER BY sys_date
            """;
    private static final String DELETE_SQL_QUERY = """
            DELETE FROM request_history
            WHERE chat_id = ?
            """;

    private final DatabaseConnectionPoolService databaseConnectionPoolService;

    public PostgresHistoryDao(DatabaseConnectionPoolService databaseConnectionPoolService) {
        this.databaseConnectionPoolService = databaseConnectionPoolService;
    }

    @Override
    public void addHistory(long chatId, String message, boolean isUserRequest, boolean isInlineButtonClick) {
        try {
            Connection connection = databaseConnectionPoolService.getConnection();
            PreparedStatement preparedStatement = getPreparedStatementInsert(connection, chatId, message, isUserRequest, isInlineButtonClick);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            databaseConnectionPoolService.returnToPool(connection);
        } catch (Exception e) {
            throw new RuntimeException("Cannot attach message to history", e);
        }
    }

    @Override
    public List<String> getHistory(long chatId) {
        try {
            Connection connection = databaseConnectionPoolService.getConnection();
            PreparedStatement preparedStatement = getPreparedStatement(connection, chatId, SELECT_SQL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> selectedRows = extractMessages(resultSet);
            resultSet.close();
            preparedStatement.close();
            databaseConnectionPoolService.returnToPool(connection);
            return selectedRows;
        } catch (Exception e) {
            throw new RuntimeException("Cannot get message history", e);
        }
    }

    @Override
    public void deleteHistory(long chatId) {
        try {
            Connection connection = databaseConnectionPoolService.getConnection();
            PreparedStatement preparedStatement = getPreparedStatement(connection, chatId, DELETE_SQL_QUERY);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            databaseConnectionPoolService.returnToPool(connection);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete message history", e);
        }

    }

    @NotNull
    private static List<String> extractMessages(ResultSet resultSet) throws SQLException {
        List<String> selectedRows = new ArrayList<>();
        while (resultSet.next()) {
            selectedRows.add(resultSet.getString("message"));
        }
        return selectedRows;
    }

    private PreparedStatement getPreparedStatementInsert(Connection connection, long chatId, String message, boolean isUserRequest, boolean isInlineButtonClick) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL_QUERY);
        preparedStatement.setInt(1, Long.valueOf(chatId).intValue());
        preparedStatement.setString(2, message);
        preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setBoolean(4, isUserRequest);
        preparedStatement.setBoolean(5, isInlineButtonClick);
        return preparedStatement;
    }

    private PreparedStatement getPreparedStatement(Connection connection, long chatId, String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, Long.valueOf(chatId).intValue());
        return preparedStatement;
    }

}
