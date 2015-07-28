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

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The 'parallel-tasks' Gradle {@link Plugin}
 */
class ParallelTasksPlugin implements Plugin<Project> {

    protected static final String PLUGIN_NAME = ParallelTasksPlugin.class.getSimpleName()
    protected static final String TASK_NAME = "runParallelTasks"
    protected static final String EXTENSION_NAME = "parallelTasks"
    protected static final String DESCRIPTION = "Provides the ability to execute tasks in parallel"

    /**
     * {@inheritDoc}
     */
    @Override
    void apply(Project project) {

        project.getExtensions().create(EXTENSION_NAME, ParallelTasksExtension)
        ParallelTasks parallelTasks = project.getTasks().create(TASK_NAME, ParallelTasks)
        parallelTasks.setDescription(DESCRIPTION)
    }
}
