package novikov.nikita.express_delivery.repos;

import novikov.nikita.express_delivery.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
