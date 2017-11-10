package com.xinra.reviewcommunity.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SearchService extends AbstractService {
  
  @PersistenceContext
  private EntityManager entityManager;
  
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

}
