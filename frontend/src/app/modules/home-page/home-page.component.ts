import {Component, OnInit} from '@angular/core';
import {GamesService} from "../../core/services/games.service";

@Component({
  selector: 'app-home',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent implements OnInit{
  constructor(protected readonly gameService: GamesService) {
  }

  sections = [
    { title: 'Cámara táctica.', description: 'Analiza el partido a través de una cámara táctica y visualiza las jugadas destacadas.', link: 'analysis/video' },
    { title: 'Visualización 2D.', description: 'Analiza el partido en un terreno de dos dimensiones con una clara y precisa visualización de la posición de los jugadores y del balón.', link: '2d-analysis/map' },
    { title: 'Informe del partido.', description: 'Visualiza las principales estadísticas del partido con la posibilidad de exportar el informe a tu equipo.', link: 'export-resume'}
  ];
  isGameSelected: boolean = false;

  async ngOnInit() {
    await this.gameService.fetchGames();
  }

  onSelectionChange(event: any) {
    this.gameService.selectedGame = event.value;
    this.isGameSelected = !!this.gameService.selectedGame;
  }
}
