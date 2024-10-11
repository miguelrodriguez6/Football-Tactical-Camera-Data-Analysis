// login.component.ts
import {Component, OnInit} from '@angular/core';
import {UserService} from "../../core/services/user.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-login',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit{
  userName: string = '';
  password: string = '';
  public loginValid = true;

  constructor(protected readonly userService: UserService,
              protected readonly router: Router,
              protected readonly snackBar: MatSnackBar) {
  }
  async onSubmit() {
    try {
      await this.userService.postLogIn(this.userName, this.password);
      await this.router.navigate(['select-game']);
    } catch (error) {
      this.snackBar.open('No se ha podido iniciar sesión.', 'Close', {
        duration: 2500
      })
    }
  }

  ngOnInit() {
    let user = localStorage.getItem('userId');
    if (user!=null){
      this.router.navigate(['select-game'])
    }
  }
}
