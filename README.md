[![Build Status](https://travis-ci.org/jburwell/gradle-javarepl-plugin.svg?branch=master)](https://travis-ci.org/jburwell/gradle-javarepl-plugin)

# Motivation

This plugin runs [JavaREPL](http://www.javarepl.com) within the context a Gradle build file.  It configures the classpath used by javarepl to resolve classes to include all [`testRuntime`](https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_plugin_and_dependency_management) dependencies, as well as, the compile and test artifacts for this project.  Using this plugin, developers can easily explore and execute different aspects of their projects via a REPL.

The following is a sample of the plugin output:

```
➜ gradle-javarepl-plugin git:(jsb/release-1.0.0) ✗ ./gradlew --no-daemon --console plain javarepl
:compileJava NO-SOURCE
:processResources
:classes
:compileTestJava NO-SOURCE
:processTestResources NO-SOURCE
:testClasses UP-TO-DATE
:javarepl


Welcome to JavaREPL version 428 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_92)
Type expression to evaluate, :help for more options or press tab to auto-complete.
Connected to local instance at http://localhost:63534
java>
```

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
|``configurations``|The list of Gradle configurations from which the classpath used to run Java REPL is derived|``["testRuntime"]``|
|``dependsOn``|The task on which the javarepl tasks depends for execution|``testClassses``|
|``heapSize``|The heap size used to run JavaREPL|``null``|
|``timeout``|The length of time to run Java REPL before killing it.  This parameter is primarily use by unit tests. |``null``|
|``stackSize``|The stack size used to run JavaREPL|``null``|
|``version``|The version of Java REPL to install|428|

# Running

Normally, Gradle will attempt to use an existing daemon to speed up builds.  Unfortunately, this feature prevents interactive console applications such as JavaREPL from running properly.  Therefore, Gradle should be started with the ``--no-daemon`` commmand line option when running the ``javarepl`` task.  In addition to the daemon issue, Gradle's rich console can interfere with the JavaREPL's operations.  Running the task with the ``--console plain`` option will disable the rich console to avoid these potential issues.  The following is an example command line to properly execute the ``javarepl`` task:

```
./gradlew javarepl --no-daemon --console plain
```

To reduce the overhead to typing this command line regularly, it is suggested that users add the following alias to their shell profile:

```
alias grel='./gradlew javarepl --no-daemon --console plain'
```

With this alias, the ``grepl`` command will start a Java REPL using Gradle.

# Manual Testing

The `test-repl.sh` script is provided to run development builds of the plugin for manual testing purposes.

# Known Issues

Quiting JavaREPL, either by using the ``:quit`` command or `CTRL-C`, causes the following stack trace:

```
Exception in thread "main" java.lang.RuntimeException: { expected, j encountered.
	at javarepl.internal.totallylazy.parser.Failure.exception(Failure.java:76)
	at javarepl.internal.totallylazy.parser.Failure.value(Failure.java:72)
	at javarepl.internal.totallylazy.json.Json.map(Json.java:20)
	at javarepl.client.JavaREPLClient.execute(JavaREPLClient.java:72)
	at javarepl.Main.main(Main.java:66)
```

This exception is spurious and does not cause any problems.
