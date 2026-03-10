import { Component, OnInit } from '@angular/core';
import { RecomendacionesService } from '../../../../services/recomendaciones.service';
import { EntrenamientoSugeridoDTO } from '../../../../../models/recomendaciones/entrenamientoSugeridoDTO.model';
import { AuthService } from '../../../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormatSecondsPipe } from '../../../../pipes/format-seconds.pipe';
import { Estado } from '../../../../services/recomendaciones.service';
import { RitmoPipe } from '../../../../pipes/ritmo.pipe';
import { PdfService } from '../../../../services/pdf.service';

@Component({
  selector: 'app-recomendacion',
  standalone: true,
  imports: [CommonModule, FormatSecondsPipe, RitmoPipe],
  templateUrl: './recomendacion.component.html',
  styleUrl: './recomendacion.component.css'
})
export class RecomendacionComponent implements OnInit {

  recomendacion?: EntrenamientoSugeridoDTO;
  loading = true;
  status: 'pending' | 'accepted' | 'rejected' | 'completed' = 'pending';
  userId?: number;

  constructor(
    private recomendacionesService: RecomendacionesService,
    private authService: AuthService,
    private pdfService: PdfService
  ) { }

  ngOnInit(): void {
    this.authService.user$.subscribe(user => {
      if (user) {
        this.userId = user.usuario.id;
        this.loading = true;
        this.recomendacionesService.getRecomendaciones(this.userId).subscribe({
          next: (recomendacion) => {
            this.recomendacion = recomendacion;
            this.loading = false;
            console.log(this.recomendacion);
          },
          error: (error) => {
            console.error('Error al obtener recomendación:', error);
            this.loading = false;
          }
        });
      }
    });
  }

  aceptar() {
    if (!this.recomendacion) return;
    this.recomendacionesService.actualizarEstadoRecomendacion(this.recomendacion.id, Estado.ACEPTADO).subscribe({
      next: () => {
        this.status = 'accepted';
        this.recargarRecomendacion();
      },

      error: (err) => console.log("Error al aceptar la recomendacion")
    })
  }

  rechazar() {
    if (!this.recomendacion) return;
    this.recomendacionesService.actualizarEstadoRecomendacion(this.recomendacion.id, Estado.RECHAZADO).subscribe({
      next: () => {
        this.status = 'rejected'
        this.recargarRecomendacion();
      },
      error: (err) => console.log("Error al rechazar la recomendacion")
    })
  }

  completar() {
    if (!this.recomendacion) return;
    this.recomendacionesService.actualizarEstadoRecomendacion(this.recomendacion.id, Estado.COMPLETADO).subscribe({
      next: () => {
        this.status = 'completed'
        this.recargarRecomendacion();
      },
      error: (err) => console.log("Error al completar la recomendacion")
    })
  }

  descargarPDF() {
    if (!this.recomendacion) return;
    this.pdfService.exportarEntrenamiento(this.recomendacion);
  }

  recargarRecomendacion() {
    this.recomendacionesService.getRecomendaciones(this.userId).subscribe({
      next: (recomendacion) => {
        this.recomendacion = recomendacion;
        this.loading = false;
        console.log(this.recomendacion);
      },
      error: (error) => {
        console.error('Error al obtener recomendación:', error);
        this.loading = false;
      }
    });
  }

  get targetZoneColor(): string {
    return this.intensityColor;
  }

  get maxZone(): number {
    if (!this.recomendacion || !this.recomendacion.bloques.length) return 1;
    //se obtiene la zona maxima que se alcanzará en este entrenamiento
    return Math.max(...this.recomendacion.bloques.map(b => b.zonaObjetivo));
  }

  get intensityColor(): string {
    const zoneColors: { [key: number]: string } = {
      1: '#328bdf',
      2: '#30d31a',
      3: '#ffd900',
      4: '#ff7e56',
      5: '#ff0000'
    };
    return zoneColors[this.maxZone] || '#fc4c02';
  }
}
