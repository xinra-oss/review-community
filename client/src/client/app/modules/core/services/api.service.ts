// angular
import { Injectable } from '@angular/core';
import { Http, RequestMethod, RequestOptionsArgs, Headers } from '@angular/http';

// libs
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import * as _ from 'lodash';

// app
import { Config, LogService, Core } from '../../core';
import { AppState } from '../../ngrx';
import { Market, CsrfToken } from '../../shared/models';

// module
//import { NameList } from '../actions/index';

declare type Params = string
                    | URLSearchParams
                    | { [key: string]: any | any[]; }
                    | null;

const MARKET_AGNOSTIC_ENDPOINTS = [
  "/csrf-token"
];

/**
 * This service is used to communitcate with the server through its REST API.
 */
@Injectable()
export class ApiService {

  private market: Market = {pk: undefined, slug: "de"};
  private csrfToken: CsrfToken;
  private sessionCookie: string;

  constructor(
    private http: Http,
    //private log: LogService, // doesn't work somehow
    private store: Store<AppState>
  ) {
    this.store.subscribe(state => {
      this.market = state.core.market;
      this.csrfToken = state.auth.csrfToken;
      this.sessionCookie = "JSESSIONID=" + state.auth.sessionId;
    });
  }

  get<T>(path: string, params?: Params): Observable<T> {
    return this.handleRequest(path, RequestMethod.Get, params);
  }

  post<T>(path: string, params?: Params, body?: any): Observable<T> {
    return this.handleRequest(path, RequestMethod.Post, params, body);
  }

  put<T>(path: string, params?: Params, body?: any): Observable<T> {
    return this.handleRequest(path, RequestMethod.Put, params, body);
  }

  delete<T>(path: string, params?: Params): Observable<T> {
    return this.handleRequest(path, RequestMethod.Delete, params);
  }

  private handleRequest<T>(path: string, method: RequestMethod, params?: Params, body?: any): Observable<T> {
    
    if (!_.includes(MARKET_AGNOSTIC_ENDPOINTS, path)) {
      path = "/" + this.market.slug + path;
    }
    path = Config.ENVIRONMENT().API + path;

    const headers = new Headers();

    if (this.csrfToken != undefined) {
      headers.append(this.csrfToken.headerName, this.csrfToken.token);
    }

    if (this.sessionCookie != undefined) {
      headers.append("Cookie", this.sessionCookie);
    }

    const options: RequestOptionsArgs = {
      method: method,
      params: params,
      body: body,
      headers: headers
    }

    return this.http.request(path, options)
      .map(response => response.json())
      .catch(error => {
        this.store.dispatch(new Core.HandleErrorAction(error));
        throw error;
      });
  }

}