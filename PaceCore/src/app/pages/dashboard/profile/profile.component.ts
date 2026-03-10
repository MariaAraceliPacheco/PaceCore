import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../../services/usuario.service';
import { UsuarioUpdate } from '../../../../models/usuario-update.model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../components/confirm-dialog/confirm-dialog.component';
import { Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FormsModule } from '@angular/forms';
import { ZonasService } from '../../../services/zonas.service';
import { ZonasResponseDTO } from '../../../../models/zonasResponseDTO.model';
import { ZonasUpdateDTO } from '../../../../models/zonasUpdateDTO.model';

interface LoginResponse {
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

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [AsyncPipe, CommonModule, FormsModule, MatDialogModule, MatSnackBarModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  //en el auth.service, la variable $user es un Observable que contiene el usuario actual
  //el observale devuelve un objeto de tipo Observable que puede ser LoginResponse o null
  user$!: Observable<LoginResponse | null>;

  nombre: string = '';
  email: string = '';
  descripcion: string = '';
  peso: number = 0;
  altura: number = 0;
  edad: number = 0;
  zonas: ZonasResponseDTO[] = [];
  zonasEdit: ZonasResponseDTO[] = [];
  currentUserId?: number;
  isEditingZones: boolean = false;

  constructor(
    private authService: AuthService,
    private usuarioService: UsuarioService,
    private dialog: MatDialog,
    private router: Router,
    private snackBar: MatSnackBar,
    private zonasService: ZonasService
  ) { }

  ngOnInit(): void {
    //otra forma de hacerlo
    //this.user = this.authService.currentUser;
    this.user$ = this.authService.user$;

    // Rellenamos los campos cuando el usuario esté disponible
    this.authService.user$.subscribe(user => {
      if (user) {
        this.currentUserId = user.usuario.id;
        this.nombre = user.usuario.nombre;
        this.email = user.usuario.email;
        this.descripcion = user.usuario.descripcion || '';
        this.peso = user.usuario.peso || 0;
        this.altura = user.usuario.altura || 0;
        this.edad = user.usuario.edad || 0;
        console.log(user);

        // Llamamos a fetchZonas una vez tenemos el ID
        this.fetchZonas(user.usuario.id);
      }
    });
  }

  fetchZonas(userId: number) {
    this.zonasService.getZonas(userId).subscribe({
      next: (zonas) => {
        this.zonas = zonas.sort((a, b) => a.numero_zona - b.numero_zona);
      },
      error: (error) => {
        console.error("Error al obtener las zonas", error);
      }
    });
  }

  toggleEditZones() {
    this.isEditingZones = true;
    // Creamos una copia profunda o al menos de los objetos para editar sin tocar el original
    this.zonasEdit = JSON.parse(JSON.stringify(this.zonas));
  }

  cancelEditZones() {
    this.isEditingZones = false;
    this.zonasEdit = [];
  }

  saveZonasBulk() {
    // Validamos brevemente los rangos
    const hasError = this.zonasEdit.some(z => z.fc_minima >= z.fc_maxima);
    if (hasError) {
      this.snackBar.open('Error: La FC mínima debe ser menor que la máxima en todas las zonas', 'Cerrar', { duration: 5000 });
      return;
    }

    // Mapeamos al DTO que espera la API
    const dtos: ZonasUpdateDTO[] = this.zonasEdit.map(z => ({
      id: z.id,
      nombre_zona: z.nombre_zona,
      descripcion: z.descripcion,
      fc_minima: z.fc_minima,
      fc_maxima: z.fc_maxima,
      numero_zona: z.numero_zona
    }));

    console.log("Saving bulk zones:", dtos);

    this.zonasService.updateZonas(dtos, this.currentUserId!).subscribe({
      next: () => {
        this.snackBar.open('Zonas actualizadas correctamente', 'Cerrar', { duration: 3000 });
        this.isEditingZones = false;
        if (this.currentUserId) {
          this.fetchZonas(this.currentUserId);
        }
      },
      error: (error: any) => {
        console.error("Error al actualizar las zonas", error);
        this.snackBar.open('Error al actualizar las zonas', 'Cerrar', { duration: 5000 });
      }
    });
  }

  saveProfile() {
    const usuarioUpdate: UsuarioUpdate = {
      nombre: this.nombre!,
      email: this.email!,
      descripcion: this.descripcion!,
      peso: this.peso!,
      altura: this.altura!,
      edad: this.edad!,
    }
    this.usuarioService.updateProfile(usuarioUpdate).subscribe({
      next: (usuario) => {
        console.log("usuario modificado correctamente", usuario);
        // Actualizamos el estado global para que el resto de la app (sidebar, overview) se entere
        this.authService.updateCurrentUser(usuario);
        this.snackBar.open('¡Perfil actualizado correctamente!', 'Cerrar', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom',
        });
      }, error: (error) => {
        console.log("error al modificar el usuario", error);
      }
    })
  }

  deleteProfile() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Eliminar cuenta',
        message: '¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.',
        confirmText: 'Eliminar',
        cancelText: 'Cancelar'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.usuarioService.deleteProfile().subscribe({
          next: () => {
            console.log("Usuario eliminado correctamente");
            this.authService.logout();
            this.router.navigate(['/']).then(() => {
              this.dialog.open(ConfirmDialogComponent, {
                data: {
                  title: 'Cuenta eliminada',
                  message: 'Tu cuenta ha sido eliminada correctamente. ¡Esperamos volver a verte pronto!',
                  confirmText: 'Aceptar',
                  showCancel: false
                }
              });
            });
          },
          error: (error) => {
            console.log("Error al eliminar el usuario", error);
          }
        });
      }
    });
  }

}
