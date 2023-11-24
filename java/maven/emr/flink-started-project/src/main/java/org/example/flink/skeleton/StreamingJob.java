package org.example.flink.skeleton;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.flink.skeleton.config.AppConfig;
import org.example.flink.skeleton.connections.sinks.ConsoleSinkFunction;
import org.example.flink.skeleton.connections.sources.RandomNumberGeneratorSourceFunction;
import org.example.flink.skeleton.helper.ApplicationConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class StreamingJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        ApplicationConfigurationHelper applicationConfigurationHelper = ApplicationConfigurationHelper.builder()
                .environmentArgs(args).build();

        AppConfig appConfig = applicationConfigurationHelper.loadApplicationConfig();
        log.info("log: Application configuration has been loaded..{}",appConfig);
        int mathPower = appConfig.getMathPowerInteger();
        DataStream<Long> integersStream = streamExecutionEnvironment.addSource(RandomNumberGeneratorSourceFunction.builder().build()).name("input_stream").uid("input_stream");
        integersStream
                .map(i -> String.valueOf(Math.pow(i, mathPower))).name("map_math_power").uid("map_math_power")
                .addSink(ConsoleSinkFunction.builder().build()).name("console_log_sink").uid("console_log_sink");
        streamExecutionEnvironment.execute(appConfig.getApplicationName());
    }

}
