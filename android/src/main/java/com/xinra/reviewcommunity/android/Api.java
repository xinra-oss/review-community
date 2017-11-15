package com.xinra.reviewcommunity.android;

import com.google.common.collect.ImmutableMap;
import com.xinra.reviewcommunity.shared.OrderBy;
import com.xinra.reviewcommunity.shared.dto.CreateReviewDto;
import com.xinra.reviewcommunity.shared.dto.CsrfTokenDto;
import com.xinra.reviewcommunity.shared.dto.InitDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.RegistrationDto;
import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
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

  public Single<ProductDto> getProductDto(int productSerial) {
    return withResponse("/product/{serial}", HttpMethod.GET, ProductDto.class, null, false, ImmutableMap.of("serial", productSerial));

  }

  public Single<List<ProductDto>> getProductList(String query) {
    return withResponse("/product?q={query}", HttpMethod.GET, ProductDto[].class, null, false, ImmutableMap.of("query", query))
        .map(Arrays::asList);
  }

  public Single<List<ProductDto>> getCategory(int categorySerial) {
    return withResponse("/category/{serial}", HttpMethod.GET, ProductDto[].class, null, false, ImmutableMap.of("serial", categorySerial))
        .map(Arrays::asList);
  }

  public Single<List<ReviewDto>> getReviewList(int productSerial, OrderBy orderBy) {
    return withResponse("/product/{productSerial}/review?orderBy={orderBy}", HttpMethod.GET, ReviewDto[].class, null, false, ImmutableMap.of("productSerial", productSerial, "orderBy", orderBy))
        .map(Arrays::asList);
  }

  public Single<List<ReviewCommentDto>> getCommentList(int productSerial, int reviewSerial) {
    return withResponse("/product/{productSerial}/review/{reviewSerial}/comment", HttpMethod.GET, ReviewCommentDto[].class, null, false, ImmutableMap.of("productSerial", productSerial, "reviewSerial", reviewSerial))
        .map(Arrays::asList);
  }

  public Completable createReview(CreateReviewDto createReviewDto, int productSerial) {
    return withoutResponse("/product/{productSerial}/review", HttpMethod.POST, createReviewDto, false, ImmutableMap.of("productSerial", productSerial));
  }

  public Single<Integer> getProductSerialByBarcode(String barcode) {
    return withResponse("/product/serial?barcode={barcode}", HttpMethod.GET, Integer.class, null, false, ImmutableMap.of("barcode", barcode));
  }

}
