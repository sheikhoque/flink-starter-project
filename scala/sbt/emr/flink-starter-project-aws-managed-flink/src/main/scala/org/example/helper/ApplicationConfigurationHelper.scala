package org.example.helper

import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.java.utils.ParameterTool
import org.slf4j.{Logger, LoggerFactory}

import java.io.{FileInputStream, IOException}
import java.util.{Objects, Properties}

object ApplicationConfigurationHelper {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def getApplicationProperties(args: Array[String]): Properties = {
    val appEnv = findApplicationEnvironment(args)
    val configUrl = this.getClass.getResource("/env_config/" + appEnv + ".properties")
    if (Objects.isNull(configUrl)) {
      val message = "There is no" + (appEnv + ".properties") + "names property file in resources directory"
      throw new RuntimeException(message)
    }
    val prop = new Properties
    try {
      val input = new FileInputStream(configUrl.getPath)
      try prop.load(input)
      catch {
        case ex: IOException =>
          throw new RuntimeException("Unable to read configs for " + configUrl.getPath)
      } finally if (input != null) input.close()
    }
    prop
  }

  def findApplicationEnvironment(environmentArgs: Array[String]): String = {
    if (environmentArgs.length == 0) throw new RuntimeException("env is not set to load right property file")
    val parameterTool = ParameterTool.fromArgs(environmentArgs)
    val appRunEnv = StringUtils.upperCase(parameterTool.get("env", "INVALID"))
    if ("INVALID" eq appRunEnv) throw new RuntimeException("There is no property file for environment")

    appRunEnv.toLowerCase()
  }

}
