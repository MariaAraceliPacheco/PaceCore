import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { WorkoutFormComponent } from '../workouts/workout-form/workout-form.component';
import { WorkoutPreviewComponent } from '../workouts/workout-preview/workout-preview.component';
import { WorkoutService } from '../../../services/workout.service';
import { Entreno } from '../../../../models/entreno.model';
import { ActivityIconPipe } from '../../../pipes/activity-icon.pipe';
import { LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { FormatSecondsPipe } from '../../../pipes/format-seconds.pipe';
import { Estadisticas } from '../../../../models/estadisticas.model';
import { RecomendacionComponent } from './recomendacion/recomendacion.component';
import { RitmoPipe } from '../../../pipes/ritmo.pipe';

registerLocaleData(localeEs);

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
  }
}

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [RouterLink, AsyncPipe, CommonModule, MatDialogModule, ActivityIconPipe, WorkoutPreviewComponent, FormatSecondsPipe, RecomendacionComponent, RitmoPipe],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.css',
  providers: [{ provide: LOCALE_ID, useValue: 'es-ES' }]
})
export class OverviewComponent {
  recentWorkouts: Entreno[] = [];

  estadisticas: Estadisticas = {
    distanciaTotalKm: 0,
    desnivelTotal: 0,
    caloriasTotales: 0,
    ritmoMedioRunning: 0,
    velocidadMediaBici: 0,
    tiempoTotal: 0,
    marca5k: '',
    marca10k: '',
    marca21k: '',
    marca42k: ''
  };

  // Propiedades para tendencias
  distanciaTrend = { info: 'vs semana anterior', percent: 0, isUp: true, label: 'Distribución estable' };
  ritmoTrend = { info: 'vs semana anterior', percent: 0, isUp: true, label: 'Ritmo estable' };
  tiempoTrend = { info: 'vs semana anterior', percent: 0, isUp: true, label: 'Tiempo estable' };

  user$!: Observable<LoginResponse | null>;

  constructor(
    private authService: AuthService,
    private dialog: MatDialog,
    private workoutService: WorkoutService
  ) { }

  ngOnInit(): void {
    this.user$ = this.authService.user$;

    // Obtenemos los 3 entrenamientos más recientes y calculamos tendencias
    this.workoutService.entrenos$.subscribe(entrenos => {
      this.recentWorkouts = entrenos.slice(0, 3);
      this.calculateTrends(entrenos);
    });

    if (localStorage.getItem("auth_token")) {
      this.authService.restoreSession();
    }

    this.authService.user$.subscribe(user => {
      if (user) {
        this.workoutService.getEstadisticas(user.usuario.id).subscribe({
          next: (estadisticas) => {
            console.log('Estadisticas recibidas en el componente:', estadisticas);
            this.estadisticas = estadisticas;
          },
          error: (err) => console.error('Error en la suscripción de estadisticas:', err)
        });
      }
    });
  }

  openWorkoutForm() {
    this.dialog.open(WorkoutFormComponent, {
      width: '800px',
      maxWidth: '95vw',
      panelClass: 'custom-dialog-container'
    });
  }

  verDetalle(entreno: Entreno) {
    this.dialog.open(WorkoutPreviewComponent, {
      width: '800px',
      maxWidth: '95vw',
      panelClass: 'custom-dialog-container',
      data: { entreno }
    });
  }

  editarEntreno(id: number) {
    this.workoutService.getEntreno(id).subscribe({
      next: (entreno) => {
        this.dialog.open(WorkoutFormComponent, {
          width: '800px',
          maxWidth: '95vw',
          panelClass: 'custom-dialog-container',
          data: { entreno }
        })
      }
    })
  }

  private calculateTrends(entrenos: Entreno[]) {
    const now = new Date();
    const day = now.getDay();
    const diffToMonday = (day === 0 ? 6 : day - 1);

    const currentWeekStart = new Date(now);
    currentWeekStart.setDate(now.getDate() - diffToMonday);
    currentWeekStart.setHours(0, 0, 0, 0);

    const lastWeekStart = new Date(currentWeekStart);
    lastWeekStart.setDate(currentWeekStart.getDate() - 7);

    const lastWeekEnd = new Date(currentWeekStart);
    lastWeekEnd.setMilliseconds(-1);

    const thisWeekWorkouts = entrenos.filter(e => new Date(e.fecha) >= currentWeekStart);
    const lastWeekWorkouts = entrenos.filter(e => {
      const d = new Date(e.fecha);
      return d >= lastWeekStart && d <= lastWeekEnd;
    });

    // Distancia
    const thisWeekDist = thisWeekWorkouts.reduce((acc, curr) => acc + curr.distancia_entreno, 0);
    const lastWeekDist = lastWeekWorkouts.reduce((acc, curr) => acc + curr.distancia_entreno, 0);
    this.distanciaTrend = this.getTrendData(thisWeekDist, lastWeekDist, 'km');

    // Tiempo (convertir HH:mm:ss a segundos)
    const thisWeekTime = thisWeekWorkouts.reduce((acc, curr) => acc + this.timeToSeconds(curr.tiempo_total_entreno), 0);
    const lastWeekTime = lastWeekWorkouts.reduce((acc, curr) => acc + this.timeToSeconds(curr.tiempo_total_entreno), 0);
    this.tiempoTrend = this.getTrendData(thisWeekTime, lastWeekTime, 'm');

    // Ritmo (solo running para ser coherentes con el stat del dashboard)
    const thisWeekRunning = thisWeekWorkouts.filter(e => e.tipo_actividad_entreno.nombre.toLowerCase().includes('run'));
    const lastWeekRunning = lastWeekWorkouts.filter(e => e.tipo_actividad_entreno.nombre.toLowerCase().includes('run'));

    const getAvgPace = (workouts: Entreno[]) => {
      const dist = workouts.reduce((acc, curr) => acc + curr.distancia_entreno, 0);
      const time = workouts.reduce((acc, curr) => acc + this.timeToSeconds(curr.tiempo_total_entreno), 0);
      return dist > 0 ? time / dist : 0; // segundos por km
    };

    const thisWeekPace = getAvgPace(thisWeekRunning);
    const lastWeekPace = getAvgPace(lastWeekRunning);

    // Para el ritmo, "mejorar" significa que el número es MENOR (más rápido)
    if (lastWeekPace > 0 && thisWeekPace > 0) {
      const diff = lastWeekPace - thisWeekPace; // positivo si es más rápido ahora
      const percent = Math.abs((diff / lastWeekPace) * 100);
      this.ritmoTrend = {
        info: `vs semana anterior`,
        percent: Math.round(percent),
        isUp: diff >= 0, // lo llamamos "isUp" si "mejora" (es más rápido)
        label: `${Math.round(percent)}% ${diff >= 0 ? 'más rápido' : 'más lento'}`
      };
    } else {
      this.ritmoTrend = { info: 'vs semana anterior', percent: 0, isUp: true, label: 'Ritmo estable' };
    }
  }

  private timeToSeconds(timeStr: string): number {
    if (!timeStr) return 0;
    const parts = timeStr.split(':').map(Number);
    if (parts.length === 3) {
      return parts[0] * 3600 + parts[1] * 60 + parts[2];
    } else if (parts.length === 2) {
      return parts[0] * 60 + parts[1];
    }
    return 0;
  }

  private getTrendData(curr: number, prev: number, unit: string) {
    if (prev === 0) {
      return {
        info: 'vs semana anterior',
        percent: curr > 0 ? 100 : 0,
        isUp: true,
        label: curr > 0 ? `+${curr}${unit} esta semana` : 'Sin actividad'
      };
    }
    const diff = curr - prev;
    const percent = Math.abs((diff / prev) * 100);
    return {
      info: 'vs semana anterior',
      percent: Math.round(percent),
      isUp: diff >= 0,
      label: `${Math.round(percent)}% ${diff >= 0 ? 'más' : 'menos'} que la anterior`
    };
  }

  getPaceUnit(tipo: string): string {
    if (!tipo) return 'min/km';
    const t = tipo.toLowerCase();
    return (t.includes('bici') || t.includes('ciclismo') || t.includes('bike')) ? 'km/h' : 'min/km';
  }
}
