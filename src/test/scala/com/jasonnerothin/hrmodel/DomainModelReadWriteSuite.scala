/*
 * Copyright [2014] [Jason Nerothin]
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.jasonnerothin.hrmodel

import com.jasonnerothin.GsI10nSuite
import com.jasonnerothin.project.hrmodel.Salary
import org.scalatest.{Assertions, ConfigMap}

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/27/14
  * Time: 3:20 PM
  */
class DomainModelReadWriteSuite extends GsI10nSuite {

  override def beforeEach(configMap: ConfigMap): Unit = {
    assume(spaceContents() == 0)
  }

  test("read write Employee")(pending)

  test("read write JobDescription")(pending)

  test("read write Salary") {

    val salary = Salary(32, 100000)
    val lc = gigaSpace.write(salary)

    assert(lc != null)

  }

}
