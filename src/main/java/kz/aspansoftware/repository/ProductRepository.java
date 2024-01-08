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
                .set(PRODUCT.CATEGORY_, product.category())
                .set(PRODUCT.VIDEO_, product.video())
                .set(PRODUCT.IS_NEW_, product.isNew())
                .set(PRODUCT.IS_REMOVED_, product.isRemoved())
                .set(PRODUCT.IS_SANTEC_, product.isSantec())
                .returningResult(PRODUCT.ID_, PRODUCT.NAME_, PRODUCT.DESCRIPTION_, PRODUCT.CATEGORY_, PRODUCT.VIDEO_, PRODUCT.IS_NEW_, PRODUCT.IS_REMOVED_, PRODUCT.IS_SANTEC_)
                .fetchOne(mapping(Product::new));
    }

    public Product update(Product product) {
        return this.dsl
                .update(PRODUCT)
                .set(PRODUCT.NAME_, product.name())
                .set(PRODUCT.DESCRIPTION_, product.description())
                .set(PRODUCT.CATEGORY_, product.category())
                .set(PRODUCT.VIDEO_, product.video())
                .set(PRODUCT.IS_NEW_, product.isNew())
                .set(PRODUCT.IS_REMOVED_, product.isRemoved())
                .set(PRODUCT.IS_SANTEC_, product.isSantec())
                .where(PRODUCT.ID_.eq(product.id()))
                .returningResult(PRODUCT.ID_, PRODUCT.NAME_, PRODUCT.DESCRIPTION_, PRODUCT.CATEGORY_, PRODUCT.VIDEO_, PRODUCT.IS_NEW_, PRODUCT.IS_REMOVED_, PRODUCT.IS_SANTEC_)
                .fetchOne(mapping(Product::new));
    }

    public int delete(Long id) {
        return this.dsl.update(PRODUCT)
                .set(PRODUCT.IS_REMOVED_, true)
                .where(PRODUCT.ID_.eq(id))
                .execute();
    }

    public List<Product> findAll() {
        return this.dsl.selectFrom(PRODUCT)
                .fetch().stream().map(Product::toProduct)
                .collect(Collectors.toList());
    }

    public List<Product> findFirstPage(int pageSize) {
        return this.dsl.selectFrom(PRODUCT)
                .orderBy(PRODUCT.ID_.desc())
                .maxRows(pageSize)
                .fetchSize(pageSize)
                .stream().map(Product::toProduct)
                .collect(Collectors.toList());
    }

    public List<Product> findNextPage(Long lastId, int pageSize) {
        return this.dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID_.lt(lastId))
                .orderBy(PRODUCT.ID_.desc())
                .maxRows(pageSize)
                .fetchSize(pageSize)
                .stream().map(Product::toProduct)
                .collect(Collectors.toList());
    }

    public List<Product> findPrevPage(Long firstId, int pageSize) {
        return this.dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID_.gt(firstId))
                .orderBy(PRODUCT.ID_.desc())
                .maxRows(pageSize)
                .fetchSize(pageSize)
                .stream().map(Product::toProduct)
                .collect(Collectors.toList());
    }


    public Product findById(Long id) {
        var result = this.dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID_.eq(id))
                .fetchOne();
        if(result != null) {
            return Product.toProduct(result);
        }
        return null;
    }

    public List<Product>  findByCategory(Long categoryId) {
        return this.dsl.selectFrom(PRODUCT)
                .where(PRODUCT.CATEGORY_.eq(categoryId))
                .orderBy(PRODUCT.ID_.desc())
                .fetch()
                .stream().map(Product::toProduct)
                .collect(Collectors.toList());
    }
}
