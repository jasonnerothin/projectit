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
import org.scalatest.ConfigMap
import com.sun.servicetag.SystemEnvironment
import scala.sys.SystemProperties
import java.util.NoSuchElementException

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/27/14
  * Time: 3:20 PM
  */
class DomainModelReadWriteSuite extends GsI10nSuite {

  override def beforeEach(configMap: ConfigMap): Unit = {
    assume(spaceContents() == 0)
  }

  ignore("negative control") {
    assert(1 + 1 + 1 === 2)
  }

  test("positive control") {
    assert(1 + 1 === 2)
  }

  test("properties can be read from the gradle test file") {

    var propName = "HOME"
    system(propName)
    env(propName)

    propName = "com.gs.protectiveMode.typeWithoutId"
    system(propName)
    env(propName)


  }

  private def env(propName: String): Unit = {
    try {
      println(String.format("Environment prop name: [%s], prop value [%s]", propName, sys.env(propName)))
    } catch {
      case e: NoSuchElementException => println(String.format("Environment prop NOT FOUND: [%s]", propName))
      case e: Throwable => fail(e)
    }
  }

  private def system(propName: String): Unit = {
    val props = new SystemProperties
    println(String.format("System prop name: [%s], prop value [%s]", propName, props.get(propName)))
  }

  test("read write Employee")(pending)

  test("read write JobDescription")(pending)

  test("read write Salary") {

    val salary = Salary(32, 100000)
    val lc = gigaSpace.write(salary)

    assert(lc != null)

  }

}
