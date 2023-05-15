package example.actuator_issue;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.endpoint.expose.EndpointExposure;
import org.springframework.boot.actuate.autoconfigure.endpoint.expose.IncludeExcludeEndpointFilter;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
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

    // Workaround: Set this bean to enable WebEndpoint filters
    // ref: https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/endpoint/web/WebEndpointAutoConfiguration.java#L112-L116
//    @Bean
//    public IncludeExcludeEndpointFilter<ExposableWebEndpoint> webExposeExcludePropertyEndpointFilter(WebEndpointProperties properties) {
//        WebEndpointProperties.Exposure exposure = properties.getExposure();
//        return new IncludeExcludeEndpointFilter<>(ExposableWebEndpoint.class, exposure.getInclude(),
//                exposure.getExclude(), EndpointExposure.WEB.getDefaultIncludes());
//    }
}
