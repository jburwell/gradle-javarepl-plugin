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

class JavaReplPluginExtension {

    List<String> configurations = ["testRuntime"]
    String dependsOn = "testClasses"
    Integer heapSize = null
    Integer stackSize = null
    Integer timeout = null
    String version = "428"

    void validate() {

        if (!configurations) {
            throw new GradleException("The JavaREPL plugin requires the specification of one or more configurations")
        }

        if (!dependsOn) {
            throw new GradleException("The Java REPL plugin requires the specification of dependent task.")
        }

        if (!version) {
            throw new GradleException("The Java REPL plugin requires the specification of a Java REPL version.")
        }

    }

}
