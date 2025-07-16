package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionPoolServiceImpl implements DatabaseConnectionPoolService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionPoolServiceImpl.class);

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
        Connection connection = DriverManager.getConnection(url, user, password);
        if (!connection.getAutoCommit()) {
            connection.setAutoCommit(true);
        }
        return connection;
    }

    @Override
    public void returnToPool(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.info("Cannot close connection: {}", e.getLocalizedMessage());
        }
    }
}
