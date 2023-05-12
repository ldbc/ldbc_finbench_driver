package org.ldbcouncil.finbench.driver.csv;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles the connection with DuckDb.
 */
public class DuckDbExtractor implements Closeable {

    private final Connection connection;

    public DuckDbExtractor() throws SQLException {
        connection = DriverManager.getConnection("jdbc:duckdb:");
    }

    /**
     * Get connection to DuckDb instance
     *
     * @return Connection object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Close connection
     */
    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
