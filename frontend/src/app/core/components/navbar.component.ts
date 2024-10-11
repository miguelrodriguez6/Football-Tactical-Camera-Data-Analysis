import {Component} from "@angular/core";
import {UserService} from "../services/user.service";
import {GamesService} from "../services/games.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: 'navbar.component.scss'
})
export class NavbarComponent{

  constructor(protected readonly userService: UserService,
              protected readonly gameService: GamesService,
              protected readonly router: Router) {
    if (gameService.currentGameId==null ||gameService.currentGame==null){
      this.router.navigate(["login"])
    }
  }
}
