package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.dto.CreateProductDto;
import com.xinra.reviewcommunity.dto.ProductDto;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.rest.conf.MarketAgnostic;
import com.xinra.reviewcommunity.service.ProductService;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractController {


  /**
  * Creates a new product.
  *
  * @param createProductDto {@link com.xinra.reviewcommunity.dto.CreateProductDto}
  */
  @RequestMapping(path = "", method = RequestMethod.POST)
  public void createProduct(CreateProductDto createProductDto) {

    serviceProvider.getService(ProductService.class)
            .createProduct(createProductDto);
  }

  @RequestMapping(path = "/{id}", method = RequestMethod.GET)
  public ProductDto findProductDtoById(@PathVariable String id) {
    return serviceProvider.getService(ProductService.class)
        .findProductById(id);
  }
}
