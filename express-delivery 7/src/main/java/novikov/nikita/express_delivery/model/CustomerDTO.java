package novikov.nikita.express_delivery.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 15)
    private String phoneNumber;

    @NotNull
    @Size(max = 255)
    private String district;

    @NotNull
    @Size(max = 255)
    private String street;

    @NotNull
    private String house;
    @NotNull
    private Long appartment;

    private Double bonusPoints;
}
