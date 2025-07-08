package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionPoolServiceImpl implements DatabaseConnectionPoolService {
    private final String url;
    private final String user;
    private final String password;
    
    public DatabaseConnectionPoolServiceImpl(ConfigurationService configuration){
        this.url = configuration.getConfigurationProperty("postgres.url");
        this.user = configuration.getConfigurationProperty("postgres.user");
        this.password = configuration.getConfigurationProperty("postgres.password");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void returnToPool(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Cannot close connection");
        }
    }
}
