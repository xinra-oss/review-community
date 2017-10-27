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
import { LogService, ApiService } from '../';
import { CsrfToken } from '../../shared/models';

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
      return this.api.get<CsrfToken>('/csrf-token');
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
    private log: LogService,
    private api: ApiService
  ) { }
}
