package org.ldbcouncil.finbench.driver.workloads.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class SimpleRead6Result {
    public static final String COMPANY_ID = "companyId";
    private final long companyId;

    public SimpleRead6Result(@JsonProperty(COMPANY_ID) long companyId) {
        this.companyId = companyId;
    }

    public long getCompanyId() {
        return companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleRead6Result that = (SimpleRead6Result) o;
        return companyId == that.companyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId);
    }

    @Override
    public String toString() {
        return "SimpleRead6Result{"
            + "companyId="
            + companyId
            + '}';
    }
}

