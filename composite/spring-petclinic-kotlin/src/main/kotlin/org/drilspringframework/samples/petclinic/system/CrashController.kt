/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drilspringframework.samples.petclinic.system


import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * @author Antoine Rey
 * <p/>
 * Also see how a view that resolves to "5xx" has been added ("5xx.html").
 */
@Controller
class CrashController {

    @GetMapping("/oups")
    fun triggerException() {
        asd()
    }

    private fun asd() {
        var q: Int = 10123
        var s: String = "sadasd"
        try {
            throw RuntimeException("Expected: controller used to showcase what happens when an exception is thrown")
        } catch (ex: Exception) {

            throw RuntimeException("xx")
        }
    }

}
