package novikov.nikita.express_delivery.repos;

import novikov.nikita.express_delivery.domain.Courier;
import novikov.nikita.express_delivery.domain.Delivery;
import novikov.nikita.express_delivery.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Delivery findFirstByCourier(Courier courier);

    Delivery findFirstByOrder(Order order);

}
