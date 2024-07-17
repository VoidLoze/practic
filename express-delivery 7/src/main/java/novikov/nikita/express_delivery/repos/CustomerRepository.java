package novikov.nikita.express_delivery.repos;

import novikov.nikita.express_delivery.domain.Customer;
import novikov.nikita.express_delivery.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
