import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../../core/services/user.service";
import {GamesService} from "../../core/services/games.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'add-game-app',
  templateUrl: 'add-game.component.html',
  styleUrl: 'add-game.component.scss'
})
export class AddGameComponent implements OnInit{
  // @ts-ignore
  stepOneForm: FormGroup;

  constructor(private fb: FormBuilder,
              private http: HttpClient,
              protected readonly userService: UserService,
              protected readonly gameService: GamesService,
              private snackbar: MatSnackBar) {}
  ngOnInit(): void {
    this.stepOneForm = this.fb.group({
      gameName: ['', Validators.required]
    });
  }

  fileNameData = '';
  fileNameMetadata = '';
  fileNameInsight = '';
  fileNamePhysicalSummary = '';
  fileNameVideo = '';

  gameId: number | undefined;
  gameName: string = '';
  gameSelected: string = '';

  currentFile: File | undefined;

  async uploadData() {
    this.snackbar.open("Cargando...", 'Close');
    try {
      await new Promise((resolve, reject) => {
        if (this.currentFile){
          this.fileNameData = this.currentFile.name;
          const formData = new FormData();
          formData.append("file", this.currentFile);
          if (this.userService.loggedUserId!=null){
            formData.append("userId", this.userService.loggedUserId.toString());
          }
          if (this.gameService.currentGameId!=null){
            formData.append("gameId", this.gameService.currentGameId.toString());
          }
          this.http.post("http://localhost:5000/games/upload/data", formData).subscribe({
            next: data => resolve(data),
            error: err => reject(err)
          });
        }
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
    this.snackbar.dismiss();
  }

  async uploadInsight() {
    this.snackbar.open("Cargando...", 'Close');
    try {
      await new Promise((resolve, reject) => {
        if (this.currentFile){
          this.fileNameData = this.currentFile.name;
          const formData = new FormData();
          formData.append("file", this.currentFile);
          if (this.userService.loggedUserId!=null){
            formData.append("userId", this.userService.loggedUserId.toString());
          }
          if (this.gameService.currentGameId!=null){
            formData.append("gameId", this.gameService.currentGameId.toString());
          }
          this.http.post("http://localhost:5000/games/upload/insight", formData).subscribe({
            next: data => resolve(data),
            error: err => reject(err)
          });
        }
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
    this.snackbar.dismiss();
  }

  async uploadMetadata() {
    this.snackbar.open("Cargando...", 'Close');
    try {
      await new Promise((resolve, reject) => {
        if (this.currentFile){
          this.fileNameData = this.currentFile.name;
          const formData = new FormData();
          formData.append("file", this.currentFile);
          if (this.userService.loggedUserId!=null){
            formData.append("userId", this.userService.loggedUserId.toString());
          }
          if (this.gameService.currentGameId!=null){
            formData.append("gameId", this.gameService.currentGameId.toString());
          }
          this.http.post("http://localhost:5000/games/upload/metadata", formData).subscribe({
            next: data => resolve(data),
            error: err => reject(err)
          });
        }
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
    this.snackbar.dismiss();
  }

  async uploadPhysicalSummary() {
    this.snackbar.open("Cargando...", 'Close');
    try {
      await new Promise((resolve, reject) => {
        if (this.currentFile) {
          this.fileNameData = this.currentFile.name;
          const formData = new FormData();
          formData.append("file", this.currentFile);
          if (this.gameService.currentGameId!=null){
            formData.append("gameId", this.gameService.currentGameId.toString());
          }
          this.http.post("http://localhost:5000/games/upload/summary", formData).subscribe({
            next: data => resolve(data),
            error: err => reject(err)
          });
        }
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
    this.snackbar.dismiss();
  }

  async uploadMp4Video() {
    this.snackbar.open("Cargando...", 'Close');
    try {
      await new Promise((resolve, reject) => {
        if (this.currentFile) {
          this.fileNameData = this.currentFile.name;
          const formData = new FormData();
          formData.append("file", this.currentFile);
          this.http.post("http://localhost:5000/games/upload/video", formData).subscribe({
            next: data => resolve(data),
            error: err => reject(err)
          });
        }
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
    }
    this.snackbar.dismiss();
  }

  selectFile(event: any){
    this.currentFile = event.target.files[0];
  }

}
