package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.dto.BrandDto;
import com.xinra.reviewcommunity.dto.CategoryDto;
import com.xinra.reviewcommunity.dto.CreateProductDto;
import com.xinra.reviewcommunity.dto.ProductDto;
import com.xinra.reviewcommunity.entity.Brand;
import com.xinra.reviewcommunity.entity.Category;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.repo.BrandRepository;
import com.xinra.reviewcommunity.repo.CategoryRepository;
import com.xinra.reviewcommunity.repo.ProductRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class ProductService extends AbstractService {

  @Autowired
  private ProductRepository<Product> productRepo;
  @Autowired
  private CategoryRepository<Category> categoryRepo;
  @Autowired
  private BrandRepository<Brand> brandRepo;

  /**
   * Creates a new product.
   *
   * @param createProductDto {@link com.xinra.reviewcommunity.dto.CreateProductDto}
   */
  public void createProduct(@NonNull CreateProductDto createProductDto) {

    Category category = categoryRepo.findOne(createProductDto.getCategoryId());
    Brand brand = brandRepo.findOne(createProductDto.getBrandId());

    Product product = entityFactory.createEntity(Product.class);
    product.setName(createProductDto.getName());
    product.setDescription(createProductDto.getDescription());
    product.setCategory(category);
    product.setBrand(brand);

    productRepo.save(product);

    log.info("Created Product with name '{}'", createProductDto.getName());
  }

  /**
   * Finds product with given id from database.
   *
   * @param id The name of the product.
   * @return {@link com.xinra.reviewcommunity.entity.Product}
   *
   */
  public ProductDto findProductById(String id) throws IllegalArgumentException {

    ProductDto productDto = new ProductDto();
    log.info("Fetching product with id: '{}'", id);
    Product product = productRepo.findOne(id);

    if (product == null) {
      log.error("Product with id: '{}' not found.", id);
      throw new IllegalArgumentException(id);
      //TODO exceptionHandler
    }
    return fillProductDtoWithProduct(product);
  }


  private ProductDto fillProductDtoWithProduct(Product product) {

    ProductDto productDto = new ProductDto();

    productDto.setName(product.getName());
    productDto.setDescription(product.getDescription());

    BrandDto brandDto = new BrandDto();
    brandDto.setName(product.getBrand().getName());
    brandDto.setBrandId(product.getBrand().getPk().getId());

    productDto.setBrandDto(brandDto);

    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setName(product.getCategory().getName());
    categoryDto.setCategoryId(product.getCategory().getPk().getId());

    productDto.setCategoryDto(categoryDto);

    return productDto;
  }
}
