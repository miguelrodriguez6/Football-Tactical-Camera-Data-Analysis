import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FooterComponent} from "./core/components/footer.component";
import {RouterModule} from "@angular/router";
import {NavbarComponent} from "./core/components/navbar.component";
import {VideoLoaderComponent} from "./modules/video-filter/video-loader/video-loader.component";
import {MatToolbar} from "@angular/material/toolbar";
import {MatAnchor, MatButton, MatIconButton, MatMiniFabButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatSidenav, MatSidenavContainer} from "@angular/material/sidenav";
import {NgOptimizedImage} from "@angular/common";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {VideoFilterComponent} from "./modules/video-filter/video-filter.component";
import {MatCard, MatCardActions, MatCardContent, MatCardHeader} from "@angular/material/card";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HomePageComponent} from "./modules/home-page/home-page.component";
import {HttpClientModule} from "@angular/common/http";
import {LoginPageComponent} from "./modules/login-page/login-page.component";
import {RegisterPageComponent} from "./modules/register-page/register-page.component";
import {MatInput} from "@angular/material/input";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {VideoClipsComponent} from "./modules/video-clips/video-clips.component";
import {TwoDimensionsPitchComponent} from "./modules/two-dimenssions-pitch/two-dimensions-pitch.component";
import {MatSlider, MatSliderModule, MatSliderThumb} from "@angular/material/slider";
import {LoadPitchComponent} from "./modules/two-dimenssions-pitch/load-pitch/load-pitch.component";
import {MatExpansionPanelActionRow} from "@angular/material/expansion";
import {MatTooltip} from "@angular/material/tooltip";
import {PlayerMapInfoComponent} from "./modules/two-dimenssions-pitch/player-map-info/player-map-info.component";
import {PlayerStatsComponent} from "./modules/player-stats/player-stats.component";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {WelcomePageComponent} from "./modules/welcome-page/welcome-page.component";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {SelectGameComponent} from "./modules/select-game/select-game.component";
import {AddGameComponent} from "./modules/add-game/add-game.component";
import {MatStep, MatStepLabel, MatStepper, MatStepperNext, MatStepperPrevious} from "@angular/material/stepper";
import {ExportResumeComponent} from "./modules/export-resume/export-resume.component";
import {MatProgressBar} from "@angular/material/progress-bar";

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    NavbarComponent,
    VideoLoaderComponent,
    VideoFilterComponent,
    HomePageComponent,
    LoginPageComponent,
    RegisterPageComponent,
    VideoClipsComponent,
    TwoDimensionsPitchComponent,
    LoadPitchComponent,
    PlayerMapInfoComponent,
    PlayerStatsComponent,
    WelcomePageComponent,
    SelectGameComponent,
    AddGameComponent,
    ExportResumeComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        RouterModule.forRoot([]),
        MatToolbar,
        MatIconButton,
        MatIcon,
        MatSidenavContainer,
        MatSidenav,
        MatAnchor,
        NgOptimizedImage,
        MatCardContent,
        MatCard,
        MatFormField,
        MatSelect,
        MatOption,
        FormsModule,
        MatButton,
        HttpClientModule,
        MatInput,
        MatCardHeader,
        MatError,
        MatGridList,
        MatGridTile,
        MatLabel,
        MatSlider,
        MatSliderThumb,
        MatSliderModule,
        ReactiveFormsModule,
        MatExpansionPanelActionRow,
        MatTooltip,
        MatMenuTrigger,
        MatMenu,
        MatMenuItem,
        MatMiniFabButton,
        MatRadioGroup,
        MatRadioButton,
        MatCardActions,
        MatStepper,
        MatStep,
        MatStepLabel,
        MatStepperNext,
        MatStepperPrevious,
        MatProgressBar
    ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
