import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { EntrenamientoSugeridoDTO } from '../../models/recomendaciones/entrenamientoSugeridoDTO.model';
import { Observable } from 'rxjs';

export enum Estado {
  SUGERIDO = "SUGERIDO",
  ACEPTADO = "ACEPTADO",
  COMPLETADO = "COMPLETADO",
  RECHAZADO = "RECHAZADO"
}

@Injectable({
  providedIn: 'root'
})
export class RecomendacionesService {

  constructor(private http: HttpClient) { }

  getRecomendaciones(userId?: number): Observable<EntrenamientoSugeridoDTO> {
    return this.http.get<EntrenamientoSugeridoDTO>("/recomendaciones/" + userId);
  }

  actualizarEstadoRecomendacion(id: number, estado: Estado): Observable<any> {
    return this.http.put("/recomendaciones/" + id + "/estado/" + estado, {});
  }

}
