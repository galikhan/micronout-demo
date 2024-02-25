package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;
import kz.jooq.model.tables.records.ProductRecord;

import java.util.List;

@Serdeable
public record Product(Long id, String name, String description, Long category, String video, Boolean isNew, Boolean isRemoved, Boolean isSantec, String brand) {
    public static Product toProduct(ProductRecord record) {
        return new Product(record.getId_(),
                record.getName_(),
                record.getDescription_(),
                record.getCategory_(),
                record.getVideo_(),
                record.getIsNew_(),
                record.getIsRemoved_(),
                record.getIsSantec_(),
                record.getBrand_());
    }

}
