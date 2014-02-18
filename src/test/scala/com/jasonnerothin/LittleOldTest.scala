package com.jasonnerothin

import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory

/**
  * Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/18/14
  * Time: 2:56 AM
  *
  *
  */
class LittleOldTest extends FunSuite {
//with MockFactory {

  test("that gradle will run my test successfully"){

    assert(1+1 === 2)
  }

}
