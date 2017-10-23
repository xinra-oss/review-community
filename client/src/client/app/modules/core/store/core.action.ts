import { Action } from '@ngrx/store';
import { type } from '../../core/utils/index';
import { ApiError } from '../../shared/models';

export namespace Core {
  // Category to uniquely identify the actions
  export const CATEGORY: string = 'Core';

  export interface CoreActions {
    INIT: string,
    INITIALIZED: string,
    HANDLE_API_ERROR: string,
    CLEAR_API_ERROR: string
  }

  export const ActionTypes: CoreActions = {
    INIT: type(`[${CATEGORY}] Init`),
    INITIALIZED: type(`[${CATEGORY}] Initialized`),
    HANDLE_API_ERROR: type(`[${CATEGORY}] Handle API error`),
    CLEAR_API_ERROR: type(`[${CATEGORY}] Clear API error`),
  };

  export class InitAction implements Action {
    type = ActionTypes.INIT;
    payload = null;
  }

  export class InitializedAction implements Action {
    type = ActionTypes.INITIALIZED;
    payload = null;
  }

  export class HandleApiErrorAction implements Action {
    type = ActionTypes.HANDLE_API_ERROR;
    constructor(public payload: ApiError) {}
  }

  export class ClearApiErrorAction implements Action {
    type = ActionTypes.CLEAR_API_ERROR;
    payload = null;
  }

  export type Actions
    = InitAction
    | InitializedAction
    | HandleApiErrorAction
    | ClearApiErrorAction;
}
