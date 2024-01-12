package kz.aspansoftware.controller;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import kz.aspansoftware.records.Category;
import kz.aspansoftware.records.CategoryAndImage;
import kz.aspansoftware.records.SantecFile;
import kz.aspansoftware.repository.CategoryRepository;
import kz.jooq.model.tables.records.CategoryRecord;
import kz.jooq.model.tables.records.FileRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/category")
@Introspected
public class CategoryController {

    private Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Inject
    private CategoryRepository repository;


    @Get("/extended/parent/{id}")
    @Connectable
    public List<CategoryAndImage> findCategoryAndImagePyParent(@PathVariable Long id) {

        var map = new HashMap<CategoryRecord, List<FileRecord>>();
        map = (HashMap<CategoryRecord, List<FileRecord>>) this.repository.findCategoryAndImageByParams(id);
        var list = new ArrayList<CategoryAndImage>();
        map.entrySet().stream().forEach(i -> {
            var category = Category.toCategory(i.getKey());
            SantecFile file = null;
            if (i.getValue() != null && i.getValue().size() > 0) {
                file = SantecFile.toSantecFile(i.getValue().get(0));
            }
            list.add(new CategoryAndImage(category, file));
        });
        return list;
    }

    @Get("/by/rows-parent-id/{id}")
    @Connectable
    public List<Category> findLevelCategoriesById(@PathVariable Long id) {
        return this.repository.findLevelCategoriesById(id);
    }

    @Get("/by/first-level-rows-by-child-id/{id}")
    @Connectable
    public List<Category> findFirstLevelCategoriesByChildId(@PathVariable Long id) {
        return this.repository.findFirstLevelCategoriesByChildId(id);
    }
}
