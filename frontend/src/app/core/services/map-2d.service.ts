import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {
  ballIcon,
  blueIcon, bluePlayer,
  cornerIcon, foulIcon,
  golIcon,
  offSideIcon, red1, red10, red11, red12, red13, red14, red15, red16, red17, red18, red19, red20, red21, red22, red23, red24, red25, red26, red27, red28, red29, red30, red31, red32, red33, red34, red35, red2, red3, red4, red5, red6, red7, red8, red9,
  redIcon, redPlayer,
  blue1, blue2, blue3, blue4, blue5, blue6, blue7, blue8, blue9, blue10, blue11, blue12, blue13, blue14, blue15, blue16, blue17, blue18, blue19, blue20, blue21, blue22, blue23, blue24, blue25, blue26, blue27, blue28, blue29, blue30, blue31, blue32, blue33, blue34, blue35
} from "../../shared/components/leaflet-color-markers";
import L from 'leaflet';
import {MatSnackBar} from "@angular/material/snack-bar";
import {DataService} from "./data.service";
import { PlayerModel } from "../../shared/domain/player.model";
import {ChartService} from "./chart.service";
import {GamesService} from "./games.service";

@Injectable({
  providedIn: "root"
})
export class Map2dService {
  dataFramePlayers: Object[] = [];
  dataFramesBall: Object[] = [];
  topThreePlayers: Object[] = [];
  playerStats: Object[] = [];
  playerChart: Object[] = [];
  heatData: Object[] = [];
  events: Object[] = [];

  map: any;
  playersGroup: L.LayerGroup = new L.LayerGroup;
  ballMarker: L.Marker | undefined;
  players: any[] = [];
  eventMarker: L.Marker | undefined;

  minPerPage: number = 2;
  fps: number = 25;
  numFramesPerPage = this.minPerPage * 60 * this.fps; //3000
  tamPagPlayers = 22 * this.numFramesPerPage;
  tamPagBall = this.numFramesPerPage
  imgHeight: number = 86 * 1.5 + 40; //169
  imgWidth: number = 125 * 1.5 + 40; //227,5
  timePag = (1000 / this.fps) / 2 - 10;
  isPaused: Boolean = true;

  currentFrame: number = 0;
  maxFrame: number = 0;
  gameTime: string = '00:00';

  homePlayerIcons = {
    1: red1,
    2: red2,
    3: red3,
    4: red4,
    5: red5,
    6: red6,
    7: red7,
    8: red8,
    9: red9,
    10: red10,
    11: red11,
    12: red12,
    13: red13,
    14: red14,
    15: red15,
    16: red16,
    17: red17,
    18: red18,
    19: red19,
    20: red20,
    21: red21,
    22: red22,
    23: red23,
    24: red24,
    25: red25,
    26: red26,
    27: red27,
    28: red28,
    29: red29,
    30: red30,
    31: red31,
    32: red32,
    33: red33,
    34: red34,
    35: red35
  };
  awayPlayerIcons = {
    1: blue1,
    2: blue2,
    3: blue3,
    4: blue4,
    5: blue5,
    6: blue6,
    7: blue7,
    8: blue8,
    9: blue9,
    10: blue10,
    11: blue11,
    12: blue12,
    13: blue13,
    14: blue14,
    15: blue15,
    16: blue16,
    17: blue17,
    18: blue18,
    19: blue19,
    20: blue20,
    21: blue21,
    22: blue22,
    23: blue23,
    24: blue24,
    25: blue25,
    26: blue26,
    27: blue27,
    28: blue28,
    29: blue29,
    30: blue30,
    31: blue31,
    32: blue32,
    33: blue33,
    34: blue34,
    35: blue35
  };

  constructor(private http: HttpClient,
              protected readonly _snackbar: MatSnackBar,
              protected readonly dataService: DataService,
              protected readonly chartService: ChartService,
              protected readonly gameService: GamesService) {

  }

  getPlayerDataFrames(page: number, pageSize: number): Observable<Object[]> {
    return this.http.get<Object[]>('http://localhost:5000/2d/dataframe/players?page=' + page + '&page_size=' + pageSize + '&game_id=' + this.gameService.currentGameId);
  }

  getBallDataFrames(page: number, pageSize: number): Observable<Object[]> {
    return this.http.get<Object[]>('http://localhost:5000/2d/dataframe/ball?page=' + page + '&page_size=' + pageSize + '&game_id=' + this.gameService.currentGameId);
  }

  getEvents(gameId: number): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:5000/2d/getEvents/${gameId}`);
  }

  getTopThreeByStat(stat: string, gameId: number): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:5000/2d/top_three/` + stat + `${gameId}`);
  }

  updateTopThreeByStat(stat: string, gameId: number) {
    this.getTopThreeByStat(stat, gameId).subscribe(data => {
      this.topThreePlayers = data;
    })
  }

  getPlayerStats(optaId: number, gameId: number): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:5000/2d/player_stats/` + optaId + `${gameId}`);
  }

  updatePlayerStats(optaId: number, gameId: number) {
    this.getPlayerStats(optaId, gameId).subscribe(data => {
      this.playerStats = data;
    })
  }

  getPlayerChart(optaId: number): Observable<Object[]> {
    return this.http.get<Object[]>('http://localhost:5000/2d/player_chart/' + optaId);
  }

  updatePlayerChart(optaId: number) {
    this.getPlayerChart(optaId).subscribe(data => {
      this.playerChart = data;
    })
  }

  getHeatData(optaId: number, gameId: number): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:5000/2d/heat_data/` + optaId + `/${gameId}`);
  }

  updateHeatData(optaId: number, gameId: number) {
    this.getHeatData(optaId, gameId).subscribe(data => {
      this.heatData = data;
    })
  }

  representPlayers(frame: number, playerDataFrames: Object[]): L.LayerGroup {
    this.players = [];
    const layerGroup = L.layerGroup()
    const markerOptions: L.MarkerOptions = {
      icon: bluePlayer,
    }
    const playerKeys = Object.keys(playerDataFrames)
    const numPlayers = playerKeys.length
    let homeTeam = undefined;
    for (let i = 0; i < numPlayers; i++) {
      const key = playerKeys[i]
      // @ts-ignore
      const player = playerDataFrames[key]
      if (i == 0) {
        homeTeam = player.team;
      }
      if (player.frameIdx === frame) {
        const markerPosition: L.LatLngTuple = [
          player.y * (this.imgHeight - 40) + 20,
          player.x * (this.imgWidth - 40) + 20,
        ]
        if (player.team == homeTeam) {
          // markerOptions.icon = redPlayer;
          // @ts-ignore
          markerOptions.icon = (this.homePlayerIcons)[player.number] || redPlayer;
        } else {
          // markerOptions.icon = blueIcon;
          // @ts-ignore
          markerOptions.icon = (this.awayPlayerIcons)[player.number] || blueIcon;
        }
        const marker = L.marker(markerPosition, markerOptions)
        this.players.push(player);
        layerGroup.addLayer(marker)
      }
    }
    layerGroup.addTo(this.map)
    // @ts-ignore
    return layerGroup
  }

  createPopup(marker: L.Marker<any>, playerData: PlayerModel) {
    var popupContent =
      "<b>Name: </b>" + playerData.name + "<br/>" +
      "<b>Number: </b>" + playerData.number + "<br/>" +
      "<b>Position: </b>" + playerData.position + "<br/>";
    marker.bindPopup(popupContent);
  }

  createPopups() {
    let i= 0;
    this.playersGroup.eachLayer((layer) => {
      if (layer instanceof L.Marker) {
        if (this.players[i].optaId){
          this.dataService.getPlayerDataByOptaId(this.players[i].optaId).subscribe(playerData => {
            this.createPopup(layer, playerData);
          });
        }
      }
      i++;
    });
  }

  representBall(frame: number, ballDataFrames: Object[]): L.Marker{
    var markerOptions = {
      icon: ballIcon,
    }
    let marker: L.Marker;
    for (var key in this.dataFramesBall) {
      // @ts-ignore
      if (ballDataFrames[key]['frameIdx'] == frame) {

        var markerPosition: L.LatLngTuple = [
          // @ts-ignore
          ballDataFrames[key]['y'] * (this.imgHeight - 40) + 20,
          // @ts-ignore
          ballDataFrames[key]['x'] * (this.imgWidth - 40) + 20,
        ]
        marker = L.marker(markerPosition, markerOptions)
        marker.addTo(this.map)
        break
      }
    }
    // @ts-ignore
    return marker;
  }

  representsEvents(frame: number, events: Object[]): L.Marker | undefined{
    // this.awayOptaId =
    let marker;
    let x;
    let y;
    for (var key in events) {
      // @ts-ignore
      if (events[key][3] == frame) {
        // @ts-ignore
        if (events[key][5] == 1) {
          // @ts-ignore
          if (events[key][8] == this.gameService.awayTeamOptaId) {
            // @ts-ignore
            x = 100 - events[key][6]
            // @ts-ignore
            y = 100 - events[key][7]
          } else {
            // @ts-ignore
            x = events[key][6]
            // @ts-ignore
            y = events[key][7]
          }
        } else {
          // @ts-ignore
          if (events[key][8] == this.gameService.awayTeamOptaId) {
            // @ts-ignore
            x = events[key][6]
            // @ts-ignore
            y = events[key][7]
          } else {

            // @ts-ignore
            x = 100 - events[key][6]
            // @ts-ignore
            y = 100 - events[key][7]
          }
        }
        // @ts-ignore
        switch (events[key][0]) {
          case 6: // Event: Corner
            // @ts-ignore
            if (events[key][5] == 1) {
              // @ts-ignore
              if (events[key][8] == this.gameService.awayTeamOptaId) {
                // @ts-ignore
                if (events[key][9] == 'right') {
                  y = 100
                  x = 0
                } else { // @ts-ignore
                  if (events[key][9] == 'left') {
                    y = 0
                    x = 0
                  }
                }
              } else {
                // @ts-ignore
                if (events[key][9] == 'right') {
                  y = 0
                  x = 100
                } else { // @ts-ignore
                  if (events[key][9] == 'left') {
                    y = 100
                    x = 100
                  }
                }
              }
            } else {
              // @ts-ignore
              if (events[key][8] == this.gameService.awayTeamOptaId) {
                // @ts-ignore
                if (events[key][9] == 'right') {
                  y = 0
                  x = 100
                } else { // @ts-ignore
                  if (events[key][9] == 'left') {
                    y = 100
                    x = 100
                  }
                }
              } else {
                // @ts-ignore
                if (events[key][9] == 'right') {
                  y = this.imgHeight
                  x = 0
                } else { // @ts-ignore
                  if (events[key][9] == 'left') {
                    y = 0
                    x = 0
                  }
                }
              }
            }
            marker = L.marker(
              [
                (y * (this.imgHeight - 40)) / 100 + 20,
                (x * (this.imgWidth - 40)) / 100 + 20,
              ],
              {
                icon: cornerIcon,
              }
            )
            return marker.addTo(this.map)

          case 16: // Event: Goal
            marker = L.marker(
              [
                (y * (this.imgHeight - 40)) / 100 + 20,
                (x * (this.imgWidth - 40)) / 100 + 20,
              ],
              {
                icon: golIcon,
              }
            )
            return marker.addTo(this.map)

          case 2: // Event: Offside
            marker = L.marker(
              [
                (y * (this.imgHeight - 40)) / 100 + 20,
                (x * (this.imgWidth - 40)) / 100 + 20,
              ],
              {
                icon: offSideIcon,
              }
            )
            return marker.addTo(this.map)

          case 4: // Event: Foul
            marker = L.marker(
              [
                (y * (this.imgHeight - 40)) / 100 + 20,
                (x * (this.imgWidth - 40)) / 100 + 20,
              ],
              {
                icon: foulIcon,
              }
            )
            return marker.addTo(this.map)
          default:
            break
        }
      }
    }
    return undefined;
  }

  async fetchBallData(index: number) {
    try {
      this.dataFramesBall = await new Promise((resolve, reject) => {
        this.getBallDataFrames(index, this.tamPagBall).subscribe({
          next: data => resolve(data),
          error: err => reject(err)
        });
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
  }

  async fetchPlayerData(index: number) {
    try {
      this.dataFramePlayers = await new Promise((resolve, reject) => {
        this.getPlayerDataFrames(index, this.tamPagPlayers).subscribe({
          next: data => resolve(data),
          error: err => reject(err)
        });
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
  }

  async framesLoop() {
    this.removeMarkers()
    if (this.gameService.currentGameId!=null){
      this.getEvents(this.gameService.currentGameId).subscribe(data => {
        this.events = data;
      });
    }
    let index: number = 0;
    if ((this.currentFrame / this.fps / 60) % 2 == 0) {
      index = Math.ceil(this.currentFrame / this.numFramesPerPage) + 1
    } else {
      index = Math.ceil(this.currentFrame / this.numFramesPerPage)
    }
    if (index == 0) {
      index = 1
    }
    let snackBarRef = this._snackbar.open('Cargando', 'Cerrar');
    await this.fetchBallData(index);
    await this.fetchPlayerData(index);
    snackBarRef.dismiss();
    while (this.currentFrame < this.maxFrame && !this.isPaused) {
      if (this.currentFrame!=1 && this.currentFrame % this.numFramesPerPage == 1) {
        index++;
        await this.fetchBallData(index);
        await this.fetchPlayerData(index);
      }

      this.gameTime = this.framesToMinutesAndSeconds(this.currentFrame)
      this.ballMarker = this.representBall(this.currentFrame, this.dataFramesBall);
      this.playersGroup = this.representPlayers(this.currentFrame, this.dataFramePlayers);
      let eventMarker = this.representsEvents(this.currentFrame, this.events);
      if (eventMarker!=undefined){
        this.eventMarker = eventMarker;
      }
      await new Promise((resolve) => setTimeout(resolve, this.timePag)) //(1000/this.fps)/2 - 10 //this.timePag

      if (eventMarker != undefined) {
        new Promise((resolve) => {
          var p = this.eventMarker
          if (this.timePag == 0) setTimeout(() => resolve(p), 10 * 500)
          else setTimeout(() => resolve(p), this.timePag * 500)
        }).then((p) => {
          this.map.removeLayer(p);
        })
      }
      this.currentFrame++;
      if (!this.isPaused) {
        this.removeMarkers();
      }
    }
  }

  removeMarkers() {
    if (this.playersGroup != undefined){
      this.map.removeLayer(this.playersGroup)
    }
    if (this.ballMarker != undefined) {
      this.map.removeLayer(this.ballMarker)
    }
    // if (this.eventMarker != undefined) {
    //   this.map.removeLayer(this.eventMarker)
    // }
    // this.players = []
    if(this.chartService.heatMarker!=undefined){
      this.map.removeLayer(this.chartService.heatMarker);
    }
  }

  framesToMinutesAndSeconds(frameNumber: number): string {
    if (frameNumber>76709){
      frameNumber = frameNumber - 76709 + 67500; //end frameid firstPeriod, frameid 45 mins
    }
    const totalSeconds = frameNumber / this.fps;
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = Math.floor(totalSeconds % 60);
    const formattedSeconds = seconds < 10 ? `0${seconds}` : `${seconds}`;
    return `${minutes}:${formattedSeconds}`;
  }

}
