package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record Page(Boolean prev, Boolean next, List<Product> products) {
}
