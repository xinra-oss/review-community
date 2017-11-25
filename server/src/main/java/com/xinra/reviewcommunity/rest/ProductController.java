package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.auth.AccessRequires;
import com.xinra.reviewcommunity.service.BarcodeService;
import com.xinra.reviewcommunity.service.BarcodeService.BarcodeAlreadyExistsException;
import com.xinra.reviewcommunity.service.ProductService;
import com.xinra.reviewcommunity.service.SearchService;
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.CreateProductDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.SerialDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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
  public SerialDto create(@RequestBody @Valid CreateProductDto createProductDto,
      BindingResult result) throws BindException {
    
    if (result.hasErrors()) {
      throw new BindException(result);
    }
    try {
      return serviceProvider.getService(ProductService.class).createProduct(createProductDto);
    } catch (BarcodeAlreadyExistsException ex) {
      result.rejectValue("barcode", "AlreadyExists");
      throw new BindException(result);
    }
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
  @RequestMapping(path = "", params = "q", method = RequestMethod.GET)
  public List<ProductDto> getList(@RequestParam(name = "q") String query) {
    return serviceProvider.getService(SearchService.class).searchProducts(query);
  }
  
  /**
   * GET a list of products in the given category.
   */
  @RequestMapping(path = "", params = "category", method = RequestMethod.GET)
  public List<ProductDto> getByCategory(@RequestParam(name = "category") int categorySerial) {
    return serviceProvider.getService(ProductService.class).getProductsByCategory(categorySerial);
  }

  /**
   * GET a product serial by its barcode.
   */
  @RequestMapping(path = "/serial", params = "barcode", method = RequestMethod.GET)
  public int getByBarcode(@RequestParam String barcode) {
    return serviceProvider.getService(BarcodeService.class).getProductSerial(barcode);
  }
}
