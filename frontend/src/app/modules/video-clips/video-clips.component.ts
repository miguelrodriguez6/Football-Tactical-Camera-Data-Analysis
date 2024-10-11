import {Component} from "@angular/core";
import {DataService} from "../../core/services/data.service";
import {GamesService} from "../../core/services/games.service";

@Component({
  selector: 'app-video-clips',
  templateUrl: 'video-clips.component.html',
  styleUrl: 'video-clips.component.scss'
})
export class VideoClipsComponent{

  constructor(protected readonly dataService: DataService,
              protected readonly gameService: GamesService) {
  }

  convertirSegundosAMinutos(segundos: number): string {
    const minutos = Math.floor(segundos / 60) - 1;
    let segundosRestantes = (segundos % 60);
    if (segundosRestantes>=3){
      segundosRestantes=segundosRestantes-3;
    } else {
      segundosRestantes = 0;
    }
    return `${minutos}:${segundosRestantes < 10 ? '0' : ''}${segundosRestantes}`;
  }

  toggleFullScreen(videoId: string) {
    const videoElement = document.getElementById(videoId) as HTMLVideoElement;
    if (videoElement.requestFullscreen) {
      videoElement.requestFullscreen();
    } else { // @ts-ignore
      if (videoElement.mozRequestFullScreen) { /* Firefox */
        // @ts-ignore
            videoElement.mozRequestFullScreen();
        // @ts-ignore
          } else if (videoElement.webkitRequestFullscreen) { /* Chrome, Safari & Opera */
        // @ts-ignore
            videoElement.webkitRequestFullscreen();
        // @ts-ignore
          } else if (videoElement.msRequestFullscreen) { /* IE/Edge */
        // @ts-ignore
            videoElement.msRequestFullscreen();
      }
    }
    // videoElement.click();
  }
}
