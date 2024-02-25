package kz.aspansoftware.records;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record ProductWithBrandFilter(List<ProductExtended> products,  List<Brand> brands) {

}
