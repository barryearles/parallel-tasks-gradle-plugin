# Parallel Task Execution

This plugin allows tasks to be executed in parallel within the same module.  It should not be confused with the [--parallel](https://github.com/gradle/gradle/blob/master/design-docs/done/parallel-project-execution.md "--parallel") option that allows tasks from separate projects in a decoupled multi-project build to be executed in parallel.

## Usage

### Add the dependency

	TODO

### Apply the plugin

	apply plugin: 'parallel-tasks'

### Configure the tasks that should be executed in parallel

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

## Build

	./gradlew build