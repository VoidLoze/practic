package novikov.nikita.express_delivery.rest;

import jakarta.validation.Valid;
import java.util.List;
import novikov.nikita.express_delivery.model.CustomerDTO;
import novikov.nikita.express_delivery.service.CustomerService;
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
@RequestMapping(value = "/api/customer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customerService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid final CustomerDTO customerDTO) {
        final Long createdId = customerService.create(customerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CustomerDTO customerDTO) {
        customerService.update(id, customerDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = customerService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
