package novikov.nikita.express_delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class ExpressDeliveryApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ExpressDeliveryApplication.class, args);
    }

}
