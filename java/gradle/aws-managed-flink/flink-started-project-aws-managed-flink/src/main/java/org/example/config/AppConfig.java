package org.example.config;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;

@Data
@Builder
@Getter
public class AppConfig {
    private int checkpointInterval;
    private int checkpointTimeout;
    private int minPauseBetweenCheckpoints;
    private int maxConcurrentCheckpoints;
    private CheckpointConfig.ExternalizedCheckpointCleanup checkpointCleanup;
    private CheckpointingMode checkpointMode;
    private String applicationName;
    private int mathPowerInteger;
}
