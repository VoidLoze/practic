package novikov.nikita.express_delivery.repos;

import novikov.nikita.express_delivery.domain.Customer;
import novikov.nikita.express_delivery.domain.Order;
import novikov.nikita.express_delivery.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByProduct(Product product);

    Order findFirstByCustomer(Customer customer);

}
