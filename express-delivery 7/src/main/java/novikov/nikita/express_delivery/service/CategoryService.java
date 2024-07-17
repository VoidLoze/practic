package novikov.nikita.express_delivery.service;

import novikov.nikita.express_delivery.domain.Category;
import novikov.nikita.express_delivery.model.CategoryDTO;
import novikov.nikita.express_delivery.repos.CategoryRepository;
import novikov.nikita.express_delivery.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(category -> mapToDTO(category, new CategoryDTO())).toList();
    }

    public CategoryDTO get(final Long id) {
        return categoryRepository.findById(id).map(category -> mapToDTO(category, new CategoryDTO())).orElseThrow(NoClassDefFoundError::new);
    }

    public Long create(final CategoryDTO categoryDTO) {
        final Category category = new Category();
        mapToEntity(categoryDTO, category);
        return categoryRepository.save(category).getId();
    }

    public void update(final Long id, final CategoryDTO categoryDTO) {
        final Category category = categoryRepository.findById(id).orElseThrow(NotFoundException::new);
        mapToEntity(categoryDTO, category);
        categoryRepository.save(category);
    }

    public void delete(final Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDTO mapToDTO(final Category category, final CategoryDTO categoryDTO) {
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public void mapToEntity(final CategoryDTO categoryDTO, final Category category) {
        category.setName(categoryDTO.getName());
    }
}
