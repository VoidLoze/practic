package novikov.nikita.express_delivery.model;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
public class DeliveryDTO {

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime dataArrived;

    @Size(max = 255)
    private String taken;

    private CourierDTO courier;

    private OrderDTO order;

}
