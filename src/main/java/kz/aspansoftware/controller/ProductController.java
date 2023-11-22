package kz.aspansoftware.controller;

import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import kz.aspansoftware.records.Product;
import kz.aspansoftware.repository.ProductRepository;

import java.util.List;

@Controller("/product")
public class ProductController {

    @Inject
    ProductRepository repository;

    @Get("/all")
    @Connectable
    public List<Product> getAll() {
        return this.repository.findAll();
    }

    @Post("/")
    @Connectable
    public Product create(@Body Product product) {
        return this.repository.create(product);
    }
}
