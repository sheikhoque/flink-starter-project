package org.example.flink.skeleton.connections.sinks;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Builder
@Slf4j
public class ConsoleSinkFunction extends RichSinkFunction<String> {
    @Override
    public void invoke(String value, Context context) throws Exception {
        log.info("output:{}", value);
    }
}
