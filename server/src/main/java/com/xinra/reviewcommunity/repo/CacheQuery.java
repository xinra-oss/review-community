package com.xinra.reviewcommunity.repo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;

/**
 * Add this to Spring Data JPA repository methods to enable query cache.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
public @interface CacheQuery {}
