import { NgModule, NO_ERRORS_SCHEMA, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { TNSFontIconModule } from 'nativescript-ngx-fonticon';
import { NativeScriptFormsModule } from 'nativescript-angular/forms';

// modules
import { MultilingualModule } from '../i18n/index';
import { SHARED_COMPONENTS } from './components/index';

const SHARED_MODULES: any[] = [
  CommonModule,
  HttpModule,
  FormsModule,
  RouterModule,
  MultilingualModule,
  // It's probably bad to do this here. See
  // https://github.com/NathanWalker/angular-seed-advanced/issues/366 for details on the
  // workaround. We may need to fix this properly for the web client to work.
  NativeScriptFormsModule
];

/**
 * SharedModule
 * Only for shared components, directives and pipes
 * Do not specify providers here
 * https://angular.io/docs/ts/latest/cookbook/ngmodule-faq.html#!#what-kinds-of-modules-should-i-have-and-how-should-i-use-them-
 */

@NgModule({
  imports: [
    ...SHARED_MODULES,
    // This should not really be here. It is {N} specific but inlcuding it in the ComponentModule
    // doesn't work because it has to be on top of the hierarchy in order for the pipe to work.
    TNSFontIconModule.forRoot({
      'fa': 'font-awesome.css'
    })
  ],
  declarations: [
    ...SHARED_COMPONENTS
  ],
  schemas: [
    NO_ERRORS_SCHEMA,
    CUSTOM_ELEMENTS_SCHEMA,
  ],
  exports: [
    ...SHARED_MODULES,
    ...SHARED_COMPONENTS,
    TNSFontIconModule
  ]
})
export class SharedModule {}
