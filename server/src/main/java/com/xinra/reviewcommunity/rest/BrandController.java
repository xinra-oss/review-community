package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.auth.AccessRequires;
import com.xinra.reviewcommunity.service.BrandService;
import com.xinra.reviewcommunity.service.ProductService;
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.BrandDto;
import com.xinra.reviewcommunity.shared.dto.CreateBrandDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;

import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
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
  @AccessRequires(Permission.CREATE_BRAND)
  @RequestMapping(path = "", method = RequestMethod.POST)
  public void create(@RequestBody @Valid CreateBrandDto createBrandDto) {
    serviceProvider.getService(BrandService.class).createBrand(createBrandDto);
  }

  /**
   * GET a list of all Brands.
   */
  @RequestMapping(path = "", method = RequestMethod.GET)
  public List<BrandDto> get() {
    return serviceProvider.getService(BrandService.class).getAllBrands();
  }

  /**
   * GET a list of all products of a brand.
   */
  @RequestMapping(path = "/{serial}", method = RequestMethod.GET)
  public List<ProductDto> getByBrand(@PathVariable int serial) {
    return serviceProvider.getService(ProductService.class).getProductsByBrand(serial);
  }

}
