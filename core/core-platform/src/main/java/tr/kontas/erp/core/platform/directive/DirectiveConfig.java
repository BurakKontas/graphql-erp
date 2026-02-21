package tr.kontas.erp.core.platform.directive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class DirectiveConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(AuthDirective authDirective,
                                                           AuditDirective auditDirective) {
        return builder -> builder
                .directive("auth", authDirective)
                .directive("audit", auditDirective);
    }
}