package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;
import kz.jooq.model.tables.records.ProductSizeRecord;

@Serdeable
public record ProductSize(Long id, Long product, String article, String size, boolean isRemoved) {
    public static ProductSize toProduct(ProductSizeRecord record) {
        return new ProductSize(record.getId_(),
                record.getProduct_(),
                record.getArticle_(),
                record.getSize_(),
                record.getIsRemoved_());
    }
}
