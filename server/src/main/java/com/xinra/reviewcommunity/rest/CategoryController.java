package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.dto.CreateCategoryDto;
import com.xinra.reviewcommunity.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

  /**
   * Create a new category.
   */
  @RequestMapping(path = "", method = RequestMethod.POST)
  public void create(CreateCategoryDto createCategoryDto) {
    serviceProvider.getService(CategoryService.class).createCategory(createCategoryDto);
  }
}
