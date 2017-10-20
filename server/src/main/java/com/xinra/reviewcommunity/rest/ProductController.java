package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.dto.CreateProductDto;
import com.xinra.reviewcommunity.dto.ProductDto;
import com.xinra.reviewcommunity.dto.SerialDto;
import com.xinra.reviewcommunity.service.ProductService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController extends AbstractController {


  /**
   * Create a new product.
   */
  @RequestMapping(path = "", method = RequestMethod.POST)
  public SerialDto create(@RequestBody @Valid CreateProductDto createProductDto) {
    return serviceProvider.getService(ProductService.class).createProduct(createProductDto);
  }

  /**
   * GET a product by it's Serial.
   */
  @RequestMapping(path = "/{serial}", method = RequestMethod.GET)
  public ProductDto get(@PathVariable int serial) {
    return serviceProvider.getService(ProductService.class).getProductBySerial(serial);
  }


}
