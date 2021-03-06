package com.xinra.reviewcommunity.rest.conf;

import com.xinra.reviewcommunity.Context;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used on a controller class or method to indicate that it is scoped to the whole application.
 * Market will not be available in {@link Context}.
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MarketAgnostic {}
