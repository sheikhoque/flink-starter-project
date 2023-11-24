package org.example.connections.sinks;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.config.AppConfig;


@Builder
@Slf4j
@Data
@Getter
public class SinkFactory {
    private AppConfig appConfig;
}
