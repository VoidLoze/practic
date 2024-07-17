package novikov.nikita.express_delivery.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("novikov.nikita.express_delivery.domain")
@EnableJpaRepositories("novikov.nikita.express_delivery.repos")
@EnableTransactionManagement
public class DomainConfig {
}
