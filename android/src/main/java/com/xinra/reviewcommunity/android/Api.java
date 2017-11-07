package com.xinra.reviewcommunity.android;

import com.xinra.reviewcommunity.shared.dto.CsrfTokenDto;
import com.xinra.reviewcommunity.shared.dto.InitDto;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
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

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Api {

  private RestTemplate restTemplate;
  private CompositeDisposable subscriptions;
  private AppState state;
  private MarketDto market;

  public Api(AppState state) {
    this.state = state;
    subscriptions = new CompositeDisposable();
    subscriptions.add(state.market.subscribe(m -> market = m));

    restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    subscriptions.dispose();
  }

  private <RESPONSE, REQUEST> RESPONSE performRequest(String path, HttpMethod method, Class<RESPONSE> responseType, REQUEST requestBody, boolean marketAgnostic) {
    final String url = "http://192.168.0.11:8080"
        + (marketAgnostic ? "" : "/de")
        + "/api" + path;

    HttpHeaders headers = new HttpHeaders();
    if (state.csrfToken != null) {
      headers.set(state.csrfToken.getHeaderName(), state.csrfToken.getToken());
    }
    if (state.sessionCookie != null) {
      headers.set("Cookie", state.sessionCookie);
    }
    if (requestBody instanceof MultiValueMap) {
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    HttpEntity<REQUEST> entity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<RESPONSE> response = restTemplate.exchange(url, method, entity, responseType);

    String newSessionCookie = response.getHeaders().getFirst("Set-Cookie");
    if (newSessionCookie != null) {
      state.sessionCookie = newSessionCookie;
    }

    return response.getBody();
  }

  private <REQUEST, RESPONSE> Single<RESPONSE> withResponse(String path, HttpMethod method, Class<RESPONSE> responseType, REQUEST requestBody, boolean marketAgnostic) {
    return Single.<RESPONSE>create(source -> {
      try {
        source.onSuccess(performRequest(path, method, responseType, requestBody, marketAgnostic));
      } catch (Exception ex) {
        source.onError(ex);
      }
    })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  private <REQUEST> Completable withoutResponse(String path, HttpMethod method, REQUEST requestBody, boolean marketAgnostic) {
    return Completable.create(source -> {
      try {
        performRequest(path, method, Void.class, requestBody, marketAgnostic);
        source.onComplete();
      } catch (Exception ex) {
        source.onError(ex);
      }
    })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  // ### ENDPOINT METHODS ###

  public Single<CsrfTokenDto> getCsrfToken() {
    return withResponse("/csrf-token", HttpMethod.GET, CsrfTokenDto.class, null, true);
  }

  public Single<InitDto> getInit() {
    return withResponse("/init", HttpMethod.GET, InitDto.class, null, false);
  }

  public Single<SuccessfulAuthenticationDto> getSession(String usernameOrEmail, String password) {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("username", usernameOrEmail);
    parameters.add("password", password);
    return withResponse("/session", HttpMethod.POST, SuccessfulAuthenticationDto.class, parameters, true);
  }

  public Completable deleteSession() {
    return withoutResponse("/session", HttpMethod.DELETE, null, true);
  }

  public Completable createUser(RegistrationDto registration) {
    return withoutResponse("/user", HttpMethod.POST, registration, true);
  }
}
