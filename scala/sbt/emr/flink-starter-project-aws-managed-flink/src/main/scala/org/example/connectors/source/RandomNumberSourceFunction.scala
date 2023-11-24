package org.example.connectors.source

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

class RandomNumberSourceFunction extends RichSourceFunction[Long] {
  override def run(sourceContext: SourceFunction.SourceContext[Long]): Unit =
    while (true) {
      sourceContext.collect(Math.round(Math.random * 100))
      Thread.sleep(2000)
    }

  override def cancel(): Unit = ???
}
