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

import org.gmock.WithGMock
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * Test class for {@link RunParallelTasksTask}
 */
class RunParallelTasksTaskTest {

    Project project
    RunParallelTasksTask runParallelTasksTask
    List<Thread> taskThreads

    @Before
    void setup() {
        createProject()
        createParallelTaskExtension()
        taskThreads = []
    }

    @Test
    void testExecuteWithMultipleParallelTasks() {
        addExtensionConfiguration(3)

        runParallelTasksTask.exec()

        assertEquals 'Project contains the correct number of tasks', 4, project.getTasks().size()

        assertNotNull 'Project contains task-1', project.getTasks().getByName('task-1')
        assertNotNull 'Project contains task-2', project.getTasks().getByName('task-2')
        assertNotNull 'Project contains task-3', project.getTasks().getByName('task-3')

        assertNotNull "Project contains ${ParallelTasksPlugin.TASK_NAME} task",
            project.getTasks().getByName(ParallelTasksPlugin.TASK_NAME)
    }

    @Test
    void testExecuteWithSingleTask() {
        addExtensionConfiguration(1)

        runParallelTasksTask.exec()

        assertEquals 'Project contains the correct number of tasks', 2, project.getTasks().size()

        assertNotNull 'Project contains task-1', project.getTasks().getByName('task-1')

        assertNotNull "Project contains ${ParallelTasksPlugin.TASK_NAME} task",
            project.getTasks().getByName(ParallelTasksPlugin.TASK_NAME)
    }

    @Test
    void testExecuteWithNoParallelTasks() {
        runParallelTasksTask.exec()

        assertEquals 'Project contains the correct number of tasks', 1, project.getTasks().size()

        assertNotNull "Project contains ${ParallelTasksPlugin.TASK_NAME} task",
            project.getTasks().getByName(ParallelTasksPlugin.TASK_NAME)
    }

    @Test
    void testExecuteTasksInParallel() {

        addExtensionConfiguration(5)

        runParallelTasksTask.executeTasksInParallel(taskThreads)

        assertEquals 'Project contains the correct number of tasks', 6, project.getTasks().size()

        assertNotNull 'Project contains task-1', project.getTasks().getByName('task-1')
        assertNotNull 'Project contains task-2', project.getTasks().getByName('task-2')
        assertNotNull 'Project contains task-3', project.getTasks().getByName('task-3')
        assertNotNull 'Project contains task-4', project.getTasks().getByName('task-4')
        assertNotNull 'Project contains task-5', project.getTasks().getByName('task-5')

        assertNotNull "Project contains ${ParallelTasksPlugin.TASK_NAME} task",
            project.getTasks().getByName(ParallelTasksPlugin.TASK_NAME)
    }

    @Test
    void testWaitForCompletionOfMultipleThreadTasks() {
        createTasks(5)
        runParallelTasksTask.waitForCompletionOf(taskThreads)
    }

    @Test
    void testWaitForCompletionOfSingleThreadTasks() {
        createTasks(1)
        runParallelTasksTask.waitForCompletionOf(taskThreads)
    }

    @Test
    void testWaitForCompletionOfNoThreadTasks() {
        createTasks(0)
        runParallelTasksTask.waitForCompletionOf(taskThreads)
    }

    void createProject() {
        project = ProjectBuilder.builder().build()
        runParallelTasksTask = project.getTasks().create(ParallelTasksPlugin.TASK_NAME, RunParallelTasksTask);
    }

    void createParallelTaskExtension() {
        project.getExtensions().create(ParallelTasksPlugin.EXTENSION_NAME, ParallelTasksExtension)
    }

    void addExtensionConfiguration(int numberOfTasks) {
        project.getExtensions().getByName(ParallelTasksPlugin.EXTENSION_NAME).taskNames = createTasks(numberOfTasks)
    }

    List<String> createTasks(int numOfTasks) {
        List<String> taskNames = []
        (1..numOfTasks).each {
            String taskName = "task-${it}"
            taskNames.add(taskName)
            TaskActionsExecutor taskActionsExecutor = new TaskActionsExecutor(project, taskName)
            taskThreads.add(new Thread(taskActionsExecutor))
            project.getTasks().create(taskName)
        }
        taskNames
    }
}
