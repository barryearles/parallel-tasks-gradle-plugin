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

/**
 * The Gradle DSL extension for the 'parallel-tasks' plugin.
 *
 * <pre>
 *     apply plugin: 'parallel-tasks'
 *
 *     task example1 << {
 *         println "example1 task"
 *     }
 *
 *     task example2 << {
 *         println "example2 task"
 *     }
 *
 *     parallelTasks {
 *         taskNames = [ "example1", "example2" ]
 *     }
 * </pre>
 */
class ParallelTasksExtension {
    List<String> taskNames = []
}
