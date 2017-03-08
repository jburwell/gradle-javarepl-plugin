[![Build Status](https://travis-ci.org/jburwell/gradle-javarepl-plugin.svg?branch=master)](https://travis-ci.org/jburwell/gradle-javarepl-plugin)

# Motivation

This plugin runs [JavaREPL](http://www.javarepl.com) within the context a Gradle build file.  It configures the classpath used by javarepl to resolve classes to include all [`testRuntime`](https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_plugin_and_dependency_management) dependencies, as well as, the compile and test artifacts for this project.  Using this plugin, developers can easily explore and execute different aspects of their projects via a REPL.

# Installation 

**N.B.** This plugin requires JDK 8+.  Java 7 and below are **not** supported.

Adding the following block to a `build.gradle` installs this plugin:

```
    plugins {
        id 'net.cockamamy.gradle.javarepl'
    }
```

If the [java](https://docs.gradle.org/current/userguide/java_plugin.html) plugin has not been applied when this plugin is invoked, it will be applied automatically.

# Configuration

The plugin adds a ``javarepl`` extension namespace with the following options:

|Extension Property|Description|Default Value|
|------------------|-----------|-------------|
|``baseConfiguration``|The Gradle configuration from which the classpath used to run Java REPL is derived|``testRuntime``|
|``version``|The version of Java REPL to install|428|
|``timeout``|The length of time to run Java REPL before killing it.  This parameter is primarily for unit testing purposes.|``null``|

# Running

Normally, Gradle runs using a rich console which adds a status line to depict build status.  It will also attempt to use an existing daemon to speed up builds.  Unfortunately, these two features prevent interactive console applications such as JavaREPL from running properly.  Therefore, Gradle should be run with the ``--console plain`` and ``--no-daemon`` options when running the ``javarepl`` task.  The following is an example command line to properly execute the ``javarepl`` task:

```
./gradlew javarepl --console plain --no-daemon
```

To reduce the overhead to typing this command line regularly, it is suggested that users add the following alias to their shell profile:

```
alias grel='./gradlew javarepl --console plain --no-daemon"
```

With this alias, the ``grepl`` command will start a Java REPL using Gradle.
