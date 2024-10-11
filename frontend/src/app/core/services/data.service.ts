import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {EventModel, PlayerModel} from "../../shared/domain/player.model";
import {VideoClipModel} from "../../shared/domain/video-clip.model";
import {GamesService} from "./games.service";

@Injectable({
  providedIn: 'root'
})
export class DataService{


  clipsData: VideoClipModel[] = [];
  clipsPlayers = new Set<string>();

  constructor(private http: HttpClient,
              protected readonly gameService: GamesService) {
    this.getData('home', 'contraataque').subscribe(data => {
      this.clipsData=data;
      this.crearListaJuadores();
    })
  }

  enviarClipsData(side: string, play: string) {
    this.getData(side, play).subscribe(data => {
      this.clipsData=data;
      this.crearListaJuadores();
    })
  }

  filteredClipsData(side: string, play: string, name: string) {
    this.getData(side, play).subscribe(data => {
      this.clipsData = data.filter(data => data.name==name);
    })
  }

  crearListaJuadores(){
    this.clipsPlayers = new Set<string>([]);
    this.clipsData.forEach(clip => {
      this.clipsPlayers.add(clip.name);
    });
  }

  getData(side: string, play: string): Observable<VideoClipModel[]> {
    if (this.gameService.currentGameId){
      console.log('http://localhost:5000/jugadas/' + play + '?side=' + side + '&gameId=' + this.gameService.currentGameId);
      return this.http.get<VideoClipModel[]>('http://localhost:5000/jugadas/' + play + '?side=' + side + '&gameId=' + this.gameService.currentGameId);
    }
    return of();
  }

  getJugadoresEquipoLocal(gameId: number): Observable<PlayerModel[]> {
    return this.http.get<PlayerModel[]>(`http://localhost:5000/jugadores/local/${gameId}`);
  }

  getJugadoresEquipoVisitante(gameId: number): Observable<PlayerModel[]> {
    return this.http.get<PlayerModel[]>(`http://localhost:5000/jugadores/visitante/${gameId}`);
  }

  getGoals(gameId: number): Observable<EventModel[]> {
    return this.http.get<EventModel[]>(`http://localhost:5000/2d/getGoals/${gameId}`);
  }

  getCorners(gameId: number): Observable<EventModel[]> {
    return this.http.get<EventModel[]>(`http://localhost:5000/2d/getCorners/${gameId}`);
  }

  getPlayerDataByOptaId(id: number): Observable<PlayerModel> {
    return this.http.get<PlayerModel>(`http://localhost:5000/jugadores/optaId/${id}`);
  }

}
