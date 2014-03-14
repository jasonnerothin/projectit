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

package com.jasonnerothin.project.hrmodel

//import org.openspaces.scala.core.aliases.annotation._
import com.gigaspaces.annotation.pojo.{SpaceProperty, SpaceId, SpaceClassConstructor}
import scala.beans.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 2/20/14
 * Time: 10:19 AM
 */
case class Salary @SpaceClassConstructor()
(

  @BeanProperty
  @SpaceProperty(nullValue = "-1")
  @SpaceId
  id: Long = -1,

  @BeanProperty
  @SpaceProperty(nullValue = "-1")
  perAnnum: Double = -1

)
