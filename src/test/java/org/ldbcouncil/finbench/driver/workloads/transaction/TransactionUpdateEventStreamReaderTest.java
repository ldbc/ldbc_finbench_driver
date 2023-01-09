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
import org.ldbcouncil.finbench.driver.csv.DuckDbConnectionState;
import org.ldbcouncil.finbench.driver.csv.ParquetLoader;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.workloads.QueryEventStreamReader;
import org.ldbcouncil.finbench.driver.workloads.UpdateEventStreamReader;


public class TransactionUpdateEventStreamReaderTest {
    private DuckDbConnectionState db;
    private Statement stmt;

    /**
     * Initialize mock objects used in all the tests
     *
     * @throws SQLException
     */
    @BeforeEach
    public void init() throws SQLException {
        Connection connection = mock(Connection.class);
        db = mock(DuckDbConnectionState.class);
        when(db.getConnection()).thenReturn(connection);
        stmt = mock(Statement.class);
        when(connection.createStatement()).thenReturn(stmt);
    }

    @Test
    public void shouldParseAllWrite1Events() throws WorkloadException, SQLException {
        // Arrange
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.getLong(1))
            .thenReturn(1L)
            .thenReturn(2L)
            .thenReturn(3L)
            .thenReturn(4L);
        when(rs.getString(2))
            .thenReturn("A")
            .thenReturn("B")
            .thenReturn("C")
            .thenReturn("D");
        when(rs.getLong(3))
            .thenReturn(1001L)
            .thenReturn(1002L)
            .thenReturn(1003L)
            .thenReturn(1004L);
        when(rs.getTimestamp(4))
            .thenReturn(Timestamp.valueOf("2012-07-29 08:52:02.735"))
            .thenReturn(Timestamp.valueOf("2012-07-29 08:52:02.735"))
            .thenReturn(Timestamp.valueOf("2012-07-29 08:52:02.735"))
            .thenReturn(Timestamp.valueOf("2012-07-29 08:52:02.735"));
        when(rs.getBoolean(5))
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false)
            .thenReturn(false);
        when(rs.getString(6))
            .thenReturn("AA")
            .thenReturn("BB")
            .thenReturn("CC")
            .thenReturn("DD");
        EventStreamReader.EventDecoder<Operation> decoder = new UpdateEventStreamReader.EventDecoderWrite1();
        ParquetLoader loader = new ParquetLoader(db);
        Iterator<Operation> opStream = loader.loadOperationStream("/somepath", decoder);

        // Act
        Iterator<Operation> reader = new QueryEventStreamReader(
            opStream
        );

        // Assert
        Write1 operation;

        operation = (Write1) reader.next();
        assertThat(operation.getPersonId(), is(1L));
        assertThat(operation.getPersonName(), is("A"));
        assertThat(operation.getAccountId(), is(1001L));
        assertThat(operation.getCurrentTime().getTime(),
            equalTo(Timestamp.valueOf("2012-07-29 08:52:02.735").getTime()));
        assertThat(operation.getAccountBlocked(), is(true));
        assertThat(operation.getAccountType(), is("AA"));

        operation = (Write1) reader.next();
        assertThat(operation.getPersonId(), is(2L));
        assertThat(operation.getPersonName(), is("B"));
        assertThat(operation.getAccountId(), is(1002L));
        assertThat(operation.getCurrentTime().getTime(),
            equalTo(Timestamp.valueOf("2012-07-29 08:52:02.735").getTime()));
        assertThat(operation.getAccountBlocked(), is(true));
        assertThat(operation.getAccountType(), is("BB"));

        operation = (Write1) reader.next();
        assertThat(operation.getPersonId(), is(3L));
        assertThat(operation.getPersonName(), is("C"));
        assertThat(operation.getAccountId(), is(1003L));
        assertThat(operation.getCurrentTime().getTime(),
            equalTo(Timestamp.valueOf("2012-07-29 08:52:02.735").getTime()));
        assertThat(operation.getAccountBlocked(), is(false));
        assertThat(operation.getAccountType(), is("CC"));

        operation = (Write1) reader.next();
        assertThat(operation.getPersonId(), is(4L));
        assertThat(operation.getPersonName(), is("D"));
        assertThat(operation.getAccountId(), is(1004L));
        assertThat(operation.getCurrentTime().getTime(),
            equalTo(Timestamp.valueOf("2012-07-29 08:52:02.735").getTime()));
        assertThat(operation.getAccountBlocked(), is(false));
        assertThat(operation.getAccountType(), is("DD"));

        assertThat(reader.hasNext(), is(false));
    }

    @Test
    public void shouldParseAllWrite2Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite3Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite4Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite5Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite6Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite7Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite8Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite9Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite10Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite11Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite12Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite13Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }

    @Test
    public void shouldParseAllWrite14Events() throws WorkloadException, SQLException {
        // TODO add write events test
    }
}
