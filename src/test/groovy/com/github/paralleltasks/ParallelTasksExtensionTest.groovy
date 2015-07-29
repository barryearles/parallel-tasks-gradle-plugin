/**
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Barry Earles
 */
package com.github.paralleltasks

import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * Test class for {@link ParallelTasksExtension}
 */
public class ParallelTasksExtensionTest {

    @Test
    public void testParallelTaskExtension() {
        ParallelTasksExtension parallelTasksExtension = new ParallelTasksExtension();
        assertEquals("Contains no task names", 0, parallelTasksExtension.taskNames.size());

        parallelTasksExtension.taskNames = ["task1", "task2"]
        assertEquals("Contains the correct number of task names", 2, parallelTasksExtension.taskNames.size());
    }
}
