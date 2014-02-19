package com.jasonnerothin.project

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/18/14
  * Time: 6:43 PM
  *
  * Copyright [2014] [Jason Nerothin]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  *
  */
object Main {

  /**
    * Toy example of short-comings of the current implementation
    * @param args utterly worthless
    */
  def main(args: Array[String]) {

    val projector = new Object with Projection

    val inOneDimension = projector(List(
      Point(1, 2, 3),
      Point(1, 3, 4),
      Point(3, 4, 5)
    ))

    val seq = inOneDimension map {
      case (p: Point) => "(" + p.x +","+ p.y + ","+ p.z + ")"
    }

    println(seq)

  }

}