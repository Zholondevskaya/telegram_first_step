package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseMigrationServiceImpl implements DatabaseMigrationService {
    private static final String CHECK_SQL_QUERY = """
            SELECT to_regclass('public.request_history')
            """;
    private static final String CREATE_TABLE_SQL_QUERY = """
            CREATE TABLE IF NOT EXISTS request_history (
                chat_id bigint,
                message text,
                sys_date timestamp,
                id bigint
            )
            """;
    private static final String CREATE_SEQ_SQL_QUERY = """
            CREATE SEQUENCE IF NOT EXISTS seq_request_history
            AS bigint
            INCREMENT 1
            START 100
            """;

    private final DatabaseConnectionPoolService databaseConnectionPoolService;

    public DatabaseMigrationServiceImpl(DatabaseConnectionPoolService databaseConnectionPoolService) {
        this.databaseConnectionPoolService = databaseConnectionPoolService;
    }

    @Override
    public void initSchema() {
        try {
            Connection connection = databaseConnectionPoolService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CHECK_SQL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("resultSet is empty");
            }
            String checkResult = resultSet.getString("to_regclass");

            if (checkResult == null) {
                PreparedStatement preparedStatementCreateSequence = connection.prepareStatement(CREATE_SEQ_SQL_QUERY);
                preparedStatementCreateSequence.executeUpdate();
                PreparedStatement preparedStatementCreateTable = connection.prepareStatement(CREATE_TABLE_SQL_QUERY);
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
