package org.ldbcouncil.finbench.driver.workloads.transaction;

import static java.lang.String.format;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Ordering;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.ldbcouncil.finbench.driver.ChildOperationGenerator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.generator.RandomDataGeneratorFactory;
import org.ldbcouncil.finbench.driver.util.Tuple;
import org.ldbcouncil.finbench.driver.util.Tuple2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead13;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1Result;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead9;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ReadWrite3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.SimpleRead8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write10;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write11;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write12;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write13;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write14;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write15;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write2;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write3;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write4;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write6;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write7;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write8;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.Write9;

public class LdbcFinbenchSimpleReadGenerator implements ChildOperationGenerator {
    private final double initialProbability;
    private final LdbcSimpleQueryFactory[] simpleQueryFactories;
    private final double[] probabilityDegradationFactors;
    private final Queue<Long> accountIdBuffer;
    private final Queue<Long> personIdBuffer;
    private final Queue<Long> companyIdBuffer;
    private final long[] interleavesAsMilli;
    private final BufferReplenishFun bufferReplenishFun;

    public LdbcFinbenchSimpleReadGenerator(double initialProbability,
                                           double probabilityDegradationFactor,
                                           long updateInterleaveAsMilli,
                                           Set<Class<? extends Operation>> enabledSimpleReadOperationTypes,
                                           double compressionRatio,
                                           Queue<Long> accountIdBuffer,
                                           Queue<Long> personIdBuffer,
                                           Queue<Long> companyIdBuffer,
                                           RandomDataGeneratorFactory randomFactory,
                                           Map<Integer, Long> longReadInterleaves,
                                           ScheduledStartTimePolicy scheduledStartTimePolicy,
                                           BufferReplenishFun bufferReplenishFun) {
        this.initialProbability = initialProbability;
        this.accountIdBuffer = accountIdBuffer;
        this.personIdBuffer = personIdBuffer;
        this.companyIdBuffer = companyIdBuffer;
        this.bufferReplenishFun = bufferReplenishFun;

        // TODO the max TYPE
        int maxReadOperationType =
            Ordering.<Integer>natural().max(ComplexRead13.TYPE, SimpleRead8.TYPE) + 1;
        this.interleavesAsMilli = new long[maxReadOperationType];
        for (Integer longReadOperationType : longReadInterleaves.keySet()) {
            this.interleavesAsMilli[longReadOperationType] =
                Math.round(Math.ceil(compressionRatio * longReadInterleaves.get(longReadOperationType)));
        }
        this.interleavesAsMilli[SimpleRead1.TYPE] =
            Math.round(Math.ceil(compressionRatio * updateInterleaveAsMilli));
        this.interleavesAsMilli[SimpleRead2.TYPE] =
            Math.round(Math.ceil(compressionRatio * updateInterleaveAsMilli));
        this.interleavesAsMilli[SimpleRead3.TYPE] =
            Math.round(Math.ceil(compressionRatio * updateInterleaveAsMilli));
        this.interleavesAsMilli[SimpleRead4.TYPE] =
            Math.round(Math.ceil(compressionRatio * updateInterleaveAsMilli));
        this.interleavesAsMilli[SimpleRead5.TYPE] =
            Math.round(Math.ceil(compressionRatio * updateInterleaveAsMilli));
        this.interleavesAsMilli[SimpleRead6.TYPE] =
            Math.round(Math.ceil(compressionRatio * updateInterleaveAsMilli));
        this.interleavesAsMilli[SimpleRead7.TYPE] =
            Math.round(Math.ceil(compressionRatio * updateInterleaveAsMilli));
        this.interleavesAsMilli[SimpleRead8.TYPE] =
            Math.round(Math.ceil(compressionRatio * updateInterleaveAsMilli));

        int maxOperationType = Ordering.<Integer>natural().max(ComplexRead13.TYPE, SimpleRead8.TYPE,
            ReadWrite3.TYPE) + 1;
        this.simpleQueryFactories = new LdbcSimpleQueryFactory[maxOperationType];
        this.probabilityDegradationFactors = new double[maxOperationType];
        for (int i = 0; i < probabilityDegradationFactors.length; i++) {
            probabilityDegradationFactors[i] = 0;
        }

        /*
        ENABLED
            S1_INDEX -> true/false
            S2_INDEX -> true/false
            S3_INDEX -> true/false
            S4_INDEX -> true/false
            S5_INDEX -> true/false
            S6_INDEX -> true/false
            S7_INDEX -> true/false
         */
        boolean[] enabledSimpleReads = new boolean[maxOperationType];
        enabledSimpleReads[SimpleRead1.TYPE] =
            enabledSimpleReadOperationTypes.contains(SimpleRead1.class);
        enabledSimpleReads[SimpleRead2.TYPE] =
            enabledSimpleReadOperationTypes.contains(SimpleRead2.class);
        enabledSimpleReads[SimpleRead3.TYPE] =
            enabledSimpleReadOperationTypes.contains(SimpleRead3.class);
        enabledSimpleReads[SimpleRead4.TYPE] =
            enabledSimpleReadOperationTypes.contains(SimpleRead4.class);
        enabledSimpleReads[SimpleRead5.TYPE] =
            enabledSimpleReadOperationTypes.contains(SimpleRead5.class);
        enabledSimpleReads[SimpleRead6.TYPE] =
            enabledSimpleReadOperationTypes.contains(SimpleRead6.class);
        enabledSimpleReads[SimpleRead7.TYPE] =
            enabledSimpleReadOperationTypes.contains(SimpleRead7.class);
        enabledSimpleReads[SimpleRead8.TYPE] =
            enabledSimpleReadOperationTypes.contains(SimpleRead8.class);

        /*
        MAPPING
            S1_INDEX -> S1
            S2_INDEX -> S2
            S3_INDEX -> S3
            S4_INDEX -> S4
            S5_INDEX -> S5
            S6_INDEX -> S6
            S7_INDEX -> S7
         */
        LdbcSimpleQueryFactory[] baseSimpleReadFactories = new LdbcSimpleQueryFactory[maxOperationType];
        baseSimpleReadFactories[SimpleRead1.TYPE] =
            new LdbcSimpleQuery1Factory(scheduledStartTimePolicy);
        baseSimpleReadFactories[SimpleRead2.TYPE] =
            new LdbcSimpleQuery2Factory(scheduledStartTimePolicy);
        baseSimpleReadFactories[SimpleRead3.TYPE] =
            new LdbcSimpleQuery3Factory(scheduledStartTimePolicy);
        baseSimpleReadFactories[SimpleRead4.TYPE] =
            new LdbcSimpleQuery4Factory(scheduledStartTimePolicy);
        baseSimpleReadFactories[SimpleRead5.TYPE] =
            new LdbcSimpleQuery5Factory(scheduledStartTimePolicy);
        baseSimpleReadFactories[SimpleRead6.TYPE] =
            new LdbcSimpleQuery6Factory(scheduledStartTimePolicy);
        baseSimpleReadFactories[SimpleRead7.TYPE] =
            new LdbcSimpleQuery7Factory(scheduledStartTimePolicy);
        baseSimpleReadFactories[SimpleRead8.TYPE] =
            new LdbcSimpleQuery7Factory(scheduledStartTimePolicy);

        /*
        FACTORIES
            S1_INDEX -> <if> (false == ENABLED[S1]) <then> ERROR
            S2_INDEX -> <if> (false == ENABLED[S2]) <then> ERROR
            S3_INDEX -> <if> (false == ENABLED[S3]) <then> ERROR
            S4_INDEX -> <if> (false == ENABLED[S4]) <then> ERROR
            S5_INDEX -> <if> (false == ENABLED[S5]) <then> ERROR
            S6_INDEX -> <if> (false == ENABLED[S6]) <then> ERROR
            S7_INDEX -> <if> (false == ENABLED[S7]) <then> ERROR
         */
        if (!enabledSimpleReads[SimpleRead1.TYPE]) {
            simpleQueryFactories[SimpleRead1.TYPE] =
                new ErrorFactory(SimpleRead1.class);
        }
        if (!enabledSimpleReads[SimpleRead2.TYPE]) {
            simpleQueryFactories[SimpleRead2.TYPE] = new ErrorFactory(SimpleRead2.class);
        }
        if (!enabledSimpleReads[SimpleRead3.TYPE]) {
            simpleQueryFactories[SimpleRead3.TYPE] =
                new ErrorFactory(SimpleRead3.class);
        }
        if (!enabledSimpleReads[SimpleRead4.TYPE]) {
            simpleQueryFactories[SimpleRead4.TYPE] =
                new ErrorFactory(SimpleRead4.class);
        }
        if (!enabledSimpleReads[SimpleRead5.TYPE]) {
            simpleQueryFactories[SimpleRead5.TYPE] =
                new ErrorFactory(SimpleRead5.class);
        }
        if (!enabledSimpleReads[SimpleRead6.TYPE]) {
            simpleQueryFactories[SimpleRead6.TYPE] =
                new ErrorFactory(SimpleRead6.class);
        }
        if (!enabledSimpleReads[SimpleRead7.TYPE]) {
            simpleQueryFactories[SimpleRead7.TYPE] =
                new ErrorFactory(SimpleRead7.class);
        }
        if (!enabledSimpleReads[SimpleRead8.TYPE]) {
            simpleQueryFactories[SimpleRead8.TYPE] =
                new ErrorFactory(SimpleRead8.class);
        }

        /*
        (FIRST_PERSON,FIRST_PERSON_INDEX) = ...
        (FIRST_MESSAGE,FIRST_MESSAGE_INDEX) = ...
         */
        Tuple2<Integer, LdbcSimpleQueryFactory> firstAccountQueryAndIndex =
            firstAccountQueryOrNoOp(enabledSimpleReadOperationTypes, randomFactory, 0, initialProbability,
                scheduledStartTimePolicy);
        Tuple2<Integer, LdbcSimpleQueryFactory> firstPersonQueryAndIndex =
            firstPersonQueryOrNoOp(enabledSimpleReadOperationTypes, randomFactory, 0, initialProbability,
                scheduledStartTimePolicy);
        Tuple2<Integer, LdbcSimpleQueryFactory> firstCompanyQueryAndIndex =
            firstCompanyQueryOrNoOp(enabledSimpleReadOperationTypes, randomFactory, 0, initialProbability,
                scheduledStartTimePolicy);


        LdbcSimpleQueryFactory firstAccountQuery = (Integer.MAX_VALUE == firstAccountQueryAndIndex._1())
            ? ((Integer.MAX_VALUE == firstPersonQueryAndIndex._1())
            ? firstCompanyQueryAndIndex._2() : firstPersonQueryAndIndex._2()) : firstAccountQueryAndIndex._2();

        LdbcSimpleQueryFactory firstPersonQuery = (Integer.MAX_VALUE == firstPersonQueryAndIndex._1())
            ? ((Integer.MAX_VALUE == firstCompanyQueryAndIndex._1())
            ? firstAccountQueryAndIndex._2() : firstCompanyQueryAndIndex._2()) : firstPersonQueryAndIndex._2();

        LdbcSimpleQueryFactory firstCompanyQuery = (Integer.MAX_VALUE == firstCompanyQueryAndIndex._1())
            ? ((Integer.MAX_VALUE == firstAccountQueryAndIndex._1())
            ? firstPersonQueryAndIndex._2() : firstAccountQueryAndIndex._2()) : firstCompanyQueryAndIndex._2();


        /*
        RANDOM_FIRST = RANDOM(FIRST_PERSON,FIRST_MESSAGE)
         */
        LdbcSimpleQueryFactory randomFirstQuery =
            selectRandomFirstSimpleQuery(firstAccountQueryAndIndex, firstPersonQueryAndIndex,
                firstCompanyQueryAndIndex);

        /*
        LAST_ACCOUNT_INDEX = ...
        LAST_PERSON_INDEX = ...
        LAST_COMPANY_INDEX = ...
         */
        int lastAccountQueryIndex = lastAccountQueryIndex(enabledSimpleReadOperationTypes);
        int lastPersonQueryIndex = lastPersonQueryIndex(enabledSimpleReadOperationTypes);
        int lastCompanyQueryIndex = lastCompanyQueryIndex(enabledSimpleReadOperationTypes);

        if (Integer.MAX_VALUE != lastAccountQueryIndex) {
            probabilityDegradationFactors[lastAccountQueryIndex] = probabilityDegradationFactor;
        }
        if (Integer.MAX_VALUE != lastPersonQueryIndex) {
            probabilityDegradationFactors[lastPersonQueryIndex] = probabilityDegradationFactor;
        }
        if (Integer.MAX_VALUE != lastCompanyQueryIndex) {
            probabilityDegradationFactors[lastCompanyQueryIndex] = probabilityDegradationFactor;
        }

        /*
        FACTORIES
            [LONG_READ_INDEXES] -> RANDOM_FIRST
            [UPDATE_INDEXES] -> NO_OP
         */
        simpleQueryFactories[ComplexRead1.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead2.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead3.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead4.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead5.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead6.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead7.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead8.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead9.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead10.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead11.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead12.TYPE] = randomFirstQuery;
        simpleQueryFactories[ComplexRead13.TYPE] = randomFirstQuery;

        simpleQueryFactories[Write1.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write2.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write3.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write4.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write5.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write6.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write7.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write8.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write9.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write10.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write11.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write12.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write13.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write14.TYPE] = new NoOpFactory();
        simpleQueryFactories[Write15.TYPE] = new NoOpFactory();

        simpleQueryFactories[ReadWrite1.TYPE] = new NoOpFactory();
        simpleQueryFactories[ReadWrite2.TYPE] = new NoOpFactory();
        simpleQueryFactories[ReadWrite3.TYPE] = new NoOpFactory();

        simpleQueryFactories[SimpleRead1.TYPE] = null;
        simpleQueryFactories[SimpleRead2.TYPE] = null;
        simpleQueryFactories[SimpleRead3.TYPE] = null;
        simpleQueryFactories[SimpleRead4.TYPE] = null;
        simpleQueryFactories[SimpleRead5.TYPE] = null;
        simpleQueryFactories[SimpleRead6.TYPE] = null;
        simpleQueryFactories[SimpleRead7.TYPE] = null;
        simpleQueryFactories[SimpleRead8.TYPE] = null;

        /*
        FACTORIES
            <if> (LAST_PERSON_INDEX != MAX_INTEGER) <then>
                LAST_PERSON_INDEX (S1/S2/S3) -> FIRST_MESSAGE
            <if> (LAST_MESSAGE_INDEX != MAX_INTEGER) <then>
                LAST_MESSAGE_INDEX (S4/S5/S6/S7) -> FIRST_PERSON
         */
        if (Integer.MAX_VALUE != lastAccountQueryIndex) {
            simpleQueryFactories[lastAccountQueryIndex] = firstPersonQuery;
        }
        if (Integer.MAX_VALUE != lastPersonQueryIndex) {
            simpleQueryFactories[lastPersonQueryIndex] = firstCompanyQuery;
        }
        if (Integer.MAX_VALUE != lastCompanyQueryIndex) {
            simpleQueryFactories[lastCompanyQueryIndex] = firstAccountQuery;
        }

        /*
        FACTORIES
            S1_INDEX -> <if> (ENABLED[S1_INDEX] && UNASSIGNED == FACTORIES[S1_INDEX]) <then>
                            index = indexOfNextEnabledAndUnassigned(MAPPING,S1_INDEX)
                            <if> (index > LAST_PERSON_INDEX) <then> FIRST_MESSAGE <else> MAPPING[index]
            S2_INDEX -> <if> (ENABLED[S2_INDEX] && UNASSIGNED == FACTORIES[S2_INDEX]) <then>
                            index = indexOfNextEnabledAndUnassigned(MAPPING,S2_INDEX)
                            <if> (index > LAST_PERSON_INDEX) <then> FIRST_MESSAGE <else> MAPPING[index]
            S3_INDEX -> // must have already been assigned, or is disabled
            S4_INDEX -> <if> (ENABLED[S4_INDEX] && UNASSIGNED == FACTORIES[S4_INDEX]) <then>
                            index = indexOfNextEnabledAndUnassigned(MAPPING,S4_INDEX)
                            <if> (index > LAST_MESSAGE_INDEX) <then> FIRST_PERSON <else> MAPPING[index]
            S5_INDEX -> <if> (ENABLED[S5_INDEX] && UNASSIGNED == FACTORIES[S5_INDEX]) <then>
                            index = indexOfNextEnabledAndUnassigned(MAPPING,S5_INDEX)
                            <if> (index > LAST_MESSAGE_INDEX) <then> FIRST_PERSON <else> MAPPING[index]
            S6_INDEX -> <if> (ENABLED[S6_INDEX] && UNASSIGNED == FACTORIES[S6_INDEX]) <then>
                            index = indexOfNextEnabledAndUnassigned(MAPPING,S6_INDEX)
                            <if> (index > LAST_MESSAGE_INDEX) <then> FIRST_PERSON <else> MAPPING[index]
            S7_INDEX -> // must have already been assigned, or is disabled
         */

        for (int i = SimpleRead1.TYPE; i <= SimpleRead6.TYPE; i++) {
            if (enabledSimpleReads[i] && null == simpleQueryFactories[i]) {
                int index = indexOfNextEnabled(enabledSimpleReads, i);
                if (index > lastAccountQueryIndex) {
                    simpleQueryFactories[i] = firstPersonQuery;
                } else {
                    simpleQueryFactories[i] = baseSimpleReadFactories[index];
                }
            }
        }
        for (int i = SimpleRead7.TYPE; i <= SimpleRead7.TYPE; i++) {
            if (enabledSimpleReads[i] && null == simpleQueryFactories[i]) {
                int index = indexOfNextEnabled(enabledSimpleReads, i);
                if (index > lastPersonQueryIndex) {
                    simpleQueryFactories[i] = firstCompanyQuery;
                } else {
                    simpleQueryFactories[i] = baseSimpleReadFactories[index];
                }
            }
        }
        for (int i = SimpleRead8.TYPE; i <= SimpleRead8.TYPE; i++) {
            if (enabledSimpleReads[i] && null == simpleQueryFactories[i]) {
                int index = indexOfNextEnabled(enabledSimpleReads, i);
                if (index > lastCompanyQueryIndex) {
                    simpleQueryFactories[i] = firstAccountQuery;
                } else {
                    simpleQueryFactories[i] = baseSimpleReadFactories[index];
                }
            }
        }
    }

    static Queue<Long> synchronizedCircularQueueBuffer(int bufferSize) {
        return Queues.synchronizedQueue(EvictingQueue.<Long>create(bufferSize));
    }

    static Queue<Long> constantBuffer(final long value) {
        return new Queue<Long>() {
            @Override
            public boolean add(Long longValue) {
                return true;
            }

            @Override
            public boolean offer(Long longValue) {
                return true;
            }

            @Override
            public boolean remove(Object o) {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public Long remove() {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public Long poll() {
                return value;
            }

            @Override
            public Long element() {
                return value;
            }

            @Override
            public Long peek() {
                return value;
            }

            @Override
            public int size() {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public boolean isEmpty() {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public boolean contains(Object o) {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public Iterator<Long> iterator() {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public Object[] toArray() {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public <T> T[] toArray(T[] a) {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public boolean addAll(Collection<? extends Long> c) {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException("Method not implemented");
            }

            @Override
            public void clear() {
                throw new UnsupportedOperationException("Method not implemented");
            }
        };
    }

    private int indexOfNextEnabled(boolean[] enabledSimpleReads, int simpleReadType) {
        for (int i = simpleReadType + 1; i <= SimpleRead8.TYPE; i++) {
            if (enabledSimpleReads[i]) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    private Tuple2<Integer, LdbcSimpleQueryFactory> firstAccountQueryOrNoOp(
        Set<Class<? extends Operation>> enabledSimpleReadOperationTypes,
        RandomDataGeneratorFactory randomFactory,
        double minProbability,
        double maxProbability,
        ScheduledStartTimePolicy scheduledStartTimePolicy) {
        if (enabledSimpleReadOperationTypes.contains(SimpleRead1.class)) {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                SimpleRead1.TYPE,
                new CoinTossingFactory(randomFactory.newRandom(),
                    new LdbcSimpleQuery1Factory(scheduledStartTimePolicy), minProbability, maxProbability)
            );
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead2.class)) {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                SimpleRead2.TYPE,
                new CoinTossingFactory(randomFactory.newRandom(),
                    new LdbcSimpleQuery2Factory(scheduledStartTimePolicy), minProbability, maxProbability)
            );
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead3.class)) {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                SimpleRead3.TYPE,
                new CoinTossingFactory(randomFactory.newRandom(),
                    new LdbcSimpleQuery3Factory(scheduledStartTimePolicy), minProbability, maxProbability)
            );
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead4.class)) {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                SimpleRead3.TYPE,
                new CoinTossingFactory(randomFactory.newRandom(),
                    new LdbcSimpleQuery3Factory(scheduledStartTimePolicy), minProbability, maxProbability)
            );
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead5.class)) {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                SimpleRead3.TYPE,
                new CoinTossingFactory(randomFactory.newRandom(),
                    new LdbcSimpleQuery3Factory(scheduledStartTimePolicy), minProbability, maxProbability)
            );
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead6.class)) {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                SimpleRead3.TYPE,
                new CoinTossingFactory(randomFactory.newRandom(),
                    new LdbcSimpleQuery3Factory(scheduledStartTimePolicy), minProbability, maxProbability)
            );
        } else {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                Integer.MAX_VALUE,
                new NoOpFactory()
            );
        }
    }

    private Tuple2<Integer, LdbcSimpleQueryFactory> firstPersonQueryOrNoOp(
        Set<Class<? extends Operation>> enabledSimpleReadOperationTypes,
        RandomDataGeneratorFactory randomFactory,
        double minProbability,
        double maxProbability,
        ScheduledStartTimePolicy scheduledStartTimePolicy) {
        if (enabledSimpleReadOperationTypes.contains(SimpleRead7.class)) {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                SimpleRead7.TYPE,
                new CoinTossingFactory(randomFactory.newRandom(),
                    new LdbcSimpleQuery1Factory(scheduledStartTimePolicy), minProbability, maxProbability)
            );
        } else {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                Integer.MAX_VALUE,
                new NoOpFactory()
            );
        }
    }

    private Tuple2<Integer, LdbcSimpleQueryFactory> firstCompanyQueryOrNoOp(
        Set<Class<? extends Operation>> enabledSimpleReadOperationTypes,
        RandomDataGeneratorFactory randomFactory,
        double minProbability,
        double maxProbability,
        ScheduledStartTimePolicy scheduledStartTimePolicy) {
        if (enabledSimpleReadOperationTypes.contains(SimpleRead8.class)) {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                SimpleRead8.TYPE,
                new CoinTossingFactory(randomFactory.newRandom(),
                    new LdbcSimpleQuery1Factory(scheduledStartTimePolicy), minProbability, maxProbability)
            );
        } else {
            return Tuple.<Integer, LdbcSimpleQueryFactory>tuple2(
                Integer.MAX_VALUE,
                new NoOpFactory()
            );
        }
    }

    private int lastAccountQueryIndex(Set<Class<? extends Operation>> enabledSimpleReadOperationTypes) {
        if (enabledSimpleReadOperationTypes.contains(SimpleRead1.class)) {
            return SimpleRead6.TYPE;
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead2.class)) {
            return SimpleRead5.TYPE;
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead3.class)) {
            return SimpleRead4.TYPE;
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead4.class)) {
            return SimpleRead3.TYPE;
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead5.class)) {
            return SimpleRead2.TYPE;
        } else if (enabledSimpleReadOperationTypes.contains(SimpleRead6.class)) {
            return SimpleRead1.TYPE;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private int lastPersonQueryIndex(Set<Class<? extends Operation>> enabledSimpleReadOperationTypes) {
        if (enabledSimpleReadOperationTypes.contains(SimpleRead7.class)) {
            return SimpleRead7.TYPE;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private int lastCompanyQueryIndex(Set<Class<? extends Operation>> enabledSimpleReadOperationTypes) {
        if (enabledSimpleReadOperationTypes.contains(SimpleRead8.class)) {
            return SimpleRead8.TYPE;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private LdbcSimpleQueryFactory selectRandomFirstSimpleQuery(
        Tuple2<Integer, LdbcSimpleQueryFactory> firstAccountQueryAndIndex,
        Tuple2<Integer, LdbcSimpleQueryFactory> firstPersonQueryAndIndex,
        Tuple2<Integer, LdbcSimpleQueryFactory> firstCompanyQueryAndIndex) {
        if (!firstAccountQueryAndIndex._1().equals(Integer.MAX_VALUE)) {
            return firstAccountQueryAndIndex._2();
        } else if (!firstPersonQueryAndIndex._1().equals(Integer.MAX_VALUE)) {
            return firstPersonQueryAndIndex._2();
        } else if (!firstCompanyQueryAndIndex._1().equals(Integer.MAX_VALUE)) {
            return firstCompanyQueryAndIndex._2();
        } else {
            return new RoundRobbinFactory(firstAccountQueryAndIndex._2(), firstPersonQueryAndIndex._2(),
                firstCompanyQueryAndIndex._2());
        }
    }

    @Override
    public double initialState() {
        return initialProbability;
    }

    @Override
    public Operation nextOperation(
        double state,
        Operation operation,
        Object result,
        long actualStartTimeAsMilli,
        long runDurationAsNano) throws WorkloadException {
        bufferReplenishFun.replenish(operation, result);
        return simpleQueryFactories[operation.type()].create(
            accountIdBuffer,
            personIdBuffer,
            companyIdBuffer,
            operation,
            actualStartTimeAsMilli,
            runDurationAsNano,
            state
        );
    }

    /*
    BufferReplenishFun
     */

    @Override
    public double updateState(double previousState, int previousOperationType) {
        double degradeBy = probabilityDegradationFactors[previousOperationType];
        return previousState - degradeBy;
    }


    public static enum ScheduledStartTimePolicy {
        PREVIOUS_OPERATION_SCHEDULED_START_TIME,
        PREVIOUS_OPERATION_ACTUAL_FINISH_TIME,
        ESTIMATED
    }

    public static interface BufferReplenishFun {
        void replenish(Operation operation, Object result);
    }

    /*
    LdbcSimpleQueryFactory
     */

    private interface LdbcSimpleQueryFactory {
        Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state);

        String describe();
    }

    private interface ScheduledStartTimeFactory {
        long nextScheduledStartTime(Operation previousOperation, long actualStartTimeAsMilli,
                                    long previousOperationRunDurationAsNano);
    }

    public static class NoOpBufferReplenishFun implements BufferReplenishFun {
        @Override
        public void replenish(Operation operation, Object result) {
        }
    }

    public static class ResultBufferReplenishFun implements BufferReplenishFun {
        private final Queue<Long> accountIdBuffer;
        private final Queue<Long> personIdBuffer;
        private final Queue<Long> companyIdBuffer;

        public ResultBufferReplenishFun(Queue<Long> accountIdBuffer, Queue<Long> personIdBuffer,
                                        Queue<Long> companyIdBuffer) {
            this.accountIdBuffer = accountIdBuffer;
            this.personIdBuffer = personIdBuffer;
            this.companyIdBuffer = companyIdBuffer;
        }

        @Override
        public void replenish(Operation operation, Object result) {
            // TODO add complex result out
            switch (operation.type()) {
                case ComplexRead1.TYPE: {
                    List<ComplexRead1Result> typedResults = (List<ComplexRead1Result>) result;
                    for (int i = 0; i < typedResults.size(); i++) {
                        accountIdBuffer.add(Long.parseLong(typedResults.get(i).getMediumId()));
                        // personIdBuffer.add( typedResults.get( i ) );
                    }
                    break;
                }
                default:
                    break;
                /* case LdbcQuery2.TYPE:
                {
                    List<LdbcQuery2Result> typedResults = (List<LdbcQuery2Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        LdbcQuery2Result typedResult = typedResults.get( i );
                        personIdBuffer.add( typedResult.getPersonId() );
                        messageIdBuffer.add( typedResult.getMessageId() );
                    }
                    break;
                }
                case LdbcQuery3a.TYPE:
                {
                    List<LdbcQuery3Result> typedResults = (List<LdbcQuery3Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        personIdBuffer.add( typedResults.get( i ).getPersonId() );
                    }
                    break;
                }
                case LdbcQuery3b.TYPE:
                {
                    List<LdbcQuery3Result> typedResults = (List<LdbcQuery3Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        personIdBuffer.add( typedResults.get( i ).getPersonId() );
                    }
                    break;
                }
                case LdbcQuery7.TYPE:
                {
                    List<LdbcQuery7Result> typedResults = (List<LdbcQuery7Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        LdbcQuery7Result typedResult = typedResults.get( i );
                        personIdBuffer.add( typedResult.getPersonId() );
                        messageIdBuffer.add( typedResult.getMessageId() );
                    }
                    break;
                }
                case LdbcQuery8.TYPE:
                {
                    List<LdbcQuery8Result> typedResults = (List<LdbcQuery8Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        LdbcQuery8Result typedResult = typedResults.get( i );
                        personIdBuffer.add( typedResult.getPersonId() );
                        messageIdBuffer.add( typedResult.getCommentId() );
                    }
                    break;
                }
                case LdbcQuery9.TYPE:
                {
                    List<LdbcQuery9Result> typedResults = (List<LdbcQuery9Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        LdbcQuery9Result typedResult = typedResults.get( i );
                        personIdBuffer.add( typedResult.getPersonId() );
                        messageIdBuffer.add( typedResult.getMessageId() );
                    }
                    break;
                }
                case LdbcQuery10.TYPE:
                {
                    List<LdbcQuery10Result> typedResults = (List<LdbcQuery10Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        personIdBuffer.add( typedResults.get( i ).getPersonId() );
                    }
                    break;
                }
                case LdbcQuery11.TYPE:
                {
                    List<LdbcQuery11Result> typedResults = (List<LdbcQuery11Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        personIdBuffer.add( typedResults.get( i ).getPersonId() );
                    }
                    break;
                }
                case LdbcQuery12.TYPE:
                {
                    List<LdbcQuery12Result> typedResults = (List<LdbcQuery12Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        personIdBuffer.add( typedResults.get( i ).getPersonId() );
                    }
                    break;
                }
                case LdbcQuery14a.TYPE:
                {
                    List<LdbcQuery14Result> typedResults = (List<LdbcQuery14Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        for ( Number personId : typedResults.get( i ).getPersonIdsInPath() )
                        {
                            personIdBuffer.add( personId.longValue() );
                        }
                    }
                    break;
                }
                case LdbcQuery14b.TYPE:
                {
                    List<LdbcQuery14Result> typedResults = (List<LdbcQuery14Result>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        for ( Number personId : typedResults.get( i ).getPersonIdsInPath() )
                        {
                            personIdBuffer.add( personId.longValue() );
                        }
                    }
                    break;
                }
                case LdbcSimpleQuery2PersonPosts.TYPE:
                {
                    List<LdbcSimpleQuery2PersonPostsResult> typedResults = (List<LdbcSimpleQuery2PersonPostsResult>)
                    result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        LdbcSimpleQuery2PersonPostsResult typedResult = typedResults.get( i );
                        personIdBuffer.add( typedResult.getOriginalPostAuthorId() );
                        messageIdBuffer.add( typedResult.getMessageId() );
                        messageIdBuffer.add( typedResult.getOriginalPostId() );
                    }
                    break;
                }
                case LdbcSimpleQuery3PersonFriends.TYPE:
                {
                    List<LdbcSimpleQuery3PersonFriendsResult> typedResults =
                        (List<LdbcSimpleQuery3PersonFriendsResult>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        personIdBuffer.add( typedResults.get( i ).getPersonId() );
                    }
                    break;
                }
                case LdbcSimpleQuery5MessageCreator.TYPE:
                {
                    LdbcSimpleQuery5MessageCreatorResult typedResult = (LdbcSimpleQuery5MessageCreatorResult) result;
                    personIdBuffer.add( typedResult.getPersonId() );
                    break;
                }
                case LdbcSimpleQuery6MessageForum.TYPE:
                {
                    LdbcSimpleQuery6MessageForumResult typedResult = (LdbcSimpleQuery6MessageForumResult) result;
                    personIdBuffer.add( typedResult.getModeratorId() );
                    break;
                }
                case LdbcSimpleQuery7MessageReplies.TYPE:
                {
                    List<LdbcSimpleQuery7MessageRepliesResult> typedResults =
                        (List<LdbcSimpleQuery7MessageRepliesResult>) result;
                    for ( int i = 0; i < typedResults.size(); i++ )
                    {
                        LdbcSimpleQuery7MessageRepliesResult typedResult = typedResults.get( i );
                        personIdBuffer.add( typedResult.getReplyAuthorId() );
                        messageIdBuffer.add( typedResult.getCommentId() );
                    }
                    break;
                }*/
            }
        }
    }

    private class NoOpFactory implements LdbcSimpleQueryFactory {
        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            return null;
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class ErrorFactory implements LdbcSimpleQueryFactory {
        private final Class operationType;

        private ErrorFactory(Class operationType) {
            this.operationType = operationType;
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            throw new RuntimeException(
                format("Encountered disabled simple read: %s - it should not have been executed",
                    operationType.getSimpleName()));
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class CoinTossingFactory implements LdbcSimpleQueryFactory {
        private final RandomDataGenerator random;
        private final LdbcSimpleQueryFactory innerFactory;
        private final double min;
        private final double max;

        private CoinTossingFactory(RandomDataGenerator random,
                                   LdbcSimpleQueryFactory innerFactory,
                                   double min,
                                   double max) {
            this.random = random;
            this.innerFactory = innerFactory;
            this.min = min;
            this.max = max;
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            double coinToss = random.nextUniform(min, max);
            if (state > coinToss) {
                return innerFactory.create(
                    accountIdBuffer,
                    personIdBuffer,
                    companyIdBuffer,
                    previousOperation,
                    previousOperationActualStartTimeAsMilli,
                    previousOperationRunDurationAsNano,
                    state
                );
            } else {
                return null;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName() + "[" + innerFactory.describe() + "]";
        }
    }

    private class RoundRobbinFactory implements LdbcSimpleQueryFactory {
        private final LdbcSimpleQueryFactory[] innerFactories;
        private final int innerFactoriesCount;
        private int nextFactoryIndex;

        private RoundRobbinFactory(LdbcSimpleQueryFactory... innerFactories) {
            this.innerFactories = innerFactories;
            this.innerFactoriesCount = innerFactories.length;
            this.nextFactoryIndex = -1;
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            nextFactoryIndex = (nextFactoryIndex + 1) % innerFactoriesCount;
            return innerFactories[nextFactoryIndex].create(
                accountIdBuffer,
                personIdBuffer,
                companyIdBuffer,
                previousOperation,
                previousOperationActualStartTimeAsMilli,
                previousOperationRunDurationAsNano,
                state
            );
        }

        @Override
        public String describe() {
            String description = getClass().getSimpleName() + "[";
            if (innerFactories.length > 0) {
                description += innerFactories[0].describe();
                for (int i = 1; i < innerFactories.length; i++) {
                    description += "," + innerFactories[i].describe();
                }
            }
            return description + "]";
        }
    }

    private class LdbcSimpleQuery1Factory implements LdbcSimpleQueryFactory {
        private final ScheduledStartTimeFactory scheduledStartTimeFactory;

        private LdbcSimpleQuery1Factory(ScheduledStartTimePolicy scheduledStartTimePolicy) {
            switch (scheduledStartTimePolicy) {
                case ESTIMATED: {
                    this.scheduledStartTimeFactory = new EstimatedScheduledStartTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_ACTUAL_FINISH_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationActualFinishTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_SCHEDULED_START_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationScheduledStartTimeFactory();
                    break;
                }
                default: {
                    throw new RuntimeException(format("Unexpected value, should be one of %s but was %s",
                        Arrays.toString(ScheduledStartTimePolicy.values()),
                        scheduledStartTimePolicy.name()));
                }
            }
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            Long id = accountIdBuffer.poll();
            if (null == id) {
                return null;
            } else {
                Operation operation = new SimpleRead1(id);
                operation.setScheduledStartTimeAsMilli(
                    scheduledStartTimeFactory.nextScheduledStartTime(
                        previousOperation,
                        previousOperationActualStartTimeAsMilli,
                        previousOperationRunDurationAsNano
                    )
                );
                return operation;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class LdbcSimpleQuery2Factory implements LdbcSimpleQueryFactory {
        private final ScheduledStartTimeFactory scheduledStartTimeFactory;

        private LdbcSimpleQuery2Factory(ScheduledStartTimePolicy scheduledStartTimePolicy) {
            switch (scheduledStartTimePolicy) {
                case ESTIMATED: {
                    this.scheduledStartTimeFactory = new EstimatedScheduledStartTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_ACTUAL_FINISH_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationActualFinishTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_SCHEDULED_START_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationScheduledStartTimeFactory();
                    break;
                }
                default: {
                    throw new RuntimeException(format("Unexpected value, should be one of %s but was %s",
                        Arrays.toString(ScheduledStartTimePolicy.values()),
                        scheduledStartTimePolicy.name()));
                }
            }
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            Long id = accountIdBuffer.poll();
            if (null == id) {
                return null;
            } else {
                Operation operation = new SimpleRead2(id, id, null, null); //TODO new SimpleRead2(id);
                operation.setScheduledStartTimeAsMilli(
                    scheduledStartTimeFactory.nextScheduledStartTime(
                        previousOperation,
                        previousOperationActualStartTimeAsMilli,
                        previousOperationRunDurationAsNano
                    )
                );
                return operation;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class LdbcSimpleQuery3Factory implements LdbcSimpleQueryFactory {
        private final ScheduledStartTimeFactory scheduledStartTimeFactory;

        private LdbcSimpleQuery3Factory(ScheduledStartTimePolicy scheduledStartTimePolicy) {
            switch (scheduledStartTimePolicy) {
                case ESTIMATED: {
                    this.scheduledStartTimeFactory = new EstimatedScheduledStartTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_ACTUAL_FINISH_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationActualFinishTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_SCHEDULED_START_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationScheduledStartTimeFactory();
                    break;
                }
                default: {
                    throw new RuntimeException(format("Unexpected value, should be one of %s but was %s",
                        Arrays.toString(ScheduledStartTimePolicy.values()),
                        scheduledStartTimePolicy.name()));
                }
            }
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            Long id = accountIdBuffer.poll();
            if (null == id) {
                return null;
            } else {
                Operation operation = new SimpleRead3(id); //TODO new SimpleRead2(id);
                operation.setScheduledStartTimeAsMilli(
                    scheduledStartTimeFactory.nextScheduledStartTime(
                        previousOperation,
                        previousOperationActualStartTimeAsMilli,
                        previousOperationRunDurationAsNano
                    )
                );
                return operation;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class LdbcSimpleQuery4Factory implements LdbcSimpleQueryFactory {
        private final ScheduledStartTimeFactory scheduledStartTimeFactory;

        private LdbcSimpleQuery4Factory(ScheduledStartTimePolicy scheduledStartTimePolicy) {
            switch (scheduledStartTimePolicy) {
                case ESTIMATED: {
                    this.scheduledStartTimeFactory = new EstimatedScheduledStartTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_ACTUAL_FINISH_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationActualFinishTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_SCHEDULED_START_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationScheduledStartTimeFactory();
                    break;
                }
                default: {
                    throw new RuntimeException(format("Unexpected value, should be one of %s but was %s",
                        Arrays.toString(ScheduledStartTimePolicy.values()),
                        scheduledStartTimePolicy.name()));
                }
            }
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            Long id = accountIdBuffer.poll();
            if (null == id) {
                return null;
            } else {
                Operation operation = new SimpleRead4(id, 0, null, null); //TODO new SimpleRead2(id);
                operation.setScheduledStartTimeAsMilli(
                    scheduledStartTimeFactory.nextScheduledStartTime(
                        previousOperation,
                        previousOperationActualStartTimeAsMilli,
                        previousOperationRunDurationAsNano
                    )
                );
                return operation;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class LdbcSimpleQuery5Factory implements LdbcSimpleQueryFactory {
        private final ScheduledStartTimeFactory scheduledStartTimeFactory;

        private LdbcSimpleQuery5Factory(ScheduledStartTimePolicy scheduledStartTimePolicy) {
            switch (scheduledStartTimePolicy) {
                case ESTIMATED: {
                    this.scheduledStartTimeFactory = new EstimatedScheduledStartTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_ACTUAL_FINISH_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationActualFinishTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_SCHEDULED_START_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationScheduledStartTimeFactory();
                    break;
                }
                default: {
                    throw new RuntimeException(format("Unexpected value, should be one of %s but was %s",
                        Arrays.toString(ScheduledStartTimePolicy.values()),
                        scheduledStartTimePolicy.name()));
                }
            }
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            Long id = accountIdBuffer.poll();
            if (null == id) {
                return null;
            } else {
                Operation operation = new SimpleRead5(id, null, null); //TODO new SimpleRead2(id);
                operation.setScheduledStartTimeAsMilli(
                    scheduledStartTimeFactory.nextScheduledStartTime(
                        previousOperation,
                        previousOperationActualStartTimeAsMilli,
                        previousOperationRunDurationAsNano
                    )
                );
                return operation;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    /*
    ScheduledStartTimeFactory
     */

    private class LdbcSimpleQuery6Factory implements LdbcSimpleQueryFactory {
        private final ScheduledStartTimeFactory scheduledStartTimeFactory;

        private LdbcSimpleQuery6Factory(ScheduledStartTimePolicy scheduledStartTimePolicy) {
            switch (scheduledStartTimePolicy) {
                case ESTIMATED: {
                    this.scheduledStartTimeFactory = new EstimatedScheduledStartTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_ACTUAL_FINISH_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationActualFinishTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_SCHEDULED_START_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationScheduledStartTimeFactory();
                    break;
                }
                default: {
                    throw new RuntimeException(format("Unexpected value, should be one of %s but was %s",
                        Arrays.toString(ScheduledStartTimePolicy.values()),
                        scheduledStartTimePolicy.name()));
                }
            }
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            Long id = accountIdBuffer.poll();
            if (null == id) {
                return null;
            } else {
                Operation operation = new SimpleRead6(id);  //TODO new SimpleRead2(id);
                operation.setScheduledStartTimeAsMilli(
                    scheduledStartTimeFactory.nextScheduledStartTime(
                        previousOperation,
                        previousOperationActualStartTimeAsMilli,
                        previousOperationRunDurationAsNano
                    )
                );
                return operation;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class LdbcSimpleQuery7Factory implements LdbcSimpleQueryFactory {
        private final ScheduledStartTimeFactory scheduledStartTimeFactory;

        private LdbcSimpleQuery7Factory(ScheduledStartTimePolicy scheduledStartTimePolicy) {
            switch (scheduledStartTimePolicy) {
                case ESTIMATED: {
                    this.scheduledStartTimeFactory = new EstimatedScheduledStartTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_ACTUAL_FINISH_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationActualFinishTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_SCHEDULED_START_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationScheduledStartTimeFactory();
                    break;
                }
                default: {
                    throw new RuntimeException(format("Unexpected value, should be one of %s but was %s",
                        Arrays.toString(ScheduledStartTimePolicy.values()),
                        scheduledStartTimePolicy.name()));
                }
            }
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            Long id = personIdBuffer.poll();
            if (null == id) {
                return null;
            } else {
                Operation operation = new SimpleRead7(id, null, null); //TODO new SimpleRead2(id);
                operation.setScheduledStartTimeAsMilli(
                    scheduledStartTimeFactory.nextScheduledStartTime(
                        previousOperation,
                        previousOperationActualStartTimeAsMilli,
                        previousOperationRunDurationAsNano
                    )
                );
                return operation;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class LdbcSimpleQuery8Factory implements LdbcSimpleQueryFactory {
        private final ScheduledStartTimeFactory scheduledStartTimeFactory;

        private LdbcSimpleQuery8Factory(ScheduledStartTimePolicy scheduledStartTimePolicy) {
            switch (scheduledStartTimePolicy) {
                case ESTIMATED: {
                    this.scheduledStartTimeFactory = new EstimatedScheduledStartTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_ACTUAL_FINISH_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationActualFinishTimeFactory();
                    break;
                }
                case PREVIOUS_OPERATION_SCHEDULED_START_TIME: {
                    this.scheduledStartTimeFactory = new PreviousOperationScheduledStartTimeFactory();
                    break;
                }
                default: {
                    throw new RuntimeException(format("Unexpected value, should be one of %s but was %s",
                        Arrays.toString(ScheduledStartTimePolicy.values()),
                        scheduledStartTimePolicy.name()));
                }
            }
        }

        @Override
        public Operation create(
            Queue<Long> accountIdBuffer,
            Queue<Long> personIdBuffer,
            Queue<Long> companyIdBuffer,
            Operation previousOperation,
            long previousOperationActualStartTimeAsMilli,
            long previousOperationRunDurationAsNano,
            double state) {
            Long id = companyIdBuffer.poll();
            if (null == id) {
                return null;
            } else {
                Operation operation = new SimpleRead8(id, 0, null, null);  //TODO new SimpleRead2(id);
                operation.setScheduledStartTimeAsMilli(
                    scheduledStartTimeFactory.nextScheduledStartTime(
                        previousOperation,
                        previousOperationActualStartTimeAsMilli,
                        previousOperationRunDurationAsNano
                    )
                );
                return operation;
            }
        }

        @Override
        public String describe() {
            return getClass().getSimpleName();
        }
    }

    private class EstimatedScheduledStartTimeFactory implements ScheduledStartTimeFactory {
        @Override
        public long nextScheduledStartTime(Operation previousOperation, long actualStartTimeAsMilli,
                                           long previousOperationRunDurationAsNano) {
            return previousOperation.scheduledStartTimeAsMilli() + interleavesAsMilli[previousOperation.type()];
        }
    }

    /*
    Buffer
     */

    private class PreviousOperationActualFinishTimeFactory implements ScheduledStartTimeFactory {
        @Override
        public long nextScheduledStartTime(Operation previousOperation, long actualStartTimeAsMilli,
                                           long previousOperationRunDurationAsNano) {
            return actualStartTimeAsMilli + TimeUnit.NANOSECONDS.toMillis(previousOperationRunDurationAsNano);
        }
    }

    private class PreviousOperationScheduledStartTimeFactory implements ScheduledStartTimeFactory {
        @Override
        public long nextScheduledStartTime(Operation previousOperation, long actualStartTimeAsMilli,
                                           long previousOperationRunDurationAsNano) {
            return previousOperation.scheduledStartTimeAsMilli();
        }
    }
}
