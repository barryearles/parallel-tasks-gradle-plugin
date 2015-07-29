# Parallel Task Execution Gradle Plugin
![Build Status](https://travis-ci.org/barryearles/parallel-tasks-gradle-plugin.svg)

This plugin allows tasks that are defined within the same module to be executed in parallel (as specified in the __parallelTasks__ configuration).  It should not be confused with the [--parallel](https://github.com/gradle/gradle/blob/master/design-docs/done/parallel-project-execution.md "--parallel") option that allows tasks from separate projects in a decoupled multi-project build to be executed in parallel.

## Use Case

I needed the ability to run several servers concurrently to assist with development

## Usage

Add the dependency to your build script

	TODO

Apply the plugin

	apply plugin: 'parallel-tasks'

After applying the plugin, a new task named __runParallelTasks__ is now available

	$ ./gradlew tasks
    :tasks

    ...

    Other tasks
    -----------
    runParallelTasks - Provides the ability to execute tasks in parallel

Configure the tasks that you wish to be executed in parallel

	task webpackDevServer << {
        // Start Webpack Dev Server
    }

    task browsersyncServer << {
        // Start Browsersync Server
    }

    parallelTasks {
        taskNames = [
            "browsersyncServer",
            "webpackDevServer",
            "bootRun"  // Task provided by "spring-boot" gradle plugin (e.g. apply plugin: 'spring-boot')
        ]
    }

Execute the tasks in parallel

	./gradlew runParallelTasks

## Building from Source

	./gradlew clean build
