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
    
    case Core.ActionTypes.CLEAR_API_ERROR:
      return {
        ...state,
        apiError: undefined
      }

    default:
      return state;
  }
}
