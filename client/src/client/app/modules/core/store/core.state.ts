import { Observable } from 'rxjs/Observable';
import * as _ from 'lodash';

import { ApiError, Market } from '../../shared/models';

export interface CoreState {
  error: any;
  initialized: boolean;
  market: Market;
}

export const coreInitialState: CoreState = {
  error: undefined,
  initialized: false,
  market: undefined
};

// selectors

export function getError(state$: Observable<CoreState>) {
  return state$.select(state => state.error);
}

export function isInitialized(state$: Observable<CoreState>) {
  return state$.select(state => state.initialized);
}