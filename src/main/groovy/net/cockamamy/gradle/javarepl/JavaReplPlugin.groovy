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

import javarepl.console.Console
import javarepl.console.ConsoleConfig
import javarepl.console.SimpleConsole
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class JavaReplPlugin implements Plugin<Project> {

    static final String JAVAREPL_CONFIGURATION = "javarepl"

    @Override
    void apply(Project project) {

        // This plugin requires the Java plugin.  If it has not been applied
        // then add it ...
        if (!project.plugins.hasPlugin(JavaPlugin.class)) {
            // TODO Log a message that the Java plugin is being applied
            project.apply plugin: "java"
        }

        project.extensions.create("javarepl", JavaReplPluginExtension)

        project.configurations.maybeCreate(JAVAREPL_CONFIGURATION)
            .extendsFrom(project.configurations.getByName(project.javarepl.baseConfiguration))

        project.dependencies {
            javarepl "com.javarepl:javarepl:${project.javarepl.version}"
        }

        project.tasks.create('javarepl', {
            doLast {

                // TODO Configure history file from plugin parameters
                Console console = new SimpleConsole(ConsoleConfig.consoleConfig())
                console.start()

            }
        })

    }

}
