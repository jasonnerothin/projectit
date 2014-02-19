/**
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

package com.jasonnerothin.project

import org.scalatest.FunSuite

/**
  * Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/19/14
  * Time: 2:40 PM
  */
class PointSuite extends FunSuite with BitTwiddling{

  test("(x,y,z) to BigInt implicit conversion in companion object"){
    val point = Point(1,2,3)
    val bi = point.bigInt

    val one = lowInt(bi)
    val two = middleInt(bi)
    val three = highInt(bi)

    assert( one === 1)
    assert( two === 2)
    assert( three === 3)
  }

}
