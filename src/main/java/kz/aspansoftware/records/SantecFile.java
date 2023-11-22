package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;

@Serdeable
public record SantecFile(Long id, Long container, String containerClass, String uuid, String filename, String filepath, LocalDateTime created, LocalDateTime modified) {
}
