package com.xinra.reviewcommunity.android;

import com.google.common.collect.ImmutableMap;
import com.xinra.reviewcommunity.shared.dto.CsrfTokenDto;
import com.xinra.reviewcommunity.shared.dto.InitDto;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.RegistrationDto;
import com.xinra.reviewcommunity.shared.dto.SuccessfulAuthenticationDto;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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

  public Single<List<ProductDto>> search(String query) {
    return withResponse("/product", HttpMethod.GET, ProductDto[].class, null, false, ImmutableMap.of("q", query))
        .map(Arrays::asList);
  }
}
