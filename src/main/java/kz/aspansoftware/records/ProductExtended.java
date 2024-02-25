package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;
import org.jooq.Record;

@Serdeable
public record ProductExtended(Long id, String name, String description, Long category, String video, Boolean isNew,
                              Boolean isRemoved, Boolean isSantec, String brand, String filename) {
    public static ProductExtended  toProduct(Record record) {
        return new ProductExtended(record.getValue("id_", Long.class),
                record.getValue("name_", String.class),
                record.getValue("description_", String.class),
                record.getValue("category_", Long.class),
                record.getValue("video_", String.class),
                record.getValue("is_new_", Boolean.class),
                record.getValue("is_removed_", Boolean.class),
                record.getValue("is_santec_", Boolean.class),
                record.getValue("brand_", String.class),
                record.getValue("filename_", String.class)
                );
    }
}
