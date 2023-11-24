package org.example.connectors.sink

import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.slf4j.{Logger, LoggerFactory}

class ConsoleSinkFunction extends RichSinkFunction[String] {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  @throws[Exception]
  override def invoke(value: String, context: SinkFunction.Context): Unit = {
    logger.info("output: {}", value)
  }
}
