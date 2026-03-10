import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';
import { ZonasResponseDTO } from '../../models/zonasResponseDTO.model';
import { ZonasUpdateDTO } from '../../models/zonasUpdateDTO.model';
import { Observable } from 'rxjs';
import { PorcentajesZonasDTO } from '../../models/PorcentajesZonasDTO.model';
import { ActividadConteoDTO } from '../../models/actividadConteoDTO.model';

@Injectable({
  providedIn: 'root'
})
export class ZonasService {

  private apiUrl = '/zonas';
  private apiUrl2 = '/zonas/porcentajesZonas';
  private tipoActividadApiUrl = '/actividades/recuentoTiposActividad';
  constructor(private http: HttpClient, private authService: AuthService) {
  }

  getZonas(userId: number): Observable<ZonasResponseDTO[]> {
    return this.http.get<ZonasResponseDTO[]>(this.apiUrl + '/' + userId);
  }

  updateZonas(zonas: ZonasUpdateDTO[], userId: number): Observable<ZonasResponseDTO[]> {
    return this.http.put<ZonasResponseDTO[]>(this.apiUrl + "/" + userId, zonas);
  }

  getPorcentajesZonas(userId: number, idTipoActividad?: number, inicio?: Date, fin?: Date) {
    const inicioStr = inicio ? this.formatDateForBackend(inicio) : null;
    const finStr = fin ? this.formatDateForBackend(fin) : null;

console.log(inicioStr, finStr, idTipoActividad)

    //si existe el tipo de actividad...
    if (idTipoActividad) {
      //se comprueba si existe la fecha de inicio y fin
      if (inicioStr && finStr) {
        return this.http.get<PorcentajesZonasDTO>(`${this.apiUrl2}/${userId}?idTipoActividad=${idTipoActividad}&inicio=${inicioStr}&fin=${finStr}`);
      } else { //si no, se devuelve solamente filtrado por tipo de actividad
        return this.http.get<PorcentajesZonasDTO>(`${this.apiUrl2}/${userId}?idTipoActividad=${idTipoActividad}`);
      }
      //si no existe tipo de actividad pero sí que existe la fecha...
    } else if (inicioStr && finStr) {
      return this.http.get<PorcentajesZonasDTO>(`${this.apiUrl2}/${userId}?inicio=${inicioStr}&fin=${finStr}`);
    } else { //si no existe ni tipo de actividad ni fecha, se devuelve todo
      return this.http.get<PorcentajesZonasDTO>(`${this.apiUrl2}/${userId}`);
    }
  }

  getConteoActividades(userId: number, inicio?: Date, fin?: Date): Observable<ActividadConteoDTO[]> {
    const inicioStr = inicio ? this.formatDateForBackend(inicio) : null;
    const finStr = fin ? this.formatDateForBackend(fin) : null;

    if (inicioStr && finStr) {
      return this.http.get<ActividadConteoDTO[]>(this.tipoActividadApiUrl + "/" + userId + "?inicio=" + inicioStr + "&fin=" + finStr);
    } else {
      return this.http.get<ActividadConteoDTO[]>(this.tipoActividadApiUrl + "/" + userId);
    }
  }


  private formatDateForBackend(date: Date): string {
    // Convierte a formato LocalDateTime (yyyy-MM-ddTHH:mm:ss)
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }

}
