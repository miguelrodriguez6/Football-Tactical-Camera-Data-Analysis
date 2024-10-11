import {Component, OnInit} from '@angular/core';
import {DataService} from "../../core/services/data.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-form',
  templateUrl: './video-filter.component.html',
  styleUrls: ['./video-filter.component.scss']
})
export class VideoFilterComponent {
  selectedTeam: string;
  selectedPlay: string;
  selectedPlayer: string;

  teams: {label: string, value: string}[] = [
    {label: 'Local', value: 'home'},
    {label: 'Visitante', value: 'away'}
  ];
  plays: {label: string, value: string}[] = [
    {label: 'Contraataque', value: 'contraataque'},
    {label: 'Desmarque en apoyo', value: 'desmarque_apoyo'},
    {label: 'Desmarque en ruptura', value: 'desmarque_ruptura'},
    {label: 'Ataque por banda', value: 'ataque_banda'}
  ];

  constructor(protected readonly dataService: DataService,
              private readonly _snackBar: MatSnackBar) {
    this.selectedTeam = this.teams[0].value;
    this.selectedPlay = this.plays[0].value;
    this.selectedPlayer = '';
  }

  onSubmit() {
    this.dataService.enviarClipsData(this.selectedTeam, this.selectedPlay);
    this._snackBar.open('Cargando...', '', {
      duration: 2000
    });
  }

  onPlayerFilter(){
    this.dataService.filteredClipsData(this.selectedTeam, this.selectedPlay, this.selectedPlayer);
  }
}
