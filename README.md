[![Build Status](https://travis-ci.org/jburwell/gradle-javarepl-plugin.svg?branch=master)](https://travis-ci.org/jburwell/gradle-javarepl-plugin)

# Motivation

This plugin runs [JavaREPL](http://www.javarepl.com) within the context a Gradle build file.  It configures the classpath used by javarepl to resolve classes to include all [`testRuntime`](https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_plugin_and_dependency_management) dependencies, as well as, the compile and test artifacts for this project.  Using this plugin, developers can easily explore and execute different aspects of their projects via a REPL.

# Installation and Configuration

**N.B.** This plugin requires JDK 8+.  Java 7 and below are not supported.

Adding the following block to a `build.gradle` installs this plugin:

```
    plugins {
        id 'net.cockamamy.gradle.javarepl'
    }
```

If the `java` plugin has not been applied when this plugin is invoked, it will be applied automatically.

## Gradle Dependency Configuration

By default, the plugin configures javarepl to use all dependencies defined in the `testRuntime` configuration.  The `baseConfiguration` plugin parameter overrides the configuration which the javarepl classpath is built.

## JavaREPL Version

By default, the plugin installs JavaREPL depedency (`com.javarepl:javarepl`) version 428.  The `version` plugin parameter overrides the JavaREPL version installed.
