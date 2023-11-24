package org.example.helper;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.example.config.AppConfig;
import org.example.entities.AppRunEnv;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;


@Data
@Builder
@Slf4j
public class ApplicationConfigurationHelper {
    private String[] environmentArgs;

    public AppConfig loadApplicationConfig() {
        String env = findApplicationEnvironment();
        log.info("Env is {}", env);
        URL configUrl = this.getClass().getResource("/env_config/" + env + ".properties");
        if (Objects.isNull(configUrl)) {
            String message = "There is no" + (env + ".properties") + "names property file in resources directory";
            throw new RuntimeException(message);
        }
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(configUrl.getPath())) {
            prop.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read configs for " + configUrl.getPath());
        }

        return loadApplicationConfig(prop);

    }

    public AppConfig loadApplicationConfig(Properties props) {
        AppConfig appConfig = AppConfig.builder()
                .checkpointInterval(Integer.parseInt(props.getProperty("checkpointInterval", "60000")))
                .checkpointTimeout(Integer.parseInt(props.getProperty("checkpointTimeout", "30000")))
                .minPauseBetweenCheckpoints(Integer.parseInt(props.getProperty("minPauseBetweenCheckpoints", "6000")))
                .maxConcurrentCheckpoints(Integer.parseInt(props.getProperty("maxConcurrentCheckpoints", "1")))
                .checkpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
                .checkpointMode(CheckpointingMode.AT_LEAST_ONCE)
                .appRunningEnv(props.getProperty(AppConstants.APP_ENV))
                .applicationName(props.getProperty("applicationName"))
                .mathPowerInteger(Integer.parseInt(props.getProperty("mathPowerInteger", "2"))).build();


        return appConfig;

    }

    public String findApplicationEnvironment() {
        if (environmentArgs.length == 0)
            throw new RuntimeException("env is not set to load right property file");

        ParameterTool parameterTool = ParameterTool.fromArgs(environmentArgs);
        AppRunEnv appRunEnv = AppRunEnv.valueOf(StringUtils.upperCase(parameterTool.get(AppConstants.APP_ENV, "INVALID")));

        if (AppRunEnv.INVALID == appRunEnv) {
            throw new RuntimeException("There is no property file for environment -" + parameterTool.get(AppConstants.APP_ENV));
        }

        return appRunEnv.name().toLowerCase();
    }
}
