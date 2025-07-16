package org.example.migration;

import org.example.DatabaseConnectionPoolService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class V2Migration extends AbstractMigration {
    private static final String BLOCKING_HISTORY_CHECK_SQL_QUERY = """
            SELECT to_regclass('public.blocking_history')
            """;
    private static final String BLOCKING_HISTORY_CREATE_TABLE_SQL_QUERY = """
            CREATE TABLE IF NOT EXISTS blocking_history (
                chat_id bigint,
                blocking_status text,
                blocking_count bigint,
                sys_date timestamp,
                id bigint
            )
            """;
    private static final String BLOCKING_HISTORY_CREATE_SEQ_SQL_QUERY = """
            CREATE SEQUENCE IF NOT EXISTS seq_blocking_history
            AS bigint
            INCREMENT 1
            START 100
            """;

    private final DatabaseConnectionPoolService databaseConnectionPoolService;

    public V2Migration(DatabaseConnectionPoolService databaseConnectionPoolService) {
        this.databaseConnectionPoolService = databaseConnectionPoolService;
    }


    @Override
    protected void migrateInternal() {
        try {
            Connection connection = databaseConnectionPoolService.getConnection();

            PreparedStatement preparedStatementForBlocking = connection.prepareStatement(BLOCKING_HISTORY_CHECK_SQL_QUERY);
            ResultSet resultSetForBlocking = preparedStatementForBlocking.executeQuery();

            if (!resultSetForBlocking.next()) {
                throw new RuntimeException("resultSet is empty");
            }
            String checkResultForBlocking = resultSetForBlocking.getString("to_regclass");

            if (checkResultForBlocking == null) {
                PreparedStatement preparedStatementCreateSequence = connection.prepareStatement(BLOCKING_HISTORY_CREATE_SEQ_SQL_QUERY);
                preparedStatementCreateSequence.executeUpdate();
                PreparedStatement preparedStatementCreateTable = connection.prepareStatement(BLOCKING_HISTORY_CREATE_TABLE_SQL_QUERY);
                preparedStatementCreateTable.executeUpdate();

                preparedStatementCreateSequence.close();
                preparedStatementCreateTable.close();
            }
            resultSetForBlocking.close();
            preparedStatementForBlocking.close();

            databaseConnectionPoolService.returnToPool(connection);
        } catch (Exception e) {
            throw new RuntimeException("Cannot init schema", e);
        }
    }
}
