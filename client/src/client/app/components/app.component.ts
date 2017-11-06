// any operators needed throughout your application
import './operators';

// libs
import { Component, OnInit } from '@angular/core';

// app
import { AnalyticsService } from '../modules/analytics/services/index';
import { LogService, AppService } from '../modules/core/services/index';
import { Config } from '../modules/core/utils/index';

// for back button
import { RouterExtensions } from "nativescript-angular";
import * as application from "application";
import { AndroidApplication, AndroidActivityBackPressedEventData } from "application";
import { isAndroid } from "platform";

/**
 * This class represents the main application component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-app',
  templateUrl: 'app.component.html'
})
export class AppComponent implements OnInit {
  
  constructor(
    public analytics: AnalyticsService,
    public log: LogService,
    private appService: AppService,
    private routerExtensions: RouterExtensions
  ) {
    log.debug(`Config env: ${Config.ENVIRONMENT().ENV}`);
  }

  ngOnInit(): void {
    // At the moment we use a <router-outlet> instead of a <page-router-outlet> because this way we
    // have the same ActionBar everywhere (this is subject to change in the future). If there are no
    // pages, we have to take care of the "back" button on Android ourselves.
    if (isAndroid) {
      application.android.on(AndroidApplication.activityBackPressedEvent, (data: AndroidActivityBackPressedEventData) => {
          if (!this.routerExtensions.router.isActive("", true)) {
            data.cancel = true; // prevents default back button behavior
            this.routerExtensions.back();
          }
        });
    }
  }
}
