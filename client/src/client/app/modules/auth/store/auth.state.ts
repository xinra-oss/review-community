import { Observable } from 'rxjs/Observable';
import * as _ from 'lodash';

import { CsrfToken, User, Permission } from '../../shared/models';

export interface AuthState {
  csrfToken: CsrfToken;
  authenticatedUser: User;
  /**
   * This is used in the mobile client to store the session ID. For the web client this is managed
   * by the browser (there we can't even access the session ID for security reasons).
   */
  sessionId: string;
  permissions: Permission[];
}

export const authInitialState: AuthState = {
  csrfToken: undefined,
  authenticatedUser: undefined,
  sessionId: undefined,
  permissions: []
};

// selectors

export function getAuthenticatedUser(state$: Observable<AuthState>) {
  return state$.select(state => state.authenticatedUser);
}

export function isAuthenticated(state$: Observable<AuthState>) {
  return state$.select(state => !_.isNil(state.authenticatedUser));
}