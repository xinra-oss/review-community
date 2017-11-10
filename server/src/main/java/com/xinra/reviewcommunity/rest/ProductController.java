package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.auth.AccessRequires;
import com.xinra.reviewcommunity.service.ProductService;
import com.xinra.reviewcommunity.service.SearchService;
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.CreateProductDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.SerialDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController {


  /**
   * Create a new product.
   */
  @AccessRequires(Permission.CREATE_PRODUCT)
  @RequestMapping(path = "", method = RequestMethod.POST)
  public SerialDto create(@RequestBody @Valid CreateProductDto createProductDto) {
    return serviceProvider.getService(ProductService.class).createProduct(createProductDto);
  }

  /**
   * GET a product by its Serial.
   */
  @RequestMapping(path = "/{serial}", method = RequestMethod.GET)
  public ProductDto get(@PathVariable int serial) {
    return serviceProvider.getService(ProductService.class).getProductBySerial(serial);
  }
  
  /**
   * GET a list of products using full text search.
   */
  @RequestMapping(path = "", method = RequestMethod.GET)
  public List<ProductDto> getList(@RequestParam(name = "q") String query) {
    return serviceProvider.getService(SearchService.class).searchProducts(query);
  }

}
