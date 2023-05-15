package example.reactor_hooks_issue;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.reactor3.RequestContextHooks;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Simple application that just does hello world
 */
@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        RequestContextHooks.enable();
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(
            RedisReactiveCommands<String, String> commands
    ) {
        return builder -> {
            builder.service("/", (ctx, req) -> {
                        final var mono = commands.set("test-key", "test-value")
                                .map(it -> {
                                    log.info("ArmeriaContext={}", ServiceRequestContext.currentOrNull());
                                    return HttpResponse.of(it);
                                });
                        return HttpResponse.from(mono.toFuture());
                    }
            );
        };
    }

    @Bean
    public ClientResources clientResources() {
        return DefaultClientResources.builder().build();
    }

    @Bean
    public RedisClient redisClient(ClientResources clientResources) {
        return RedisClient.create(clientResources, RedisURI.builder().withHost("localhost").withPort(6379).build());
    }

    @Bean
    public StatefulRedisConnection<String, String> redisConnection(RedisClient redisClient) {
        return redisClient.connect();
    }

    @Bean
    public RedisReactiveCommands<String, String> redisReactiveCommands(StatefulRedisConnection<String, String> redisConnection) {
        return redisConnection.reactive();
    }
}
