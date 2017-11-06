// angular
import { NgModule, Optional, SkipSelf, NO_ERRORS_SCHEMA, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';

// app
import { SharedModule } from '../shared';
import { AUTH_PROVIDERS } from './services';
import { MultilingualModule } from '../i18n/multilingual.module';

import { LoginComponent } from './components';

/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
  imports: [
    SharedModule,
    MultilingualModule
  ],
  providers: [
    ...AUTH_PROVIDERS
  ],
  declarations: [
    LoginComponent
  ],
  schemas: [
    NO_ERRORS_SCHEMA,
    CUSTOM_ELEMENTS_SCHEMA
  ],
  exports: [
    SharedModule
  ]
})
export class AuthModule {

  constructor(@Optional() @SkipSelf() parentModule: AuthModule) {
    if (parentModule) {
      throw new Error('AuthModule already loaded; Import in root module only.');
    }
  }
}
