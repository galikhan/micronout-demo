package kz.aspansoftware;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class MicronoutDemoTest {

    @Inject
    EmbeddedApplication<?> application;
    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }


    @Test
    public void testHello() {
        HttpRequest<?> request = HttpRequest.GET("/hello").accept(MediaType.TEXT_PLAIN);
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("Hello world of micronaut!", body);
    }
}
