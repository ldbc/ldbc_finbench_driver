package org.ldbcouncil.finbench.driver.workloads.transaction;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ldbcouncil.finbench.driver.result.Path;
import org.ldbcouncil.finbench.driver.truncation.TruncationOrder;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead1;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5;
import org.ldbcouncil.finbench.driver.workloads.transaction.queries.ComplexRead5Result;

public class TransactionQueryEqualityTest {
    public long id1;
    public long id2;
    public Date startTime1;
    public Date startTime2;
    public Date endTime1;
    public Date endTime2;
    public int truncationLimit1;
    public int truncationLimit2;
    public TruncationOrder truncationOrder1;
    public TruncationOrder truncationOrder2;

    @BeforeEach
    public void init() {
        // Given ordinary params
        id1 = 1;
        startTime1 = new Date(1);
        endTime1 = new Date(2);
        truncationLimit1 = 10;
        truncationOrder1 = TruncationOrder.TIMESTAMP_DESCENDING;

        id2 = 2;
        startTime2 = new Date(3);
        endTime2 = new Date(4);
        truncationLimit2 = 100;
        truncationOrder2 = TruncationOrder.TIMESTAMP_ASCENDING;
    }

    @Test
    public void complexRead1ShouldDoEqualsCorrectly() {
        // Given special params

        // When
        ComplexRead1 query1 = new ComplexRead1(id1, startTime1, endTime1, truncationLimit1, truncationOrder1);
        ComplexRead1 query2 = new ComplexRead1(id1, startTime1, endTime1, truncationLimit1, truncationOrder1);
        ComplexRead1 query3 = new ComplexRead1(id2, startTime2, endTime2, truncationLimit2, truncationOrder2);

        // Then
        assertThat(query1, equalTo(query2));
        assertThat(query1, not(equalTo(query3)));
    }

    @Test
    public void complexRead2ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead3ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead4ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead5ShouldDoEqualsCorrectly() {
        List<ComplexRead5Result> query1 = new ArrayList<>();
        List<ComplexRead5Result> query2 = new ArrayList<>();
        List<Long> path1 =  new ArrayList<>();
        path1.add(1L);
        path1.add(2L);
        path1.add(3L);
        List<Long> path2 =  new ArrayList<>();
        path2.add(2L);
        path2.add(3L);
        path2.add(4L);
        query1.add(new ComplexRead5Result(new Path(path1)));
        query1.add(new ComplexRead5Result(new Path(path2)));
        query2.add(new ComplexRead5Result(new Path(path2)));
        query2.add(new ComplexRead5Result(new Path(path1)));
        ComplexRead5 complexRead5 = new ComplexRead5(0, null, null,
            0, null);
        complexRead5.resultSort(query1);
        complexRead5.resultSort(query2);
        assertThat(query1, equalTo(query2));
    }

    @Test
    public void complexRead6ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead7ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead8ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead9ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead10ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead11ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead12ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void complexRead13ShouldDoEqualsCorrectly() {
        // TODO add complex read params equals test
    }

    @Test
    public void simpleRead1ShouldDoEqualsCorrectly() {
        // TODO add simple read params equals test
    }

    @Test
    public void simpleRead2ShouldDoEqualsCorrectly() {
        // TODO add simple read params equals test
    }

    @Test
    public void simpleRead3ShouldDoEqualsCorrectly() {
        // TODO add simple read params equals test
    }

    @Test
    public void simpleRead4ShouldDoEqualsCorrectly() {
        // TODO add simple read params equals test
    }

    @Test
    public void simpleRead5ShouldDoEqualsCorrectly() {
        // TODO add simple read params equals test
    }

    @Test
    public void simpleRead6ShouldDoEqualsCorrectly() {
        // TODO add simple read params equals test
    }

    @Test
    public void simpleRead7ShouldDoEqualsCorrectly() {
        // TODO add simple read params equals test
    }

    @Test
    public void simpleRead8ShouldDoEqualsCorrectly() {
        // TODO add simple read params equals test
    }

    @Test
    public void write1ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write2ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write3ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write4ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write5ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write6ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write7ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write8ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write9ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write10ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write11ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write12ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }

    @Test
    public void write13ShouldDoEqualsCorrectly() {
        // TODO add write params equals test
    }
}
