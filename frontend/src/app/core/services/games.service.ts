import {Injectable} from "@angular/core";
import {EMPTY, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {UserService} from "./user.service";

@Injectable({
  providedIn: 'root'
})
export class GamesService{

  gamesList: any;
  currentGame: any;
  selectedGame: string = '';
  currentGameId: number | null = null;
  homeTeamOptaId: number | null = null;
  awayTeamOptaId: number | null = null;
  gameDescription: string = '';
  constructor(private http: HttpClient,
              protected readonly userService: UserService) {
    let game = localStorage.getItem('gameId');
    if (game!=null){
      this.currentGameId = parseInt(game);
      this.fetchGame(this.currentGameId);
    }
  }

  findAllGames(userId: number | null): Observable<any>{
    if (userId!=null){
      const url = `http://localhost:5000/games/all?userId=${userId}`;
      return this.http.get<any>(url);
    } else {
      console.error('Error al obtener datos');
      return EMPTY;
    }
  }

  findGame(gameId: number){
    const url = `http://localhost:5000/games/game?gameId=${gameId}`;
    return this.http.get<any>(url);
  }

  async fetchGames() {
    try {
      this.gamesList = await new Promise((resolve, reject) => {
        this.findAllGames(this.userService.loggedUserId).subscribe({
          next: data => resolve(data),
          error: err => reject(err)
        });
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
      throw error;
    }
  }

  async fetchGame(gameId: number){
    try {
      this.currentGame = await new Promise((resolve, reject) => {
        this.findGame(gameId).subscribe({
          next: data => {
            this.homeTeamOptaId = data.homeTeam.optaId;
            this.awayTeamOptaId = data.awayTeam.optaId;
            this.gameDescription = data.description;
            resolve(data)
          },
          error: err => reject(err)
        });
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
      throw error;
    }
  }

  setAllToNull(){
    this.gamesList = null;
    this.currentGame = null;
    this.selectedGame = '';
    this.currentGameId = null;
    this.homeTeamOptaId = null;
    this.awayTeamOptaId= null;
    this.gameDescription = '';
  }

}
