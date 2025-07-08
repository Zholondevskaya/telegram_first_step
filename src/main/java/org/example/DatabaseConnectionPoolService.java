package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnectionPoolService {
    Connection getConnection() throws SQLException;
    void returnToPool(Connection connection);
}
