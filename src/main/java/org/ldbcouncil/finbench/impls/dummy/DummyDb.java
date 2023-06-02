package org.ldbcouncil.finbench.impls.dummy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ldbcouncil.finbench.driver.Db;
import org.ldbcouncil.finbench.driver.DbConnectionState;
import org.ldbcouncil.finbench.driver.DbException;
import org.ldbcouncil.finbench.driver.OperationHandler;
import org.ldbcouncil.finbench.driver.ResultReporter;
import org.ldbcouncil.finbench.driver.log.LoggingService;
import org.ldbcouncil.finbench.driver.result.Path;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead10Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead11Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead12Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead2Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead3Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead4Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead6Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead7Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead8Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead9;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead9Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead1Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead6;
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

public class DummyDb extends Db {
    static Logger logger = LogManager.getLogger("DummyDb");

    @Override
    protected void onInit(Map<String, String> map, LoggingService loggingService) throws DbException {
        logger.info("DummyDb initialized");

        // complex reads
        registerOperationHandler(ComplexRead1.class, ComplexRead1Handler.class);
        registerOperationHandler(ComplexRead2.class, ComplexRead2Handler.class);
        registerOperationHandler(ComplexRead3.class, ComplexRead3Handler.class);
        registerOperationHandler(ComplexRead4.class, ComplexRead4Handler.class);
        registerOperationHandler(ComplexRead5.class, ComplexRead5Handler.class);
        registerOperationHandler(ComplexRead6.class, ComplexRead6Handler.class);
        registerOperationHandler(ComplexRead7.class, ComplexRead7Handler.class);
        registerOperationHandler(ComplexRead8.class, ComplexRead8Handler.class);
        registerOperationHandler(ComplexRead9.class, ComplexRead9Handler.class);
        registerOperationHandler(ComplexRead10.class, ComplexRead10Handler.class);
        registerOperationHandler(ComplexRead11.class, ComplexRead11Handler.class);
        registerOperationHandler(ComplexRead12.class, ComplexRead12Handler.class);

        // simple reads
        registerOperationHandler(SimpleRead1.class, SimpleRead1Handler.class);
        registerOperationHandler(SimpleRead2.class, SimpleRead2Handler.class);
        registerOperationHandler(SimpleRead3.class, SimpleRead3Handler.class);
        registerOperationHandler(SimpleRead4.class, SimpleRead4Handler.class);
        registerOperationHandler(SimpleRead5.class, SimpleRead5Handler.class);
        registerOperationHandler(SimpleRead6.class, SimpleRead6Handler.class);

        // writes
        registerOperationHandler(Write1.class, Write1Handler.class);
        registerOperationHandler(Write2.class, Write2Handler.class);
        registerOperationHandler(Write3.class, Write3Handler.class);
        registerOperationHandler(Write4.class, Write4Handler.class);
        registerOperationHandler(Write5.class, Write5Handler.class);
        registerOperationHandler(Write6.class, Write6Handler.class);
        registerOperationHandler(Write7.class, Write7Handler.class);
        registerOperationHandler(Write8.class, Write8Handler.class);
        registerOperationHandler(Write9.class, Write9Handler.class);
        registerOperationHandler(Write10.class, Write10Handler.class);
        registerOperationHandler(Write11.class, Write11Handler.class);
        registerOperationHandler(Write12.class, Write12Handler.class);
        registerOperationHandler(Write13.class, Write13Handler.class);
        registerOperationHandler(Write14.class, Write14Handler.class);
        registerOperationHandler(Write15.class, Write15Handler.class);
        registerOperationHandler(Write16.class, Write16Handler.class);
        registerOperationHandler(Write17.class, Write17Handler.class);
        registerOperationHandler(Write18.class, Write18Handler.class);
        registerOperationHandler(Write19.class, Write19Handler.class);

        // read-writes
        registerOperationHandler(ReadWrite1.class, ReadWrite1Handler.class);
        registerOperationHandler(ReadWrite2.class, ReadWrite2Handler.class);
        registerOperationHandler(ReadWrite3.class, ReadWrite3Handler.class);
    }

    @Override
    protected void onClose() throws IOException {
        logger.info("DummyDb closed");
    }

    @Override
    protected DbConnectionState getConnectionState() throws DbException {
        return null;
    }

    public static class ComplexRead1Handler implements OperationHandler<ComplexRead1, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead1 cr1, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr1.toString());

            //The output of ComplexReads is the input of SimpleReads,
            // so ComplexRead1 outputs some results for verify that SimpleReads are correct.
            List<ComplexRead1Result> complexRead1Results = new ArrayList<>();
            complexRead1Results.add(new ComplexRead1Result(1, 0, 0, "101"));
            complexRead1Results.add(new ComplexRead1Result(2, 0, 0, "102"));
            complexRead1Results.add(new ComplexRead1Result(3, 0, 0,  "103"));
            complexRead1Results.add(new ComplexRead1Result(4, 0, 0,  "104"));
            complexRead1Results.add(new ComplexRead1Result(5, 0, 0,  "105"));
            complexRead1Results.add(new ComplexRead1Result(6, 0, 0,  "106"));
            complexRead1Results.add(new ComplexRead1Result(7, 0, 0,  "107"));
            complexRead1Results.add(new ComplexRead1Result(8, 0, 0,  "108"));
            complexRead1Results.add(new ComplexRead1Result(9, 0, 0,  "109"));
            complexRead1Results.add(new ComplexRead1Result(10, 0, 0,  "1010"));
            complexRead1Results.add(new ComplexRead1Result(11, 0, 0,  "1011"));
            complexRead1Results.add(new ComplexRead1Result(12, 0, 0,  "1012"));
            complexRead1Results.add(new ComplexRead1Result(13, 0, 0,  "1013"));
            complexRead1Results.add(new ComplexRead1Result(14, 0, 0,  "1014"));
            complexRead1Results.add(new ComplexRead1Result(15, 0, 0,  "1015"));
            complexRead1Results.add(new ComplexRead1Result(16, 0, 0,  "1016"));
            complexRead1Results.add(new ComplexRead1Result(17, 0, 0,  "1017"));
            complexRead1Results.add(new ComplexRead1Result(18, 0, 0,  "1018"));
            resultReporter.report(complexRead1Results.size(), complexRead1Results, cr1);
        }
    }

    public static class ComplexRead2Handler implements OperationHandler<ComplexRead2, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead2 cr2, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr2.toString());
            List<ComplexRead2Result> complexRead2Results = new ArrayList<>();
            complexRead2Results.add(new ComplexRead2Result(0, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(1, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(2, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(3, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(4, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(5, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(6, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(7, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(8, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(9, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(10, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(11, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(12, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(13, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(14, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(15, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(16, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(17, 0, 0));
            complexRead2Results.add(new ComplexRead2Result(18, 0, 0));
            resultReporter.report(complexRead2Results.size(), complexRead2Results, cr2);
        }
    }

    public static class ComplexRead3Handler implements OperationHandler<ComplexRead3, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead3 cr3, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr3.toString());
            List<ComplexRead3Result> complexRead3Results = new ArrayList<>();
            ComplexRead3Result complexRead3Result = new ComplexRead3Result(3);
            complexRead3Results.add(complexRead3Result);
            resultReporter.report(complexRead3Results.size(), complexRead3Results, cr3);
        }
    }

    public static class ComplexRead4Handler implements OperationHandler<ComplexRead4, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead4 cr4, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr4.toString());
            List<ComplexRead4Result> complexRead4Results = new ArrayList<>();
            ComplexRead4Result complexRead4Result = new ComplexRead4Result(4, 0, 
                0, 0, 0, 0, 0);
            complexRead4Results.add(complexRead4Result);
            resultReporter.report(complexRead4Results.size(), complexRead4Results, cr4);
        }
    }

    public static class ComplexRead5Handler implements OperationHandler<ComplexRead5, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead5 cr5, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr5.toString());
            List<ComplexRead5Result> complexRead5Results = new ArrayList<>();
            List<Long> path1 =  new ArrayList<>();
            path1.add(1L);
            path1.add(2L);
            path1.add(3L);
            List<Long> path2 =  new ArrayList<>();
            path2.add(2L);
            path2.add(3L);
            path2.add(4L);
            complexRead5Results.add(new ComplexRead5Result(new Path(path1)));
            complexRead5Results.add(new ComplexRead5Result(new Path(path2)));

            // validation
            // complexRead5Results.add(new ComplexRead5Result(new Path(path2)));
            // complexRead5Results.add(new ComplexRead5Result(new Path(path1)));
            resultReporter.report(complexRead5Results.size(), complexRead5Results, cr5);
        }
    }

    public static class ComplexRead6Handler implements OperationHandler<ComplexRead6, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead6 cr6, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr6.toString());
            List<ComplexRead6Result> complexRead6Results = new ArrayList<>();
            ComplexRead6Result complexRead6Result = new ComplexRead6Result(6, 0, 0);
            complexRead6Results.add(complexRead6Result);
            resultReporter.report(complexRead6Results.size(), complexRead6Results, cr6);
        }
    }

    public static class ComplexRead7Handler implements OperationHandler<ComplexRead7, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead7 cr7, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr7.toString());
            List<ComplexRead7Result> complexRead7Results = new ArrayList<>();
            ComplexRead7Result complexRead7Result = new ComplexRead7Result(0, 0, 0);
            complexRead7Results.add(complexRead7Result);
            resultReporter.report(complexRead7Results.size(), complexRead7Results, cr7);
        }
    }

    public static class ComplexRead8Handler implements OperationHandler<ComplexRead8, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead8 cr8, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr8.toString());
            List<ComplexRead8Result> complexRead8Results = new ArrayList<>();
            ComplexRead8Result complexRead8Result = new ComplexRead8Result(0, 0, 0);
            complexRead8Results.add(complexRead8Result);
            resultReporter.report(complexRead8Results.size(), complexRead8Results, cr8);
        }
    }

    public static class ComplexRead9Handler implements OperationHandler<ComplexRead9, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead9 cr9, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr9.toString());
            List<ComplexRead9Result> complexRead9Results = new ArrayList<>();
            ComplexRead9Result complexRead9Result = new ComplexRead9Result(9, 0, 0);
            complexRead9Results.add(complexRead9Result);
            resultReporter.report(complexRead9Results.size(), complexRead9Results, cr9);
        }
    }

    public static class ComplexRead10Handler implements OperationHandler<ComplexRead10, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead10 cr10, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr10.toString());
            List<ComplexRead10Result> complexRead10Results = new ArrayList<>();
            ComplexRead10Result complexRead10Result = new ComplexRead10Result(0);
            complexRead10Results.add(complexRead10Result);
            resultReporter.report(complexRead10Results.size(), complexRead10Results, cr10);
        }
    }

    public static class ComplexRead11Handler implements OperationHandler<ComplexRead11, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead11 cr11, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr11.toString());
            List<ComplexRead11Result> complexRead11Results = new ArrayList<>();
            ComplexRead11Result complexRead11Result = new ComplexRead11Result(0, 0);
            complexRead11Results.add(complexRead11Result);
            resultReporter.report(complexRead11Results.size(), complexRead11Results, cr11);
        }
    }

    public static class ComplexRead12Handler implements OperationHandler<ComplexRead12, DummyDbConnectionState> {
        @Override
        public void executeOperation(ComplexRead12 cr12, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(cr12.toString());
            List<ComplexRead12Result> complexRead12Results = new ArrayList<>();
            ComplexRead12Result complexRead12Result = new ComplexRead12Result(0, 0);
            complexRead12Results.add(complexRead12Result);
            resultReporter.report(complexRead12Results.size(), complexRead12Results, cr12);
        }
    }

    public static class SimpleRead1Handler implements OperationHandler<SimpleRead1, DummyDbConnectionState> {
        @Override
        public void executeOperation(SimpleRead1 sr1, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(sr1.toString());
            List<SimpleRead1Result> simpleRead1Results = new ArrayList<>();
            simpleRead1Results.add(new SimpleRead1Result(new Date(1), true, "a"));
            resultReporter.report(1, simpleRead1Results, sr1);
        }
    }

    public static class SimpleRead2Handler implements OperationHandler<SimpleRead2, DummyDbConnectionState> {
        @Override
        public void executeOperation(SimpleRead2 sr2, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(sr2.toString());
            resultReporter.report(0, Collections.EMPTY_LIST, sr2);
        }
    }

    public static class SimpleRead3Handler implements OperationHandler<SimpleRead3, DummyDbConnectionState> {
        @Override
        public void executeOperation(SimpleRead3 sr3, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(sr3.toString());
            resultReporter.report(0, Collections.EMPTY_LIST, sr3);
        }
    }

    public static class SimpleRead4Handler implements OperationHandler<SimpleRead4, DummyDbConnectionState> {
        @Override
        public void executeOperation(SimpleRead4 sr4, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(sr4.toString());
            resultReporter.report(0, Collections.EMPTY_LIST, sr4);
        }
    }

    public static class SimpleRead5Handler implements OperationHandler<SimpleRead5, DummyDbConnectionState> {
        @Override
        public void executeOperation(SimpleRead5 sr5, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(sr5.toString());
            resultReporter.report(0, Collections.EMPTY_LIST, sr5);
        }
    }

    public static class SimpleRead6Handler implements OperationHandler<SimpleRead6, DummyDbConnectionState> {
        @Override
        public void executeOperation(SimpleRead6 sr6, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(sr6.toString());
            resultReporter.report(0, Collections.EMPTY_LIST, sr6);
        }
    }

    public static class Write1Handler implements OperationHandler<Write1, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write1 w1, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w1.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w1);
        }
    }

    public static class Write2Handler implements OperationHandler<Write2, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write2 w2, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w2.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w2);
        }
    }

    public static class Write3Handler implements OperationHandler<Write3, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write3 w3, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w3.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w3);
        }
    }

    public static class Write4Handler implements OperationHandler<Write4, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write4 w4, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w4.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w4);
        }
    }

    public static class Write5Handler implements OperationHandler<Write5, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write5 w5, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w5.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w5);
        }
    }

    public static class Write6Handler implements OperationHandler<Write6, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write6 w6, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w6.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w6);
        }
    }

    public static class Write7Handler implements OperationHandler<Write7, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write7 w7, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w7.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w7);
        }
    }

    public static class Write8Handler implements OperationHandler<Write8, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write8 w8, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w8.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w8);
        }
    }

    public static class Write9Handler implements OperationHandler<Write9, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write9 w9, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w9.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w9);
        }
    }

    public static class Write10Handler implements OperationHandler<Write10, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write10 w10, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w10.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w10);
        }
    }

    public static class Write11Handler implements OperationHandler<Write11, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write11 w11, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w11.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w11);
        }
    }

    public static class Write12Handler implements OperationHandler<Write12, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write12 w12, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w12.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w12);
        }
    }

    public static class Write13Handler implements OperationHandler<Write13, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write13 w13, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w13.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w13);
        }
    }

    public static class Write14Handler implements OperationHandler<Write14, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write14 w14, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w14.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w14);
        }
    }

    public static class Write15Handler implements OperationHandler<Write15, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write15 w15, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w15.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w15);
        }
    }

    public static class Write16Handler implements OperationHandler<Write16, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write16 w16, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w16.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w16);
        }
    }

    public static class Write17Handler implements OperationHandler<Write17, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write17 w17, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w17.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w17);
        }
    }

    public static class Write18Handler implements OperationHandler<Write18, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write18 w18, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w18.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w18);
        }
    }

    public static class Write19Handler implements OperationHandler<Write19, DummyDbConnectionState> {
        @Override
        public void executeOperation(Write19 w19, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(w19.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, w19);
        }
    }

    public static class ReadWrite1Handler implements OperationHandler<ReadWrite1, DummyDbConnectionState> {
        @Override
        public void executeOperation(ReadWrite1 rw1, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(rw1.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, rw1);
        }
    }

    public static class ReadWrite2Handler implements OperationHandler<ReadWrite2, DummyDbConnectionState> {
        @Override
        public void executeOperation(ReadWrite2 rw2, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {

            resultReporter.report(0, LdbcNoResult.INSTANCE, rw2);
        }
    }

    public static class ReadWrite3Handler implements OperationHandler<ReadWrite3, DummyDbConnectionState> {
        @Override
        public void executeOperation(ReadWrite3 rw3, DummyDbConnectionState dummyDbConnectionState,
                                     ResultReporter resultReporter) throws DbException {
            DummyDb.logger.info(rw3.toString());
            resultReporter.report(0, LdbcNoResult.INSTANCE, rw3);
        }
    }
}
