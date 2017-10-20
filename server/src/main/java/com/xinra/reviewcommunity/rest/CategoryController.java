package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.dto.CategoryDto;
import com.xinra.reviewcommunity.dto.CreateCategoryDto;
import com.xinra.reviewcommunity.service.CategoryService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController extends AbstractController {

  /**
   * Create a new category.
   */
  @RequestMapping(path = "", method = RequestMethod.POST)
  public void create(@RequestBody @Valid CreateCategoryDto createCategoryDto) {
    serviceProvider.getService(CategoryService.class).createCategory(createCategoryDto);

  }

  /**
   * GET a list of all categories.
   */
  @RequestMapping(path = "", method = RequestMethod.GET)
  public List<CategoryDto> get() {
    return serviceProvider.getService(CategoryService.class).getAllCategories();
  }
}
