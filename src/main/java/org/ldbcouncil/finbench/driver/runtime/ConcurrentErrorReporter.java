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

package org.ldbcouncil.finbench.driver.runtime;

import static java.lang.String.format;

import com.google.common.collect.ImmutableList;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// TODO rewrite like sync CT tracker class, to be more threadsafe
public class ConcurrentErrorReporter {
    public static String stackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    private static String whoAmI(Object caller) {
        Thread myThread = Thread.currentThread();
        return format("%s [%s] (Thread: ID=%s, Name=%s, Priority=%s)",
            caller.getClass().getSimpleName(),
            Thread.currentThread().getStackTrace()[3].getLineNumber(),
            myThread.getId(),
            myThread.getName(),
            myThread.getPriority());
    }

    private static String formatErrors(List<ErrorReport> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n- Start Error Log -");
        // Do this to avoid ConcurrentModificationException in case error is reported while iterating through errors
        Iterator<ErrorReport> errorsIterator = ImmutableList.copyOf(errors).iterator();
        while (errorsIterator.hasNext()) {
            ErrorReport error = errorsIterator.next();
            sb.append("\n\tSOURCE:\t").append(error.source());
            sb.append("\n\tERROR:\t").append(error.error());
        }
        sb.append("\n- End Error Log -\n");
        return sb.toString();
    }

    private final List<ErrorReport> errorMessages = new ArrayList<>();

    public synchronized void reportError(Object caller, String errMsg) {
        syncGetErrorMessages().add(new ErrorReport(whoAmI(caller), errMsg));
    }

    public boolean errorEncountered() {
        return !syncGetErrorMessages().isEmpty();
    }

    List<ErrorReport> errorMessages() {
        return syncGetErrorMessages();
    }

    @Override
    public String toString() {
        if (syncGetErrorMessages().isEmpty()) {
            return "No Reported Errors";
        } else {
            return formatErrors(syncGetErrorMessages());
        }
    }

    private synchronized List<ErrorReport> syncGetErrorMessages() {
        return errorMessages;
    }

    public static class ErrorReport {
        private final String source;
        private final String error;

        ErrorReport(String source, String error) {
            this.source = source;
            this.error = error;
        }

        public String source() {
            return source;
        }

        public String error() {
            return error;
        }
    }
}
