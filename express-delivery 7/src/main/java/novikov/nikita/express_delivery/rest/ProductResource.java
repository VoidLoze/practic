package novikov.nikita.express_delivery.rest;

import jakarta.validation.Valid;
import java.util.List;
import novikov.nikita.express_delivery.model.ProductDTO;
import novikov.nikita.express_delivery.service.ProductService;
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
@RequestMapping(value = "/api/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductResource {

    private final ProductService productService;

    public ProductResource(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("all")
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/top10/{customerId}")
    public ResponseEntity<List<ProductDTO>> getTopProducts(@PathVariable long customerId) {
        return ResponseEntity.ok(productService.findMostFrequentProductsByUser(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid final ProductDTO productDTO) {
        final Long createdId = productService.create(productDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> update(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProductDTO productDTO) {
        productService.update(id, productDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = productService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
