/*
 * Copyright © 2022 Linked Data Benchmark Council (info@ldbcouncil.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ldbcouncil.finbench.driver.workloads.transaction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.csv.DuckDbExtractor;
import org.ldbcouncil.finbench.driver.csv.FileLoader;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write1;


public class TransactionUpdateEventStreamReaderTest {
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

    @Test
    public void shouldParseAllWrite1Events() throws WorkloadException, SQLException {
        // Arrange
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.getLong(3))
            .thenReturn(1L)
            .thenReturn(2L)
            .thenReturn(3L)
            .thenReturn(4L);
        when(rs.getString(4))
            .thenReturn("A")
            .thenReturn("B")
            .thenReturn("C")
            .thenReturn("D");
        when(rs.getBoolean(5))
            .thenReturn(true)
            .thenReturn(true)
            .thenReturn(false)
            .thenReturn(false);
        EventStreamReader.EventDecoder<Operation> decoder = new UpdateEventStreamReader.EventDecoderWrite1();
        FileLoader loader = new FileLoader(db);
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
        assertThat(operation.getIsBlocked(), is(true));

        operation = (Write1) reader.next();
        assertThat(operation.getPersonId(), is(2L));
        assertThat(operation.getPersonName(), is("B"));
        assertThat(operation.getIsBlocked(), is(true));

        operation = (Write1) reader.next();
        assertThat(operation.getPersonId(), is(3L));
        assertThat(operation.getPersonName(), is("C"));
        assertThat(operation.getIsBlocked(), is(false));

        operation = (Write1) reader.next();
        assertThat(operation.getPersonId(), is(4L));
        assertThat(operation.getPersonName(), is("D"));
        assertThat(operation.getIsBlocked(), is(false));

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
