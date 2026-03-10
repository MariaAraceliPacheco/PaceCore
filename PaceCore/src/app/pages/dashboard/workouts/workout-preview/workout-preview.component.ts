import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Entreno } from '../../../../../models/entreno.model';
import { ActivityIconPipe } from '../../../../pipes/activity-icon.pipe';
import { WorkoutFormComponent } from '../workout-form/workout-form.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { WorkoutService } from '../../../../services/workout.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import localeEs from '@angular/common/locales/es';
import { registerLocaleData } from '@angular/common';
import { LOCALE_ID } from '@angular/core';
import { RitmoPipe } from '../../../../pipes/ritmo.pipe';

registerLocaleData(localeEs);

@Component({
    selector: 'app-workout-preview',
    standalone: true,
    imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule, ActivityIconPipe, RitmoPipe],
    templateUrl: './workout-preview.component.html',
    styleUrl: './workout-preview.component.css',
    providers: [{ provide: LOCALE_ID, useValue: 'es-ES' }]
})
export class WorkoutPreviewComponent {
    entreno: Entreno;

    constructor(
        public dialogRef: MatDialogRef<WorkoutPreviewComponent>,
        @Inject(MAT_DIALOG_DATA) public data: { entreno: Entreno },
        private dialog: MatDialog,
        private workoutService: WorkoutService,
        private snackBar: MatSnackBar
    ) {
        this.entreno = data.entreno;
        console.log(this.entreno);
    }

    onEdit(): void {
        this.dialogRef.close();
        this.dialog.open(WorkoutFormComponent, {
            width: '800px',
            maxWidth: '95vw',
            panelClass: 'custom-dialog-container',
            data: { entreno: this.entreno }
        });
    }

    onDelete(): void {
        const confirmRef = this.dialog.open(ConfirmDialogComponent, {
            data: {
                title: 'Eliminar Entrenamiento',
                message: '¿Estás seguro de que quieres eliminar este entrenamiento? Esta acción no se puede deshacer.'
            }
        });

        confirmRef.afterClosed().subscribe(result => {
            if (result) {
                this.workoutService.eliminarEntreno(this.entreno.id).subscribe({
                    next: () => {
                        this.workoutService.refreshEntrenos();
                        this.snackBar.open('Entrenamiento eliminado correctamente', 'Cerrar', {
                            duration: 3000,
                        });
                        this.dialogRef.close();
                    },
                    error: (err) => console.error('Error al eliminar:', err)
                });
            }
        });
    }

    onClose(): void {
        this.dialogRef.close();
    }

    formatTime(time: any): string {
        if (!time) return '00:00:00';
        if (typeof time === 'string') return time;

        const h = String(time.hour || 0).padStart(2, '0');
        const m = String(time.minute || 0).padStart(2, '0');
        const s = String(time.second || 0).padStart(2, '0');
        return `${h}:${m}:${s}`;
    }

    getZonaClass(zona: number | undefined): string {
        if (!zona) return '';
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
