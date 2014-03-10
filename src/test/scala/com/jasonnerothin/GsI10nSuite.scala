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
import org.openspaces.core.cluster.ClusterInfo
import org.openspaces.pu.container.{ProcessingUnitContainerProvider, ProcessingUnitContainer}
import org.openspaces.core.space.UrlSpaceConfigurer
import org.openspaces.core.space.cache.{LocalViewSpaceConfigurer, LocalCacheSpaceConfigurer}
import com.j_spaces.core.client.SQLQuery
import org.openspaces.pu.container.integrated.IntegratedProcessingUnitContainerProvider

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

  private val defaults = Map[String, Any](
    schemaProperty -> "partitioned-sync2backup"
    , numInstancesProperty -> int2Integer(2)
    , numBackupsProperty -> int2Integer(1)
    , instanceIdProperty -> int2Integer(1)
    , spaceUrlProperty -> "/./space"
    , spaceModeProperty -> SpaceMode.Embedded
    , configLocationProperty -> "classpath*:/com/jasonnerothin/GsGridI10nSuite.xml"
    , localViewQueryListProperty -> List[SQLQuery[_]]()
  )

  /**
   * Test instances. The purpose of this class is to initialize these members
   */
  protected var containerProvider: ProcessingUnitContainerProvider = null
  protected var container: ProcessingUnitContainer = null
  protected var gigaSpace: GigaSpace = null

  object SpaceMode extends Enumeration {
    type SpaceMode = Value
    val Embedded, Remote, LocalCache, LocalView = Value
  }

  import SpaceMode._

  private def getDefault[T](name: String): T = {
    defaults.get(name).get.asInstanceOf[T]
  }

  /* convenience methods */

  protected def spaceContents(): Int = {
    assume( gigaSpace != null )
    gigaSpace.count(new Object())
  }

  /* Default setup/tear-down behaviors */

  override def beforeAll(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {

    containerProvider = createProvider(configMap)
    container = createContainer(configMap)
    gigaSpace = createGigaSpace(configMap)

  }

  override def afterAll(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {
    container.close()
  }

  /* i10n infrastructure setup methods */

  private def createClusterInfo(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): ClusterInfo = {

    val schema = configMap.getOrElse(schemaProperty, getDefault(schemaProperty))
    val numInstances = configMap.getOrElse(numInstancesProperty, getDefault(numInstancesProperty))
    val numBackups = configMap.getOrElse(numBackupsProperty, getDefault(numBackupsProperty))
    val instanceId = configMap.getOrElse(instanceIdProperty, getDefault(instanceIdProperty))

    val clusterInfo = new ClusterInfo
    clusterInfo.setSchema(schema.asInstanceOf[String])
    clusterInfo.setNumberOfInstances(numInstances.asInstanceOf[Integer])
    clusterInfo.setNumberOfBackups(numBackups.asInstanceOf[Integer])
    clusterInfo.setInstanceId(instanceId.asInstanceOf[Integer])
    clusterInfo

  }

  private def createGigaSpace(configMap: ConfigMap): GigaSpace = {

    def makeGs(configurer: UrlSpaceConfigurer): GigaSpace = {
      new GigaSpaceConfigurer(configurer).gigaSpace()
    }

    val spaceUrl = configMap.getOrElse(spaceUrlProperty, getDefault(spaceUrlProperty))
    val configurer = new UrlSpaceConfigurer(spaceUrl.asInstanceOf[String])

    configMap.getOrElse(spaceModeProperty, getDefault(spaceModeProperty)) match {
      case Embedded =>
        makeGs(configurer)
      case Remote =>
        makeGs(configurer)
      case LocalCache =>
        new GigaSpaceConfigurer(new LocalCacheSpaceConfigurer(configurer)).gigaSpace()
      case LocalView =>
        val queries = configMap.getOrElse(localViewQueryListProperty, getDefault(localViewQueryListProperty)).asInstanceOf[List[SQLQuery[_]]]
        val viewConfigurer = new LocalViewSpaceConfigurer(configurer)
        queries.foreach(qry => {
          viewConfigurer.addViewQuery(qry)
        })
        new GigaSpaceConfigurer(viewConfigurer).gigaSpace()
    }

  }

  private def createProvider(configMap: ConfigMap): ProcessingUnitContainerProvider = {
    val containerProvider = new IntegratedProcessingUnitContainerProvider
    containerProvider.setClusterInfo(createClusterInfo(configMap))
    containerProvider.addConfigLocation(configMap.getOrElse(configLocationProperty, getDefault(configLocationProperty)).asInstanceOf[String])
    containerProvider
  }

  private def createContainer(configMap: ConfigMap): ProcessingUnitContainer = {
    containerProvider.createContainer()
  }

}