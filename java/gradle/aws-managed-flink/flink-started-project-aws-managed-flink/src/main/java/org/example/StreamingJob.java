package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.config.AppConfig;
import org.example.connections.sinks.ConsoleSinkFunction;
import org.example.connections.sources.RandomNumberGeneratorSourceFunction;
import org.example.helper.ApplicationConfigurationHelper;



@Slf4j
public class StreamingJob {
    private static final String PROPERTY_GRP_ID = "StreamingJobAppProperty";
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        ApplicationConfigurationHelper applicationConfigurationHelper = ApplicationConfigurationHelper.builder()
                .propertyGroupId(PROPERTY_GRP_ID).streamExecutionEnvironment(streamExecutionEnvironment).build();

        AppConfig appConfig = applicationConfigurationHelper.loadApplicationConfig();
        log.info("Application configuration has been loaded.. {}", appConfig);
        int mathPower = appConfig.getMathPowerInteger();
        DataStream<Long> integersStream = streamExecutionEnvironment.addSource(RandomNumberGeneratorSourceFunction.builder().build()).name("random_input_stream").uid("random_input_stream");
        integersStream
                .map(i -> String.valueOf(Math.pow(i, mathPower))).name("map_math_power").uid("map_math_power")
                .addSink(ConsoleSinkFunction.builder().build()).name("console_log_sink").uid("console_log_sink");
        streamExecutionEnvironment.execute(appConfig.getApplicationName());
    }

}
