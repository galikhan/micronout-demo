package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;
import kz.jooq.model.tables.records.BrandRecord;

@Serdeable
public record Brand(String code, String name) {
    public static Brand to(BrandRecord record) {
        return new Brand(record.getCode_(), record.getName_());
    }
}
