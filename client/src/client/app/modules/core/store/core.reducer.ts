import { CoreState, coreInitialState } from './core.state';
import { Core } from './core.action';
import { SuccessfulAuthentication } from '../../shared/models';

export function reducer(
  state: CoreState = coreInitialState,
  action: Core.Actions
): CoreState {
  switch (action.type) {
    case Core.ActionTypes.INITIALIZED:
      return {
        ...state,
        initialized: true
      }

    case Core.ActionTypes.HANDLE_ERROR:
      return {
        ...state,
        error: action.payload
      }
    
    case Core.ActionTypes.CLEAR_ERROR:
      return {
        ...state,
        error: undefined
      }

    default:
      return state;
  }
}
