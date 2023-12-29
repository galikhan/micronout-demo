package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;
import kz.jooq.model.tables.records.CategoryRecord;
import org.jooq.Record;

import java.time.LocalDateTime;

@Serdeable
public record Category(Long id, Long parent, String name, String description, Boolean isRemoved, Integer level, Long image) {

    public static Category toCategory(CategoryRecord record) {
        return new Category(record.getId_(), record.getParent_(),
                record.getName_(), record.getDescription_(),
                record.getIsRemoved_(),
                record.getLevel_(),
                record.getImage_()
        );
    }
}
