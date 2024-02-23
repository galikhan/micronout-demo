package kz.aspansoftware.controller;

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
import io.micronaut.transaction.annotation.Transactional;
import kz.aspansoftware.records.Page;
import kz.aspansoftware.records.Product;
import kz.aspansoftware.records.ProductExtended;
import kz.aspansoftware.repository.ProductRepository;
import kz.aspansoftware.service.ProductPagination;

import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/product")
public class ProductController {

    ProductRepository repository;
    ProductPagination pagination;

    public ProductController(ProductRepository repository, ProductPagination pagination) {
        this.repository = repository;
        this.pagination = pagination;
    }

    @Get("/id/{id}")
    @Connectable
    public Product findById(@PathVariable Long id) {
        return this.repository.findById(id);
    }

    @Get("/search")
    @Connectable
    public List<Product> searchByQuery(@QueryValue String query) {
        return this.repository.searchByQuery(query);
    }

    @Get("/search-ext")
    @Connectable
    public List<ProductExtended> searchExtByQuery(@QueryValue String query) {
        return this.repository.searchByQueryExtendedWithFilename(query);
    }


    @Get("/category/{categoryId}")
    @Connectable
    public List<ProductExtended> findByCategory(@PathVariable Long categoryId,
                                                @QueryValue Optional<Boolean> isSantec,
                                                @QueryValue Optional<Boolean> isValtec
    ) {
        return this.repository.findByCategoryAndParamsExtendedWithFilename(categoryId);
    }

}
