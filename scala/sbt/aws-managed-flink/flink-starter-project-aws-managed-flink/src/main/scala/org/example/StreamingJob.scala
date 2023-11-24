package org.example

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.example.connectors.sink.ConsoleSinkFunction
import org.example.connectors.source.RandomNumberSourceFunction
import org.example.helper.ApplicationConfigurationHelper
import org.slf4j.{Logger, LoggerFactory}

object StreamingJob {
  @transient lazy private val logger: Logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = ApplicationConfigurationHelper.getApplicationProperties(env)

    logger.info("loaded properties ... {}", properties)

    val mathPowerVal = Integer.parseInt(properties.getProperty("mathPowerInteger", "2"))

    val randomNumberStream = env.addSource(new RandomNumberSourceFunction).name("random_input_stream").uid("random_input_stream")

    randomNumberStream.map(rns => String.valueOf(Math.pow(rns, mathPowerVal))).name("map_math_power").uid("map_math_power")
      .addSink(new ConsoleSinkFunction).name("console_log_sink").uid("console_log_sink")


    env.execute(properties.getProperty("applicationName"))

  }
}
