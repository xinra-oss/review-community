import { AuthState, authInitialState } from './auth.state';
import { Auth } from './auth.action';
import { SuccessfulAuthentication } from '../../shared/models';

export function reducer(
  state: AuthState = authInitialState,
  action: Auth.Actions
): AuthState {
  switch (action.type) {
    case Auth.ActionTypes.SET_CSRF_TOKEN:
      return (<any>Object).assign({}, state, {
        csrfToken: action.payload
      });
    
    case Auth.ActionTypes.SET_SESSION_ID:
      return (<any>Object).assign({}, state, {
        sessionId: action.payload
      });

    case Auth.ActionTypes.LOGGED_IN:
      const auth = <SuccessfulAuthentication>action.payload;
      return (<any>Object).assign({}, state, {
        initialized: action.payload,
        csrfToken: auth.csrfToken,
        authenticatedUser: auth.user,
        permissions: auth.permissions
      });

    case Auth.ActionTypes.LOGGED_OUT:
      return (<any>Object).assign({}, state, {
        authenticatedUser: undefined,
        permissions: []
      });

    default:
      return state;
  }
}
