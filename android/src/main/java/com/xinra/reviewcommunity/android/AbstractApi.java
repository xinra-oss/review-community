package com.xinra.reviewcommunity.android;


import com.xinra.reviewcommunity.shared.dto.MarketDto;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * This exists to keep the source of {@link Api} tidy. This way it only contains the public
 * methods that represent API calls.
 */
public abstract class AbstractApi {
  private RestTemplate restTemplate;
  private CompositeDisposable subscriptions;
  private AppState state;
  private MarketDto market;

  public AbstractApi(AppState state) {
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

  private <RESPONSE, REQUEST> RESPONSE performRequest(String path, HttpMethod method, Class<RESPONSE> responseType, REQUEST requestBody, boolean marketAgnostic, Map<String, ?> uriVariables) {
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
    ResponseEntity<RESPONSE> response = uriVariables == null
        ? restTemplate.exchange(url, method, entity, responseType)
        : restTemplate.exchange(url, method, entity, responseType, uriVariables);

    String newSessionCookie = response.getHeaders().getFirst("Set-Cookie");
    if (newSessionCookie != null) {
      state.sessionCookie = newSessionCookie;
    }

    return response.getBody();
  }

  /**
   * Executes a request that has a response.
   *
   * @param path
   * @param method
   * @param responseType
   * @param requestBody may be null
   * @param marketAgnostic
   * @param uriVariables may be null
   * @param <REQUEST>
   * @param <RESPONSE>
   * @return
   */
  protected <REQUEST, RESPONSE> Single<RESPONSE> withResponse(String path, HttpMethod method, Class<RESPONSE> responseType, REQUEST requestBody, boolean marketAgnostic, Map<String, ?> uriVariables) {
    return Single.<RESPONSE>create(source -> {
      try {
        source.onSuccess(performRequest(path, method, responseType, requestBody, marketAgnostic, uriVariables));
      } catch (Exception ex) {
        source.onError(ex);
      }
    }).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * Executes a request that doesn't have a response.
   *
   * @param path
   * @param method
   * @param requestBody may be null
   * @param marketAgnostic
   * @param uriVariables may be null
   * @param <REQUEST>
   * @return
   */
  protected <REQUEST> Completable withoutResponse(String path, HttpMethod method, REQUEST requestBody, boolean marketAgnostic, Map<String, ?> uriVariables) {
    return Completable.create(source -> {
      try {
        performRequest(path, method, Void.class, requestBody, marketAgnostic, uriVariables);
        source.onComplete();
      } catch (Exception ex) {
        source.onError(ex);
      }
    }).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }
}
