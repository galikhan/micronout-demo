package kz.aspansoftware.repository;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.Category;
import kz.jooq.model.tables.records.CategoryRecord;
import kz.jooq.model.tables.records.FileRecord;
import org.jooq.DSLContext;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kz.jooq.model.tables.Category.CATEGORY;
import static kz.jooq.model.tables.File.FILE;
import static org.jooq.Records.mapping;

@Singleton
public class CategoryRepository {

    private final DSLContext dsl;

    public CategoryRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    //    Category(Long id, Long parent, String name, String description, Boolean isRemoved) {
    public Category create(Category category) {
        return this.dsl
                .insertInto(CATEGORY)
                .set(CATEGORY.PARENT_, category.parent())
                .set(CATEGORY.NAME_, category.name())
                .set(CATEGORY.DESCRIPTION_, category.description())
                .set(CATEGORY.IS_REMOVED_, false)
                .set(CATEGORY.CREATED_, LocalDateTime.now())
                .set(CATEGORY.LEVEL_, category.level())
                .set(CATEGORY.IMAGE_, category.image())
                .set(CATEGORY.IS_VALTEC_, category.isValtec())
                .set(CATEGORY.IS_SANTEC_, category.isSantec())

                .returningResult(CATEGORY.ID_, CATEGORY.PARENT_, CATEGORY.NAME_,
                        CATEGORY.DESCRIPTION_, CATEGORY.IS_REMOVED_, CATEGORY.LEVEL_,
                        CATEGORY.IMAGE_, CATEGORY.IS_SANTEC_, CATEGORY.IS_VALTEC_)
                .fetchOne(mapping(Category::new));
    }

    public Category update(Category category) {
        return this.dsl
                .update(CATEGORY)
                .set(CATEGORY.PARENT_, category.parent())
                .set(CATEGORY.NAME_, category.name())
                .set(CATEGORY.DESCRIPTION_, category.description())
                .set(CATEGORY.IS_REMOVED_, false)
                .set(CATEGORY.LEVEL_, category.level())
                .set(CATEGORY.IMAGE_, category.image())
                .set(CATEGORY.IS_VALTEC_, category.isValtec())
                .set(CATEGORY.IS_SANTEC_, category.isSantec())

                .where(CATEGORY.ID_.eq(category.id()))

                .returningResult(CATEGORY.ID_, CATEGORY.PARENT_, CATEGORY.NAME_,
                        CATEGORY.DESCRIPTION_, CATEGORY.IS_REMOVED_, CATEGORY.LEVEL_,
                        CATEGORY.IMAGE_, CATEGORY.IS_SANTEC_, CATEGORY.IS_VALTEC_)
                .fetchOne(mapping(Category::new));
    }


    public List<Category> findCategoryPyParent(Long parent) {
        return this.dsl
                .selectFrom(CATEGORY)
                .where(CATEGORY.PARENT_.eq(parent).and(CATEGORY.IS_REMOVED_.eq(false)))
                .fetch().stream().map(Category::toCategory).collect(Collectors.toList());
    }

    public Map<CategoryRecord, List<FileRecord>> findCategoryAndImagePyParent(Long parent) {
        return this.dsl
                .select(CATEGORY.fields())
                .select(FILE.fields())
                .from(CATEGORY)
                .leftJoin(FILE)
                .on(CATEGORY.IMAGE_.eq(FILE.ID_))
                .where(CATEGORY.PARENT_.eq(parent).and(CATEGORY.IS_REMOVED_.eq(false)))
                .orderBy(CATEGORY.NAME_)
                .fetchGroups(
                        c -> c.into(CATEGORY),
                        f -> f.into(FILE)
                );
    }

    public Map<CategoryRecord, List<FileRecord>> findCategoryAndImageByParams(Long parent, Boolean isSantec, Boolean isValtec) {
        System.out.println("isSantec | isValtec = " +  isSantec + " | " + isValtec);
        var query = this.dsl
                .select(CATEGORY.fields())
                .select(FILE.fields())
                .from(CATEGORY)
                .leftJoin(FILE)
                .on(CATEGORY.IMAGE_.eq(FILE.ID_))
                .where(
                        CATEGORY.PARENT_.eq(parent)
                                .and(CATEGORY.IS_REMOVED_.eq(false))
                                .and(CATEGORY.IS_SANTEC_.eq(isSantec).and(CATEGORY.IS_VALTEC_.eq(isValtec)))
                )
                .orderBy(CATEGORY.NAME_);

        System.out.println(query);
        return query.fetchGroups(
                        c -> c.into(CATEGORY),
                        f -> f.into(FILE)
                );

    }

    public List<Category> findLevelCategoriesById(Long id) {
        Long parent = this.dsl.select(CATEGORY.PARENT_).from(CATEGORY).where(CATEGORY.ID_.eq(id)).fetch().get(0).value1();
        return this.dsl
                .selectFrom(CATEGORY)
                .where(CATEGORY.PARENT_.eq(parent).and(CATEGORY.IS_REMOVED_.eq(false)))
                .fetch().stream().map(Category::toCategory).collect(Collectors.toList());
    }

    public List<Category> findFirstLevelCategoriesByChildId(Long id) {
        Long secondLevelParent = this.dsl.select(CATEGORY.PARENT_).from(CATEGORY).where(CATEGORY.ID_.eq(id)).fetch().get(0).value1();
        Long firstLevelParent = this.dsl.select(CATEGORY.PARENT_).from(CATEGORY).where(CATEGORY.ID_.eq(secondLevelParent)).fetch().get(0).value1();
        return this.dsl
                .selectFrom(CATEGORY)
                .where(CATEGORY.PARENT_.eq(firstLevelParent).and(CATEGORY.IS_REMOVED_.eq(false)))
                .fetch().stream().map(Category::toCategory).collect(Collectors.toList());
    }

    public int delete(Long id) {
        return this.dsl.update(CATEGORY)
                .set(CATEGORY.IS_REMOVED_, true)
                .where(CATEGORY.ID_.eq(id))
                .execute();
    }

}

