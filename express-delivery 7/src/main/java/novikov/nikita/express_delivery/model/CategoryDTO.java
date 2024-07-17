package novikov.nikita.express_delivery.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDTO {

    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

}
