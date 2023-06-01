package org.ldbcouncil.finbench.driver.workloads.transaction.queries;
/*
 * Transaction workload write query 9:
 * -- Add Invest Between Company And Company --
 * Add a *invest* edge from a *Company* node to a *Company* node. 
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcNoResult;
import org.ldbcouncil.finbench.driver.workloads.transaction.LdbcOperation;

public class Write9 extends LdbcOperation<LdbcNoResult> {
    public static final int TYPE = 1009;
    public static final String COMPANY_ID1 = "companyId1";
    public static final String COMPANY_ID2 = "companyId2";
    public static final String TIME = "time";
    public static final String RATIO = "ratio";
    private final long companyId1;
    private final long companyId2;
    private final Date time;
    private final double ratio;

    public Write9(@JsonProperty(COMPANY_ID1) long companyId1,
                  @JsonProperty(COMPANY_ID2) long companyId2,
                  @JsonProperty(TIME) Date time,
                  @JsonProperty(RATIO) double ratio) {
        this.companyId1 = companyId1;
        this.companyId2 = companyId2;
        this.time = time;
        this.ratio = ratio;
    }

    public Write9(Write9 operation) {
        this.companyId1 = operation.companyId1;
        this.companyId2 = operation.companyId2;
        this.time = operation.time;
        this.ratio = operation.ratio;
    }

    @Override
    public Write9 newInstance() {
        return new Write9(this);
    }

    public long getCompanyId1() {
        return companyId1;
    }

    public long getCompanyId2() {
        return companyId2;
    }

    public Date getTime() {
        return time;
    }

    public double getRatio() {
        return ratio;
    }

    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Map<String, Object> parameterMap() {
        return ImmutableMap.<String, Object>builder()
            .put(COMPANY_ID1, companyId1)
            .put(COMPANY_ID2, companyId2)
            .put(TIME, time)
            .put(RATIO, ratio)
            .build();
    }

    @Override
    public LdbcNoResult deserializeResult(String serializedResults) {
        return LdbcNoResult.INSTANCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Write9 that = (Write9) o;
        return companyId1 == that.companyId1
            && companyId2 == that.companyId2
            && Objects.equals(time, that.time)
            && ratio == that.ratio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId1, companyId2, time, ratio);
    }

    @Override
    public String toString() {
        return "Write9{"
            + "companyId1="
            + companyId1
            + ", companyId2="
            + companyId2
            + ", time="
            + time
            + ", ratio="
            + ratio
            + '}';
    }
}

