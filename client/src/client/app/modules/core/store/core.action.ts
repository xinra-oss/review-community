import { Action } from '@ngrx/store';
import { type } from '../../core/utils/index';

export namespace Core {
  // Category to uniquely identify the actions
  export const CATEGORY: string = 'Core';

  export interface CoreActions {
    INIT: string,
    INITIALIZED: string,
    HANDLE_ERROR: string,
    CLEAR_ERROR: string
  }

  export const ActionTypes: CoreActions = {
    INIT: type(`[${CATEGORY}] Init`),
    INITIALIZED: type(`[${CATEGORY}] Initialized`),
    HANDLE_ERROR: type(`[${CATEGORY}] Handle error`),
    CLEAR_ERROR: type(`[${CATEGORY}] Clear error`),
  };

  export class InitAction implements Action {
    type = ActionTypes.INIT;
    payload = null;
  }

  export class InitializedAction implements Action {
    type = ActionTypes.INITIALIZED;
    payload = null;
  }

  export class HandleErrorAction implements Action {
    type = ActionTypes.HANDLE_ERROR;
    constructor(public payload: any) {}
  }

  export class ClearErrorAction implements Action {
    type = ActionTypes.CLEAR_ERROR;
    payload = null;
  }

  export type Actions
    = InitAction
    | InitializedAction
    | HandleErrorAction
    | ClearErrorAction;
}
