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

import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertEquals

import org.gmock.WithGMock
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

@WithGMock
class RunParallelTasksTaskPluginTest {

    ParallelTasksPlugin parallelTasksPlugin

    Project mockProject
    RunParallelTasksTask mockParallelTasks
    ExtensionContainer mockExtensionContainer
    TaskContainer mockTaskContainer

    @Before
    void setup() {
        parallelTasksPlugin = new ParallelTasksPlugin()
    }

    @Test
    void testCanAddParallelTasksTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        Task task = project.task(ParallelTasksPlugin.TASK_NAME, type: RunParallelTasksTask)

        assertTrue(task instanceof RunParallelTasksTask)
    }

    @Test
    void canAddActionsToParallelTasksTask() {
        Project project = ProjectBuilder.builder().build()
        Task task = project.task(ParallelTasksPlugin.TASK_NAME, type: RunParallelTasksTask)
        List<String> taskNames = addTasksToProject(project, 5);

        task.doFirst {
            print "first"
        }

        task.doLast {
            print "last"
        }

        assertEquals(3, task.getActions().size())
    }

    @Test
    void testApply() {
        mockProject()

        play {
            parallelTasksPlugin.apply(mockProject)
        }
    }

    void mockProject() {

        mockProject = mock(Project)
        mockExtensionContainer = mock(ExtensionContainer)
        mockTaskContainer = mock(TaskContainer)
        mockParallelTasks = mock(RunParallelTasksTask)

        mockProject.getExtensions().returns(mockExtensionContainer)
        mockExtensionContainer.create(ParallelTasksPlugin.EXTENSION_NAME, ParallelTasksExtension)
        mockProject.getTasks().returns(mockTaskContainer)
        mockTaskContainer.create(ParallelTasksPlugin.TASK_NAME, RunParallelTasksTask).returns(mockParallelTasks)
        mockParallelTasks.setDescription(ParallelTasksPlugin.DESCRIPTION)
    }

    List<String> addTasksToProject(Project project, int numberOfTasksToAdd) {
        List<String> taskNames = []
        (1..numberOfTasksToAdd).each {
            String taskName = "task-${it}"
            project.task(taskName, { print taskName })
            taskNames.add(taskName)
        }
        taskNames
    }
}
