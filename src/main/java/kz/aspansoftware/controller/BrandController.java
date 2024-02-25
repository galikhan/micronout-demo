package kz.aspansoftware.controller;

import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import kz.aspansoftware.records.Brand;
import kz.aspansoftware.repository.BrandRepository;

import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/brand")
public class BrandController {

    @Inject
    BrandRepository repository;

    @Get("/all")
    @Connectable
    public List<Brand> testPost() {
        return repository.findAll();
    }


}
