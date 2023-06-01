package org.ldbcouncil.finbench.driver.workloads.transaction;

import static java.lang.String.format;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader.EventDecoder;
import org.ldbcouncil.finbench.driver.truncation.TruncationOrder;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead9;

public class QueryEventStreamReader implements Iterator<Operation> {

    private final Iterator<Operation> operationStream;

    public QueryEventStreamReader(Iterator<Operation> operationStream) {
        this.operationStream = operationStream;
    }

    public static Map<Integer, EventDecoder<Operation>> getDecoders() {
        Map<Integer, EventDecoder<Operation>> decoders = new HashMap<>();
        decoders.put(ComplexRead1.TYPE, new ComplexRead1Decoder());
        decoders.put(ComplexRead2.TYPE, new ComplexRead2Decoder());
        decoders.put(ComplexRead3.TYPE, new ComplexRead3Decoder());
        decoders.put(ComplexRead4.TYPE, new ComplexRead4Decoder());
        decoders.put(ComplexRead5.TYPE, new ComplexRead5Decoder());
        decoders.put(ComplexRead6.TYPE, new ComplexRead6Decoder());
        decoders.put(ComplexRead7.TYPE, new ComplexRead7Decoder());
        decoders.put(ComplexRead8.TYPE, new ComplexRead8Decoder());
        decoders.put(ComplexRead9.TYPE, new ComplexRead9Decoder());
        decoders.put(ComplexRead10.TYPE, new ComplexRead10Decoder());
        decoders.put(ComplexRead11.TYPE, new ComplexRead11Decoder());
        decoders.put(ComplexRead12.TYPE, new ComplexRead12Decoder());
        return decoders;
    }

    private static long convertStringToLong(String dateString) {
        return Instant.parse(dateString.replace(" ", "T") + "Z").toEpochMilli();
    }

    @Override
    public boolean hasNext() {
        return operationStream.hasNext();
    }

    @Override
    public Operation next() {
        Operation query = operationStream.next();
        Operation operation = query.newInstance();
        operation.setDependencyTimeStamp(query.dependencyTimeStamp());
        operation.setExpiryTimeStamp(query.expiryTimeStamp());
        return operation;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(format("%s does not support remove()", getClass().getSimpleName()));
    }

    public static class ComplexRead1Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead1 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                Date startTime = new Date(rs.getLong(2));
                Date endTime = new Date(rs.getLong(3));
                int truncationLimit = rs.getInt(4);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(5));
                // TODO dependencyTimeStamp & expiryTimeStamp, if Complex Queries need dependencyTimeStamp,
                //  we should add the time in the params
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead1(
                    id,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead1: %s", e), e);
            }
        }
    }

    public static class ComplexRead2Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead2 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                Date startTime = new Date(rs.getLong(2));
                Date endTime = new Date(rs.getLong(3));
                int truncationLimit = rs.getInt(4);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(5));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead2(
                    id,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead2: %s", e));
            }
        }
    }

    public static class ComplexRead3Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead3 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id1 = rs.getLong(1);
                long id2 = rs.getLong(2);
                Date startTime = new Date(rs.getLong(3));
                Date endTime = new Date(rs.getLong(4));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead3(
                    id1,
                    id2,
                    startTime,
                    endTime
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead3: %s", e));
            }
        }
    }

    public static class ComplexRead4Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead4 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id1 = rs.getLong(1);
                long id2 = rs.getLong(2);
                Date startTime = new Date(rs.getLong(3));
                Date endTime = new Date(rs.getLong(4));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead4(
                    id1,
                    id2,
                    startTime,
                    endTime
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead4: %s", e));
            }
        }
    }

    public static class ComplexRead5Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead5 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                Date startTime = new Date(rs.getLong(2));
                Date endTime = new Date(rs.getLong(3));
                int truncationLimit = rs.getInt(4);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(5));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead5(
                    id,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead5: %s", e));
            }
        }
    }

    public static class ComplexRead6Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead6 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                double threshold1 = rs.getDouble(2);
                double threshold2 = rs.getDouble(3);
                Date startTime = new Date(rs.getLong(4));
                Date endTime = new Date(rs.getLong(5));
                int truncationLimit = rs.getInt(6);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(7));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead6(
                    id,
                    threshold1,
                    threshold2,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead6: %s", e));
            }
        }
    }

    public static class ComplexRead7Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead7 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                double threshold = rs.getLong(2);
                Date startTime = new Date(rs.getLong(3));
                Date endTime = new Date(rs.getLong(4));
                int truncationLimit = rs.getInt(5);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(6));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead7(
                    id,
                    threshold,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead7: %s", e));
            }
        }
    }

    public static class ComplexRead8Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead8 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                float threshold = rs.getFloat(2);
                Date startTime = new Date(rs.getLong(3));
                Date endTime = new Date(rs.getLong(4));
                int truncationLimit = rs.getInt(5);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(6));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead8(
                    id,
                    threshold,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead8: %s", e));
            }
        }
    }

    public static class ComplexRead9Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead9 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                double threshold = rs.getDouble(2);
                Date startTime = new Date(rs.getLong(3));
                Date endTime = new Date(rs.getLong(4));
                int truncationLimit = rs.getInt(5);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(6));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead9(
                    id,
                    threshold,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead9: %s", e));
            }
        }
    }

    public static class ComplexRead10Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead10 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id1 = rs.getLong(1);
                long id2 = rs.getLong(2);
                Date startTime = new Date(rs.getLong(3));
                Date endTime = new Date(rs.getLong(4));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead10(
                    id1,
                    id2,
                    startTime,
                    endTime
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead10: %s", e));
            }
        }
    }

    public static class ComplexRead11Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead11 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                Date startTime = new Date(rs.getLong(2));
                Date endTime = new Date(rs.getLong(3));
                int truncationLimit = rs.getInt(4);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(5));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead11(
                    id,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead11: %s", e));
            }
        }
    }

    public static class ComplexRead12Decoder implements EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ComplexRead12 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long id = rs.getLong(1);
                Date startTime = new Date(rs.getLong(2));
                Date endTime = new Date(rs.getLong(3));
                int truncationLimit = rs.getInt(4);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(5));
                long dependencyTimeStamp = 0;
                long expiryTimeStamp = Long.MAX_VALUE;
                Operation query = new ComplexRead12(
                    id,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder
                );
                query.setDependencyTimeStamp(dependencyTimeStamp);
                query.setExpiryTimeStamp(expiryTimeStamp);
                return query;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ComplexRead12: %s", e));
            }
        }
    }
}
