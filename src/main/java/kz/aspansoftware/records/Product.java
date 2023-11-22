package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;
import kz.jooq.model.tables.records.ProductRecord;

@Serdeable
public record Product(Long id, String name, String description) {
    public static Product toProduct(ProductRecord record) {
        return new Product(record.getId_(), record.getName_(), record.getDescription_());
    }
}
