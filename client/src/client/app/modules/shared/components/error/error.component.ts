import { Injector, Component, OnInit, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from '../../../ngrx';
import { LogService, WindowService, Core } from '../../../core';
import { Subscription } from 'rxjs';

/**
 * This component displays errors that may occur when using the application, e.g. invlaid form input
 * or connection timeout.
 */
@Component({
  moduleId: module.id,
  selector: 'rc-error',
  templateUrl: 'error.component.html',
  styleUrls: [
    'error.component.css',
  ],
})
export class ErrorComponent implements OnInit, OnDestroy {

  subscription: Subscription;

  constructor(
    private store: Store<AppState>,
    private log: LogService,
    private win: WindowService
  ) {}

  ngOnInit() {
    this.subscription = this.store.select(state => state.core.error).subscribe(error => {
      if (error != undefined) {
        this.win.alert(JSON.stringify(error));
        this.store.dispatch(new Core.ClearErrorAction());
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
  
}