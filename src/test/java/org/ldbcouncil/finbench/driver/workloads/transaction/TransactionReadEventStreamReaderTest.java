package org.ldbcouncil.finbench.driver.workloads.transaction;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.csv.DuckDbExtractor;
import org.ldbcouncil.finbench.driver.csv.FileLoader;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;


public class TransactionReadEventStreamReaderTest {
    private DuckDbExtractor db;
    private Statement stmt;

    /**
     * Initialize mock objects used in all the tests
     *
     * @throws SQLException
     */
    @BeforeEach
    public void init() throws SQLException {
        Connection connection = mock(Connection.class);
        db = mock(DuckDbExtractor.class);
        when(db.getConnection()).thenReturn(connection);
        stmt = mock(Statement.class);
        when(connection.createStatement()).thenReturn(stmt);
    }

    // TODO wait the real params format
    // @Test
    public void shouldParseAllComplexRead1Events() throws WorkloadException, SQLException {
        // Arrange
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.getLong(1))
            .thenReturn(1L)
            .thenReturn(2L)
            .thenReturn(3L)
            .thenReturn(4L);
        when(rs.getLong(2))
            .thenReturn(1673576055938L)
            .thenReturn(1673576055938L)
            .thenReturn(1673576055938L)
            .thenReturn(1673576055938L);
        when(rs.getLong(3))
            .thenReturn(1673576055938L)
            .thenReturn(1673576055938L)
            .thenReturn(1673576055938L)
            .thenReturn(1673576055938L);
        /* when(rs.getTimestamp(2))
            .thenReturn(Timestamp.valueOf("2012-07-29 08:52:02.735"))
            .thenReturn(Timestamp.valueOf("2012-07-29 08:52:02.735"))
            .thenReturn(Timestamp.valueOf("2012-07-29 08:52:02.735"))
            .thenReturn(Timestamp.valueOf("2012-07-29 08:52:02.735"));
        when(rs.getTimestamp(3))
            .thenReturn(Timestamp.valueOf("2019-12-30 00:00:00.0"))
            .thenReturn(Timestamp.valueOf("2019-12-30 00:00:00.0"))
            .thenReturn(Timestamp.valueOf("2019-12-30 00:00:00.0"))
            .thenReturn(Timestamp.valueOf("2019-12-30 00:00:00.0"));*/
        when(rs.getInt(4))
            .thenReturn(15)
            .thenReturn(100)
            .thenReturn(1000)
            .thenReturn(50);

        // TODO Blob type
        /*when(rs.getBlob(5))
            .thenReturn("DESC")
            .thenReturn("DESC")
            .thenReturn("DESC")
            .thenReturn("ASC");*/
        EventStreamReader.EventDecoder<Operation> decoder = new QueryEventStreamReader.ComplexRead1Decoder();
        FileLoader loader = new FileLoader(db);
        Iterator<Operation> opStream = loader.loadOperationStream("/somepath", decoder);

        // Act
        Iterator<Operation> reader = new QueryEventStreamReader(
            opStream
        );

        // Assert
        ComplexRead1 operation;

        operation = (ComplexRead1) reader.next();
        assertThat(operation.getId(), is(1L));
        assertThat(operation.getStartTime().getTime(), equalTo(Timestamp.valueOf("2012-07-29 08:52:02.735").getTime()));
        assertThat(operation.getEndTime().getTime(), is(Timestamp.valueOf("2019-12-30 00:00:00.0").getTime()));
        assertThat(operation.getTruncationLimit(), is(15));
        assertThat(operation.getTruncationOrder().name(), equalTo("DESC"));

        operation = (ComplexRead1) reader.next();
        assertThat(operation.getId(), is(2L));
        assertThat(operation.getStartTime().getTime(), is(Timestamp.valueOf("2012-07-29 08:52:02.735").getTime()));
        assertThat(operation.getEndTime().getTime(), is(Timestamp.valueOf("2019-12-30 00:00:00.0").getTime()));
        assertThat(operation.getTruncationLimit(), is(100));
        assertThat(operation.getTruncationOrder().name(), equalTo("DESC"));

        operation = (ComplexRead1) reader.next();
        assertThat(operation.getId(), is(3L));
        assertThat(operation.getStartTime().getTime(), is(Timestamp.valueOf("2012-07-29 08:52:02.735").getTime()));
        assertThat(operation.getEndTime().getTime(), is(Timestamp.valueOf("2019-12-30 00:00:00.0").getTime()));
        assertThat(operation.getTruncationLimit(), is(1000));
        assertThat(operation.getTruncationOrder().name(), equalTo("DESC"));

        operation = (ComplexRead1) reader.next();
        assertThat(operation.getId(), is(4L));
        assertThat(operation.getStartTime().getTime(), is(Timestamp.valueOf("2012-07-29 08:52:02.735").getTime()));
        assertThat(operation.getEndTime().getTime(), is(Timestamp.valueOf("2019-12-30 00:00:00.0").getTime()));
        assertThat(operation.getTruncationLimit(), is(50));
        assertThat(operation.getTruncationOrder().name(), equalTo("ASC"));

        assertThat(reader.hasNext(), is(false));
    }

    @Test
    public void shouldParseAllComplexRead2Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead3Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead4Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead5Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead6Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead7Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead8Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead9Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead10Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead11Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead12Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }

    @Test
    public void shouldParseAllComplexRead13Events() throws WorkloadException, SQLException {
        // TODO add complex read events test
    }
}
