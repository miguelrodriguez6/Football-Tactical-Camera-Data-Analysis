import {Component, OnInit} from "@angular/core";
import {GamesService} from "../../core/services/games.service";
import {Router} from "@angular/router";

@Component({
  selector: 'select-game-app',
  templateUrl: 'select-game.component.html',
  styleUrl: 'select-game.component.scss'
})
export class SelectGameComponent implements OnInit{
  isGameSelected: boolean = false;
  constructor(protected readonly gameService: GamesService,
              private router: Router) {
  }

  async ngOnInit() {
    await this.gameService.fetchGames();
  }

  async onSelectionChange(event: any) {
    this.gameService.selectedGame = event.value;
    this.gameService.currentGameId = event.value;
    localStorage.setItem('gameId', <string>this.gameService.currentGameId?.toString());
    if (this.gameService.currentGameId != null) {
      await this.gameService.fetchGame(this.gameService.currentGameId);
    }
    this.isGameSelected = !!this.gameService.selectedGame;
  }

  redirectToAddGame(){
    this.router.navigate(['add-game']);
  }

  redirectToHomePage(){
    this.router.navigate(['home']);
  }

}
