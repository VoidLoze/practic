package novikov.nikita.express_delivery.repos;

import novikov.nikita.express_delivery.domain.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    @Query(value = "SELECT c.id, c.first_name, c.last_name, c.phone_number, c.delivery_type FROM courier c" +
            "                  LEFT JOIN delivery d on d.courier_id = c.id" +
            "                  WHERE d.taken ilike 'Да'" +
            "                  GROUP BY c.id" +
            "                  ORDER BY COUNT(DISTINCT d.id) DESC", nativeQuery = true)
    List<Courier> findTopCouriersByDeliveredOrders();

}
