import {Component, OnInit} from "@angular/core";
import {Map2dService} from "../../core/services/map-2d.service";
import {DataService} from "../../core/services/data.service";
import {ChartService} from "../../core/services/chart.service";
import {Router} from "@angular/router";
import L from 'leaflet';
import 'leaflet.heat';
import 'leaflet.heat/dist/leaflet-heat.js';
import {GamesService} from "../../core/services/games.service";
import {EventModel} from "../../shared/domain/player.model";

@Component({
  selector: 'two-dimensions-pitch-app',
  templateUrl: './two-dimensions-pitch.component.html',
  styleUrl: './two-dimensions-pitch.component.scss'
})
export class TwoDimensionsPitchComponent implements OnInit{
  selectedPlayer: any;
  selectedGoal: string = '';
  selectedCorner: string = '';
  initialMinute: number = 0;
  finalMinute: number = 0;
  initialFrame: number = 0;
  finalFrame: number = 0;
  homePlayersList: any[] = [];
  awayPlayerList: any[] = [];
  goalList: EventModel[] = [];
  cornerList: EventModel[] = [];

  constructor(protected readonly mapService: Map2dService,
              protected readonly dataService: DataService,
              protected readonly chartService: ChartService,
              private router: Router,
              protected readonly gameService: GamesService) {
  }

  ngOnInit(): void {
    if (this.gameService.currentGameId!=null){
      this.dataService.getJugadoresEquipoLocal(this.gameService.currentGameId).subscribe((result) => {
        this.homePlayersList = result;
      });
      this.dataService.getJugadoresEquipoVisitante(this.gameService.currentGameId).subscribe((result) => {
        this.awayPlayerList = result;
      });
      this.dataService.getGoals(this.gameService.currentGameId).subscribe((result) => {
        this.goalList = result;
      });
      this.dataService.getCorners(this.gameService.currentGameId).subscribe((result) => {
        this.cornerList = result;
      });
    }
  }

  onFinalMinuteChange(event: any){
    this.finalMinute = event.target.value;
  }

  onInitialMinuteChange(event: any){
    this.initialMinute = event.target.value;
  }

  async initialPosition() {
    this.cleanData()
    await this.mapService.fetchBallData(1);
    await this.mapService.fetchPlayerData(1);
    this.mapService.ballMarker = this.mapService.representBall(0, this.mapService.dataFramesBall);
    this.mapService.playersGroup = this.mapService.representPlayers(0, this.mapService.dataFramePlayers);
    this.mapService.createPopups();
  }

  async startVisualization() {
    this.cleanData();
    if (this.initialMinute >= this.finalMinute) {
      alert('Final minute has to be greater than initial minute.')
    } else {
      this.initialFrame = this.initialMinute * 60 * this.mapService.fps;
      this.finalFrame = this.finalMinute * 60 * this.mapService.fps;
      this.mapService.currentFrame = this.initialFrame;
      this.mapService.maxFrame = this.finalFrame;
      await this.resume();
    }
  }
  cleanData(){
    this.pause()
    this.mapService.gameTime = '';
    this.mapService.players = []
    this.mapService.removeMarkers();
  }

  pause(){
    this.mapService.isPaused = true;
    if (this.mapService.players.length>0){
      this.mapService.createPopups();
    }
  }

  async resume() {
    this.pause()
    await new Promise((resolve) => setTimeout(resolve, 1000))
    this.mapService.isPaused = false;
    await this.mapService.framesLoop()
  }

  async onSelectEvent(value: number) {
    this.cleanData();
    this.initialFrame = value - 10 * this.mapService.fps;
    this.finalFrame = value + 10 * this.mapService.fps;
    this.mapService.currentFrame = this.initialFrame;
    this.mapService.maxFrame = this.finalFrame;
    await this.resume();
  }

  async onSelectPlayer(value: any) {
    this.cleanData();
    this.chartService.selectedPlayer = value;
    if (this.gameService.currentGameId!=null){
      await this.chartService.fetchHeatData(this.chartService.selectedPlayer.optaId, this.gameService.currentGameId);
    }
    var heatmapData = {
      max: 500,
      blur: 15,
      data: this.chartService.heatData,
    }
    // @ts-ignore
    this.chartService.heatMarker = L.heatLayer([], {radius: 25}).addTo(this.mapService.map);

    this.chartService.heatMarker.setLatLngs(heatmapData.data)
    this.chartService.heatMarker.setOptions({
      max: heatmapData.max,
      blur: heatmapData.blur,
    })
  }

  viewPlayerStats(){
    this.chartService.selectedPlayer = this.selectedPlayer;
    this.router.navigate(['/home/2d-analysis/player-stats']);
  }
}
