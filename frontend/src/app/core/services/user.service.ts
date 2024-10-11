import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {CryptoService} from "./crypto.service";

@Injectable({
  providedIn: 'root'
})
export class UserService{

  loggedUserId: number | null;
  loggedUser: string | null;
  token: string | undefined;

  constructor(private http: HttpClient,
              protected readonly router: Router,
              protected readonly cryptoService: CryptoService) {
    const userId = localStorage.getItem('userId');
    let storedUserId: string | null = null;
    if (userId!=null){
      storedUserId = cryptoService.decrypt(userId);
    }
    if (storedUserId != null) {
      const parsedUserId = parseInt(storedUserId, 10);
      this.loggedUserId = isNaN(parsedUserId) ? null : parsedUserId;
    } else {
      this.loggedUserId = null;
    }
    this.loggedUser = localStorage.getItem('user');
  }

  cleanUserData(){
    this.loggedUser = null;
  }

  logInUser(username: string, password: string): Observable<any>{
    const url = 'http://localhost:5000/user/login';
    const data = { userName: username, password: password };
    return this.http.post<any>(url, data);
  }

  async postLogIn(username: string, password: string) {
    try {
      await new Promise((resolve, reject) => {
        this.logInUser(username, password).subscribe({
          next: data => {
            this.loggedUserId = data.id;
            this.loggedUser = data.userName;
            resolve(data);
          },
          error: err => reject(err)
        });
      });
      if (this.loggedUserId !== null) {
        const encryptedUserId = this.cryptoService.encrypt(this.loggedUserId.toString());
        localStorage.setItem('userId', encryptedUserId);

      }
      if (typeof this.loggedUser === "string") {
        localStorage.setItem('user', this.loggedUser);
      }
    } catch (error) {
      console.error('Error al obtener datos:', error);
      throw error;
    }
  }

  signInUser(userName: string, password: string, firstName: string, lastName: string, email: string): Observable<any>{
    const url = 'http://localhost:5000/user/signUp';
    const data = { userName: userName, password: password, firstName: firstName, lastName: lastName, email: email};
    return this.http.post<any>(url, data);
  }

  async postSignIn(userName: string, password: string, firstName: string, lastName: string, email: string) {
    try {
      await new Promise((resolve, reject) => {
        this.signInUser(userName, password, firstName, lastName, email).subscribe({
          next: data => resolve(data),
          error: err => reject(err)
        });
      });
    } catch (error) {
      console.error('Error al obtener datos:', error);
      throw error;
    }
  }

  logOutUser(){
    this.loggedUser = null;
    this.loggedUserId = null;
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
    localStorage.removeItem('gameId');
    this.router.navigate(['/login']);

  }
}
