package org.example.flink.skeleton.connections.sources;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

@Builder
@Slf4j
public class RandomNumberGeneratorSourceFunction extends RichSourceFunction<Long> {
    @Override
    public void run(SourceContext<Long> sourceContext) throws Exception {
        while(true) {
            sourceContext.collect(Math.round(Math.random()*100));
            Thread.sleep(2000);
        }
    }

    @Override
    public void cancel() {

    }

}
