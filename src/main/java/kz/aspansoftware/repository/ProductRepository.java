package kz.aspansoftware.repository;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.Product;
import kz.aspansoftware.records.ProductExtended;
import kz.jooq.model.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.stream.Collectors;

import static kz.jooq.model.tables.File.FILE;
import static kz.jooq.model.tables.Product.PRODUCT;
import static kz.jooq.model.tables.ProductSize.PRODUCT_SIZE;
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
                .set(PRODUCT.IS_REMOVED_, (product.isRemoved() != null ? product.isRemoved() : false))
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
        if (result != null) {
            return Product.toProduct(result);
        }
        return null;
    }

    public List<Product> findByCategory(Long categoryId) {
        return this.dsl.selectFrom(PRODUCT)
                .where(PRODUCT.CATEGORY_.eq(categoryId))
                .orderBy(PRODUCT.ID_.desc())
                .fetch()
                .stream().map(Product::toProduct)
                .collect(Collectors.toList());
    }

    public List<Product> findByCategoryAndParams(Long categoryId, Boolean isSantec, Boolean isValtec) {

        SelectConditionStep<ProductRecord> query = this.dsl.selectFrom(PRODUCT)
                .where(PRODUCT.CATEGORY_.eq(categoryId));

        if (isSantec && !isValtec) {
            query.and(PRODUCT.IS_SANTEC_.eq(true));
        } else if (!isSantec && isValtec) {
            query.and(PRODUCT.IS_SANTEC_.eq(false).or(PRODUCT.IS_SANTEC_.isNull()));
        }
        return query.orderBy(PRODUCT.ID_.desc())
                .fetch()
                .stream().map(Product::toProduct)
                .collect(Collectors.toList());
    }

    public List<ProductExtended> findByCategoryAndParamsExtendedWithFilename(Long categoryId) {

        var query
                = this.dsl
                .select(PRODUCT.fields())
                .select(
                        DSL.field(
                                this.dsl.select(FILE.FILENAME_)
                                        .from(FILE)
                                        .where(FILE.CONTAINER_.eq(PRODUCT.ID_))
                                        .and(FILE.FILENAME_.startsWith("thumbnail"))
                                        .and(FILE.IS_REMOVED_.eq(false))
                        .orderBy(FILE.ID_).limit(1)
                        ).as("filename_")
                )
                .from(PRODUCT)
                .where(PRODUCT.CATEGORY_.eq(categoryId))
                .and(PRODUCT.IS_REMOVED_.eq(false));

        return query.orderBy(PRODUCT.NAME_)
                .fetch()
                .stream().map(ProductExtended::toProduct)
                .collect(Collectors.toList());
    }

    public List<Product> searchByQuery(String searchQuery) {
        //1 search in titles
        //2 search in description
        //2 search in article and size
        var psizeQuery = this.dsl.selectFrom(PRODUCT_SIZE)
                .where(PRODUCT_SIZE.ARTICLE_.likeIgnoreCase("%" + searchQuery + "%"))
                .or(PRODUCT_SIZE.SIZE_.likeIgnoreCase("%" + searchQuery + "%")).fetch().stream().map(i -> i.getProduct_()).collect(Collectors.toList());

        var result = this.dsl.selectFrom(PRODUCT)
                .where(PRODUCT.NAME_.likeIgnoreCase("%" + searchQuery + "%"))
                .or(PRODUCT.DESCRIPTION_.likeIgnoreCase("%" + searchQuery + "%"))
                .or(PRODUCT.ID_.in(psizeQuery)).stream().map(Product::toProduct).collect(Collectors.toList());
        return result;
    }

    public List<ProductExtended> searchByQueryExtendedWithFilename(String searchQuery) {
        //1 search in titles
        //2 search in description
        //2 search in article and size
        var psizeQuery = this.dsl.selectFrom(PRODUCT_SIZE)
                .where(PRODUCT_SIZE.ARTICLE_.likeIgnoreCase("%" + searchQuery + "%"))
                .or(PRODUCT_SIZE.SIZE_.likeIgnoreCase("%" + searchQuery + "%")).fetch().stream().map(i -> i.getProduct_()).collect(Collectors.toList());


        var result = this.dsl.select(PRODUCT.fields()).select(FILE.fields(FILE.FILENAME_)).distinctOn(PRODUCT.ID_)
                .from(PRODUCT).leftJoin(FILE)
                .on(FILE.CONTAINER_.eq(PRODUCT.ID_).and(FILE.IS_REMOVED_.eq(false).and(FILE.FILENAME_.startsWith("thumbnail"))))
                .where(PRODUCT.NAME_.likeIgnoreCase("%" + searchQuery + "%"))
                .or(PRODUCT.DESCRIPTION_.likeIgnoreCase("%" + searchQuery + "%"))
                .or(PRODUCT.ID_.in(psizeQuery)).fetchSize(30);
        return result.stream().map(ProductExtended::toProduct).collect(Collectors.toList());
    }


}
