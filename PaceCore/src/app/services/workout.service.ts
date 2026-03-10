import { Injectable } from '@angular/core';
import { AuthService, LoginResponse } from './auth.service';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, shareReplay, tap, catchError, of } from 'rxjs';
import { TipoActividad } from '../../models/tipo-actividad.model';
import { Entreno } from '../../models/entreno.model';
import { EntrenoCreate } from '../../models/entreno-create.model';
import { Estadisticas } from '../../models/estadisticas.model';

@Injectable({
    providedIn: 'root'
})
export class WorkoutService {

    private apiUrl = '/entrenos';

    private currentUser: LoginResponse | null = null;

    //un sujeto es un objeto que emite eventos
    // Sujeto para manejar el estado reactivo de los entrenamientos
    private entrenosSubject = new BehaviorSubject<Entreno[]>([]);
    // Observable al que se suscribirán los componentes
    public entrenos$ = this.entrenosSubject.asObservable();
    public actividades$: Observable<TipoActividad[]>;

    constructor(private authService: AuthService, private http: HttpClient) {
        // Inicializamos el observable de actividades
        this.actividades$ = this.http.get<TipoActividad[]>('/actividades').pipe(
            tap(act => console.log('Actividades cargadas desde la API:', act)),
            shareReplay(1),
            catchError(err => {
                console.error('Error cargando actividades:', err);
                return of([]); // Devolvemos lista vacía en caso de error
            })
        );

        // Nos suscribimos al usuario para tener siempre su ID actualizado
        this.authService.user$.subscribe(user => {
            this.currentUser = user;
            if (user) {
                this.refreshEntrenos(); // Cargar datos al iniciar sesión
            } else {
                this.entrenosSubject.next([]); // Limpiar al cerrar sesión
            }
        });
    }

    getActividades(): Observable<TipoActividad[]> {
        return this.http.get<TipoActividad[]>('/actividades');
    }

    addWorkout(workout: EntrenoCreate): void {
        this.crearEntreno(workout).subscribe({
            next: (savedWorkout) => {
                console.log('Workout added successfully:', savedWorkout);
                this.refreshEntrenos(); // Recargar la lista automáticamente
            },
            error: (error) => {
                console.error('Error adding workout:', error);
            }
        });
    }

    crearEntreno(entreno: EntrenoCreate): Observable<Entreno> {
        return this.http.post<Entreno>(this.apiUrl, entreno);
    }

    updateWorkout(id: number, entreno: EntrenoCreate): void {
        this.http.put<Entreno>(this.apiUrl + "/" + id, entreno).subscribe({
            next: (savedWorkout) => {
                console.log('Workout updated successfully:', savedWorkout);
                this.refreshEntrenos();
            },
            error: (error) => {
                console.error('Error updating workout:', error);
            }
        });
    }

    getEntrenos(tipoActividadId?: number): Observable<Entreno[]> {
        if (tipoActividadId && tipoActividadId != 0) {
            return this.http.get<Entreno[]>(this.apiUrl + "/entrenosPorUsuario/" + this.currentUser?.usuario.id + "?tipoActividadId=" + tipoActividadId);
        }
        return this.http.get<Entreno[]>(this.apiUrl + "/entrenosPorUsuario/" + this.currentUser?.usuario.id);
    }

    //El onbaservable que devuelve sirve para que el componente pueda suscribirse y actualizar la lista de entrenos 
    eliminarEntreno(id: number): Observable<void> {
        return this.http.delete<void>(this.apiUrl + "/" + id);
    }

    // Método para forzar la actualización de la lista
    refreshEntrenos(): void {
        if (this.currentUser) {
            this.getEntrenos().subscribe({
                //next es un callback que se ejecuta cuando el observable emite un valor
                //cuando entrenosSubject emite un valor, se actualiza la lista de entrenos
                next: (entrenos) => this.entrenosSubject.next(entrenos),
                error: (err) => console.error('Error al refrescar entrenos:', err)
            });
        }
    }

    getEntreno(id: number): Observable<Entreno> {
        return this.http.get<Entreno>(this.apiUrl + "/" + id);
    }

    getEstadisticas(idUser: number): Observable<Estadisticas> {
        return this.http.get<Estadisticas>("/usuarios/estadisticas/" + idUser);
    }



}
