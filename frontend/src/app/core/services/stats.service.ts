import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class StatsService{

  possessionData: {homePossession: number, awayPossession: number} = {homePossession: 0, awayPossession: 0};
  constructor(private http: HttpClient) {
  }

  getPossessionData(gameId: number): Observable<any>{
    return this.http.get<any>(`http://localhost:5000/stats/possession?gameId=${gameId}`);
  }

  getDistanceData(gameId: number, teamOptaId: number): Observable<any>{
    return this.http.get<any>(`http://localhost:5000/stats/team-distance?gameId=${gameId}&teamOptaId=${teamOptaId}`);
  }

  getTopThreePlayersDataByStat(gameId: number, stat: string): Observable<any>{
    return this.http.get<any>(`http://localhost:5000/2d/top_three/${stat}/${gameId}`);
  }

  getTopSpeed(gameId: number){
    return this.http.get<any>(`http://localhost:5000/stats/top-speed?gameId=${gameId}`);
  }

  getPlayerStats(gameId: number, playerOptaId: number){
    return this.http.get<any>(`http://localhost:5000/stats/player-stats?gameId=${gameId}&playerOptaId=${playerOptaId}`);
  }

  async fetchPossessionData(gameId: number) {
    try {
      await new Promise((resolve, reject) => {
        this.getPossessionData(gameId).subscribe({
          next: data => {
            this.possessionData = {homePossession: data[0], awayPossession: data[1]};
            resolve(data);
          },
          error: err => reject(err)
        });
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
  }

}
