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
package com.github.paralleltasks;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.MarkerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Task to allow concurrent execution of tasks within the same module
 */
class ParallelTasks extends DefaultTask {

    /**
     * Constructor
     */
    public ParallelTasks() { }

    /**
     * Adds concurrent task execution behavior to this task
     */
    @TaskAction
    public void exec() {
        ArrayList<Thread> taskThreads = new ArrayList<>();
        executeTasksInParallel(taskThreads);
    }

    /**
     * Execute the tasks in parallel
     *
     * @param taskThreads The list of threads that represent each task to execute
     */
    protected void executeTasksInParallel(List<Thread> taskThreads) {

        ParallelTasksExtension parallelTasksExtension;

        parallelTasksExtension =
                (ParallelTasksExtension) getProject().getExtensions().getByName(ParallelTasksPlugin.EXTENSION_NAME);

        List<String> tasksToExecuteInParallel = parallelTasksExtension.getTaskNames();
        if (tasksToExecuteInParallel.isEmpty()) {
            getLogger().info(
                    MarkerFactory.getMarker(ParallelTasksPlugin.PLUGIN_NAME),
                    "{} configuration not provided, continuing without running any tasks in parallel",
                    ParallelTasksPlugin.EXTENSION_NAME);
        } else {
            tasksToExecuteInParallel.forEach((String task) -> {
                TaskActionsExecutor taskActionsExecutor = new TaskActionsExecutor(getProject(), task);
                Thread taskThread = new Thread(taskActionsExecutor);
                taskThreads.add(taskThread);
                taskThread.start();
            });

            waitForCompletionOf(taskThreads);
        }
    }

    /**
     * Wait for parallel tasks to complete before executing other tasks
     *
     * @param taskThreads The tasks executing in parallel
     */
    protected void waitForCompletionOf(List<Thread> taskThreads) {
        taskThreads.forEach((taskThread) -> {
            try {
                taskThread.join();
            } catch(InterruptedException ie) {
                getProject().getLogger().error(
                        MarkerFactory.getMarker(ParallelTasksPlugin.PLUGIN_NAME),
                        "Error waiting for completion of thread: {}", ToStringBuilder.reflectionToString(taskThread));
                ie.printStackTrace();
            }
        });
    }
}
