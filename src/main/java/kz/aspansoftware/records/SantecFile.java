package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;
import kz.jooq.model.tables.records.FileRecord;

import java.time.LocalDateTime;

@Serdeable
public record SantecFile(Long id, Long container, String containerClass, String uuid, String filename, String filepath, LocalDateTime created, LocalDateTime modified, Boolean isRemoved, String description) {
    public static SantecFile toSantecFile(FileRecord r) {
        return new SantecFile(r.getId_(),
                r.getContainer_(),
                r.getContainerClass_(),
                r.getUuid_(),
                r.getFilename_(),
                r.getFilepath_(),
                r.getCreated_(),
                r.getUpdated_(),
                r.getIsRemoved_(),
                r.getDescription_()
        );
    }
}
