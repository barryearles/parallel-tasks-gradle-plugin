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

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

/**
 * Test class for the {@link ParallelTasksPlugin}
 */
class ParallelTasksPluginTest {

    private static final String TASK_1 = 'task-1'
    private static final String TASK_2 = 'task-2'

    Project project

    @Before
    void setup() {
        project = ProjectBuilder.builder().build()
    }

    @Test
    void applyParallelTasksPlugin() {
        project.with {
            apply plugin: ParallelTasksPlugin.PLUGIN_ID
        }

        assertTrue project.getPlugins().hasPlugin(ParallelTasksPlugin.PLUGIN_ID)
        assertNotNull project.getTasks().getByName(ParallelTasksPlugin.TASK_NAME)
    }

    @Test
    void declareMultipleTaskNamesToRunInParallel() {

        project.getTasks().create(TASK_1)
        project.getTasks().create(TASK_2)

        project.with {
            apply plugin: ParallelTasksPlugin.PLUGIN_ID

            parallelTasks {
                taskNames = [
                    TASK_1,
                    TASK_2
                ]
            }

            gradle.afterProject { project, state ->
                if (state.failure) {
                    fail()
                }
            }
        }

        project.evaluate()

        assertEquals 'The project contains the parallelTasks configuration with multiple tasks to run in parallel',
            2, project.parallelTasks.taskNames.size()

        assertTrue "The project contains the parallelTasks configuration with ${TASK_1}",
            project.parallelTasks.taskNames.contains(TASK_1)

        assertTrue "The project contains the parallelTasks configuration with ${TASK_2}",
            project.parallelTasks.taskNames.contains(TASK_2)

        assertEquals "The project contains ${TASK_1}", TASK_1, project.getTasks().getByName(TASK_1).getName()
        assertEquals "The project contains ${TASK_2}", TASK_2, project.getTasks().getByName(TASK_2).getName()
    }


    @Test
    void declareSingleTask() {
        project.with {
            apply plugin: ParallelTasksPlugin.PLUGIN_ID

            parallelTasks {
                taskNames = [
                    TASK_1
                ]
            }

            gradle.afterProject { project, state ->
                if (state.failure) {
                    fail()
                }
            }
        }

        project.evaluate()

        assertEquals 'The project contains the parallelTasks configuration with a single task',
            1, project.parallelTasks.taskNames.size()

        assertTrue "The project contains the parallelTasks configuration with ${TASK_1}",
            project.parallelTasks.taskNames.contains(TASK_1)
    }

    @Test
    void declareNoTasksToRunInParallel() {
        project.with {
            apply plugin: ParallelTasksPlugin.PLUGIN_ID
        }

        assertEquals 'The project contains the parallelTasks configuration with a single task',
            0, project.parallelTasks.taskNames.size()
    }


    @Test
    void canAddActionsToRunParallelTasksTask() {

        project.getTasks().create(TASK_1)
        project.getTasks().create(TASK_2)

        project.with {
            apply plugin: ParallelTasksPlugin.PLUGIN_ID

            runParallelTasks {
                doFirst {
                    print 'first action'
                }

                doLast {
                    print 'last action'
                }
            }

            parallelTasks {
                taskNames = [
                    TASK_1,
                    TASK_2
                ]
            }

            gradle.afterProject { project, state ->
                if (state.failure) {
                    fail()
                }
            }
        }

        project.evaluate()

        assertEquals 'The runParallelTasks has more than one action', 3, project.tasks.size()
    }
}
