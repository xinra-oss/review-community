package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.dto.BrandDto;
import com.xinra.reviewcommunity.dto.CategoryDto;
import com.xinra.reviewcommunity.dto.CreateProductDto;
import com.xinra.reviewcommunity.dto.ProductDto;
import com.xinra.reviewcommunity.dto.SerialDto;
import com.xinra.reviewcommunity.entity.Brand;
import com.xinra.reviewcommunity.entity.Category;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.repo.BrandRepository;
import com.xinra.reviewcommunity.repo.CategoryRepository;
import com.xinra.reviewcommunity.repo.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
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
  public SerialDto createProduct(@NonNull CreateProductDto createProductDto) {

    Category category = categoryRepo.findBySerial(createProductDto.getCategorySerial());

    if (category == null) {
      throw new SerialNotFoundException(Category.class, createProductDto.getCategorySerial());
    }

    Brand brand = brandRepo.findBySerial(createProductDto.getBrandSerial());
    if (brand == null) {
      throw new SerialNotFoundException(Brand.class, createProductDto.getBrandSerial());
    }

    Product product = entityFactory.createEntity(Product.class);
    product.setName(createProductDto.getName());
    product.setDescription(createProductDto.getDescription());
    product.setCategory(category);
    product.setBrand(brand);

    int serial = serviceProvider.getService(SerialService.class).getNextSerial(Product.class);
    product.setSerial(serial);
    productRepo.save(product);

    log.info("Created Product with name '{}'", createProductDto.getName());

    SerialDto serialDto = dtoFactory.createDto(SerialDto.class);
    serialDto.setSerial(serial);

    return serialDto;
  }

  /**
   * Returns the product with the given Serial.
   */
  public ProductDto getProductBySerial(int serial) {

    Product product = productRepo.findBySerial(serial);

    if (product == null) {
      throw new SerialNotFoundException(Product.class, serial);
    }
    return toDto(product);
  }

  /**
   * Returns a list of all products of a brand.
   **/
  public List<ProductDto> getProductsByBrand(int serial) {
    List<ProductDto> list = productRepo.findProductsByBrandSerial(serial).stream()
        .map(this::toDto)
        .collect(Collectors.toList());

    if (list.isEmpty()) {
      throw new SerialNotFoundException(Brand.class, serial);
    }
    return list;
  }

  /**
   * Returns a list of all products of a category.
   */
  public List<ProductDto> getProductsByCategory(int serial) {
    List<ProductDto> list = productRepo.findProductsByCategorySerial(serial).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
    if (list.isEmpty()) {
      throw new SerialNotFoundException(Category.class, serial);
    }
    return list;
  }

  private ProductDto toDto(Product product) {

    ProductDto productDto = dtoFactory.createDto(ProductDto.class);

    productDto.setSerial(product.getSerial());
    productDto.setName(product.getName());
    productDto.setDescription(product.getDescription());
    productDto.setNumRatings(product.getNumRatings());
    productDto.setAvgRating(product.getAvgRating());

    BrandDto brandDto = dtoFactory.createDto(BrandDto.class);
    brandDto.setName(product.getBrand().getName());
    brandDto.setSerial(product.getBrand().getSerial());

    productDto.setBrand(brandDto);

    CategoryDto categoryDto = dtoFactory.createDto(CategoryDto.class);
    categoryDto.setSerial(product.getCategory().getSerial());

    productDto.setCategory(categoryDto);

    return productDto;
  }
}
