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

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/private/product/")
public class ProductPrivateController {

    ProductRepository repository;
    ProductPagination pagination;

    public ProductPrivateController(ProductRepository repository, ProductPagination pagination) {
        this.repository = repository;
        this.pagination = pagination;
    }

    @Get("/page")
    @Transactional
    public Page findByPage(
            @QueryValue Optional<Long> first,
            @QueryValue Optional<Long> last,
            @QueryValue Integer size
    ) {
        return this.pagination.findPageByParams(first.orElse(null), last.orElse(null), size);
    }

    @Post
    @Connectable
    public Product create(@Body Product product) {
        return this.repository.create(product);
    }

    @Put
    @Connectable
    public Product update(@Body Product product) {
        return this.repository.update(product);
    }

    @Delete("/id/{id}")
    @Connectable
    public int delete(@PathVariable Long id) {
        return this.repository.delete(id);
    }


}
