package novikov.nikita.express_delivery.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import novikov.nikita.express_delivery.domain.Category;


@Getter
@Setter
public class ProductDTO {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String menuName;

    @NotNull
    private int price;

    @NotNull
    private CategoryDTO category;



}
