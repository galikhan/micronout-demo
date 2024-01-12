package kz.aspansoftware.auth;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Singleton
public class AuthProvider implements AuthenticationProvider<HttpRequest<?>> {

    private Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {

        var id = authenticationRequest.getIdentity();
        var sec = authenticationRequest.getSecret();
        logger.info("id {} sec {}", id, sec);
        return Flux.create(emitter -> {
            if (authenticationRequest.getIdentity().equals("admin") &&
                    authenticationRequest.getSecret().equals("123")) {
                emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity()));
                emitter.complete();
            } else {
                emitter.error(AuthenticationResponse.exception());
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }

}