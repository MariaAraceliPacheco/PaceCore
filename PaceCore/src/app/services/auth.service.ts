
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

export interface LoginResponse {
  token: string;
  usuario: {
    id: number;
    nombre: string;
    email: string;
    descripcion?: string;
    peso?: number;
    altura?: number;
    rol: string;
    edad?: number;
  }
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // Al usar un proxy, no necesitamos la URL completa 'http://localhost:8080'.
  // Al poner '/auth/login', las peticiones finales serán:
  // Login:    /auth/login/login
  // Register: /auth/login/register
  // El proxy redirigirá esto correctamente al puerto 8080.
  private apiUrl = '/auth';

  // BehaviorSubject mantiene el estado del usuario. 
  // 'null' significa que nadie ha iniciado sesión todavía.
  private userSubject = new BehaviorSubject<LoginResponse | null>(null);

  // Exponemos el estado como un Observable ($) para que los componentes puedan "escuchar" cambios.
  public user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) { }

  /**
   * Intenta recuperar la sesión del usuario si hay un token guardado.
   * Devuelve un Observable para que APP_INITIALIZER pueda esperar a que termine.
   */
  public restoreSession(): Observable<any> {
    const token = localStorage.getItem('token');
    if (token) {
      return this.getProfile().pipe(
        tap((usuario) => {
          this.userSubject.next({ token, usuario });
        }),
        catchError(() => {
          this.logout();
          return of(null);
        })
      );
    }
    return of(null);
  }

  /**
   * Obtiene el perfil del usuario autenticado actualmente.
   */
  getProfile(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/me?token=${localStorage.getItem('token')}`);
  }

  /**
   * Intenta iniciar sesión con email y contraseña.
   * @param email Correo del usuario
   * @param password Contraseña
   */
  login(email: string, password: string): Observable<LoginResponse> {
    // La petición se hace a través del proxy: /auth/login
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { email, password: password })
      .pipe(
        tap(user => {
          localStorage.setItem('token', user.token);
          // Si el login es correcto, guardamos el usuario en nuestro "estado"
          this.userSubject.next(user);
        })
      );
  }

  /**
   * Registra un nuevo usuario en la base de datos.
   */
  register(nombre: string, email: string, password: string, descripcion: string, peso: number, altura: number, edad: number): Observable<LoginResponse> {
    // La petición se hace a través del proxy: /auth/register
    return this.http.post<LoginResponse>(`${this.apiUrl}/register`, {
      nombre,
      email,
      password,
      descripcion,
      peso,
      altura,
      edad
    })
      .pipe(
        tap(user => {
          localStorage.setItem('token', user.token);
          // Tras registrarse, automáticamente lo marcamos como usuario logueado
          this.userSubject.next(user);
        })
      );
  }

  /**
   * Limpia el estado del usuario (Cerrar sesión).
   */
  logout() {
    localStorage.removeItem('token');
    this.userSubject.next(null);
  }

  /**
   * Actualiza los datos del usuario en el estado global.
   */
  updateCurrentUser(usuario: any) {
    const current = this.userSubject.value;
    if (current) {
      this.userSubject.next({ ...current, usuario: { ...current.usuario, ...usuario } });
    }
  }

  /**
   * Devuelve los datos del usuario actual sin necesidad de suscribirse.
   */
  get currentUser() {
    return this.userSubject.value;
  }
}
