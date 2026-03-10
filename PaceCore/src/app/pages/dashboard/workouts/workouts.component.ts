import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { WorkoutFormComponent } from './workout-form/workout-form.component';
import { WorkoutPreviewComponent } from './workout-preview/workout-preview.component';
import { WorkoutService } from '../../../services/workout.service';
import { Entreno } from '../../../../models/entreno.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TipoActividad } from '../../../../models/tipo-actividad.model';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { LOCALE_ID } from '@angular/core';
import { ActivityIconPipe } from '../../../pipes/activity-icon.pipe';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { TipoActividadService } from '../../../services/tipo-actividad.service';

registerLocaleData(localeEs);
@Component({
  selector: 'app-workouts',
  standalone: true,
  imports: [MatDialogModule, CommonModule, ActivityIconPipe, WorkoutPreviewComponent, ConfirmDialogComponent, MatButtonToggleModule],
  templateUrl: './workouts.component.html',
  styleUrl: './workouts.component.css',
  providers: [{ provide: LOCALE_ID, useValue: 'es-ES' }]

})
export class WorkoutsComponent {
  entrenos: Entreno[] = [];
  activityTypes: TipoActividad[] = [];
  tiposActividadUsuario: TipoActividad[] = [];

  constructor(private dialog: MatDialog, private workoutService: WorkoutService, private snackBar: MatSnackBar, private tipoActividadService: TipoActividadService) { }

  openWorkoutForm() {
    this.dialog.open(WorkoutFormComponent, {
      width: '800px',
      maxWidth: '95vw',
      panelClass: 'custom-dialog-container'
    });
  }

  verDetalle(entreno: Entreno) {
    this.workoutService.getEntreno(entreno.id).subscribe({
      next: (fullEntreno) => {
        this.dialog.open(WorkoutPreviewComponent, {
          width: '800px',
          maxWidth: '95vw',
          panelClass: 'custom-dialog-container',
          data: { entreno: fullEntreno }
        });
      }
    });
  }

  ngOnInit(): void {
    // Nos suscribimos al observable del servicio para recibir actualizaciones automaticamente
    this.workoutService.entrenos$.subscribe(entrenos => {
      this.entrenos = entrenos;
    });

    this.workoutService.actividades$.subscribe({
      next: (actividades) => {
        console.log('Actividades recibidas en el componente:', actividades);
        this.activityTypes = actividades;
      },
      error: (err) => console.error('Error en la suscripción de actividades:', err)
    });

    this.tipoActividadService.getTipoActividadesDelUsuario().subscribe({
      next: (tiposActividad) => {
        this.tiposActividadUsuario = tiposActividad;
      }
    });

  }

  eliminarEntreno(id: number) {
    const confirmRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Eliminar Entrenamiento',
        message: '¿Estás seguro de que quieres eliminar este entrenamiento? Esta acción no se puede deshacer.'
      }
    });

    confirmRef.afterClosed().subscribe(result => {
      if (result) {
        this.workoutService.eliminarEntreno(id).subscribe({
          //next sirve para manejar el valor emitido por el observable
          //si el observable emite un valor, se ejecuta el callback
          next: () => {
            //esto es el callback
            this.workoutService.refreshEntrenos();
            this.snackBar.open('Entrenamiento eliminado correctamente', 'Cerrar', {
              duration: 5000,
              horizontalPosition: 'center',
              verticalPosition: 'bottom',
            });
          },
          error: (err) => {
            console.error('Error al eliminar el entrenamiento:', err);
          }
        })
      }
    });
  }

  editarEntreno(id: number) {
    this.workoutService.getEntreno(id).subscribe({
      next: (entreno) => {
        this.dialog.open(WorkoutFormComponent, {
          width: '800px',
          maxWidth: '95vw',
          panelClass: 'custom-dialog-container',
          data: { entreno } //estos seran los datos que se le pasen al componente WorkoutFormComponent
        })
      }
    })
  }

  filterEntrenos(tipoActividadId: number) {
    this.workoutService.getEntrenos(tipoActividadId).subscribe({
      next: (entrenos) => {
        this.entrenos = entrenos;
      }
    })
  }

  getZonaClass(zona: number | undefined): string {
    //si el entrenamiento no tiene zona o tiene 0, no se muestra nada
    if (!zona) return '';

    //si no, se hace un mapeo de las zonas 
    //es decir, en caso de que la zona (key) sea 1, se devuelve 'z1'
    const zoneMap: { [key: number]: string } = {
      1: 'z1',
      2: 'z2',
      3: 'z3',
      4: 'z4',
      5: 'z5'
    };
    return zoneMap[zona] || '';
  }

  getPaceUnit(tipo: string): string {
    if (!tipo) return 'min/km';
    const t = tipo.toLowerCase();
    return (t.includes('bici') || t.includes('ciclismo') || t.includes('bike')) ? 'km/h' : 'min/km';
  }

}
