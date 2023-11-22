package kz.aspansoftware.repository;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.Product;
import kz.aspansoftware.records.SantecFile;
import org.jooq.DSLContext;

import java.time.LocalDateTime;

import static kz.jooq.model.tables.File.FILE;
import static kz.jooq.model.tables.Product.PRODUCT;
import static org.jooq.Records.mapping;

@Singleton
public class FileRepository {

    private final DSLContext dsl;

    public FileRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public SantecFile create(SantecFile file) {
        return this.dsl
                .insertInto(FILE)
                .set(FILE.CONTAINER_, file.container())
                .set(FILE.CONTAINER_CLASS_, file.containerClass())
                .set(FILE.FILENAME_, file.filename())
                .set(FILE.FILEPATH_, file.filepath())
                .set(FILE.UUID_, file.uuid())
                .set(FILE.CREATED_, LocalDateTime.now())
                .returning()
                .fetchOne(mapping(SantecFile::new));
    }

}
