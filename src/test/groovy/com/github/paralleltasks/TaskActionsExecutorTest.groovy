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
import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

import static org.gmock.GMockTestCase.mock

import org.gradle.api.Project
import org.junit.Before
import org.junit.Test

@WithGMock
class TaskActionsExecutorTest {

    TaskActionsExecutor taskActionsExecutor

    Project mockProject
    Set<Task> mockTasks
    List<Action> mockActions

    @Before
    void setup() {
        mockTasks = []
    }

    @Test
    void testRun() {

        mockGradleProject mockDefaultActions
        taskActionsExecutor = new TaskActionsExecutor(mockProject, ParallelTasksPlugin.TASK_NAME)

        play {
            taskActionsExecutor.run()
        }
    }

    @Test
    void testRunWithEmptyTaskNames() {

        mockGradleProject mockEmptyActions
        taskActionsExecutor = new TaskActionsExecutor(mockProject, ParallelTasksPlugin.TASK_NAME)

        play {
            taskActionsExecutor.run()
        }
    }

    def mockEmptyActions = {
        []
    }

    def mockDefaultActions = {
        Action mockAction1 = mock(Action)
        Action mockAction2 = mock(Action)
        [mockAction1, mockAction2]
    }

    void mockGradleProject(Closure mockActionsClosure) {
        mockActions = mockActionsClosure()

        Task mockParallelTasksTask = mock(Task)
        TaskContainer mockTaskContainer = mock(TaskContainer)
        mockProject = mock(Project)

        mockProject.getTasks().returns(mockTaskContainer)
        mockTaskContainer.getByPath(ParallelTasksPlugin.TASK_NAME).returns(mockParallelTasksTask)
        mockParallelTasksTask.getActions().returns(mockActions)

        mockActions.each { mockAction ->
            mockAction.execute(mockParallelTasksTask)
        }
    }
}
