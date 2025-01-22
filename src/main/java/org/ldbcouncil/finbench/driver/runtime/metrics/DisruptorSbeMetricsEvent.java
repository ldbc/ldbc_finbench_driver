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

package org.ldbcouncil.finbench.driver.runtime.metrics;

import com.lmax.disruptor.EventFactory;
import java.nio.ByteBuffer;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.ldbcouncil.finbench.driver.runtime.metrics.sbe.MessageHeader;
import org.ldbcouncil.finbench.driver.runtime.metrics.sbe.MetricsEvent;

public class DisruptorSbeMetricsEvent {
    // Simple Binary Encoding related
    static final int MESSAGE_HEADER_SIZE;
    static final int ACTING_BLOCK_LENGTH;
    static final int SCHEMA_ID;
    static final int ACTING_VERSION;
    static final short MESSAGE_TEMPLATE_VERSION = 0;

    // Event Type Codes
    //   * Submit operation result for its metrics to be collected
    static final byte SUBMIT_OPERATION_RESULT = 1;
    //   * Request metrics summary
    static final byte GET_WORKLOAD_STATUS = 2;
    //   * Request complete workload results
    static final byte GET_WORKLOAD_RESULTS = 3;

    static {
        MetricsEvent metricsEvent = new MetricsEvent();
        MessageHeader messageHeader = new MessageHeader();
        UnsafeBuffer directBuffer = new MetricsCollectionEventFactory().newInstance();
        messageHeader.wrap(directBuffer, 0, MESSAGE_TEMPLATE_VERSION)
            .blockLength(metricsEvent.sbeBlockLength())
            .templateId(metricsEvent.sbeTemplateId())
            .schemaId(metricsEvent.sbeSchemaId())
            .version(metricsEvent.sbeSchemaVersion());
        MESSAGE_HEADER_SIZE = messageHeader.size();
        ACTING_BLOCK_LENGTH = messageHeader.blockLength();
        SCHEMA_ID = messageHeader.schemaId();
        ACTING_VERSION = messageHeader.version();
    }

    public static class MetricsCollectionEventFactory implements EventFactory<DirectBuffer> {
        private final MetricsEvent metricsEvent = new MetricsEvent();
        private final MessageHeader messageHeader = new MessageHeader();

        @Override
        public UnsafeBuffer newInstance() {
            final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(64);
            UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
            messageHeader.wrap(directBuffer, 0, MESSAGE_TEMPLATE_VERSION)
                .blockLength(metricsEvent.sbeBlockLength())
                .templateId(metricsEvent.sbeTemplateId())
                .schemaId(metricsEvent.sbeSchemaId())
                .version(metricsEvent.sbeSchemaVersion());
            return directBuffer;
        }
    }

    public static String toString(UnsafeBuffer event) {
        final MetricsEvent metricsEvent = new MetricsEvent();
        metricsEvent.wrapForDecode(event, MESSAGE_HEADER_SIZE, ACTING_BLOCK_LENGTH, ACTING_VERSION);
        return "METRICS_EVENT.eventType(){"
            + "eventType=" + metricsEvent.eventType()
            + ", operationType=" + metricsEvent.operationType()
            + ", scheduledStartTimeAsMilli=" + metricsEvent.scheduledStartTimeAsMilli()
            + ", actualStartTimeAsMilli=" + metricsEvent.actualStartTimeAsMilli()
            + ", runDurationAsNano=" + metricsEvent.runDurationAsNano()
            + ", resultCode=" + metricsEvent.resultCode()
            + ", originalStartTime=" + metricsEvent.originalStartTime()
            + '}';
    }
}
