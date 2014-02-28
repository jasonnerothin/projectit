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

package com.jasonnerothin

import org.scalatest._
import org.openspaces.core.{GigaSpaceConfigurer, GigaSpace}
import org.openspaces.pu.container.standalone.StandaloneProcessingUnitContainerProvider
import org.openspaces.core.cluster.ClusterInfo
import org.openspaces.pu.container.ProcessingUnitContainer
import org.openspaces.core.space.UrlSpaceConfigurer
import org.openspaces.core.space.cache.{LocalViewSpaceConfigurer, LocalCacheSpaceConfigurer}
import com.j_spaces.core.client.SQLQuery

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/27/14
  * Time: 3:25 PM
  *
  * An abstract test suite that can be used to instrument scala tests that start up a
  * new container in standalone mode and then create a [[GigaSpace]] reference into it.
  */
abstract class GsI10nSuite extends FunSuite with BeforeAndAfterAllConfigMap with BeforeAndAfterEach {

  val schemaProperty = "schema"
  val spaceUrlProperty = "spaceUrl"
  val numInstancesProperty = "numInstances"
  val numBackupsProperty = "numBackups"
  val instanceIdProperty = "instanceId"
  val spaceModeProperty = "spaceMode"
  val configLocationProperty = "configLocation"
  val localViewQueryListProperty = "localViewQueryList"

  val puJarPathProperty = "puJarPath"

  val defaults = Map[String, Any](
    schemaProperty -> "partitioned-sync2backup"
    , numInstancesProperty -> int2Integer(2)
    , numBackupsProperty -> int2Integer(1)
    , instanceIdProperty -> int2Integer(1)
    , spaceUrlProperty -> "/./space"
    , spaceModeProperty -> SpaceMode.Embedded
    , configLocationProperty -> "classpath*:/com/jasonnerothin/GsGridI10nSuite.xml"
    , puJarPathProperty -> "" // TODO needs to be built by gradle before this test runs...
    , localViewQueryListProperty -> List[SQLQuery[_]]()
  )

  object SpaceMode extends Enumeration {
    type SpaceMode = Value
    val Embedded, Remote, LocalCache, LocalView = Value
  }

  import SpaceMode._

  def getDefault[T](name:String): T = {
    defaults.get(name).get.asInstanceOf[T]
  }

  var container: ProcessingUnitContainer = null
  var gigaSpace: GigaSpace = null

  override def beforeAll(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {

    container = createContainer(configMap)
    gigaSpace = createGigaSpace(configMap)

  }

  def createGigaSpace(configMap: ConfigMap): GigaSpace = {

    def makeGs(configurer: UrlSpaceConfigurer): GigaSpace = {
      new GigaSpaceConfigurer(configurer).gigaSpace()
    }

    val spaceUrl = configMap.getOrElse[String](spaceUrlProperty, getDefault(spaceUrlProperty))
    val configurer = new UrlSpaceConfigurer(spaceUrl)

    configMap.getOrElse[SpaceMode.Value](spaceModeProperty, getDefault(spaceModeProperty)) match {
      case Embedded =>
        makeGs(configurer)
      case Remote =>
        makeGs(configurer)
      case LocalCache =>
        new GigaSpaceConfigurer(new LocalCacheSpaceConfigurer(configurer)).gigaSpace()
      case LocalView =>
        val queries = configMap.getOrElse[List[SQLQuery[_]]](localViewQueryListProperty, getDefault(localViewQueryListProperty))
        val viewConfigurer = new LocalViewSpaceConfigurer(configurer)
        queries.foreach(qry => {
          viewConfigurer.addViewQuery(qry)
        })
        new GigaSpaceConfigurer(viewConfigurer).gigaSpace()
    }

  }

  def createContainer(configMap: ConfigMap): ProcessingUnitContainer = {
    val containerProvider = new StandaloneProcessingUnitContainerProvider(configMap.getOrElse[String](puJarPathProperty, getDefault(puJarPathProperty)))
    containerProvider.setClusterInfo(clusterInfo(configMap))
    containerProvider.addConfigLocation(configMap.getOrElse[String](configLocationProperty, getDefault(configLocationProperty)))

    containerProvider.createContainer()
  }

  override def beforeEach(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {
    gigaSpace.clear(new Object())
  }

  override def afterAll(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {
    container.close()
  }

  def clusterInfo(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): ClusterInfo = {

    val schema = configMap.getOrElse[String](schemaProperty, getDefault(schemaProperty))
    val numInstances = configMap.getOrElse[Integer](numInstancesProperty, getDefault(numInstancesProperty))
    val numBackups = configMap.getOrElse[Integer](numBackupsProperty, getDefault(numBackupsProperty))
    val instanceId = configMap.getOrElse[Integer](instanceIdProperty, getDefault(instanceIdProperty))

    val clusterInfo = new ClusterInfo
    clusterInfo.setSchema(schema)
    clusterInfo.setNumberOfInstances(numInstances)
    clusterInfo.setNumberOfBackups(numBackups)
    clusterInfo.setInstanceId(instanceId)
    clusterInfo

  }

}