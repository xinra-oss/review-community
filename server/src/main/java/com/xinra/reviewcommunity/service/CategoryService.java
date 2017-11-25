package com.xinra.reviewcommunity.service;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.xinra.nucleus.common.ContextHolder;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.entity.Category;
import com.xinra.reviewcommunity.entity.Market;
import com.xinra.reviewcommunity.repo.CategoryRepository;
import com.xinra.reviewcommunity.repo.MarketRepository;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;
import com.xinra.reviewcommunity.shared.dto.CreateCategoryDto;
import com.xinra.reviewcommunity.shared.dto.SerialDto;
import java.util.Collection;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CategoryService extends AbstractService {

  private @Autowired ContextHolder<Context> contextHolder;
  private @Autowired CategoryRepository<Category> categoryRepo;
  private @Autowired MarketRepository<Market> marketRepo;

  /**
   * Creates a new Category.
   */
  public SerialDto createCategory(@NonNull CreateCategoryDto createCategoryDto) {

    Category category = entityFactory.createEntity(Category.class);
    category.setName(createCategoryDto.getName());

    if (createCategoryDto.getParentSerial() != 0) {
      Category parentCategory = categoryRepo.findBySerial(createCategoryDto.getParentSerial());

      if (parentCategory == null) {
        throw new SerialNotFoundException(Category.class, createCategoryDto.getParentSerial());
      }
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
   * Returns all categories in a tree structure.
   */
  public Collection<CategoryDto> getCategoryTree() {
    
    Multimap<Integer, CategoryDto> children = MultimapBuilder.treeKeys().arrayListValues().build();
    
    Market market = marketRepo.findBySlug(contextHolder.get().getMarket().get().getSlug());
    
    for (Object[] result : categoryRepo.findAllWithNumProducts(market)) {
      CategoryDto category = toDtoIgnoringChildrenAndNumProducts((Category) result[0]);
      category.setNumProducts((int) (long) result[1]);
      children.put(category.getParentSerial(), category);
    }
    
    Collection<CategoryDto> rootCategories = children.get(0);
    rootCategories.forEach(rootCategory -> addChildrenRecursively(rootCategory, children));
    cumulateNumProductsRecursively(rootCategories);
        
    return rootCategories;
  }
  
  /**
   * A product that is placed in a category is also included in all parents of this category.
   * This function cumulates the number of products of a category accordingly.
   */
  private int cumulateNumProductsRecursively(Collection<CategoryDto> children) {
    int cumulatedNumProducts = 0;
    for (CategoryDto child : children) {
      int cumulatedNumProductsOfChild = child.getNumProducts();
      cumulatedNumProductsOfChild += cumulateNumProductsRecursively(child.getChildren());
      child.setNumProducts(cumulatedNumProductsOfChild);
      cumulatedNumProducts += cumulatedNumProductsOfChild;
    }
    return cumulatedNumProducts;
  }
  
  private void addChildrenRecursively(CategoryDto parent, Multimap<Integer, CategoryDto> children) {
    parent.setChildren(children.get(parent.getSerial()));
    parent.getChildren().forEach(child -> addChildrenRecursively(child, children));
  }

  private CategoryDto toDtoIgnoringChildrenAndNumProducts(Category category) {

    CategoryDto categoryDto = dtoFactory.createDto(CategoryDto.class);

    categoryDto.setName(category.getName());
    categoryDto.setSerial(category.getSerial());
    if (category.getParent() != null) {
      categoryDto.setParentSerial(category.getParent().getSerial());
    }
    
    return categoryDto;
  }

}
