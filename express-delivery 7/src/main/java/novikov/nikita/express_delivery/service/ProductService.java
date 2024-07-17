package novikov.nikita.express_delivery.service;

import java.util.List;


import novikov.nikita.express_delivery.domain.Category;
import novikov.nikita.express_delivery.domain.Order;
import novikov.nikita.express_delivery.domain.Product;
import novikov.nikita.express_delivery.model.CategoryDTO;
import novikov.nikita.express_delivery.model.ProductDTO;
import novikov.nikita.express_delivery.repos.CategoryRepository;
import novikov.nikita.express_delivery.repos.OrderRepository;
import novikov.nikita.express_delivery.repos.ProductRepository;
import novikov.nikita.express_delivery.util.NotFoundException;
import novikov.nikita.express_delivery.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CategoryService categoryService;

    public ProductService(final ProductRepository productRepository,
                          final OrderRepository orderRepository,
                          final CategoryService categoryService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.categoryService = categoryService;
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public ProductDTO get(final Long id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getId();
    }

    public void update(final Long id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final Long id) {
        productRepository.deleteById(id);
    }


    public List<ProductDTO> findMostFrequentProductsByUser(Long getCustomerId){
        return productRepository.findMostFrequentProductsByUser(getCustomerId).stream()
                .map(product -> mapToDTO(product, new ProductDTO())).toList();
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setMenuName(product.getMenuName());
        productDTO.setPrice(product.getPrice());
        CategoryDTO categoryDTO = categoryService.mapToDTO(product.getCategory(), new CategoryDTO());
        productDTO.setCategory(categoryDTO);
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setMenuName(productDTO.getMenuName());
        product.setPrice(productDTO.getPrice());
        Category category = new Category();
        categoryService.mapToEntity(productDTO.getCategory(), category);
        product.setCategory(category);
        return product;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Order productOrder = orderRepository.findFirstByProduct(product);
        if (productOrder != null) {
            referencedWarning.setKey("product.orders.product.referenced");
            referencedWarning.addParam(productOrder.getId());
            return referencedWarning;
        }
        return null;
    }

}
