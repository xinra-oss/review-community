package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.dto.CreateBrandDto;
import com.xinra.reviewcommunity.service.BrandService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brand")
public class BrandController extends AbstractController {

  /**
   * Creates a new Brand.
   */
  @RequestMapping(path = "", method = RequestMethod.POST)
  public void create(@RequestBody @Valid CreateBrandDto createBrandDto) {
    serviceProvider.getService(BrandService.class).createBrand(createBrandDto);
  }
}
