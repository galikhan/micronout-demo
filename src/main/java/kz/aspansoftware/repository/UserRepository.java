package kz.aspansoftware.repository;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.User;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.jooq.model.tables.Users.USERS;
import static org.jooq.Records.mapping;

@Singleton
public class UserRepository {

    private final DSLContext dsl;

    UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public User createUser(User user) {
        return this.dsl
                .insertInto(USERS)
                .set(USERS.NAME, user.name())
                .set(USERS.EMAIL, user.email())
                .set(USERS.CREATED_AT, LocalDateTime.now())
                .returningResult(USERS.ID, USERS.NAME, USERS.EMAIL)
                .fetchOne(mapping(User::new));
    }

    public Optional<User> getUserByEmail(String email) {
        return this.dsl
                .select(USERS.ID, USERS.NAME, USERS.EMAIL)
                .from(USERS)
                .where(USERS.EMAIL.equalIgnoreCase(email))
                .fetchOptional(mapping(User::new));
    }

    public void deleteUser(Long id) {
        dsl.deleteFrom(USERS).where(USERS.ID.eq(id)).execute();
    }

    public List<User> findAll() {
        return this.dsl.selectFrom(USERS)
                .fetch().stream().map(User::toUser)
                .collect(Collectors.toList());
    }
}
