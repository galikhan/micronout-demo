package kz.aspansoftware.controller;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
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
import java.util.Optional;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/private/category")
@Introspected
public class CategoryPrivateController {

    private Logger log = LoggerFactory.getLogger(CategoryPrivateController.class);

    @Inject
    private CategoryRepository repository;

    @Post("/")
    @Connectable
    public Category create(@Body Category category) {
        return this.repository.create(category);
    }

    @Put("/")
    @Connectable
    public Category update(@Body Category category) {
        return this.repository.update(category);
    }

    @Get("/parent/{id}")
    @Connectable
    public List<Category> findCategoryPyParent(@PathVariable Long id) {
        return this.repository.findCategoryPyParent(id);
    }



    @Delete("/{id}")
    @Connectable
    public int delete(@PathVariable Long id) {
        return this.repository.delete(id);
    }

}
