package novikov.nikita.express_delivery.rest;

import jakarta.validation.Valid;
import java.util.List;
import novikov.nikita.express_delivery.model.DeliveryDTO;
import novikov.nikita.express_delivery.service.DeliveryService;
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
@RequestMapping(value = "/api/delivery", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeliveryResource {

    private final DeliveryService deliveryService;

    public DeliveryResource(final DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAll() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> get(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(deliveryService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid final DeliveryDTO deliveryDTO) {
        final Long createdId = deliveryService.create(deliveryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DeliveryDTO deliveryDTO) {
        deliveryService.update(id, deliveryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") final Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
