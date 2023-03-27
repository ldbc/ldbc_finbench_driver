package org.ldbcouncil.finbench.driver.workloads.transaction.queries;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead8Result {
    public static final String ACC_ID = "accId";
    public static final String P1_ID = "p1Id";
    public static final String P2_ID = "p2Id";
    public static final String COM_ID = "comId";
    public static final String LOAN_ID = "loanId";
    private final long accId;
    private final long p1Id;
    private final long p2Id;
    private final long comId;
    private final long loanId;

    public SimpleRead8Result(@JsonProperty(ACC_ID) long accId,
                             @JsonProperty(P1_ID) long p1Id,
                             @JsonProperty(P2_ID) long p2Id,
                             @JsonProperty(COM_ID) long comId,
                             @JsonProperty(LOAN_ID) long loanId) {
        this.accId = accId;
        this.p1Id = p1Id;
        this.p2Id = p2Id;
        this.comId = comId;
        this.loanId = loanId;
    }

    public long getAccId() {
        return accId;
    }

    public long getP1Id() {
        return p1Id;
    }

    public long getP2Id() {
        return p2Id;
    }

    public long getComId() {
        return comId;
    }

    public long getLoanId() {
        return loanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead8Result that = (SimpleRead8Result) o;
        return accId == that.accId
            && p1Id == that.p1Id
            && p2Id == that.p2Id
            && comId == that.comId
            && loanId == that.loanId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accId, p1Id, p2Id, comId, loanId);
    }

    @Override
    public String toString() {
        return "SimpleRead8Result{"
            + "accId="
            + accId
            + ", p1Id="
            + p1Id
            + ", p2Id="
            + p2Id
            + ", comId="
            + comId
            + ", loanId="
            + loanId
            + '}';
    }
}

