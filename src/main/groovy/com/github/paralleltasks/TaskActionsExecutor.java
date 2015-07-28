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

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * Executes each Gradle task actions for the task identified by {@link TaskActionsExecutor#taskName}
 */
public class TaskActionsExecutor implements Runnable {

    /**
     * Reference to the Gradle {@link Project}
     */
    private final Project project;

    /**
     * The name of the gradle {@link Task} to execute
     */
    private final String taskName;

    /**
     *
     * @param project The Gradle {@link Project}
     * @param taskName The name of the Gradle {@link Task}
     */
    public TaskActionsExecutor(Project project, String taskName) {
        this.project = project;
        this.taskName = taskName;
    }

    /**
     * Execute each {@link Action} from the provided {@link TaskActionsExecutor#taskName}
     */
    @Override
    public void run() {
        Task task = project.getTasks().getByPath(taskName);
        task.getActions().forEach((Action action) -> action.execute(task));
    }
}
