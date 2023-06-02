package org.ldbcouncil.finbench.driver.csv;

import static java.lang.String.format;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.util.Tuple2;

/**
 * Class to read Parquet/CSV files for operation streams.
 */
public class FileLoader {

    private final DuckDbExtractor db;

    public FileLoader(DuckDbExtractor db) throws SQLException {
        this.db = db;
    }

    public Iterator<Operation> loadOperationStream(String path, EventStreamReader.EventDecoder<Operation> decoder)
        throws
        WorkloadException, SQLException {
        Statement stmt = null;
        List<Operation> results = new ArrayList<>();
        try {
            Connection connection = db.getConnection();
            stmt = connection.createStatement();
            ResultSet rs;
            if (path.contains(".parquet")) {
                rs = stmt.executeQuery("SELECT * FROM read_parquet('" + path + "');");
            } else {
                rs = stmt.executeQuery("SELECT * FROM read_csv_auto('" + path + "', delim='|', header=TRUE);");
            }
            while (rs.next()) {
                Operation obj = decoder.decodeEvent(rs);
                results.add(obj);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WorkloadException(
                format("Error loading substitution parameters into temporary database: %s", path), e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return results.iterator();
    }


    /**
     * Fetch a batch from the parquet file
     *
     * @param offset    Offset to use
     * @param batchSize Size of the batch to load.
     * @return Iterator with event operations.
     * @throws WorkloadException
     * @throws SQLException
     */
    public Iterator<Operation> getOperationStreamBatch(
        EventStreamReader.EventDecoder<Operation> decoder,
        String viewName,
        String batchColumnName,
        long offset,
        long batchSize
    ) throws WorkloadException, SQLException {
        Statement stmt = null;
        List<Operation> results = new ArrayList<>();
        try {
            Connection connection = db.getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                format("SELECT * FROM %s WHERE %s >= %d AND %s < %d ORDER BY %s ASC", viewName, batchColumnName, offset,
                    batchColumnName, offset + batchSize, batchColumnName));
            while (rs.next()) {
                Operation obj = decoder.decodeEvent(rs);
                results.add(obj);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WorkloadException(
                format("Error loading batch from database from view: %s on batch column %s", viewName, batchColumnName),
                e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return results.iterator();
    }


    /**
     * Creates a view on a parquet file using DuckDBs read_parquet function.
     * This creates a view without loading all data into memory.
     *
     * @throws WorkloadException When a view could not be created
     * @throws SQLException      When the statement could not be closed properly.
     */
    public void createViewOnParquetFile(String path, String viewName) throws WorkloadException, SQLException {
        Statement stmt = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.createStatement();

            if (path.contains(".parquet")) {
                stmt.execute("CREATE VIEW " + viewName + " AS SELECT * FROM read_parquet('" + path + "');");
            } else {
                stmt.execute("CREATE VIEW " + viewName + " AS SELECT * FROM read_csv_auto("
                    + "'" + path + "', delim='|', header=TRUE);");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WorkloadException(format("Error creating view on temporary database: %s", path), e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * Get the start and end values of the column used for the batching. These values are used to
     * determine when there are no more events left to load.
     *
     * @throws WorkloadException
     * @throws SQLException
     */
    public Tuple2<Long, Long> getBoundaryValues(String batchColumnName, String viewName)
        throws WorkloadException, SQLException {
        Statement stmt = null;
        long startValue = -1;
        long endValue = -1;
        try {
            Connection connection = db.getConnection();
            stmt = connection.createStatement();
            ResultSet rsStart = stmt.executeQuery(
                "SELECT " + batchColumnName + " FROM " + viewName + " ORDER BY " + batchColumnName + " ASC LIMIT 1 ");
            while (rsStart.next()) {
                startValue = rsStart.getLong(1);
            }
            rsStart.close();
            ResultSet rsEnd = stmt.executeQuery(
                "SELECT " + batchColumnName + " FROM " + viewName + " ORDER BY " + batchColumnName + " DESC LIMIT 1 ");
            while (rsEnd.next()) {
                endValue = rsEnd.getLong(1);
            }
            rsEnd.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WorkloadException(format("Error executing query on temporary database with view: %s", viewName),
                e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return new Tuple2<>(startValue, endValue);
    }
}
