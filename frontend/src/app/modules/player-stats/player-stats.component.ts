import {Component, OnInit} from "@angular/core";
import {ChartService} from "../../core/services/chart.service";
import {Location} from "@angular/common";
import {Router} from "@angular/router";
import {Map2dService} from "../../core/services/map-2d.service";
import Chart from 'chart.js/auto';
import {GamesService} from "../../core/services/games.service";
import {StatsService} from "../../core/services/stats.service";

@Component({
  selector: 'player-stats-app',
  templateUrl: 'player-stats.component.html',
  styleUrl: 'player-stats.component.scss'
})
export class PlayerStatsComponent implements OnInit{
  selectedPlayer: any;
  playerStats: any;
  constructor(protected readonly chartService: ChartService,
              private location: Location,
              private router: Router,
              private mapService: Map2dService,
              protected readonly gameService: GamesService,
              protected readonly statsService: StatsService) {
  }

  async ngOnInit() {
    if (this.chartService.selectedPlayer==undefined){
      await this.router.navigate(['2d-analysis/map']);
    } else if (this.gameService.currentGameId){
      this.selectedPlayer = this.chartService.selectedPlayer;
      console.log(this.selectedPlayer)
      this.statsService.getPlayerStats(this.gameService.currentGameId, this.selectedPlayer.optaId).subscribe((data: any) => {
        this.playerStats = data;
        this.createChart(this.playerStats);
      })
    }
  }

  goBack(){
    this.location.back();
  }

  createChart(data: any) {
    const ctx = document.getElementById('circularChart') as HTMLCanvasElement;
    new Chart(ctx, {
      type: 'radar',
      data: {
        labels: ['Andando', 'Trotando', 'Corriendo', 'Sprint'],
        datasets: [{
          label: 'Kilometros',
          data: [this.convertMetersToKilometers(data.walking), this.convertMetersToKilometers(data.jogging), this.convertMetersToKilometers(data.running), this.convertMetersToKilometers(data.sprinting)],
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          r: {
            suggestedMin: 0,
            suggestedMax: Math.max(this.convertMetersToKilometers(data[2]), this.convertMetersToKilometers(data[3]), this.convertMetersToKilometers(data[4]), this.convertMetersToKilometers(data[5])) + 1
          }
        }
      }
    });
  }

  convertMetersToKilometers(meters: number): number {
    if (isNaN(meters)) {
      return 0;
    }
    return parseFloat((meters / 1000).toFixed(2));
  }
}
