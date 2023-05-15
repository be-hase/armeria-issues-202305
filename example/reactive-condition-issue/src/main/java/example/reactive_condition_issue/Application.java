package example.reactive_condition_issue;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Simple application that just does hello world
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator() {
        return builder -> {
            builder.service("/", (ctx, req) -> HttpResponse.of("Hello, world!"));
        };
    }
}
