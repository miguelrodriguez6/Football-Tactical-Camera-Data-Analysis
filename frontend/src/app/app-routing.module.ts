import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomePageComponent} from "./modules/home-page/home-page.component";
import {VideoFilterComponent} from "./modules/video-filter/video-filter.component";
import {LoginPageComponent} from "./modules/login-page/login-page.component";
import {RegisterPageComponent} from "./modules/register-page/register-page.component";
import {VideoLoaderComponent} from "./modules/video-filter/video-loader/video-loader.component";
import {VideoClipsComponent} from "./modules/video-clips/video-clips.component";
import {TwoDimensionsPitchComponent} from "./modules/two-dimenssions-pitch/two-dimensions-pitch.component";
import {LoadPitchComponent} from "./modules/two-dimenssions-pitch/load-pitch/load-pitch.component";
import {PlayerStatsComponent} from "./modules/player-stats/player-stats.component";
import {WelcomePageComponent} from "./modules/welcome-page/welcome-page.component";
import {SelectGameComponent} from "./modules/select-game/select-game.component";
import {AddGameComponent} from "./modules/add-game/add-game.component";
import {ExportResumeComponent} from "./modules/export-resume/export-resume.component";

const routes: Routes = [
  {
    path: '',
    component: WelcomePageComponent
  },
  {
    path: 'home',
    component: HomePageComponent
  },
  {
    path: 'select-game',
    component: SelectGameComponent
  },
  {
    path: 'add-game',
    component: AddGameComponent
  },
  {
    path: 'home/analysis',
    component: VideoFilterComponent,
    children: [
      {
        path: 'clips',
        component: VideoClipsComponent
      },
      {
        path: 'video',
        component: VideoLoaderComponent
      }
    ]
  },
  {
    path: 'home/2d-analysis',
    component: TwoDimensionsPitchComponent,
    children: [
      {
        path: 'map',
        component: LoadPitchComponent,
      },
    ]
  },
  {
    path: 'home/2d-analysis/player-stats',
    component: PlayerStatsComponent
  },
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: 'register',
    component: RegisterPageComponent
  },
  {
    path: 'home/export-resume',
    component: ExportResumeComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
