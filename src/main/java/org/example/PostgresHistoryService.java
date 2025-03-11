package org.example;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostgresHistoryService implements HistoryService {

    private static final String INSERT_SQL_QUERY = """
            INSERT into request_history (chat_id, message, sys_date, id)
            VALUES (?, ?, ?, nextval('seq_request_history'))
            """;
    private static final String SELECT_SQL_QUERY = """
            SELECT message
            FROM request_history
            WHERE chat_id = ?
            """;
    private static final String DELETE_SQL_QUERY = """
            DELETE FROM request_history
            WHERE chat_id = ?
            """;

    private final String url = "jdbc:postgresql://localhost:5432/telegram";
    private final String user = "postgres";
    private final String password = "postgres";


//    public PostgresHistoryService (String url, String user, String password) {
//        this.url = url;
//        this.user = user;
//        this.password = password;
//    }

    @Override
    public void addHistory(long chatId, String message) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = getPreparedStatementInsert(connection, chatId, message);
            preparedStatement.executeUpdate();
            // TODO commit transaction
            closeResources(preparedStatement, connection);
        } catch (Exception e) {
            throw new RuntimeException("Cannot attach message to history", e);
        }
    }

    @Override
    public List<String> getHistory(long chatId) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = getPreparedStatement(connection, chatId, SELECT_SQL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> selectedRows = extractMessages(resultSet);
            closeResources(resultSet, preparedStatement, connection);
            return selectedRows;
        } catch (Exception e) {
            throw new RuntimeException("Cannot get message history", e);
        }
    }

    @Override
    public void deleteHistory(long chatId) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = getPreparedStatement(connection, chatId, DELETE_SQL_QUERY);
            preparedStatement.executeUpdate();
            // TODO commit transaction
            closeResources(preparedStatement, connection);
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


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void closeResources(AutoCloseable... resources) throws Exception {
        for (AutoCloseable resource : resources) {
            resource.close();
        }
    }

    private PreparedStatement getPreparedStatementInsert(Connection connection, long chatId, String message) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL_QUERY);
        preparedStatement.setInt(1, Long.valueOf(chatId).intValue());
        preparedStatement.setString(2, message);
        preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        return preparedStatement;
    }

    private PreparedStatement getPreparedStatement(Connection connection, long chatId, String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, Long.valueOf(chatId).intValue());
        return preparedStatement;
    }

}
