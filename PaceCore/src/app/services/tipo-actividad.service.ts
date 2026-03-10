import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { TipoActividad } from '../../models/tipo-actividad.model';

@Injectable({
  providedIn: 'root'
})
export class TipoActividadService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  getTipoActividadesDelUsuario(): Observable<TipoActividad[]> {
    let userId = this.authService.currentUser?.usuario.id;
    return this.http.get<TipoActividad[]>('/actividades/tiposActividadUsuario/' + userId);
  }
}
