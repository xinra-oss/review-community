import { Action } from '@ngrx/store';
import { type } from '../../core/utils/index';
import { CsrfToken, User, Permission } from '../../shared/models';

export namespace Auth {
  // Category to uniquely identify the actions
  export const CATEGORY: string = 'Auth';

  export interface AuthActions {
    SET_CSRF_TOKEN: string,
    SET_SESSION_ID: string,
    LOG_IN: string,
    SET_AUTHENTICATED_USER: string,
    SET_PERMISSIONS: string,
    LOG_OUT: string
    LOGGED_OUT: string
  }

  export const ActionTypes: AuthActions = {
    SET_CSRF_TOKEN: type(`[${CATEGORY}] Set CSRF token`),
    SET_SESSION_ID: type(`[${CATEGORY}] Set session ID`),
    LOG_IN: type(`[${CATEGORY}] Log in`),
    SET_AUTHENTICATED_USER: type(`[${CATEGORY}] Set authenticated user`),
    SET_PERMISSIONS: type(`[${CATEGORY}] Set permissions`),
    LOG_OUT: type(`[${CATEGORY}] Log out`),
    LOGGED_OUT: type(`[${CATEGORY}] Logged out`),
  };

  export class SetCsrfTokenAction implements Action {
    type = ActionTypes.SET_CSRF_TOKEN;
    constructor(public payload: CsrfToken) {}
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

  export class SetAuthenticatedUserAction implements Action {
    type = ActionTypes.SET_AUTHENTICATED_USER;
    constructor(public payload: User) {}
  }

  export class SetPermissionsAction implements Action {
    type = ActionTypes.SET_PERMISSIONS;
    constructor(public payload: Permission[]) {}
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
    | SetAuthenticatedUserAction
    | SetPermissionsAction
    | LogOutAction
    | LoggedOutAction;
}
