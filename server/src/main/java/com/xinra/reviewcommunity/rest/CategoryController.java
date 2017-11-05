package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.auth.AccessRequires;
import com.xinra.reviewcommunity.service.CategoryService;
import com.xinra.reviewcommunity.service.ProductService;
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;
import com.xinra.reviewcommunity.shared.dto.CreateCategoryDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.SerialDto;

import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
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
  @AccessRequires(Permission.CREATE_CATEGORY)
  @RequestMapping(path = "", method = RequestMethod.POST)
  public SerialDto create(@RequestBody @Valid CreateCategoryDto createCategoryDto) {
    return serviceProvider.getService(CategoryService.class).createCategory(createCategoryDto);
  }

  /**
   * GET a list of all categories.
   */
  @RequestMapping(path = "", method = RequestMethod.GET)
  public List<CategoryDto> get() {
    return serviceProvider.getService(CategoryService.class).getAllCategories();
  }


  /**
   * GET a list of all products of a category.
   */
  @RequestMapping(path = "/{serial}", method = RequestMethod.GET)
  public List<ProductDto> getByCategory(@PathVariable int serial) {
    return serviceProvider.getService(ProductService.class).getProductsByCategory(serial);
  }
}
