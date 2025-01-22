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

package org.ldbcouncil.finbench.driver.validation;

import java.util.ArrayList;
import java.util.List;

public class ResultsLogValidationResult {
    public enum ValidationErrorType {
        TOO_MANY_LATE_OPERATIONS,
        LATE_OPERATIONS_FOR_TYPE,
        UNEXPECTED
    }

    private boolean aboveThreshold = false;

    public static class ValidationError {
        private final ValidationErrorType errorType;
        private final String message;

        public ValidationError(ValidationErrorType errorType, String message) {
            this.errorType = errorType;
            this.message = message;
        }

        public ValidationErrorType errorType() {
            return errorType;
        }

        public String message() {
            return message;
        }
    }

    private final List<ValidationError> errors;

    public ResultsLogValidationResult() {
        this.errors = new ArrayList<>();
    }

    public void addError(ValidationErrorType validationErrorType, String errorMessage) {
        errors.add(new ValidationError(validationErrorType, errorMessage));
    }

    public void aboveThreshold() {
        this.aboveThreshold = true;
    }

    public boolean getAboveThreshold() {
        return this.aboveThreshold;
    }

    public List<ValidationError> errors() {
        return errors;
    }

    public boolean isSuccessful() {
        return !aboveThreshold;
    }

    public String getScheduleAuditResult(boolean recordDelayedOperations) {
        StringBuffer sb = new StringBuffer();
        if (isSuccessful()) {
            sb.append("PASSED SCHEDULE AUDIT -- workload operations executed to schedule");
        } else {
            recordDelayedOperations = true;
            sb.append("FAILED SCHEDULE AUDIT -- errors:");
        }
        if (recordDelayedOperations) {
            for (ValidationError error : errors) {
                sb.append("\n\t").append(error.errorType().name()).append(" : ").append(error.message());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (isSuccessful()) {
            sb.append("PASSED SCHEDULE AUDIT -- workload operations executed to schedule");
        } else {
            sb.append("FAILED SCHEDULE AUDIT -- errors:");
            for (ValidationError error : errors) {
                sb.append("\n\t").append(error.errorType().name()).append(" : ").append(error.message());
            }
        }
        return sb.toString();
    }
}
