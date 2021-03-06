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

/** A projection function that would work for our implementation, but won't be
  * very memory efficient.
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
    Point(p.bigInt)
  }

  /** A function to get us from 1-dimension back out to 3-dimensions.
    * Note that this method should also be idempotent:
    * inverse(x) = inverse(inverse(x)). It should also be one-to-one
    * and onto.
    * @param p a point on the x-axis
    * @return a point in 3-dimensions
    */
  def inverse(p: Point): Point = {
    Point(p.bigInt)
  }

}

/** A simple class representing a point in 3-D space.
  */
case class Point(bigInt: BigInt) extends BitTwiddling{

  def x = lowInt(bigInt)

  def y = middleInt(bigInt)

  def z = highInt(bigInt)

}

object Point {

  import scala.language.implicitConversions

  val tooBig = 2^IntWidth + 1

  implicit def xyzToBigInt(arr: Array[Int]) : BigInt = {
    require(arr.length == 3)
    val x = arr(0)
    val y = arr(1)
    val z = arr(2)
    require( x < tooBig && y < tooBig && z < tooBig )
    BigInt.int2bigInt(x) | BigInt.int2bigInt(y) << IntWidth | BigInt.int2bigInt(z) << IntWidth
  }

  def apply(x:Int, y:Int, z:Int) : Point = {
    new Point(Array(x,y,z))
  }

}