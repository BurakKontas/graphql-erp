package tr.kontas.erp.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "tr.kontas.erp")
@EnableJpaRepositories(basePackages = "tr.kontas.erp")
@EntityScan(basePackages = "tr.kontas.erp")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = "tr.kontas.erp")
public class AppApplication {
    static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
