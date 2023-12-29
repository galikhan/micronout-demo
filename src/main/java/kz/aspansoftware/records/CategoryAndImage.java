package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CategoryAndImage(Category category, SantecFile image) {
}
