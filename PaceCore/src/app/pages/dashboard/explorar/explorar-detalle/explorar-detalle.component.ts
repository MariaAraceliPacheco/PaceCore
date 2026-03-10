import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

export interface ExplorarDetalleData {
    titulo: string;
    subtitulo: string;
    imagen: string;
    contenido: any[];
}

@Component({
    selector: 'app-explorar-detalle',
    standalone: true,
    imports: [CommonModule, MatDialogModule, MatButtonModule],
    templateUrl: './explorar-detalle.component.html',
    styleUrl: './explorar-detalle.component.css'
})
export class ExplorarDetalleComponent {
    constructor(
        public dialogRef: MatDialogRef<ExplorarDetalleComponent>,
        @Inject(MAT_DIALOG_DATA) public data: ExplorarDetalleData
    ) { }

    close(): void {
        this.dialogRef.close();
    }
}
