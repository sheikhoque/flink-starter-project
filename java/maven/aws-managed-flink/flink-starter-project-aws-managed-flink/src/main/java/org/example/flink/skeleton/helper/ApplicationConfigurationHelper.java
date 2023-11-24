package org.example.flink.skeleton.helper;

import com.amazonaws.services.kinesisanalytics.runtime.KinesisAnalyticsRuntime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.flink.skeleton.config.AppConfig;
import org.example.flink.skeleton.entities.AppRunEnv;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


@Data
@Builder
@Slf4j
public class ApplicationConfigurationHelper {
    private final String propertyGroupId;
    private final StreamExecutionEnvironment streamExecutionEnvironment;

    public AppConfig loadApplicationConfig() throws IOException {
        Map<String, Properties> applicationProperties;
        if (streamExecutionEnvironment instanceof LocalStreamEnvironment) {
            applicationProperties = KinesisAnalyticsRuntime.getApplicationProperties(ApplicationConfigurationHelper.class.getClassLoader().getResource("env_config/app_properties.json").getPath());
            streamExecutionEnvironment.setParallelism(1);
        } else {
            applicationProperties = KinesisAnalyticsRuntime.getApplicationProperties();
        }

        Properties props = applicationProperties.get(propertyGroupId);
        if (props == null || props.size() <= 0) {
            throw new IllegalArgumentException("No such property group found or group have no properties");
        }

        return loadApplicationConfig(props);

    }

    public AppConfig loadApplicationConfig(Properties props) {
        AppConfig appConfig = AppConfig.builder()
                .checkpointInterval(Integer.parseInt(props.getProperty("checkpointInterval", "60000")))
                .checkpointTimeout(Integer.parseInt(props.getProperty("checkpointTimeout", "30000")))
                .minPauseBetweenCheckpoints(Integer.parseInt(props.getProperty("minPauseBetweenCheckpoints", "6000")))
                .maxConcurrentCheckpoints(Integer.parseInt(props.getProperty("maxConcurrentCheckpoints", "1")))
                .checkpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
                .checkpointMode(CheckpointingMode.AT_LEAST_ONCE)
                .applicationName(props.getProperty("applicationName"))
                .mathPowerInteger(Integer.parseInt(props.getProperty("mathPowerInteger", "2"))).build();

        return appConfig;

    }

}
