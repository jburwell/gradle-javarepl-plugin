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

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

import static java.lang.ProcessBuilder.Redirect.INHERIT
import static java.util.concurrent.TimeUnit.SECONDS
import static org.gradle.api.JavaVersion.VERSION_1_8
import static org.gradle.api.JavaVersion.VERSION_1_9

class JavaReplPlugin implements Plugin<Project> {

    static final String JAVAREPL_CONFIGURATION = "javarepl"

    @Override
    void apply(Project project) {

        project.extensions.create("javarepl", JavaReplPluginExtension)

        project.afterEvaluate {

            // This plugin requires the Java plugin.  If it has not been applied
            // then add it ...
            if (!project.plugins.hasPlugin(JavaPlugin.class)) {
                // TODO Log a message that the Java plugin is being applied
                project.apply plugin: "java"
            }

            project.task('javarepl', dependsOn: 'testClasses') {

                description = "Runs Java REPL with the classpath defined in the ${project.javarepl.baseConfiguration} configuration"

                if (JavaVersion.current() != VERSION_1_8 && JavaVersion.current() != VERSION_1_9) {
                    throw new GradleException("The javarepl plugin must be run with Java 8 or above")
                }

                final aConfiguration = project.configurations.maybeCreate(JAVAREPL_CONFIGURATION)
                    .extendsFrom(project.configurations.getByName(project.javarepl.baseConfiguration))

                project.dependencies {
                    javarepl "com.javarepl:javarepl:${project.javarepl.version}"
                }

                final aCommand = [
                        'java',
                        'javarepl.Main'
                ]

                final aProcessBuilder = new ProcessBuilder(aCommand)
                    .redirectInput(INHERIT)
                    .redirectOutput(INHERIT)
                    .redirectError(INHERIT)

                // Specify the classpath as an environment variable rather than a command line option
                // to allow for a longer classpath string than default shell command lines support ...
                final aClasspath = aConfiguration.asPath
                aProcessBuilder.environment().put("CLASSPATH", aClasspath)

                final aProcess = aProcessBuilder.start()

                final Integer aTimeout = project.javarepl.timeout
                aTimeout != null ? aProcess.waitFor(aTimeout, SECONDS) : aProcess.waitFor()
                aProcess.destroyForcibly()

            }

        }

    }

}
