package com.jasonnerothin.project

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/18/14
  * Time: 11:12 AM
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

/** A hypothetical projection function. Incorrect implementation since it is
  * not invertible. Since we can't get y and z out of the co-domain of the apply
  * function, any reasonable implementation based upon this definition would be lossy.
  */
trait Projection {

  /** Turns a bunch of points in 3-D into an co-domain of
    * points on the x-axis. This method should be one-to-one, onto
    * and idempotent.
    * @param points some points
    * @return some points on the x-axis
    */
  def apply(points: Seq[Point]): Seq[Point] = {
    points map {
      case pnt: Point => function(pnt)
    }
  }

  /** Turns a bunch of points on the x-axis into a bunch of points
    * in 3-D by returning the domain of the apply function. In order to meet
    * our larger implementation goals, this method should be one-to-one, onto
    * and idempotent.
    * @param points a corresponding bunch of points in 3-dimensions
    * @return the projection operation's domain
    */
  def unapply(points: Seq[Point]): Seq[Point] = {
    points map {
      case pnt: Point => inverse(pnt)
    }
  }

  /** Maps from a Point defined in 3-dimensions to a point on
    * the x-axis. Note that this method is idempotent (can be
    * repeated against its own output and result in the same
    * output), i.e. function(x) = function(function(x)). It should also
    * be one-to-one and onto.
    * @param p a point with no y,z components
    * @return a point
    */
  def function(p: Point): Point = {
    Point(p.x, 0, 0)
  }

  /** A function to get us from 1-dimension back out to 3-dimensions.
    * Note that this method should also be idempotent:
    * inverse(x) = inverse(inverse(x)). It should also be one-to-one
    * and onto.
    * @param p a point on the x-axis
    * @return a point in 3-dimensions
    */
  def inverse(p: Point): Point = {
    Point(p.x, 1, 1)
    //    throw new UnsupportedOperationException("This implementation is lossy. Undefined.")
  }

}

/** A simple case class representing a point in 3-D space.
  * @param x x-component
  * @param y y-component
  * @param z z-component
  */
case class Point(x: Int, y: Int, z: Int) {
  def asInt: Int = x
}