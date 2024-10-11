import {Component, OnInit} from "@angular/core";
import {GamesService} from "../../core/services/games.service";
import {DataService} from "../../core/services/data.service";
import {PlayerModel} from "../../shared/domain/player.model";
import {StatsService} from "../../core/services/stats.service";
import {Map2dService} from "../../core/services/map-2d.service";
import Chart from "chart.js/auto";
import {ChartService} from "../../core/services/chart.service";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";

@Component({
  selector: 'export-resume-app',
  templateUrl: 'export-resume.component.html',
  styleUrl: 'export-resume.component.scss'
})
export class ExportResumeComponent implements OnInit{

  homeTeamPlayers: PlayerModel[] = [];
  awayTeamPlayers: PlayerModel[] = [];
  homeTeamPossession: number = 0;
  awayTeamPossession: number = 0;
  homeTeamTotalDistance: number = 0;
  awayTeamTotalDistance: number = 0;
  topThreePlayersByDistance: [] = [];
  events: { minute: number, second: number, name: string, icon: string}[] = [];
  topSpeedPlayers: any[] = [];

  constructor(protected readonly gameService: GamesService,
              protected readonly dataService: DataService,
              protected readonly statsService: StatsService,
              protected readonly mapService: Map2dService,
              protected readonly chartService: ChartService) {
    if (this.gameService.currentGameId!=null){
      this.dataService.getJugadoresEquipoLocal(this.gameService.currentGameId).subscribe(data => {
        this.homeTeamPlayers = data;
      });
      this.dataService.getJugadoresEquipoVisitante(this.gameService.currentGameId).subscribe(data => {
        this.awayTeamPlayers = data;
      })
      this.statsService.getPossessionData(this.gameService.currentGameId).subscribe(data => {
        this.homeTeamPossession = data[0];
        this.awayTeamPossession = data[1];
      });
      if (this.gameService.awayTeamOptaId!=null && this.gameService.homeTeamOptaId!=null){
        this.statsService.getDistanceData(this.gameService.currentGameId, this.gameService.homeTeamOptaId).subscribe(data => {
          this.homeTeamTotalDistance = data;
        })
        this.statsService.getDistanceData(this.gameService.currentGameId, this.gameService.awayTeamOptaId).subscribe(data => {
          this.awayTeamTotalDistance = data;
        });
      }
      this.statsService.getTopThreePlayersDataByStat(this.gameService.currentGameId, 'distance').subscribe(data => {
        this.topThreePlayersByDistance = data;
      })
      this.events = [];
      this.mapService.getEvents(this.gameService.currentGameId).subscribe(data => {
        for (const event of data) {
          // @ts-ignore
          switch(event[0]) {
            case 16:
              // @ts-ignore
              this.dataService.getPlayerDataByOptaId(event[4]).subscribe(data => {
                // @ts-ignore
                this.events.push({minute: event[1], second: event[2], name: data.name, icon: 'sports_soccer'});
              })
              break;
            case 18:
              // @ts-ignore
              this.dataService.getPlayerDataByOptaId(event[4]).subscribe(data => {
                // @ts-ignore
                this.events.push({minute: event[1], second: event[2], name: data.name, icon: 'arrow_back'});
              })
              break;
            case 19:
              // @ts-ignore
              this.dataService.getPlayerDataByOptaId(event[4]).subscribe(data => {
                // @ts-ignore
                this.events.push({minute: event[1], second: event[2], name: data.name, icon: 'arrow_forward'});
              })
              break;
          }
        }
      });
      if (this.gameService.homeTeamOptaId!=null && this.gameService.awayTeamOptaId!=null){
        this.chartService.getTeamStats(this.gameService.currentGameId, this.gameService.homeTeamOptaId).subscribe(data => {
          this.createChart(data[0], 'homeChart', 'Equipo local');
        })
        this.chartService.getTeamStats(this.gameService.currentGameId, this.gameService.awayTeamOptaId).subscribe(data => {
          this.createChart(data[0], 'awayChart', 'Equipo visitante');
        })
      }
      this.statsService.getTopSpeed(this.gameService.currentGameId).subscribe(data => {
        this.topSpeedPlayers = data;
      })
    }
  }

  ngOnInit(): void {

  }

  createChart(data: any, chartId: string, title: string) {
    const ctx = document.getElementById(chartId) as HTMLCanvasElement;
    new Chart(ctx, {
      type: 'radar',
      data: {
        labels: ['Andando', 'Trotando', 'Corriendo', 'Sprint'],
        datasets: [{
          label: 'Kilómetros',
          data: [this.convertMetersToKilometers(data[0]).toFixed(2), this.convertMetersToKilometers(data[1]).toFixed(2), this.convertMetersToKilometers(data[2]).toFixed(2), this.convertMetersToKilometers(data[3]).toFixed(2)],
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          r: {
            suggestedMin: 0,
            suggestedMax: Math.max(data[2], data[3], data[4], data[5]) + 10
          }
        },
        plugins: {
          title: {
            display: true,
            text: title,
            font: {
              size: 18
            }
          }
        }
      }
    });
  }

  export(type: string) {
    const data = document.getElementById('export');
    // @ts-ignore
    html2canvas(data).then(canvas => {
      const imgWidth = 208;
      const imgHeight = canvas.height * imgWidth / canvas.width;
      switch (type){
        case 'PNG':
          // @ts-ignore
          html2canvas(data).then(canvas => {
            const contentDataURL = canvas.toDataURL('image/png');
            const a = document.createElement('a');
            a.href = contentDataURL;
            a.download = this.gameService.gameDescription +  '.png';
            a.click();
          });
          break;
        case 'PDF':
          const contentDataURL = canvas.toDataURL('image/png');
          const pdf = new jsPDF('p', 'mm', 'a4');
          const position = 0;
          pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight);
          pdf.save(this.gameService.gameDescription + '.pdf');
          break;
        case 'JPEG':
          // @ts-ignore
          html2canvas(data).then(canvas => {
            const contentDataURL = canvas.toDataURL('image/jpeg');
            const a = document.createElement('a');
            a.href = contentDataURL;
            a.download = this.gameService.gameDescription +  '.jpeg';
            a.click();
          });
          break;
      }
    });
  }

  convertMetersToKilometers(meters: number): number {
    return meters / 1000;
  }

  protected readonly Date = Date;

  getFormattedTime(timestamp: number): string {
    if (!timestamp) {
      return '';
    }

    const date = new Date(timestamp);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${hours}:${minutes}`;
  }
}
