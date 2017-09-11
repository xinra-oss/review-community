package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.entity.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService extends AbstractService {

  @SuppressWarnings("unused")
  @Autowired
  private ProductRepository<Product> productRepo;
  
}
