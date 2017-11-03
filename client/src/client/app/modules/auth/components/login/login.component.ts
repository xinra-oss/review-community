import { Injector, Component } from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'rc-login',
  templateUrl: 'login.component.html',
  styleUrls: [
    'login.component.css',
  ],
})
export class LoginComponent {

  input = {
    username: "",
    password: ""
  }

  login() {

  }

}
