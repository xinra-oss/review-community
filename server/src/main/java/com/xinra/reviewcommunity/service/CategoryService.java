package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.dto.CategoryDto;
import com.xinra.reviewcommunity.dto.CreateCategoryDto;
import com.xinra.reviewcommunity.dto.SerialDto;
import com.xinra.reviewcommunity.entity.Category;
import com.xinra.reviewcommunity.repo.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CategoryService extends AbstractService {

  private @Autowired CategoryRepository<Category> categoryRepo;

  /**
   * Creates a new Category.
   */
  public SerialDto createCategory(@NonNull CreateCategoryDto createCategoryDto) {

    Category category = entityFactory.createEntity(Category.class);
    category.setName(createCategoryDto.getName());

    if (createCategoryDto.getParentSerial() != 0) {
      Category parentCategory = categoryRepo.findBySerial(createCategoryDto.getParentSerial());
      category.setParent(parentCategory);
    }

    int serial = serviceProvider.getService(SerialService.class).getNextSerial(Category.class);
    category.setSerial(serial);
    categoryRepo.save(category);

    log.info("Created category with name '{}'", createCategoryDto.getName());

    SerialDto serialDto = dtoFactory.createDto(SerialDto.class);
    serialDto.setSerial(serial);
    return serialDto;
  }

  /**
   * Returns a list of all categories.
   *
   */
  public List<CategoryDto> getAllCategories() {
    return categoryRepo.findByParentIsNull().stream()
      .map(this::toDto)
      .collect(Collectors.toList());

//    List<CategoryDto> list = new ArrayList<>();
//
//    for(Category cat : categoryRepo.findByParentIsNull()) {
//      list.add(toDto(cat));
//    }
//
//    return list;
  }

  private CategoryDto toDto(Category category) {

    CategoryDto categoryDto = dtoFactory.createDto(CategoryDto.class);

    categoryDto.setName(category.getName());
    categoryDto.setSerial(category.getSerial());

    categoryDto.setChildren(category.getChildren().stream()
            .map(this::toDto)
            .collect(Collectors.toList()));

    return categoryDto;
  }

}
