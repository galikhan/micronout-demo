package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;
import kz.jooq.model.tables.records.UsersRecord;

@Serdeable
public record User(Long id,
                   String name,
                   String email) {

    public static User toUser(UsersRecord record) {
        return new User(record.getId(), record.getName(), record.getEmail());
    }
}
