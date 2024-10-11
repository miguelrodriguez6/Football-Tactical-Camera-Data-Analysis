import {ErrorHandler, Injectable, NgZone} from "@angular/core";
import {MatSnackBar} from "@angular/material/snack-bar";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: "root"
})
export class GlobalErrorHandler implements ErrorHandler {
  constructor(
    private readonly _snackBar: MatSnackBar,
    private zone: NgZone
  ) {}

  handleError(error: Error | HttpErrorResponse): void {
    if (error instanceof HttpErrorResponse) {
      this.zone.run(() => {
        let message: string;
        switch (error.status) {
          case 400:
            message = 'Bad Request';
            break;
          case 401:
            message = 'Unauthorized';
            break;
          case 403:
            message = 'Forbidden';
            break;
          case 404:
            message = 'Not found';
            break;
          case 500:
            message = 'Internal Server Error';
            break;
          case 503:
            message = 'Service Unavailable';
            break;
          case 504:
            message = 'Gateway Timeout';
            break;
          default:
            message = 'Something went wrong';
            break;
        }
        this._snackBar.open(message, 'Close', {
          duration: 3000
        });
      });
    } else {
      console.error(error);
    }
  }
}
