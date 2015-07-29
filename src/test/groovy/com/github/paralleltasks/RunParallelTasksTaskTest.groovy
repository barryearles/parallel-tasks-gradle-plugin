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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

/**
 * Test class for {@link RunParallelTasksTask}
 */
class RunParallelTasksTaskTest {

    Project project
    RunParallelTasksTask parallelTasks
    List<Thread> taskThreads

    @Before
    void setup() {
        project = ProjectBuilder.builder().build()
        parallelTasks = project.getTasks().create(ParallelTasksPlugin.TASK_NAME, RunParallelTasksTask);
        taskThreads = []
    }

    @Test
    void testWaitForCompletionOfMultipleThreadTasks() {
        createTaskActionsExecutors(5)
        parallelTasks.waitForCompletionOf(taskThreads)
    }

    @Test
    void testWaitForCompletionOfSingleThreadTasks() {
        createTaskActionsExecutors(1)
        parallelTasks.waitForCompletionOf(taskThreads)
    }

    @Test
    void testWaitForCompletionOfNoThreadTasks() {
        createTaskActionsExecutors(0)
        parallelTasks.waitForCompletionOf(taskThreads)
    }

    void createTaskActionsExecutors(int numOfThreads) {
        (1..numOfThreads).each {
            TaskActionsExecutor taskActionsExecutor = new TaskActionsExecutor(project, "task-${it}")
            taskThreads.add(new Thread(taskActionsExecutor))
        }
    }
}
