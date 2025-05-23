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

package org.ldbcouncil.finbench.driver;

import stormpot.Poolable;
import stormpot.Slot;

public class InstantiatingOperationHandlerRunnerFactory implements OperationHandlerRunnerFactory {
    private static final Slot DUMMY_SLOT = new Slot() {
        @Override
        public void release(Poolable obj) {
            // do nothing
        }

        @Override
        public void expire(Poolable poolable) {
            // do nothing
        }
    };

    @Override
    public OperationHandlerRunnableContext newOperationHandlerRunner() throws OperationException {
        OperationHandlerRunnableContext operationHandlerRunnableContext = new OperationHandlerRunnableContext();
        operationHandlerRunnableContext.setSlot(DUMMY_SLOT);
        return operationHandlerRunnableContext;
    }

    @Override
    public void shutdown() throws OperationException {
        // nothing to do here
    }
}
