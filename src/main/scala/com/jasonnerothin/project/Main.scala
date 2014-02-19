package com.jasonnerothin.project

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 2/18/14
 * Time: 6:43 PM
 * Provides...
 */
object Main {

  def main(args: Array[String]){

    val projector = new Object with Projection
    val inOneDimension = projector(List(Point(1,2,3),Point(2,3,4),Point(3,4,5)))

    inOneDimension map{
      case(p:Point) => println(p.asInt)
    }

  }

}
