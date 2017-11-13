package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.entity.Brand;
import com.xinra.reviewcommunity.entity.Category;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.entity.SerialEntity;
import com.xinra.reviewcommunity.repo.BrandRepository;
import com.xinra.reviewcommunity.repo.CategoryRepository;
import com.xinra.reviewcommunity.repo.ProductRepository;
import com.xinra.reviewcommunity.shared.dto.BrandDto;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;
import com.xinra.reviewcommunity.shared.dto.CreateProductDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.SerialDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
    product.setBarcode(createProductDto.getBarcode());
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
   * Returns the product with the given barcode.
   */
  public ProductDto getProductByBarcode(String barcode) {
    Product product = productRepo.findByBarcode(barcode);

    if (product == null) {
      throw new BarcodeNotFoundException(Product.class, barcode);
    }
    return toDto(product);
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

  public ProductDto toDto(Product product) {

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

    productDto.setCategorySerial(product.getCategory().getSerial());

    return productDto;
  }
}

final class BarcodeNotFoundException extends RuntimeException {


  private static final long serialVersionUID = 1L;

  /**
   * Thrown if product cannot be found by the given barcode.
   */
  public BarcodeNotFoundException(Class<? extends SerialEntity> type, String barcode) {
    super("There is no entity of type " + type.getSimpleName() + " with barcode " + barcode);
  }
}

