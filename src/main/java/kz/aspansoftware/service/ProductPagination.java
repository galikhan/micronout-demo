package kz.aspansoftware.service;

import jakarta.inject.Singleton;
import kz.aspansoftware.records.Page;
import kz.aspansoftware.records.Product;
import kz.aspansoftware.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProductPagination {

    ProductRepository repository;

    public ProductPagination(ProductRepository repository) {
        this.repository = repository;
    }

    public Page findPageByParams(Long first, Long last, Integer size) {

        var prev = true;
        var next = true;
        List<Product> products;

        if(first == null && last == null) {
            products = this.repository.findFirstPage(size);
        } else {
            if(first != null) {
                products = this.repository.findPrevPage(first, size);
            } else {
                products = this.repository.findNextPage(last, size) ;
            }
        }

        if(products.size() == 0) {
            if(first != null) {
                prev = false;
            } else {
                next = false;
            }
            products = new ArrayList<>();
 //            throw new NoMoreRowsException("Больше строк нет, вы достигли предела!");
        }
        return new Page(prev, next, products);
    }
}
