// angular
import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

// libs
import { Observable } from 'rxjs/Observable';

// app
import { Config } from '../../core/index';
import { Analytics, AnalyticsService } from '../../analytics/index';

// module
//import { NameList } from '../actions/index';

@Injectable()
export class ProductService extends Analytics {

  constructor(
    public analytics: AnalyticsService,
    private http: Http
  ) {
    super(analytics);
    //this.category = NameList.CATEGORY;
  }

}
