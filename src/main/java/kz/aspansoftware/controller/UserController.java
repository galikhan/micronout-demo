package kz.aspansoftware.controller;

import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import kz.aspansoftware.records.User;
import kz.aspansoftware.repository.UserRepository;

import java.util.List;
import java.util.logging.Logger;

@Controller("/")
public class UserController {

    @Inject
    UserRepository repository;

    @Get("/user")
    public void testPost() {

    }

    @Post("/user")
    @Transactional
    public String testHello(@Body User user) {
//        var user = new User(2L, "Xwing", "xxx@maail.ru");
//        log.info("user {}", user);
        if(user != null) {
            System.out.println(user);
        } else {
            System.out.println("null");
        }
        var created = repository.createUser(user);
        return created.toString();
    }

    @Get("/users")
    @Connectable
    public List<User> users() {
        return repository.findAll();
    }
}
