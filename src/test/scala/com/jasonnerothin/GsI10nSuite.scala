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
import com.gigaspaces.cluster.activeelection.SpaceMode
import org.openspaces.core.space.cache.LocalCacheSpaceConfigurer

/** Created by IntelliJ IDEA.
  * User: jason
  * Date: 2/27/14
  * Time: 3:25 PM
  *
  * An abstract test suite that can be used to instrument scala tests that start up a
  * new container in standalone mode and then create a [[GigaSpace]] reference into it.
  */
abstract class GsI10nSuite extends FunSuite with BeforeAndAfterAllConfigMap with BeforeAndAfterEach {

  val defaultConfigLocation = "classpath*:/com/jasonnerothin/GsGridI10nSuite.xml"

  // cluster info members
  val defaultSchema = "partitioned-sync2backup"
  val defaultInstances = int2Integer(2)
  val defaultBackups = int2Integer(1)
  val defaultInstanceId = int2Integer(1)

  // space info
  val defaultSpaceUrl = "/./space"
  val defaultSpaceMode = SpaceMode.Embedded

  var container: ProcessingUnitContainer = null
  var gigaSpace: GigaSpace = null

  object SpaceMode extends Enumeration{
    type SpaceMode = Value
    val Embedded, Remote, LocalCache, LocalView = Value
  }
  import SpaceMode._

  override def beforeAll(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {

    val containerProvider = new StandaloneProcessingUnitContainerProvider("") // TODO
    containerProvider.setClusterInfo(clusterInfo(configMap))
    containerProvider.addConfigLocation(configMap.getOrElse[String]("configLocation", defaultConfigLocation))

    container = containerProvider.createContainer()

    val spaceUrl = configMap.getOrElse[String]("spaceUrl", defaultSpaceUrl)
    val configurer = new UrlSpaceConfigurer(spaceUrl)
    gigaSpace = configMap.getOrElse[SpaceMode.Value]("spaceMode", defaultSpaceMode) match{
      case Embedded =>
        default(configurer)
      case Remote =>
        default(configurer)
      case LocalCache =>
        new GigaSpaceConfigurer(new LocalCacheSpaceConfigurer(configurer)).gigaSpace()
      case LocalView =>
        throw new UnsupportedOperationException("Not supported yet.")
        // TODO figure out how to pass all the SQLQuery stuff through the configMap
    }

    def default(configurer: UrlSpaceConfigurer) : GigaSpace = {
      new GigaSpaceConfigurer(configurer).gigaSpace()
    }

  }

  override def beforeEach(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {
    gigaSpace.clear(new Object())
  }

  override def afterAll(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): Unit = {
    container.close()
  }

  def clusterInfo(configMap: ConfigMap = new ConfigMap(Map[String, Any]())): ClusterInfo = {

    val schema = configMap.getOrElse[String]("schema", defaultSchema)
    val numInstances = configMap.getOrElse[Integer]("numInstances", defaultInstances)
    val numBackups = configMap.getOrElse[Integer]("numBackups", defaultInstances)
    val instanceId = configMap.getOrElse[Integer]("instanceId", defaultInstanceId)

    val clusterInfo = new ClusterInfo
    clusterInfo.setSchema(schema)
    clusterInfo.setNumberOfInstances(numInstances)
    clusterInfo.setNumberOfBackups(numBackups)
    clusterInfo.setInstanceId(instanceId)
    clusterInfo

  }

}