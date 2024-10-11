import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {UserService} from "../../core/services/user.service";

@Component({
  selector: 'welcome-page-app',
  templateUrl: 'welcome-page.component.html',
  styleUrl: 'welcome-page.component.scss'
})
export class WelcomePageComponent implements OnInit{

  constructor(private router: Router,
              protected readonly userService: UserService) {}

  ngOnInit() {
    if (this.userService.loggedUserId!=null){
      this.router.navigate(['select-game'])
    }
  }

  redirectToLogin() {
    this.router.navigate(['/login']);
  }

  redirectToRegister() {
    this.router.navigate(['/register']);
  }
}
