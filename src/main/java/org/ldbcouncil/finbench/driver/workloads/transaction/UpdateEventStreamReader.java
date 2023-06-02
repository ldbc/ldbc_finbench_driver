package org.ldbcouncil.finbench.driver.workloads.transaction;

import static java.lang.String.format;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader;
import org.ldbcouncil.finbench.driver.generator.EventStreamReader.EventDecoder;
import org.ldbcouncil.finbench.driver.truncation.TruncationOrder;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write13;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write14;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write15;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write16;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write17;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write18;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write19;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write9;

/**
 * Decoder for the update events. There are 14 write events.
 * Each event has a separate decoder that decodes a java.sql.Resultset
 */
public class UpdateEventStreamReader implements Iterator<Operation> {
    private final Iterator<Operation> operationStream;

    public UpdateEventStreamReader(Iterator<Operation> operationStream) {
        this.operationStream = operationStream;
    }

    /**
     * Get the first attribute of the ResultSet rs representing an operation's date.
     */
    static long getOperationDate(ResultSet rs) throws SQLException {
        return rs.getLong(1);
    }

    static long getDependencyTimeStamp(ResultSet rs) throws SQLException {
        return rs.getLong(2);
    }

    public static Map<Class<? extends Operation>, EventDecoder<Operation>> getDecoders() {
        Map<Class<? extends Operation>, EventDecoder<Operation>> decoders = new HashMap<>();
        decoders.put(Write1.class, new EventDecoderWrite1());
        decoders.put(Write2.class, new EventDecoderWrite2());
        decoders.put(Write3.class, new EventDecoderWrite3());
        decoders.put(Write4.class, new EventDecoderWrite4());
        decoders.put(Write5.class, new EventDecoderWrite5());
        decoders.put(Write6.class, new EventDecoderWrite6());
        decoders.put(Write7.class, new EventDecoderWrite7());
        decoders.put(Write8.class, new EventDecoderWrite8());
        decoders.put(Write9.class, new EventDecoderWrite9());
        decoders.put(Write10.class, new EventDecoderWrite10());
        decoders.put(Write11.class, new EventDecoderWrite11());
        decoders.put(Write12.class, new EventDecoderWrite12());
        decoders.put(Write13.class, new EventDecoderWrite13());
        decoders.put(Write14.class, new EventDecoderWrite14());
        decoders.put(Write15.class, new EventDecoderWrite15());
        decoders.put(Write16.class, new EventDecoderWrite16());
        decoders.put(Write17.class, new EventDecoderWrite17());
        decoders.put(Write18.class, new EventDecoderWrite18());
        decoders.put(Write19.class, new EventDecoderWrite19());
        decoders.put(ReadWrite1.class, new EventDecoderReadWrite1());
        decoders.put(ReadWrite2.class, new EventDecoderReadWrite2());
        decoders.put(ReadWrite3.class, new EventDecoderReadWrite3());
        return decoders;
    }

    @Override
    public boolean hasNext() {
        return operationStream.hasNext();
    }

    @Override
    public Operation next() {
        return operationStream.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException(format("%s does not support remove()", getClass().getSimpleName()));
    }

    public static class EventDecoderWrite1 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write1 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long personId = rs.getLong(3);
                String personName = rs.getString(4);
                boolean isBlocked = rs.getBoolean(5);

                Operation operation = new Write1(
                    personId,
                    personName,
                    isBlocked);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write1: %s", e));
            }
        }
    }

    public static class EventDecoderWrite2 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write2 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long companyId = rs.getLong(3);
                String companyName = rs.getString(4);
                boolean isBlocked = rs.getBoolean(5);

                Operation operation = new Write2(
                    companyId,
                    companyName,
                    isBlocked);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write2: %s", e));
            }
        }
    }

    public static class EventDecoderWrite3 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write3 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long mediumId = rs.getLong(3);
                String mediumType = rs.getString(4);
                boolean isBlocked = rs.getBoolean(5);

                Operation operation = new Write3(
                    mediumId,
                    mediumType,
                    isBlocked);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write3: %s", e));
            }
        }
    }

    public static class EventDecoderWrite4 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write4 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long personId = rs.getLong(3);
                long accountId = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);
                String accountType = rs.getString(5);
                boolean accountBlocked = rs.getBoolean(6);

                Operation operation = new Write4(
                    personId,
                    accountId,
                    time,
                    accountBlocked,
                    accountType);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write4: %s", e));
            }
        }
    }

    public static class EventDecoderWrite5 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write5 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long companyId = rs.getLong(3);
                long accountId = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);
                String accountType = rs.getString(5);
                boolean accountBlocked = rs.getBoolean(6);

                Operation operation = new Write5(
                    companyId,
                    accountId,
                    time,
                    accountBlocked,
                    accountType);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write5: %s", e));
            }
        }
    }

    public static class EventDecoderWrite6 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write6 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long personId = rs.getLong(3);
                long loanId = rs.getLong(4);
                double loanAmount = rs.getDouble(5);
                double balance = rs.getDouble(6);
                Date time = new Date(scheduledStartTimeAsMilli);

                Operation operation = new Write6(
                    personId,
                    loanId,
                    loanAmount,
                    balance,
                    time);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write6: %s", e));
            }
        }
    }

    public static class EventDecoderWrite7 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write7 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long companyId = rs.getLong(3);
                long loanId = rs.getLong(4);
                double loanAmount = rs.getDouble(5);
                double balance = rs.getDouble(6);
                Date time = new Date(scheduledStartTimeAsMilli);

                Operation operation = new Write7(
                    companyId,
                    loanId,
                    loanAmount,
                    balance,
                    time);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write7: %s", e));
            }
        }
    }

    public static class EventDecoderWrite8 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write8 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long personId = rs.getLong(3);
                long companyId = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);
                double ratio = rs.getDouble(5);

                Operation operation = new Write8(
                    personId,
                    companyId,
                    time,
                    ratio);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write8: %s", e));
            }
        }
    }

    public static class EventDecoderWrite9 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write9 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long companyId1 = rs.getLong(3);
                long companyId2 = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);
                double ratio = rs.getDouble(5);

                Operation operation = new Write9(
                    companyId1,
                    companyId2,
                    time,
                    ratio);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write9: %s", e));
            }
        }
    }

    public static class EventDecoderWrite10 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write10 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long personId1 = rs.getLong(3);
                long personId2 = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);

                Operation operation = new Write10(
                    personId1,
                    personId2,
                    time);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write10: %s", e));
            }
        }
    }

    public static class EventDecoderWrite11 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write11 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long companyId1 = rs.getLong(3);
                long companyId2 = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);

                Operation operation = new Write11(
                    companyId1,
                    companyId2,
                    time);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write11: %s", e));
            }
        }
    }

    public static class EventDecoderWrite12 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write12 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long accountId1 = rs.getLong(3);
                long accountId2 = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);
                double amount = rs.getDouble(5);

                Operation operation = new Write12(
                    accountId1,
                    accountId2,
                    time,
                    amount);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write12: %s", e));
            }
        }
    }

    public static class EventDecoderWrite13 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write13 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long accountId1 = rs.getLong(3);
                long accountId2 = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);
                double amount = rs.getDouble(5);

                Operation operation = new Write13(
                    accountId1,
                    accountId2,
                    time,
                    amount);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write13: %s", e));
            }
        }
    }

    public static class EventDecoderWrite14 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write14 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long accountId = rs.getLong(3);
                long loanId = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);
                double amount = rs.getDouble(5);

                Operation operation = new Write14(
                    accountId,
                    loanId,
                    time,
                    amount);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write14: %s", e));
            }
        }
    }

    public static class EventDecoderWrite15 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write15 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long loanId = rs.getLong(4);
                long accountId = rs.getLong(3);
                Date time = new Date(scheduledStartTimeAsMilli);
                double amount = rs.getDouble(5);

                Operation operation = new Write15(
                    loanId,
                    accountId,
                    time,
                    amount);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write15: %s", e));
            }
        }
    }

    public static class EventDecoderWrite16 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write16 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long mediumId = rs.getLong(3);
                long accountId = rs.getLong(4);
                Date time = new Date(scheduledStartTimeAsMilli);

                Operation operation = new Write16(
                    mediumId,
                    accountId,
                    time);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write16: %s", e));
            }
        }
    }

    public static class EventDecoderWrite17 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write17 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long accountId = rs.getLong(3);

                Operation operation = new Write17(
                    accountId);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write17: %s", e));
            }
        }
    }

    public static class EventDecoderWrite18 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write18 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long accountId = rs.getLong(3);

                Operation operation = new Write18(
                    accountId);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write18: %s", e));
            }
        }
    }

    public static class EventDecoderWrite19 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return Write19 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long personId = rs.getLong(3);

                Operation operation = new Write19(
                    personId);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for Write19: %s", e));
            }
        }
    }

    public static class EventDecoderReadWrite1 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ReadWrite1 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long srcId = rs.getLong(3);
                long dstId = rs.getLong(4);
                Date currentTime = new Date(scheduledStartTimeAsMilli);
                long amt = rs.getLong(5);
                Date startTime = new Date(rs.getLong(10));
                Date endTime = new Date(rs.getLong(11));

                Operation operation = new ReadWrite1(
                    srcId,
                    dstId,
                    currentTime,
                    amt,
                    startTime,
                    endTime);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ReadWrite1: %s", e));
            }
        }
    }

    public static class EventDecoderReadWrite2 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ReadWrite2 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long srcId = rs.getLong(3);
                long dstId = rs.getLong(4);
                Date currentTime = new Date(scheduledStartTimeAsMilli);
                long amt = rs.getLong(5);
                double amountThreshold = rs.getDouble(14);
                Date startTime = new Date(rs.getLong(10));
                Date endTime = new Date(rs.getLong(11));
                float ratioThreshold = rs.getFloat(15);
                int truncationLimit = rs.getInt(12);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(13));

                Operation operation = new ReadWrite2(
                    srcId,
                    dstId,
                    currentTime,
                    amt,
                    amountThreshold,
                    startTime,
                    endTime,
                    ratioThreshold,
                    truncationLimit,
                    truncationOrder);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ReadWrite2: %s", e));
            }
        }
    }

    public static class EventDecoderReadWrite3 implements EventStreamReader.EventDecoder<Operation> {
        /**
         * @param rs ResultSet object containing the row to decode
         * @return ReadWrite3 Object
         * @throws WorkloadException when an error occurs reading the resultSet
         */
        @Override
        public Operation decodeEvent(ResultSet rs) throws WorkloadException {
            try {
                long scheduledStartTimeAsMilli = getOperationDate(rs);
                long dependencyTimeStamp = getDependencyTimeStamp(rs);
                long srcId = rs.getLong(3);
                long dstId = rs.getLong(4);
                Date currentTime = new Date(scheduledStartTimeAsMilli);
                double threshold = rs.getDouble(10);
                Date startTime = new Date(rs.getLong(6));
                Date endTime = new Date(rs.getLong(7));
                int truncationLimit = rs.getInt(8);
                TruncationOrder truncationOrder = TruncationOrder.valueOf(rs.getString(9));

                Operation operation = new ReadWrite3(
                    srcId,
                    dstId,
                    currentTime,
                    threshold,
                    startTime,
                    endTime,
                    truncationLimit,
                    truncationOrder);
                operation.setScheduledStartTimeAsMilli(scheduledStartTimeAsMilli);
                operation.setTimeStamp(scheduledStartTimeAsMilli);
                operation.setDependencyTimeStamp(dependencyTimeStamp);
                return operation;
            } catch (SQLException e) {
                throw new WorkloadException(format("Error while decoding ResultSet for ReadWrite3: %s", e));
            }
        }
    }
}
