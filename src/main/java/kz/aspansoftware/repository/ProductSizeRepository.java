package kz.aspansoftware.repository;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.ProductSize;
import org.jooq.DSLContext;

import java.util.List;
import java.util.stream.Collectors;

import static kz.jooq.model.tables.ProductSize.PRODUCT_SIZE;
import static org.jooq.Records.mapping;

@Singleton
public class ProductSizeRepository {

    private DSLContext dsl;

    public ProductSizeRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public ProductSize create(ProductSize productSize) {
        return this.dsl
                .insertInto(PRODUCT_SIZE)
                .set(PRODUCT_SIZE.PRODUCT_, productSize.product())
                .set(PRODUCT_SIZE.ARTICLE_, productSize.article())
                .set(PRODUCT_SIZE.SIZE_, productSize.size())
                .returningResult(PRODUCT_SIZE.ID_, PRODUCT_SIZE.PRODUCT_, PRODUCT_SIZE.ARTICLE_, PRODUCT_SIZE.SIZE_, PRODUCT_SIZE.IS_REMOVED_)
                .fetchOne(mapping(ProductSize::new));
    }

    public List<ProductSize> findAllByProductId(Long productId) {
        return this.dsl
                .selectFrom(PRODUCT_SIZE)
                .where(PRODUCT_SIZE.PRODUCT_.eq(productId))
                .orderBy(PRODUCT_SIZE.ID_)
                .fetch().stream().map(p -> ProductSize.toProduct(p))
                .collect(Collectors.toList());
    }

    public ProductSize update(ProductSize productSize) {
        return this.dsl.update(PRODUCT_SIZE)
                .set(PRODUCT_SIZE.PRODUCT_, productSize.product())
                .set(PRODUCT_SIZE.ARTICLE_, productSize.article())
                .set(PRODUCT_SIZE.SIZE_, productSize.size())
                .where(PRODUCT_SIZE.ID_.eq(productSize.id()))
                .returningResult(PRODUCT_SIZE.ID_, PRODUCT_SIZE.PRODUCT_, PRODUCT_SIZE.ARTICLE_, PRODUCT_SIZE.SIZE_, PRODUCT_SIZE.IS_REMOVED_)
                .fetchOne(mapping(ProductSize::new));
    }


    public int delete(Long id) {
        return this.dsl.update(PRODUCT_SIZE)
                .set(PRODUCT_SIZE.IS_REMOVED_, true)
                .where(PRODUCT_SIZE.ID_.eq(id))
                .execute();
    }
}
