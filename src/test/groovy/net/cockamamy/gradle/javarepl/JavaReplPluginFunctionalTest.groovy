package net.cockamamy.gradle.javarepl

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

class JavaReplPluginFunctionalTest extends Specification {

    @Rule final TemporaryFolder myTestProjectDir = new TemporaryFolder();
    File myBuildFile

    def setup() {
        myBuildFile = myTestProjectDir.newFile("build.gradle")
    }

    def "Start Java REPL"() {
        given:
            myBuildFile << """
                plugins {
                    id 'net.cockamamy.gradle.javarepl'
                }
            """

        when:
            def runner = GradleRunner.create()
                .withProjectDir(myTestProjectDir.root)
                .withArguments("javarepl")
                .withPluginClasspath()

            println("Runner classpath: " + runner.getPluginClasspath())
            def result = runner.build()

        then:
            println("Available tasks: " + result.getTasks())
            result.task(":javarepl").outcome == SUCCESS

    }

}
