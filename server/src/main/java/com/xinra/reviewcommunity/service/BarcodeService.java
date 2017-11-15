package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.entity.Barcode;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.repo.BarcodeRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class BarcodeService extends AbstractService {

  public static class BarcodeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private BarcodeNotFoundException(String code) {
      super("There is no product with barcode " + code);
    }
  }

  public static class ProductOutOfScopeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ProductOutOfScopeException(String code) {
      super("The product with barcode " + code + " is out of scope!");
    }
  }

  public static class BarcodeAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private final int productSerial;

    private BarcodeAlreadyExistsException(String code, Product product) {
      super(
          "The barcode " + code + " already belongs to product with serial " + product.getSerial());
      this.productSerial = product.getSerial();
    }
  }

  private @Autowired BarcodeRepository<Barcode> barcodeRepo;

  /**
   * Returns the serial of the product with the given barcode.
   * 
   * @throws BarcodeNotFoundException
   *           if the barcode is unknown to this application
   * @throws ProductOutOfScopeException
   *           if the given barcode is not in the scope of this application
   */
  public int getProductSerial(@NonNull String code) {
    final Barcode barcode = barcodeRepo.findByCode(code);

    if (barcode == null) {
      throw new BarcodeNotFoundException(code);
    }

    if (barcode.getProduct() == null) {
      throw new ProductOutOfScopeException(code);
    }

    return barcode.getProduct().getSerial();
  }

  /**
   * Associates the given barcode with the given product.
   * 
   * @throws BarcodeAlreadyExistsException
   *           if the barcode already belongs to another product
   * @throws ProductOutOfScopeException
   *           if the given barcode is not in the scope of this application
   */
  public void setBarcodeOfProduct(@NonNull Product product, @NonNull String code) {
    Barcode barcode = barcodeRepo.findByCode(code);

    if (barcode != null) {
      if (barcode.getProduct() == null) {
        throw new ProductOutOfScopeException(code);
      } else if (product.equals(barcode.getProduct())) {
        return; // barcode already belongs to this product
      } else {
        throw new BarcodeAlreadyExistsException(code, barcode.getProduct());
      }
    }

    barcode = barcodeRepo.findByProduct(product);

    if (barcode == null) {
      barcode = entityFactory.createEntity(Barcode.class);
      barcode.setProduct(product);
    }

    barcode.setCode(code);
    barcodeRepo.save(barcode);
    log.info("Set barcode of {} to {}", product, barcode);
  }

  /**
   * Deletes a barcode.
   * 
   * @throws BarcodeNotFoundException
   *           if the carcode does not exist.
   */
  public void deleteBarcode(@NonNull String code) {
    Barcode barcode = barcodeRepo.findByCode(code);

    if (barcode == null) {
      throw new BarcodeNotFoundException(code);
    }

    barcodeRepo.delete(barcode);
    log.info("Deleted barcode {}", barcode);
  }

  /**
   * Marks the given barcode as out of scope of this application.
   * 
   * @throws BarcodeAlreadyExistsException
   *           if the given barcode is already associated with a product
   */
  public void makeBarcodeOutOfScope(@NonNull String code) {
    Barcode barcode = barcodeRepo.findByCode(code);

    if (barcode != null) {
      if (barcode.getProduct() != null) {
        throw new BarcodeAlreadyExistsException(code, barcode.getProduct());
      }
      return; // barcode is already out of scope
    }

    barcode = entityFactory.createEntity(Barcode.class);
    barcode.setCode(code);
    barcodeRepo.save(barcode);
    log.info("Marked barcode {} as out of scope", barcode);
  }
  
  /**
   * Checks that the given barcode is unknown to the application. Must be called from within the
   * transaction that relies on the result of this check!
   * 
   * @throws BarcodeAlreadyExistsException
   *           if the given barcode already exists
   */
  @Transactional(propagation = Propagation.MANDATORY)
  public void checkBarcodeDoesNotExist(@NonNull String code) {
    Barcode barcode = barcodeRepo.findByCode(code);
    if (barcode != null) {
      throw new BarcodeAlreadyExistsException(code, barcode.getProduct());
    }
  }

}
