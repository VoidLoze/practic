package novikov.nikita.express_delivery.model;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
public class OrderDTO {

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime dataGet;

    @NotNull
    private ProductDTO product;

    @NotNull
    private CustomerDTO customer;

}
