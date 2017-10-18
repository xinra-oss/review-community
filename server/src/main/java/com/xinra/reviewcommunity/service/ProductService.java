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

  private @Autowired ProductRepository<Product> productRepo;
  private @Autowired CategoryRepository<Category> categoryRepo;
  private @Autowired BrandRepository<Brand> brandRepo;

  /**
   * Creates a new product.
   */
  public void createProduct(@NonNull CreateProductDto createProductDto) {

    Category category = categoryRepo.findBySerial(createProductDto.getCategorySerial());
    Brand brand = brandRepo.findBySerial(createProductDto.getBrandSerial());

    Product product = entityFactory.createEntity(Product.class);
    product.setName(createProductDto.getName());
    product.setDescription(createProductDto.getDescription());
    product.setCategory(category);
    product.setBrand(brand);

    productRepo.save(product);

    log.info("Created Product with name '{}'", createProductDto.getName());
  }

  /**
   * Returns the product with the given ID.
   */
  public ProductDto getProductBySerial(int serial) {

    Product product = productRepo.findBySerial(serial);

    if (product == null) {
      throw new SerialNotFoundException(Product.class, serial);
    }
    return toDto(product);
  }


  private ProductDto toDto(Product product) {

    ProductDto productDto = dtoFactory.createDto(ProductDto.class);

    productDto.setName(product.getName());
    productDto.setDescription(product.getDescription());

    BrandDto brandDto = dtoFactory.createDto(BrandDto.class);
    brandDto.setName(product.getBrand().getName());
    brandDto.setSerial(product.getBrand().getSerial());

    productDto.setBrand(brandDto);

    CategoryDto categoryDto = dtoFactory.createDto(CategoryDto.class);
    categoryDto.setName(product.getCategory().getName());
    categoryDto.setSerial(product.getCategory().getSerial());

    productDto.setCategory(categoryDto);

    return productDto;
  }
}