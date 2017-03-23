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

    static final TEST_VERSIONS = ["3.3", "3.4"]

    @Rule final TemporaryFolder myTestProjectDir = new TemporaryFolder();
    File myBuildFile

    def setup() {
        myBuildFile = myTestProjectDir.newFile("build.gradle")
    }

    def "Run Java REPL with default settings"() {
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
            result.task(":javarepl").outcome == SUCCESS
            result.task(":testClasses").outcome == SUCCESS || result.task(":testClasses").outcome == UP_TO_DATE
            result.tasks.findIndexOf { it.path == ":testClasses" } < result.tasks.findIndexOf { it.path == ":javarepl"}

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a single, valid, custom configuration"() {
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
                            configurations = ['compile']
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
            result.task(":javarepl").outcome == SUCCESS
            result.task(":testClasses").outcome == SUCCESS || result.task(":testClasses").outcome == UP_TO_DATE
            result.tasks.findIndexOf { it.path == ":testClasses" } < result.tasks.findIndexOf { it.path == ":javarepl"}

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a multiple, valid custom configurations"() {
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
                                configurations = ['compile', 'testCompile']
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
            result.task(":javarepl").outcome == SUCCESS
            result.task(":testClasses").outcome == SUCCESS || result.task(":testClasses").outcome == UP_TO_DATE
            result.tasks.findIndexOf { it.path == ":testClasses" } < result.tasks.findIndexOf { it.path == ":javarepl"}

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a single, invalid, custom configuration"() {
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
                                configurations = ['foo']
                            }
                        }
                    """

        when:
            final result = GradleRunner.create()
                .withProjectDir(myTestProjectDir.root)
                .withArguments("javarepl")
                .withPluginClasspath()
                .buildAndFail()

        then:
            result.output.contains("Configuration with name 'foo' not found.")

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a custom heap size"() {
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
                            heapSize = 512
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
            result.task(":javarepl").outcome == SUCCESS
            result.task(":testClasses").outcome == SUCCESS || result.task(":testClasses").outcome == UP_TO_DATE
            result.tasks.findIndexOf { it.path == ":testClasses" } < result.tasks.findIndexOf { it.path == ":javarepl"}

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a custom stack size"() {
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
                                stackSize = 256
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
            result.task(":javarepl").outcome == SUCCESS
            result.task(":testClasses").outcome == SUCCESS || result.task(":testClasses").outcome == UP_TO_DATE
            result.tasks.findIndexOf { it.path == ":testClasses" } < result.tasks.findIndexOf { it.path == ":javarepl"}

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a custom heap and stack size"() {
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
                                    heapSize = 512
                                    stackSize = 256
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
            result.task(":javarepl").outcome == SUCCESS
            result.task(":testClasses").outcome == SUCCESS || result.task(":testClasses").outcome == UP_TO_DATE
            result.tasks.findIndexOf { it.path == ":testClasses" } < result.tasks.findIndexOf { it.path == ":javarepl"}

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a custom dependent task"() {
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
                                    dependsOn = "classes"
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
            result.task(":javarepl").outcome == SUCCESS
            result.task(":classes").outcome == SUCCESS || result.task(":classes").outcome == UP_TO_DATE
            result.tasks.findIndexOf { it.path == ":classes" } < result.tasks.findIndexOf { it.path == ":javarepl"}

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with an invalid custom dependent task"() {
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
                                    dependsOn = "clazzes"
                                }
                            }
                        """

        when:
            final result = GradleRunner.create()
                .withProjectDir(myTestProjectDir.root)
                .withArguments("javarepl")
                .withPluginClasspath()
                .buildAndFail()

        then:
            result.output.contains("Task with path 'clazzes' not found")

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a null custom dependent task"() {
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
                                    dependsOn = null
                                }
                            }
                        """

        when:
            final result = GradleRunner.create()
                .withProjectDir(myTestProjectDir.root)
                .withArguments("javarepl")
                .withPluginClasspath()
                .buildAndFail()

        then:
            result.output.contains("The Java REPL plugin requires the specification of dependent task.")

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a null JavaREPL version"() {
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
                                    version = null
                                }
                            }
                        """

        when:
            final result = GradleRunner.create()
                .withProjectDir(myTestProjectDir.root)
                .withArguments("javarepl")
                .withPluginClasspath()
                .buildAndFail()

        then:
            result.output.contains("The Java REPL plugin requires the specification of a Java REPL version.")

        where:
            gradleVersion << TEST_VERSIONS

    }

    def "Run Java REPL with a blank JavaREPL version"() {
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
                                    version = ""
                                }
                            }
                        """

        when:
            final result = GradleRunner.create()
                .withProjectDir(myTestProjectDir.root)
                .withArguments("javarepl")
                .withPluginClasspath()
                .buildAndFail()

        then:
            result.output.contains("The Java REPL plugin requires the specification of a Java REPL version.")

        where:
            gradleVersion << TEST_VERSIONS

    }

}
