import { Observable } from 'rxjs/Observable';
import * as _ from 'lodash';

import { ApiError } from '../../shared/models';

export interface CoreState {
  error: any;
  initialized: boolean;
}

export const coreInitialState: CoreState = {
  error: undefined,
  initialized: false
};

// selectors

export function getError(state$: Observable<CoreState>) {
  return state$.select(state => state.error);
}

export function isInitialized(state$: Observable<CoreState>) {
  return state$.select(state => state.initialized);
}