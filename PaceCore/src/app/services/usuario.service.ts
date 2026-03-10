import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService, LoginResponse } from './auth.service';
import { UsuarioUpdate } from '../../models/usuario-update.model';
import { Observable } from 'rxjs';
import { EstadisticasSemanalesDTO } from '../../models/estadisticasSemanalesDTO.model';

interface Usuario {
  id: number;
  nombre: string;
  email: string;
  descripcion?: string;
  peso?: number;
  altura?: number;
  rol: string;
  fechaCreacion: string;
  password: string;
  edad?: number;
}

@Injectable({
  providedIn: 'root'
})

export class UsuarioService {

  constructor(private HttpClient: HttpClient, private AuthService: AuthService) { }

  updateProfile(usuario: UsuarioUpdate): Observable<Usuario> {
    const userId = this.AuthService.currentUser?.usuario.id;
    if (!userId) {
      throw new Error("No hay usuario autenticado");
    }
    //el LoginResponse es el tipo de respuesta que espera el cliente
    return this.HttpClient.put<Usuario>("/usuarios/" + userId, usuario);
  }

  deleteProfile(): Observable<void> {
    const userId = this.AuthService.currentUser?.usuario.id;
    if (!userId) {
      throw new Error("No hay usuario autenticado");
    }
    return this.HttpClient.delete<void>("/usuarios/" + userId);
  }

  getEstadisticasSemanales(idUser: number, tipoActividadId?: number) {
    if (tipoActividadId) {
      return this.HttpClient.get<EstadisticasSemanalesDTO[]>("/usuarios/estadisticas/semanales/" + idUser + "?idTipoActividad=" + tipoActividadId);
    } else {
      return this.HttpClient.get<EstadisticasSemanalesDTO[]>("/usuarios/estadisticas/semanales/" + idUser);
    }
  }

}
