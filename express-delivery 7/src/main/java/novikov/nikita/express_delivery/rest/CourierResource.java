package novikov.nikita.express_delivery.rest;

import jakarta.validation.Valid;
import java.util.List;
import novikov.nikita.express_delivery.model.CourierDTO;
import novikov.nikita.express_delivery.service.CourierService;
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
@RequestMapping(value = "/api/courier", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourierResource {

    private final CourierService courierService;

    public CourierResource(final CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<CourierDTO>> getAll() {
        return ResponseEntity.ok(courierService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourierDTO> get(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(courierService.get(id));
    }

    @GetMapping("/top")
    public ResponseEntity<List<CourierDTO>> getTop() {
        return ResponseEntity.ok(courierService.findTopCouriersByDeliveredOrders() );
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid final CourierDTO courierDTO) {
        final Long createdId = courierService.create(courierDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CourierDTO courierDTO) {
        courierService.update(id, courierDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = courierService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        courierService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
