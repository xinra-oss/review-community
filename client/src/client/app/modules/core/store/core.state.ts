import { Observable } from 'rxjs/Observable';
import * as _ from 'lodash';

import { ApiError } from '../../shared/models';

export interface CoreState {
  apiError: ApiError;
  initialized: boolean;
}

export const coreInitialState: CoreState = {
  apiError: undefined,
  initialized: false
};

// selectors

export function getApiError(state$: Observable<CoreState>) {
  return state$.select(state => state.apiError);
}

export function isInitialized(state$: Observable<CoreState>) {
  return state$.select(state => state.initialized);
}