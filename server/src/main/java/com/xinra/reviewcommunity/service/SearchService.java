package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.shared.dto.BrandDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SearchService extends AbstractService {
  
  private @PersistenceContext EntityManager entityManager;
  
  /**
   * Rebuilds the Lucene index to make sure it is in sync with the database.
   */
  public void rebuildIndex() {
    log.info("Start building Lucene index");
    try {
      Search.getFullTextEntityManager(entityManager).createIndexer().startAndWait();
    } catch (InterruptedException ex) {
      log.error("Building Lucene index was interrupted", ex);
    }
    log.info("Finished building Lucene index");
  }
  
  /**
   * Searches for products in the Lucene index.
   */
  public List<ProductDto> searchProducts(String queryString) {
    
    final FullTextEntityManager fullTextEntityManager 
        = Search.getFullTextEntityManager(entityManager);
    
    final QueryBuilder queryBuilder = fullTextEntityManager
        .getSearchFactory()
        .buildQueryBuilder()
        .forEntity(Product.class)
        .get();
    
    final Query luceneQuery = queryBuilder
        .keyword()
        .onFields("name", "description", "category.name", "brand.name")
        .matching(queryString)
        .createQuery();
    
    final FullTextQuery query = fullTextEntityManager.createFullTextQuery(luceneQuery);
    query.setProjection("name", "description", "category.serial", "brand.name", "brand.serial",
        "avgRating", "numRatings", "serial");
    
    @SuppressWarnings("unchecked")
    List<Object[]> results = query.getResultList();
    
    return results.stream().map(result -> {
      ProductDto product = dtoFactory.createDto(ProductDto.class);
      BrandDto brand = dtoFactory.createDto(BrandDto.class);
      
      brand.setName((String) result[3]);
      brand.setSerial((int) result[4]);
      product.setBrand(brand);
      
      product.setName((String) result[0]);
      product.setDescription((String) result[1]);
      product.setCategorySerial((int) result[2]);
      product.setAvgRating((double) result[5]);
      product.setNumRatings((int) result[6]);
      product.setSerial((int) result[7]);

      return product;
    }).collect(Collectors.toList());
  }

}
