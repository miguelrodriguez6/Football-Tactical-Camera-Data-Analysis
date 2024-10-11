// register.component.ts
import {Component, OnInit} from '@angular/core';
import {UserService} from "../../core/services/user.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-register',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.scss']
})
export class RegisterPageComponent implements OnInit{
  firstName: string = '';
  lastName: string = '';
  userName: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';

  constructor(protected readonly userService: UserService,
              protected readonly router: Router,
              protected readonly snackBar: MatSnackBar) {
  }

  ngOnInit() {
    if (this.userService.loggedUserId!=null){
      this.router.navigate(['select-game'])
    }
  }
  async onSubmit() {

    try {
      await this.userService.postSignIn(this.userName, this.password, this.firstName, this.lastName, this.email);
      await this.router.navigate(['login']);
    } catch (error) {
      this.snackBar.open('No se ha podido registrar un nuevo usuario.', 'Close', {
        duration: 2500
      })
    }
  }
}
