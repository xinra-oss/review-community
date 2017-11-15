package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Barcode;
import com.xinra.reviewcommunity.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface BarcodeRepository<T extends Barcode> extends AbstractEntityRepository<T> {

  T findByCode(String code);
  
  T findByProduct(Product product);
  
}
