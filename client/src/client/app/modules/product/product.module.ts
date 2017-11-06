// angular
import { NgModule, Optional, SkipSelf, NO_ERRORS_SCHEMA, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';

// app
import { SharedModule } from '../shared/index';
import { PRODUCT_PROVIDERS } from './services/index';
import { ProductComponent } from './components';
import { MultilingualModule } from '../i18n/multilingual.module';

/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
  imports: [
    SharedModule,
    MultilingualModule,
  ],
  providers: [
    ...PRODUCT_PROVIDERS
  ],
  declarations: [
    ProductComponent
  ],
  schemas: [
    NO_ERRORS_SCHEMA,
    CUSTOM_ELEMENTS_SCHEMA
  ],
  exports: [
    SharedModule
  ]
})
export class ProductModule {

  constructor(@Optional() @SkipSelf() parentModule: ProductModule) {
    if (parentModule) {
      throw new Error('ProductModule already loaded; Import in root module only.');
    }
  }
}
