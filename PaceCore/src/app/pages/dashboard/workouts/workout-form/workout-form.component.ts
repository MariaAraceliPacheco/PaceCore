import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { WorkoutService } from '../../../../services/workout.service';
import { AuthService } from '../../../../services/auth.service';
import { TipoActividad } from '../../../../../models/tipo-actividad.model';
import { EntrenoCreate } from '../../../../../models/entreno-create.model';
import { IntervalCreate } from '../../../../../models/interval-create.model';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Entreno } from '../../../../../models/entreno.model';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

registerLocaleData(localeEs);
@Component({
    selector: 'app-workout-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatIconModule,
        MatSnackBarModule
    ],
    templateUrl: './workout-form.component.html',
    styleUrl: './workout-form.component.css'
})
export class WorkoutFormComponent implements OnInit {
    workoutForm!: FormGroup;
    activityTypes: TipoActividad[] = [];
    entreno: Entreno | null = null;


    constructor(
        private fb: FormBuilder,
        private workoutService: WorkoutService,
        private authService: AuthService,
        public dialogRef: MatDialogRef<WorkoutFormComponent>,
        private snackBar: MatSnackBar,
        @Inject(MAT_DIALOG_DATA) public data: { entreno: Entreno }
    ) {
        if (data && data.entreno) {
            this.entreno = data.entreno;
        }
    }


    ngOnInit(): void {
        console.log('WorkoutFormComponent inicializando, suscribiéndose a actividades...');
        this.workoutService.actividades$.subscribe({
            next: (actividades) => {
                console.log('Actividades recibidas en el componente:', actividades);
                this.activityTypes = actividades;
            },
            error: (err) => console.error('Error en la suscripción de actividades:', err)
        });
        this.initForm();

        if (this.entreno) {
            console.log('Editando entrenamiento:', this.entreno);
            this.patchFormValues();
        }
    }

    initForm(): void {
        const now = new Date();
        // Ajustamos a la zona horaria local para el input datetime-local (YYYY-MM-DDTHH:mm)
        const localDateTime = new Date(now.getTime() - (now.getTimezoneOffset() * 60000))
            .toISOString()
            .slice(0, 16);

        this.workoutForm = this.fb.group({
            titulo: ['', Validators.required],
            fecha: [localDateTime, Validators.required],
            distancia: [null, [Validators.required, Validators.min(0.1)]],
            tiempo_total: ['', Validators.required],
            tipo_actividad_id: [null, Validators.required],
            desnivel: [null],
            fcMedia: [null],
            fcMaxima: [null],
            descripcion: [''],
            intervalos: this.fb.array([])
        });

        // Suscribirse a cambios en los intervalos para auto-cálculo
        this.intervalos.valueChanges.subscribe(() => {
            this.calculateTotalsFromIntervals();
        });
    }

    get intervalos(): FormArray {
        return this.workoutForm.get('intervalos') as FormArray;
    }

    addInterval(): void {
        const intervalForm = this.fb.group({
            tipo_actividad_id: [null, Validators.required],
            distancia: [null, [Validators.required, Validators.min(0)]],
            duracion: ['', Validators.required],
            desnivel: [null],
            fcMedia: [null],
            fcMaxima: [null]
        });
        this.intervalos.push(intervalForm);
    }

    removeInterval(index: number): void {
        this.intervalos.removeAt(index);
    }

    onSubmit(): void {
        if (this.workoutForm.valid) {
            const formData = this.workoutForm.value;
            const currentUser = this.authService.currentUser;

            if (!currentUser) {
                console.error('No hay usuario autenticado');
                return;
            }

            // Map activities in intervals to IntervalCreate structure
            const mappedIntervalos: IntervalCreate[] = formData.intervalos.map((interval: any) => {
                const intervalData = {
                    tipo_actividad_id: interval.tipo_actividad_id,
                    distancia: interval.distancia,
                    duracion: interval.duracion,
                    desnivel: interval.desnivel,
                    fcMedia: interval.fcMedia,
                    fcMaxima: interval.fcMaxima
                };
                console.log('Mapeando intervalo para envío:', intervalData);
                return intervalData;
            });

            const workoutData: EntrenoCreate = {
                id_usuario: currentUser.usuario.id,
                titulo: formData.titulo,
                fecha: new Date(formData.fecha).toISOString(),
                distancia: formData.distancia,
                tiempo_total: formData.tiempo_total,
                tipo_actividad_id: formData.tipo_actividad_id,
                desnivel: formData.desnivel,
                fcMedia: formData.fcMedia,
                fcMaxima: formData.fcMaxima,
                descripcion: formData.descripcion,
                intervalos: mappedIntervalos,
            };

            console.log('Enviando datos de entrenamiento:', workoutData);

            if (this.entreno) {
                this.workoutService.updateWorkout(this.entreno.id, workoutData);
            } else {
                this.workoutService.addWorkout(workoutData);
                this.snackBar.open('¡Entrenamiento añadido correctamente!', 'Cerrar', {
                    duration: 5000,
                    horizontalPosition: 'center',
                    verticalPosition: 'bottom',
                });
            }

            this.dialogRef.close(true);
        } else {
            console.warn(' Formulario inválido. Revisa los campos marcados en rojo.');
            this.workoutForm.markAllAsTouched();
        }
    }

    onCancel(): void {
        this.dialogRef.close();
    }

    //rellenar los campos del formulario si entreno existe
    private patchFormValues(): void {
        if (!this.entreno) return;

        console.log('DEBUG: Entreno recibido para editar:', this.entreno);

        this.workoutForm.patchValue({
            titulo: this.entreno.titulo,
            fecha: this.entreno.fecha ? this.entreno.fecha.slice(0, 16) : null,
            distancia: this.entreno.distancia_entreno || (this.entreno as any).distancia,
            tiempo_total: this.formatTimeObject(this.entreno.tiempo_total_entreno || (this.entreno as any).tiempoTotal),
            tipo_actividad_id: this.entreno.tipo_actividad_entreno?.id || (this.entreno as any).tipoactividad?.id,
            desnivel: this.entreno.desnivel_entreno !== undefined ? this.entreno.desnivel_entreno : (this.entreno as any).desnivel,
            fcMedia: this.entreno.fcMedia !== undefined ? this.entreno.fcMedia : (this.entreno as any).fc_media_entreno,
            fcMaxima: this.entreno.fcMaxima !== undefined ? this.entreno.fcMaxima : (this.entreno as any).fc_max_entreno,
            descripcion: this.entreno.descripcion
        });

        // Limpiar el FormArray antes de llenarlo por si se llamara varias veces
        while (this.intervalos.length !== 0) {
            this.intervalos.removeAt(0);
        }

        if (this.entreno.intervalos && this.entreno.intervalos.length > 0) {
            console.log('DEBUG: Cargando intervalos del entreno:', this.entreno.intervalos);
            this.entreno.intervalos.forEach((intervalo, index) => {
                console.log(`DEBUG: Intervalo #${index + 1} crudo:`, intervalo);

                // Intentamos obtener el ID de varias formas por si el backend lo devuelve con otro nombre
                const activityId = (intervalo as any).tipo_actividad_id?.id || (intervalo as any).tipo_actividad?.id;

                const intervalForm = this.fb.group({
                    tipo_actividad_id: [activityId, Validators.required],
                    distancia: [intervalo.distancia, [Validators.required, Validators.min(0)]],
                    duracion: [intervalo.duracion, Validators.required],
                    desnivel: [intervalo.desnivel],
                    fcMedia: [intervalo.fcMedia !== undefined ? intervalo.fcMedia : (intervalo as any).fc_media],
                    fcMaxima: [intervalo.fcMaxima !== undefined ? intervalo.fcMaxima : (intervalo as any).fc_maxima]
                });
                this.intervalos.push(intervalForm);
            });
        }

        // Si hay intervalos, recalcular y bloquear campos
        if (this.intervalos.length > 0) {
            this.calculateTotalsFromIntervals();
        }
    }

    private calculateTotalsFromIntervals(): void {
        const intervals = this.intervalos.value;
        if (intervals.length === 0) return;

        let totalDist = 0;
        let totalDesnivel = 0;
        let totalSeconds = 0;

        intervals.forEach((interval: any) => {
            totalDist += (interval.distancia || 0);
            totalDesnivel += (interval.desnivel || 0);
            totalSeconds += this.timeToSeconds(interval.duracion || '00:00:00');
        });

        this.workoutForm.patchValue({
            distancia: parseFloat(totalDist.toFixed(2)),
            desnivel: totalDesnivel,
            tiempo_total: this.secondsToTime(totalSeconds)
        }, { emitEvent: false }); // Evitar bucle infinito de ValueChanges
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

    private secondsToTime(totalSeconds: number): string {
        const hours = Math.floor(totalSeconds / 3600);
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;
        return [hours, minutes, seconds]
            .map(v => v.toString().padStart(2, '0'))
            .join(':');
    }

    private formatTimeObject(time: any): string {
        if (!time) return '00:00:00';
        if (typeof time === 'string') return time;

        const h = String(time.hour || 0).padStart(2, '0');
        const m = String(time.minute || 0).padStart(2, '0');
        const s = String(time.second || 0).padStart(2, '0');
        return `${h}:${m}:${s}`;
    }
}
