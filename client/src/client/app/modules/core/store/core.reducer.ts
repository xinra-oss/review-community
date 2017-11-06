import { CoreState, coreInitialState } from './core.state';
import { Core } from './core.action';
import { SuccessfulAuthentication } from '../../shared/models';

export function reducer(
  state: CoreState = coreInitialState,
  action: Core.Actions
): CoreState {
  switch (action.type) {
    case Core.ActionTypes.INITIALIZED:
      return (<any>Object).assign({}, state, {
        initialized: action.payload
      });

    case Core.ActionTypes.HANDLE_ERROR:
      return (<any>Object).assign({}, state, {
        error: action.payload
      });
    
    case Core.ActionTypes.CLEAR_ERROR:
      return (<any>Object).assign({}, state, {
        error: undefined
      });

    case Core.ActionTypes.SET_MARKET:
      return (<any>Object).assign({}, state, {
        market: action.payload
      });

    case Core.ActionTypes.SET_AVAILAVLE_MARKETS:
      return (<any>Object).assign({}, state, {
        availableMarkets: action.payload
      });

    default:
      return state;
  }
}
