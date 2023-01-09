package org.ldbcouncil.finbench.driver.workloads;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.ldbcouncil.finbench.driver.ChildOperationGenerator;
import org.ldbcouncil.finbench.driver.Operation;
import org.ldbcouncil.finbench.driver.Workload;
import org.ldbcouncil.finbench.driver.WorkloadException;
import org.ldbcouncil.finbench.driver.WorkloadStreams;
import org.ldbcouncil.finbench.driver.control.ConsoleAndFileDriverConfiguration;
import org.ldbcouncil.finbench.driver.control.DriverConfigurationException;
import org.ldbcouncil.finbench.driver.generator.GeneratorFactory;
import org.ldbcouncil.finbench.driver.generator.RandomDataGeneratorFactory;
import org.ldbcouncil.finbench.driver.log.Log4jLoggingServiceFactory;
import org.ldbcouncil.finbench.driver.log.LoggingServiceFactory;
import org.ldbcouncil.finbench.driver.util.Tuple3;
import org.ldbcouncil.finbench.driver.workloads.dummy.NothingOperation;
import org.ldbcouncil.finbench.driver.workloads.dummy.TimedNamedOperation1;
import org.ldbcouncil.finbench.driver.workloads.dummy.TimedNamedOperation1Factory;
import org.ldbcouncil.finbench.driver.workloads.dummy.TimedNamedOperation2;
import org.ldbcouncil.finbench.driver.workloads.dummy.TimedNamedOperation2Factory;
import org.ldbcouncil.finbench.driver.workloads.dummy.TimedNamedOperation3;
import org.ldbcouncil.finbench.driver.workloads.dummy.TimedNamedOperation3Factory;

public class WorkloadStreamsTest {

    @Test
    public void shouldReturnSameWorkloadStreamsAsCreatedWith() {
        WorkloadStreams workloadStreamsBefore = getWorkloadStreams();

        Operation firstAsyncDependencyOperation =
            workloadStreamsBefore.asynchronousStream().dependencyOperations().next();
        Operation secondAsyncDependencyOperation =
            workloadStreamsBefore.asynchronousStream().dependencyOperations().next();
        assertThat(firstAsyncDependencyOperation.scheduledStartTimeAsMilli(), is(0L));
        assertThat(firstAsyncDependencyOperation.timeStamp(), is(0L));
        assertThat(firstAsyncDependencyOperation.dependencyTimeStamp(), is(0L));
        assertThat(secondAsyncDependencyOperation.scheduledStartTimeAsMilli(), is(10L));
        assertThat(secondAsyncDependencyOperation.timeStamp(), is(10L));
        assertThat(secondAsyncDependencyOperation.dependencyTimeStamp(), is(10L));

        Operation firstAsyncNonDependencyOperation =
            workloadStreamsBefore.asynchronousStream().nonDependencyOperations().next();
        Operation secondAsyncNonDependencyOperation =
            workloadStreamsBefore.asynchronousStream().nonDependencyOperations().next();
        assertThat(firstAsyncNonDependencyOperation.scheduledStartTimeAsMilli(), is(2L));
        assertThat(firstAsyncNonDependencyOperation.timeStamp(), is(2L));
        assertThat(firstAsyncNonDependencyOperation.dependencyTimeStamp(), is(2L));
        assertThat(secondAsyncNonDependencyOperation.scheduledStartTimeAsMilli(), is(102L));
        assertThat(secondAsyncNonDependencyOperation.timeStamp(), is(102L));
        assertThat(secondAsyncNonDependencyOperation.dependencyTimeStamp(), is(102L));
    }

    @Test
    public void shouldPerformTimeOffsetCorrectly() throws WorkloadException {
        long offset = TimeUnit.SECONDS.toMillis(100);
        WorkloadStreams workloadStreamsBefore = WorkloadStreams.timeOffsetAndCompressWorkloadStreams(
            getWorkloadStreams(),
            0L + offset,
            1.0,
            new GeneratorFactory(new RandomDataGeneratorFactory(42L)));

        Operation firstAsyncDependencyOperation =
            workloadStreamsBefore.asynchronousStream().dependencyOperations().next();
        Operation secondAsyncDependencyOperation =
            workloadStreamsBefore.asynchronousStream().dependencyOperations().next();
        assertThat(firstAsyncDependencyOperation.scheduledStartTimeAsMilli(), is(0L + offset));
        assertThat(firstAsyncDependencyOperation.timeStamp(), is(0L));
        assertThat(firstAsyncDependencyOperation.dependencyTimeStamp(), is(0L));
        assertThat(secondAsyncDependencyOperation.scheduledStartTimeAsMilli(), is(10L + offset));
        assertThat(secondAsyncDependencyOperation.timeStamp(), is(10L));
        assertThat(secondAsyncDependencyOperation.dependencyTimeStamp(), is(10L));

        Operation firstAsyncNonDependencyOperation =
            workloadStreamsBefore.asynchronousStream().nonDependencyOperations().next();
        Operation secondAsyncNonDependencyOperation =
            workloadStreamsBefore.asynchronousStream().nonDependencyOperations().next();
        assertThat(firstAsyncNonDependencyOperation.scheduledStartTimeAsMilli(), is(2L + offset));
        assertThat(firstAsyncNonDependencyOperation.timeStamp(), is(2L));
        assertThat(firstAsyncNonDependencyOperation.dependencyTimeStamp(), is(2L));
        assertThat(secondAsyncNonDependencyOperation.scheduledStartTimeAsMilli(), is(102L + offset));
        assertThat(secondAsyncNonDependencyOperation.timeStamp(), is(102L));
        assertThat(secondAsyncNonDependencyOperation.dependencyTimeStamp(), is(102L));
    }

    @Test
    public void shouldPerformTimeOffsetAndCompressionCorrectly() throws WorkloadException {
        long offset = TimeUnit.SECONDS.toMillis(100);
        WorkloadStreams workloadStreamsBefore = WorkloadStreams.timeOffsetAndCompressWorkloadStreams(
            getWorkloadStreams(),
            0L + offset,
            0.5,
            new GeneratorFactory(new RandomDataGeneratorFactory(42L)));

        Operation firstAsyncDependencyOperation =
            workloadStreamsBefore.asynchronousStream().dependencyOperations().next();
        Operation secondAsyncDependencyOperation =
            workloadStreamsBefore.asynchronousStream().dependencyOperations().next();
        assertThat(firstAsyncDependencyOperation.scheduledStartTimeAsMilli(), is(0L + offset));
        assertThat(firstAsyncDependencyOperation.timeStamp(), is(0L));
        assertThat(secondAsyncDependencyOperation.scheduledStartTimeAsMilli(), is(5L + offset));
        assertThat(secondAsyncDependencyOperation.timeStamp(), is(10L));
        assertThat(secondAsyncDependencyOperation.scheduledStartTimeAsMilli()
            - firstAsyncDependencyOperation.scheduledStartTimeAsMilli(), is(5L));
        assertThat(secondAsyncDependencyOperation.timeStamp()
            - firstAsyncDependencyOperation.timeStamp(), is(10L));
        assertThat(secondAsyncDependencyOperation.dependencyTimeStamp()
            - firstAsyncDependencyOperation.dependencyTimeStamp(), is(10L));

        Operation firstAsyncNonDependencyOperation =
            workloadStreamsBefore.asynchronousStream().nonDependencyOperations().next();
        Operation secondAsyncNonDependencyOperation =
            workloadStreamsBefore.asynchronousStream().nonDependencyOperations().next();
        assertThat(firstAsyncNonDependencyOperation.scheduledStartTimeAsMilli(), is(1L + offset));
        assertThat(firstAsyncNonDependencyOperation.timeStamp(), is(2L));
        assertThat(firstAsyncNonDependencyOperation.dependencyTimeStamp(), is(2L));
        assertThat(secondAsyncNonDependencyOperation.scheduledStartTimeAsMilli(), is(51L + offset));
        assertThat(secondAsyncNonDependencyOperation.timeStamp(), is(102L));
        assertThat(secondAsyncNonDependencyOperation.dependencyTimeStamp(), is(102L));
    }

    @Test
    public void shouldLimitWorkloadCorrectly() throws WorkloadException, DriverConfigurationException, IOException {
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42L));
        WorkloadFactory workloadFactory = new WorkloadFactory() {
            @Override
            public Workload createWorkload() throws WorkloadException {
                return new TestWorkload();
            }
        };
        ConsoleAndFileDriverConfiguration configuration =
            ConsoleAndFileDriverConfiguration.fromDefaults(null, null, 100);
        boolean returnStreamsWithDbConnector = false;
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory(false);
        Tuple3<WorkloadStreams, Workload, Long> limitedWorkloadStreamsAndWorkload =
            WorkloadStreams.createNewWorkloadWithOffsetAndLimitedWorkloadStreams(
                workloadFactory,
                configuration,
                gf,
                returnStreamsWithDbConnector,
                0,
                configuration.operationCount(),
                loggingServiceFactory
            );
        WorkloadStreams workloadStreams = limitedWorkloadStreamsAndWorkload._1();
        Workload workload = limitedWorkloadStreamsAndWorkload._2();
        assertThat(Iterators
                .size(WorkloadStreams
                    .mergeSortedByStartTimeExcludingChildOperationGenerators(gf, workloadStreams)),
            is(100));
        workload.close();
    }

    @Test
    public void shouldLimitWorkloadCorrectly_WITH_OFFSET()
        throws WorkloadException, DriverConfigurationException, IOException {
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42L));
        WorkloadFactory workloadFactory = new WorkloadFactory() {
            @Override
            public Workload createWorkload() throws WorkloadException {
                return new TestWorkload();
            }
        };
        ConsoleAndFileDriverConfiguration configuration =
            ConsoleAndFileDriverConfiguration.fromDefaults(null, null, 100);
        configuration = (ConsoleAndFileDriverConfiguration) configuration
            .applyArg(ConsoleAndFileDriverConfiguration.WARMUP_COUNT_ARG, Long.toString(10));
        boolean returnStreamsWithDbConnector = false;
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory(false);
        Tuple3<WorkloadStreams, Workload, Long> limitedWorkloadStreamsAndWorkload =
            WorkloadStreams.createNewWorkloadWithOffsetAndLimitedWorkloadStreams(
                workloadFactory,
                configuration,
                gf,
                returnStreamsWithDbConnector,
                configuration.warmupCount(),
                configuration.operationCount(),
                loggingServiceFactory
            );
        WorkloadStreams workloadStreams = limitedWorkloadStreamsAndWorkload._1();
        Workload workload = limitedWorkloadStreamsAndWorkload._2();
        assertThat(Iterators
                .size(WorkloadStreams
                    .mergeSortedByStartTimeExcludingChildOperationGenerators(gf, workloadStreams)),
            is(100));
        workload.close();
    }

    @Test
    public void shouldLimitStreamsCorrectly() throws WorkloadException {
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42L));

        List<Operation> stream0 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(0L, 0L, 0L, "0-1"),
            new TimedNamedOperation1(1L, 1L, 0L, "0-2"),
            new TimedNamedOperation1(2L, 2L, 0L, "0-3"),
            new TimedNamedOperation1(6L, 6L, 0L, "0-4"),
            new TimedNamedOperation1(7L, 7L, 0L, "0-5")
        );

        List<Operation> stream1 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(0L, 0L, 0L, "1-1"),
            new TimedNamedOperation1(3L, 3L, 0L, "1-2"),
            new TimedNamedOperation1(4L, 4L, 0L, "1-3"),
            new TimedNamedOperation1(9L, 9L, 0L, "1-4")
        );

        List<Operation> stream2 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(1L, 1L, 0L, "2-1"),
            new TimedNamedOperation1(3L, 3L, 0L, "2-2"),
            new TimedNamedOperation1(4L, 4L, 0L, "2-3"),
            new TimedNamedOperation1(8L, 8L, 0L, "2-4"),
            new TimedNamedOperation1(8L, 8L, 0L, "2-5"),
            new TimedNamedOperation1(9L, 9L, 0L, "2-6")
        );

        List<Operation> stream3 = Lists.newArrayList(
        );

        List<Operation> stream4 = Lists.newArrayList(gf.limit(
            new TimedNamedOperation1Factory(
                gf.incrementing(10L, 1L),
                gf.constant(0L),
                gf.constant("4-x")
            ),
            1000000
            )
        );

        List<Iterator<Operation>> streams = Lists.newArrayList(
            stream0.iterator(),
            stream1.iterator(),
            stream2.iterator(),
            stream3.iterator(),
            stream4.iterator()
        );

        List<ChildOperationGenerator> childOperationGenerators = Lists.newArrayList(
            null,
            null,
            null,
            null,
            null
        );

        long offset = 0;
        long count = 10;
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory(false);
        Tuple3<long[], long[], Long> kForIteratorAndMinimums =
            WorkloadStreams.fromAmongAllRetrieveTopCountFromOffset(
                streams,
                offset,
                count,
                childOperationGenerators,
                loggingServiceFactory
            );
        long[] startForIterator = kForIteratorAndMinimums._1();
        long[] countForIterator = kForIteratorAndMinimums._2();
        long minimumTimeStamp = kForIteratorAndMinimums._3();

        List<Operation> topK = Lists.newArrayList(
            gf.mergeSortOperationsByTimeStamp(
                gf.limit(
                    stream0.iterator(),
                    countForIterator[0]
                ),
                gf.limit(
                    stream1.iterator(),
                    countForIterator[1]
                ),
                gf.limit(
                    stream2.iterator(),
                    countForIterator[2]
                ),
                gf.limit(
                    stream3.iterator(),
                    countForIterator[3]
                ),
                gf.limit(
                    stream4.iterator(),
                    countForIterator[4]
                )
            )
        );

        assertThat((long) topK.size(), is(count));
        assertThat(minimumTimeStamp, is(0L));
        assertThat(((TimedNamedOperation1) topK.get(0)).name(), anyOf(equalTo("0-1"), equalTo("1-1")));
        assertThat(((TimedNamedOperation1) topK.get(1)).name(), anyOf(equalTo("0-1"), equalTo("1-1")));
        assertThat(((TimedNamedOperation1) topK.get(0)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(1)).name())));
        assertThat(((TimedNamedOperation1) topK.get(2)).name(), anyOf(equalTo("0-2"), equalTo("2-1")));
        assertThat(((TimedNamedOperation1) topK.get(3)).name(), anyOf(equalTo("0-2"), equalTo("2-1")));
        assertThat(((TimedNamedOperation1) topK.get(2)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(3)).name())));
        assertThat(((TimedNamedOperation1) topK.get(4)).name(), anyOf(equalTo("0-3")));
        assertThat(((TimedNamedOperation1) topK.get(5)).name(), anyOf(equalTo("1-2"), equalTo("2-2")));
        assertThat(((TimedNamedOperation1) topK.get(6)).name(), anyOf(equalTo("1-2"), equalTo("2-2")));
        assertThat(((TimedNamedOperation1) topK.get(5)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(6)).name())));
        assertThat(((TimedNamedOperation1) topK.get(7)).name(), anyOf(equalTo("1-3"), equalTo("2-3")));
        assertThat(((TimedNamedOperation1) topK.get(8)).name(), anyOf(equalTo("1-3"), equalTo("2-3")));
        assertThat(((TimedNamedOperation1) topK.get(7)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(8)).name())));
        assertThat(((TimedNamedOperation1) topK.get(9)).name(), anyOf(equalTo("0-4")));
    }

    @Test
    public void shouldLimitStreamsCorrectlyWhenLimitIsHigherThanActualStreamsLength() throws WorkloadException {
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42L));

        List<Operation> stream0 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(0L, 0L, 0L, "0-1"),
            new TimedNamedOperation1(1L, 1L, 0L, "0-2"),
            new TimedNamedOperation1(2L, 2L, 0L, "0-3"),
            new TimedNamedOperation1(6L, 6L, 0L, "0-4")
        );

        List<Operation> stream1 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(0L, 0L, 0L, "1-1"),
            new TimedNamedOperation1(3L, 3L, 0L, "1-2"),
            new TimedNamedOperation1(4L, 4L, 0L, "1-3")
        );

        List<Operation> stream2 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(1L, 1L, 0L, "2-1"),
            new TimedNamedOperation1(3L, 3L, 0L, "2-2"),
            new TimedNamedOperation1(4L, 4L, 0L, "2-3")
        );

        List<Operation> stream3 = Lists.newArrayList(
        );

        List<Operation> stream4 = Lists.newArrayList(gf.limit(
            new TimedNamedOperation1Factory(
                gf.incrementing(10L, 1L),
                gf.constant(0L),
                gf.constant("4-x")
            ),
            1000000
            )
        );

        List<Iterator<Operation>> streams = Lists.newArrayList(
            stream0.iterator(),
            stream1.iterator(),
            stream2.iterator(),
            stream3.iterator(),
            stream4.iterator()
        );

        List<ChildOperationGenerator> childOperationGenerators = Lists.newArrayList(
            null,
            null,
            null,
            null,
            null
        );

        long offset = 0;
        long count = 10000;
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory(false);
        Tuple3<long[], long[], Long> kForIteratorAndMinimums =
            WorkloadStreams.fromAmongAllRetrieveTopCountFromOffset(
                streams,
                offset,
                count,
                childOperationGenerators,
                loggingServiceFactory
            );
        long[] startForIterator = kForIteratorAndMinimums._1();
        long[] countForIterator = kForIteratorAndMinimums._2();
        long minimumTimeStamp = kForIteratorAndMinimums._3();

        List<Operation> topK = Lists.newArrayList(
            gf.mergeSortOperationsByTimeStamp(
                gf.limit(
                    stream0.iterator(),
                    countForIterator[0]
                ),
                gf.limit(
                    stream1.iterator(),
                    countForIterator[1]
                ),
                gf.limit(
                    stream2.iterator(),
                    countForIterator[2]
                ),
                gf.limit(
                    stream3.iterator(),
                    countForIterator[3]
                ),
                gf.limit(
                    stream4.iterator(),
                    countForIterator[4]
                )
            )
        );

        assertThat((long) topK.size(), is(count));
        assertThat(minimumTimeStamp, is(0L));
        assertThat(((TimedNamedOperation1) topK.get(0)).name(), anyOf(equalTo("0-1"), equalTo("1-1")));
        assertThat(((TimedNamedOperation1) topK.get(1)).name(), anyOf(equalTo("0-1"), equalTo("1-1")));
        assertThat(((TimedNamedOperation1) topK.get(0)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(1)).name())));
        assertThat(((TimedNamedOperation1) topK.get(2)).name(), anyOf(equalTo("0-2"), equalTo("2-1")));
        assertThat(((TimedNamedOperation1) topK.get(3)).name(), anyOf(equalTo("0-2"), equalTo("2-1")));
        assertThat(((TimedNamedOperation1) topK.get(2)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(3)).name())));
        assertThat(((TimedNamedOperation1) topK.get(4)).name(), anyOf(equalTo("0-3")));
        assertThat(((TimedNamedOperation1) topK.get(5)).name(), anyOf(equalTo("1-2"), equalTo("2-2")));
        assertThat(((TimedNamedOperation1) topK.get(6)).name(), anyOf(equalTo("1-2"), equalTo("2-2")));
        assertThat(((TimedNamedOperation1) topK.get(5)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(6)).name())));
        assertThat(((TimedNamedOperation1) topK.get(7)).name(), anyOf(equalTo("1-3"), equalTo("2-3")));
        assertThat(((TimedNamedOperation1) topK.get(8)).name(), anyOf(equalTo("1-3"), equalTo("2-3")));
        assertThat(((TimedNamedOperation1) topK.get(7)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(8)).name())));
        assertThat(((TimedNamedOperation1) topK.get(9)).name(), anyOf(equalTo("0-4")));
    }

    @Test
    public void shouldStartAtOffsetAndLimitStreamsCorrectlyWhenLimitIsLowerThanStreamsLength()
        throws WorkloadException {
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42L));

        List<Operation> stream0 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(0L, 0L, 0L, "0-1--0"),
            new TimedNamedOperation1(1L, 1L, 0L, "0-2--1"),
            new TimedNamedOperation1(2L, 2L, 0L, "0-3--2"),
            new TimedNamedOperation1(6L, 6L, 0L, "0-4--6"),
            new TimedNamedOperation1(7L, 7L, 0L, "0-5--7")
        );

        List<Operation> stream1 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(0L, 0L, 0L, "1-1--0"),
            new TimedNamedOperation1(3L, 3L, 0L, "1-2--3"),
            new TimedNamedOperation1(4L, 4L, 0L, "1-3--4"),
            new TimedNamedOperation1(9L, 9L, 0L, "1-4--9")
        );

        List<Operation> stream2 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(1L, 1L, 0L, "2-1--1"),
            new TimedNamedOperation1(3L, 3L, 0L, "2-2--3"),
            new TimedNamedOperation1(4L, 4L, 0L, "2-3--4"),
            new TimedNamedOperation1(8L, 8L, 0L, "2-4--8"),
            new TimedNamedOperation1(8L, 8L, 0L, "2-5--8"),
            new TimedNamedOperation1(9L, 9L, 0L, "2-6--9")
        );

        List<Operation> stream3 = Lists.newArrayList(
        );

        List<Operation> stream4 = Lists.newArrayList(
            gf.limit(
                new TimedNamedOperation1Factory(
                    gf.incrementing(10L, 1L),
                    gf.constant(0L),
                    gf.constant("4-x--y")
                ),
                1000000
            )
        );

        List<Iterator<Operation>> streams = Lists.newArrayList(
            stream0.iterator(),
            stream1.iterator(),
            stream2.iterator(),
            stream3.iterator(),
            stream4.iterator()
        );

        List<ChildOperationGenerator> childOperationGenerators = Lists.newArrayList(
            null,
            null,
            null,
            null,
            null
        );

        long offset = 5;
        long count = 6;
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory(false);
        Tuple3<long[], long[], Long> kForIteratorAndMinimums =
            WorkloadStreams.fromAmongAllRetrieveTopCountFromOffset(
                streams,
                offset,
                count,
                childOperationGenerators,
                loggingServiceFactory
            );
        long[] startForIterator = kForIteratorAndMinimums._1();
        long[] countForIterator = kForIteratorAndMinimums._2();
        long minimumTimeStamp = kForIteratorAndMinimums._3();

        List<Iterator<Operation>> offsetStreams = Lists.newArrayList(
            stream0.iterator(),
            stream1.iterator(),
            stream2.iterator(),
            stream3.iterator(),
            stream4.iterator()
        );

        for (int i = 0; i < offsetStreams.size(); i++) {
            Iterator<Operation> offsetStream = offsetStreams.get(i);
            gf.consume(offsetStream, startForIterator[i]);
        }

        List<Operation> topK = Lists.newArrayList(
            gf.mergeSortOperationsByTimeStamp(
                gf.limit(
                    offsetStreams.get(0),
                    countForIterator[0]
                ),
                gf.limit(
                    offsetStreams.get(1),
                    countForIterator[1]
                ),
                gf.limit(
                    offsetStreams.get(2),
                    countForIterator[2]
                ),
                gf.limit(
                    offsetStreams.get(3),
                    countForIterator[3]
                ),
                gf.limit(
                    offsetStreams.get(4),
                    countForIterator[4]
                )
            )
        );

        /*
        offset = 5
        count = 6

        TimeStamp       Operation
        0               0-1--0, 1-1--0
        1               0-2--1, 2-1--1
        2               0-3--2
        ----- 5 -----
        3               1-2--3, 2-2--3
        4               1-3--4, 2-3--4
        6               0-4--6
        ----- 10 ----
        7               0-5--7
        8               2-4--8, 2-5--8
        9               1-4--9, 2-6--9
        ----- 15 ----
        10              4-x--y
        11              4-x--y
        ...
        1000000         4-x--y
         */

        assertThat((long) topK.size(), is(count));
        assertThat(minimumTimeStamp, is(3L));
        assertThat(startForIterator.length, is(5));
        assertThat(countForIterator.length, is(5));

        assertThat(((TimedNamedOperation1) topK.get(0)).name(), anyOf(equalTo("1-2--3"), equalTo("2-2--3")));
        assertThat(((TimedNamedOperation1) topK.get(1)).name(), anyOf(equalTo("1-2--3"), equalTo("2-2--3")));
        // does not return the same operation twice, when their time stamps are equal
        assertThat(((TimedNamedOperation1) topK.get(0)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(1)).name())));

        assertThat(((TimedNamedOperation1) topK.get(2)).name(), anyOf(equalTo("1-3--4"), equalTo("2-3--4")));
        assertThat(((TimedNamedOperation1) topK.get(3)).name(), anyOf(equalTo("1-3--4"), equalTo("2-3--4")));
        // does not return the same operation twice, when their time stamps are equal
        assertThat(((TimedNamedOperation1) topK.get(2)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(3)).name())));

        assertThat(((TimedNamedOperation1) topK.get(4)).name(), anyOf(equalTo("0-4--6")));

        assertThat(((TimedNamedOperation1) topK.get(5)).name(), anyOf(equalTo("0-5--7")));
    }

    @Test
    public void shouldStartAtOffsetAndLimitStreamsCorrectlyWhenLimitIsHigherThanStreamsLength()
        throws WorkloadException {
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42L));

        List<Operation> stream0 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(0L, 0L, 0L, "0-1--0"),
            new TimedNamedOperation1(1L, 1L, 0L, "0-2--1"),
            new TimedNamedOperation1(2L, 2L, 0L, "0-3--2"),
            new TimedNamedOperation1(6L, 6L, 0L, "0-4--6"),
            new TimedNamedOperation1(7L, 7L, 0L, "0-5--7")
        );

        List<Operation> stream1 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(0L, 0L, 0L, "1-1--0"),
            new TimedNamedOperation1(3L, 3L, 0L, "1-2--3"),
            new TimedNamedOperation1(4L, 4L, 0L, "1-3--4"),
            new TimedNamedOperation1(9L, 9L, 0L, "1-4--9")
        );

        List<Operation> stream2 = Lists.<Operation>newArrayList(
            new TimedNamedOperation1(1L, 1L, 0L, "2-1--1"),
            new TimedNamedOperation1(3L, 3L, 0L, "2-2--3"),
            new TimedNamedOperation1(4L, 4L, 0L, "2-3--4"),
            new TimedNamedOperation1(8L, 8L, 0L, "2-4--8"),
            new TimedNamedOperation1(8L, 8L, 0L, "2-5--8"),
            new TimedNamedOperation1(9L, 9L, 0L, "2-6--9")
        );

        List<Operation> stream3 = Lists.newArrayList(
        );

        List<Iterator<Operation>> streams = Lists.newArrayList(
            stream0.iterator(),
            stream1.iterator(),
            stream2.iterator(),
            stream3.iterator()
        );

        List<ChildOperationGenerator> childOperationGenerators = Lists.newArrayList(
            null,
            null,
            null,
            null,
            null
        );

        long offset = 3;
        long count = 100;
        LoggingServiceFactory loggingServiceFactory = new Log4jLoggingServiceFactory(false);
        Tuple3<long[], long[], Long> kForIteratorAndMinimums =
            WorkloadStreams.fromAmongAllRetrieveTopCountFromOffset(
                streams,
                offset,
                count,
                childOperationGenerators,
                loggingServiceFactory
            );
        long[] startForIterator = kForIteratorAndMinimums._1();
        long[] countForIterator = kForIteratorAndMinimums._2();
        long minimumTimeStamp = kForIteratorAndMinimums._3();

        List<Iterator<Operation>> offsetStreams = Lists.newArrayList(
            stream0.iterator(),
            stream1.iterator(),
            stream2.iterator(),
            stream3.iterator()
        );

        for (int i = 0; i < offsetStreams.size(); i++) {
            Iterator<Operation> offsetStream = offsetStreams.get(i);
            gf.consume(offsetStream, startForIterator[i]);
        }

        List<Operation> topK = Lists.newArrayList(
            gf.mergeSortOperationsByTimeStamp(
                gf.limit(
                    offsetStreams.get(0),
                    countForIterator[0]
                ),
                gf.limit(
                    offsetStreams.get(1),
                    countForIterator[1]
                ),
                gf.limit(
                    offsetStreams.get(2),
                    countForIterator[2]
                ),
                gf.limit(
                    offsetStreams.get(3),
                    countForIterator[3]
                )
            )
        );

        /*
        offset = 3
        count = 100

        TimeStamp       Operation
        0               0-1--0, 1-1--0
        ----- 2 -----
        1               0-2--1, 2-1--1
        ----- 4 -----
        2               0-3--2
        3               1-2--3, 2-2--3
        4               1-3--4, 2-3--4
        6               0-4--6
        7               0-5--7
        8               2-4--8, 2-5--8
        9               1-4--9, 2-6--9
         */

        assertThat((long) topK.size(), is(12L));
        assertThat(minimumTimeStamp, is(1L));
        assertThat(startForIterator.length, is(4));
        assertThat(countForIterator.length, is(4));

        assertThat(((TimedNamedOperation1) topK.get(0)).name(), anyOf(equalTo("0-2--1"), equalTo("2-1--1")));

        assertThat(((TimedNamedOperation1) topK.get(1)).name(), anyOf(equalTo("0-3--2")));

        assertThat(((TimedNamedOperation1) topK.get(2)).name(), anyOf(equalTo("1-2--3"), equalTo("2-2--3")));
        assertThat(((TimedNamedOperation1) topK.get(3)).name(), anyOf(equalTo("1-2--3"), equalTo("2-2--3")));
        assertThat(((TimedNamedOperation1) topK.get(2)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(3)).name())));

        assertThat(((TimedNamedOperation1) topK.get(4)).name(), anyOf(equalTo("1-3--4"), equalTo("2-3--4")));
        assertThat(((TimedNamedOperation1) topK.get(5)).name(), anyOf(equalTo("1-3--4"), equalTo("2-3--4")));
        assertThat(((TimedNamedOperation1) topK.get(4)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(5)).name())));

        assertThat(((TimedNamedOperation1) topK.get(6)).name(), anyOf(equalTo("0-4--6")));

        assertThat(((TimedNamedOperation1) topK.get(7)).name(), anyOf(equalTo("0-5--7")));

        assertThat(((TimedNamedOperation1) topK.get(8)).name(), anyOf(equalTo("2-4--8"), equalTo("2-5--8")));
        assertThat(((TimedNamedOperation1) topK.get(9)).name(), anyOf(equalTo("2-4--8"), equalTo("2-5--8")));
        assertThat(((TimedNamedOperation1) topK.get(8)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(9)).name())));


        assertThat(((TimedNamedOperation1) topK.get(10)).name(), anyOf(equalTo("1-4--9"), equalTo("2-6--9")));
        assertThat(((TimedNamedOperation1) topK.get(11)).name(), anyOf(equalTo("1-4--9"), equalTo("2-6--9")));
        assertThat(((TimedNamedOperation1) topK.get(10)).name(),
            not(equalTo(((TimedNamedOperation1) topK.get(11)).name())));
    }

    private WorkloadStreams getWorkloadStreams() {
        GeneratorFactory gf = new GeneratorFactory(new RandomDataGeneratorFactory(42L));
        Iterator<Operation> asyncDependencyStream = new TimedNamedOperation1Factory(
            gf.incrementing(0L, 10L),
            gf.incrementing(0L, 10L),
            gf.constant("ad")
        );
        Iterator<Operation> asyncNonDependencyStream = new TimedNamedOperation1Factory(
            gf.incrementing(2L, 100L),
            gf.incrementing(2L, 100L),
            gf.constant("an")
        );
        Iterator<Operation> blockingDependencyStream1 = new TimedNamedOperation2Factory(
            gf.incrementing(4L, 1000L),
            gf.incrementing(4L, 1000L),
            gf.constant("bd1")
        );
        Iterator<Operation> blockingNonDependencyStream1 = new TimedNamedOperation2Factory(
            gf.incrementing(6L, 10000L),
            gf.incrementing(6L, 10000L),
            gf.constant("bn1")
        );
        Iterator<Operation> blockingDependencyStream2 = new TimedNamedOperation3Factory(
            gf.incrementing(8L, 10000L),
            gf.incrementing(8L, 10000L),
            gf.constant("bd2")
        );
        Iterator<Operation> blockingNonDependencyStream2 = new TimedNamedOperation3Factory(
            gf.incrementing(10L, 100000L),
            gf.incrementing(10L, 100000L),
            gf.constant("bn2")
        );
        WorkloadStreams workloadStreams = new WorkloadStreams();
        workloadStreams.setAsynchronousStream(
            new HashSet<Class<? extends Operation>>(),
            Sets.<Class<? extends Operation>>newHashSet(TimedNamedOperation1.class),
            asyncDependencyStream,
            asyncNonDependencyStream,
            null
        );

        return workloadStreams;
    }

    private class TestWorkload extends Workload {

        @Override
        public Map<Integer, Class<? extends Operation>> operationTypeToClassMapping() {
            Map<Integer, Class<? extends Operation>> operationTypeToClassMapping = new HashMap<>();
            operationTypeToClassMapping.put(NothingOperation.TYPE, NothingOperation.class);
            operationTypeToClassMapping.put(TimedNamedOperation1.TYPE, TimedNamedOperation1.class);
            operationTypeToClassMapping.put(TimedNamedOperation2.TYPE, TimedNamedOperation2.class);
            operationTypeToClassMapping.put(TimedNamedOperation3.TYPE, TimedNamedOperation3.class);
            return operationTypeToClassMapping;
        }

        @Override
        public void onInit(Map<String, String> params) throws WorkloadException {
        }

        @Override
        public Set<Class> enabledValidationOperations() {
            return Collections.emptySet();
        }

        @Override
        public void onClose() {

        }

        @Override
        public Class<? extends Operation> getOperationClass() {
            return Operation.class;
        }

        @Override
        protected WorkloadStreams getStreams(GeneratorFactory generators, boolean hasDbConnected)
            throws WorkloadException {
            return getWorkloadStreams();
        }
    }
}
