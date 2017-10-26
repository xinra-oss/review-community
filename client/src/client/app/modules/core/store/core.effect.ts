// angular
import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

// libs
import { Store, Action } from '@ngrx/store';
import { Effect, Actions } from '@ngrx/effects';
import { Observable } from 'rxjs/Observable';

// module
import { AppService } from '../services';
import { Core } from './';
import { Auth } from '../../auth';
import { LogService } from '../';

@Injectable()
export class CoreEffects {

  /**
   * This effect makes use of the `startWith` operator to trigger
   * the effect immediately on startup.
   */
  @Effect() init$: Observable<Action> = this.actions$
    .ofType(Core.ActionTypes.INIT)
    .startWith(new Core.InitAction())
    .switchMap(() => {
      return this.http.get('http://192.168.42.215:8080/api/csrf-token')
        .map(res => res.json());
    })
    .map(csrfToken => {
      this.log.info(JSON.stringify(csrfToken));
      return new Auth.SetCsrfTokenAction(csrfToken);
    })
    // .merge(csrfToken => {
    //   return [new Auth.SetCsrfTokenAction(csrfToken)];
    // })
    .concat(() => new Core.InitializedAction())
    .catch((error) => Observable.of(new Core.HandleErrorAction(error)));

  constructor(
    private store: Store<any>,
    private actions$: Actions,
    private http: Http,
    private log: LogService
  ) { }
}
