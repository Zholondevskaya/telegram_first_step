package org.example.migration;

import org.example.DatabaseConnectionPoolService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class V3Migration extends AbstractMigration {

    private static final String CHECK_COLUMN_EXIST_SQL_QUERY = """ 
            SELECT column_name
            FROM information_schema.columns
            WHERE table_name='request_history' and column_name = ?;
            """;
    private static final String CREATE_COLUMN_SQL_QUERY = """ 
            ALTER TABLE IF EXISTS request_history
            ADD COLUMN ? boolean;
            """;

    private final DatabaseConnectionPoolService databaseConnectionPoolService;

    public V3Migration(DatabaseConnectionPoolService databaseConnectionPoolService) {
        this.databaseConnectionPoolService = databaseConnectionPoolService;
    }

    @Override
    protected void migrateInternal() {
        try {
            List<String> columnList = Arrays.asList("is_user_request", "is_inline_button_click");
            Connection connection = databaseConnectionPoolService.getConnection();
            for (String i : columnList) {
                PreparedStatement preparedStatement = getPreparedStatement(connection, i, CHECK_COLUMN_EXIST_SQL_QUERY);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    /*
                    * Commented out because calling getPreparedStatement returns the following result:
                    * ALTER TABLE IF EXISTS request_history
                    * ADD COLUMN ('is_user_request') boolean
                    *
                    * This result contains a syntax error
                    * */

//                    PreparedStatement preparedStatementCreateSequence = getPreparedStatement(connection, i, CREATE_COLUMN_SQL_QUERY);
                    String test = CREATE_COLUMN_SQL_QUERY.replace("?", i);
                    PreparedStatement preparedStatementCreateSequence = connection.prepareStatement(test);
                    preparedStatementCreateSequence.executeUpdate();
                    preparedStatementCreateSequence.close();
                }
                resultSet.close();
                preparedStatement.close();
            }
            databaseConnectionPoolService.returnToPool(connection);

        } catch (SQLException e) {
            throw new RuntimeException("Cannot init schema", e);
        }
    }

    private PreparedStatement getPreparedStatement(Connection connection, String columnName, String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, columnName);
        return preparedStatement;
    }
}
