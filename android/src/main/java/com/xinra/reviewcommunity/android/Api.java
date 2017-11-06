package com.xinra.reviewcommunity.android;

import android.content.Context;

import com.xinra.reviewcommunity.shared.dto.CsrfTokenDto;
import com.xinra.reviewcommunity.shared.dto.InitDto;
import com.xinra.reviewcommunity.shared.dto.MarketDto;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Api {

  private CompositeDisposable subscriptions;
  private AppState state;
  private MarketDto market;
  private Context context;

  public Api(AppState state, Context context) {
    this.state = state;
    this.context = context;
    subscriptions = new CompositeDisposable();
    subscriptions.add(state.market.subscribe(m -> market = m));
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    subscriptions.dispose();
  }

  private <T> Single<T> get(String path, Class<T> responseType) {
    return Single.<T>create(source -> {
      try {
        final String url = "http://192.168.0.11:8080/de/api" + path;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        T response = restTemplate.getForObject(url, responseType);
        source.onSuccess(response);
      } catch (Exception ex) {
        source.onError(ex);
      }
    })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Single<InitDto> getInitData() {
    return get("/init", InitDto.class);
 }
}
