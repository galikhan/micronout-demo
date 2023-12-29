package kz.aspansoftware.controller;

import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import kz.aspansoftware.records.Product;
import kz.aspansoftware.records.ProductSize;
import kz.aspansoftware.repository.ProductRepository;
import kz.aspansoftware.repository.ProductSizeRepository;

import java.util.List;

@Controller("/product")
public class ProductSizeController {

    @Inject
    ProductSizeRepository repository;

    @Get("/size/{productId}")
    @Connectable
    public List<ProductSize> findAllByProductId(@PathVariable Long productId) {
        return this.repository.findAllByProductId(productId);
    }

    @Post("/size")
    @Connectable
    public ProductSize create(@Body ProductSize productSize) {
        return this.repository.create(productSize);
    }


    @Put("/size")
    @Connectable
    public ProductSize update(@Body ProductSize productSize) {
        return this.repository.update(productSize);
    }

    @Delete("/size/{id}")
    @Connectable
    public int delete(@PathVariable Long id) {
        return this.repository.delete(id);
    }


}
