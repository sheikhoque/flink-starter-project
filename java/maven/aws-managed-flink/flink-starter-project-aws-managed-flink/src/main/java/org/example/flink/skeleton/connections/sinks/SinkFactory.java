package org.example.flink.skeleton.connections.sinks;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.flink.skeleton.config.AppConfig;

@Builder
@Slf4j
@Data
@Getter
public class SinkFactory {
    private AppConfig appConfig;
}
