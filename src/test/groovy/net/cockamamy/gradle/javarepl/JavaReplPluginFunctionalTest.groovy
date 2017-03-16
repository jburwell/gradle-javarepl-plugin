/* Copyright 2017 John S. Burwell III
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    def "Successfully start Java REPL"() {
        given:
            myBuildFile << """
                plugins {
                    id 'net.cockamamy.gradle.javarepl'
                }
                
                repositories {
                    jcenter()
                }
                
                ext {
                    javarepl {
                        timeout = 10
                    }
                }
            """

        when:
            final result = GradleRunner.create()
                .withProjectDir(myTestProjectDir.root)
                .withArguments("javarepl")
                .withPluginClasspath()
                .build()

        then:
            result.task(":javarepl").outcome == UP_TO_DATE

        where:
            gradleVersion << ["3.3", "3.4"]

    }

}
