// libs
import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs/Observable';

// app
import { RouterExtensions, Config } from '../../modules/core/index';

@Component({
  moduleId: module.id,
  selector: 'sd-home',
  templateUrl: 'home.component.html',
  styleUrls: ['home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(public routerext: RouterExtensions) {}

  ngOnInit() {
    
  }

  viewCategories() {
    this.routerext.navigate(['/categories']);
  }

  viewHistory() {
    // Try this in the {N} app
    // {N} can use these animation options
    this.routerext.navigate(['/categories'], {
      transition: {
        duration: 400,
        name: 'slideTop',
      }
    });
  }

  scan() {

  }
}
