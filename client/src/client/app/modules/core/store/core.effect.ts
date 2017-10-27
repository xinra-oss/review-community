// angular
import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

// libs
import { Store, Action } from '@ngrx/store';
import { Effect, Actions } from '@ngrx/effects';
import { Observable } from 'rxjs/Observable';
import * as _ from 'lodash';

// module
import { AppService } from '../services';
import { Core } from './';
import { Auth } from '../../auth';
import { LogService, ApiService } from '../';
import { Init } from '../../shared/models';

@Injectable()
export class CoreEffects {

  /**
   * This effect makes use of the `startWith` operator to trigger
   * the effect immediately on startup.
   */
  @Effect() init$: Observable<Action> = this.actions$
    .ofType(Core.ActionTypes.INIT)
    .startWith(new Core.InitAction())
    .switchMap(() => this.api.get<Init>('/init'))
    .mergeMap(init => {
      const actions : Action[] = [
        new Auth.SetCsrfTokenAction(init.csrfToken),
        new Core.SetAvailableMarketsAction(init.markets)
      ]
      if (!_.isNil(init.authenticatedUser)) {
        actions.push(new Auth.SetAuthenticatedUserAction(init.authenticatedUser));
        actions.push(new Auth.SetPermissionsAction(init.permissions));
      }
      return actions;
    })
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
