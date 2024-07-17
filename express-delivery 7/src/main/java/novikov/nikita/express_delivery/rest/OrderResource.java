package novikov.nikita.express_delivery.rest;

import jakarta.validation.Valid;
import java.util.List;
import novikov.nikita.express_delivery.model.OrderDTO;
import novikov.nikita.express_delivery.service.OrdersService;
import novikov.nikita.express_delivery.util.ReferencedException;
import novikov.nikita.express_delivery.util.ReferencedWarning;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {

    private final OrdersService ordersService;

    public OrderResource(final OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAll() {
        return ResponseEntity.ok(ordersService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> get(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(ordersService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid final OrderDTO orderDTO) {
        final Long createdId = ordersService.create(orderDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OrderDTO orderDTO) {
        ordersService.update(id, orderDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = ordersService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ordersService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
