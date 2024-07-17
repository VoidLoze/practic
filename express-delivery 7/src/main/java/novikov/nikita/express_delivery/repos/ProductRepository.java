package novikov.nikita.express_delivery.repos;

import novikov.nikita.express_delivery.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT *\n" +
            "FROM PRODUCT P\n" +
            "WHERE P.CATEGORY_ID = (SELECT P.CATEGORY_ID cat\n" +
            "                       FROM SOME_ORDER\n" +
            "                                LEFT JOIN PRODUCT P ON P.ID = SOME_ORDER.PRODUCT_ID\n" +
            "                       WHERE SOME_ORDER.CUSTOMER_ID = ?\n" +
            "                       GROUP BY cat\n" +
            "                       ORDER BY cat\n" +
            "                       LIMIT 1)\n" +
            "ORDER BY RANDOM()\n" +
            "LIMIT 10;", nativeQuery = true)
    List<Product> findMostFrequentProductsByUser(@Param("customerId") Long customerId);
}
