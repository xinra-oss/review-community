import { Action } from '@ngrx/store';
import { type } from '../../core/utils/index';
import { Market } from '../../shared/models';

export namespace Core {
  // Category to uniquely identify the actions
  export const CATEGORY: string = 'Core';

  export interface CoreActions {
    INIT: string,
    INITIALIZED: string,
    HANDLE_ERROR: string,
    CLEAR_ERROR: string,
    SET_MARKET: string,
    SET_AVAILAVLE_MARKETS: string
  }

  export const ActionTypes: CoreActions = {
    INIT: type(`[${CATEGORY}] Init`),
    INITIALIZED: type(`[${CATEGORY}] Initialized`),
    HANDLE_ERROR: type(`[${CATEGORY}] Handle error`),
    CLEAR_ERROR: type(`[${CATEGORY}] Clear error`),
    SET_MARKET: type(`[${CATEGORY}] Set market`),
    SET_AVAILAVLE_MARKETS: type(`[${CATEGORY}] Set available markets`)
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

  export class SetMarketAction implements Action {
    type = ActionTypes.SET_MARKET;
    constructor(public payload: Market) {}
  }

  export class SetAvailableMarketsAction implements Action {
    type = ActionTypes.SET_AVAILAVLE_MARKETS;
    constructor(public payload: Market[]) {}
  }

  export type Actions
    = InitAction
    | InitializedAction
    | HandleErrorAction
    | ClearErrorAction
    | SetMarketAction
    | SetAvailableMarketsAction;
}
