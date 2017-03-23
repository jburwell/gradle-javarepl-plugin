#!/bin/sh

./gradlew -b test-build.gradle --no-daemon --stacktrace --console plain javarepl
