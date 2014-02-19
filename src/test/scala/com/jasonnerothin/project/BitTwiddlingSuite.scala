/** Copyright [2014] [Jason Nerothin]
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
  */
package com.jasonnerothin.project

import org.scalatest.FunSuite

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/19/14
  * Time: 12:52 PM
  */
class BitTwiddlingSuite extends FunSuite {

  test("offsetSmaller makes numbers smaller") {

    val four = BigInt.int2bigInt(4)
    val two = systemUnderTest.offsetSmaller(four, 1)

    assert(two === 2)

  }

  def systemUnderTest: BitTwiddling = new Object with BitTwiddling

  test("offsetLarger makes numbers larger") {

    val two = 2
    val four = systemUnderTest.offsetLarger(two, 1)

    assert(four === 4)

  }


  test("offsetSmaller requires shifts larger than 0") {

    intercept[IllegalArgumentException] {
      systemUnderTest.offsetLarger(23, 0)
    }

  }

  test("offsetLarger requires shifts larger than 0") {

    intercept[IllegalArgumentException] {
      systemUnderTest.offsetLarger(29292, 0)
    }

  }

  test("lowInt returns the least int from a 96-bit BigInt"){

    val expected = 123455

    val actual = systemUnderTest.lowInt(BigInt.int2bigInt(expected))

    assert( actual === expected )
  }

  test("lowInt does not require 32 bits of data"){
    systemUnderTest.lowInt(BigInt.int2bigInt(2))
  }

  test("middleInt returns the middle Int from a 96-bit BigInt"){

    val expected = 42

    val fortyTwo = BigInt.int2bigInt(expected)
    val andThenSome = fortyTwo << IntWidth

    val result = systemUnderTest.middleInt(andThenSome)

    assert( result === expected )

  }

  test("middleInt requires 64 bits of data"){
    intercept[IllegalArgumentException]{
      systemUnderTest.middleInt(BigInt.int2bigInt(2))
    }
  }

  test("highInt returns the greatest Int form a 96-bit BigInt"){
    val expected = 1
    val one = BigInt.int2bigInt(expected)
    val stillTheOne = systemUnderTest.highInt(one << IntWidth * 2)

    assert( stillTheOne === expected )
  }

  test("highInt requires 96-bits worth of data"){
    intercept[IllegalArgumentException]{
      systemUnderTest.highInt(BigInt.int2bigInt(2))
    }
  }

}
