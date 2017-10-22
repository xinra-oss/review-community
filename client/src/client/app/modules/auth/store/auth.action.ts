import { Action } from '@ngrx/store';
import { type } from '../../core/utils/index';
import { CsrfToken, SuccessfulAuthentication } from '../../shared/models';

export namespace Auth {
  // Category to uniquely identify the actions
  export const CATEGORY: string = 'Auth';

  export interface AuthActions {
    SET_CSRF_TOKEN: string,
    SET_SESSION_ID: string,
    LOG_IN: string,
    LOGGED_IN: string,
    LOG_OUT: string
    LOGGED_OUT: string
  }

  export const ActionTypes: AuthActions = {
    SET_CSRF_TOKEN: type(`[${CATEGORY}] Set CSRF token`),
    SET_SESSION_ID: type(`[${CATEGORY}] Set session ID`),
    LOG_IN: type(`[${CATEGORY}] Log in`),
    LOGGED_IN: type(`[${CATEGORY}] Logged in`),
    LOG_OUT: type(`[${CATEGORY}] Log out`),
    LOGGED_OUT: type(`[${CATEGORY}] Logged out`),
  };

  export class SetCsrfTokenAction implements Action {
    type = ActionTypes.SET_CSRF_TOKEN;
    constructor(public payload: string) {}
  }

  export class SetSessionIdAction implements Action {
    type = ActionTypes.SET_SESSION_ID;
    constructor(public payload: string) {}
  }

  export class LogInAction implements Action {
    type = ActionTypes.LOG_IN;
    constructor(public payload: {
      username: string,
      password: string
    }) {}
  }

  export class LoggedInAction implements Action {
    type = ActionTypes.LOGGED_IN;
    constructor(public payload: SuccessfulAuthentication) {}
  }

  export class LogOutAction implements Action {
    type = ActionTypes.LOG_OUT;
    payload = null;
  }

  export class LoggedOutAction implements Action {
    type = ActionTypes.LOGGED_OUT;
    payload = null;
  }

  export type Actions
    = SetCsrfTokenAction
    | SetSessionIdAction
    | LogInAction
    | LoggedInAction
    | LogOutAction
    | LoggedOutAction;
}
