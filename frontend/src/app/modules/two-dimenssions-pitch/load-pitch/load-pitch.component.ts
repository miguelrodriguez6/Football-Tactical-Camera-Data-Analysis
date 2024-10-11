import {AfterViewInit, Component} from "@angular/core";
import * as L from 'leaflet';
import {Map2dService} from "../../../core/services/map-2d.service";

@Component({
  selector: 'load-pitch-app',
  templateUrl: 'load-pitch.component.html',
  styleUrl: 'load-pitch.component.scss'
})
export class LoadPitchComponent implements AfterViewInit{

  constructor(protected readonly mapService: Map2dService) {
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    // if (this.mapService.map!=undefined){
    //   this.mapService.map.remove();
    // }

    // if (this.mapService.map==undefined){
      this.mapService.map = L.map('map', {
        minZoom: 1,
        maxZoom: 4,
        crs: L.CRS.Simple
      });
      var bounds: L.LatLngBoundsExpression = [
        [0, 0],
        [this.mapService.imgHeight, this.mapService.imgWidth]
      ]
      L.imageOverlay('../../../../assets/media/images/campo_futbol.png', bounds).addTo(this.mapService.map)
      this.mapService.map.fitBounds(bounds)
    // }
  }
}
