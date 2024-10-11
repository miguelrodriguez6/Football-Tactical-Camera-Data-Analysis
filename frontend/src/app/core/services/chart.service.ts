import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: "root"
})
export class ChartService{
  selectedPlayer: any;
  heatData: any;
  heatMarker: any;
  constructor(private http: HttpClient) {
  }

  getPlayerstats(optaId: number, gameId: number): Observable<any>{
    return this.http.get<any>(`http://localhost:5000/2d/player_stats/${optaId}/${gameId}`)
  }

  getTeamStats(gameId: number, teamOptaId: number){
    return this.http.get<any>(`http://localhost:5000/stats/team-stats?gameId=${gameId}&teamOptaId=${teamOptaId}`)
  }

  getHeatData(optaId: number, gameId: number): Observable<any>{
    return this.http.get<any>(`http://localhost:5000/2d/heat_data/${optaId}/${gameId}`)
  }

  async fetchHeatData(optaId: number, gameId: number) {
    try {
      this.heatData = await new Promise((resolve, reject) => {
        this.getHeatData(optaId, gameId).subscribe({
          next: data => resolve(data),
          error: err => reject(err)
        });
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
  }

}
