package example.tracing_issue;

import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.CurrentTraceContextCustomizer;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.brave.RequestContextCurrentTraceContext;
import com.linecorp.armeria.server.brave.BraveService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.tracing.BraveTracing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Simple application that just does hello world
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public Tracing tracing() {
//        return Tracing.newBuilder()
//                .localServiceName("myService")
//                .currentTraceContext(RequestContextCurrentTraceContext.ofDefault())
//                .build();
//    }

    @Bean
    public CurrentTraceContext braveCurrentTraceContext(List<CurrentTraceContext.ScopeDecorator> scopeDecorators,
                                                        List<CurrentTraceContextCustomizer> currentTraceContextCustomizers) {
        final var builder = RequestContextCurrentTraceContext.builder();
        scopeDecorators.forEach(builder::addScopeDecorator);
        for (CurrentTraceContextCustomizer currentTraceContextCustomizer : currentTraceContextCustomizers) {
            currentTraceContextCustomizer.customize(builder);
        }
        return builder.build();
    }

    @Bean
    public ClientResources clientResources(Tracing tracing) {
        return DefaultClientResources.builder()
                .tracing(BraveTracing.create(tracing))
                .build();
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
    public ArmeriaServerConfigurator armeriaServerConfigurator(
            Tracing tracing
    ) {
        return builder -> {
            builder.service("/", (ctx, req) -> HttpResponse.of("Hello, world!"))
                    .decorator(BraveService.newDecorator(tracing))
            ;
        };
    }
}
