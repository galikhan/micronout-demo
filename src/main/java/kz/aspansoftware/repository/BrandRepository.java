package kz.aspansoftware.repository;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.Brand;
import kz.aspansoftware.records.Category;
import org.jooq.DSLContext;

import java.util.List;
import java.util.stream.Collectors;

import static kz.jooq.model.tables.Brand.BRAND;
import static kz.jooq.model.tables.Category.CATEGORY;

@Singleton
public class BrandRepository {
    private final DSLContext dsl;

    public BrandRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<Brand> findAll() {
        return this.dsl
                .selectFrom(BRAND)
                .fetch().stream().map(Brand::to)
                .collect(Collectors.toList());
    }
}
