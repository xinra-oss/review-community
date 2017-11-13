package com.xinra.reviewcommunity.android;

import com.google.common.collect.ImmutableMap;
import com.xinra.reviewcommunity.shared.OrderBy;
import com.xinra.reviewcommunity.shared.dto.CsrfTokenDto;
import com.xinra.reviewcommunity.shared.dto.InitDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.RegistrationDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;
import com.xinra.reviewcommunity.shared.dto.SerialDto;
import com.xinra.reviewcommunity.shared.dto.SuccessfulAuthenticationDto;

import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class Api extends AbstractApi {

  public Api(AppState state) {
    super(state);
  }

  public Single<CsrfTokenDto> getCsrfToken() {
    return withResponse("/csrf-token", HttpMethod.GET, CsrfTokenDto.class, null, true, null);
  }

  public Single<InitDto> getInit() {
    return withResponse("/init", HttpMethod.GET, InitDto.class, null, false, null);
  }

  public Single<SuccessfulAuthenticationDto> getSession(String usernameOrEmail, String password) {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("username", usernameOrEmail);
    parameters.add("password", password);
    return withResponse("/session", HttpMethod.POST, SuccessfulAuthenticationDto.class, parameters, true, null);
  }

  public Completable deleteSession() {
    return withoutResponse("/session", HttpMethod.DELETE, null, true, null);
  }

  public Completable createUser(RegistrationDto registration) {
    return withoutResponse("/user", HttpMethod.POST, registration, true, null);
  }

  public Single<List<ProductDto>> getProductList(String query) {
    return withResponse("/product?q={query}", HttpMethod.GET, ProductDto[].class, null, false, ImmutableMap.of("query", query))
        .map(Arrays::asList);
  }

  public Single<List<ReviewDto>> getReviewList(int productSerial,OrderBy orderBy) {
    return withResponse("/product/{productSerial}/review?orderBy={orderBy}", HttpMethod.GET, ReviewDto[].class, null, false, ImmutableMap.of("productSerial", productSerial, "orderBy", orderBy))
            .map(Arrays::asList);
  }
}
