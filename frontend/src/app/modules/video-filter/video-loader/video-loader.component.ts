import {Component} from "@angular/core";
import {GamesService} from "../../../core/services/games.service";

@Component({
  selector: 'app-video-loader',
  template:
    `
      <video width="900" height="auto" controls>
        <source src="../../../../assets/media/video/{{gameService.currentGame.videoUrl}}" type="video/mp4">
        Your browser does not support the video tag.
      </video>
    `
})
export class VideoLoaderComponent {
  constructor(protected readonly gameService: GamesService) {
  }
}
