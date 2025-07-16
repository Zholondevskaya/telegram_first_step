package org.example.migration;

import org.example.DatabaseConnectionPoolService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class V1Migration extends AbstractMigration {
    private static final String REQUEST_HISTORY_CHECK_SQL_QUERY = """
            SELECT to_regclass('public.request_history')
            """;
    private static final String REQUEST_HISTORY_CREATE_TABLE_SQL_QUERY = """
            CREATE TABLE IF NOT EXISTS request_history (
                chat_id bigint,
                message text,
                sys_date timestamp,
                id bigint
            )
            """;
    private static final String REQUEST_HISTORY_CREATE_SEQ_SQL_QUERY = """
            CREATE SEQUENCE IF NOT EXISTS seq_request_history
            AS bigint
            INCREMENT 1
            START 100
            """;

    private final DatabaseConnectionPoolService databaseConnectionPoolService;

    public V1Migration(DatabaseConnectionPoolService databaseConnectionPoolService) {
        this.databaseConnectionPoolService = databaseConnectionPoolService;
    }

    @Override
    protected void migrateInternal() {
        try {
            Connection connection = databaseConnectionPoolService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(REQUEST_HISTORY_CHECK_SQL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("resultSet is empty");
            }
            String checkResult = resultSet.getString("to_regclass");

            if (checkResult == null) {
                PreparedStatement preparedStatementCreateSequence = connection.prepareStatement(REQUEST_HISTORY_CREATE_SEQ_SQL_QUERY);
                preparedStatementCreateSequence.executeUpdate();
                PreparedStatement preparedStatementCreateTable = connection.prepareStatement(REQUEST_HISTORY_CREATE_TABLE_SQL_QUERY);
                preparedStatementCreateTable.executeUpdate();

                preparedStatementCreateSequence.close();
                preparedStatementCreateTable.close();
            }
            resultSet.close();
            preparedStatement.close();
            databaseConnectionPoolService.returnToPool(connection);
        } catch (Exception e) {
            throw new RuntimeException("Cannot init schema", e);
        }
    }
}
