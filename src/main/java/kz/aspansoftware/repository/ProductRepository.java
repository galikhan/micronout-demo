package kz.aspansoftware.repository;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.Product;
import org.jooq.DSLContext;

import java.util.List;
import java.util.stream.Collectors;

import static kz.jooq.model.tables.Product.PRODUCT;
import static org.jooq.Records.mapping;

@Singleton
public class ProductRepository {

    private final DSLContext dsl;

    ProductRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Product create(Product product) {
        return this.dsl
                .insertInto(PRODUCT)
                .set(PRODUCT.NAME_, product.name())
                .set(PRODUCT.DESCRIPTION_, product.description())
                .returningResult(PRODUCT.ID_, PRODUCT.NAME_, PRODUCT.DESCRIPTION_)
                .fetchOne(mapping(Product::new));
    }

//    public Optional<User> getUserByEmail(String email) {
//        return this.dsl
//                .select(USERS.ID, USERS.NAME, USERS.EMAIL)
//                .from(USERS)
//                .where(USERS.EMAIL.equalIgnoreCase(email))
//                .fetchOptional(mapping(User::new));
//    }

//    public void deleteUser(Long id) {
//        dsl.deleteFrom(USERS).where(USERS.ID.eq(id)).execute();
//    }
//
    public List<Product> findAll() {
        return this.dsl.selectFrom(PRODUCT)
                .fetch().stream().map(Product::toProduct)
                .collect(Collectors.toList());
    }
}
