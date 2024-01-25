package kz.aspansoftware.repository;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.SantecFile;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static kz.jooq.model.tables.File.FILE;
import static org.jooq.Records.mapping;

@Singleton
public class FileRepository {

    private final DSLContext dsl;

    public FileRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public SantecFile create(Long container, String contClass, String filename, String filePath) {
        return this.dsl
                .insertInto(FILE)
                .set(FILE.CONTAINER_, container)
                .set(FILE.CONTAINER_CLASS_, contClass)
                .set(FILE.FILENAME_, filename)
                .set(FILE.FILEPATH_, filePath)
                .set(FILE.UUID_, UUID.randomUUID().toString())
                .set(FILE.CREATED_, LocalDateTime.now())
                .returning()
                .fetchOne(mapping(SantecFile::new));
    }

    public List<SantecFile> findByContainer(Long container) {
        return this.dsl

                .selectFrom(FILE)
                .where(FILE.CONTAINER_.eq(container))
                .fetch().stream().map(SantecFile::toSantecFile)
                .collect(Collectors.toList());
    }

    public List<SantecFile> findByContainerAndClass(Long container, String containerClass) {
        return this.dsl
                .selectFrom(FILE)
                .where(FILE.CONTAINER_.eq(container).and(FILE.CONTAINER_CLASS_.eq(containerClass)))
                .fetch().stream().map(SantecFile::toSantecFile)
                .collect(Collectors.toList());
    }

    public int delete(Long id) {
        return this.dsl.update(FILE)
                .set(FILE.IS_REMOVED_, true)
                .where(FILE.ID_.eq(id))
                .execute();
    }

    public SantecFile findById(Long id) {
        return this.dsl
                .selectFrom(FILE)
                .where(FILE.ID_.eq(id))
                .fetchOne(SantecFile::toSantecFile);
    }

    public int updateDocumentDescription(Long id, String description) {
        return this.dsl.update(FILE)
                .set(FILE.DESCRIPTION_, description)
                .where(FILE.ID_.eq(id))
                .execute();
    }
}
