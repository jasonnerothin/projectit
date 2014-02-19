package com.jasonnerothin.project

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 2/19/14
 * Time: 12:36 PM
 * Provides bit manipulation operations
 */
trait BitTwiddling {

  def offsetLarger(num: Int, shift: Int) : BigInt = {
    require (shift > 0 )
    BigInt.int2bigInt(num) << shift
  }

  def offsetSmaller(num: BigInt, shift: Int) : Int = {
    require( shift > 0 )
    (num >> shift).toInt
  }

  def lowInt(bigInt: BigInt) : Int = {
    require( bigInt.bitLength > 0)
    bigInt.toInt
  }

  def middleInt(bigInt: BigInt) : Int = {
    require( bigInt.bitLength > 32 )
    (bigInt >> 32).toInt
  }

  def highInt(bigInt: BigInt) : Int = {
    require( bigInt.bitLength > 64 )
    (bigInt >> 64).toInt
  }

}