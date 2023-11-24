package org.example.helper

import com.amazonaws.services.kinesisanalytics.runtime.KinesisAnalyticsRuntime
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.slf4j.{Logger, LoggerFactory}

import java.util
import java.util.Properties

object ApplicationConfigurationHelper {
  private val logger: Logger = LoggerFactory.getLogger(getClass)
  private val PROPERTY_GRP_ID = "StreamingJobAppProperty"

  def getApplicationProperties(streamExecutionEnvironment: StreamExecutionEnvironment): Properties = {
    var appProperties: util.Map[String, Properties] = null
    if (streamExecutionEnvironment.getJavaEnv.isInstanceOf[LocalStreamEnvironment]) {
      appProperties = KinesisAnalyticsRuntime.getApplicationProperties(getClass.getClassLoader.getResource("env_config/app_properties.json").getPath)
      streamExecutionEnvironment.setParallelism(1)
    } else {
      appProperties = KinesisAnalyticsRuntime.getApplicationProperties
    }

    appProperties.get(PROPERTY_GRP_ID)

  }

}
